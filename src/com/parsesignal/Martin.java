package com.parsesignal;

import java.util.ArrayList;

public class Martin {
      int y=0;
      static ArrayList<Integer> index = new ArrayList<Integer>();
      static int level=0;
      final int begin_quantity;
      Martin(Parse.Dialog dialog){
          begin_quantity = Integer.parseInt(dialog.getQuantitytext());

  }


      public int coefficient(){
          if(Parse.list.size()==0||Parse.list.get(Parse.list.size()-1)>level){
              Parse.list.clear();
              System.out.println("Список очищен");
              y=begin_quantity;
              System.out.println(y);
              return y;
          }
          else {
              try {
                  if (Parse.list.get(Parse.list.size() - 1) < 0 || Parse.list.get(Parse.list.size() - 1) <= level) {
                      y = index.get(Parse.list.size() - 1);
                      System.out.println(y);
                      return y;
                  }
              }
              catch(Exception ex){
                  if(index.size()!=0) {
                      y = index.get(index.size() - 1);
                      System.out.println(y);
                      return y;
                  }
                  else {
                      y=begin_quantity;
                      System.out.println(y);
                      return y;
                  }
              }

              }

        return y;
      }
}
