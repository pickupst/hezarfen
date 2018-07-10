package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

import istanbul.gamelab.ngdroid.util.Utils;

public class Seagull {
    private NgApp root;
    GameCanvas gameCanvas;



    private int scale;
    private int seagullDagree[];
    private Bitmap bSeagullIleri[];
    private Rect seagullDestination[], seagullSource, screen;
    private int seagullDstX[];
    private int seagullDstY[];
    private int seagullDstW;
    private int seagullDstH;
    private int seagullTimeSecond;
    private int vSeagullTimeSecond;
    private int seagullFrameNum, seagullMaxFrameNum;
    private int seagullSpeed;
    private int seagullDirection[];
    private int maxSeagullCount;
    private boolean seagullOnScreen[];
    private int seagullCount;
    private Random randomGenerator;
    private int timeMin;
    private int timeMax;
    private static int vTimeMax;
    private static int vTimeMin;

    private int vTabanAdet;


    public Seagull(NgApp root){
        gameCanvas = new GameCanvas(root);
        this.randomGenerator = new Random();
        screen = new Rect();

        vTimeMax = 3;
        vTimeMin = 3;

        this.root = root;

    }

    public void setSeagull(int width, int height) {
        vTabanAdet = 0;

        timeMax = 5;
        timeMin = 5;

        screen.set(0 - seagullDstW * 2 - (vTabanAdet * seagullDstW),0 - seagullDstH * 2 - (vTabanAdet * seagullDstH), width + seagullDstW + (vTabanAdet * seagullDstW), height + seagullDstW + (vTabanAdet * seagullDstH)); //Ekranın dışına çıkma durumunu kontrol edeceğiz.

        scale = 2;
        seagullTimeSecond = randomGenerator.nextInt(timeMax) + timeMin;

        if (seagullCount < maxSeagullCount) {
            Log.i("seagullCount", "setSeagull: " + seagullCount);
            seagullDagree[seagullCount] = 0;
            seagullMaxFrameNum = 3;
            bSeagullIleri = new Bitmap[seagullMaxFrameNum];

            seagullDestination = new Rect[maxSeagullCount];

            seagullDstW = 55 * scale;
            seagullDstH = 35 * scale;

            if (seagullOnScreen[seagullCount] == false) {
                for (int i = 0; i < maxSeagullCount; i++) {
                    seagullDestination[i] = new Rect();
                }

                for (int i = 0; i < seagullMaxFrameNum; i++) {
                    bSeagullIleri[i] = Utils.loadImage(root, "seagull/seagull" + i + ".png");
                    bSeagullIleri[i] = Bitmap.createScaledBitmap(bSeagullIleri[i],seagullDstW, seagullDstH,false);
                }
            }
            seagullSource = new Rect();

            seagullKonumYonBelirle(seagullCount, width, height);

            seagullFrameNum = 0;
            seagullSpeed = 12;

            seagullAciHesapla(seagullCount);
            Log.i("count", "setSeagull: " + seagullCount);


            seagullOnScreen[seagullCount] = false;
            seagullCount++;
        }
    }

    private void seagullKonumYonBelirle(int index, int width, int height){
        //Kuşun random konumlarda oluşması için;
        int dikeyYatay = randomGenerator.nextInt(2);
        if (dikeyYatay == 0) {
            dikeyYatay = randomGenerator.nextInt(2);
            if (dikeyYatay == 0) {
                seagullDirection[index] = 3; //2 yukarı 1 sola 3 sağa 0 aşağı
                seagullDstX[index] = 0 - seagullDstW;
                seagullDstY[index] = randomGenerator.nextInt(height - seagullDstH);
            } else if (dikeyYatay == 1) {
                seagullDirection[index] = 1; //2 yukarı 1 sola 3 sağa 0 aşağı
                seagullDstX[index] = width + seagullDstW;
                seagullDstY[index] = randomGenerator.nextInt(height - seagullDstH);
            }
        } else if (dikeyYatay == 1) {
            dikeyYatay = randomGenerator.nextInt(2);
            if (dikeyYatay == 0) {
                seagullDirection[index] = 0; //2 yukarı 1 sola 3 sağa 0 aşağı
                seagullDstX[index] = randomGenerator.nextInt(width - seagullDstW);
                seagullDstY[index] = 0 - seagullDstH;
            } else if (dikeyYatay == 1) {
                seagullDirection[index] = 2; //2 yukarı 1 sola 3 sağa 0 aşağı
                seagullDstX[index] = randomGenerator.nextInt(width - seagullDstW);
                seagullDstY[index] = height + seagullDstH;
            }
        }
    }

    public void drawSeagull(Canvas canvas, int index) {

            seagullSource.set(0, 0, 0 + seagullDstW, 0 + seagullDstH);
            //Log.i("imiz", "drawSeagull: " + i);
            seagullDestination[index].set(seagullDstX[index], seagullDstY[index], seagullDstX[index] + seagullDstW, seagullDstY[index] + seagullDstH);
            //Log.i("seagulDest", "X :" + seagullDstX + " Y :" + seagullDstY + " X2:" + (seagullDstX + seagullDstW) + " Y2 :" + (seagullDstY + seagullDstH) + "  " + getWidth() + "  " + getHeight());
            gameCanvas.drawRectBorder(canvas, seagullDestination[index], Color.BLUE);
            canvas.drawBitmap(bSeagullIleri[seagullFrameNum], Player.rotatePlayer(seagullDstX[index] + seagullDstW / 2, seagullDstY[index] + seagullDstH / 2, seagullDstW, seagullDstH, seagullDagree[index]), null);
            Log.i("seagullCount", "drawSeagull: " + seagullCount);
    }

    public void vSeagullCreate(int vTabanAdet, int width, int height){
        //en fazla ekranın yarısın kaplayabilir

        Log.i("vSea", "taban V: " + vTabanAdet);
        int kullanilabilirSeagull = 0;

        for (int i = 0; i < seagullCount; i++) {

            if (getSeagullOnScreen(i) == false) kullanilabilirSeagull++;
            else kullanilabilirSeagull--;

        }

        Log.i("inScreen", "KullanılabilirSeagull: " + kullanilabilirSeagull);

        if (kullanilabilirSeagull >= (vTabanAdet * 2)) {

            Log.i("vSea", "taban V: " + vTabanAdet);

            int index = 0;

            int dikeyYatay = randomGenerator.nextInt(2);
            int dikeyYatay2 = randomGenerator.nextInt(2);

            int randY = randomGenerator.nextInt(height - (vTabanAdet * seagullDstH));
            int randX = randomGenerator.nextInt(width - (seagullDstW * vTabanAdet));

            for (int i = vTabanAdet; i > 0; i--) { //Derinlik kadar
                Log.i("vSea", "i : " + i);
                for (int j = 0; j < 2; j++) { //Her derinlikte iki kuş
                    Log.i("vSea", "j : " + j);
                    if (getSeagullOnScreen(index) == false) {//False ise ekranda değildir
                        Log.i("vSea", "getonindex index: " + index + " deger : " + getSeagullOnScreen(index));
                        if (dikeyYatay == 0) {
                            if (dikeyYatay2 == 0) {
                                seagullDirection[index] = 3; //2 yukarı 1 sola 3 sağa 0 aşağı
                                seagullDstX[index] = 0 - (seagullDstW * i);    // DERİNLİK
                                seagullDstY[index] = randY + (((seagullDstH / 2) * i) * j) - (Math.abs((seagullDstH / 2) * (j - 1) * i));
                            } else if (dikeyYatay2 == 1) {
                                seagullDirection[index] = 1; //2 yukarı 1 sola 3 sağa 0 aşağı
                                seagullDstX[index] = width + (seagullDstW * i); // DERİNLİK
                                seagullDstY[index] = randY + (((seagullDstH / 2) * i) * j) - (Math.abs((seagullDstH / 2) * (j - 1) * i));
                            }
                        } else if (dikeyYatay == 1) {
                            if (dikeyYatay2 == 0) {
                                seagullDirection[index] = 0; //2 yukarı 1 sola 3 sağa 0 aşağı
                                seagullDstY[index] = 0 - (i * seagullDstH);    // DERİNLİK
                                seagullDstX[index] = randX + (((seagullDstW / 2) * i) * j) - (Math.abs((seagullDstW / 2) * (j - 1) * i));
                            } else if (dikeyYatay2 == 1) {
                                seagullDirection[index] = 2; //2 yukarı 1 sola 3 sağa 0 aşağı
                                seagullDstX[index] = randX + (((seagullDstW / 2) * i) * j) - (Math.abs((seagullDstW / 2) * (j - 1) * i));
                                seagullDstY[index] = height + (i * seagullDstH);   // DERİNLİK
                            }
                        }

                        seagullOnScreen[index] = true;
                        seagullAciHesapla(index);
                    }
                    index++;
                }

            }

        }
    }

    public void seagullMove(int geciciX, int geciciY) {

        seagullFrameNum++;
        if(seagullFrameNum >= seagullMaxFrameNum) { //Eğer animasyon sona geldiyse başa sarmakiçin.
            seagullFrameNum = 0;
        }

        for (int i = 0; i < seagullCount; i++) {
            if (seagullDirection[i] == 0) {
                seagullDstY[i] += seagullSpeed;
            } else if (seagullDirection[i] == 1) {
                seagullDstX[i] -= seagullSpeed;
            } else if (seagullDirection[i] == 2) {
                seagullDstY[i] -= seagullSpeed;
            } else if (seagullDirection[i] == 3) {
                seagullDstX[i] += seagullSpeed;
            }
            seagullDstY[i] -= geciciY * 5;
            seagullDstX[i] -= geciciX * 5;
            seagullOnScreen(i);
            Log.i("inScreen", "seagullMove: " + i + ". Kuş " + seagullOnScreen[i]);
        }
    }

    public void seagullAciHesapla(int index){
        //if(0 != (seagullDstY - screenMidPointY)) seagullDagreeLast = Math.toDegrees(Math.atan((seagullDstX - screenMidPointX) / (seagullDstY - screenMidPointY)));
        //seagullDagree = (int) (360 - seagullDagreeLast);
        if (seagullDirection[index] == 0) {
            seagullDagree[index] = -180;
        } else if (seagullDirection[index] == 1) {
            seagullDagree[index] = -90;
        } else if (seagullDirection[index] == 2) {
            seagullDagree[index] = 0;
        } else if (seagullDirection[index] == 3) {
            seagullDagree[index] = 90;
        }
    }

    private void seagullOnScreen(int index) {
        Log.i("onEkran", "seagullOnScreen: " + index + ". kuş " + getSeagullOnScreen(index));
       // int nowTime =(int) ((System.currentTimeMillis() - startTime) / 1000);

        if (!gameCanvas.isColliding(seagullDestination[index], screen)) {//Ekran dışındaysa
            setSeagullOnScreen(index, false);

            if (getSeagullTimeSecond() == gameCanvas.getNowTime()) {// ve Zamanı geldiyse
                setSeagullOnScreen(index, true);

                seagullTimeSecond = randomGenerator.nextInt(timeMax) + timeMin;

                gameCanvas.setNowTime(0);
                gameCanvas.setStartTime(System.currentTimeMillis());

                seagullKonumYonBelirle(index,gameCanvas.getWidth(), gameCanvas.getHeight());
                seagullAciHesapla(index);
            }
        }else setSeagullOnScreen(index, true);

        Log.i("seagullTimeSecond", "seagullOnScreen: " + seagullTimeSecond + "  " + gameCanvas.getNowTime() + "  " +gameCanvas.getStartTime());

    }


    public int getSeagullTimeSecond() {
        return seagullTimeSecond;
    }

    public void setSeagullTimeSecond(int seagullTimeSecond) {
        this.seagullTimeSecond = seagullTimeSecond;
    }

    public int getMaxSeagullCount() {
        return maxSeagullCount;
    }

    public void setMaxSeagullCount(int maxSeagullCount) {
        this.maxSeagullCount = maxSeagullCount;
    }

    public int getSeagullCount() {
        return seagullCount;
    }

    public void setSeagullCount(int seagullCount) {
        this.seagullCount = seagullCount;
    }

    public int[] getSeagullDagree() {
        return seagullDagree;
    }

    public void setSeagullDagree(int[] seagullDagree) {
        this.seagullDagree = seagullDagree;
    }

    public Rect[] getSeagullDestination() {
        return seagullDestination;
    }

    public Rect getSeagullDestination(int index) {
        return seagullDestination[index];
    }

    public void setSeagullDestination(Rect[] seagullDestination) {
        this.seagullDestination = seagullDestination;
    }

    public int[] getSeagullDstX() {
        return seagullDstX;
    }

    public void setSeagullDstX(int[] seagullDstX) {
        this.seagullDstX = seagullDstX;
    }

    public int[] getSeagullDstY() {
        return seagullDstY;
    }

    public void setSeagullDstY(int[] seagullDstY) {
        this.seagullDstY = seagullDstY;
    }

    public boolean[] getSeagullOnScreen() {
        return seagullOnScreen;
    }

    public boolean getSeagullOnScreen(int index) {
        return seagullOnScreen[index];
    }

    public void setSeagullOnScreen(boolean[] seagullOnScreen) {
        this.seagullOnScreen = seagullOnScreen;
    }

    public void setSeagullOnScreen(int index, boolean value) {
        this.seagullOnScreen[index] = value;
    }

    public int[] getSeagullDirection() {
        return seagullDirection;
    }

    public int getSeagullDirection(int index) {
        return seagullDirection[index];
    }

    public void setSeagullDirection(int[] seagullDirection) {
        this.seagullDirection = seagullDirection;
    }

    public int getTimeMin() {
        return timeMin;
    }

    public void setTimeMin(int timeMin) {
        this.timeMin = timeMin;
    }

    public int getTimeMax() {
        return timeMax;
    }

    public void setTimeMax(int timeMax) {
        this.timeMax = timeMax;
    }


    public int getSeagullDstW() {
        return seagullDstW;
    }

    public int getSeagullDstH() {
        return seagullDstH;
    }

    public int getvTabanAdet() {
        return vTabanAdet;
    }

    public int getvSeagullTimeSecond() {
        return vSeagullTimeSecond;
    }

    public void setvSeagullTimeSecond(int vSeagullTimeSecond) {
        this.vSeagullTimeSecond = vSeagullTimeSecond;
    }

    public static int getvTimeMax() {
        return vTimeMax;
    }

    public static int getvTimeMin() {
        return vTimeMin;
    }
}
