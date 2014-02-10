#ifndef Z_TRONUTIL_H_
#define Z_TRONUTIL_H_

#include "tronol.h"

class TronRoom;

std::string ReadData(boost::asio::ip::tcp::socket& socket);
void WriteData(boost::asio::ip::tcp::socket& socket, std::string& data);

class TronSqlite3
{
public:
    typedef std::map<std::string,std::string> Row;
    TronSqlite3(){};
    ~TronSqlite3(){};
    void open(std::string database)
    {
        sqlite3_open(database.c_str(),&database_);
    }
    void close()
    {
        if(database_) sqlite3_close(database_);
    }
    std::vector<Row> query(std::string format,...);
private:
    static int callback(void* data,int argc,char ** argv,char **names)
    {
        std::vector<Row>* pvec = (std::vector<Row>*)data;
        Row row;
        for(int i=0;i!=argc;i++)
            row[names[i]] = argv[i]?argv[i]:"";
        pvec->push_back(row);
        return 0;
    }
    sqlite3* database_;
};

struct TronRoomCompare
{
    bool operator()(TronRoom* a,TronRoom* b);
};


#endif
