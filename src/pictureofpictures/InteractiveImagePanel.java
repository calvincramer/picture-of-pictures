package pictureofpictures;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class InteractiveImagePanel extends JPanel{
    protected BufferedImage image;
    protected double scale;
    
    protected int offSetX;
    protected int offSetY;
    
    private Point mouseDownLocation;
    
    private final double offSetMultiplier = 0.65;
    private final int MIN_INSIDE = 30;

    public InteractiveImagePanel(BufferedImage img) {
        
        this.mouseDownLocation = new Point(0,0);
        
        this.scale = 1.0;
        this.setImage(img);
        this.offSetX = 0;
        this.offSetY = 0;
        
        this.addMouseWheelListener(new MouseWheelListener() {
            @Override public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() > 0) //rotated back, zoom out
                    zoomOut();
                else
                    zoomIn();
                repaint();
        }});
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override public void mouseDragged(MouseEvent e) {
                int deltaX = e.getX() - mouseDownLocation.x;
                int deltaY = e.getY() - mouseDownLocation.y;

                addOffSetX(deltaX);
                addOffSetY(deltaY);

                mouseDownLocation.x = e.getX();
                mouseDownLocation.y = e.getY();

                repaint();
            }
            @Override public void mouseMoved(MouseEvent e) {}   
        });
        this.addMouseListener(new MouseListener() {
            @Override public void mousePressed(MouseEvent e) {
                mouseDownLocation.x = e.getX();
                mouseDownLocation.y = e.getY();

                if (image == null)
                    noImageMousePress();
            }
            @Override public void mouseClicked(MouseEvent e) {}
            @Override public void mouseReleased(MouseEvent e) {}
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        });
    }
  
    @Override public void paint(Graphics g) {
        super.paint(g);
        
        Graphics2D g2 = (Graphics2D)g;
        //g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        if (this.image == null)
            return;
        
        int w = getWidth();
        int h = getHeight();
        
        int imageWidth = this.image.getWidth(null);
        int imageHeight = this.image.getHeight(null);
        
        double x = this.calcX();
        double y = this.calcY();
        
        AffineTransform at = AffineTransform.getTranslateInstance(x, y);
        at.scale(scale, scale);
        g2.drawRenderedImage(image, at);
    }
    
    private int calcX() {
        if (this.image == null) return -1;
        
        double d =  ( this.getWidth() - scale * this.image.getWidth() ) / 2; //center
        int n = (int) (d + (this.offSetX * this.offSetMultiplier)); //add offset
        return n;
    }
    private int calcY() {
        if (this.image == null) return -1;
        
        double d =  ( this.getHeight() - scale * this.image.getHeight() ) / 2; //center
        int n = (int) (d + (this.offSetY * this.offSetMultiplier)); //add offset
        return n;
    }
    
    protected void zoomOut() {
        if (this.image == null) return;
        
        this.scale -= this.scale * 0.05;
        if (this.scale < 0.1)
            this.scale = 0.1;
        this.checkImageInsidePanel();
    }
    protected void zoomIn() {
        if (this.image == null) return;
        
        this.scale += this.scale * 0.05;
        this.checkImageInsidePanel();
    }
    
    protected void addOffSetX(int delta) {
        if (this.image == null) return;
        
        this.offSetX += delta;
        
        int x = this.calcX();
        double width = this.scale * this.image.getWidth();
        
        if (x + width < this.MIN_INSIDE)
            this.offSetX -= delta;
        else if (x > this.getWidth() - this.MIN_INSIDE)
            this.offSetX -= delta;      
    }
    protected void addOffSetY(int delta) {
        if (this.image == null) return;
        
        this.offSetY += delta;
        
        int y = this.calcY();
        double height = this.scale * this.image.getHeight();
        
        if (y + height < this.MIN_INSIDE)
            this.offSetY -= delta;
        else if (y > this.getHeight() - this.MIN_INSIDE)
            this.offSetY -= delta;
    }
    
    protected void checkImageInsidePanel() {
        if (this.image == null) return;
        
        Rectangle bounds = this.getImageBounds();
        
        if (bounds.x > this.getWidth() - this.MIN_INSIDE)
            this.offSetX = 0;
        else if (bounds.x < this.MIN_INSIDE)
            this.offSetX = 0;
        
        if (bounds.y > this.getHeight() - this.MIN_INSIDE)
            this.offSetY = 0;
        else if (bounds.y < this.MIN_INSIDE)
            this.offSetY = 0;
        
    }
    
    protected int getImageWidth() {
        if (this.image == null) return -1;
        return (int) this.scale * this.image.getWidth();
    }
    protected int getImageHeight() {
        if (this.image == null) return -1;
        return (int) this.scale * this.image.getHeight();
    }
    
    protected Rectangle getImageBounds() {
        if (this.image == null) return null;
        
        Rectangle r = new Rectangle();
        r.setBounds(this.calcX(), this.calcY(), this.getImageHeight(), this.getImageHeight());
        return r;
    }
    
    protected final void setImage(BufferedImage img) {
        this.image = img;
        
        this.offSetX = 0;
        this.offSetY = 0;
        this.scale = 1;
        
        if (this.image != null) {
            this.scale = this.getHeight() * 1.0 / this.image.getHeight();
        }
    }
    
    protected void noImageMousePress() {
        //override this method to do whatever when no image
    }
}
