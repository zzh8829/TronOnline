#ifndef Z_TRONGAME_H_
#define Z_TRONGAME_H_

#include "tronol.h"
#include "TronServer.h"

class TronGame
{
public:
    struct State{
        int x,y;
        int dx,dy;
        int status;
    };
    struct Move{
        Move(int x_=0,int y_=0,int v_=0):
            x(x_),y(y_),v(v_){}
        int x,y,v;
    };

	TronGame(TronServer& server);
	~TronGame();

    void reset();
    void start(int n);
    int update();
    bool set_dir(int pid,int dir);
    void add_dir(int pid,int dir);

    std::vector< std::vector<Move> > moves;
    std::vector< std::deque<int> > dirs;
    int tick;


private:

    TronServer& server_;
    std::vector<State> players_;
    int grid[100][100];

public:
    boost::mutex lock;
};

#endif
