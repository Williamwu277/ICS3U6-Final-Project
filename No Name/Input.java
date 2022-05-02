public class Input{
    public static final int OFFSET_SENSITIVITY = 5;
    private int x;
    private int y;
    private int offsetX;
    private int offsetY;
    private int keyPress;
    private boolean mouseClicked;
    
    public Input(){
        this.offsetX = 0;
        this.offsetY = 0;
        this.keyPress = 0;
        this.mouseClicked = false;
    }
    
    public int getX(){
        return this.x;
    }
    
    public int getY(){
        return this.y;
    }
    
    public int getOffsetX(){
        return this.offsetX;
    }
    
    public int getOffsetY(){
        return this.offsetY;
    }
    
    public int getKeyPress(){
        return this.keyPress;
    }
    
    public boolean getMouseClicked(){
        return this.mouseClicked;
    }
    // generates mouse offsets so that when you move your mouse, it shifts the screen
    public void setX(int value){
        this.x = value;
        this.offsetX = Math.max(Math.min((this.x - Const.CENTER_X - Const.BLOCK_SIZE / 2)/OFFSET_SENSITIVITY, Const.MOUSE_OFFSET), -Const.MOUSE_OFFSET);
    }
    
    public void setY(int value){
        this.y = value;
        this.offsetY = Math.max(Math.min((this.y - Const.CENTER_Y - Const.BLOCK_SIZE / 2)/OFFSET_SENSITIVITY, Const.MOUSE_OFFSET), -Const.MOUSE_OFFSET);
    }
    
    public void setKeyPress(int key){
        this.keyPress = key;
    }
    
    public void setMouseClicked(boolean value){
        this.mouseClicked = value;
    }
}