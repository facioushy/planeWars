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
    }

    public void draw(Graphics g){
        g.drawImage(image, x, y, width, height, null);
    }
}
