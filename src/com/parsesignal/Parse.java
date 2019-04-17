package com.parsesignal;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by Gavryushkin Stanislav  on 21.02.2018.
 * (rework 16.02.2019 add:Equity)
 * (rework 19.02.2019 add:Equity graph)
 * @version 2.4
 */
public class Parse {

    JFrame frame;
    JTabbedPane tabs;
    MyLabel screenLabel;
    JLabel screenLabel_2;
    JPanel panel;
    JScrollPane screenScroll;
    JScrollPane screenScroll_2;
    JTextField text;
    BufferedImage screen;
    ImageIcon image;
    ImageIcon image2;
    JButton butscreen;
    JButton start;
    JButton stop;
    JButton buy;
    JButton sell;
    JButton hold;
    JButton buy_2;
    JButton sell_2;
    JButton hold_2;
    JButton buy_3;
    JButton sell_3;
    JButton hold_3;
    JButton cond_1;
    JButton cond_2;
    JButton option;
    JButton info;
    JButton instrument;
    JButton martin;
    JButton equity;
    Dialog dialog, dialog_2;
    Checkbox def_cor;

    int status = 0;
    int status_2 = 0;
    int status_3 = 0;
    int status_4 = 0;
    int status_5 = 0;
    int size = 0;
    static int [] x_toint=null;


    //Метод контроля коректности скрина
    //*********************************************************************************************//
    //Парсинг цены с сайта
    public void getPrice() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {

                    Document doc = null;
                    Document doc_2 = null;
                    try {
                        //doc = Jsoup.connect("https://smart-lab.ru/").get();
                        doc = Jsoup.connect("https://www.finam.ru/quotes/futures/moex/").get();
                        doc_2 = Jsoup.connect("https://www.finam.ru/profile/mosbirzha-fyuchersy/sbrf").get();
                    } catch (IOException e) {
                        e.printStackTrace();
                        //test.sendSignal("ERROR","Нет соединения с сервером для запроса текущей цены инструмента, " +
                        //       "либо отсутствует связь с интернетом "+new Date());
                        continue;
                    }

                    StringBuffer buffer = new StringBuffer(doc.text());
                    StringBuffer buffer_2 = new StringBuffer(doc_2.text());
                    //System.out.println(buffer);
                    //Наполнение карты*****************//
                    Map<String, String> map = new HashMap<>();
                    try {
                        map.put(elements[1], buffer.substring(buffer.indexOf("Si "), buffer.indexOf("Si ") + 8).split("Si ")[1]);
                        map.put(elements[2], buffer.substring(buffer.indexOf("RTS "), buffer.indexOf("RTS ") + 10).split("RTS ")[1]);
                        map.put(elements[3], buffer.substring(buffer.indexOf("LKOH "), buffer.indexOf("LKOH ") + 10).split("LKOH ")[1]);
                        map.put(elements[4], buffer.substring(buffer.indexOf("BR "), buffer.indexOf("BR ") + 8).split("BR ")[1]);
                        map.put(elements[5], buffer.substring(buffer.indexOf("GAZP "), buffer.indexOf("GAZP ") + 10).split("GAZP ")[1]);
                        map.put(elements[6], buffer.substring(buffer.indexOf("GOLD "), buffer.indexOf("GOLD ") + 11).split("GOLD ")[1]);
                        map.put(elements[7], buffer.substring(buffer.indexOf("MOEX "), buffer.indexOf("MOEX ") + 10).split("MOEX ")[1]);
                        map.put(elements[8], buffer.substring(buffer.indexOf("MIX "), buffer.indexOf("MIX ") + 10).split("MIX ")[1]);
                        map.put(elements[9], "0");
                        map.put(elements[10], buffer_2.substring(buffer_2.indexOf("Последняя сделка "), buffer_2.indexOf("Последняя сделка ") + 23).split(" ")[2] +
                                buffer_2.substring(buffer_2.indexOf("Последняя сделка "), buffer_2.indexOf("Последняя сделка ") + 23).split(" ")[3]);
                    } catch (Exception e) {
                        System.out.println(e);
                        text.setText("Ошибка парсинга цены и наполнение карты");
                        continue;
                    }
                    //************////////////////////
                    price = String.valueOf((int) Double.parseDouble(map.get((String) dialog.cbFirst.getSelectedItem())));
                    price_2 = String.valueOf((int) Double.parseDouble(map.get((String) dialog.cbSecond.getSelectedItem())));
                    price_3 = String.valueOf((int) Double.parseDouble(map.get((String) dialog.cbThird.getSelectedItem())));
                    //price=buffer.substring(buffer.indexOf("Si RUB "), buffer.indexOf("fSi") + 9).split("fSi ")[1];
                    //System.out.println(price);
                    System.out.println((String) dialog.cbFirst.getSelectedItem());
                    System.out.println(map.get((String) dialog.cbFirst.getSelectedItem()));
                    System.out.println("//**************************//");
                    System.out.println((String) dialog.cbSecond.getSelectedItem());
                    System.out.println(map.get((String) dialog.cbSecond.getSelectedItem()));
                    System.out.println("//**************************//");
                    System.out.println((String) dialog.cbThird.getSelectedItem());
                    System.out.println(map.get((String) dialog.cbThird.getSelectedItem()));
                    System.out.println("//**************************//");
                    if (work == false && size == 0) {
                        text.setText("TS_1:" + ((String) dialog.cbFirst.getSelectedItem()) +
                                map.get((String) dialog.cbFirst.getSelectedItem()) +
                                " " + "TS_2:" + ((String) dialog.cbSecond.getSelectedItem()) +
                                map.get((String) dialog.cbSecond.getSelectedItem()) + "TS_3:" + ((String) dialog.cbThird.getSelectedItem()) +
                                map.get((String) dialog.cbThird.getSelectedItem()) + " 10 sec delay");
                    }
                    buffer = null;
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        }).start();
    }

    volatile String price;
    volatile String price_2, price_3;

    Rectangle selection;
    Point anchor;
    boolean work;
    int b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, m = 0;
    String condtext_1, condtext_2;
    Test test;

    JButton trade1;
    JButton trade2;
    JButton trade3;
    boolean append;
    boolean append2;
    static int ID = 0;
    //Список инструментов
    public String[] elements = null;

    Trade tr;
    Thread mythread;


    public static void main(String[] args) {
        int j = 0;
        while (j != 1) {
            try {
                Parse parse = new Parse();
                parse.createGUI();

                //Очистка файлов перед запуском*****************
                try {
                    BufferedWriter writer_1 = new BufferedWriter(new FileWriter("tro.tro"));
                    writer_1.write("");
                    writer_1.flush();
                    writer_1.close();
                    BufferedWriter writer_2 = new BufferedWriter(new FileWriter("trade.tri", false));
                    writer_2.write("");
                    writer_2.flush();
                    writer_2.close();
                    BufferedWriter writer_3 = new BufferedWriter(new FileWriter("trr.trr"));
                    writer_3.write("");
                    writer_3.flush();
                    writer_3.close();


                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                //*****************************************
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < parse.elements.length; i++) {
                    parse.dialog.cbModel.addElement((String) parse.elements[i]);
                    parse.dialog.cbModel_2.addElement((String) parse.elements[i]);
                    parse.dialog.cbModel_3.addElement((String) parse.elements[i]);
                }
                parse.dialog.cbModel.setSelectedItem((String) (parse.dialog.typesave));
                parse.dialog.cbModel_2.setSelectedItem((String) (parse.dialog.typesave_2));
                parse.dialog.cbModel_3.setSelectedItem((String) (parse.dialog.typesave_3));
                parse.dialog.create();
                parse.dialog.setVisible(false);
                j++;
                System.out.println("Cтатус активности кнопок " + parse.b1 + ";" + parse.b8);
                System.out.println(parse.dialog.getMail().split("@")[1]);
            } catch (Exception e) {
                System.out.println("Заполнение");
                BufferedWriter writer = null;
                try {
                    writer = new BufferedWriter(new FileWriter("auth.txt"));
                    writer.write("9*********\n" +
                            "**********\n" +
                            "**********\n" +
                            "parsesignal@yandex.ru\n" +
                            "**********\n" +
                            "152360\n" +
                            "28282\n" +
                            "ZIZ8\n" +
                            "1\n" +
                            "300\n" +
                            "false\n" +
                            "false\n" +
                            "false\n" +
                            "-2103578\n" +
                            "-2103578\n" +
                            "-2103578\n" +
                            "-2103578\n" +
                            "-2103578\n" +
                            "-2103578\n" +
                            "-2103578\n" +
                            "-2103578\n" +
                            "GOLD RUB \n" +
                            "null\n" +
                            "null\n" +
                            "1\n" +
                            "0\n" +
                            "0\n" +
                            "0\n" +
                            "0\n" +
                            "0\n" +
                            "0\n" +
                            "0\n" +
                            "0\n" +
                            "true\n" +
                            "SIU8\n" +
                            "1\n" +
                            "RTS RUB \n" +
                            "300\n" +
                            "123568\n" +
                            "0\n" +
                            "0\n" +
                            "0\n" +
                            "0\n" +
                            "0\n" +
                            "0\n" +
                            "0\n" +
                            "SIU8\n" +
                            "1\n" +
                            "1000\n" +
                            "-2103578\n" +
                            "-2103578\n" +
                            "-2103578\n" +
                            "RTS RUB \n");
                    writer.flush();
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    //Запись текста в файл
    void write_file(String path, String code, String id_poz, String poz) {
        BufferedWriter wr = null;
        try {
            wr = new BufferedWriter(new FileWriter(path, false));
            wr.write(code + " " + id_poz + " " + poz);
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //    Чтения  текста из файла
    int read_file(String path) {
        BufferedReader rd = null;
        String[] x = null;
        int y = 0;
        try {
            rd = new BufferedReader(new FileReader(path));
            x = rd.readLine().split(" ");
            rd.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println(x.length);
//        if(x.length%2==0){
//        y=Arrays.stream(Stream.of(x).mapToInt(Integer::parseInt).toArray()).sum();
//        }
//        else {
        System.out.println(Arrays.toString(x));
        x_toint = Stream.of(x).mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < Stream.of(x).mapToInt(Integer::parseInt).toArray().length-1; i++) {
            y+=x_toint[i]+x_toint[i+1];
            System.out.println(y);
        }
        if (y < 0)
            equity.setBackground(Color.red);
        else if (y > 0) {
            equity.setBackground(Color.green);
        } else if (y == 0) {
            equity.setBackground(Color.BLUE);
        }
        return y;
    }


    //Метод:делаем скан и направляем на почту
    void createScreenformail(){
        Robot robot= null;
        try {
            robot = new Robot();
            screen = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(screen, "JPG", new File("screen.jpg"));
        } catch (AWTException e) {
            System.out.println("Ошибка в разделе сканирования экрана для отправки по электронной почте");
        } catch (IOException e) {
            System.out.println("Ошибка в разделе записи скана для отправки по электронной почте");
        }

    }
    //****************************************************************//
    void createGUI() {
//        Наполнение главного фрейма
        try {
            //UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
            //UIManager.setLookAndFeel("com.jtattoo.plaf.texture.TextureLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        dialog=new Dialog(frame,"Settings");
        ImageIcon icon_2 = new ImageIcon("image_set.png");
        dialog.setIconImage(icon_2.getImage());
        dialog_2=new Dialog(frame);
        /**Инициализация почтового модуля
         *
         */
        test=new Test();
        test.save();
        /*--------------------------------------------

         */

        frame = new JFrame("Parse_Signal_v.2.4 at 16.02.2019");
        ImageIcon icon = new ImageIcon("image.png");
        frame.setIconImage(icon.getImage());
        frame.setBounds(1000,2,350,380);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        //****Подтверждение закрытия окна**//////////////////
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            public void windowClosing(WindowEvent event) {
                Object[] options = { "Да", "Нет" };
                int n = JOptionPane
                        .showOptionDialog(event.getWindow(), "Выйти из программы?",
                                "Message", JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, options,
                                options[0]);
                if (n == 0) {
                    dialog.save();
                    event.getWindow().setVisible(false);
                    System.exit(0);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        //****Подтверждение закрытия окна**//////////////////
        //Меню бара
        My_JMenuBar menu=new My_JMenuBar();
        frame.setJMenuBar(menu);
        frame.getContentPane().setLayout(new BorderLayout());
        text = new JTextField();
        text.setEditable(false);
        frame.getContentPane().add(text, BorderLayout.SOUTH);
        tabs = new JTabbedPane(JTabbedPane.BOTTOM);
        frame.getContentPane().add(tabs, BorderLayout.CENTER);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        frame.getContentPane().add(panel, BorderLayout.EAST);
//        Наполнение вкладки TAB  и подключение слушателей
        screenLabel = new MyLabel();
        screenLabel_2 = new JLabel();
        screenScroll = new JScrollPane(screenLabel);
        screenScroll_2 = new JScrollPane(screenLabel_2);
        tabs.add("FullScreen", screenScroll);
        tabs.add("Screen_Area", screenScroll_2);

        tabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JScrollPane pane = (JScrollPane) ((JTabbedPane) e.getSource()).getSelectedComponent();
            }
        });
        tabs.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // Определяем индекс выделенной мышкой вкладки
                int idx = ((JTabbedPane) e.getSource()).indexAtLocation(e.getX(), e.getY());
                text.setText("Выбрана вкладка " + idx);
            }
        });
//        Кнопка скрина и слушатель для нее
        butscreen = new JButton("SCREEN  ");
        start = new JButton("START     ");
        start.addMouseListener(new ListnerButtonStart());
        stop = new JButton("STOP       ");
        stop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                work = false;
                //tr.Order_H();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                try {
                    BufferedWriter writer_1 = new BufferedWriter(new FileWriter("tro.tro"));
                    writer_1.write("");
                    writer_1.flush();
                    writer_1.close();
                    BufferedWriter writer_2=new BufferedWriter(new FileWriter("trade.tri",append=false));
                    writer_2.write("");
                    writer_2.flush();
                    writer_2.close();
                    BufferedWriter writer_3=new BufferedWriter(new FileWriter("trr.trr"));
                    writer_3.write("");
                    writer_3.flush();
                    writer_3.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                size=0;
                status=0;
                status_2=0;
                status_3=0;
                status_4=0;
                status_5=0;
                //ID=0;
                text.setText("Работа остановлена, файлы tri,tro,trr очищены!");
                text.setBackground(Color.WHITE);
            }
        });
        butscreen.addMouseListener(new ScreenLister());
        panel.add(butscreen);
        panel.add(start);
        panel.add(stop);
        //Формируем список доступных инструменто
        elements= new String[] {"STOCK ","Si RUB ", "RTS RUB ", "LKON RUB ",
                "BR RUB ", "GAZP RUB ","GOLD RUB ","MOEX RUB ","MIX RUB ","STOCK ","SBER RUB "};
        //*************************************************************************//
        buy = new JButton( "Buy_1   ");
        sell = new JButton("Sell_1   ");
        hold = new JButton("Hold_1  ");
        buy_2=new JButton( "Buy_2   ");
        sell_2=new JButton("Sell_2   ");
        hold_2=new JButton("Hold_2  ");
        buy_3=new JButton( "Buy_3   ");
        sell_3=new JButton("Sell_3   ");
        hold_3=new JButton("Hold_3  ");
        cond_1=new JButton("cond_1   ");
        cond_2=new JButton("cond_2   ");
        option=new JButton("SET        ");
        trade1=new JButton("  B  ");
        instrument=new JButton(dialog.seccodetext.getText());
        instrument.setForeground(Color.BLUE);
        trade2=new JButton("  S  ");
        trade3=new JButton("  H  ");
        equity=new JButton("Equity");
        //martin=new JButton("MaRtiN...");
        def_cor=new Checkbox();
        tr=new Trade();
//        Cоздание диалогового окна
        option.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dialog.create();
            }
        });
        info=new JButton("INFO       ");
        //Ссылка в ИНФО
        JEditorPane ep = new JEditorPane("text/html","C вопросам и предложениями обращаться <br> по e-mail : parsesignal@yandex.ru <br> " +
                "Финансовая поддержка проекта:  <a href=\"http://www.donationalerts.ru/r/246925\">RUB/USD</a>\n<br>_________Parse_Signal_v.2.3__________"
        );

        // handle link events
        ep.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                try {
                    if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
                        Desktop.getDesktop().browse(new URI("http://www.donationalerts.ru/r/246925"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });
        ep.setEditable(false);
        ep.setBackground(panel.getBackground());
        //Ссылка в ИНФО
        /**************************************************/
        info.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(frame,ep);
            }
        });
        panel.add(buy);
        buy.addMouseListener(new DefinedColor());
        sell.addMouseListener(new DefinedColor());
        hold.addMouseListener(new DefinedColor());
        buy_2.addMouseListener(new DefinedColor());
        sell_2.addMouseListener(new DefinedColor());
        hold_2.addMouseListener(new DefinedColor());
        buy_3.addMouseListener(new DefinedColor());
        sell_3.addMouseListener(new DefinedColor());
        hold_3.addMouseListener(new DefinedColor());
        cond_1.addMouseListener(new DefinedColor());
        cond_2.addMouseListener(new DefinedColor());
        //*********Установка цвета кнопок******************************
        buy.setBackground(new Color(Integer.parseInt(dialog.bcol_1)));
        sell.setBackground(new Color(Integer.parseInt(dialog.scol_1)));
        hold.setBackground(new Color(Integer.parseInt(dialog.hcol_1)));
        buy_2.setBackground(new Color(Integer.parseInt(dialog.bcol_2)));
        sell_2.setBackground(new Color(Integer.parseInt(dialog.scol_2)));
        hold_2.setBackground(new Color(Integer.parseInt(dialog.hcol_2)));
        buy_3.setBackground(new Color(Integer.parseInt(dialog.bcol_3)));
        sell_3.setBackground(new Color(Integer.parseInt(dialog.scol_3)));
        hold_3.setBackground(new Color(Integer.parseInt(dialog.hcol_3)));
        cond_1.setBackground(new Color(Integer.parseInt(dialog.ccol_1)));
        cond_2.setBackground(new Color(Integer.parseInt(dialog.ccol_2)));
        //*************************************************************
        panel.add(sell);
        panel.add(hold);
//            panel.add(buy_2);
//            panel.add(buy_2);
//            panel.add(sell_2);
//            panel.add(hold_2);
//            panel.add(buy_3);
//            panel.add(sell_3);
//            panel.add(hold_3);
//            panel.add(cond_1);
//            panel.add(cond_2);

        panel.add(option);
//        panel.add(info);
        panel.add(instrument);
        instrument.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(m==0){m+=2;}else{m++;}
                if(m==4){m=1;}
                if(m==3){
                    instrument.setText(dialog.seccodetext_3.getText());
                }
                else if(m==2){
                    instrument.setText(dialog.seccodetext_2.getText());
                }
                else if(m==1){
                    instrument.setText(dialog.seccodetext.getText());
                }
                System.out.println(m);
            }
        });
        panel.add(trade1);
        trade1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // work=false;
               /* if(mythread!=null) {
                    try {
                        mythread.join();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                */
                tr.Order_B();
                //status=1;
                size=size+Integer.parseInt(dialog.getQuantitytext());
                text.setText("Покупка");
                text.setText("Лотов в сделке: " +size+" Кол-во сделок "+ID);
            }
        });
        panel.add(trade2);

        trade2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                /*work=false;
                text.setText("Продажа");
                if(mythread!=null) {
                    try {
                        mythread.join();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                */
                tr.Order_S();
                //status=-1;
                size=size-Integer.parseInt(dialog.getQuantitytext());
                text.setText("Продажа");
                text.setText("Лотов в сделке: " +size+" Кол-во сделок "+ID);

            }
        });
        panel.add(trade3);
        trade3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tr.Order_H();
                text.setText("Лотов в сделке: " +size+" Кол-во сделок "+ID);
            }
        });
        panel.add(equity);
        equity.setBackground(new Color(223, 230, 230));
        equity.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                equity.setText(String.valueOf(read_file("data.txt")));
                new LineChart1("Equity").create_graphics();
            }
        });
        /*panel.add(martin);
        martin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dialog_2.create_2();
            }
        });*/
        //panel.add(def_cor);
        getPrice();
        frame.setVisible(true);
    }

    class ScreenLister extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            frame.setVisible(false);
            text.setText("Cделан скрин");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            Robot robot = null;
            try {
                robot = new Robot();
            } catch (AWTException e1) {
                e1.printStackTrace();
            }
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            screen = robot.createScreenCapture(new Rectangle(screenSize));
            image = new ImageIcon(screen);
            screenLabel.setIcon(image);
            frame.setVisible(true);
        }
    }

    class MyLabel extends JLabel implements MouseMotionListener, MouseListener {

        public MyLabel() {
            addMouseListener(this);
            addMouseMotionListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (selection != null) {
                Graphics2D g2d = (Graphics2D) g;
                BasicStroke pen = new BasicStroke(2); //толщина линии 20
                g2d.setStroke(pen);
                g2d.setColor(Color.red);
                g2d.draw(selection);
            }
        }

        public void mousePressed(MouseEvent e) {
            anchor = e.getPoint();
            selection = new Rectangle(anchor);
        }

        public void mouseDragged(MouseEvent e) {
            selection.setBounds((int) Math.min(anchor.x, e.getX()), (int) Math.min(anchor.y, e.getY()),
                    (int) Math.abs(e.getX() - anchor.x), (int) Math.abs(e.getY() - anchor.y));
            repaint();
        }

        public void mouseReleased(MouseEvent e) {
            repaint();
            text.setText("Область для скана: " + "Ось X " + selection.x + " " + "Ось Y " + selection.y + " " +
                    "Высота" + selection.getHeight() + " " + "Ширина" + selection.getWidth());
        }

        // unused
        public void mouseMoved(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }

    class ListnerButtonStart extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            frame.setBounds(1000,2,350,470);
            Runnable run = new MyRunnable();
            mythread = new Thread(run);
            mythread.start();
        }
    }

    public class MyRunnable implements Runnable {
        int count = 1;
        int position=0;
        int position_2=0;
        int position_3=0;
        int position_4=0;
        int position_5=0;
        //Переменные условий, учитывающие изменение координат цвета*********//
        int numpixels_1=0;
        int valuepix_1=0;
        int numpixels_2=0;
        int valuepix_2=0;
        int numpixels_3=0;
        int valuepix_3=0;
        //int count_unique=0;
        /*int numpixels_3=0;
        int valuepix_3=0;
        int numpixels_4=0;
        int valuepix_4=0;*/
        //Переменные условий, учитывающие изменение координат цвета*********//
  /*      String quantitydef_1=dialog.getQuantitytext();
        String quantitydef_2=dialog.getQuantitytext_2();*/


        @Override
        public void run() {
            work = true;
            //Заглушка
            if(Calendar.getInstance().get(Calendar.YEAR)==2100)
                JOptionPane.showMessageDialog(new Frame("Message"),"Обратитесь к разработчику:\n parsesignal@yandex.ru");
            //*************************************************************************************************************************//
            while (work != false&&Calendar.getInstance().get(Calendar.YEAR)!=2100) {
                Robot robot = null;
                try {
                    robot = new Robot();
                } catch (AWTException e1) {
                    e1.printStackTrace();
                }
                BufferedImage buf=null;
                try{buf = robot.createScreenCapture(new Rectangle(selection.x, selection.y, selection.width, selection.height));

                    image2 = new ImageIcon(buf);
                } catch (NullPointerException e){
                    JOptionPane.showMessageDialog(frame,"Выделите область и нажмите кнопку START");
                }
                screenLabel_2.setIcon(image2);
                text.setText("Скрин: " + count +" ID: "+ID+" TS_1"+":"+position*Integer.parseInt(dialog.getQuantitytext())+";"+" TS_2"+":"+position_2*Integer.parseInt(dialog.getQuantitytext_2())+";"+" TS_3"+":"+position_5*Integer.parseInt(dialog.getQuantitytext_3()));
                int[] pixels = copyFromBufferedImage(buf);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    text.setText("Прерван поток!");
                }

              /*  if (uniqueCount(pixels) == 0) {
                    count_unique++;
                    if (count_unique >=500) {
                        test.sendSignal("FATAL ERROR", "Некорректное сканироваание: в сканируемом изображении не найденно ни одного дубликата");
                        //System.out.println("Некорректное сканирование: в сканируемом изображении не найденно ни одного дубликата");
                    }
                }else{count_unique=0;}*/
                count++;
                searchpixel(pixels);
                /*Скрин в файл
                save_screen();
                */
            }
        }

        private void searchpixel(int[] pixels) {
            //list_1.forEach(System.out::println);
            //Режим мартингейла************************************Модуль MaRtiN*****************************//
//            if(dialog_2.mart.getState()==true){
//                for(int i=0;i<pixels.length;i++ ) {
//                    if (pixels[i] == (dialog_2.ris_1.getBackground().getRGB() & 0xFFFFFF)) {
//                        valuepix_3=i;
//                    }
//                    else if (pixels[i] == (dialog_2.ris_2.getBackground().getRGB() & 0xFFFFFF)) {
//                        valuepix_4=i;
//                    }
//                    else if (pixels[i] == (dialog_2.def_1.getBackground().getRGB() & 0xFFFFFF)) {
//                        dialog.quantitytext.setText(quantitydef_1);
//                    }
//                    else if (pixels[i] == (dialog_2.def_2.getBackground().getRGB() & 0xFFFFFF)) {
//                        dialog.quantitytext.setText(quantitydef_2);
//                    }
//                }
//                //Сигналы модуля мартингейла***************************************//
//                if (b9==9&&numpixels_3!=valuepix_3){
//                    numpixels_3=valuepix_3;
//                    dialog.quantitytext.setText((String.valueOf(Integer.parseInt(quantitydef_1)+Integer.parseInt(dialog_2.coefficient.getText()))));
//                }
//                if (b10==10&&numpixels_4!=valuepix_4){
//                    numpixels_4=valuepix_4;
//                    dialog.quantitytext_2.setText((String.valueOf(Integer.parseInt(quantitydef_2)+Integer.parseInt(dialog_2.coefficient.getText()))));
//                }
//            }
            //Режим мартингейла*****************************************************************//
            for(int i=0;i<pixels.length;i++ ) {
                if (pixels[i] == (buy.getBackground().getRGB() & 0xFFFFFF)) {
                    position = 1;
                    //System.out.println("Нашёл сигнал покупки");
                } else if (pixels[i] == (sell.getBackground().getRGB() & 0xFFFFFF)) {
                    position = -1;
                    //System.out.println("Нашёл сигнал продажи");
                } else if (pixels[i] == (hold.getBackground().getRGB() & 0xFFFFFF)) {
                    position = 0;
                } else if (pixels[i] == (buy_2.getBackground().getRGB() & 0xFFFFFF)) {
                    position_2 = 1;
                    //list_2.add(Integer.parseInt(price_2)*(-1));
                } else if (pixels[i] == (sell_2.getBackground().getRGB() & 0xFFFFFF)) {
                    position_2 = -1;
                    //list_1.add(Integer.parseInt(price_2));
                } else if (pixels[i] == (hold_2.getBackground().getRGB() & 0xFFFFFF)) {
                    position_2 = 0;
                } else if (pixels[i] == (buy_3.getBackground().getRGB() & 0xFFFFFF)) {
                    position_5 = 1;
                    //list_3.add(Integer.parseInt(price_3)*(-1));
                } else if (pixels[i] == (sell_3.getBackground().getRGB() & 0xFFFFFF)) {
                    position_5 = -1;
                    // list_1.add(Integer.parseInt(price_3));
                } else if (pixels[i] == (hold_3.getBackground().getRGB() & 0xFFFFFF)) {
                    position_5 = 0;
                } else if (pixels[i] == (cond_1.getBackground().getRGB() & 0xFFFFFF)) {
                    position_3 = 1;
                    valuepix_1 = i;
                } else if (pixels[i] == (cond_2.getBackground().getRGB() & 0xFFFFFF)) {
                    position_4 = 1;
                    valuepix_2 = i;
                }
            }
            if  (position==1&&status!=1&&b1==1) {
                //list_1.add(Integer.parseInt(price)*(-1));
                //text.setBackground(buy.getBackground());
                if(dialog.isSend_phone()==true) {
                    new SMS().sendSms(dialog.getPhone(), "TS_1: СИГНАЛ НА ПОКУПКУ "+(String)dialog.cbFirst.getSelectedItem()+" "+price+" "+new Date(), "TEST-SMS", dialog.getLogin(), dialog.getPassword());
                }
                if(dialog.isSend_trade()==true){
                    tr.Order_Buy();
                }
                if(dialog.isSend_mail()==true){
                    test.sendSignal("BUY","TS_1: Buy in signal at price "+(String)dialog.cbFirst.getSelectedItem()+" "+price+" "+new Date());
                }
                status = 1;
                if(dialog.isSend_trade()==false){
                    ID++;
                    write_file("poz.txt",dialog.getSeccodetext(),String.valueOf(ID),String.valueOf(dialog.getQuantitytext()));
                }
                else {
                    write_file("poz.txt",dialog.getSeccodetext(),String.valueOf(ID),String.valueOf(dialog.getQuantitytext()));
                }
            }
            else if  (position==-1&&status!=-1&&b2==2) {
                //list_1.add(Integer.parseInt(price));
                //text.setBackground(sell.getBackground());
                if(dialog.isSend_phone()==true) {
                    new SMS().sendSms(dialog.getPhone(), "TS_1: СИГНАЛ НА ПРОДАЖУ " +(String)dialog.cbFirst.getSelectedItem()+" "+ price+" "+new Date(), "TEST-SMS", dialog.getLogin(), dialog.getPassword());
                }
                if(dialog.isSend_trade()==true){
                    tr.Order_Sell();
                }
                if(dialog.isSend_mail()==true){
                    test.sendSignal("SELL","TS_1: Sell in signal at price "+(String)dialog.cbFirst.getSelectedItem()+" "+price+" "+new Date());
                }
                status=-1;
                if(dialog.isSend_trade()==false){
                    ID++;
                    write_file("poz.txt",dialog.getSeccodetext(),String.valueOf(ID),String.valueOf(Integer.parseInt(dialog.getQuantitytext())*(-1)));

                }
                else {
                    write_file("poz.txt",dialog.getSeccodetext(),String.valueOf(ID),String.valueOf(Integer.parseInt(dialog.getQuantitytext())*(-1)));
                }
            }
            else if  (position==0&&status!=0&&b3==3) {
                // text.setBackground(hold.getBackground());
                if(dialog.isSend_phone()==true) {
                    new SMS().sendSms(dialog.getPhone(), "TS_1: БЕЗ ПОЗИЦИИ (ВНЕ РЫНКА) " +(String)dialog.cbFirst.getSelectedItem()+" "+price+" "+new Date(), "TEST-SMS", dialog.getLogin(), dialog.getPassword());
                }
                if(dialog.isSend_trade()==true){
                    tr.Order_HOLD();
                }
                if(dialog.isSend_mail()==true){
                    test.sendSignal("HOLD","TS_1: HOLD in signal at price "+(String)dialog.cbFirst.getSelectedItem()+" "+price+" "+new Date());
                }
                status=0;
            }
            //*********Дополнительные сигналы************************\
            if  (position_2==1&&status_2!=1&&b4==4) {
                if(dialog.isSend_phone()==true) {
                    new SMS().sendSms(dialog.getPhone(), "TS_2: СИГНАЛ НА ПОКУПКУ " +(String)dialog.cbSecond.getSelectedItem()+" "+price_2+" "+new Date(), "TEST-SMS", dialog.getLogin(), dialog.getPassword());
                }
                if(dialog.isSend_trade()==true){
                    tr.Order_Buy_2();
                }
                if(dialog.isSend_mail()==true){
                    test.sendSignal("BUY","TS_2: Buy in signal at price "+(String)dialog.cbSecond.getSelectedItem()+" "+price_2+" "+new Date());
                }
                status_2 = 1;
            }
            else if  (position_2==-1&&status_2!=-1&&b5==5) {
                //text.setBackground(sell.getBackground());
                if(dialog.isSend_phone()==true) {
                    new SMS().sendSms(dialog.getPhone(), "TS_2: СИГНАЛ НА ПРОДАЖУ "+(String)dialog.cbSecond.getSelectedItem()+" " + price_2+" "+new Date(), "TEST-SMS", dialog.getLogin(), dialog.getPassword());
                }
                if(dialog.isSend_trade()==true){
                    tr.Order_Sell_2();
                }
                if(dialog.isSend_mail()==true){
                    test.sendSignal("SELL","TS_2: Sell in signal at price "+(String)dialog.cbSecond.getSelectedItem()+" "+price_2+" "+new Date());
                }
                status_2=-1;
            }
            else if  (position_2==0&&status_2!=0&&b6==6) {
                //text.setBackground(hold.getBackground());
                if(dialog.isSend_phone()==true) {
                    new SMS().sendSms(dialog.getPhone(), "TS_2: БЕЗ ПОЗИЦИИ (ВНЕ РЫНКА) "+(String)dialog.cbSecond.getSelectedItem()+" "+price_2+" "+new Date(), "TEST-SMS", dialog.getLogin(), dialog.getPassword());
                }
                if(dialog.isSend_trade()==true){
                    tr.Order_HOLD_2();
                }
                if(dialog.isSend_mail()==true){
                    test.sendSignal("HOLD","TS_2: HOLD in signal at price "+(String)dialog.cbSecond.getSelectedItem()+" "+price_2+" "+new Date());
                }
                status_2=0;

            }
            //Сигналы третьей сиcтемы*****************************//
            if  (position_5==1&&status_5!=1&&b13==13) {
                if(dialog.isSend_phone()==true) {
                    new SMS().sendSms(dialog.getPhone(), "TS_3: СИГНАЛ НА ПОКУПКУ " +(String)dialog.cbThird.getSelectedItem()+" "+price_3+" "+new Date(), "TEST-SMS", dialog.getLogin(), dialog.getPassword());
                }
                if(dialog.isSend_trade()==true){
                    tr.Order_Buy_3();
                }
                if(dialog.isSend_mail()==true){
                    test.sendSignal("BUY","TS_3: Buy in signal at price "+(String)dialog.cbThird.getSelectedItem()+" "+price_3+" "+new Date());
                }
                status_5 = 1;
            }
            else if  (position_5==-1&&status_5!=-1&&b14==14) {
                //text.setBackground(sell.getBackground());
                if(dialog.isSend_phone()==true) {
                    new SMS().sendSms(dialog.getPhone(), "TS_3: СИГНАЛ НА ПРОДАЖУ "+(String)dialog.cbThird.getSelectedItem()+" " + price_3+" "+new Date(), "TEST-SMS", dialog.getLogin(), dialog.getPassword());
                }
                if(dialog.isSend_trade()==true){
                    tr.Order_Sell_3();
                }
                if(dialog.isSend_mail()==true){
                    test.sendSignal("SELL","TS_3: Sell in signal at price "+(String)dialog.cbThird.getSelectedItem()+" "+price_3+" "+new Date());
                }
                status_5=-1;
            }
            else if  (position_5==0&&status_5!=0&&b15==15) {
                //text.setBackground(hold.getBackground());
                if(dialog.isSend_phone()==true) {
                    new SMS().sendSms(dialog.getPhone(), "TS_3: БЕЗ ПОЗИЦИИ (ВНЕ РЫНКА) "+(String)dialog.cbThird.getSelectedItem()+" "+price_3+" "+new Date(), "TEST-SMS", dialog.getLogin(), dialog.getPassword());
                }
                if(dialog.isSend_trade()==true){
                    tr.Order_HOLD_3();
                }
                if(dialog.isSend_mail()==true){
                    test.sendSignal("HOLD","TS_3: HOLD in signal at price "+(String)dialog.cbThird.getSelectedItem()+" "+price_3+" "+new Date());
                }
                status_5=0;

            }
            //********************************************************************************//
            //***Сигналы условий***//
            if  (position_3==1&&b7==7&&numpixels_1!=valuepix_1) {
                if (dialog.isSend_phone() == true) {
                    new SMS().sendSms(dialog.getPhone(), "Cond_1:"+condtext_1+" " + price + " " + new Date(), "TEST-SMS", dialog.getLogin(), dialog.getPassword());
                }
                if (dialog.isSend_mail() == true) {
                    test.sendSignal("Condition_1", "Cond_1:" +condtext_1+" "+ price + " " + new Date());
                }
                //status_3=1;
                numpixels_1=valuepix_1;
            }

            if (position_4==1&&b8==8&&numpixels_2!=valuepix_2) {
                if (dialog.isSend_phone() == true) {
                    new SMS().sendSms(dialog.getPhone(), "Cond_2:"+condtext_2+" " + price + " " + new Date(), "TEST-SMS", dialog.getLogin(), dialog.getPassword());
                }
                if (dialog.isSend_mail() == true) {
                    test.sendSignal("Condition_2", "Cond_2:" +condtext_2+" "+ price + " " + new Date());
                }
                //status_4=1;
                numpixels_2=valuepix_2;
            }
            //***Сигналы условий***//
            //*********Дополнительные сигналы************************\

        }
        /*
        private void save_screen(){
            Robot robot = null;
            try {
                robot = new Robot();
            } catch (AWTException e1) {
                e1.printStackTrace();
            }

            BufferedImage bi =robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                 File img=new File("screen_"+count+".png");
            try {
                ImageIO.write(bi,"png",img);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        */
        public int uniqueCount(int[] array) {
            int countUnique = 0;
            for (int i = 0; i < array.length-1; i++) {
                if(array[i]!=array[i+1]){
                    countUnique++;
                }
            }

            return countUnique;
        }
        private int[] copyFromBufferedImage(BufferedImage bi) {
            int[] pict = new int[bi.getHeight() * bi.getWidth()];
            for (int i = 0; i < bi.getWidth(); i++)
                for (int j = 0; j < bi.getHeight(); j++)
                    pict[i * bi.getHeight() + j] = bi.getRGB(i, j) & 0xFFFFFF; // 0xFFFFFF: записываем только 3 младших байта RGB
            return pict;
        }
    }

    class DefinedColor extends MouseAdapter {

        JButton button;

        @Override
        public void mouseClicked(MouseEvent e) {
            button = (JButton) e.getSource();
            text.setText("Определяем цвет");
            if(button.getText().trim().equals("Buy_1")){
                b1=0; }
            else if(button.getText().trim().equals("Sell_1"))
                b2=0;
            else if(button.getText().trim().equals("Hold_1"))
                b3=0;
            else if(button.getText().trim().equals("Buy_2"))
                b4=0;
            else if(button.getText().trim().equals("Sell_2"))
                b5=0;
            else if(button.getText().trim().equals("Hold_2"))
                b6=0;
            else if(button.getText().trim().equals("Buy_3"))
                b13=0;
            else if(button.getText().trim().equals("Sell_3"))
                b14=0;
            else if(button.getText().trim().equals("Hold_3"))
                b15=0;
            else if(button.getText().trim().equals("cond_1"))
                b7=0;
            else if(button.getText().trim().equals("cond_2"))
                b8=0;
            /*else if(button.getText().trim().equals("TS_1-новый объём"))
                b9=0;
            else if(button.getText().trim().equals("TS_2-новый объём"))
                b10=0;
            else if(button.getText().trim().equals("TS_1-начальный объём"))
                b11=0;
            else if(button.getText().trim().equals("TS_2-начальный объём"))
                b12=0;*/
            screenLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            screenLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Robot robot = null;
                    try {
                        robot = new Robot();
                    } catch (AWTException e1) {
                        e1.printStackTrace();
                    }
                    Color color = robot.getPixelColor(e.getPoint().x, e.getPoint().y);
                    text.setText(color.toString());
                    if(button.getText().trim().equals("Buy_1")&&b1!=1) {
                        buy.setBackground(color);
                        b1=1;
                    }
                    else if(button.getText().trim().equals("Sell_1")&&b2!=2){
                        sell.setBackground(color);
                        b2=2;
                    }
                    else if(button.getText().trim().equals("Hold_1")&&b3!=3) {
                        hold.setBackground(color);
                        b3=3;
                    }
                    else if(button.getText().trim().equals("Buy_2")&&b4!=4) {
                        buy_2.setBackground(color);
                        b4=4;
                    }
                    else if(button.getText().trim().equals("Sell_2")&&b5!=5){
                        sell_2.setBackground(color);
                        b5=5;
                    }
                    else if(button.getText().trim().equals("Hold_2")&&b6!=6) {
                        hold_2.setBackground(color);
                        b6=6;
                    }
                    else if(button.getText().trim().equals("Buy_3")&&b13!=13) {
                        buy_3.setBackground(color);
                        b13=13;
                    }
                    else if(button.getText().trim().equals("Sell_3")&&b14!=14){
                        sell_3.setBackground(color);
                        b14=14;
                    }
                    else if(button.getText().trim().equals("Hold_3")&&b15!=15) {
                        hold_3.setBackground(color);
                        b15=15;
                    }
                    else if(button.getText().trim().equals("cond_1")&&b7!=7){
                        cond_1.setBackground(color);
                        b7=7;
                        condtext_1=JOptionPane.showInputDialog(new Frame(),"Укажите событие");
                        System.out.println(condtext_1);
                    }
                    else if(button.getText().trim().equals("cond_2")&&b8!=8) {
                        cond_2.setBackground(color);
                        b8=8;
                        condtext_2=JOptionPane.showInputDialog(new Frame(),"Укажите событие");
                        System.out.println(condtext_2);
                    }
                    /*else if(button.getText().trim().equals("TS_1-новый объём")&&b9!=9) {
                        dialog_2.ris_1.setBackground(color);
                        b9=9;
                        dialog_2.setVisible(true);
                    }
                    else if(button.getText().trim().equals("TS_2-новый объём")&&b10!=10) {
                        dialog_2.ris_2.setBackground(color);
                        b10=10;
                        dialog_2.setVisible(true);
                    }
                    else if(button.getText().trim().equals("TS_1-начальный объём")&&b11!=11) {
                        dialog_2.def_1.setBackground(color);
                        b11=11;
                        dialog_2.setVisible(true);
                    }
                    else if(button.getText().trim().equals("TS_2-начальный объём")&&b12!=12) {
                        dialog_2.def_2.setBackground(color);
                        b12=12;
                        dialog_2.setVisible(true);
                    }*/
                }
            });
        }
    }
//******************************************************************************************************************************
    /** КОД ДЛЯ ОТПРАВКИ СМС СООБЩЕНИЙ
     *
     */
    public  class SMS {

        public SMS() {
        }

        private void sendSms(String phone, String text, String sender, String name, String password) {
            try {
                System.out.println(phone + " " + text + " " + sender + " " + name + " " + password);
                String authString = name + ":" + password;
                String authStringEnc = Base64.getEncoder().encodeToString(authString.getBytes());

                URL url = new URL("http", "api.smsfeedback.ru", 80, "/messages/v2/send/?phone=%2B" + phone + "&text=" + URLEncoder.encode(text, "UTF-8") + "&sender=" + sender);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setRequestProperty("Authorization", authStringEnc);
                InputStream is = urlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int numCharsRead;
                char[] charArray = new char[1024];
                StringBuffer sb = new StringBuffer();
                while ((numCharsRead = isr.read(charArray)) > 0) {
                    sb.append(charArray, 0, numCharsRead);
                }
                String result = sb.toString();

                System.out.println("*** BEGIN ***");
                System.out.println(result);
                System.out.println("*** END ***");

            } catch (MalformedURLException ex) {
                System.out.println(ex.toString());
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
        }
    }

//**************************************************************************************************************************
    /**КОД ДИАЛОГОВОГО ОКНА
     *
     */
    public  class Dialog extends JDialog {
        private    JTextField phone;
        private  JTextField mail;
        private    JTextField login;
        private  JTextField password;
        private  JTextField passmail;
        private JLabel text1;
        private JLabel text2;
        private JLabel text3;
        private JLabel text4;
        private JLabel text5;
        private Checkbox boxmail;
        private Checkbox screenmail;
        private Checkbox boxphone;
        private Checkbox boxtrade;
        private JButton button;
        private JButton button_1;
        private JTextField accounttext;
        private JTextField accounttext_2;
        private JTextField clientcodetext;
        private JTextField seccodetext;
        private JTextField seccodetext_2;
        private JTextField seccodetext_3;
        private JTextField quantitytext;
        private JTextField quantitytext_2;
        private JTextField quantitytext_3;
        private JTextField delta;
        private JTextField delta_2;
        private JTextField delta_3;
        //private JTextField uppertext;
        //private JTextField lowertext;
        private JLabel accountlab;
        private JLabel accountlab_2;
        private JLabel clientcodelab;
        private JLabel seccodelab;
        private JLabel quantitylab;
        private JLabel deltalab;
        //private JLabel upperlab;
        //private JLabel lowerlab;
        private JLabel type;
        private JLabel type_2;
        private JLabel type_3;
        private String bcol_1,scol_1,hcol_1,bcol_2,scol_2,hcol_2,ccol_1,ccol_2,typesave,typesave_2;
        private String typesave_3,bcol_3,scol_3,hcol_3;
        private JComboBox<String> cbFirst;
        private JComboBox<String> cbSecond;
        private JComboBox<String> cbThird;
        private DefaultComboBoxModel<String> cbModel;
        private DefaultComboBoxModel<String> cbModel_2;
        private DefaultComboBoxModel<String> cbModel_3;
        private HashMap<String,String> map_delta;
        private JButton form_1;
        private JButton form_2;
        private JButton form_3;
        private JTextArea field;
        private JTextField coefficient;
        private Checkbox mart;




        public Dialog(Frame owner, String title) {
            super(owner, title);
            text1=new JLabel("Phone number ");
            phone=new JTextField(15);
            text2=new JLabel("Mail (Yandex,Gmail) ");
            text5=new JLabel("Password e-mail");
            text3=new JLabel("Mobile Login ");
            text4=new JLabel("Phone Passwod ");
            login=new JTextField(15);
            password=new JTextField(15);
            passmail=new JTextField(15);
            mail=new JTextField(15);
            boxmail=new Checkbox("E-mail   ");
            boxphone=new Checkbox("Phone  ");
            boxtrade=new Checkbox("Quik  ");
            screenmail=new Checkbox("Screen ");
            button=new JButton("SAVE");
            button_1=new JButton("DEFAULT");
            accounttext=new JTextField(15);
            accounttext_2=new JTextField(15);
            clientcodetext=new JTextField(8);
            seccodetext=new JTextField(3);
            seccodetext_2=new JTextField(3);
            seccodetext_3=new JTextField(3);
            quantitytext=new JTextField(3);
            quantitytext_2=new JTextField(3);
            quantitytext_3=new JTextField(3);
            delta=new JTextField(3);
            delta_2=new JTextField(3);
            delta_3=new JTextField(3);
            //uppertext=new JTextField(10);
            //lowertext=new JTextField(10);
            accountlab=new JLabel(  "Account_Forts");
            accountlab_2=new JLabel(  "Account_Stocks");
            clientcodelab=new JLabel("Code client");
            seccodelab=new JLabel(  "Secode");
            quantitylab=new JLabel("Quantity");
            deltalab=new JLabel("Delta    ");
            //upperlab=new JLabel(   "Upper L");
            //lowerlab=new JLabel(   "Lower L");
            cbModel = new DefaultComboBoxModel<String>();
            cbModel_2= new DefaultComboBoxModel<String>();
            cbModel_3= new DefaultComboBoxModel<String>();
            cbFirst = new JComboBox<String>(cbModel);
            cbFirst.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    delta.setText(map_delta.get((String)dialog.cbFirst.getSelectedItem()));
                }
            });
            cbSecond = new JComboBox<String>(cbModel_2);
            cbSecond.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    delta_2.setText(map_delta.get((String)dialog.cbSecond.getSelectedItem()));
                }
            });
            cbThird = new JComboBox<String>(cbModel_3);
            cbThird.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    delta_3.setText(map_delta.get((String)dialog.cbThird.getSelectedItem()));
                }
            });
            type=new JLabel("TYPE_1");
            type_2=new JLabel("TYPE_2");
            type_3=new JLabel("TYPE_3");
            fill_mapdelta();
            loadoption();
        }

        public Dialog(Frame owner) {
            super(owner);
            form_1=new JButton("Данные 1-го инструмента");
            form_2=new JButton("Данные 2-го инструмента");
            form_3=new JButton("Данные 3-го инструмента");
            //def_2=new JButton("TS_2-начальный объём");
            //text1=new JLabel("Коэффициент");
            //coefficient=new JTextField(10);
            mart=new Checkbox("Yes/No");
            field=new JTextArea(5,20);
        }


        void fill_mapdelta(){
            map_delta=new HashMap<>();
            map_delta.put("Si RUB ","350") ;
            map_delta.put("RTS RUB ", "1000");
            map_delta.put("LKON RUB ", "800");
            map_delta.put("BR RUB ", "1");
            map_delta.put("GAZP RUB ", "200");
            map_delta.put("GOLD RUB ", "10");
            map_delta.put("MOEX RUB ","320");
            map_delta.put("MIX RUB ", "900");
            map_delta.put("STOCK ", "0");
            map_delta.put("SBER RUB ","350");
        }
        void loadoption() {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader("auth.txt"));
                while(reader.ready()){
                    phone.setText(reader.readLine());
                    login.setText(reader.readLine());
                    password.setText(reader.readLine());
                    mail.setText(reader.readLine());
                    passmail.setText(reader.readLine());
                    accounttext.setText(reader.readLine());
                    clientcodetext.setText(reader.readLine());
                    seccodetext.setText(reader.readLine());
                    quantitytext.setText(reader.readLine());
                    delta.setText(reader.readLine());
                    boxmail.setState(Boolean.parseBoolean(reader.readLine()));
                    boxphone.setState(Boolean.parseBoolean(reader.readLine()));
                    boxtrade.setState(Boolean.parseBoolean(reader.readLine()));
                    bcol_1=reader.readLine();
                    scol_1=reader.readLine();
                    hcol_1=reader.readLine();
                    bcol_2=reader.readLine();
                    scol_2=reader.readLine();
                    hcol_2=reader.readLine();
                    ccol_1=reader.readLine();
                    ccol_2=reader.readLine();
                    typesave=reader.readLine();
                    condtext_1=reader.readLine();
                    condtext_2=reader.readLine();
                    ID=Integer.parseInt(reader.readLine());
                    b1=Integer.parseInt(reader.readLine());
                    b2=Integer.parseInt(reader.readLine());
                    b3=Integer.parseInt(reader.readLine());
                    b4=Integer.parseInt(reader.readLine());
                    b5=Integer.parseInt(reader.readLine());
                    b6=Integer.parseInt(reader.readLine());
                    b7=Integer.parseInt(reader.readLine());
                    b8=Integer.parseInt(reader.readLine());
                    screenmail.setState(Boolean.parseBoolean(reader.readLine()));
                    seccodetext_2.setText(reader.readLine());
                    quantitytext_2.setText(reader.readLine());
                    typesave_2=reader.readLine();
                    delta_2.setText(reader.readLine());
                    accounttext_2.setText(reader.readLine());
                    b9=Integer.parseInt(reader.readLine());
                    b10=Integer.parseInt(reader.readLine());
                    b11=Integer.parseInt(reader.readLine());
                    b12=Integer.parseInt(reader.readLine());
                    b13=Integer.parseInt(reader.readLine());
                    b14=Integer.parseInt(reader.readLine());
                    b15=Integer.parseInt(reader.readLine());
                    seccodetext_3.setText(reader.readLine());
                    quantitytext_3.setText(reader.readLine());
                    delta_3.setText(reader.readLine());
                    bcol_3=reader.readLine();
                    scol_3=reader.readLine();
                    hcol_3=reader.readLine();
                    typesave_3=reader.readLine();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        void save(){
            try {
                BufferedWriter writer=new BufferedWriter(new FileWriter("auth.txt"));
                writer.write(phone.getText()+"\n"+login.getText()+"\n"+password.getText()+
                        "\n"+mail.getText()+"\n"+passmail.getText()+"\n"+accounttext.getText()+"\n"+clientcodetext.getText()+"\n"+
                        seccodetext.getText()+"\n"+quantitytext.getText()+"\n"+delta.getText()+"\n"+isSend_mail()+"\n"+isSend_phone()+"\n"+
                        isSend_trade()+"\n"+buy.getBackground().getRGB()+"\n"+sell.getBackground().getRGB()+"\n"+hold.getBackground().getRGB()+
                        "\n"+buy_2.getBackground().getRGB()+"\n"+sell_2.getBackground().getRGB()+"\n"+hold_2.getBackground().getRGB()+
                        "\n"+cond_1.getBackground().getRGB()+"\n"+cond_2.getBackground().getRGB()+"\n"+cbFirst.getSelectedItem()+"\n"+condtext_1+"\n"+condtext_2+"\n"+ID+"\n"+b1+"\n"+b2+
                        "\n"+b3+"\n"+b4+"\n"+b5+"\n"+b6+"\n"+b7+"\n"+b8+"\n"+getScreenmail()+"\n"+seccodetext_2.getText()+"\n"+quantitytext_2.getText()+"\n"+cbSecond.getSelectedItem()+"\n"+delta_2.getText()+"\n"+accounttext_2.getText()+"\n"+
                        +b9+"\n"+b10+ "\n"+b11+"\n"+b12+"\n"+b13+"\n"+b14+"\n"+b15+"\n"+seccodetext_3.getText()+"\n"+quantitytext_3.getText()+"\n"+delta_3.getText()+"\n"+buy_3.getBackground().getRGB()+"\n"+sell_3.getBackground().getRGB()+"\n"+hold_3.getBackground().getRGB()+"\n"+cbThird.getSelectedItem());
                writer.flush();
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        void create() {
            setBounds(1000,2,210,670);
           /*// for (int i = 0; i < elements.length; i++) {
                cbModel.addElement((String) elements[i]);
            }*/
            //cbModel.setSelectedItem((String)(typesave));
            setLayout(new FlowLayout());
            getContentPane().add(text1);
            getContentPane().add(phone);
            getContentPane().add(text3);
            getContentPane().add(login);
            getContentPane().add(text4);
            getContentPane().add(password);
            getContentPane().add(text2);
            getContentPane().add(mail);
            getContentPane().add(text5);
            getContentPane().add(passmail);
            getContentPane().add(accountlab);
            getContentPane().add(accounttext);
            getContentPane().add(accountlab_2);
            getContentPane().add(accounttext_2);
            getContentPane().add(clientcodelab);
            getContentPane().add(clientcodetext);
            getContentPane().add(type);
            getContentPane().add(cbFirst);
            getContentPane().add(type_2);
            getContentPane().add(cbSecond);
            getContentPane().add(type_3);
            getContentPane().add(cbThird);
            getContentPane().add(seccodelab);
            getContentPane().add(seccodetext);
            getContentPane().add(seccodetext_2);
            getContentPane().add(seccodetext_3);
            getContentPane().add(quantitylab);
            getContentPane().add(quantitytext);
            getContentPane().add(quantitytext_2);
            getContentPane().add(quantitytext_3);
            getContentPane().add(deltalab);
            getContentPane().add(delta);
            getContentPane().add(delta_2);
            getContentPane().add(delta_3);
            //getContentPane().add(upperlab);
            //getContentPane().add(uppertext);
            //getContentPane().add(lowerlab);
            //getContentPane().add(lowertext);
            getContentPane().add(boxmail);
            getContentPane().add(screenmail);
            getContentPane().add(boxphone);
            getContentPane().add(boxtrade);
            getContentPane().add(button);
            getContentPane().add(button_1);
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        BufferedWriter writer=new BufferedWriter(new FileWriter("auth.txt"));
                        writer.write(phone.getText()+"\n"+login.getText()+"\n"+password.getText()+
                                "\n"+mail.getText()+"\n"+passmail.getText()+"\n"+accounttext.getText()+"\n"+clientcodetext.getText()+"\n"+
                                seccodetext.getText()+"\n"+quantitytext.getText()+"\n"+delta.getText()+"\n"+isSend_mail()+"\n"+isSend_phone()+"\n"+
                                isSend_trade()+"\n"+buy.getBackground().getRGB()+"\n"+sell.getBackground().getRGB()+"\n"+hold.getBackground().getRGB()+
                                "\n"+buy_2.getBackground().getRGB()+"\n"+sell_2.getBackground().getRGB()+"\n"+hold_2.getBackground().getRGB()+
                                "\n"+cond_1.getBackground().getRGB()+"\n"+cond_2.getBackground().getRGB()+"\n"+cbFirst.getSelectedItem()+"\n"+condtext_1+"\n"+condtext_2+"\n"+ID+"\n"+b1+"\n"+b2+
                                "\n"+b3+"\n"+b4+"\n"+b5+"\n"+b6+"\n"+b7+"\n"+b8+"\n"+getScreenmail()+"\n"+seccodetext_2.getText()+"\n"+quantitytext_2.getText()+"\n"+cbSecond.getSelectedItem()+"\n"+delta_2.getText()+"\n"+accounttext_2.getText()+"\n"+
                                +b9+"\n"+b10+ "\n"+b11+"\n"+b12+"\n"+b13+"\n"+b14+"\n"+b15+"\n"+seccodetext_3.getText()+"\n"+quantitytext_3.getText()+"\n"+delta_3.getText()+"\n"+buy_3.getBackground().getRGB()+"\n"+sell_3.getBackground().getRGB()+"\n"+hold_3.getBackground().getRGB()+"\n"+cbThird.getSelectedItem());
                        writer.flush();
                        writer.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    dispose();
                }
            });
            button_1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Color x=new Color(223, 230, 230);
                    buy.setBackground(x);
                    sell.setBackground(x);
                    hold.setBackground(x);
                    buy_2.setBackground(x);
                    sell_2.setBackground(x);
                    hold_2.setBackground(x);
                    buy_3.setBackground(x);
                    sell_3.setBackground(x);
                    hold_3.setBackground(x);
                    cond_1.setBackground(x);
                    cond_2.setBackground(x);
                    b1=0;b2=0;b3=0;b4=0;b5=0;b6=0;b7=0;b8=0;b13=0;b14=0;b15=0;
                }
            });
            setVisible(true);
        }
        /*void create_2() {
            setBounds(1000, 435, 300, 300);
            setLayout(new FlowLayout());
            getContentPane().add(form_1);
            form_1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    StringBuffer buff=new StringBuffer();
                    for(Integer i:list_1) {
                        System.out.print(buff.append(String.valueOf(i)+";"));
                    }
                  field.setText(buff.toString());
                }
            });
            getContentPane().add(form_2);
            getContentPane().add(form_3);
            //getContentPane().add(def_2);
            //getContentPane().add(text1);
            //getContentPane().add(coefficient);
            getContentPane().add(mart);
            getContentPane().add(field);
            setVisible(true);
        }*/

        public boolean isSend_mail() {
            return boxmail.getState();
        }


        public boolean isSend_phone() {
            return boxphone.getState();
        }

        public boolean isSend_trade() {
            return boxtrade.getState();
        }


        public String  getPhone() {
            return phone.getText();
        }

        public String getMail() {
            return mail.getText();
        }


        public String getLogin() {
            return login.getText();
        }

        public String getPassword() {
            return password.getText();
        }
        public String getMailpass (){
            return passmail.getText();
        }
        public String getAccounttext() {
            return accounttext.getText();
        }

        public String getClientcodetext() {
            return clientcodetext.getText();
        }

        public String getSeccodetext() {
            return seccodetext.getText();
        }
        public String getSeccodetext_2() {
            return seccodetext_2.getText();
        }

        public String getQuantitytext() {
            return quantitytext.getText();
        }
        public String getQuantitytext_2() {
            return quantitytext_2.getText();
        }
        public String getQuantitytext_3() {
            return quantitytext_3.getText();
        }
        public String getDelta() {
            return delta.getText();
        }
        public String getDelta_2() {
            return delta_2.getText();
        }
        public String getDelta_3() {
            return delta_3.getText();
        }
        public boolean getScreenmail() {
            return screenmail.getState();
        }

   /*     public String getUppertext() {
            return uppertext.getText();
        }

        public String getLowertext() {
            return lowertext.getText();
        }*/
    }

    //-------------------------------------------------------------------------------------------------------------------------------
    /**КОД ДЛЯ ОТПРАВКИ СИГНАЛОВ НА ПОЧТУ
     *
     */

    public  class Test {
        private Message message = null;
        private String SMTP_SERVER = null;
        private String SMTP_Port = null;
        private String SMTP_AUTH_USER = null;
        private String SMTP_AUTH_PWD = null;
        private String EMAIL_FROM = null;
        private String FILE_PATH = null;
        private String REPLY_TO = null;

        Properties pr = new Properties();

        void save() {
            pr.put("server", dialog.getMail().split("@")[1].equals("yandex.ru") ? "smtp.yandex.ru" : "smtp.gmail.com");
            pr.put("port", dialog.getMail().split("@")[1].equals("yandex.ru") ? "465" : "465");
            pr.put("from", dialog.getMail());
            pr.put("user", dialog.getMail().split("@")[0]);
            pr.put("pass", dialog.getMailpass());
            pr.put("to", dialog.getMail());
            pr.put("replyto", "java-online@mail.ru");
        }

        void SendEmail(final String emailTo, final String thema) {
            // Настройка SMTP SSL
            Properties properties = new Properties();
            properties.put("mail.smtp.host", SMTP_SERVER);
            properties.put("mail.smtp.port", SMTP_Port);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            try {
                Authenticator auth = new EmailAuthenticator(SMTP_AUTH_USER,
                        SMTP_AUTH_PWD);
                Session session = Session.getDefaultInstance(properties, auth);
                session.setDebug(false);

                InternetAddress email_from = new InternetAddress(EMAIL_FROM);
                InternetAddress email_to = new InternetAddress(emailTo);
                InternetAddress reply_to = (REPLY_TO != null) ?
                        new InternetAddress(REPLY_TO) : null;
                message = new MimeMessage(session);
                message.setFrom(email_from);
                message.setRecipient(Message.RecipientType.TO, email_to);
                message.setSubject(thema);
                if (reply_to != null)
                    message.setReplyTo(new Address[]{reply_to});
            } catch (AddressException e) {
                System.err.println(e.getMessage());
            } catch (MessagingException e) {
                System.err.println(e.getMessage());
            }
        }

        class EmailAuthenticator extends Authenticator {
            private String login;
            private String password;

            public EmailAuthenticator(final String login, final String password) {
                this.login = login;
                this.password = password;
            }

            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(login, password);
            }
        }

        public MimeBodyPart createFileAttachment(String filepath) {
            // Создание MimeBodyPart
            MimeBodyPart mbp = new MimeBodyPart();

            // Определение файла в качестве контента
            FileDataSource fds = new FileDataSource(filepath);
            try {
                mbp.setDataHandler(new DataHandler(fds));
                mbp.setFileName(fds.getName());

            } catch (MessagingException e) {
                JOptionPane.showMessageDialog(new JFrame("Message"), "Ошибка прикрепления файла");
            }
            return mbp;
        }



        public boolean sendMessage (final String content)
        {
            boolean result = false;
            try {
                // Содержимое сообщения
                Multipart mmp = new MimeMultipart();
                // Текст сообщения
                MimeBodyPart bodyPart = new MimeBodyPart();
                bodyPart.setContent(content, "text/plain; charset=utf-8");
                mmp.addBodyPart(bodyPart);
                //Отправка скрина на почту
                if(dialog.screenmail.getState()==true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    createScreenformail();
                    MimeBodyPart mbr = createFileAttachment("screen.jpg");
                    mmp.addBodyPart(mbr);
                }
                //Отправка скрина на почту
                // Определение контента сообщения
                message.setContent(mmp);
                // Отправка сообщения
                Transport.send(message);
                result = true;
            } catch (MessagingException e){
                // Ошибка отправки сообщения
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(frame,e.getMessage());
            }
            return result;
        }
//----------------------------------------------------------------------------------------------------------------------------------
        /**КОД ОТПРАВКИ СООБЩЕНИЯ
         *
         * @throws FileNotFoundException
         */
        void sendSignal(String thema,String text)  {
            SMTP_SERVER    = pr.getProperty ("server" );
            SMTP_Port      = pr.getProperty ("port"   );
            EMAIL_FROM     = pr.getProperty ("from"   );
            SMTP_AUTH_USER = pr.getProperty ("user"   );
            SMTP_AUTH_PWD  = pr.getProperty ("pass"   );
            REPLY_TO       = pr.getProperty ("replyto");


            String emailTo = pr.getProperty ("to"   );

            SendEmail(emailTo, thema);
            sendMessage(text);
            System.out.println ("Сообщение отправлено "+ EMAIL_FROM);
        }
    }

//--------------------------------------------------------------------------------------------------------------------------------
    /**КОД ОТПРАВКИ ТОРГОВЫХ СИГНАЛОВ
     *
     */

    public class Trade {

        private String field_1 = "ACCOUNT";
        private String field_2 = "CLIENT_CODE";
        private String field_3 = "TYPE";
        private String field_4 = "TRANS_ID";
        private String field_5 = "CLASSCODE";
        private String field_6 = "SECCODE";
        private String field_7 = "ACTION";
        private String field_8 = "OPERATION";
        private String field_9 = "PRICE";
        private String field_10 = "QUANTITY";
        private String code="";
        private String account="";
        private String price_order="";
        private String delta_order="";
        private Properties pr = new Properties();
        private ArrayList<String> list = new ArrayList<>();


        public Trade() {
            list.add(field_1);
            list.add(field_2);
            list.add(field_3);
            list.add(field_4);
            list.add(field_5);
            list.add(field_6);
            list.add(field_7);
            list.add(field_8);
            list.add(field_9);
            list.add(field_10);
            pr.put("ACCOUNT", dialog.getAccounttext());
            pr.put("CLIENT_CODE", dialog.getClientcodetext());
            pr.put("TYPE", "M");
            pr.put("TRANS_ID", "0");
            pr.put("CLASSCODE", "SPBFUT");
            pr.put("SECCODE", dialog.getSeccodetext());
            pr.put("ACTION", "NEW_ORDER");
            pr.put("OPERATION", "B");
            pr.put("PRICE", "58000");
            pr.put("QUANTITY", dialog.getQuantitytext());
        }

        void Order_Buy() {
            pr.setProperty("ACCOUNT", dialog.cbFirst.getSelectedItem().equals("STOCK ")?dialog.accounttext_2.getText():dialog.accounttext.getText());
            pr.setProperty("CLASSCODE",dialog.cbFirst.getSelectedItem().equals("STOCK ")?"TQBR":"SPBFUT");
            pr.setProperty("SECCODE", dialog.seccodetext.getText());
            pr.setProperty("OPERATION", "B");
            pr.setProperty("PRICE", String.valueOf(Integer.parseInt(price) + Integer.parseInt(dialog.getDelta())));
            pr.setProperty("TRANS_ID", String.valueOf(++ID));
            String str = "";
            if (status == -1) {
                pr.setProperty("QUANTITY", String.valueOf(Integer.parseInt(dialog.getQuantitytext()) * 2));
                for (int i = 0; i < list.size(); i++) {
                    str += list.get(i) + "=" + pr.getProperty(list.get(i)) + "; ";
                }
                size+=Integer.parseInt(dialog.getQuantitytext())*2;
            } else if (status == 0) {
                pr.setProperty("QUANTITY", String.valueOf(Integer.parseInt(dialog.getQuantitytext())));
                for (int i = 0; i < list.size(); i++) {
                    str += list.get(i) + "=" + pr.getProperty(list.get(i)) + "; ";
                }
                size+=Integer.parseInt(dialog.getQuantitytext());
            }
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("trade.tri", append = true));
                writer.write(str + "\r\n");
                writer.flush();
                writer.close();
                text.setText("Покупка...");
            } catch (IOException e1) {
                test.sendSignal("ERROR","Ошибка записи в файл транзакций "+new Date());
                JOptionPane.showMessageDialog(new JFrame("Message"), "Ошибка записи в файл транзакций");
                e1.printStackTrace();
            }
            //write_file("poz.txt",String.valueOf (ID),String.valueOf(size));
        }


        void Order_Sell() {
            pr.setProperty("ACCOUNT", dialog.cbFirst.getSelectedItem().equals("STOCK ")?dialog.accounttext_2.getText():dialog.accounttext.getText());
            pr.setProperty("CLASSCODE",dialog.cbFirst.getSelectedItem().equals("STOCK ")?"TQBR":"SPBFUT");
            pr.setProperty("SECCODE", dialog.seccodetext.getText());
            pr.setProperty("OPERATION", "S");
            pr.setProperty("TRANS_ID", String.valueOf(++ID));
            pr.setProperty("PRICE", String.valueOf(Integer.parseInt(price) - Integer.parseInt(dialog.getDelta())));
            String str = "";
            if (status == 1) {
                pr.setProperty("QUANTITY", String.valueOf(Integer.parseInt(dialog.getQuantitytext()) * 2));
                for (int i = 0; i < list.size(); i++) {
                    str += list.get(i) + "=" + pr.getProperty(list.get(i)) + "; ";
                }
                size-=Integer.parseInt(dialog.getQuantitytext())*2;
            } else if (status == 0) {
                pr.setProperty("QUANTITY", String.valueOf(Integer.parseInt(dialog.getQuantitytext())));
                for (int i = 0; i < list.size(); i++) {
                    str += list.get(i) + "=" + pr.getProperty(list.get(i)) + "; ";
                }
                size-=Integer.parseInt(dialog.getQuantitytext());
            }
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("trade.tri", append = true));
                writer.write(str + "\r\n");
                writer.flush();
                writer.close();
                text.setText("Продажа...");
            } catch (IOException e1) {
                test.sendSignal("ERROR","Ошибка записи в файл транзакций "+new Date());
                JOptionPane.showMessageDialog(new JFrame("Message"), "Ошибка записи в файл транзакций");
                e1.printStackTrace();
            }
            //write_file("poz.txt",String.valueOf (ID),String.valueOf(size));
        }

        void Order_HOLD() {
            pr.setProperty("ACCOUNT", dialog.cbFirst.getSelectedItem().equals("STOCK ")?dialog.accounttext_2.getText():dialog.accounttext.getText());
            pr.setProperty("CLASSCODE",dialog.cbFirst.getSelectedItem().equals("STOCK ")?"TQBR":"SPBFUT");
            pr.setProperty("SECCODE", dialog.seccodetext.getText());
            pr.setProperty("TRANS_ID", String.valueOf(++ID));
            String str = "";
            if (status == 1) {
                pr.setProperty("QUANTITY", String.valueOf(Integer.parseInt(dialog.getQuantitytext())));
                pr.setProperty("OPERATION", "S");
                pr.setProperty("PRICE", String.valueOf(Integer.parseInt(price) - Integer.parseInt(dialog.getDelta())));
                for (int i = 0; i < list.size(); i++) {
                    str += list.get(i) + "=" + pr.getProperty(list.get(i)) + "; ";
                }
                size-=Integer.parseInt(dialog.getQuantitytext());
            } else if (status == -1) {
                pr.setProperty("QUANTITY", String.valueOf(Integer.parseInt(dialog.getQuantitytext())));
                pr.setProperty("OPERATION", "B");
                pr.setProperty("PRICE", String.valueOf(Integer.parseInt(price) + Integer.parseInt(dialog.getDelta())));
                for (int i = 0; i < list.size(); i++) {
                    str += list.get(i) + "=" + pr.getProperty(list.get(i)) + "; ";
                }
                size+=Integer.parseInt(dialog.getQuantitytext())*2;
            }

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("trade.tri", append = true));
                writer.write(str + "\r\n");
                writer.flush();
                writer.close();
                text.setText("Выход из позиции...");
            } catch (IOException e1) {
                test.sendSignal("ERROR","Ошибка записи в файл транзакций "+new Date());
                JOptionPane.showMessageDialog(new JFrame("Message"), "Ошибка записи в файл транзакций");
                e1.printStackTrace();
            }
            //write_file("poz.txt",String.valueOf (ID),String.valueOf(size));
        }
        //Ордера для второй системы//
        void Order_Buy_2() {
            pr.setProperty("ACCOUNT", dialog.cbSecond.getSelectedItem().equals("STOCK ")?dialog.accounttext_2.getText():dialog.accounttext.getText());
            pr.setProperty("CLASSCODE",dialog.cbSecond.getSelectedItem().equals("STOCK ")?"TQBR":"SPBFUT");
            pr.setProperty("SECCODE", dialog.seccodetext_2.getText());
            pr.setProperty("OPERATION", "B");
            pr.setProperty("PRICE", String.valueOf(Integer.parseInt(price_2) + Integer.parseInt(dialog.getDelta_2())));
            pr.setProperty("TRANS_ID", String.valueOf(++ID));
            String str = "";
            if (status_2==-1) {
                pr.setProperty("QUANTITY", String.valueOf(Integer.parseInt(dialog.getQuantitytext_2()) * 2));
                for (int i = 0; i < list.size(); i++) {
                    str += list.get(i) + "=" + pr.getProperty(list.get(i)) + "; ";
                }
                size+=Integer.parseInt(dialog.getQuantitytext_2())*2;
            } else if (status_2==0) {
                pr.setProperty("QUANTITY", String.valueOf(Integer.parseInt(dialog.getQuantitytext_2())));
                for (int i = 0; i < list.size(); i++) {
                    str += list.get(i) + "=" + pr.getProperty(list.get(i)) + "; ";
                }
                size+=Integer.parseInt(dialog.getQuantitytext_2());
            }
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("trade.tri", append = true));
                writer.write(str + "\r\n");
                writer.flush();
                writer.close();
                text.setText("Покупка...");
            } catch (IOException e1) {
                test.sendSignal("ERROR","Ошибка записи в файл транзакций "+new Date());
                JOptionPane.showMessageDialog(new JFrame("Message"), "Ошибка записи в файл транзакций");
                e1.printStackTrace();
            }
        }


        void Order_Sell_2() {
            pr.setProperty("ACCOUNT", dialog.cbSecond.getSelectedItem().equals("STOCK ")?dialog.accounttext_2.getText():dialog.accounttext.getText());
            pr.setProperty("CLASSCODE",dialog.cbSecond.getSelectedItem().equals("STOCK ")?"TQBR":"SPBFUT");
            pr.setProperty("SECCODE", dialog.seccodetext_2.getText());
            pr.setProperty("OPERATION", "S");
            pr.setProperty("TRANS_ID", String.valueOf(++ID));
            pr.setProperty("PRICE", String.valueOf(Integer.parseInt(price_2) - Integer.parseInt(dialog.getDelta_2())));
            String str = "";
            if (status_2==1) {
                pr.setProperty("QUANTITY", String.valueOf(Integer.parseInt(dialog.getQuantitytext_2()) * 2));
                for (int i = 0; i < list.size(); i++) {
                    str += list.get(i) + "=" + pr.getProperty(list.get(i)) + "; ";
                }
                size-=Integer.parseInt(dialog.getQuantitytext_2())*2;
            } else if (status_2==0) {
                pr.setProperty("QUANTITY", String.valueOf(Integer.parseInt(dialog.getQuantitytext_2())));
                for (int i = 0; i < list.size(); i++) {
                    str += list.get(i) + "=" + pr.getProperty(list.get(i)) + "; ";
                }
                size-=Integer.parseInt(dialog.getQuantitytext_2());
            }
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("trade.tri", append = true));
                writer.write(str + "\r\n");
                writer.flush();
                writer.close();
                text.setText("Продажа...");
            } catch (IOException e1) {
                test.sendSignal("ERROR","Ошибка записи в файл транзакций "+new Date());
                JOptionPane.showMessageDialog(new JFrame("Message"), "Ошибка записи в файл транзакций");
                e1.printStackTrace();
            }
        }

        void Order_HOLD_2() {
            pr.setProperty("ACCOUNT", dialog.cbSecond.getSelectedItem().equals("STOCK ")?dialog.accounttext_2.getText():dialog.accounttext.getText());
            pr.setProperty("CLASSCODE",dialog.cbSecond.getSelectedItem().equals("STOCK ")?"TQBR":"SPBFUT");
            pr.setProperty("SECCODE", dialog.seccodetext_2.getText());
            pr.setProperty("TRANS_ID", String.valueOf(++ID));
            String str = "";
            if (status_2==1) {
                pr.setProperty("QUANTITY", String.valueOf(Integer.parseInt(dialog.getQuantitytext_2())));
                pr.setProperty("OPERATION", "S");
                pr.setProperty("PRICE", String.valueOf(Integer.parseInt(price_2) - Integer.parseInt(dialog.getDelta_2())));
                for (int i = 0; i < list.size(); i++) {
                    str += list.get(i) + "=" + pr.getProperty(list.get(i)) + "; ";
                }
                size-=Integer.parseInt(dialog.getQuantitytext_2());
            } else if (status_2==-1) {
                pr.setProperty("QUANTITY", String.valueOf(Integer.parseInt(dialog.getQuantitytext_2())));
                pr.setProperty("OPERATION", "B");
                pr.setProperty("PRICE", String.valueOf(Integer.parseInt(price_2) + Integer.parseInt(dialog.getDelta_2())));
                for (int i = 0; i < list.size(); i++) {
                    str += list.get(i) + "=" + pr.getProperty(list.get(i)) + "; ";
                }
                size+=Integer.parseInt(dialog.getQuantitytext_2())*2;
            }

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("trade.tri", append = true));
                writer.write(str + "\r\n");
                writer.flush();
                writer.close();
                text.setText("Выход из позиции...");
            } catch (IOException e1) {
                test.sendSignal("ERROR","Ошибка записи в файл транзакций "+new Date());
                JOptionPane.showMessageDialog(new JFrame("Message"), "Ошибка записи в файл транзакций");
                e1.printStackTrace();
            }
        }
        //*******************************************************************////////////
        //Ордера для третей системы//
        void Order_Buy_3() {
            pr.setProperty("ACCOUNT", dialog.cbThird.getSelectedItem().equals("STOCK ")?dialog.accounttext_2.getText():dialog.accounttext.getText());
            pr.setProperty("CLASSCODE",dialog.cbThird.getSelectedItem().equals("STOCK ")?"TQBR":"SPBFUT");
            pr.setProperty("SECCODE", dialog.seccodetext_3.getText());
            pr.setProperty("OPERATION", "B");
            pr.setProperty("PRICE", String.valueOf(Integer.parseInt(price_3) + Integer.parseInt(dialog.getDelta_3())));
            pr.setProperty("TRANS_ID", String.valueOf(++ID));
            String str = "";
            if (status_5==-1) {
                pr.setProperty("QUANTITY", String.valueOf(Integer.parseInt(dialog.getQuantitytext_3()) * 2));
                for (int i = 0; i < list.size(); i++) {
                    str += list.get(i) + "=" + pr.getProperty(list.get(i)) + "; ";
                }
                size+=Integer.parseInt(dialog.getQuantitytext_3())*2;
            } else if (status_5==0) {
                pr.setProperty("QUANTITY", String.valueOf(Integer.parseInt(dialog.getQuantitytext_3())));
                for (int i = 0; i < list.size(); i++) {
                    str += list.get(i) + "=" + pr.getProperty(list.get(i)) + "; ";
                }
                size+=Integer.parseInt(dialog.getQuantitytext_3());
            }
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("trade.tri", append = true));
                writer.write(str + "\r\n");
                writer.flush();
                writer.close();
                text.setText("Покупка...");
            } catch (IOException e1) {
                test.sendSignal("ERROR","Ошибка записи в файл транзакций "+new Date());
                JOptionPane.showMessageDialog(new JFrame("Message"), "Ошибка записи в файл транзакций");
                e1.printStackTrace();
            }
        }

        void Order_Sell_3() {
            pr.setProperty("ACCOUNT", dialog.cbThird.getSelectedItem().equals("STOCK ")?dialog.accounttext_2.getText():dialog.accounttext.getText());
            pr.setProperty("CLASSCODE",dialog.cbThird.getSelectedItem().equals("STOCK ")?"TQBR":"SPBFUT");
            pr.setProperty("SECCODE", dialog.seccodetext_3.getText());
            pr.setProperty("OPERATION", "S");
            pr.setProperty("TRANS_ID", String.valueOf(++ID));
            pr.setProperty("PRICE", String.valueOf(Integer.parseInt(price_3) - Integer.parseInt(dialog.getDelta_3())));
            String str = "";
            if (status_5==1) {
                pr.setProperty("QUANTITY", String.valueOf(Integer.parseInt(dialog.getQuantitytext_3()) * 2));
                for (int i = 0; i < list.size(); i++) {
                    str += list.get(i) + "=" + pr.getProperty(list.get(i)) + "; ";
                }
                size-=Integer.parseInt(dialog.getQuantitytext_3())*2;
            } else if (status_5==0) {
                pr.setProperty("QUANTITY", String.valueOf(Integer.parseInt(dialog.getQuantitytext_3())));
                for (int i = 0; i < list.size(); i++) {
                    str += list.get(i) + "=" + pr.getProperty(list.get(i)) + "; ";
                }
                size-=Integer.parseInt(dialog.getQuantitytext_3());
            }
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("trade.tri", append = true));
                writer.write(str + "\r\n");
                writer.flush();
                writer.close();
                text.setText("Продажа...");
            } catch (IOException e1) {
                test.sendSignal("ERROR","Ошибка записи в файл транзакций "+new Date());
                JOptionPane.showMessageDialog(new JFrame("Message"), "Ошибка записи в файл транзакций");
                e1.printStackTrace();
            }
        }

        void Order_HOLD_3() {
            pr.setProperty("ACCOUNT", dialog.cbThird.getSelectedItem().equals("STOCK ")?dialog.accounttext_2.getText():dialog.accounttext.getText());
            pr.setProperty("CLASSCODE",dialog.cbThird.getSelectedItem().equals("STOCK ")?"TQBR":"SPBFUT");
            pr.setProperty("SECCODE", dialog.seccodetext_3.getText());
            pr.setProperty("TRANS_ID", String.valueOf(++ID));
            String str = "";
            if (status_5==1) {
                pr.setProperty("QUANTITY", String.valueOf(Integer.parseInt(dialog.getQuantitytext_3())));
                pr.setProperty("OPERATION", "S");
                pr.setProperty("PRICE", String.valueOf(Integer.parseInt(price_3) - Integer.parseInt(dialog.getDelta_3())));
                for (int i = 0; i < list.size(); i++) {
                    str += list.get(i) + "=" + pr.getProperty(list.get(i)) + "; ";
                }
                size-=Integer.parseInt(dialog.getQuantitytext_3());
            } else if (status_5==-1) {
                pr.setProperty("QUANTITY", String.valueOf(Integer.parseInt(dialog.getQuantitytext_3())));
                pr.setProperty("OPERATION", "B");
                pr.setProperty("PRICE", String.valueOf(Integer.parseInt(price_3) + Integer.parseInt(dialog.getDelta_3())));
                for (int i = 0; i < list.size(); i++) {
                    str += list.get(i) + "=" + pr.getProperty(list.get(i)) + "; ";
                }
                size+=Integer.parseInt(dialog.getQuantitytext_3())*2;
            }

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("trade.tri", append = true));
                writer.write(str + "\r\n");
                writer.flush();
                writer.close();
                text.setText("Выход из позиции...");
            } catch (IOException e1) {
                test.sendSignal("ERROR","Ошибка записи в файл транзакций "+new Date());
                JOptionPane.showMessageDialog(new JFrame("Message"), "Ошибка записи в файл транзакций");
                e1.printStackTrace();
            }
        }
        //**********************Простые ордера на покупку и продажу*********************
        void Order_B() {
            if(m==2&&dialog.cbSecond.getSelectedItem().equals("STOCK ")||m==1&&dialog.cbFirst.getSelectedItem().equals("STOCK ")||m==3&&dialog.cbThird.getSelectedItem().equals("STOCK ")) {
                code = "TQBR";
                account = dialog.accounttext_2.getText();
            }
            else {
                code = "SPBFUT";
                account = dialog.accounttext.getText();
            }
            if(m==1||m==0){
                price_order=price;
                delta_order=dialog.getDelta();
            }
            else if (m==2){
                System.out.println(m);
                price_order=price_2;
                delta_order=dialog.getDelta_2();
            }
            else if (m==3){
                System.out.println(m);
                price_order=price_3;
                delta_order=dialog.getDelta_3();
            }
            System.out.println("price order"+price_order);
            //System.out.println(m);
            pr.setProperty("PRICE",  String.valueOf(Integer.parseInt(price_order) + Integer.parseInt(delta_order)));
            pr.setProperty("CLASSCODE",code);
            pr.setProperty("ACCOUNT", account);
            pr.setProperty("SECCODE", instrument.getText());
            pr.setProperty("OPERATION", "B");
            pr.setProperty("TRANS_ID", String.valueOf(++ID));
            String str = "";
            pr.setProperty("QUANTITY", "1");
            for (int i = 0; i < list.size(); i++) {
                str += list.get(i) + "=" + pr.getProperty(list.get(i)) + "; ";
            }
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("trade.tri", append = true));
                writer.write(str + "\r\n");
                writer.flush();
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if(dialog.isSend_trade()==false) {
                ID++;
                write_file("poz.txt",dialog.getSeccodetext(), String.valueOf(ID), dialog.getQuantitytext());
            }
            else{
                write_file("poz.txt",dialog.getSeccodetext(), String.valueOf(ID), dialog.getQuantitytext());
            }
        }


        void Order_S() {
            if(m==2&&dialog.cbSecond.getSelectedItem().equals("STOCK ")||m==1&&dialog.cbFirst.getSelectedItem().equals("STOCK ")||m==3&&dialog.cbThird.getSelectedItem().equals("STOCK ")) {
                code = "TQBR";
                account = dialog.accounttext_2.getText();
            }
            else {
                code = "SPBFUT";
                account = dialog.accounttext.getText();
            }
            if(m==1||m==0){
                price_order=price;
                delta_order=dialog.getDelta();
            }
            else if (m==2){
                System.out.println(m);
                price_order=price_2;
                delta_order=dialog.getDelta_2();
            }
            else if (m==3){
                System.out.println(m);
                price_order=price_3;
                delta_order=dialog.getDelta_3();
            }
            pr.setProperty("PRICE",  String.valueOf(Integer.parseInt(price_order) - Integer.parseInt(delta_order)));
            pr.put("CLASSCODE",code);
            pr.put("ACCOUNT", account);
            pr.setProperty("SECCODE", instrument.getText());
            pr.setProperty("OPERATION", "S");
            pr.setProperty("TRANS_ID", String.valueOf(++ID));
            String str = "";
            pr.setProperty("QUANTITY", "1");
            for (int i = 0; i < list.size(); i++) {
                str += list.get(i) + "=" + pr.getProperty(list.get(i)) + "; ";}
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("trade.tri", append = true));
                writer.write(str + "\r\n");
                writer.flush();
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if(dialog.isSend_trade()==false) {
                ID++;
                write_file("poz.txt", dialog.getSeccodetext(), String.valueOf(ID), String.valueOf(Integer.parseInt(dialog.getQuantitytext()) * (-1)));
            }
            else{
                write_file("poz.txt", dialog.getSeccodetext(), String.valueOf(ID), String.valueOf(Integer.parseInt(dialog.getQuantitytext()) * (-1)));
            }
        }
        void Order_H() {
            if(m==2&&dialog.cbSecond.getSelectedItem().equals("STOCK ")||m==1&&dialog.cbFirst.getSelectedItem().equals("STOCK ")||m==3&&dialog.cbThird.getSelectedItem().equals("STOCK ")) {
                code = "TQBR";
                account = dialog.accounttext_2.getText();
            }
            else {
                code = "SPBFUT";
                account = dialog.accounttext.getText();
            }
            if(m==1||m==0){
                System.out.println(m);
                price_order=price;
                delta_order=dialog.getDelta();
                pr.setProperty("QUANTITY","1");
            }
            else if (m==2){
                System.out.println(m);
                price_order=price_2;
                delta_order=dialog.getDelta_2();
                pr.setProperty("QUANTITY", "1");
            }
            else if (m==3){
                System.out.println(m);
                price_order=price_3;
                delta_order=dialog.getDelta_3();
                pr.setProperty("QUANTITY", "1");
            }
            pr.setProperty("CLASSCODE",code);
            pr.setProperty("ACCOUNT", account);
            pr.setProperty("SECCODE", instrument.getText());
            if(size!=0){
                pr.setProperty("TRANS_ID", String.valueOf(++ID));
            }
            String str = "";
            if (size >0) {
                pr.setProperty("OPERATION", "S");
                pr.setProperty("PRICE",  String.valueOf(Integer.parseInt(price_order) - Integer.parseInt(delta_order)));
                for (int i = 0; i < list.size(); i++) {
                    str += list.get(i) + "=" + pr.getProperty(list.get(i)) + "; ";
                }
                size-=1;
            } else if (size <0) {
                pr.setProperty("OPERATION", "B");
                pr.setProperty("PRICE",  String.valueOf(Integer.parseInt(price_order) + Integer.parseInt(delta_order)));
                for (int i = 0; i < list.size(); i++) {
                    str += list.get(i) + "=" + pr.getProperty(list.get(i)) + "; ";
                }
                size+=1;
                System.out.println(size);
            }

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("trade.tri", append = true));
                writer.write(str + "\r\n");
                writer.flush();
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            text.setBackground(Color.WHITE);
        }


        //****************************************************************************
    }

}




