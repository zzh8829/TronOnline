tron-online
===========

tron lighting cycle online

Demo
----
![login](https://raw.githubusercontent.com/zzh8829/TronOnline/master/login.png)
![game](https://raw.githubusercontent.com/zzh8829/TronOnline/master/game.png)


Requirement
-----------
Client:
Java 7
Maven 3
Python 3

Server:
gcc
boost
protobuf

Compile
-------
Deps (mac)
```bash
brew install protobuf pkg-config boost maven
brew cask install java
```

Client:
```bash
cd tron-client
mvn package
```

Server:
```bash
cd tron-server
make -j 4
```

Database
--------
```bash
sqlite3 tron-server/bin/tron.db < tron-server/db.sql
```

## Running
cd tron-server/bin && ./server
cd tron-client/target && java -jar tron-client-1.0.jar
