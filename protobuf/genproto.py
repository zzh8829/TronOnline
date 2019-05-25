import glob
import os
import sys
import shutil


def main():
    language = sys.argv[1]
    if language == "java":
        gen_path = "../tron-client/src/main/java"
        opt_out = "--java_out=" + gen_path
    elif language == "cpp":
        gen_path = "../tron-server/src"
        opt_out = "--cpp_out=" + gen_path
    elif language == 'go':
        gen_path = "../tron-server-go/pkg/protobuf"
        opt_out = "--go_out=" + gen_path
    else:
        print("Unknown Language")
        sys.exit(1)

    os.chdir(os.path.dirname(os.path.abspath(__file__)))

    os.makedirs(gen_path, exist_ok=True)
    protos = glob.glob("*.proto")

    for proto in protos:
        os.system('protoc %s %s' % (opt_out, proto))


if __name__ == '__main__':
    main()
