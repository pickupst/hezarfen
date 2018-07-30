package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import istanbul.gamelab.ngdroid.util.Utils;

public class dangerAlert {

    private GameCanvas gameCanvas;

    private boolean isLive;

    private Bitmap alert[];
    private Rect alertSource;
    private Rect alertDestination;
    private int alertSrcX, alertSrcY, alertSrcH, alertSrcW;
    private int alertDstX, alertDstY, alertDstW, alertDstH;
    private int alertFrameNum;
    private int alertMaxFrameNum;


    private NgApp root;

    public static int alertCount;

    private int yon;

    private int id;


    public dangerAlert( NgApp root) { //ALERT DÜŞMAN EKRANA GİRDİĞİNDE KAYBOLACAK!
        this.root = root;

        isLive = false;

        alertCount++;
        id = alertCount;

    }

    public void setAlert(int x, int y) {

        alertDestination = new Rect();
        alertSource = new Rect();

        yon = -1;

        this.alertDstX = x;
        this.alertDstY = y;

        alertFrameNum = 0;
        alertMaxFrameNum = 8;

        alert = new Bitmap[alertMaxFrameNum];

        gameCanvas = new GameCanvas(root);


        for (int i = 0; i < alertMaxFrameNum; i++) {
            alert[i] = Utils.loadImage(root, "unlem/unlem" + i + ".png");
        }



        alertSrcW = alert[0].getWidth();
        alertSrcH = alert[0].getHeight();
        alertSrcX = 0;
        alertSrcY = 0;

        alertDstW = 64;
        alertDstH = 64;

    }



    public void drawAlert(Canvas canvas){

        if (isLive == true) {
            alertSource.set(alertSrcX, alertSrcY, alertSrcX + alertSrcW, alertSrcY + alertSrcH);
            alertDestination.set(alertDstX, alertDstY, alertDstX + alertDstW, alertDstY + alertDstH);
            //gameCanvas.drawRectBorder(canvas, alertDestination, Color.RED);
            canvas.drawBitmap(alert[alertFrameNum], alertSource, alertDestination, null);
        }
    }

    public void alertMove(int enemyX, int enemyY, int enemyW, int enemyH) {

        if (isLive == true) {

            alertKonum(enemyX, enemyY, enemyW, enemyH); // yönü sürekli kontrol edilsin ekranın neresinde sağ sol yukarı aşağı

            if (yon == 0) {
                if(enemyX < gameCanvas.getWidth() - alertDstW  && enemyX > 0) {
                    alertDstX = enemyX;
                    alertDstY = 0;
                }
            }else if (yon == 1) {
                if ( enemyY < gameCanvas.getHeight() - alertDstH && enemyY > 0) {
                    alertDstY = enemyY;
                    alertDstX = gameCanvas.getWidth() - alertDstW;
                }
            }else if (yon == 2) {
                if(enemyX < gameCanvas.getWidth() - alertDstW && enemyX > 0) {
                    alertDstX = enemyX;
                    alertDstY = gameCanvas.getHeight() - alertDstH;
                }
            }else if (yon == 3) {
                if ( enemyY < gameCanvas.getHeight() - alertDstH && enemyY > 0) {
                    alertDstY = enemyY;
                    alertDstX = 0;
                }
            }
            Log.i("dangerYon","id: " + id + " yon: " + yon);
            alertAnimation();
        }
    }

    private void alertKonum(int enemyX, int enemyY, int enemyW, int enemyH) {

        if (enemyX < gameCanvas.getWidth() - enemyW  && enemyX > 0 && enemyY < 0 - enemyH) {
            yon = 0;
        } else if (enemyX < gameCanvas.getWidth() - enemyW  && enemyX > 0 && enemyY > gameCanvas.getHeight()) {
            yon = 2;
        } else if (enemyY > 0  && enemyY < gameCanvas.getHeight() - enemyH && enemyX > gameCanvas.getWidth()) {
            yon = 1;
        } else if (enemyY > 0  && enemyY < gameCanvas.getHeight() - enemyH && enemyX < 0 - enemyW) {
            yon = 3;
        }

    }

    private void alertAnimation() {

        alertFrameNum++;
        if (alertFrameNum >= alertMaxFrameNum) {
            alertFrameNum = 0;
        }
    }

    public int getAlertDstH() {
        return alertDstH;
    }

    public int getAlertDstW() {
        return alertDstW;
    }

    public int getYon() {
        return yon;
    }

    public void setYon(int yon) {
        this.yon = yon;
    }

    public boolean getIsLive() {
        return isLive;
    }

    public void setIsLive(boolean live) {
        isLive = live;

        Log.i("kekeleme", "live " + live + " setIsLive: " + isLive);
    }
}
