package server

import (
	"errors"
	"fmt"
	"log"
	"math/rand"
	"net"
	"sync"
)

// Server struct
type Server struct {
	players    []*Player
	playerLock sync.Mutex
	rooms      []*Room
	roomMutex  sync.Mutex
}

// NewServer new
func NewServer() (*Server, error) {
	s := &Server{}

	return s, nil
}

// Run server
func (s *Server) Run() error {
	l, err := net.Listen("tcp4", "0.0.0.0:10001")
	if err != nil {
		log.Printf("listen error\n")
		return err
	}
	defer l.Close()

	for {
		c, err := l.Accept()
		if err != nil {
			log.Printf("accept error\n")
			return err
		}
		s.onPlayerConnect(c)
	}
}

func (s *Server) onPlayerConnect(c net.Conn) {
	player := &Player{
		server: s,
		conn:   c,
	}
	s.players = append(s.players, player)
	go func() {
		err := player.Run()
		log.Printf("player disconnected: %v\n", err)
		if player.room != nil {
			s.LeaveRoom(player, player.room.id)
		}
		for i, p := range s.players {
			if p == player {
				s.players = append(s.players[0:i], s.players[i+1:]...)
				break
			}
		}
	}()
}

func (s *Server) getRoomByID(roomID int32) (int, error) {
	for i, r := range s.rooms {
		if r.id == roomID {
			return i, nil
		}
	}
	return -1, fmt.Errorf("No room found %v", roomID)
}

// NewRoom func
func (s *Server) NewRoom(p *Player, name string) *Room {
	room := &Room{
		id:     rand.Int31(),
		name:   name,
		host:   p,
		status: "open",
	}
	s.rooms = append(s.rooms, room)
	s.JoinRoom(p, room.id)
	return room
}

// JoinRoom func
func (s *Server) JoinRoom(p *Player, roomID int32) error {
	if p.room != nil {
		if p.room.id == roomID {
			return nil
		}
		s.LeaveRoom(p, p.room.id)
	}
	s.roomMutex.Lock()
	defer s.roomMutex.Unlock()
	i, err := s.getRoomByID(roomID)
	if err != nil {
		return err
	}
	p.room = s.rooms[i]
	s.rooms[i].players = append(s.rooms[i].players, p)
	return nil
}

// LeaveRoom func
func (s *Server) LeaveRoom(p *Player, roomID int32) error {
	s.roomMutex.Lock()
	defer s.roomMutex.Unlock()
	i, err := s.getRoomByID(roomID)
	if err != nil {
		return err
	}
	if p.room == nil {
		return errors.New("user not in any room")
	}
	if p.room.id != s.rooms[i].id {
		return errors.New("wrong room")
	}
	if err := s.rooms[i].Leave(p); err != nil {
		return err
	}
	p.room = nil
	if len(s.rooms[i].players) == 0 {
		// delete room
		s.rooms = append(s.rooms[0:i], s.rooms[i+1:]...)
	}
	return nil
}
