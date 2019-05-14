CC            = gcc
CXX           = g++
CFLAGS        = -m64 -pipe -O3 -pthread -Wall -W -fPIE $(DEFINES)
CXXFLAGS      = -m64 -pipe -O3 -pthread -std=c++11 -Wall -W -fPIE $(DEFINES)
INCPATH       = -I. -I/usr/local/include
LINK          = g++
LFLAGS        =
LIBS          = $(SUBLIBS) -lboost_system -lboost_thread -lsqlite3 -pthread -L/usr/local/lib -lprotobuf -lpthread

OBJECTS_DIR   = ./build

SOURCES       = main.cpp \
		TronUtil.cpp \
		TronServer.cpp \
		TronRoom.cpp \
		TronPlayer.cpp \
		TronGame.cpp \
		tron.pb.cc
OBJECTS       = main.o \
		TronUtil.o \
		TronServer.o \
		TronRoom.o \
		TronPlayer.o \
		TronGame.o \
		tron.pb.o

TARGET        = ../bin/server

.cpp.o:
	$(CXX) -c $(CXXFLAGS) $(INCPATH) -o "$@" "$<"

all: $(TARGET)

$(TARGET):  $(OBJECTS)
	$(LINK) $(LFLAGS) -o $(TARGET) $(OBJECTS) $(OBJCOMP) $(LIBS)

main.o: main.cpp tronol.h \
		tron.pb.h \
		TronUtil.h \
		TronServer.h
	$(CXX) -c $(CXXFLAGS) $(INCPATH) -o main.o main.cpp `pkg-config --cflags --libs protobuf`

TronUtil.o: TronUtil.cpp TronUtil.h \
		tronol.h \
		tron.pb.h
	$(CXX) -c $(CXXFLAGS) $(INCPATH) -o TronUtil.o TronUtil.cpp `pkg-config --cflags --libs protobuf`

TronServer.o: TronServer.cpp TronServer.h \
		tronol.h \
		tron.pb.h \
		TronUtil.h \
		TronPlayer.h \
		TronRoom.h \
		TronGame.h
	$(CXX) -c $(CXXFLAGS) $(INCPATH) -o TronServer.o TronServer.cpp `pkg-config --cflags --libs protobuf`

TronRoom.o: TronRoom.cpp TronRoom.h \
		tronol.h \
		tron.pb.h \
		TronUtil.h \
		TronServer.h \
		TronGame.h
	$(CXX) -c $(CXXFLAGS) $(INCPATH) -o TronRoom.o TronRoom.cpp `pkg-config --cflags --libs protobuf`

TronPlayer.o: TronPlayer.cpp TronPlayer.h \
		tronol.h \
		tron.pb.h \
		TronUtil.h \
		TronServer.h
	$(CXX) -c $(CXXFLAGS) $(INCPATH) -o TronPlayer.o TronPlayer.cpp `pkg-config --cflags --libs protobuf`

TronGame.o: TronGame.cpp TronGame.h \
		tronol.h \
		tron.pb.h \
		TronUtil.h \
		TronServer.h
	$(CXX) -c $(CXXFLAGS) $(INCPATH) -o TronGame.o TronGame.cpp `pkg-config --cflags --libs protobuf`

tron.pb.o: tron.pb.cc tron.pb.h
	$(CXX) -c $(CXXFLAGS) $(INCPATH) -o tron.pb.o tron.pb.cc `pkg-config --cflags --libs protobuf`

clean:
	rm $(TARGET) *.o
