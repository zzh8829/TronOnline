#include "TronGame.h"
#include "TronRoom.h"
using namespace std;

TronGame::TronGame(TronServer &server)
    :server_(server)
{

}

TronGame::~TronGame()
{

}

void TronGame::reset()
{
    memset(grid,0,sizeof(grid));
    moves.clear();
    tick = 0;
}

void TronGame::start(int n)
{
    players_.clear();
    dirs.clear();
    for(int i=0;i!=n;i++)
    {
        players_.push_back(State());
        players_[i].status=0;
        dirs.push_back(deque<int>());
    }
    if(n==2) {

        players_[0].x = 25;
        players_[0].y = 50;
        players_[0].dx = 1;
        players_[0].dy = 0;

        players_[1].x = 75;
        players_[1].y = 50;
        players_[1].dx = -1;
        players_[1].dy = 0;

        moves.push_back(vector<Move>());
        moves[0].push_back(Move(25,50,1));
        moves[0].push_back(Move(75,50,2));
    }
}

static int newg[101][101];

int TronGame::update()
{
    lock.lock();
    for(int i=0;i!=players_.size();i++)
        while(!dirs[i].empty()) {
            int d = dirs[i].front();
            dirs[i].pop_front();
            if(set_dir(i,d))
                break;
        }
    set<int> gord;
    memset(newg,0,sizeof(newg));
    for(int i=0;i!=players_.size();i++)
    {
        if(players_[i].status!=-1)
        {
            int tx = players_[i].x + players_[i].dx;
            int ty = players_[i].y + players_[i].dy;
            if(tx < 0 || tx >= 100 || ty < 0 || ty >=100)
                continue;
            if(grid[ty][tx]==0)
            {
                if(newg[ty][tx]==0)
                {
                    newg[ty][tx]=i+1;
                    gord.insert(i+1);
                } else {
                    gord.erase(newg[ty][tx]);
                }
            }
        }
    }
    for(int i=0;i!=players_.size();i++)
        if(!gord.count(i+1))
            players_[i].status=-1;
    tick++;
    moves.push_back(vector<Move>());
    for(auto it = gord.begin();it!=gord.end();it++)
    {
        State& s = players_[*it-1];
        s.x += s.dx;
        s.y += s.dy;
        grid[s.y][s.x] = *it;
        moves[tick].push_back(Move(s.x,s.y,*it));
    }
    lock.unlock();
    if(gord.size()==0)
        return -1;
    if(gord.size()==1)
        return *gord.begin();
    return 0;
}

static int dx[] = {0,1,0,-1};
static int dy[] = {-1,0,1,0};

bool TronGame::set_dir(int pid,int dir)
{

    int tx = dx[dir];
    int ty = dy[dir];
    if(tx!=0) {
        if(players_[pid].dx==0)
        {
            players_[pid].dy=0;
            players_[pid].dx=tx;
            return true;
        }
    } else {
        if(players_[pid].dy==0)
        {
            players_[pid].dx=0;
            players_[pid].dy=ty;
            return true;
        }
    }

    return false;
}

void TronGame::add_dir(int pid,int dir)
{
    lock.lock();
    dirs[pid].push_back(dir);
    lock.unlock();
}
