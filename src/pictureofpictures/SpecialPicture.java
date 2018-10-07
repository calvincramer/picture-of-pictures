package pictureofpictures;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SpecialPicture {
    
    protected BufferedImage image;
    protected BufferedImage icon; //if larger than 174x128
    protected double[] averageColor;
    protected boolean flag; //used by MainFrame, should shy away from using
    protected int timesUsed = 0;
    
    protected String name;
    
    protected SpecialPicture(BufferedImage image) {
        this.image = image;
        this.icon = SpecialPicture.makeIcon(this, this.image);
        this.flag = false;
        this.averageColor = new double[3];
        this.averageColor = PictureHelper.getAverageColor(this.image);
        this.name = "";
    }
    
    protected SpecialPicture(BufferedImage image, double[] averageColor) {
        this.image = image;
        this.icon = SpecialPicture.makeIcon(this, this.image);
        this.flag = false;
        this.averageColor = averageColor;
        this.name = "";
    }
    
    protected SpecialPicture(BufferedImage image, String name) {
        this.image = image;
        this.icon = SpecialPicture.makeIcon(this, this.image);
        this.flag = false;
        this.averageColor = new double[3];
        this.averageColor = PictureHelper.getAverageColor(this.image);
        this.name = name;
    }

    protected BufferedImage getImage() {
        return image;
    }

    protected void setImage(BufferedImage image) {
        this.image = image;
    }

    protected double[] getAverageColor() {
        return averageColor;
    }

    protected boolean getFlag() {
        return flag;
    }

    protected void setFlag(boolean flag) {
        this.flag = flag;
    }
    
    
    protected static BufferedImage makeIcon(SpecialPicture sp, BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        int maxW = 174;
        int maxH = 128;
        Dimension iconDim = new Dimension();
        if (w > h) {
            iconDim.setSize(maxW, (int) ( (maxW * 1.0 / w) * h ));
        } else { //
            iconDim.setSize( (int) ( (maxH * 1.0 / h) * w ), maxH);
        }

        try {
            int type = image.getType();
            //type == 0 causes an exception
            //type 0 is BufferedImage.TYPE_CUSTOM
            if (type == 0)
                type = BufferedImage.TRANSLUCENT;
            
            BufferedImage icon = new BufferedImage(iconDim.width, iconDim.height, type);
            Graphics2D g2 = (Graphics2D) icon.getGraphics();
            g2.drawImage(image, 0, 0, icon.getWidth(), icon.getHeight(), null);
            return icon;
        } catch (Exception e) {
            
            System.out.println("error with image:" + image.toString());
            System.out.println(sp.name);
            e.printStackTrace();
        }

        return null;
    }
    
    public static void main(String[] args) {
        System.out.println(BufferedImage.OPAQUE);
        System.out.println(BufferedImage.BITMASK);
        System.out.println(BufferedImage.TRANSLUCENT);
        System.out.println(BufferedImage.TYPE_CUSTOM);
        System.out.println(BufferedImage.TYPE_INT_ARGB);
    }
    
}
