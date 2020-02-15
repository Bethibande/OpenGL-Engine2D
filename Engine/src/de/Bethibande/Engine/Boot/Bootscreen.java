package de.Bethibande.Engine.Boot;

import de.Bethibande.Engine.Rendering.Images;
import de.Bethibande.Engine.utils.Colors;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Bootscreen {

    private static JFrame f;

    private static SpriteLoader loader = new SpriteLoader();

    public static int progress;
    public static String action = "Starting..";
    public static void open() {
        Thread t = new Thread() {
            public void run() {
                loader.loadSprite("/res/BEngine.png", "BEngine");
                //loader.loadSprite("/res/BDocs.png", "BDocs");
                loader.loadSprite("/res/B.png", "BIcon");
                BufferedImage engine1 = Images.resize(Images.bufferImage(loader.getSprite("BEngine").getImage()), 550, 450);
                //BufferedImage docs1 = Images.resize(Images.bufferImage(loader.getSprite("BDocs").getImage()), 300, 180);
                Image engine = (Image) engine1.getSubimage(0, 0, engine1.getWidth(), engine1.getHeight());
                //Image docs = (Image)docs1.getSubimage(0, 0, docs1.getWidth(), docs1.getHeight());
                f = new

                        JFrame();
                f.setSize(600, 400);
                f.setUndecorated(true);
                Dimension s = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();
                f.setLocation((int) s.getWidth() / 2 - 300, (int) s.getHeight() / 2 - 200);
                f.setIconImage(loader.getSprite("BIcon").

                        getImage());
                f.setVisible(true);
                f.setTitle("BEngine");
                f.createBufferStrategy(2);
                while (progress < 501) {
                    Graphics g = f.getBufferStrategy().getDrawGraphics();
                    g.clearRect(0, 0, 600, 400);
                    g.setColor(Colors.black);
                    g.fillRect(0, 0, f.getWidth(), f.getHeight());
                    //g.setColor(Colors.black_dark);
                    //g.fillRect(0, 0, f.getWidth(), f.getHeight() / 20);
                    g.drawImage(engine, 25, -50, null);
                    g.setColor(Colors.blue);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setStroke(new BasicStroke(5));
                    g2d.drawLine(50, 370, 50 + progress, 370);
                    g.setColor(Colors.black_dark);
                    g.drawString(action, 50, 360);
                    f.getBufferStrategy().show();
                    if (progress >= 500) {
                        return;
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    f.setAlwaysOnTop(true);
                    f.setAutoRequestFocus(true);
                }
            }
        };
        t.start();
    }

    public static void close() {
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        f.dispose();
    }

    private static class SpriteLoader {

        @Getter
        private HashMap<String, ImageIcon> sprites = new HashMap<>();

        // path -> /Sprites/Sprite.png
        public void loadSprite(String path, String name) {
            ImageIcon img = new ImageIcon(this.getClass().getResource(path));
            if(!this.sprites.containsKey(name)) {
                this.sprites.put(name, img);
            }
        }

        public ImageIcon getSprite(String name) {
            return this.sprites.get(name);
        }

        public void unload(String name) {
            this.sprites.remove(name);
        }

    }


}
