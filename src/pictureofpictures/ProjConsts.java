package pictureofpictures;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.FontMetrics;

public class ProjConsts {
    
    protected static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    
    protected static final Font consolas14 = new Font("Consolas", Font.PLAIN, 14);
    protected static final Font NORM_FONT = new Font("Tahoma", Font.PLAIN, 12);
    
    //protected static final Color HORIZONTAL_SPACER_COLOR = new Color(0,25,135,127);
    protected static final Color HORIZONTAL_SPACER_COLOR = new Color(200,200,200,40);
    protected static final Color TEXT_COLOR = new Color(255,255,255);
    protected static final Color DISABLED_TEXT_COLOR = new Color(160,160,160);
    
    protected static final Color BACKGROUND_COLOR = new Color(25,25,25);
    protected static final Color ACCENT_BLUE  = new Color(40,60,255);
    protected static final Color DISABLED_BLUE  = new Color(60,60,130);
    
    protected static final Color DARK_GREY = new Color(40,40,40);
    protected static final Color MEDIUM_GREY  = new Color(80,80,80);
    protected static final Color LIGHT_GREY  = new Color(180,180,180);
    
    protected static final Color GREY_0 = BACKGROUND_COLOR;
    protected static final Color GREY_1 = DARK_GREY;
    protected static final Color GREY_2 = new Color(60,60,60);
    protected static final Color GREY_3 = MEDIUM_GREY;
    protected static final Color GREY_4 = new Color(100,100,100);
    protected static final Color GREY_5 = new Color(120,120,120);
    protected static final Color GREY_6 = new Color(140,140,140);
    protected static final Color GREY_7 = new Color(160,160,160);
    protected static final Color GREY_8 = LIGHT_GREY;
    protected static final Color GREY_9 = new Color(180,180,180);
    
    
    
    protected static final int SCROLL_BAR_INCREMENT = 8;
    protected static final int SCROLL_BAR_WIDTH = 8;
    protected static final Color SCROLL_BAR_COLOR = new Color(200,200,200);
    protected static final Color SCROLL_BAR_MOUSE_OVER_COLOR = new Color(230,230,230);
    
    
}
