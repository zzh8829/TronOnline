all: protobuf
	mkdir -p bin
	cd src && make

protobuf:
	cd ../protobuf && python3 genproto.py cpp

clean:
	cd src && make clean
