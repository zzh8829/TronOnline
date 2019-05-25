package server

import (
	"time"

	log "github.com/sirupsen/logrus"
	pb "github.com/zzh8829/tron-server-go/pkg/protobuf"
)

var dx = []int32{0, 1, 0, -1}
var dy = []int32{-1, 0, 1, 0}

type pstate struct {
	id  int32
	x   int32
	y   int32
	dir int32

	alive bool

	inputs  []int32
	updates []*pb.GameResponse_GameSet
}

// Game struct
type Game struct {
	tick     int32
	winner   string
	states   map[string]*pstate
	grid     [100][100]int32
	tickRate int
	room     *Room
	quit     chan bool
}

// NewGame func
func NewGame(room *Room) (*Game, error) {
	g := &Game{
		tickRate: 20,
		room:     room,
		states:   make(map[string]*pstate),
		quit:     make(chan bool, 1),
	}

	if len(room.players) == 2 {
		g.states[room.players[0].name] = &pstate{
			id:    1,
			x:     25,
			y:     50,
			dir:   1,
			alive: true,
		}
		g.states[room.players[1].name] = &pstate{
			id:    2,
			x:     75,
			y:     50,
			dir:   3,
			alive: true,
		}
	}

	for _, st := range g.states {
		g.setGrid(st.x, st.y, st.id)
	}
	return g, nil
}

// Run func
func (g *Game) Run() {
	lastTime := time.Now().UnixNano()
	ticker := time.NewTicker(time.Duration(1000.0/g.tickRate) * time.Millisecond)
	defer ticker.Stop()
	for {
		select {
		case <-ticker.C:
			curTime := time.Now().UnixNano()
			delta := float64(curTime-lastTime) / 1e9
			lastTime = curTime
			g.update(delta)
			log.Infof("loop wtf\n")
		case <-g.quit:
			log.Infof("loop stop\n")
			return
		}
	}
}

func (g *Game) update(delta float64) {
	delta = 1
	if g.winner != "" {
		return
	}

	// player input
	for _, st := range g.states {
		if !st.alive {
			continue
		}
		if len(st.inputs) != 0 {
			input := st.inputs[0]
			st.inputs = st.inputs[1:]

			if st.dir%2 != input%2 {
				st.dir = input
			}
		}
	}

	// player movement
	aliveCount := 0
	for _, st := range g.states {
		if !st.alive {
			continue
		}

		nx, ny := st.x+dx[st.dir], st.y+dy[st.dir]
		if nx >= 0 && nx < 100 && ny >= 0 && ny < 100 && g.grid[nx][ny] == 0 {
			g.setGrid(nx, ny, st.id)
			st.x = nx
			st.y = ny
			aliveCount++
		} else {
			log.Printf("%v died %v %v %v", st.id, st.x, st.y, st.dir)
			st.alive = false
		}
	}

	// game result
	if aliveCount == 0 {
		g.winner = "tie"
	}
	if aliveCount == 1 {
		for name, st := range g.states {
			if st.alive {
				g.winner = name
				g.quit <- true
				break
			}
		}
	}
}

func (g *Game) setGrid(x int32, y int32, v int32) {
	g.grid[x][y] = v
	for _, st := range g.states {
		st.updates = append(st.updates, &pb.GameResponse_GameSet{
			X: x, Y: y, V: v,
		})
	}
}

// HandlePlayerInput func
func (g *Game) HandlePlayerInput(p *Player, inputs []int32) {
	g.states[p.name].inputs = append(g.states[p.name].inputs, inputs...)
}

// GetPlayerUpdate func
func (g *Game) GetPlayerUpdate(p *Player, tick int32) []*pb.GameResponse_GameSet {
	u := g.states[p.name].updates
	g.states[p.name].updates = nil
	return u
}
