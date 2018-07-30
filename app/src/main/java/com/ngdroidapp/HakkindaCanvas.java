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

public class HakkindaCanvas extends BaseCanvas {

    private String context;
    private Bitmap hakkindamenu;
    private Rect hakkindasource,hakkindadestination;
    private int touchdownx, touchdowny;
    private NgButton crossedbutton;







    public HakkindaCanvas(NgApp ngApp) {
        super(ngApp);
        this.context = "";
        crossedbutton = new NgButton(ngApp, context + "crossed.png", context + "crosseddown.png", 1800, 20, 120, 120);
    }


    public void setup() {

        hakkindadestination = new Rect();
        hakkindasource = new Rect();
        hakkindamenu = Utils.loadImage(root,"hakkinda1.png");




    }


    public void update() {

        if (crossedbutton.IsClicked()) {
            MenuCanvas mc = new MenuCanvas(root);
            root.canvasManager.setCurrentCanvas(mc);
        }

    }


    public void draw(Canvas canvas) {

        hakkindasource.set(0, 0, hakkindamenu.getWidth(), hakkindamenu.getHeight());
        hakkindadestination.set(0 ,0 , getWidth(), getHeight());
        canvas.drawBitmap(hakkindamenu, hakkindasource, hakkindadestination, null);

        crossedbutton.draw(canvas);


    }


    public void keyPressed(int key) {

    }

    public void keyReleased(int key) {

    }

    public boolean backPressed() {
        return false;
    }


    public void touchDown(int x, int y, int id) {

        crossedbutton.onHover(x, y);


    }

    public void touchMove(int x, int y, int id) {
        crossedbutton.onHover(x, y);

    }

    public void touchUp(int x, int y, int id) {
        crossedbutton.IsClicked(x, y);

    }


    public void surfaceChanged(int width, int height) {

    }

    public void surfaceCreated() {

    }

    public void surfaceDestroyed() {

    }

    public void pause() {

    }


    public void resume() {

    }

    public void reloadTextures() {

    }

    public void showNotify() {

    }

    public void hideNotify() {

    }
}
