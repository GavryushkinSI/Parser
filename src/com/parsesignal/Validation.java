package com.parsesignal;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

//Проверяет соответсвие позиций в Quik  c позицией в ParseSignal. В случае разногласий отправляет сообщение на почту.
public class Validation implements Runnable{
    public int getPoz_quik() {
        return poz_quik;
    }

    private int  poz_quik;

     Parse.MyRunnable run=null;
     Parse.Test obj=null;
     JTextField text=null;
    Validation(Parse.MyRunnable run, Parse.Test obj, JTextField text){
        this.run=run;
        this.obj=obj;
        this.text=text;
    }
    @Override
    public void run() {
        while(Parse.work!=false){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            poz_quik=read_file("val.txt");
            //System.out.println(poz_quik);
         if(run.position!=poz_quik){
         obj.sendSignal("Позиции в Quik  и ParseSignal не равны, сделайте ручную корректировку.","");

         }
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                text.setText("Validation thread interrupted");
                obj.sendSignal("Прерван поток Validation","");
            }
        }
    }
    int read_file(String path) {
        BufferedReader rd = null;
        String[] x = null;
        try {
            rd = new BufferedReader(new FileReader(path));
            x = rd.readLine().split(" ");
            rd.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(x[0]);
    }

}
