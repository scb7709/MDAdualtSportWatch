package com.headlth.management.entity;

import java.util.List;

/**
 * Created by 1 on 2015/12/2.
 */

public class copy{
    public static List<Integer> SSSportDay;
    private static copy instance=null;
    public static copy getInstance(){
        if(instance==null){
            synchronized(copy.class){
                if(instance==null){
                    instance=new copy();
                }
            }
        }
        return instance;
    }
    private copy(){}
}