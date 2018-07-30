package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

import istanbul.gamelab.ngdroid.core.NgMediaPlayer;
import istanbul.gamelab.ngdroid.util.Utils;

public class Coin {

    private GameCanvas gameCanvas;
    private int coinsSrcW[];
    private Random randomGenerator;
    private Bitmap coins[];
    private Rect coinSource;
    private Rect[] coinDestination;
    private int coinSrcX, coinSrcY, coinSrcH;
    private int coinDstX[], coinDstY[], coinDstW, coinDstH, coinIndex;
    private int coinFrameNum;
    private int coinMaxFrameNum;
    private int coinMaxDestination;
    private NgApp root;
    private int sagsol;

    private int coinCount;
    private int coinTimeSecond;
    private int timeMin, timeMax;

    private NgMediaPlayer coinSound;
    private boolean coinSoundEnable;

    public Coin(NgApp root) {
        gameCanvas = new GameCanvas(root);
        randomGenerator = new Random();
        this.root = root;

    }

    public void setCoin(int width, int height) {
        timeMax = 5;
        timeMin = 5;
        coinCount = 0;

        coinTimeSecond = randomGenerator.nextInt(timeMax) + timeMin;

        coinMaxDestination = 10;
        randomGenerator = new Random();
        coinDestination = new Rect[coinMaxDestination];
        coinSource = new Rect();
        coinDstX = new int[coinMaxDestination];
        coinDstY = new int[coinMaxDestination];

        coinSrcH = 48;
        coinSrcX = 0;
        coinSrcY = 0;
        coinDstW = 64;
        coinDstH = 64;

        coinFrameNum = 0;
        coinMaxFrameNum = 8;

        coins = new Bitmap[coinMaxFrameNum];
        coinsSrcW = new int[coinMaxFrameNum];

        coinSoundEnable = false;
        coinSound = new NgMediaPlayer(root);
        coinSound.load("sound/coin.mp3");
        coinSound.prepare();
        coinSound.setLooping(false);

        for (int i = 0; i < coinMaxFrameNum; i++) {
            coins[i] = Utils.loadImage(root, "coins/coin" + i + ".png");
            coinsSrcW[i] = coins[i].getWidth();
        }

        coinSource.set(coinSrcX, coinSrcY, coinSrcX + coinsSrcW[coinFrameNum], coinSrcY + coinSrcH);
        for (int i = 0; i < coinMaxDestination; i++) {
            coinDestination[i] = new Rect();

        }

    }

    public void coinCreate(int index){

            sagsol = randomGenerator.nextInt(100);
            Log.d("sagsol", "setCoin: " + sagsol);
            if (sagsol % 4 == 0) {
                coinDstX[index] = randomGenerator.nextInt(gameCanvas.getWidth() - coinDstW);
                coinDstY[index] = 0 - coinDstH;
                Log.d("solx", "setCoin: " + coinDstX[index]);
            }
            else if (sagsol % 4 == 1) {
                coinDstY[index] = randomGenerator.nextInt(gameCanvas.getHeight() - coinDstH);
                coinDstX[index] = 0 - coinDstW;
                Log.d("sagx", "setCoin: " + coinDstX[index]);
            }
            else if (sagsol % 4 == 2) {
                coinDstX[index] = randomGenerator.nextInt(gameCanvas.getWidth() - coinDstW);
                coinDstY[index] = gameCanvas.getHeight() + coinDstH;
                Log.d("solx", "setCoin: " + coinDstX[index]);
            }
            else if (sagsol % 4 == 3) {
                coinDstY[index] = randomGenerator.nextInt(gameCanvas.getHeight() - coinDstH);
                coinDstX[index] = gameCanvas.getWidth() + coinDstW;
                Log.d("sagx", "setCoin: " + coinDstX[index]);
            }
            coinTimeSecond = randomGenerator.nextInt(timeMax) + timeMin;

            coinDestination[index].set(coinDstX[index], coinDstY[index], coinDstX[index] + coinDstW, coinDstY[index] + coinDstH);

            if (index == coinCount) coinCount++;


    }

    public void drawCoins(Canvas canvas, int geciciX, int geciciY, int i) {

            gameCanvas.drawRectBorder(canvas, coinDestination[i], Color.GREEN);
            canvas.drawBitmap(coins[coinFrameNum], coinSource, coinDestination[i], null);
            Log.d("kontrol", "coinIndexControl: " + i);
            coinMove(i, geciciX, geciciY);
    }

    public void coinAnimation() {

        coinFrameNum++;
        if (coinFrameNum > 7) {
            coinFrameNum = 0;
        }
    }

    private void coinMove(int i, int geciciX, int geciciY) {
        coinDstY[i] -= geciciY * 5;
        coinDstX[i] -= geciciX * 5;
        coinDestination[i].set(coinDstX[i], coinDstY[i], coinDstX[i] + coinDstW, coinDstY[i] + coinDstH);
    }

    public void startCoinSound() {

        coinSound.start();

    }

    public int getCoinCount() {
        return coinCount;
    }

    public int getCoinMaxDestination() {
        return coinMaxDestination;
    }

    public int getCoinTimeSecond() {
        return coinTimeSecond;
    }

    public Rect getCoinDestination(int index) {
        return coinDestination[index];
    }
}
