package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import istanbul.gamelab.ngdroid.util.Utils;

public class Secret {

    private long startTime, nowTime, maxTime;

    private boolean scrIsLive;

    private GameCanvas gameCanvas;
    private Enemy enemy;
    private Player player;
    private NgApp root;
    private int secretMaxFrameNum, secretFrameNum;
    private Bitmap secrets[];
    private Rect secretSource;
    private Rect secretDestination;
    private int secretSrcX, secretSrcY, secretSrcW[], secretSrcH[];
    private int secretDstX, secretDstY, secretDstW, secretDstH;

    public Secret(NgApp root) {
        gameCanvas = new GameCanvas(root);
        this.root = root;
    }

    public void setSecret(Enemy enemy, int index, Player player) {

        scrIsLive = true;
        maxTime = 2;
        startTime = System.currentTimeMillis();


        this.player = player;
        this.enemy = enemy;

        secretFrameNum = 0;
        secretMaxFrameNum = 1;

        secrets = new Bitmap[secretMaxFrameNum];

        secretSource = new Rect();
        secretDestination = new Rect();

        secretSrcW = new int[secretMaxFrameNum];
        secretSrcH = new int[secretMaxFrameNum];

        secretDstW = player.getPlayerDstW();
        secretDstH = player.getPlayerDstH();

        secretSrcX = 0;
        secretSrcY = 0;

        for (int i = 0; i < secretMaxFrameNum; i++) {
            secrets[i] = Utils.loadImage(root, "secrets/secret" + i + ".png");
            secretSrcW[i] = secrets[i].getWidth();
            secretSrcH[i] = secrets[i].getHeight();
        }

        secretSource.set(secretSrcX, secretSrcY, secretSrcX + secretSrcW[secretFrameNum], secretSrcY + secretSrcH[secretFrameNum]);

        secretDestination = new Rect();

        konumBelirle(index);
    }


    private void konumBelirle(int enemyIndex){
        secretDstX = (int)enemy.getEnemyDstX()[enemyIndex];
        secretDstY = (int)enemy.getEnemyDstY()[enemyIndex];
    }

    public void secretAnimation(int geciciX, int geciciY) {

        isLive();
        if (scrIsLive) {
            secretFrameNum++;
            if (secretFrameNum > 10) {
                secretFrameNum = 0;
            }

            scrMove(geciciX, geciciY);
        }
    }

    private void isLive() {
        nowTime = System.currentTimeMillis();

        if ((int)((nowTime - startTime) / 1000) == maxTime) {
            scrIsLive = false;
            startTime = System.currentTimeMillis();
        }
    }

    public void drawScrlosion(Canvas canvas, int index) {
        if (scrIsLive) {
            if (secrets != null) {
                secretDestination.set(secretDstX, secretDstY, secretDstX + secretDstW, secretDstY + secretDstH);
                canvas.drawBitmap(secrets[secretFrameNum], secretSource, secretDestination, null);

            }
        }
    }

    private void scrMove(int geciciX, int geciciY) {
        if (scrIsLive) {
            //Ekranda sabit kalmaları için
            secretDstY -= geciciY * 5;
            secretDstX -= geciciX * 5;
        }
    }

    public boolean isScrIsLive() {
        return scrIsLive;
    }


}


