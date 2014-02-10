#ifndef Z_TRONPLAYER_H_
#define Z_TRONPLAYER_H_

#include "tronol.h"
#include "TronServer.h"

class TronRoom;
class TronPlayer
{
public:
    TronPlayer(TronServer& server,boost::asio::ip::tcp::socket& socket);
	~TronPlayer();

	boost::asio::ip::tcp::socket& socket();

	void connect();
	void disconnect();

    void set_logged_in(bool);
    bool is_logged_in();

    void set_in_game(bool);
    bool is_in_game();

    void set_name(std::string name){name_=name;}
    std::string name(){return name_;}

    TronRoom* room(){return room_;}
    void set_room(TronRoom* room){room_ = room;}


private:
    TronServer& server_;
	boost::asio::ip::tcp::socket& socket_;

    bool is_logged_in_;
    bool is_in_game_;
    std::string name_;

    TronRoom* room_;
};

#endif
