import java.awt.Color;
import java.awt.Graphics;

public class Scoreboard extends Template{
    private int totalStorage;
    private int currentStorage;
    private Color backgroundColor;
    private Color foregroundColor;
    
    public Scoreboard(int x, int y, int length, int width, int totalStorage, Color backgroundColor, Color foregroundColor){
        super(x, y, length, width, null);
        this.totalStorage = totalStorage;
        this.currentStorage = 0;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
    }
    
    public boolean checkStatus(){
        return this.currentStorage == this.totalStorage;
    }
    
    // check if player is on the center
    // check all four corners, else it is unfair to the player on the top left
    public void update(Player player){
        int targetX = Grid.LENGTH / 2 / Const.BLOCK_SIZE;
        int targetY = Grid.LENGTH / 2 / Const.BLOCK_SIZE;
        if((player.getX() / Const.BLOCK_SIZE == targetX && player.getY() / Const.BLOCK_SIZE == targetY) ||
           ((player.getX()+Player.PLAYER_SIZE)/Const.BLOCK_SIZE == targetX && (player.getY()+Player.PLAYER_SIZE)/Const.BLOCK_SIZE == targetY) ||
           ((player.getX()+Player.PLAYER_SIZE)/Const.BLOCK_SIZE == targetX && player.getY() / Const.BLOCK_SIZE == targetY) ||
           (player.getX() / Const.BLOCK_SIZE == targetX && (player.getY()+Player.PLAYER_SIZE)/Const.BLOCK_SIZE == targetY)){
            this.currentStorage = Math.min(this.currentStorage + 1, this.totalStorage);
        }
    }
    
    public void draw(Graphics g, int offsetX, int offsetY){
        g.setColor(this.backgroundColor);
        g.fillRect(this.getX() + offsetX, this.getY() + offsetY, this.getL(), this.getW());
        g.setColor(this.foregroundColor);
        g.fillRect(this.getX() + offsetX, this.getY() + offsetY, (int)(this.getL() * (double)this.currentStorage / this.totalStorage), 
                   this.getW());
    }
}