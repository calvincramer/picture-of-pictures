package pictureofpictures;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;


public class CustomScrollBarUI
    extends BasicScrollBarUI {
    
    
    public CustomScrollBarUI() {
        this.decrGap = 5;
        this.scrollBarWidth = 100;
        
    }
    
    @Override protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(ProjConsts.BACKGROUND_COLOR);
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);

    }
    @Override protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
        //g.setColor(Color.BLACK);
        //g.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        //Color color = this.isThumbRollover() ? ProjConsts.SCROLL_BAR_MOUSE_OVER_COLOR : ProjConsts.SCROLL_BAR_COLOR;
        Color color = ProjConsts.SCROLL_BAR_COLOR;
        
        g2.setColor(color);
        g2.fillRoundRect(r.x, r.y, r.width, r.height, 12, 12);
        
        
        
        
    }
    @Override protected JButton createDecreaseButton(int orientation) {
        return this.makeZeroButton();
    }
    @Override protected JButton createIncreaseButton(int orientation) {
        return this.makeZeroButton();
    }
    
    /*
    @Override protected void configureScrollBarColors() {
        this.thumbColor = Color.RED;
        this.trackColor = Color.BLUE;
        this.trackHighlightColor = Color.YELLOW;
    }
    */
    
    
    
    private JButton makeZeroButton() {
        JButton temp = new JButton();
        temp.setPreferredSize(new Dimension(0,0));
        temp.setMinimumSize(new Dimension(0,0));
        temp.setMaximumSize(new Dimension(0,0));
        return temp;
    }
    
}