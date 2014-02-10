CC            = gcc
CXX           = g++
CFLAGS        = -m64 -pipe -O3 -pthread -Wall -W -fPIE $(DEFINES)
CXXFLAGS      = -m64 -pipe -O3 -pthread -std=c++0x -Wall -W -fPIE $(DEFINES)
INCPATH       = -I. -I/usr/local/include
LINK          = g++
LFLAGS        = -m64 -Wl,-O1
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
		/usr/local/include/google/protobuf/stubs/common.h \
		/usr/local/include/google/protobuf/generated_message_util.h \
		/usr/local/include/google/protobuf/message.h \
		/usr/local/include/google/protobuf/message_lite.h \
		/usr/local/include/google/protobuf/descriptor.h \
		/usr/local/include/google/protobuf/repeated_field.h \
		/usr/local/include/google/protobuf/stubs/type_traits.h \
		/usr/local/include/google/protobuf/stubs/template_util.h \
		/usr/local/include/google/protobuf/extension_set.h \
		/usr/local/include/google/protobuf/generated_enum_reflection.h \
		/usr/local/include/google/protobuf/unknown_field_set.h \
		TronUtil.h \
		TronServer.h
	$(CXX) -c $(CXXFLAGS) $(INCPATH) -o main.o main.cpp

TronUtil.o: TronUtil.cpp TronUtil.h \
		tronol.h \
		tron.pb.h \
		/usr/local/include/google/protobuf/stubs/common.h \
		/usr/local/include/google/protobuf/generated_message_util.h \
		/usr/local/include/google/protobuf/message.h \
		/usr/local/include/google/protobuf/message_lite.h \
		/usr/local/include/google/protobuf/descriptor.h \
		/usr/local/include/google/protobuf/repeated_field.h \
		/usr/local/include/google/protobuf/stubs/type_traits.h \
		/usr/local/include/google/protobuf/stubs/template_util.h \
		/usr/local/include/google/protobuf/extension_set.h \
		/usr/local/include/google/protobuf/generated_enum_reflection.h \
		/usr/local/include/google/protobuf/unknown_field_set.h
	$(CXX) -c $(CXXFLAGS) $(INCPATH) -o TronUtil.o TronUtil.cpp

TronServer.o: TronServer.cpp TronServer.h \
		tronol.h \
		tron.pb.h \
		/usr/local/include/google/protobuf/stubs/common.h \
		/usr/local/include/google/protobuf/generated_message_util.h \
		/usr/local/include/google/protobuf/message.h \
		/usr/local/include/google/protobuf/message_lite.h \
		/usr/local/include/google/protobuf/descriptor.h \
		/usr/local/include/google/protobuf/repeated_field.h \
		/usr/local/include/google/protobuf/stubs/type_traits.h \
		/usr/local/include/google/protobuf/stubs/template_util.h \
		/usr/local/include/google/protobuf/extension_set.h \
		/usr/local/include/google/protobuf/generated_enum_reflection.h \
		/usr/local/include/google/protobuf/unknown_field_set.h \
		TronUtil.h \
		TronPlayer.h \
		TronRoom.h \
		TronGame.h
	$(CXX) -c $(CXXFLAGS) $(INCPATH) -o TronServer.o TronServer.cpp

TronRoom.o: TronRoom.cpp TronRoom.h \
		tronol.h \
		tron.pb.h \
		/usr/local/include/google/protobuf/stubs/common.h \
		/usr/local/include/google/protobuf/generated_message_util.h \
		/usr/local/include/google/protobuf/message.h \
		/usr/local/include/google/protobuf/message_lite.h \
		/usr/local/include/google/protobuf/descriptor.h \
		/usr/local/include/google/protobuf/repeated_field.h \
		/usr/local/include/google/protobuf/stubs/type_traits.h \
		/usr/local/include/google/protobuf/stubs/template_util.h \
		/usr/local/include/google/protobuf/extension_set.h \
		/usr/local/include/google/protobuf/generated_enum_reflection.h \
		/usr/local/include/google/protobuf/unknown_field_set.h \
		TronUtil.h \
		TronServer.h \
		TronGame.h
	$(CXX) -c $(CXXFLAGS) $(INCPATH) -o TronRoom.o TronRoom.cpp

TronPlayer.o: TronPlayer.cpp TronPlayer.h \
		tronol.h \
		tron.pb.h \
		/usr/local/include/google/protobuf/stubs/common.h \
		/usr/local/include/google/protobuf/generated_message_util.h \
		/usr/local/include/google/protobuf/message.h \
		/usr/local/include/google/protobuf/message_lite.h \
		/usr/local/include/google/protobuf/descriptor.h \
		/usr/local/include/google/protobuf/repeated_field.h \
		/usr/local/include/google/protobuf/stubs/type_traits.h \
		/usr/local/include/google/protobuf/stubs/template_util.h \
		/usr/local/include/google/protobuf/extension_set.h \
		/usr/local/include/google/protobuf/generated_enum_reflection.h \
		/usr/local/include/google/protobuf/unknown_field_set.h \
		TronUtil.h \
		TronServer.h
	$(CXX) -c $(CXXFLAGS) $(INCPATH) -o TronPlayer.o TronPlayer.cpp

TronGame.o: TronGame.cpp TronGame.h \
		tronol.h \
		tron.pb.h \
		/usr/local/include/google/protobuf/stubs/common.h \
		/usr/local/include/google/protobuf/generated_message_util.h \
		/usr/local/include/google/protobuf/message.h \
		/usr/local/include/google/protobuf/message_lite.h \
		/usr/local/include/google/protobuf/descriptor.h \
		/usr/local/include/google/protobuf/repeated_field.h \
		/usr/local/include/google/protobuf/stubs/type_traits.h \
		/usr/local/include/google/protobuf/stubs/template_util.h \
		/usr/local/include/google/protobuf/extension_set.h \
		/usr/local/include/google/protobuf/generated_enum_reflection.h \
		/usr/local/include/google/protobuf/unknown_field_set.h \
		TronUtil.h \
		TronServer.h
	$(CXX) -c $(CXXFLAGS) $(INCPATH) -o TronGame.o TronGame.cpp

tron.pb.o: tron.pb.cc tron.pb.h \
		/usr/local/include/google/protobuf/stubs/common.h \
		/usr/local/include/google/protobuf/generated_message_util.h \
		/usr/local/include/google/protobuf/message.h \
		/usr/local/include/google/protobuf/message_lite.h \
		/usr/local/include/google/protobuf/descriptor.h \
		/usr/local/include/google/protobuf/repeated_field.h \
		/usr/local/include/google/protobuf/stubs/type_traits.h \
		/usr/local/include/google/protobuf/stubs/template_util.h \
		/usr/local/include/google/protobuf/extension_set.h \
		/usr/local/include/google/protobuf/generated_enum_reflection.h \
		/usr/local/include/google/protobuf/unknown_field_set.h \
		/usr/local/include/google/protobuf/stubs/once.h \
		/usr/local/include/google/protobuf/stubs/atomicops.h \
		/usr/local/include/google/protobuf/stubs/platform_macros.h \
		/usr/local/include/google/protobuf/stubs/atomicops_internals_x86_msvc.h \
		/usr/local/include/google/protobuf/stubs/atomicops_internals_macosx.h \
		/usr/local/include/google/protobuf/stubs/atomicops_internals_x86_gcc.h \
		/usr/local/include/google/protobuf/stubs/atomicops_internals_arm_gcc.h \
		/usr/local/include/google/protobuf/stubs/atomicops_internals_arm_qnx.h \
		/usr/local/include/google/protobuf/stubs/atomicops_internals_mips_gcc.h \
		/usr/local/include/google/protobuf/stubs/atomicops_internals_pnacl.h \
		/usr/local/include/google/protobuf/stubs/atomicops_internals_atomicword_compat.h \
		/usr/local/include/google/protobuf/io/coded_stream.h \
		/usr/local/include/google/protobuf/wire_format_lite_inl.h \
		/usr/local/include/google/protobuf/wire_format_lite.h \
		/usr/local/include/google/protobuf/generated_message_reflection.h \
		/usr/local/include/google/protobuf/reflection_ops.h \
		/usr/local/include/google/protobuf/wire_format.h \
		/usr/local/include/google/protobuf/descriptor.pb.h
	$(CXX) -c $(CXXFLAGS) $(INCPATH) -o tron.pb.o tron.pb.cc

clean:
	rm $(TARGET) *.o