package pictureofpictures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JSlider;
import javax.swing.plaf.SliderUI;
import javax.swing.plaf.basic.BasicSliderUI;

public class CustomSlider 
    extends JSlider {

    @Override public void setUI(SliderUI ui) {
        super.setUI(new BasicSliderUI(this) {
            @Override public Color getFocusColor() {
                return Color.WHITE;
            }
            @Override public void paintFocus(Graphics g) {
                return; //do nothing!
            }
            
            @Override public void paintTrack(Graphics g) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                
                int height = this.trackRect.height;
                int midY = height / 2;
                
                g.setColor(ProjConsts.MEDIUM_GREY);
                
                g.fillRoundRect(this.contentRect.x, midY - 2, this.contentRect.width, 4, 4, 4);
                
                //g2.setColor(Color.WHITE);
                //g2.drawRect(this.contentRect.x, this.contentRect.y, this.contentRect.width, this.contentRect.height);
                
            }
            
            @Override public void paintThumb(Graphics g) {
                if (isEnabled())
                    g.setColor(ProjConsts.ACCENT_BLUE);
                else 
                    g.setColor(ProjConsts.DISABLED_BLUE);

                g.fillRect(this.thumbRect.x, this.thumbRect.y, this.thumbRect.width, this.thumbRect.height);
            }
        });
    }
}
