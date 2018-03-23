package ca.zihao.tronol.client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * File: MainWindow.java
 * Date: 08/02/14
 * Time: 12:43 PM
 * Zihao Zhang @ zihao.ca
 */

public class MainWindow extends JFrame {

    public final static String LOGINPANEL = "Login";
    public final static String LOBBYPANEL = "Lobby";
    public final static String ROOMPANEL = "Room";

    public JPanel mainPanel;
    public LoginPanel loginPanel;
    public LobbyPanel lobbyPanel;
    public RoomPanel roomPanel;

    private CardLayout mainLayout;

    public MainWindow() {
        setTitle("Tron Online");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800,600);
        setResizable(false);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(mainLayout = new CardLayout());
        mainPanel.setLayout(mainLayout);
        mainPanel.add(loginPanel = new LoginPanel(), LOGINPANEL);
        mainPanel.add(lobbyPanel = new LobbyPanel(), LOBBYPANEL);
        mainPanel.add(roomPanel = new RoomPanel(), ROOMPANEL);

        mainLayout.show(mainPanel, LOGINPANEL);

        add(mainPanel);

        setVisible(true);

        if(!Main.client.connect("localhost", 10001)) {
            JOptionPane.showMessageDialog(null,"Unable to connect to server","Error",JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public void switchPanel(String panel) {
        mainLayout.show(mainPanel,panel);
    }
}
