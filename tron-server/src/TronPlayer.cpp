#include "TronPlayer.h"
#include "TronRoom.h"
namespace pb = tronol::protobuf;
using boost::asio::ip::tcp;
using namespace std;


TronPlayer::TronPlayer(TronServer& server, tcp::socket& socket)
    :server_(server),socket_(socket),room_(nullptr)
{
}

TronPlayer::~TronPlayer()
{
}

tcp::socket& TronPlayer::socket()
{
    return socket_;
}

void TronPlayer::set_logged_in(bool opt)
{
    is_logged_in_ = opt;
}

void TronPlayer::set_in_game(bool opt)
{
    is_in_game_ = opt;
}

bool TronPlayer::is_logged_in()
{
    return is_logged_in_;
}



void TronPlayer::connect()
{
    cout << "Player: Connected" << endl;
    try {
    while(true)
	{
        string data = ReadData(socket_);
		pb::Command cmd;
		cmd.ParseFromString(data);
        switch(cmd.type())
        {
        case pb::Command::ACCOUNT:
            server_.handle_account(this,cmd.account());
            break;
        case pb::Command::ROOM:
            server_.handle_room(this,cmd.room());
            break;
        case pb::Command::GAME:
            server_.handle_game(this,cmd.game());
        default:
            break;
        }
	}
    } catch (exception& e) {
        cerr << "Player Exception: " << e.what() << endl;
    }
    disconnect();
}

void TronPlayer::disconnect()
{
    cout << "Player: Disconnected" << endl;
	socket_.shutdown(tcp::socket::shutdown_both);
	socket_.close();

    server_.room_mutex_.lock();
    if(room_)
        room_->leave(this);
    server_.room_mutex_.unlock();

	delete this;
}
