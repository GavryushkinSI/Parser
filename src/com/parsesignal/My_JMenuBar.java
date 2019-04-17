package com.parsesignal;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class My_JMenuBar extends JMenuBar {
     String []  items = {"Menu","clear Equity"};
    JMenu menu = new JMenu(items[0]);
     JMenuItem item_1=new JMenuItem(items[1]);
     //JMenuItem item_2=new JMenuItem(items[2]);
////    static  JMenuItem item_3=new JMenuItem(items[3]);
//    static JMenuItem item_4=new JMenuItem(items[4]);
//    static  JMenuItem item_5=new JMenuItem(items[5]);

    public  My_JMenuBar() {
        super();
        this.add(create_menu());
    }
    public JMenu create_menu() {
        item_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                write_file("data.txt","");
            }
        });
        menu.add(item_1);
       // menu.add(item_2);
//        menu.add(item_2);
//        menu.add(item_3);
//        menu.add(item_4);
//        menu.add(item_5);
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
