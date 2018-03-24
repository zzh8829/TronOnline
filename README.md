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
Protobuf:
```bash
cd protobuf
python genproto.py cpp
python genproto.py java
```

Client:
```bash
cd tron-client
mvn package
```

Server:
```bash
cd tron-server
make
```

Database
--------
```bash
sqlite3 tron-server/bin/tron.db < tron-server/db.sql
```

Dependency
----------
for mac
```bash
python
maven
boost
protobuf
```
