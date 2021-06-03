package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

public class Bullet {
    private int x = 0;
    private int y = 0;
    private int width = 0;
    private int height = 0;
    private BufferedImage image = null;
    private GamePanel panel = null;
    private HashMap imageMap = null;
    private boolean alive = true;
    private boolean canMove = false;
    private int speed = 20;

    public Bullet(int x, int y, int width, int height, GamePanel panel){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.panel = panel;
        this.imageMap = panel.imageMap;
        this.image = (BufferedImage) imageMap.get("bullet");

        move();
    }

    void move(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (alive && !panel.nextEnd && !panel.nextWin){
                    y -= speed;
                    if (y <= 0){
                        clear();
                    }
                    hitEnemy();

                    try{
                        Thread.sleep(50);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void draw(Graphics g){
        g.drawImage(image, x, y, width, height, null);
    }

    public void clear(){
        alive = false;
        panel.bulletList.remove(this);
    }

    protected void hitEnemy(){
        EnemyPlane enemyPlane = null;
        List enemys = panel.enemyList;
        for (int i = 0; i < enemys.size(); i++) {
            try{
                enemyPlane = (EnemyPlane) enemys.get(i);
            }catch (Exception e){}
            if (enemyPlane == null) continue;
            if (this.isPoint(enemyPlane)){
                panel.curCount += enemyPlane.getCount();
                clear();
                enemyPlane.boom();
                if (panel.curCount >= panel.totalCount){
                    panel.myPlane.setCanMove(false);
                    panel.gameWin();
                }
            }
        }
    }

    private boolean isPoint(EnemyPlane enemyPlane){
        int x1 = x;
        int y1 = y;

        int x2 = x+width;
        int y2 = y;

        int x3 = x+width;
        int y3 = y+height;

        int x4 = x;
        int y4 = y+height;

        if (comparePoint(x1,y1,enemyPlane) || comparePoint(x2, y2, enemyPlane) || comparePoint(x3, y3, enemyPlane)
        || comparePoint(x4, y4, enemyPlane)){
            return true;
        }
        return false;
    }

    private boolean comparePoint(int x, int y, EnemyPlane plane){
        if (x > plane.getX() && y > plane.getY()
        && x<plane.getX()+plane.getWidth() && y < plane.getY()+plane.getHeight()){
            return true;
        }
        return false;
    }
}
