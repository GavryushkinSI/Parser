package com.parsesignal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class My_JMenuBar extends JMenuBar {

     String []  items = {"Menu","   Clear Equity","Set Validation","   Martin"};
    JMenu menu = new JMenu(items[0]);
     JMenuItem item_1=new JMenuItem(items[1]);
    JMenuItem item_2=new JMenuItem(items[2]);
    JMenuItem item_3=new JMenuItem(items[3]);
    static Checkbox check=new Checkbox("Set Validation");
    public static boolean getCheck() {
        return check.getState();
    }

    public  My_JMenuBar() {
        super();
        this.add(create_menu());
        check.setState(true);
    }
    public JMenu create_menu() {
        item_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                write_file("data.txt","");
                //write_file("poz.txt","");
            }
        });
        item_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
//        item_3.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                String x=JOptionPane.showInputDialog(new Frame(),"Укажите ряд мартингейла (например, 1;2;3;4)");
//                try {
//                    Martin.level = Integer.parseInt(JOptionPane.showInputDialog(new Frame(), "Укажите уровень (положительное число,либо 0)"));
//                }
//                catch (Exception ex){
//                    Martin.level=0;
//                }
//                try {
//                    String[] y = x.split(";");
//                    for (int i = 0; i < y.length; i++) {
//                        Martin.index.add(Integer.parseInt(y[i]));
//                    }
//                    System.out.println(Martin.index);
//                }
//                catch(Exception ex){
//
//                }
//
//            }
//        });

        menu.add(item_1);
//        menu.add(item_2.add(check));
        //menu.add(item_3);
        return menu;
    }
    void write_file(String path, String content) {
        BufferedWriter wr = null;
        try {
            wr = new BufferedWriter(new FileWriter(path, false));
            wr.write(content);
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
