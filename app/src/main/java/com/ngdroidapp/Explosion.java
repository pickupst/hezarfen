package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

import istanbul.gamelab.ngdroid.core.NgMediaPlayer;
import istanbul.gamelab.ngdroid.util.Utils;

public class Explosion {

    private long startTime, nowTime, maxTime;

    private boolean expIsLive;

    private GameCanvas gameCanvas;
    private Enemy enemy;
    private Player player;
    private NgApp root;
    private int explosionMaxFrameNum, explosionFrameNum;
    private Bitmap explosion[];
    private Rect explosionSource;
    private Rect explosionDestination;
    private int explosionSrcX, explosionSrcY, explosionSrcW[], explosionSrcH[];
    private int explosionDstX, explosionDstY, explosionDstW, explosionDstH;

    private NgMediaPlayer explosionSound;
    private boolean explosionSoundEnable;

    public Explosion(NgApp root) {
        gameCanvas = new GameCanvas(root);
        this.root = root;
    }

    public void setExplosion(Enemy enemy,int index, Player player) {

        expIsLive = true;
        maxTime = 2;
        startTime = System.currentTimeMillis();


        this.player = player;
        this.enemy = enemy;

        explosionFrameNum = 0;
        explosionMaxFrameNum = 11;

        explosion = new Bitmap[explosionMaxFrameNum];

        explosionSource = new Rect();
        explosionDestination = new Rect();

        explosionSrcW = new int[explosionMaxFrameNum];
        explosionSrcH = new int[explosionMaxFrameNum];

        explosionDstW = player.getPlayerDstW();
        explosionDstH = player.getPlayerDstH();

        explosionSrcX = 0;
        explosionSrcY = 0;

        for (int i = 0; i < explosionMaxFrameNum; i++) {
            explosion[i] = Utils.loadImage(root, "explosion/explosion" + i + ".png");
            explosionSrcW[i] = explosion[i].getWidth();
            explosionSrcH[i] = explosion[i].getHeight();
        }

        explosionSource.set(explosionSrcX, explosionSrcY, explosionSrcX + explosionSrcW[explosionFrameNum], explosionSrcY + explosionSrcH[explosionFrameNum]);

        explosionDestination = new Rect();

        explosionDstX = (int)enemy.getEnemyDstX()[index];
        explosionDstY = (int)enemy.getEnemyDstY()[index];

        explosionSoundEnable = false;
        explosionSound = new NgMediaPlayer(root);
        explosionSound.load("sound/Explosion.mp3");
        explosionSound.prepare();
        explosionSound.setLooping(false);

        }


    public void explosionAnimation(int geciciX, int geciciY) {

        isLive();
        if (expIsLive) {
            explosionFrameNum++;
            if (explosionFrameNum > 10) {
                explosionFrameNum = 0;
            }

            expMove(geciciX, geciciY);
        }
    }

    private void isLive() {
        nowTime = System.currentTimeMillis();

        if ((int)((nowTime - startTime) / 1000) == maxTime) {
            expIsLive = false;
            startTime = System.currentTimeMillis();
        }
    }

    public void drawExplosion(Canvas canvas, int index) {
        if (expIsLive) {
            if (explosion != null) {
                explosionDestination.set(explosionDstX, explosionDstY, explosionDstX + explosionDstW, explosionDstY + explosionDstH);
                canvas.drawBitmap(explosion[explosionFrameNum], explosionSource, explosionDestination, null);

            }
        }
    }

    private void expMove(int geciciX, int geciciY) {
    if (expIsLive) {
        //Ekranda sabit kalmaları için
        explosionDstY -= geciciY * 5;
        explosionDstX -= geciciX * 5;
        }
    }

    public boolean isExpIsLive() {
        return expIsLive;
    }

    public void startExplosionSound() {

        explosionSound.start();

    }


}


