package ca.zihao.tronol.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * File: RoomPanel.java
 * Date: 08/02/14
 * Time: 1:17 PM
 * Zihao Zhang @ zihao.ca
 */


public class RoomPanel extends JPanel {

    GamePanel gamePanel;

    JButton startButton;
    JButton exitButton;

    public JLabel[] playerLabelList;

    ArrayList<Integer> updateQueue;

    boolean isRunning;
    boolean isInGame;

    public RoomPanel() {
        isRunning = true;
        isInGame = false;

        updateQueue = new ArrayList<>();

        setLayout(null);
        setBounds(0, 0, 800, 600);
        setVisible(false);
        setFocusable(true);

        gamePanel = new GamePanel();
        startButton = new JButton("Start");
        exitButton = new JButton("Exit");

        startButton.setEnabled(false);
        exitButton.setEnabled(false);

        playerLabelList = new JLabel[4];


        for (int i = 0; i != playerLabelList.length; i++) {
            JLabel l = playerLabelList[i] = new JLabel();
            add(l);
            l.setBounds(600, i * 40 + 50, 100, 30);
        }

        add(startButton);
        add(exitButton);

        startButton.setBounds(600, 400, 100, 25);
        exitButton.setBounds(600, 450, 100, 25);

        add(gamePanel);
        gamePanel.setBounds(25, 25, 500, 500);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        Main.client.gameStart();
                        return null;
                    }

                    @Override
                    protected void done() {
                    }
                }.execute();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                while (isRunning) {
                    int last = (int) (System.currentTimeMillis() % 1000);
                    if (isVisible()) {
                        if (isInGame) {
                            Main.client.gameUpdate();
                            updateQueue.clear();
                            publish(0);
                        } else {
                            Main.client.gameRoom();
                        }
                    }
                    int now = (int) (System.currentTimeMillis() % 1000);
                    if (now - last < 33)
                        Thread.sleep(33 - now + last);
                }
                return null;
            }

            @Override
            protected void done() {
            }

            @Override
            protected void process(List<Integer> updates) {
                gamePanel.repaint();
            }
        }.execute();

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                int key = keyEvent.getKeyCode();
                if (!isInGame) return;
                switch (key) {
                    case KeyEvent.VK_W:
                        updateQueue.add(0);
                        break;
                    case KeyEvent.VK_D:
                        updateQueue.add(1);
                        break;
                    case KeyEvent.VK_S:
                        updateQueue.add(2);
                        break;
                    case KeyEvent.VK_A:
                        updateQueue.add(3);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
            }
        });
    }
}
