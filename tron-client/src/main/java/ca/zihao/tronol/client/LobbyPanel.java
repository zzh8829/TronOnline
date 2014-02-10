package ca.zihao.tronol.client;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.*;

/**
 * File: LobbyPanel.java
 * Date: 08/02/14
 * Time: 6:05 PM
 * Zihao Zhang @ zihao.ca
 */


public class LobbyPanel extends JPanel{

    public JLabel usernameLabel;
    public DefaultListModel listModel;
    private JPanel mainPanel;
    private JList roomList;
    private JButton createButton;
    private String createRoomName;
    private TronRoom selectedRoom;

    public LobbyPanel() {

        usernameLabel = new JLabel("Room List:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 20));

        createButton = new JButton("Create");


        listModel = new DefaultListModel();

        roomList = new JList(listModel);
        roomList.setFont(new Font("monospaced", Font.PLAIN, 15));

        roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomList.setSelectedIndex(0);
        roomList.setVisibleRowCount(10);

        JScrollPane roomScrollPane = new JScrollPane(roomList);

        usernameLabel.setBounds(50, 25, 700, 50);
        createButton.setBounds(500, 50, 100, 25);
        roomScrollPane.setBounds(50, 100, 700, 400);

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 0, 800, 600);
        mainPanel.add(usernameLabel);
        mainPanel.add(createButton);
        mainPanel.add(roomScrollPane);

        setLayout(null);
        add(mainPanel);

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                createRoomName = JOptionPane.showInputDialog(null, "Create new room", "Awesome Room");
                disableGUI();
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        Main.client.roomCreate(createRoomName);
                        return null;
                    }

                    @Override
                    protected void done() {
                        enableGUI();
                    }
                }.execute();
            }
        });

        roomList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() > 1) {
                    selectedRoom = (TronRoom) listModel.getElementAt(roomList.locationToIndex(e.getPoint()));
                    System.out.println(selectedRoom);
                    if (selectedRoom != null) {
                        disableGUI();
                        new SwingWorker<Void, Void>() {
                            @Override
                            protected Void doInBackground() throws Exception {
                                Main.client.roomEnter(selectedRoom.id);
                                return null;
                            }

                            @Override
                            protected void done() {
                                enableGUI();
                            }
                        }.execute();
                    }
                }
            }
        });

        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!isVisible()) return;
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        Main.client.roomList();
                        return null;
                    }

                    @Override
                    protected void done() {
                        listModel.clear();
                        for (TronRoom room : Main.client.roomList) {
                            listModel.addElement(room);
                        }
                    }
                }.execute();
            }
        });
        timer.start();


    }

    void enableGUI() {
        for (Component c : mainPanel.getComponents())
            c.setEnabled(true);
    }

    void disableGUI() {
        for (Component c : mainPanel.getComponents())
            c.setEnabled(false);
    }

}
