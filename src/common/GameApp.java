package common;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GameApp {
    public static HashMap getImageMapByIcon(String path, List nameList){
        HashMap target = new HashMap();
        BufferedImage img = null;
        String name = "";
        String key = "";
        for (int i=0; i<nameList.size(); i++){
            name = (String) nameList.get(i);
            key = name.split("\\.")[0];
            img = GameApp.getImg(path+nameList.get(i));
            target.put(key, img);
        }
        return target;
    }

    private static BufferedImage getImg(String path) {
        try{
            BufferedImage img = ImageIO.read(GameApp.class.getResource(path));
            return img;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
