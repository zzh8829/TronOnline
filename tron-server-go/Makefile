all: build

protobuf:
	protoc --go_out=./pkg/protobuf -I./../protobuf/ ./../protobuf/tron.proto

BUILD_DIR = ${GOPATH}/src/github.com/zzh8829/tron-server-go

sync: protobuf
	rsync -aq --delete --exclude vendor . $(BUILD_DIR)

build: sync
	go build ./cmd/tron-server-go

run: sync
	go run ./cmd/tron-server-go/main.go

docker: sync
	CGO_ENABLED=0 GOOS=linux go build -a -installsuffix nocgo ./cmd/tron-server-go

dep: sync
	cd $(BUILD_DIR) && dep ensure
	cp $(BUILD_DIR)/Gopkg.lock .
