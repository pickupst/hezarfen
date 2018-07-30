package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import istanbul.gamelab.ngdroid.base.BaseCanvas;

import android.graphics.Rect;

import com.ngdroidapp.GUI.NgButton;

import istanbul.gamelab.ngdroid.util.Utils;

/**
 * Created by PC LAB on 10.07.2018.
 */

public class RestartCanvas extends BaseCanvas {

    private Bitmap restartmenu;
    private Rect restartsource,restartdestination;
    private int touchdownx, touchdowny;
    private NgButton crossedbutton;






    public RestartCanvas(NgApp ngApp) {

        super(ngApp);}

    public void setup() {

        restartdestination = new Rect();
        restartsource = new Rect();
        restartmenu = Utils.loadImage(root,"hakkinda1.png");




    }


    public void update() {

    }


    public void draw(Canvas canvas) {

        restartsource.set(0, 0, restartmenu.getWidth(), restartmenu.getHeight());
        restartdestination.set(0 ,0 , getWidth(), getHeight());
        canvas.drawBitmap(restartmenu, restartsource, restartdestination, null);


    }

    @Override
    public void keyPressed(int key) {

    }

    @Override
    public void keyReleased(int key) {

    }

    @Override
    public boolean backPressed() {
        return false;
    }

    @Override
    public void touchDown(int x, int y, int id) {

    }

    @Override
    public void touchMove(int x, int y, int id) {

    }

    @Override
    public void touchUp(int x, int y, int id) {

    }

    @Override
    public void surfaceChanged(int width, int height) {

    }

    @Override
    public void surfaceCreated() {

    }

    @Override
    public void surfaceDestroyed() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void reloadTextures() {

    }

    @Override
    public void showNotify() {

    }

    @Override
    public void hideNotify() {

    }
}
