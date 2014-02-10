#include "TronRoom.h"
#include "TronPlayer.h"
using namespace std;

TronRoom::TronRoom(TronServer &server,int id)
    :server_(server),game_(server),id_(id)
{
    clear();
}


TronRoom::~TronRoom()
{

}

void TronRoom::clear()
{
    this->host = "";
    this->idle_time = boost::posix_time::microsec_clock::local_time();
    this->name = "";
    this->status = "";
    this->players.clear();
    this->winner = "";
}

void TronRoom::start_game()
{
    game_.reset();
    game_.start(players.size());
}

void TronRoom::update_game()
{
    int res = game_.update();
    if(res)
    {
        status = "open";
        if(res==-1)
            winner = "tie";
        else
            winner = players[res-1]->name();
    }
}

void TronRoom::stop_game()
{

}

void TronRoom::leave(TronPlayer* player)
{
    player->set_room(nullptr);
    auto it = find(players.begin(),players.end(),player);
    if(it!=players.end())
        players.erase(it);
    if(status == "game")
    {
        status = "open";
    }
}

