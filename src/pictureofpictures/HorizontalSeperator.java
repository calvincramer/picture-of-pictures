package pictureofpictures;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class HorizontalSeperator 
    extends JPanel {
    
    private Color color;
    private int height;
    
    public HorizontalSeperator(Color c, int height) {
        super();
        
        this.color = c;
        this.height = height;
        
        this.setOpaque(false);
        this.setBorder(new EmptyBorder(height,0,0,0));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        this.setMinimumSize(new Dimension(Integer.MAX_VALUE, height));
        this.setPreferredSize(new Dimension(50, height));
    }
    
    @Override public void paint(Graphics g) {
        super.paint(g);
        Color c = this.color;
        if (c == null)
            c = ProjConsts.HORIZONTAL_SPACER_COLOR;
        
        double middleA = c.getAlpha(); //alpha in middle (max alpha)
        double sideA = 0.0;             //alpha at very edge
        int fadeWidth = this.getWidth() >> 3; //divide by 8

        for (int i = 0; i < fadeWidth ;i++) {
            double alph = i * 1.0 / fadeWidth;
            int a = (int) (alph * middleA);
            if (a < 0)
                a = 0;
            if (a > 255)
                a = 255;

            g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), a));
            g.fillRect(i, 0, 1, this.getHeight()); //verticle line
            g.fillRect(this.getWidth() - i, 0, 1, this.getHeight()); // other side
        }
        g.setColor(c);
        g.fillRect(fadeWidth, 0, this.getWidth() - (fadeWidth * 2) + 1, this.getHeight());
    }
         
    /*
    @Override public void setMaximumSize(Dimension d) {}
    @Override public void setMinimumSize(Dimension d) {}
    @Override public void setPreferredSize(Dimension d) {}
    @Override public Dimension getMaximumSize() {return new Dimension(Integer.MAX_VALUE, this.height);}
    @Override public Dimension getMinimumSize() {return new Dimension(Integer.MAX_VALUE, this.height);}
    @Override public Dimension getPreferredSize() {
        if (this.getParent() != null)
            return new Dimension(50, this.height);
        else
            return new Dimension(50, this.height);
    }

    @Override public void setSize(Dimension d) {}
    */
}
