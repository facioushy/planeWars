package main;

import common.GameApp;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener {
    GamePanel gamePanel = this;
    private JFrame mainFrame = null;
    private JMenuBar jmb;
    public static boolean startFlag = true;
    public static boolean nextEnd = false;
    public static boolean nextWin = false;
    private int gameHeight = 0;
    private int gameWidth = 0;
    public MyPlane myPlane = null;

    public BufferedImage tempBufferedImage = null;
    public List tempImages = new ArrayList();

    public HashMap mypalneBoomImageMap;

    public HashMap imageMap = new HashMap();
    private HashMap enemy1boomImageMap = new HashMap();//
    private HashMap enemy2boomImageMap = new HashMap();
    private HashMap enemy3boomImageMap = new HashMap();
    private HashMap enemy4boomImageMap = new HashMap();
    public List enemyListMap = new ArrayList();

    public List enemyList = new ArrayList();
    public List bulletList = new ArrayList();

    public int curCount = 0;
    public int totalCount = 1000;

    public GamePanel(JFrame frame){
        this.setLayout(null);
        mainFrame = frame;
        //菜单创建
        initMenu();
        //初始化图片
        initImage();
        //初始化自己的飞机
        initMyPlane();
        //初始化敌机
        initEnemyPlane();
        //添加鼠标事件监控
        createMouseListener();

        mainFrame.setVisible(true);

        new Thread(new RefreshThread()).start();

        ready();
    }

    private void ready() {
        tempBufferedImage = (BufferedImage) imageMap.get("ready");
        tempImages.add(tempBufferedImage);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                tempImages.remove(tempBufferedImage);
            }
        }).start();
    }

    private void initEnemyPlane() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(startFlag) {
                    createEnemyPlane();
                    try{
                        Thread.sleep(500);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //创建敌人飞机
    private void createEnemyPlane(){
        EnemyPlane enemyPlane = new EnemyPlane(this);
        enemyList.add(enemyPlane);
    }

    private void createMouseListener() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (myPlane == null) return;
                if (myPlane.isCanMove()){
                    myPlane.move(x, y);
                    return;
                }
                if (myPlane.isPoint(x,y)){
                    myPlane.setCanMove(true);
                }
            }
        };
        addMouseMotionListener(mouseAdapter);
        addMouseListener(mouseAdapter);
    }

    private void initImage() {
        List commonList = new ArrayList();
        commonList.add("bg.jpg");
        commonList.add("myplane1.png");
        commonList.add("enemy1.png");
        commonList.add("enemy2.png");
        commonList.add("enemy3.png");
        commonList.add("enemy4.png");
        commonList.add("bullet.png");
        commonList.add("ready.png");
        commonList.add("win.png");
        commonList.add("lost.png");

        imageMap = GameApp.getImageMapByIcon("/images/", commonList);//加载图片

        List enemy1List = new ArrayList();
        for (int i=1; i<=6; i++){
            enemy1List.add("enemy1boom" + i +".png");
        }
        enemy1boomImageMap = GameApp.getImageMapByIcon("/images/enemy1boom/", enemy1List);
        enemyListMap.add(enemy1boomImageMap);

        List enemy2List = new ArrayList();
        for (int i=1; i<=6; i++){
            enemy2List.add("enemy2boom" + i +".png");
        }
        enemy2boomImageMap = GameApp.getImageMapByIcon("/images/enemy2boom/", enemy2List);
        enemyListMap.add(enemy2boomImageMap);

        List enemy3List = new ArrayList();
        for (int i=1; i<=6; i++){
            enemy3List.add("enemy3boom" + i +".png");
        }
        enemy3boomImageMap = GameApp.getImageMapByIcon("/images/enemy3boom/", enemy3List);
        enemyListMap.add(enemy3boomImageMap);

        List enemy4List = new ArrayList();
        for (int i=1; i<=6; i++){
            enemy4List.add("enemy4boom" + i +".png");
        }
        enemy4boomImageMap = GameApp.getImageMapByIcon("/images/enemy4boom/", enemy4List);
        enemyListMap.add(enemy4boomImageMap);

        List myplaneBoomList = new ArrayList();
        for (int i=1; i<=6; i++){
            myplaneBoomList.add("myplane1boom"+i+".png");
        }
        mypalneBoomImageMap = GameApp.getImageMapByIcon("/images/myplane1boom/", myplaneBoomList);
    }

    @Override
    public void paint(Graphics g){
        gameHeight = this.getHeight();
        gameWidth = this.getWidth();

        //画背景
        g.drawImage((BufferedImage)imageMap.get("bg"), 0, -150, null);

        //画飞机
        if (myPlane != null) myPlane.draw(g);

        //画敌人
        EnemyPlane enemyPlane = null;
        for (int i = 0; i < enemyList.size(); i++) {
            enemyPlane = (EnemyPlane) enemyList.get(i);
            enemyPlane.draw(g);
        }
        //画子弹
        Bullet bullet = null;
        for (int i = 0; i < bulletList.size(); i++) {
            bullet = (Bullet) bulletList.get(i);
            bullet.draw(g);
        }

        //临时图片
        BufferedImage tempImage = null;
        int x = 0;
        int y = 0;
        for (int i = 0; i < tempImages.size(); i++) {
            tempImage = (BufferedImage) tempImages.get(i);
            x = gameWidth/2-tempImage.getWidth()/2;
            y = gameHeight/2-tempImage.getHeight()/2;
            g.drawImage(tempImage, x, y, null);
        }

        Color color = g.getColor();
        g.setColor(Color.WHITE);
        g.setFont(new Font("微软雅黑", Font.BOLD, 16));
        g.drawString("得分：" + curCount + "", 400, 24);
        g.setColor(color);
    }

    private void initMyPlane(){
        myPlane = new MyPlane(300, 530, 132, 86, this);
    }

    private void initMenu(){
        jmb = new JMenuBar();
        JMenu jm1 = new JMenu("游戏");
        jm1.setFont(new Font("微软雅黑", Font.BOLD, 15));
        JMenu jm2 = new JMenu("帮助");
        jm2.setFont(new Font("微软雅黑", Font.BOLD, 15));

        JMenuItem jmi1 = new JMenuItem("开始新游戏");
        JMenuItem jmi2 = new JMenuItem("退出");
        jmi1.setFont(new Font("微软雅黑", Font.BOLD, 15));
        jmi2.setFont(new Font("微软雅黑", Font.BOLD, 15));

        JMenuItem jmi3 = new JMenuItem("操作说明");
        jmi3.setFont(new Font("微软雅黑", Font.BOLD, 15));
        JMenuItem jmi4 = new JMenuItem("胜利条件");
        jmi4.setFont(new Font("微软雅黑", Font.BOLD, 15));

        jm1.add(jmi1);
        jm1.add(jmi2);

        jm2.add(jmi3);
        jm2.add(jmi4);

        jmb.add(jm1);
        jmb.add(jm2);
        mainFrame.setJMenuBar(jmb);

        jmi1.addActionListener(this);
        jmi1.setActionCommand("Restart");
        jmi2.addActionListener(this);
        jmi2.setActionCommand("Exit");

        jmi3.addActionListener(this);
        jmi3.setActionCommand("help");
        jmi4.addActionListener(this);
        jmi4.setActionCommand("win");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("宋体", Font.ITALIC, 18)));
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("宋体", Font.ITALIC, 18)));
        if ("Exit".equals(command)){
            Object[] options = {"确定", "取消"};
            int response = JOptionPane.showOptionDialog(this, "您确认要推出吗", "",
                    JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (response == 0) System.exit(0);
        }else if ("Restart".equals(command)){
            if (startFlag){
                Object[] options = {"确认", "取消"};
                int response = JOptionPane.showOptionDialog(this, "游戏中，您确认要重新开始吗", "",
                        JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (response == 0){
                    realGameEnd(1);
                    restart();
                }
            }else{
                restart();
            }
        }else if ("help".equals(command)){
            JOptionPane.showMessageDialog(null, "游戏开始后，要先动鼠标到飞机触，出发移动", "提示！",
                    JOptionPane.INFORMATION_MESSAGE);
        }else if ("win".equals(command)){
            JOptionPane.showMessageDialog(null, "得分1000，获得胜利", "提示！",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void realGameEnd(int type){
        startFlag = false;

        EnemyPlane enemyPlane = null;
        for (int i = 0; i < enemyList.size(); i++) {
            enemyPlane = (EnemyPlane) enemyList.get(i);
            if (enemyPlane != null){
                enemyPlane.setAlive(false);
                enemyPlane = null;
            }
        }
        enemyList.clear();

        Bullet bullet = null;
        for (int i = 0; i < bulletList.size(); i++) {
            bullet = (Bullet) bulletList.get(i);
            if (bullet != null){
                bullet.setAlive(false);
                bullet = null;
            }
        }

    }

    private void restart(){
        realGameEnd(1);

        startFlag = true;
        nextEnd = false;
        nextWin = false;

        curCount = 0;

        if (myPlane != null){
            myPlane.clear();
            myPlane = null;
        }
        EnemyPlane enemyPlane = null;
        for (int i = 0; i < enemyList.size(); i++) {
            enemyPlane = (EnemyPlane) enemyList.get(i);
            if (enemyPlane != null){
                enemyPlane.clear();
            }
        }
        enemyList.clear();

        Bullet bullet = null;
        for (int i = 0; i < bulletList.size(); i++) {
            bullet = (Bullet) bulletList.get(i);
            if (bullet != null){
                bullet.clear();
            }
        }
        bulletList.clear();

        tempImages.clear();
        tempBufferedImage = null;

        initMyPlane();
        initEnemyPlane();

        new Thread(new RefreshThread()).start();

        ready();
    }

    private class RefreshThread implements Runnable{
        @Override
        public void run(){
            while(startFlag) {
                repaint();
                if (nextEnd || nextWin){
                    realGameEnd(0);
                    return;
                }
                try{
                    Thread.sleep(50);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void gameWin(){
        nextWin = true;
        tempBufferedImage = (BufferedImage)imageMap.get("win");
        tempImages.add(tempBufferedImage);
    }

    public void gameOver(){
        nextEnd = true;
        tempBufferedImage = (BufferedImage) imageMap.get("lost");
        tempImages.add(tempBufferedImage);
    }
}
