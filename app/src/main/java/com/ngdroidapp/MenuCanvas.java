package com.ngdroidapp;

import android.graphics.Canvas;
import android.util.Log;

import com.ngdroidapp.GUI.NgBackground;
import com.ngdroidapp.GUI.NgButton;

import istanbul.gamelab.ngdroid.base.BaseCanvas;
import istanbul.gamelab.ngdroid.core.NgMediaPlayer;
import istanbul.gamelab.ngdroid.util.Utils;

/**
 * Created by noyan on 27.06.2016.
 * Nitra Games Ltd.
 */

public class MenuCanvas extends BaseCanvas {

    private NgButton playButton;
   // private NgButton creditsButton;
    private NgButton soundButton;
   // private NgButton scorebutton;
    private NgBackground background;
    private NgButton quitbutton;
    private String context;
    private NgButton hakkindabotton;
    private NgMediaPlayer backgroundsound;
    private  boolean backgroundsoundenable;
    private int x, y;






    public MenuCanvas(NgApp ngApp) {
        super(ngApp);
        this.context = "";
        playButton = new NgButton(ngApp, context + "playbutton.png", context + "playbutton.png", 600, 400, 400, 180);
       // creditsButton = new NgButton(ngApp, context + "Credits.png",context + "CreditsClicked.png", 203, 0, 151, 151);
        soundButton = new NgButton(ngApp, context + "voicebutton.png",context + "mutebutton.png", 1800, 20, 151, 151);
        background = new NgBackground(ngApp, context + "backnew.png");
        //scorebutton = new NgButton(ngApp, context + "scorebutton.png", context + "scorebutton.png", 600, 570, 320, 160);
        quitbutton = new NgButton(ngApp, context + "quitbutton.png", context + "quitbutton.png", 600, 630 ,400 , 180);
        hakkindabotton = new NgButton(ngApp, context + "hakkindabutton.png", context + "hakkindabuttonpressed.png", 1800, 800 ,120 , 120);



    }

    public void setup() {

        backgroundsoundenable= true;

        backgroundsound = new NgMediaPlayer(root);
        backgroundsound.load("sound/volkan.mp3");
        backgroundsound.prepare();
        backgroundsound.setLooping(true);
        backgroundsound.start();



    }

    public void update() {
        if (playButton.IsClicked()) {
            GameCanvas mc = new GameCanvas(root);
            root.canvasManager.setCurrentCanvas(mc);
        }

        if(hakkindabotton.IsClicked()){
            HakkindaCanvas mc = new HakkindaCanvas(root);
            root.canvasManager.setCurrentCanvas(mc);
        }

        if(quitbutton.IsClicked()) {
            System.exit(1);
        }

    }

    public void draw(Canvas canvas) {
        background.draw(canvas);
        playButton.draw(canvas);
        soundButton.draw(canvas);
        //scorebutton.draw(canvas);
        quitbutton.draw(canvas);
        hakkindabotton.draw(canvas);

        //creditsButton.draw(canvas);
    }

    public void keyPressed(int key) {
    }

    public void keyReleased(int key) {
    }

    public boolean backPressed() {
        return false;
    }

    public void touchDown(int x, int y, int id) {
        playButton.onHover(x,y);
        soundButton.IsClicked(x,y);
     //   scorebutton.onHover(x,y);
        quitbutton.IsClicked(x,y);
        hakkindabotton.onHover(x,y);


        if (soundButton.IsClicked()){
            if (backgroundsoundenable == true) {
            backgroundsoundenable = false;
            backgroundsound.stop();

            Log.d("abcd", "girdiiiiiii");
            }else{
                backgroundsoundenable = true;
                backgroundsound.load("sound/volkan.mp3");
                backgroundsound.prepare();
                backgroundsound.setLooping(true);
                backgroundsound.start();

            }
                }

        }
       // creditsButton.onHover(x,y);


    public void touchMove(int x, int y, int id) {
        playButton.onHover(x,y);
        soundButton.onHover(x,y);
       // scorebutton.onHover(x,y);
        quitbutton.IsClicked(x,y);
        hakkindabotton.onHover(x, y);

        //creditsButton.onHover(x,y);
    }

    public void touchUp(int x, int y, int id) {
        playButton.IsClicked(x, y);
        soundButton.onHover(x, y);
     //   scorebutton.IsClicked(x,y);
        quitbutton.IsClicked(x,y);
        hakkindabotton.IsClicked(x, y);



        // creditsButton.onHover();
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
