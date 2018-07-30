package com.ngdroidapp;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.util.Random;

import istanbul.gamelab.ngdroid.util.Utils;

public class Enemy {

    dangerAlert[] alerts;

    private int enemyUzaklik;
    private Bitmap bEnemyIleri[];
    private Rect enemySource, screen;
    private RectF realScreen;
    private RectF enemyDestination[];
    private int enemyDirection[];

    private int enemySrcX, enemySrcY, enemySrcW, enemySrcH;
    private float enemyDstX[];
    private float[] enemyDstY;
    private int enemyDstW;
    private int enemyDstH;
    private int enemyFrameNum, animationType, maxFrameNum;
    private int enemySpeed;
    private int enemyDagree[];
    private NgApp root;
    private RectF[] col;

    private int timeMin;
    private int timeMax;
    private Random randomGenerator;
    private int maxEnemyCount;
    private int enemyCount;
    private int enemyTimeSecond;
    private boolean enemyOnScreen[];

    private GameCanvas gameCanvas;

    private int width, height;
    private float ivmeX, ivmeY;

    public Enemy(NgApp root){

        maxEnemyCount = 0;

        this.root = root;
        gameCanvas = new GameCanvas(root);
        this.randomGenerator = new Random();
        screen = new Rect();
        realScreen = new RectF();

    }

    public void setEnemy(int getWidth, int getHeight) {
        enemyUzaklik = 3;

        col = new RectF[maxEnemyCount];

        ivmeX = 0;
        ivmeY = 0;

        timeMax = 3;
        timeMin = 2;

        screen.set(0 - enemyDstW * enemyUzaklik,0 - enemyDstH * enemyUzaklik , getWidth + enemyDstW , getHeight + enemyDstH); //Ekranın dışına çıkma durumunu kontrol edeceğiz.

        realScreen.set(0 - enemyDstW,0 - enemyDstH, getWidth + enemyDstW , getHeight + enemyDstH); //Alertin Ekranın dışına çıkma durumunu kontrol edeceğiz.

        enemyTimeSecond = randomGenerator.nextInt(timeMax) + timeMin;

        if (enemyCount < maxEnemyCount) {

            enemyDagree[enemyCount] = 0;
            maxFrameNum = 5;

            bEnemyIleri = new Bitmap[maxFrameNum];
            enemyDestination = new RectF[maxEnemyCount];
            enemyDirection = new int[maxEnemyCount];

            enemyDstH = 200;
            enemyDstW = 200;


            if (enemyOnScreen[enemyCount] == false) {

                for (int i = 0; i < maxEnemyCount; i++) {
                    enemyDestination[i] = new RectF();
                    col[i] = new RectF();

                    if (enemyCount == 0){//bir kere oluşturulsun
                        alerts = new dangerAlert[maxEnemyCount];
                        for (int j = 0; j < maxEnemyCount; j++) {
                            alerts[j] = new dangerAlert(root);
                        }
                    }

                }

                for (int i = 0; i < maxFrameNum; i++) {
                    bEnemyIleri[i] = Utils.loadImage(root, "enemy/plane"+ i +".png");
                }

            }

            enemyKonumBelirle(enemyCount, getWidth, getHeight);

            enemySource = new Rect();

            enemyFrameNum = 0;
            enemySpeed = 10;

            enemyOnScreen[enemyCount] = false;

            enemySrcW = 200;
            enemySrcH = 200;
            enemySrcX = 0;
            enemySrcY = 0;


            animationType = 2;

            height = gameCanvas.getHeight() / 2;
            width = gameCanvas.getWidth() / 2;

            enemyCount++;

        }

    }

    public void enemyKonumBelirle(int index, int width, int height){
        //Kuşun random konumlarda oluşması için;
        int dikeyYatay = randomGenerator.nextInt(2);
        if (dikeyYatay == 0) {
            dikeyYatay = randomGenerator.nextInt(2);
            if (dikeyYatay == 0) {
                enemyDstX[index] = 0 - enemyDstW * enemyUzaklik;
                enemyDstY[index] = randomGenerator.nextInt(height - enemyDstH);

                alerts[index].setAlert(0, (int) enemyDstY[index]);
                alerts[index].setYon(3);
            } else if (dikeyYatay == 1) {
                enemyDstX[index] = width + enemyDstW * enemyUzaklik;
                enemyDstY[index] = randomGenerator.nextInt(height - enemyDstH);

                alerts[index].setAlert(width - alerts[index].getAlertDstW(), (int) enemyDstY[index]);
                alerts[index].setYon(1);
            }
        } else if (dikeyYatay == 1) {
            dikeyYatay = randomGenerator.nextInt(2);
            if (dikeyYatay == 0) {
                enemyDstX[index] = randomGenerator.nextInt(width - enemyDstW);
                enemyDstY[index] = 0 - enemyDstH * enemyUzaklik;

                alerts[index].setAlert((int) enemyDstX[index], 0);
                alerts[index].setYon(0);
            } else if (dikeyYatay == 1) {
                enemyDstX[index] = randomGenerator.nextInt(width - enemyDstW);
                enemyDstY[index] = height + enemyDstH * enemyUzaklik;


                alerts[index].setAlert((int) enemyDstX[index], height - alerts[index].getAlertDstH());
                alerts[index].setYon(2);
            }
        }
    }


    public void drawEnemy(Canvas canvas, int index) {

            enemySource.set(enemySrcX, enemySrcY, enemySrcX + enemySrcW, enemySrcY + enemySrcH);
            enemyDestination[index].set(enemyDstX[index], enemyDstY[index], enemyDstX[index] + enemyDstW, enemyDstY[index] + enemyDstH);

            //gameCanvas.drawRectBorder(canvas, enemyDestination[index], Color.RED);
            canvas.drawBitmap(bEnemyIleri[enemyFrameNum], rotateEnemy(enemyDstX[index] + enemyDstW / 2,
                    enemyDstY[index] + enemyDstH / 2, enemyDstW, enemyDstH, enemyDagree[index]), null);

            col[index].set(enemyDstX[index] + 50, enemyDstY[index] + 50, enemyDstX[index] + enemyDstW - 50, enemyDstY[index] + enemyDstH - 50);
            //Collider Border
            //gameCanvas.drawRectBorder(canvas, col[index], Color.CYAN);

            alerts[index].drawAlert(canvas);
    }

    public void enemyMove(int geciciX, int geciciY) {

        enemyFrameNum++;
        if(enemyFrameNum > 4) { //Eğer animasyon sona geldiyse başa sarmakiçin.
            enemyFrameNum = 0;
        }


        for (int i = 0; i < enemyCount; i++) {

            alerts[i].alertMove((int) enemyDstX[i], (int) enemyDstY[i], enemyDstW, enemyDstH);

            //Ekranda sabit kalmaları için
            enemyDstY[i] -= geciciY * 5;
            enemyDstX[i] -= geciciX * 5;
            enemyOnScreen(i);


            enemyYonBelirle(i);


            ivmeX = Math.round((enemySpeed * Math.sin(Math.toRadians(enemyDagree[i]))));
            ivmeY = -1 * Math.round((enemySpeed * Math.cos(Math.toRadians(enemyDagree[i]))));

            if (Math.abs(ivmeX) == 1 && Math.abs(ivmeY) == 1 ) {
                ivmeY = ivmeY * 2;
            }

            enemyDstX[i] += ivmeX; //yukarı Git
            enemyDstY[i] += ivmeY;

            Log.i("kekeleme", i + ". alert ");
            alerts[i].setIsLive(!enemyDestination[i].intersect(realScreen));


        }

    }

    public static Matrix rotateEnemy(float px, float py, float bW, float bH, float angle){

        Matrix matrix = new Matrix();
        matrix.postTranslate(-bW / 2, -bH / 2);
        matrix.postRotate(angle);
        matrix.postTranslate(px, py);
        return matrix;
    }

    private void enemyYonBelirle(int index) {

        //ALT
            //SOL
        if (enemyDstX[index] > width && enemyDstY[index] < height) {
            enemyDagree[index] =(360 - (int) (180 - Math.toDegrees(Math.asin((enemyDstX[index] - width) /
                            (Math.sqrt(Math.pow(enemyDstX[index] - width , 2) + Math.pow(enemyDstY[index] - height,2)))))));

            enemyDirection[index] = 2;
        }
            //SAĞ
        else if (enemyDstX[index] < width && enemyDstY[index] < height) {
            enemyDagree[index] = (int) (180 - Math.toDegrees(Math.asin((width - enemyDstX[index]) /
                            (Math.sqrt(Math.pow(width - enemyDstX[index],2) + Math.pow(height - enemyDstY[index],2))))));

            enemyDirection[index] = 3;
        }
        //ÜST
            //SOL
        else if (enemyDstX[index] > width && enemyDstY[index] > height) {
            enemyDagree[index] = 360 - ((int) ( Math.toDegrees(Math.asin((enemyDstX[index] - width) /
                            (Math.sqrt(Math.pow(enemyDstX[index] - width ,2) + Math.pow(enemyDstY[index] - height,2)))))));

            enemyDirection[index] = 0;
        }
            //SAĞ
        else if (enemyDstX[index] < width && enemyDstY[index] > height) {
            enemyDagree[index] = (int) (Math.toDegrees(Math.asin((width - enemyDstX[index]) /
                            (Math.sqrt(Math.pow(width - enemyDstX[index],2) + Math.pow(enemyDstY[index] - height,2))))));

            enemyDirection[index] = 1;
        }


    }

    private void enemyOnScreen(int index) {

        if (!gameCanvas.isColliding(enemyDestination[index], screen)) {//Ekran dışındaysa
            setEnemyOnScreen(index, false);

            if (getEnemyTimeSecond() == gameCanvas.getNowTime()) {// ve Zamanı geldiyse
                setEnemyOnScreen(index, true);

                enemyTimeSecond = randomGenerator.nextInt(timeMax) + timeMin;

                gameCanvas.setNowTime(0);
                gameCanvas.setStartTime(System.currentTimeMillis());

                enemyKonumBelirle(index,gameCanvas.getWidth(), gameCanvas.getHeight());

            }
        }else setEnemyOnScreen(index, true);

    }


    public int getEnemySpeed() {
        return enemySpeed;
    }

    public void setEnemySpeed(int enemySpeed) {
        this.enemySpeed = enemySpeed;
    }

    public void setEnemyDagree(int[] enemyDagree) {
        this.enemyDagree = enemyDagree;
    }

    public int getEnemyDagree(int index) {
        return enemyDagree[index];
    }

    public RectF getCol(int index) {
        return col[index];
    }

    public boolean[] getEnemyOnScreen() {
        return enemyOnScreen;
    }

    public boolean getEnemyOnScreen(int index) {
        return enemyOnScreen[index];
    }

    public void setEnemyOnScreen(boolean[] enemyOnScreen) {
        this.enemyOnScreen = enemyOnScreen;
    }

    public void setEnemyOnScreen(int index, boolean value) {
        this.enemyOnScreen[index] = value;
    }

    public int getEnemyTimeSecond() {
        return enemyTimeSecond;
    }

    public void setEnemyTimeSecond(int enemyTimeSecond) {
        this.enemyTimeSecond = enemyTimeSecond;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public int getMaxEnemyCount() {
        return maxEnemyCount;
    }

    public void incMaxEnemyCount(int maxEnemyCount) {
        this.maxEnemyCount += maxEnemyCount;
    }

    public void setEnemyCount(int enemyCount) {
        this.enemyCount = enemyCount;
    }

    public float[] getEnemyDstX() {
        return enemyDstX;
    }

    public void setEnemyDstX(float[] enemyDstX) {
        this.enemyDstX = enemyDstX;
    }

    public float[] getEnemyDstY() {
        return enemyDstY;
    }

    public void setEnemyDstY(float[] enemyDstY) {
        this.enemyDstY = enemyDstY;
    }

    public RectF getEnemyDestination(int index) {
        return enemyDestination[index];
    }

}
