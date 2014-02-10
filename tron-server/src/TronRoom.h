#ifndef Z_TRONROOM_H_
#define Z_TRONROOM_H_

#include "tronol.h"
#include "TronServer.h"
#include "TronGame.h"

class TronPlayer;
class TronRoom;

class TronRoom
{
public:
    TronRoom(TronServer& server,int id);
	~TronRoom();

    static const int MAX_PLAYER = 2;

    int id() const {return id_;}

    void clear();
    void start_game();
    void update_game();
    void stop_game();

    void leave(TronPlayer* player);

    boost::posix_time::ptime idle_time;

    std::string name;
    std::string host;
    std::string status;
    std::vector<TronPlayer*> players;

    std::string winner;

private:
	TronServer& server_;

public:
    TronGame game_;

    int id_;

    boost::mutex lock;
};


#endif
