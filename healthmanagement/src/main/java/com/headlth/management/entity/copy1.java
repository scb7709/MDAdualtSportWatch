package com.headlth.management.entity;

import java.util.List;

/**
 * Created by 1 on 2015/12/2.
 */

public class copy1 {
    public static List<Integer> SSSportDay;
    private static copy1 instance=null;
    public static copy1 getInstance(){
        if(instance==null){
            synchronized(copy1.class){
                if(instance==null){
                    instance=new copy1();
                }
            }
        }
        return instance;
    }
    private copy1(){}
}