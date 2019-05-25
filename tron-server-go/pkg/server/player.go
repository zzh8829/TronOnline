package server

import (
	"encoding/binary"
	"net"

	"github.com/golang/protobuf/proto"
	log "github.com/sirupsen/logrus"
	pb "github.com/zzh8829/tron-server-go/pkg/protobuf"
)

// Player struct
type Player struct {
	server *Server
	conn   net.Conn

	name string
	room *Room
}

// Run player
func (p *Player) Run() error {
	log.Printf("Player Connected %s\n", p.conn.RemoteAddr().String())
	defer p.conn.Close()

	for {
		var msgLen int32
		if err := binary.Read(p.conn, binary.BigEndian, &msgLen); err != nil {
			log.Printf("read len error\n")
			return err
		}

		buffer := make([]byte, msgLen)
		n, err := p.conn.Read(buffer)
		if n != int(msgLen) || err != nil {
			log.Printf("read error\n")
			return err
		}

		cmd := &pb.Command{}
		if err := proto.Unmarshal(buffer, cmd); err != nil {
			log.Printf("Unmarshal error\n")
			return err
		}

		var resp *pb.Response
		switch cmd.GetType() {
		case pb.Command_ACCOUNT:
			log.Debugf("Account: %v\n", cmd.GetAccount())
			resp, err = p.handleAccount(cmd.GetAccount())
		case pb.Command_ROOM:
			log.Debugf("Room: %v\n", cmd.GetAccount())
			resp, err = p.handleRoom(cmd.GetRoom())
		case pb.Command_GAME:
			log.Debugf("Game: %v\n", cmd.GetAccount())
			resp, err = p.handleGame(cmd.GetGame())
		}

		log.Debugf("Resp: %v\n", resp)
		data, err := proto.Marshal(resp)
		if err != nil {
			log.Printf("Marshal error\n")
			return err
		}
		if err := binary.Write(p.conn, binary.BigEndian, int32(len(data))); err != nil {
			log.Printf("write len error\n")
			return err
		}
		n, err = p.conn.Write(data)
		if n != len(data) || err != nil {
			log.Printf("write error\n")
			return err
		}
	}
}

func (p *Player) handleAccount(cmd *pb.AccountCommand) (*pb.Response, error) {
	resp := &pb.AccountResponse{
		Type: pb.AccountResponse_SUCCESS,
	}

	switch cmd.GetType() {
	case pb.AccountCommand_REGISTER:
		if err := Register(cmd.GetUsername(), cmd.GetPassword()); err != nil {
			log.Printf("Register error")
			resp.Type = pb.AccountResponse_INVALID_USERNAME
		}
	case pb.AccountCommand_LOGIN:
		for _, sp := range p.server.players {
			if sp.name == cmd.GetUsername() {
				log.Printf("Login error active")
				resp.Type = pb.AccountResponse_INVALID_USERNAME
				goto AccountEnd
			}
		}
		if err := Login(cmd.GetUsername(), cmd.GetPassword()); err != nil {
			log.Printf("Login error")
			resp.Type = pb.AccountResponse_INVALID_PASSWORD
		}
		p.name = cmd.GetUsername()
	}

AccountEnd:
	return &pb.Response{
		Type:    pb.Response_ACCOUNT,
		Account: resp,
	}, nil
}

func (p *Player) handleRoom(cmd *pb.RoomCommand) (*pb.Response, error) {
	resp := &pb.RoomResponse{
		Type: pb.RoomResponse_SUCCESS,
	}
	switch cmd.GetType() {
	case pb.RoomCommand_LIST:
		for _, r := range p.server.rooms {
			resp.Room = append(resp.Room, r.ToProto())
		}
	case pb.RoomCommand_CREATE:
		room := p.server.NewRoom(p, cmd.GetName())
		resp.Id = room.id
	case pb.RoomCommand_ENTER:
		if err := p.server.JoinRoom(p, cmd.GetId()); err != nil {
			log.Printf("join error %v", err)
			resp.Type = pb.RoomResponse_INVALID_ROOM
		}
	case pb.RoomCommand_LEAVE:
		if err := p.server.LeaveRoom(p, cmd.GetId()); err != nil {
			log.Printf("leave error %v", err)
			resp.Type = pb.RoomResponse_INVALID_ROOM
		}
	}

	return &pb.Response{
		Type: pb.Response_ROOM,
		Room: resp,
	}, nil
}

func (p *Player) handleGame(cmd *pb.GameCommand) (*pb.Response, error) {
	resp := &pb.GameResponse{
		Type: pb.GameResponse_SUCCESS,
	}

	switch cmd.GetType() {
	case pb.GameCommand_ROOM:
		for _, p := range p.room.players {
			resp.Users = append(resp.Users, p.name)
		}
		resp.Type = pb.GameResponse_ROOM
		if p.room.status == "game" {
			resp.Type = pb.GameResponse_GAME
		}
	case pb.GameCommand_START:
		if err := p.room.StartGame(); err != nil {
			log.Printf("start game error %v", err)
			resp.Type = pb.GameResponse_ERROR
		}
	case pb.GameCommand_GAME:
		p.room.game.HandlePlayerInput(p, cmd.GetDir())
		updates := p.room.game.GetPlayerUpdate(p, cmd.GetTick())
		if len(updates) != 0 || p.room.game.winner == "" {
			resp.Type = pb.GameResponse_GAME
			resp.SetAt = updates
			resp.Tick = p.room.game.tick
		} else {
			resp.Type = pb.GameResponse_OVER
			resp.Winner = p.room.game.winner
		}
	}

	log.Infof("gr %v\n", resp)

	return &pb.Response{
		Type: pb.Response_GAME,
		Game: resp,
	}, nil
}
