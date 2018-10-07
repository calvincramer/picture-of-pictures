package pictureofpictures;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;


public class PictureHelper {
    
    protected static double[] getAverageColor(BufferedImage image) {
        return getAverageColor(image, 0, 0, image.getWidth(), image.getHeight());
    }
    
    protected static double[] getAverageColor(BufferedImage image, int x1, int y1, int x2, int y2) {
        
        if (x2 >= image.getWidth())
            x2 = image.getWidth() - 1;
        if (y2 >= image.getHeight())
            y2 = image.getHeight() - 1;
        
        int red = 0, green = 0, blue = 0;
        int totalTraversed = (x2 - x1)*(y2 - y1);
        
        for (int y = y1; y < y2; y++) {
            for (int x = x1; x < x2; x++) {
                int rgb = image.getRGB(x, y);
                red += (rgb >> 16) & 0xFF;
                green += (rgb >> 8) & 0xFF;
                blue += rgb & 0xFF;
            }
        }
        
        double[] color = new double[3];
        color[0] = red * 1.0 / totalTraversed;
        color[1] = green * 1.0 / totalTraversed;
        color[2] = blue * 1.0 / totalTraversed;
        
        return color;
    }
    
    protected static double[] getAverageColor(BufferedImage image, Rectangle r) {
        return PictureHelper.getAverageColor(image, r.x, r.y, r.x + r.width, r.y + r.height);
    }
    
    protected static List<File> getAllFilesInList(File[] files) {
        List<File> allFiles = new ArrayList<>();
        
        for (int i = 0; i < files.length; i++) {
            allFiles.add(files[i]);
            if (files[i].isDirectory())
                PictureHelper.getAllFilesInDirectory(files[i], allFiles);
        }
            
        return allFiles;
    }
    
    protected static ArrayList<File> getAllFilesInDirectory(File dir) {
        ArrayList<File> allFiles = new ArrayList<>();
        PictureHelper.getAllFilesInDirectory(dir, allFiles);
        return allFiles;
    }
    
    private static void getAllFilesInDirectory(File dir, List<File> array) {
        File[] filesInCurrentDir = dir.listFiles();
        for (File f : filesInCurrentDir) {
            
            if (f.isFile()) {
                array.add(f);
            } else if (f.isDirectory()) {
                array.add(f); //add the directory anyways
                PictureHelper.getAllFilesInDirectory(f, array);
            }
            
            
        }
    }
    
    protected static ArrayList<File> removeDirectories(List<File> listOfFiles) {
        ArrayList<File> newList = new ArrayList<>();
        for (File f : listOfFiles) {
            if (f.isFile())
                newList.add(f);
        }
        return newList;
    }
    
    protected static ArrayList<File> keepFileTypes(List<File> listOfFiles, String[] fileTypes) {
        
        ArrayList<File> newList = new ArrayList<>();
        
        for (File f : listOfFiles) {
            String s = f.getName();
            String extension = PictureHelper.getLastFileExtension(s, false);
            extension = extension.toLowerCase();
            
            for (String fileType : fileTypes) {
                if (extension.equals(fileType))
                    newList.add(f);
            }
            
            
        }
        return newList;
    }
    
    protected static void printFiles(List<File> files) {
        for (File f : files) {
            if (f.isDirectory())
                System.out.println(f.getName() + "   DIRECTORY");
            else
                System.out.println(f.getName());
        }
    }
    
    protected static String getFileExtension(String file, boolean includeDot) {
        if (file == null || file.length() == 0)
            return null;
        
        char seperator = File.separatorChar;
        int lastSeperator = -1;
        for (int i = file.length() - 1; i >= 0; i--) {
            if (seperator == file.charAt(i)) {
                lastSeperator = i;
                break;
            }
        }
        
        String cutFile = file.substring(lastSeperator + 1);
        int firstDot = -1;
        for (int i = 0; i < cutFile.length(); i++) {
            if ('.' == cutFile.charAt(i)) {
                firstDot = i;
                break;
            }
        }
        
        String ext;
        if (includeDot)
            ext = cutFile.substring(firstDot, cutFile.length());
        else
            ext = cutFile.substring(firstDot + 1, cutFile.length());
        
        return ext.toLowerCase();
    }
    
    /**
     * Returns the last file extension
     * Ignores case of extension.
     * @param file
     * @param includeDot
     * @return 
     */
    protected static String getLastFileExtension(String file, boolean includeDot) {
        if (file == null) return null;
        
        char seperator = File.separatorChar;
        
        int dotIndex = -1;
        for (int i = file.length() - 1; i > 0; i--) {
            if (file.charAt(i) == '.') {
                dotIndex = i;
                break;
            }
        }
        
        return includeDot ? file.substring(dotIndex, file.length()) : file.substring(dotIndex + 1, file.length());
    }
    
    protected static void paintCenteredPicture(Graphics g, Image i, JPanel panel) {
        if (i == null) return;
        
        double aspectRatioOfPanel = panel.getWidth() * 1.0 / panel.getHeight();
        double aspectRatioOfImage = i.getWidth(null) * 1.0 / i.getHeight(null);

        int x,y,width,height;

        if (aspectRatioOfImage > aspectRatioOfPanel) {
            height = (int) (i.getHeight(null) / (i.getWidth(null) * 1.0 / panel.getWidth() ) );
            y = (panel.getHeight() / 2) - (height / 2);
            x = 0;
            width = panel.getWidth();
        } else {
            width = (int) (i.getWidth(null) / (i.getHeight(null) * 1.0 / panel.getHeight() ) );
            x = (panel.getWidth() / 2) - (width / 2);
            y = 0;
            height = panel.getHeight();
        }

        g.drawImage(i, x, y, width, height, null);
        //g.drawImage(image, 0, 0, null);
    }
    
    protected static BufferedImage makeCollageOfPictues(List<SpecialPicture> images, BufferedImage sourceImage, 
                    int scanWidth, int scanHeight, 
                    Dimension outputImagesDim, Dimension outputImageSize, 
                    int timesPictureUsed, double choosePicturesWithinRange) {
        
        int outputImagesWidth = outputImagesDim.width; //the little ones
        int outputImagesHeight = outputImagesDim.height;
        boolean usePicturesIndef = timesPictureUsed <= 0;
        int useEachPictureNumTimes = timesPictureUsed; //number of times to use each picture
        boolean selectBestPicture = (choosePicturesWithinRange <= 0.0);
        double choosePictureRandRange = choosePicturesWithinRange; //0.0 to 1.0
        
        //make output image
        BufferedImage outputImage = 
                new BufferedImage(outputImageSize.width, outputImageSize.height, sourceImage.getType() );
        
        Graphics outputGraphics = outputImage.getGraphics();
        
        Point outputPoint = new Point(0,0);
        Point sourcePoint = new Point(0,0);
        
        //loop over source image
        for (int y = 0; y < sourceImage.getHeight(); y += scanHeight) {
        for (int x = 0; x < sourceImage.getWidth() ; x += scanWidth) {
            sourcePoint.setLocation(x, y);
            
            //get color in scan region
            Rectangle scanRegion = new Rectangle (sourcePoint.x, sourcePoint.y, scanWidth, scanHeight);
            double[] colorInRegion = PictureHelper.getAverageColor(sourceImage, scanRegion);
            
            //select image to draw to output
            SpecialPicture toDraw = null;
            if (selectBestPicture == true) { //get best picture
                toDraw = PictureHelper.getBestPicture(colorInRegion, images);
            } else { //get random within range
                toDraw = PictureHelper.getRandomPictureInRange(colorInRegion, images, choosePictureRandRange);
                //if didnt find any picture in user range, select best picture
                if (toDraw == null)
                    toDraw = PictureHelper.getBestPicture(colorInRegion, images);
            }
            
            //paint to output
            if (toDraw != null) {
                //outputGraphics.drawImage(toDraw.image, outputPoint.x, outputPoint.y, null); //scale image too
                outputGraphics.drawImage(toDraw.image, outputPoint.x, outputPoint.y, 
                        outputImagesWidth, outputImagesHeight, null);
            }
            
            //increment times used if necessary
            if ( !usePicturesIndef && toDraw != null) {
                toDraw.timesUsed++;
                if (toDraw.timesUsed >= useEachPictureNumTimes) {
                    images.remove(toDraw);
                    toDraw.timesUsed = 0;
                }
            }
            
            outputPoint.x += outputImagesWidth;
        }
        //end of row
        outputPoint.y += outputImagesHeight;
        outputPoint.x = 0;
        }
        
        
        return outputImage;
    }
    
    protected static SpecialPicture getBestPicture(double[] desiredColor, List<SpecialPicture> pictures) {
        if (pictures == null || pictures.size() == 0) return null;
        
        SpecialPicture best = pictures.get(0);
        double smallestDifference = Double.MAX_VALUE;
        
        for (SpecialPicture temp : pictures) {
            if (temp != null) {
                double tempColorDifference = PictureHelper.getColorDifference(desiredColor, temp.averageColor);
                if ( tempColorDifference < smallestDifference) {
                    best = temp;
                    smallestDifference = tempColorDifference;
                }
            }
        }
        return best;
    }
    
    protected static SpecialPicture getRandomPictureInRange(double[] desiredColor, List<SpecialPicture> pictures, double range) {
        List<SpecialPicture> picturesInRange = new ArrayList<>();
        
        for (SpecialPicture temp : pictures) {
            double tempDifference = PictureHelper.getColorDifference(desiredColor, temp.averageColor);
            if (Math.abs(tempDifference) < range)
                picturesInRange.add(temp);
        }
        
        //if found no pictures in range
        if (picturesInRange.size() == 0)
            return null;
        
        Random r = new Random();
        r.setSeed(System.currentTimeMillis());
        int rand = r.nextInt(picturesInRange.size());
        
        return picturesInRange.get(rand);
    }
    
    protected static double getColorDifference(double[] c1, double[] c2) {
        double difference = 0.0;
        
        difference += Math.abs(c1[0] - c2[0]);
        difference += Math.abs(c1[1] - c2[1]);
        difference += Math.abs(c1[2] - c2[2]);
        
        difference /= 255*3; //percent difference
        
        return difference;
    }
    
    public static void main(String[] args) {
        System.out.println(PictureHelper.getFileExtension("C:\\Pictures\\myPic.jPg", true));
        System.out.println(PictureHelper.getFileExtension("myOtherPic.PnG", false));
        System.out.println(PictureHelper.getFileExtension("C:\\Users\\Calvin Cramer\\Pictures\\Calvin's Pictures\\Screen Shot 2016-02-06 at 9.33.54 PM.png", false));
        
        System.out.println(PictureHelper.getLastFileExtension("C:\\Pictures\\myPic.jPg", true));
        System.out.println(PictureHelper.getLastFileExtension("myOtherPic.PnG", false));
        System.out.println(PictureHelper.getLastFileExtension("C:\\Users\\Calvin Cramer\\Pictures\\Calvin's Pictures\\Screen Shot 2016-02-06 at 9.33.54 PM.png", false));
        
        double[] color1 = {0.0,0.0,0.0};
        double[] color2 = {255.0,255.0,255.0};
        double[] color3 = {128.0,128.0,128.0};
        System.out.println(PictureHelper.getColorDifference(color1, color2));
        System.out.println(PictureHelper.getColorDifference(color1, color3));
        System.out.println(PictureHelper.getColorDifference(color2, color3));
        System.out.println(PictureHelper.getColorDifference(color3, color2));
    }

}
