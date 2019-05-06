package com.parsesignal;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
public class Datasetp
{

    public static CategoryDataset createDataset1()
    {
        DefaultCategoryDataset dataset;

        final String series1 = "TS_1" ;
        int []x=Parse.x_toint;
        int y=0;
        dataset = new DefaultCategoryDataset();
        for(int i=0;i<x.length-1;i++){
            y+=x[i]+x[i+1];
            dataset.addValue(y,series1,String.valueOf(i+1));
        }
        return dataset;
    }

}
