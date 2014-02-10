package ca.zihao.tronol.client;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * File: GamePanel.java
 * Date: 08/02/14
 * Time: 1:17 PM
 * Zihao Zhang @ zihao.ca
 */


public class GamePanel extends JPanel {


    public BufferedImage canvas;
    public Graphics2D g;

    static final Color[] colors = {Color.BLACK,Color.RED,Color.GREEN};

    public GamePanel() {
        setPreferredSize(new Dimension(500,500));
        canvas = new BufferedImage(500,500,BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D)canvas.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0,0,500,500);

    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(canvas,0,0,null);
    }

}
