package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.provider.DocumentsContract;
import android.util.Log;

import istanbul.gamelab.ngdroid.util.Utils;

public class Player {

    private Bitmap bPlayerIleri[];
    private Rect playerDestination, playerSource;
    private int playerSrcX, playerSrcY, playerSrcW, playerSrcH;
    private int playerDstX, playerDstY, playerDstW, playerDstH;
    private int playerW, playerH;
    private int playerFrameNum, animationType, maxFrameNum;
    private int playerSpeed, playerDirection;
    private int playerDagree;
    private NgApp root;

    private GameCanvas gameCanvas;

    public Player(NgApp root){

        this.root = root;
        gameCanvas = new GameCanvas(root);

    }

    public void setPlayer(int getWidth, int getHeight) {
        maxFrameNum = 5;
        bPlayerIleri = new Bitmap[maxFrameNum];
        for (int i = 0; i < maxFrameNum; i++) {
            bPlayerIleri[i] = Utils.loadImage(root, "playerileri/plane"+ i +".png");
        }

        playerDestination = new Rect();
        playerSource = new Rect();

        playerSrcW = 200;
        playerSrcH = 200;
        playerSrcX = 0;
        playerSrcY = 0;

        playerDstH = 200;
        playerDstW = 200;

        playerDstX = (getWidth / 2) - (playerSrcW / 2);
        playerDstY = (getHeight / 2) - (playerSrcH / 2);

        playerFrameNum = 0;
        animationType = 2;
        playerSpeed = 2;
        playerDirection = 2; // 2 ileri,,, 1 sola, 3 Sağa

    }


    public void drawPlayer(Canvas canvas, int screenMidPointX, int screenMidPointY) {

        playerSource.set(playerSrcX, playerSrcY, playerSrcX + playerSrcW, playerSrcY + playerSrcH);
        playerDestination.set(playerDstX, playerDstY, playerDstX + playerDstW, playerDstY + playerDstH);

        gameCanvas.drawRectBorder(canvas, playerDestination, Color.RED);
        canvas.drawBitmap(bPlayerIleri[playerFrameNum], rotatePlayer(screenMidPointX, screenMidPointY, playerDstW, playerDstH, playerDagree) , null);
    }

    public void playerMove(boolean isTouch, int touchCount, boolean isRectTouches[],Rect leftLane, Rect rightLane, Rect rectTouches[]) {

        playerFrameNum++;
        if(playerFrameNum > 4) { //Eğer animasyon sona geldiyse başa sarmakiçin.
            playerFrameNum = 0;
        }

        playerYonBelirle(isTouch, touchCount, isRectTouches, leftLane, rightLane, rectTouches);

    }

    public static Matrix rotatePlayer(float px, float py, float bW, float bH, float angle){

        Matrix matrix = new Matrix();
        matrix.postTranslate(-bW / 2, -bH / 2);
        matrix.postRotate(angle);
        matrix.postTranslate(px, py);
        return matrix;
    }

    private void playerYonBelirle(boolean isTouch, int touchCount, boolean isRectTouches[],Rect leftLane, Rect rightLane, Rect rectTouches[]) {

        if (isTouch == false) playerDirection = 2;
        else{           //Ekranda tek dokunuş var
            if(touchCount == 1) {
                //android.util.Log.i("empty", "touchUp:0 " + rectTouches[0].isEmpty());
                if (isRectTouches[0] == true) {
                    if (gameCanvas.isColliding(rectTouches[0], rightLane)) {
                        playerDirection = 3;
                    } else if (gameCanvas.isColliding(rectTouches[0], leftLane)) {
                        playerDirection = 1;
                    }
                }
                //android.util.Log.i("empty", "touchUp:1 " + rectTouches[1].isEmpty());
                if (isRectTouches[1] == true) {
                    if (gameCanvas.isColliding(rectTouches[1], rightLane)) {
                        playerDirection = 3;
                    } else if (gameCanvas.isColliding(rectTouches[1], leftLane)) {
                        playerDirection = 1;
                    }
                }
            } else if (touchCount == 2){

                if (gameCanvas.isColliding(rectTouches[0], rightLane)) {
                    if(gameCanvas.isColliding(rectTouches[1], leftLane)) {
                        playerDirection = 2;
                    }
                }
                else if(gameCanvas.isColliding(rectTouches[0], leftLane)) {
                    if (gameCanvas.isColliding(rectTouches[1], rightLane)) {
                        playerDirection = 2;
                    }
                }
            }

        }

        //Açısını değiştirerek dönmesini sağlıyoruz.
        if (playerDirection == 3) { //sağa da git
            if (playerDagree == 360) playerDagree = 0;
            playerDagree ++;
            Log.i("degree", "sağa Açımız: " + playerDagree);

        }
        else if (playerDirection == 1) { //sola da git
            if (playerDagree == 0) playerDagree = 360;
            playerDagree --;
            Log.i("degree", "sola Açımız: " + playerDagree);

        }

    }

    public int getPlayerSpeed() {
        return playerSpeed;
    }

    public void setPlayerSpeed(int playerSpeed) {
        this.playerSpeed = playerSpeed;
    }

    public void setPlayerDagree(int playerDagree) {
        this.playerDagree = playerDagree;
    }

    public int getPlayerDagree() {
        return playerDagree;
    }
}
