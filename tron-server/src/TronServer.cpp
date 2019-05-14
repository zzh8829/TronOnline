#include "TronServer.h"
#include "TronPlayer.h"
#include "TronRoom.h"
#include "TronGame.h"

namespace pb = tronol::protobuf;
namespace asio = boost::asio;
using boost::asio::ip::tcp;
using namespace std;

static string sql_query_account = "SELECT * FROM Accounts WHERE username=%s";
static string sql_insert_account = "INSERT INTO Accounts (username,password) VALUES (%s,%s)";
static string sql_update_account = "";
static string sql_delete_account = "";

TronServer::TronServer(int port)
    : port_(port), acceptor_(io_service_, tcp::endpoint(tcp::v4(), port_))
{
    cout << "Server: Start 0.0.0.0:" << port << endl;
}

TronServer::~TronServer()
{
    database_.close();
}

void TronServer::initialize()
{
    cout << "Server: Initializing" << endl;
    database_.open("tron.db");
    for (int i = 0; i != MAX_ROOM; i++)
        room_pool_.push(new TronRoom(*this, i));
}

void TronServer::run()
{
    cout << "Server: Running" << endl;
    boost::thread update_thread(boost::bind(&TronServer::update, this));
    while (true)
    {
        tcp::socket *socket = new tcp::socket(io_service_);
        acceptor_.accept(*socket);
        cout << "Server: Player Connected" << endl;
        TronPlayer *player = new TronPlayer(*this, *socket);
        boost::thread player_thread(boost::bind(&TronPlayer::connect, player));
    }
}
void TronServer::handle_account(TronPlayer *player, const pb::AccountCommand &cmd)
{
    try
    {
        account_mutex_.lock();
        cout << "Incoming Account Commnad" << endl;
        string username, password;
        pb::AccountResponse acc;
        pb::AccountResponse::ResponseType type = pb::AccountResponse::SUCCESS;

        vector<TronSqlite3::Row> rows;
        switch (cmd.type())
        {
        case pb::AccountCommand::REGISTER:
            username = cmd.username();
            password = cmd.password();
            cout << "Register: " << username << "," << password << endl;
            if (database_.query(sql_query_account, &username).size() == 0)
            {
                database_.query(sql_insert_account, &username, &password);
            }
            else
            {
                type = pb::AccountResponse::INVALID_USERNAME;
            }

            break;
        case pb::AccountCommand::LOGIN:
            username = cmd.username();
            password = cmd.password();
            cout << "Login: " << username << endl;

            rows = database_.query(sql_query_account, &username);
            if (rows.size() == 1)
            {
                if (rows[0]["password"] == password)
                {
                    player->set_logged_in(true);
                    player->set_name(username);
                }
                else
                {
                    type = pb::AccountResponse::INVALID_PASSWORD;
                }
            }
            else
            {
                type = pb::AccountResponse::INVALID_USERNAME;
            }
            break;
        case pb::AccountCommand::EDIT:
            break;
        }
        acc.set_type(type);
        pb::Response resp;
        string data;
        resp.set_type(pb::Response::ACCOUNT);
        resp.set_allocated_account(&acc);
        resp.SerializeToString(&data);
        WriteData(player->socket(), data);
        resp.release_account();
        account_mutex_.unlock();
    }
    catch (exception &e)
    {
        throw e;
    }
}

void TronServer::handle_room(TronPlayer *player, const pb::RoomCommand &cmd)
{
    try
    {

        string name;
        int id;
        pb::RoomResponse roomresp;
        pb::RoomResponse::ResponseType type = pb::RoomResponse::SUCCESS;

        TronRoom *room;
        room_mutex_.lock();
        switch (cmd.type())
        {
        case pb::RoomCommand::CREATE:
            cout << "Create Room: " << cmd.name() << endl;
            name = cmd.name();
            if (room_pool_.empty())
            {
                type = pb::RoomResponse::INVALID_ROOM;
                break;
            }
            room = room_pool_.top();
            room_pool_.pop();
            rooms_[room->id()] = room;
            room->clear();
            room->name = name;
            room->status = "open";
            roomresp.set_id(room->id());
            break;
        case pb::RoomCommand::LIST:
            cout << "List Room: " << endl;
            for (auto it = rooms_.begin(); it != rooms_.end(); it++)
            {
                pb::RoomResponse::RoomInfo *info = roomresp.add_room();
                info->set_id(it->first);
                info->set_name(it->second->name);
                info->set_host(it->second->host);
                info->set_status(it->second->status);
            }
            cout << "Rooms: " << roomresp.room_size() << endl;
            break;
        case pb::RoomCommand::ENTER:
            cout << "Enter Room: " << cmd.id() << endl;
            id = cmd.id();
            if (rooms_.count(id) > 0 && rooms_[id]->status == "open")
            {
                player->set_room(rooms_[id]);
                rooms_[id]->players.push_back(player);
            }
            else
            {
                type = pb::RoomResponse::INVALID_ROOM;
            }
            break;
        case pb::RoomCommand::LEAVE:
            id = cmd.id();
            if (rooms_.count(id) > 0)
            {
                rooms_[id]->leave(player);
            }
            else
            {
                type = pb::RoomResponse::INVALID_ROOM;
            }
            break;
        }
        room_mutex_.unlock();
        roomresp.set_type(type);
        pb::Response resp;
        string data;
        resp.set_type(pb::Response::ROOM);
        resp.set_allocated_room(&roomresp);
        resp.SerializeToString(&data);
        WriteData(player->socket(), data);
        resp.release_room();
    }
    catch (exception &e)
    {
        throw e;
    }
}

void TronServer::handle_game(TronPlayer *player, const pb::GameCommand &cmd)
{
    try
    {
        string name;
        int id;
        int tick;
        TronGame *game = &player->room()->game_;
        pb::GameResponse gameresp;
        pb::GameResponse::ResponseType type = pb::GameResponse::SUCCESS;

        switch (cmd.type())
        {
        case pb::GameCommand::ROOM:
            for (int i = 0; i != player->room()->players.size(); i++)
            {
                string *user = gameresp.add_users();
                *user = player->room()->players[i]->name();
            }
            if (player->room()->status == "game")
                type = pb::GameResponse::GAME;
            else
                type = pb::GameResponse::ROOM;
            break;
        case pb::GameCommand::GAME:
            if (player->room()->status != "game")
            {
                gameresp.set_winner(player->room()->winner);
                type = pb::GameResponse::OVER;
                break;
            }
            tick = cmd.tick();
            for (int d : cmd.dir())
            {
                cout << "Dir: " << d << endl;
                game->add_dir(find(
                                  player->room()->players.begin(),
                                  player->room()->players.end(),
                                  player) -
                                  player->room()->players.begin(),
                              d);
            }
            for (int i = tick + 1; i <= game->tick; i++)
            {
                for (int j = 0; j != game->moves[i].size(); j++)
                {
                    pb::GameResponse::GameSet *gs = gameresp.add_set_at();
                    gs->set_x(game->moves[i][j].x);
                    gs->set_y(game->moves[i][j].y);
                    gs->set_v(game->moves[i][j].v);
                }
            }
            gameresp.set_tick(game->tick);
            type = pb::GameResponse::GAME;
            break;
        case pb::GameCommand::START:
            if (player->room()->status != "game" && player->room()->players.size() > 1)
            {
                player->room()->status = "game";
                player->room()->start_game();
            }
            else
            {
                type = pb::GameResponse::ERROR;
            }
            break;
        case pb::GameCommand::QUIT:
            if (player->room()->status == "game")
            {
                player->room()->stop_game();
            }
            else
            {
                type = pb::GameResponse::ERROR;
            }
            break;
        }

        gameresp.set_type(type);
        pb::Response resp;
        string data;
        resp.set_type(pb::Response::GAME);
        resp.set_allocated_game(&gameresp);
        resp.SerializeToString(&data);
        WriteData(player->socket(), data);
        resp.release_game();
    }
    catch (exception &e)
    {
        throw e;
    }
}

void TronServer::update()
{
    boost::posix_time::time_duration MS_PER_TICK =
        boost::posix_time::microseconds(100000);
    boost::posix_time::ptime zero = boost::posix_time::microsec_clock::local_time();

    while (true)
    {
        boost::posix_time::ptime last = boost::posix_time::microsec_clock::local_time();
        room_mutex_.lock();
        for (auto it = rooms_.begin(); it != rooms_.end();)
        {
            TronRoom *room = it->second;
            if (room->status == "game")
            {
                room->update_game();
            }
            if (room->players.size() == 0)
            {
                if (room->idle_time == zero)
                {
                    room->idle_time = boost::posix_time::microsec_clock::local_time();
                }
                else
                {
                    if (boost::posix_time::microsec_clock::local_time() - room->idle_time > boost::posix_time::seconds(1))
                    {
                        room->clear();
                        room_pool_.push(room);
                        cout << "Erase room: " << it->first << endl;
                        it = rooms_.erase(it);
                        continue;
                    }
                }
            }
            else
            {
                if (room->status != "game")
                {
                    if (room->players.size() >= room->MAX_PLAYER)
                        room->status = "full";
                    else
                        room->status = "open";
                }
                room->host = room->players[0]->name();
                room->idle_time = zero;
            }
            it++;
        }
        room_mutex_.unlock();
        boost::posix_time::ptime now = boost::posix_time::microsec_clock::local_time();
        if (now - last < MS_PER_TICK)
        {
            boost::this_thread::sleep(MS_PER_TICK - (now - last));
        }
    }
}
