package ca.zihao.tronol.client;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * File: ImageLoader.java
 * Date: 08/02/14
 * Time: 1:57 PM
 * Zihao Zhang @ zihao.ca
 */


public class ImageLoader {

    static BufferedImage load(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(ImageLoader.class.getClassLoader().getResourceAsStream(path));
        } catch (Exception e){
            e.printStackTrace();
        };
        return img;
    }
}
