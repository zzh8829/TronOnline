package server

import (
	"errors"

	log "github.com/sirupsen/logrus"
	pb "github.com/zzh8829/tron-server-go/pkg/protobuf"
)

// Room struct
type Room struct {
	id     int32
	name   string
	host   *Player
	status string

	players []*Player
	game    *Game
}

// StartGame func
func (r *Room) StartGame() error {
	if len(r.players) <= 1 {
		return errors.New("not enough players")
	}
	g, err := NewGame(r)
	if err != nil {
		return err
	}
	r.game = g
	r.status = "game"
	go func() {
		r.game.Run()
		log.Infof("game stop\n")
		r.status = "open"
	}()
	return nil
}

// Leave func
func (r *Room) Leave(p *Player) error {
	for i, rp := range r.players {
		if p == rp {
			r.players = append(r.players[0:i], r.players[i+1:]...)
			if r.host == p && len(r.players) != 0 {
				// give next player host
				r.host = r.players[0]
			}
			return nil
		}
	}
	return errors.New("user not in room")
}

// ToProto func
func (r *Room) ToProto() *pb.RoomResponse_RoomInfo {
	return &pb.RoomResponse_RoomInfo{
		Id:     r.id,
		Name:   r.name,
		Host:   r.host.name,
		Status: r.status,
	}
}
