package com.wk.excel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Created by 005689 on 2017/9/21.
 */
public class App implements Runnable {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new App());
    }

    public void run() {

        JFrame frame = new JFrame("ttt");

        JPanel panel = new JPanel(new FlowLayout());

        renderPanel(panel);
        frame.add(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setVisible(true);
    }

    private void renderPanel(JPanel panel) {

        panel.add(createBtn(panel));
        panel.add(new JButton("button2"));
        panel.add(new JButton("button3"));
        panel.add(new JButton("button4"));
        panel.add(createLogWindow());
    }

    private JTextArea createLogWindow() {

        JTextArea jTextArea = new JTextArea(200, 500);
        return jTextArea;
    }

    private JButton createBtn(JPanel panel) {

        JButton button = new JButton("button1");
        button.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("."));
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.setFileFilter(new FileNameExtensionFilter("only excel file", "xls", "xlsx"));

                int retVal = fileChooser.showOpenDialog(panel);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    File selFile = fileChooser.getSelectedFile();
                    System.out.println("selected file is::" + selFile);
                }
            }
        });

        return button;
    }
}
