package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class MyPlane {
    private int x = 0;
    private int y = 0;
    private int width = 0;
    private int height = 0;
    private BufferedImage image = null;
    private GamePanel panel = null;
    private HashMap imageMap = null;
    private boolean alive = true;
    private boolean canMove = false;
    private int key = 1;
    private HashMap boomImageMap = null;
    private boolean hitFlag = false;

    public MyPlane(int x, int y, int width, int height, GamePanel panel){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.panel = panel;
        this.imageMap = panel.imageMap;
        this.image = (BufferedImage) imageMap.get("myplane1");
        this.boomImageMap = panel.mypalneBoomImageMap;

        shoot();
    }

    private void shoot() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(alive && !panel.nextEnd && !panel.nextWin){
                    createBullet();
                    try{
                        Thread.sleep(200);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }

            private void createBullet(){
                Bullet bullet = new Bullet(x+width/2-10, y, 20, 30, panel);
                panel.bulletList.add(bullet);

            }
        }).start();
    }


    public void draw(Graphics g){
        g.drawImage(image, x, y, width, height, null);
    }

    public void boom(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (alive && panel.startFlag){
                    changeImage();
                    try{
                        Thread.sleep(80);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void changeImage(){
        key++;
        if (key > boomImageMap.size()){
            canMove = false;
            clear();
            panel.gameOver();
            return;
        }
        this.image = (BufferedImage) boomImageMap.get("myplane1boom"+key);
        width = image.getWidth();
        height = image.getHeight();
    }

    public void move(int x, int y){
        if (hitFlag) return;

        if (x-width/2 >= 0 && x<=panel.getWidth()-width/2){
            this.x = x-width/2;
        }
        if (y-height/2 >= 0 && y <= panel.getHeight()-height/2){
            this.y = y-height/2;
        }
    }

    public boolean isPoint(int x, int y){
        if (x > this.x && y > this.y && x < this.x+this.width
        && y < this.y + this.height) {
            return true;
        }
        return false;
    }

    public void clear(){
        alive = false;
        panel.myPlane = null;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isCanMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public boolean isHitFlag() {
        return hitFlag;
    }

    public void setHitFlag(boolean hitFlag) {
        this.hitFlag = hitFlag;
    }
}
