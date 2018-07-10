package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

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

        for (int i = 0; i < coinMaxFrameNum; i++) {
            coins[i] = Utils.loadImage(root, "coins/coin" + i + ".png");
            coinsSrcW[i] = coins[i].getWidth();
        }

        coinSource.set(coinSrcX, coinSrcY, coinSrcX + coinsSrcW[coinFrameNum], coinSrcY + coinSrcH);
        for (int i = 0; i < coinMaxDestination; i++) {
            coinDestination[i] = new Rect();

        }

    }

    public void coinCreate(){

            sagsol = randomGenerator.nextInt(100);
            Log.d("sagsol", "setCoin: " + sagsol);
            if (sagsol % 4 == 0) {
                coinDstX[coinCount] = randomGenerator.nextInt(gameCanvas.getWidth() - coinDstW);
                coinDstY[coinCount] = 0 - coinDstH;
                Log.d("solx", "setCoin: " + coinDstX[coinCount]);
            }
            else if (sagsol % 4 == 1) {
                coinDstY[coinCount] = randomGenerator.nextInt(gameCanvas.getHeight() - coinDstH);
                coinDstX[coinCount] = 0 - coinDstW;
                Log.d("sagx", "setCoin: " + coinDstX[coinCount]);
            }
            else if (sagsol % 4 == 2) {
                coinDstX[coinCount] = randomGenerator.nextInt(gameCanvas.getWidth() - coinDstW);
                coinDstY[coinCount] = gameCanvas.getHeight() + coinDstH;
                Log.d("solx", "setCoin: " + coinDstX[coinCount]);
            }
            else if (sagsol % 4 == 3) {
                coinDstY[coinCount] = randomGenerator.nextInt(gameCanvas.getHeight() - coinDstH);
                coinDstX[coinCount] = gameCanvas.getWidth() + coinDstW;
                Log.d("sagx", "setCoin: " + coinDstX[coinCount]);
            }
            coinTimeSecond = randomGenerator.nextInt(timeMax) + timeMin;

            coinDestination[coinCount].set(coinDstX[coinCount], coinDstY[coinCount], coinDstX[coinCount] + coinDstW, coinDstY[coinCount] + coinDstH);

            coinCount++;
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
