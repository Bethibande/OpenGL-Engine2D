package de.Bethibande.Engine.Error;

import de.Bethibande.Engine.EngineCore;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EngineError {

    public static void openError(Exception exception) {
        JFrame f = new JFrame();
        f.setSize(400, 550);
        f.setTitle("Oops! It seems like an error occurred!");

        JPanel root = new JPanel();
        root.setLayout(new GroupLayout(root));
        f.add(root);

        JTextArea text = new JTextArea();
        text.setText(exception.toString());
        for(StackTraceElement el : exception.getStackTrace()) {
            text.append(" \n    At " + el.getFileName() + ":" + el.getLineNumber() + ", " + el.getMethodName() + "()");
        }
        text.setEditable(false);
        text.setSize(360, 455);
        text.setLocation(10, 10);
        root.add(text);

        JButton close = new JButton();
        close.setText("Close");
        close.setSize(165, 30);

        JButton retry = new JButton();
        retry.setText("Restart engine");
        retry.setSize(165, 30);

        root.add(retry);
        root.add(close);

        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EngineCore.stop();
            }
        });
        retry.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                f.dispose();
                EngineCore.stop();
            }
        });
        retry.setEnabled(false);

        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        close.setLocation(10, 475);
        retry.setLocation(205, 475);

        f.setVisible(true);
    }

}