package com.headlth.management.entity;

import java.util.List;

/**
 * Created by 1 on 2015/12/2.
 */

public class copy2 {
    public static List<Integer> SSSportDay;
    private static copy2 instance=null;
    public static copy2 getInstance(){
        if(instance==null){
            synchronized(copy2.class){
                if(instance==null){
                    instance=new copy2();
                }
            }
        }
        return instance;
    }
    private copy2(){}
}