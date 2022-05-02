import java.awt.image.BufferedImage;
import java.awt.Graphics;

public abstract class Template{
    private int x;
    private int y;
    private int length;
    private int width;
    private BufferedImage picture;
    
    Template(int x, int y, int length, int width, BufferedImage picture){
        this.x = x;
        this.y = y;
        this.length = length;
        this.width = width;
        this.picture = picture;
    }
    
    public int getX(){ 
        return this.x; 
    } 
    public int getY(){ 
        return this.y; 
    } 
    public int getL(){ 
        return this.length; 
    }
    public int getW(){
        return this.width;
    }
    public BufferedImage getP(){
        return this.picture;
    }
    public void setX(int x){ 
        this.x = x; 
    } 
    public void setY(int y){ 
        this.y = y; 
    }
    abstract public void draw (Graphics g, int offsetX, int offsetY);
}