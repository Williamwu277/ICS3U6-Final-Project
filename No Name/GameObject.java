import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class GameObject extends Template{ 
    private Rectangle box;  

    GameObject(int x, int y, int length, int width, BufferedImage picture){ 
        super(x, y, length, width, picture);
        this.box = new Rectangle(this.getX(), this.getY(), this.getL(), this.getW()); 
    } 

    public Rectangle getBox(){ 
        return this.box; 
    } 
    public void setBox(){ 
        this.box.setLocation(this.getX(), this.getY());  
    } 
    
    // check if player is inside a wall. Return the wall if so.
    public Rectangle inWall(Grid grid){
        // check the nine adjacent blocks
        int currentX = (this.getX() + this.getL()) / Const.BLOCK_SIZE;
        int currentY = (this.getY() + this.getL()) / Const.BLOCK_SIZE;
        final int[] DIRECTION_X = {0, 0, -1, 1, 0, 1, -1, -1, 1};
        final int[] DIRECTION_Y = {1, -1, 0, 0, 0, 1, -1, 1, -1};
        Rectangle[][] map = grid.getGrid();

        for(int i=0; i<DIRECTION_X.length; i++){
            int newX = DIRECTION_X[i] + currentX;
            int newY = DIRECTION_Y[i] + currentY;
            if(-1 < newX && newX < map.length && -1 < newY && newY < map[newX].length && map[newX][newY] != null && 
               map[newX][newY].intersects(this.box)){
                return map[newX][newY];
            }
        }
        return null;
    }
    // check if this object is in another object
    public GameObject inGameObject(GameObject[] items, GameObject whitelist){
        // exclude the whitelisted object from this process
        for(int i=0; i<items.length; i++){
            if(items[i] != null && items[i] != this && items[i] != whitelist && this.box.intersects(items[i].getBox())){
                return items[i];
            }
        }
        return null;
    }
}