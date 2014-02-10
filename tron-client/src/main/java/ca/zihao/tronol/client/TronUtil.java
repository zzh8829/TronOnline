package ca.zihao.tronol.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * File: TronUtil.java
 * Date: 08/02/14
 * Time: 6:56 PM
 * Zihao Zhang @ zihao.ca
 */


public class TronUtil {

    static byte[] ReadData(DataInputStream in) throws Exception{
        synchronized (in) {
            byte[] data = new byte[in.readInt()];
            in.readFully(data);
            return data;
        }
    }

    static void WriteData(DataOutputStream out,byte[] data) throws Exception{
        synchronized (out) {
            out.writeInt(data.length);
            out.write(data);
        }
    }
}
