#include "TronUtil.h"
#include "TronRoom.h"

using boost::asio::ip::tcp;
using namespace std;
namespace asio = boost::asio;


string ReadData(tcp::socket& socket)
{
    try {
        char buffer[4];
        string data;
        asio::read(socket,asio::buffer(buffer,4));
        data.resize(ntohl(reinterpret_cast<int32_t&>(buffer)));
        asio::read(socket,asio::buffer(&data[0],data.size()));
        return data;
    } catch (...) {
        throw;
    }
}

void WriteData(tcp::socket& socket,string& data)
{
    try {
        int len = htonl(data.size());
        char* buffer = reinterpret_cast<char*>(&len);
        asio::write(socket,asio::buffer(buffer,4));
        asio::write(socket,asio::buffer(&data[0],data.size()));
    } catch (...) {
        throw;
    }
}
	
vector<TronSqlite3::Row> TronSqlite3::query(string format,...)
{
    char sql[1024];
    string strnull = "NULL";
    va_list args;
    va_start(args, format);
    string* s;
    char* str=sql;
    for(string::iterator it=format.begin();it!=format.end();it++)
    {
        if(*it != '%')
        {
            *str++ = *it;
            continue;
        }
        it++;
        switch(*it)
        {
        case 's':
            s = va_arg(args,string*);
            if(!s)
                s = &strnull;
            *str++ = '\'';
            for(size_t i=0;i!=s->size();i++)
            {
                if(s->at(i)=='\'')
                    *str++ = '\'';
                *str++ = s->at(i);
            }
            *str++ = '\'';
            break;
        default:
            *str++ = *it;
        }
    }
    *str = '\0';
    va_end (args);

    cout << str << endl;
    char* err;
    vector<Row> vec;

    sqlite3_exec(database_,sql,callback,&vec,&err);
    cout << "Query: " << sql << endl;
    for(int i=0;i!=vec.size();i++)
        for(Row::iterator it=vec[i].begin();it!=vec[i].end();it++)
            cout << it->first << ": " << it->second << " | ";
    cout << endl;
    return vec;
}


bool TronRoomCompare::operator()(TronRoom* a,TronRoom* b)
{
    return a->id() > b->id();
}
