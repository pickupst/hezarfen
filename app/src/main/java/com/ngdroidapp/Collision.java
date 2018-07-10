package com.ngdroidapp;

import android.graphics.Rect;

public class Collision {

    private Rect col;

    public Collision(int x, int y, int x2, int y2){

        col = new Rect();
        col.set(x, y, x2, y2);

    }



}
