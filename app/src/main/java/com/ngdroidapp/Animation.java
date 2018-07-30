package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;

import istanbul.gamelab.ngdroid.util.Utils;

public class Animation {
    //Farklı farklı imagelerden oluşan bir animasyon oluşturur

    private Bitmap images[];
    private int imageSrcW, imageSrcH;
    private int imageSrcX, imageSrcY;
    private Rect imageSource;
    private RectF imageDestination;
    private int imageDstX, imageDstY, imageDstW, imageDstH;
    private int imageFrameNum;
    private int imageMaxFrameNum;
    NgApp root;
    String path;

    private boolean isRunning;


    public Animation(Bitmap[] images, int imageSrcW, int imageSrcH, int imageSrcX, int imageSrcY ,
                     Rect imageSource, int imageDstX, int imageDstY, int imageDstW, int imageDstH
                            , int imageFrameNum, int imageMaxFrameNum, RectF imageDestination, NgApp root, String path){

        this.images = images;
        this.imageSrcW = imageSrcW;
        this.imageSrcH = imageSrcH;
        this.imageSrcX = imageSrcX;
        this.imageSrcY = imageSrcY;
        this.imageSource = imageSource;
        this.imageDstX = imageDstX;
        this.imageDstY = imageDstY;
        this.imageDstW = imageDstW;
        this.imageDstH = imageDstH;
        this.imageFrameNum = imageFrameNum;
        this.imageMaxFrameNum = imageMaxFrameNum;
        this.imageDestination = imageDestination;
        this.root = root;
        this.path = path;

        setAnimation();
    }

    private void setAnimation(){
        isRunning = false;

        for (int i = 0; i < imageMaxFrameNum; i++) {
            images[i] = Utils.loadImage(root, path + i + ".png");
            images[i] = Bitmap.createScaledBitmap(images[i], imageDstW, imageDstH, false);
        }

    }

    public void loadImagesToAnimation(){ //ONLY PNG!!!


    }

    private void runAnimation(){

    }

}
