package ca.zihao.tronol.client;

/**
 * File: TronRoom.java
 * Date: 09/02/14
 * Time: 1:27 AM
 * Zihao Zhang @ zihao.ca
 */


public class TronRoom {

    public int id;
    public String name;
    public String status;
    public String host;

    public TronRoom() {

    }

    public TronRoom(int id, String name, String host, String status) {
        this.id = id;
        this.name = name;
        this.host = host;
        this.status = status;
    }

    public String toString() {
        return String.format("| %-2d | %-40s | %-15s | %-4s |",id,name,host,status);
    }
}

