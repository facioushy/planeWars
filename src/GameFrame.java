import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    public GameFrame(){
        setTitle("飞机大战");
        setSize(526, 685);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//点击关闭按钮是否关闭程序
        setLocationRelativeTo(null);//设置居中
        setResizable(false);//不允许修改界面大小
    }
}
