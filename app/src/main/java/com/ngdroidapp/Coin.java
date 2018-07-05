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
    private Rect coinSource, coinDestination[];
    private int coinSrcX, coinSrcY, coinSrcH;
    private int coinDstX[], coinDstY[], coinDstW, coinDstH, coinIndex;
    private int coinFrameNum, coinMaxFrameNum, coinMaxDestination;
    private NgApp root;

    public Coin(NgApp root) {

        gameCanvas = new GameCanvas(root);
        randomGenerator = new Random();
        this.root = root;

    }

    public void setCoin(int width, int height) {

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
        for (int k = 0; k < coinMaxDestination; k++) {
            coinDstX[k] = randomGenerator.nextInt(width - coinDstW);
            coinDstY[k] = randomGenerator.nextInt(height - coinDstH);
        }

        coinSource.set(coinSrcX, coinSrcY, coinSrcX + coinsSrcW[coinFrameNum], coinSrcY + coinSrcH);
        for (int i = 0; i < coinMaxDestination; i++) {
            coinDestination[i] = new Rect();
            coinDestination[i].set(coinDstX[i], coinDstY[i], coinDstX[i] + coinDstW, coinDstY[i] + coinDstH);
        }
    }

    public void drawCoins(Canvas canvas, int geciciX, int geciciY) {

        for (int i = 0; i < coinMaxDestination; i++) {
            gameCanvas.drawRectBorder(canvas, coinDestination[i], Color.GREEN);
            canvas.drawBitmap(coins[coinFrameNum], coinSource, coinDestination[i], null);
            Log.d("kontrol", "coinIndexControl: " + i);
            coinMove(i, geciciX, geciciY);
        }
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
}
