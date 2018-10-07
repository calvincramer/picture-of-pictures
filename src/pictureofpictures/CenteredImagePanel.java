package pictureofpictures;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

public class CenteredImagePanel 
    extends JPanel {
    
    private Image image;
    
    public CenteredImagePanel(Image img) {
        this.image = img;
    }
    
    @Override public void paint(Graphics g) {
        super.paint(g);
        
        PictureHelper.paintCenteredPicture(g, image, this);
    }
    
}
