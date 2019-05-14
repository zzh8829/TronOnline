#include "tronol.h"
#include "TronServer.h"
using namespace std;

#define PORT 10001

int main(int argc, char* argv[])
{
	try {
		TronServer server(PORT);
        	server.initialize();
		server.run();
	} catch (exception& e) {
        	cerr << "Main Exception: " << e.what() << endl;
	}
	return 0;
}

