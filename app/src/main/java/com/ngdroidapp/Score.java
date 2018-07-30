package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

import istanbul.gamelab.ngdroid.util.Utils;

public class Score {

    private GameCanvas gameCanvas;
    private NgApp root;
    private Random randomGenerator;
    private Paint paint;
    private int distanceScoreTimeSecond;
    private int distanceScore;
    private double startTime;
    private double nowTime;

    public Score(NgApp root) {

        gameCanvas = new GameCanvas(root);
        randomGenerator = new Random();
        this.root = root;
    }

    public void setScore() {

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(64);

        distanceScore = 0;
        distanceScoreTimeSecond = 3;
        startTime = System.currentTimeMillis();
    }

    public void distanceUpdatingScore() {

        nowTime = System.currentTimeMillis();
        nowTime = (nowTime - startTime) / 1000;
        if ((int)nowTime == 3) {
            Log.i("scoreTime", "distanceUpdatingScore: " + nowTime);
            gameCanvas.setGoldPoint(10);
            startTime = System.currentTimeMillis();
        }
    }

    public void drawScore(Canvas canvas) {

        Log.i("scoreTime", "drawScore: " + gameCanvas.getGoldPoint());
        canvas.drawText("Score: " + gameCanvas.getGoldPoint(), 10, 50, paint);
    }

    public GameCanvas getGameCanvas() {
        return gameCanvas;
    }

    public void setGameCanvas(GameCanvas gameCanvas) {
        this.gameCanvas = gameCanvas;
    }
}


