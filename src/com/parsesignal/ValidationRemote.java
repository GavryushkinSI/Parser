package com.parsesignal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ValidationRemote implements Runnable {
    private static int  count =0;
     Parse.MyRunnable run=null;
     Parse.Test obj=null;
    Calendar c=Calendar.getInstance();
    DateFormat df=new SimpleDateFormat("u");
    ValidationRemote(Parse.MyRunnable run, Parse.Test obj){
        this.run=run;
        this.obj=obj;
    }
    @Override
    public void run() {
        boolean time=Integer.parseInt(df.format(c.getTime()))<=5;
        while(Parse.work!=false) {
                System.out.println("unique_pixels: " + run.count_unique);
                if (run.count_unique < 300) {
                    count++;
                    obj.sendSignal("FATAL ERROR", "Некорректное сканироваание: в сканируемом изображении не найденно ни одного дубликата");
                    System.out.println("Некорректное сканирование: в сканируемом изображении не найденно ни одного дубликата"+"COUNT:"+count);

                }

                try {
                    Thread.sleep(300000);
                } catch (InterruptedException e) {
                    System.out.println("Прерван поток ValidationRemote");
                }
            }
        }
    }

