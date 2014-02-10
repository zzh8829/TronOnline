#ifndef Z_TRONSERVER_H_
#define Z_TRONSERVER_H_

#include "tronol.h"
#include "TronUtil.h"

class TronPlayer;
class TronRoom;
class TronGame;

class TronServer
{
public:

    const int MAX_ROOM = 50;

	TronServer(int port);
	~TronServer();

    void initialize();
	void run();
    void update();

    void handle_account(TronPlayer* player, const tronol::protobuf::AccountCommand& cmd);
    void handle_room(TronPlayer* player, const tronol::protobuf::RoomCommand& cmd);
    void handle_game(TronPlayer* player, const tronol::protobuf::GameCommand& cmd);
	
private:
    TronSqlite3 database_;

    std::vector<TronPlayer*> players_;
    std::map<int,TronRoom*> rooms_;
    std::priority_queue<TronRoom*,std::vector<TronRoom*>,TronRoomCompare> room_pool_;
    std::vector<TronGame*> games_;

    int port_;
    boost::asio::io_service io_service_;
    boost::asio::ip::tcp::acceptor acceptor_;

public:
    boost::mutex room_mutex_;
    boost::mutex account_mutex_;
};

#endif
