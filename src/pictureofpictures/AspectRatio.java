package pictureofpictures;

public class AspectRatio {
    protected int width;
    protected int height;
    protected String description;
    
    public AspectRatio(int num1, int num2) {
        this.width = num1;
        this.height = num2;
        
    }
    
    public AspectRatio(int num1, int num2, String description) {
        this.width = num1;
        this.height = num2;
        this.description = description;
    }
    
    protected void invertRatio() {
        int temp = width;
        this.width = height;
        this.height = temp;
    }
    
    @Override
    public String toString() {
        if (this.description == null)
            return "" + this.width + ":" + this.height;
        else
            return this.description + " " + this.width + ":" + this.height;
    }
}
