package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;
import java.util.Vector;

import istanbul.gamelab.ngdroid.base.BaseCanvas;

import istanbul.gamelab.ngdroid.util.Utils;


/**
 * Created by noyan on 24.06.2016.
 * Nitra Games Ltd.
 */


public class GameCanvas extends BaseCanvas {

    private int goldPoint;

    private boolean isGameOver;

    private Player player;
    private Seagull seagull;
    private Coin coin;

    private static long startTime, vStartTime, coinStartTime;

    private static int nowTime, vNowTime;

    public Random randomGenerator;

    private Bitmap backGround;
    private Rect backGroundSource, backGroundDestination;
    private int bgScreenW, bgScreenH;
    private int bgScreenStartX, bgScreenStartY;

    private boolean isTouch;

    private Rect rightLane, leftLane;

    public int screenMidPointX, screenMidPointY;

    private Rect rectTouches[];
    private boolean isRectTouches[];

    private int touchCount;

    private int geciciX, geciciY;



    private void setBackGround() {
        bgScreenH = getHeight() / 5;
        bgScreenW = getWidth() / 5;

        backGround = Utils.loadImage(root, "background.jpg");

        backGroundSource = new Rect();
        backGroundDestination = new Rect();

        bgScreenStartX = backGround.getWidth()/2;
        bgScreenStartY = backGround.getHeight()/2;

        geciciX = bgScreenStartX;
        geciciY = bgScreenStartY;
    }

    private void setOther() {

        goldPoint = 0;
        randomGenerator = new Random();

        seagull.setvSeagullTimeSecond(randomGenerator.nextInt(Seagull.getvTimeMax()) + Seagull.getvTimeMin());
        seagull.setSeagullTimeSecond(3);  //Aşağıda
        seagull.setMaxSeagullCount(100);
        seagull.setSeagullCount(0);
        seagull.setSeagullDagree(new int[seagull.getMaxSeagullCount()]);
        seagull.setSeagullDirection(new int[seagull.getMaxSeagullCount()]);
        seagull.setSeagullDstX(new int[seagull.getMaxSeagullCount()]);
        seagull.setSeagullDstY(new int[seagull.getMaxSeagullCount()]);
        seagull.setSeagullOnScreen(new boolean[seagull.getMaxSeagullCount()]);

        screenMidPointX = getWidth() / 2;
        screenMidPointY = getHeight() / 2;

        rightLane = new Rect();
        rightLane.set(screenMidPointX, 0,getWidth(),getHeight());

        leftLane = new Rect();
        leftLane.set(0,0,screenMidPointX, getHeight());

        rectTouches = new Rect[2];
        rectTouches[0] = new Rect();
        rectTouches[1] = new Rect();

        isRectTouches = new boolean[2];
        isRectTouches[0] = false;
        isRectTouches[1] = false;

        touchCount = 0;
        player.setPlayerDagree(0);
    }

    public GameCanvas(NgApp ngApp) {
        super(ngApp);
    }

    public void setup() {

        isGameOver = false;

        player = new Player(root);
        seagull = new Seagull(root);
        coin = new Coin(root);

        for (int i = 0; i < seagull.getMaxSeagullCount(); i++) {
            seagull.setSeagullOnScreen(i,false);
        }

        startTime = System.currentTimeMillis();
        vStartTime = System.currentTimeMillis();
        coinStartTime = System.currentTimeMillis();

        isTouch = false;

        setOther();
        player.setPlayer(getWidth(), getHeight());
        //setSeagull();
        setBackGround();

        coin.setCoin(getWidth(), getHeight());

    }

    public void update() {

        backgroundMove();
        player.playerMove(isTouch, touchCount, isRectTouches, leftLane, rightLane, rectTouches);

        createSeagull();

        if (seagull.getSeagullCount() >= 0) seagull.seagullMove(geciciX, geciciY); //ekranda martı varsa

        createCoin();

        coin.coinAnimation();
    }

    public void draw(Canvas canvas) {

        if (isGameOver == false) {

            drawBackGround(canvas);
            player.drawPlayer(canvas, screenMidPointX, screenMidPointY);

            for (int i = 0; i < seagull.getSeagullCount(); i++) {
                if (seagull.getSeagullCount() >= 0) {
                    seagull.drawSeagull(canvas, i); //ekranda martı varsa
                    kusVSplayerCarpisma(i);
                }
            }

            for (int i = 0; i < coin.getCoinCount(); i++) {
                coin.drawCoins(canvas, geciciX, geciciY, i);
                coinVSplayerCarpisma(i);
            }
        }else {

            Log.i("coinCount", "draw: " + coin.getCoinCount());

        }
    }

    private void kusVSplayerCarpisma(int index){

        //Player kuşa çarparsa gameOver
        if (isColliding(player.getCol(), seagull.getSeagullDestination(index))){

            //GAME OVERR
            isGameOver = true;

        }

    }

    public void coinVSplayerCarpisma(int index){

        //Player kuşa çarparsa gameOver
        if (isColliding(player.getCol(), coin.getCoinDestination(index))){

            goldPoint ++;

            Log.i("goldPoint", "GOLD POİNT: " + goldPoint);
        }

    }

    public void drawRectBorder(Canvas canvas, Rect r, int color){
        Paint paint = new Paint();

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        canvas.drawRect(r, paint);
    }

    private void drawBackGround(Canvas canvas) {

        backGroundSource.set(bgScreenStartX, bgScreenStartY, bgScreenStartX + bgScreenW,bgScreenStartY + bgScreenH);
        backGroundDestination.set(0,0, getWidth(), getHeight());
        canvas.drawBitmap(backGround, backGroundSource, backGroundDestination,null);

    }

    private void backgroundMove () {

        geciciX = (int) Math.round((player.getPlayerSpeed() * Math.sin(Math.toRadians(player.getPlayerDagree()))));
        geciciY = -1 * (int) Math.round((player.getPlayerSpeed() * Math.cos(Math.toRadians(player.getPlayerDagree()))));

        if (Math.abs(geciciX) == 1 && Math.abs(geciciY) == 1 ) {
            //geciciX = geciciX * 2; //45 DERECE AÇI OLDUĞUNDA HER İKİSİ DE BİR OLUYOR VE BİRDEN PLAYER YAVAŞLIYOR.
            geciciY = geciciY * 2;
        }
        bgScreenStartY += geciciY; //yukarı Git
        bgScreenStartX += geciciX;

    }

    public boolean isColliding(Rect rect1, Rect rect2) {
        return rect1.intersect(rect2);
    }

    private void createSeagull() {

        nowTime =(int) ((System.currentTimeMillis() - startTime) / 1000);
        vNowTime =(int) ((System.currentTimeMillis() - vStartTime) / 1000);

        if (seagull.getSeagullTimeSecond() == nowTime && seagull.getSeagullCount() < seagull.getMaxSeagullCount()) {
            //Log.i("count","OLUŞTURULDU SANİYE: " + nowTime);
            startTime = System.currentTimeMillis();
            seagull.setSeagull(getWidth(),getHeight());
            //Log.i("seagullCount", "createSeagull: " + seagullCount);
        }

         if (seagull.getvSeagullTimeSecond() == vNowTime && seagull.getSeagullCount() < seagull.getMaxSeagullCount()) {

            seagull.setvSeagullTimeSecond(randomGenerator.nextInt(Seagull.getvTimeMax()) + Seagull.getvTimeMin());
            //Log.i("count","OLUŞTURULDU SANİYE: " + vNowTime);
            vNowTime = 0;
             vStartTime = System.currentTimeMillis();
            createVSeagull();
            //Log.i("seagullCount", "createSeagull: " + seagullCount);
        }

    }

    private void createCoin(){

        nowTime =(int) ((System.currentTimeMillis() - coinStartTime) / 1000);

        if (coin.getCoinTimeSecond() == nowTime && coin.getCoinCount() < coin.getCoinMaxDestination()) {
            //Log.i("count","OLUŞTURULDU SANİYE: " + nowTime);
            coinStartTime = System.currentTimeMillis();
            coin.coinCreate();
            //Log.i("seagullCount", "createSeagull: " + seagullCount);
        }


    }

    private void createVSeagull(){

        seagull.vSeagullCreate(randomGenerator.nextInt(((getHeight() / 2) / seagull.getSeagullDstH()) - 3 ) + 3, getWidth(),getHeight()); //v şeklindeki kuşların taban boy

    }







    public void keyPressed(int key) {

    }

    public void keyReleased(int key) {

    }

    public boolean backPressed() {
        return true;
    }

    public void surfaceChanged(int width, int height) {

    }

    public void surfaceCreated() {

    }

    public void surfaceDestroyed() {

    }

    public void touchDown(int x, int y, int id) {
        if (touchCount < 2) {
            touchCount++;
            android.util.Log.i("touch", "touchDown: " + touchCount + "  id:" + id);
        }
        //android.util.Log.i("touch", id + "-------->( "+ x + ", " + y + "," +x+1 + "," + y + 1 + ")");
        isTouch = true;

        if(id == 0) {
            rectTouches[0].set(x, y, x - 1, y - 1);
            isRectTouches[0] = true;
        }
        else if (id == 1) {
            rectTouches[1].set(x, y, x + 1, y + 1);
            isRectTouches[1] = true;
        }
    }

    public void touchMove(int x, int y, int id) {

    }

    public void touchUp(int x, int y, int id) {
        if (touchCount > 0) {
            touchCount--;
            android.util.Log.i("touch", "touchUP: " + touchCount + "  id:" + id);
        }

        if (touchCount == 0) isTouch = false;

        if (id == 0) {

            isRectTouches[0] = false;

        } else if (id == 1) {

            isRectTouches[1] = false;

        }
    }

    public void pause() {

    }

    public void resume() {

    }

    public void reloadTextures() {

    }

    public void showNotify() {
    }

    public void hideNotify() {
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getNowTime() {
        return nowTime;
    }

    public void setNowTime(int nowTime) {
        this.nowTime = nowTime;
    }

}
