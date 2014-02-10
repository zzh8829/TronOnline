package ca.zihao.tronol.client;

import javax.swing.*;

/**
 * File: Main.java
 * Date: 08/02/14
 * Time: 6:14 PM
 * Zihao Zhang @ zihao.ca
 */


public class Main {
    public static MainWindow window;
    public static TronClient client;

    public static void main(String[] args) {
        client = new TronClient();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Main.window = new MainWindow();
            }
        });
    }
}
