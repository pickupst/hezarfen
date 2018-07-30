package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
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
    private Score score;

    private boolean isGameOver;
    private static long startTime;
    private static int nowTime;
    public Random randomGenerator;
    private boolean isTouch;
    private Rect rightLane, leftLane;
    public int screenMidPointX, screenMidPointY;
    private Rect rectTouches[];
    private boolean isRectTouches[];
    private int touchCount;
    private int geciciX, geciciY;

    private static int goldPoint;
    private Coin coin;
    private static long coinStartTime;

    private Player player;
    private Explosion[] explosion;

    private Enemy enemy;
    private static long enemyStartTime;

    private Seagull seagull;
    private static long vStartTime;
    private static int vNowTime;

    private Bitmap backGround;
    private Rect backGroundSource, backGroundDestination;
    private int bgScreenW, bgScreenH;
    private int bgScreenStartX, bgScreenStartY;

    private void setBackGround() {
        bgScreenH = getHeight() / 5;
        bgScreenW = getWidth() / 5;

        backGround = Utils.loadImage(root, "background.png");

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

        enemy.setEnemyTimeSecond(3);  //Aşağıda
        enemy.incMaxEnemyCount(5);
        enemy.setEnemyCount(0);
        enemy.setEnemyDagree(new int[enemy.getMaxEnemyCount()]);
        enemy.setEnemyDstX(new float[enemy.getMaxEnemyCount()]);
        enemy.setEnemyDstY(new float[enemy.getMaxEnemyCount()]);
        enemy.setEnemyOnScreen(new boolean[enemy.getMaxEnemyCount()]);

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
        enemy = new Enemy(root);
        seagull = new Seagull(root);
        coin = new Coin(root);
        score = new Score(root);

        for (int i = 0; i < seagull.getMaxSeagullCount(); i++) {    //ilk başta tüm nesnelerimiz ekranda değil
            seagull.setSeagullOnScreen(i,false);
        }

        for (int i = 0; i < enemy.getMaxEnemyCount(); i++) {    //ilk başta tüm nesnelerimiz ekranda değil
            enemy.setEnemyOnScreen(i,false);
        }

        startTime = System.currentTimeMillis();
        vStartTime = System.currentTimeMillis();
        coinStartTime = System.currentTimeMillis();
        enemyStartTime = System.currentTimeMillis();

        isTouch = false;

        setOther();
        player.setPlayer(getWidth(), getHeight());
        enemy.setEnemy(getWidth(), getHeight());
        setBackGround();

        coin.setCoin(getWidth(), getHeight());

        score.setScore();
        explosion = new Explosion[enemy.getMaxEnemyCount()];
        for (int i = 0; i < enemy.getMaxEnemyCount(); i++) {
            explosion[i] = new Explosion(root);
        }

    }

    private void gameOver(){
        if (isGameOver) {
            MenuCanvas mc = new MenuCanvas(root);
            root.canvasManager.setCurrentCanvas(mc);
        }
    }

    public void update() {

        gameOver();

        backgroundMove();
        player.playerMove(isTouch, touchCount, isRectTouches, leftLane, rightLane, rectTouches);
        enemy.enemyMove(geciciX, geciciY);

        createSeagull();
        createEnemy();

        if (seagull.getSeagullCount() >= 0) seagull.seagullMove(geciciX, geciciY); //ekranda martı varsa
        //if (enemy.getEnemyCount() >= 0) enemy.enemyMove(geciciX, geciciY); //ekranda düşman varsa

        createCoin();

        coin.coinAnimation();

        score.distanceUpdatingScore();

        for (int i = 0; i < enemy.getMaxEnemyCount(); i++) {
            explosion[i].explosionAnimation(geciciX, geciciY);
        }

    }

    public void draw(Canvas canvas) {

            drawBackGround(canvas);
            player.drawPlayer(canvas, screenMidPointX, screenMidPointY);
            score.drawScore(canvas);

            for (int i = 0; i < enemy.getEnemyCount(); i++) {
                if (enemy.getEnemyCount() >= 0) {
                    enemy.drawEnemy(canvas, i);
                    enemyCarpisma(i);
                }
            }

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

            for (int j = 0; j < enemy.getEnemyCount(); j++) { // patlama için

                for (int i = 0; i < enemy.getMaxEnemyCount(); i++) {
                    explosion[i].drawExplosion(canvas,j);
                }

            }
    }



    private void kusVSplayerCarpisma(int index){

        //Player kuşa çarparsa gameOver
        if (isColliding(player.getCol(), seagull.getSeagullDestination(index))){

            //GAME OVERR
            isGameOver = true;

        }

    }

    private void enemyCarpisma(int index){

        //Player enemye çarparsa gameOver
        if (isColliding(enemy.getCol(index), player.getCol())){

            //GAME OVERR

            isGameOver = true;

        } else {

            for (int i = 0; i < enemy.getEnemyCount(); i++){  //Düşman Düşmana çarparsa

                if (index != i){ //Kendi kendisine çarpmasını istemeyiz :)
                    if (isColliding(enemy.getCol(index), enemy.getCol(i))){
                            if (!explosion[i].isExpIsLive()){
                                explosion[i].setExplosion(enemy, index, player);
                                break;
                            }
                        enemy.enemyKonumBelirle(index,getWidth(),getHeight()); //Yeniden konumlandıralım
                        enemy.enemyKonumBelirle(i,getWidth(),getHeight());
                        explosion[i].startExplosionSound();

                    }
                }

            }

        }

    }


    public void coinVSplayerCarpisma(int index){

        //Player altına çarparsa
        if (isColliding(player.getCol(), coin.getCoinDestination(index))){

            coin.coinCreate(index);
            coin.startCoinSound();

            goldPoint += 50;

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
           geciciY = geciciY * 2;
        }
        bgScreenStartY += geciciY; //yukarı Git
        bgScreenStartX += geciciX;

    }

    public boolean isColliding(Rect rect1, Rect rect2) {
        return rect1.intersect(rect2);
    }

    public boolean isColliding(RectF rect1, RectF rect2) {
        return rect1.intersect(rect2);
    }

    public boolean isColliding(RectF rect1, Rect rect2) {
        RectF gecici = new RectF();
        gecici.set((float) rect2.left, (float) rect2.top, (float) rect2.right, (float) rect2.bottom);

        return rect1.intersect(gecici);
    }

    private void createSeagull() {

        nowTime =(int) ((System.currentTimeMillis() - startTime) / 1000);
        vNowTime =(int) ((System.currentTimeMillis() - vStartTime) / 1000);

        if (seagull.getSeagullTimeSecond() == nowTime && seagull.getSeagullCount() < seagull.getMaxSeagullCount()) {

            startTime = System.currentTimeMillis();
            seagull.setSeagull(getWidth(), getHeight());

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



    private void createEnemy() {

        nowTime =(int) ((System.currentTimeMillis() - enemyStartTime) / 1000);

        if (enemy.getEnemyTimeSecond() == nowTime && enemy.getEnemyCount() < enemy.getMaxEnemyCount()) {

            enemyStartTime = System.currentTimeMillis();
            enemy.setEnemy(getWidth(),getHeight());

        }
    }

    private void createCoin(){

        nowTime =(int) ((System.currentTimeMillis() - coinStartTime) / 1000);

        if (coin.getCoinTimeSecond() == nowTime && coin.getCoinCount() < coin.getCoinMaxDestination()) {
            //Log.i("count","OLUŞTURULDU SANİYE: " + nowTime);
            coinStartTime = System.currentTimeMillis();
            Log.i("coinCount", "createCoin: " + coin.getCoinCount());
            coin.coinCreate(coin.getCoinCount());
            //Log.i("seagullCount", "createSeagull: " + seagullCount);
        }


    }

    private void createVSeagull(){

        //Sıfıra bölme hatası olmaması için
        if (seagull.getSeagullDstH() != 0) seagull.vSeagullCreate(randomGenerator.nextInt(((getHeight() / 2) / seagull.getSeagullDstH()) - 3 ) + 3, getWidth(),getHeight()); //v şeklindeki kuşların taban boy

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


    public static int getGoldPoint() {
        return goldPoint;
    }

    public static void setGoldPoint(int add) {
        goldPoint += add;
    }
}
