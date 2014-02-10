package ca.zihao.tronol.client;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import ca.zihao.tronol.protobuf.TronProtos.*;

import javax.swing.*;

/**
 * File: TronClient.java
 * Date: 08/02/14
 * Time: 6:05 PM
 * Zihao Zhang @ zihao.ca
 */


public class TronClient {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private boolean connected;
    private boolean loggedIn;

    private Command command;
    private Response response;

    private String loginUser;
    private TronRoom room;
    public List<TronRoom> roomList;
    public List<String> userList;

    int gameTick;

    public TronClient() {
        roomList = new ArrayList<>();
        userList = new ArrayList<>();
    }


    public boolean connect(String server,int port){
        try {
            socket = new Socket(server,port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            connected = true;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void disconnect(){
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void register(String username, String password) {
        command = Command.newBuilder()
                .setType(Command.CommandType.ACCOUNT)
                .setAccount(
                        AccountCommand.newBuilder()
                                .setType(AccountCommand.CommandType.REGISTER)
                                .setUsername(username)
                                .setPassword(password))
                .build();
        response = null;
        try {
            TronUtil.WriteData(out, command.toByteArray());
            response = Response.parseFrom(TronUtil.ReadData(in));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        if(response.getAccount().getType() == AccountResponse.ResponseType.SUCCESS) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(null,"Account Register Success","Success",JOptionPane.INFORMATION_MESSAGE);
                }
            });
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(null,"Account Register Failed: " + response.getAccount().getType().toString(),"Error",JOptionPane.ERROR_MESSAGE);
                }
            });

        }
    }

    public void login(String username,String password) {
        command = Command.newBuilder()
                .setType(Command.CommandType.ACCOUNT)
                .setAccount(
                        AccountCommand.newBuilder()
                                .setType(AccountCommand.CommandType.LOGIN)
                                .setUsername(username)
                                .setPassword(password))
                .build();
        response = null;
        try {
            TronUtil.WriteData(out, command.toByteArray());
            response = Response.parseFrom(TronUtil.ReadData(in));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        if(response.getAccount().getType() == AccountResponse.ResponseType.SUCCESS) {
            this.loginUser = username;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    //JOptionPane.showMessageDialog(null,"Account Login Success","Success",JOptionPane.INFORMATION_MESSAGE);
                    Main.window.switchPanel(Main.window.LOBBYPANEL);
                }
            });
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(null,"Account Login Failed: " + response.getAccount().getType().toString() ,"Error",JOptionPane.ERROR_MESSAGE);
                }
            });

        }
    }

    public void roomList(){
        command = Command.newBuilder()
                .setType(Command.CommandType.ROOM)
                .setRoom(
                        RoomCommand.newBuilder()
                                .setType(RoomCommand.CommandType.LIST)
                ).build();
        response = getResponse(command);
        if(response.getRoom().getType() == RoomResponse.ResponseType.SUCCESS) {
            roomList.clear();
            for(RoomResponse.RoomInfo info: response.getRoom().getRoomList())
            {
                TronRoom room = new TronRoom(info.getId(),info.getName(),info.getHost(),info.getStatus());
                roomList.add(room);
            }
        }
    }

    public void roomCreate(String name) {
        command = Command.newBuilder()
                .setType(Command.CommandType.ROOM)
                .setRoom(
                        RoomCommand.newBuilder()
                                .setType(RoomCommand.CommandType.CREATE)
                                .setName(name)
                ).build();
        response = getResponse(command);
        if(response.getRoom().getType() == RoomResponse.ResponseType.SUCCESS) {
            roomEnter(response.getRoom().getId());
        }
    }

    public void roomEnter(int id) {
        System.out.println("Enter " + id);
        command = Command.newBuilder()
                .setType(Command.CommandType.ROOM)
                .setRoom(
                        RoomCommand.newBuilder()
                                .setType(RoomCommand.CommandType.ENTER)
                                .setId(id)
                ).build();
        response = getResponse(command);
        if(response.getRoom().getType() == RoomResponse.ResponseType.SUCCESS) {
            for(TronRoom r:roomList) {
                if(r.id == id) {
                    room = r;
                    break;
                }
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Main.window.switchPanel(Main.window.ROOMPANEL);
                }
            });
        }
    }

    public void roomLeave(int id) {

    }

    public void gameRoom(){
        command = Command.newBuilder()
                .setType(Command.CommandType.GAME)
                .setGame(GameCommand.newBuilder()
                        .setType(GameCommand.CommandType.ROOM))
                .build();
        response = getResponse(command);
        if(response.getGame().getType() == GameResponse.ResponseType.ROOM) {
            userList.clear();
            for(String s:response.getGame().getUsersList())
            {
                userList.add(s);
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    for(int i=0;i!=4;i++){
                        String name = "";
                        if(i < userList.size())
                            name = userList.get(i);
                        Main.window.roomPanel.playerLabelList[i].setText(name);
                    }
                    if(Main.window.roomPanel.playerLabelList[0].getText().equals(loginUser) &&
                            userList.size() > 1) {
                        Main.window.roomPanel.startButton.setEnabled(true);
                    } else {
                        Main.window.roomPanel.startButton.setEnabled(false);
                    }
                }
            });
        } else if(response.getGame().getType() ==
                GameResponse.ResponseType.GAME) {
            Main.window.roomPanel.isInGame = true;
            Main.window.roomPanel.startButton.setEnabled(false);
            Main.window.roomPanel.gamePanel.g.setColor(Color.BLACK);
            Main.window.roomPanel.gamePanel.g.fillRect(0,0,500,500);
            gameTick = -1;
        }
    }

    public void gameStart(){
        command = Command.newBuilder()
                .setType(Command.CommandType.GAME)
                .setGame(GameCommand.newBuilder()
                        .setType(GameCommand.CommandType.START))
                .build();
        response = getResponse(command);
        System.out.println(response);
        if(response.getGame().getType() == GameResponse.ResponseType.SUCCESS) {
            Main.window.roomPanel.isInGame = true;
            Main.window.roomPanel.startButton.setEnabled(false);
            Main.window.roomPanel.gamePanel.g.setColor(Color.BLACK);
            Main.window.roomPanel.gamePanel.g.fillRect(0,0,500,500);
            gameTick = -1;
        }
    }

    public void gameUpdate(){
        GameCommand.Builder builder = GameCommand.newBuilder();
        builder.setType(GameCommand.CommandType.GAME);
        builder.setTick(gameTick);
        //System.out.println(Main.window.roomPanel.updateQueue.size());
        builder.addAllDir(Main.window.roomPanel.updateQueue);
        command = Command.newBuilder()
                .setType(Command.CommandType.GAME)
                .setGame(builder)
                .build();
        //System.out.println(command);
        response = getResponse(command);
        //System.out.println(response);
        if(response.getGame().getType() == GameResponse.ResponseType.GAME) {
            for(GameResponse.GameSet s:response.getGame().getSetAtList()) {
                Main.window.roomPanel.gamePanel.g.setColor(
                        Main.window.roomPanel.gamePanel.colors[s.getV()]);
                Main.window.roomPanel.gamePanel.g.fillRect(s.getX()*5,s.getY()*5,5,5);
            }
            gameTick = response.getGame().getTick();
        } else if(response.getGame().getType() == GameResponse.ResponseType.OVER) {
            gameTick = -1;
            Main.window.roomPanel.isInGame = false;
            JOptionPane.showMessageDialog(null,"Winner is: " + response.getGame().getWinner(),"Game Over",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public Response getResponse(Command cmd){
        try {
            TronUtil.WriteData(out, cmd.toByteArray());
            return Response.parseFrom(TronUtil.ReadData(in));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(1);
        return null;
    }
}
