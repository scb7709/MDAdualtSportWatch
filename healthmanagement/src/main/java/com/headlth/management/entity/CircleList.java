package com.headlth.management.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abc on 2016/9/1.
 */
public class CircleList {
    public List<Circle> circlelist=  new ArrayList<>();
    public List<Circle> mycirclelist=  new ArrayList<>();
    public List<Circle> circlelistall=  new ArrayList<>();
    public List<Circle> circlelistmy=  new ArrayList<>();
    public List<Comment> commentlist=new ArrayList<>();
    public List<Reply> replylist=new ArrayList<>();
    private static CircleList circleList;
    private CircleList() {
    }
    public static CircleList getInstance() {

        if (circleList == null) {
            synchronized (CircleList.class) {
                if (circleList == null) {
                    circleList = new CircleList();
                }
            }
        }
        return circleList;
    }

}
