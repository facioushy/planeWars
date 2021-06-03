import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class GamePanel extends JPanel implements ActionListener {
    public HashMap mypalneBoomImageMap;
    public HashMap imageMap;
    GamePanel gamePanel = this;
    private JFrame mainFrame = null;
    private JMenuBar jmb;
    private boolean startFlag = true;
    private int gameHeight;
    private int gameWidth;
    private MyPlane myPlane;
    

    public GamePanel(JFrame frame){
        this.setLayout(null);
        mainFrame = frame;
        initMenu();
        mainFrame.setVisible(true);

        //new Thread(new RefreshThread()).start();
    }

    @Override
    public void paint(Graphics g){
        gameHeight = this.getHeight();
        gameWidth = this.getWidth();

        if (myPlane != null) myPlane.draw(g);
    }

    private void initMyPlane(){
        myPlane = new MyPlane(200, 530, 132, 86, this);
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

    private void realGameEnd(int i){

    }

    private void restart(){

    }

    private class RefreshThread implements Runnable{
        @Override
        public void run(){
            while(startFlag) {
                repaint();
                try{
                    Thread.sleep(50);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
