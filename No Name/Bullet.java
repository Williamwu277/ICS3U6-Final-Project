import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject{
    public static final int BULLET_SPEED = 10;
    public static final int BULLET_SIZE = 10;
    public static final int KNOCKBACK_DURATION = 10;
    private int speedX, speedY;
    
    public Bullet(int startX, int startY, int speedX, int speedY, int bulletSize, BufferedImage picture){
        super(startX, startY, bulletSize, bulletSize, picture);
        this.speedX = speedX;
        this.speedY = speedY;
    }
    
    public void drawBullet(Graphics g){
        g.fillRect(this.getX(), this.getY(), this.getL(), this.getL());
    }
    
    public void move(){
        this.setX(this.getX() + this.speedX);
        this.setY(this.getY() + this.speedY);
        this.setBox();
    }
    
    // checks if this bullet is in a player or in a wall and acts accordingly.
    public boolean checkStatus(Player[] players, Grid grid, Player whitelist){
        Rectangle isInWall = this.inWall(grid);
        GameObject isInPlayer = this.inGameObject(players, whitelist);
        if(isInPlayer != null){
            Knockback knockback = new Knockback(this.speedX, this.speedY, KNOCKBACK_DURATION);
            ((Player)isInPlayer).addStatusEffect(knockback);
        }
        return isInWall != null || isInPlayer != null;
    }
    
    @Override
    public void draw(Graphics g, int offsetX, int offsetY){
        g.drawImage(this.getP(), Const.CENTER_X + this.getX() - offsetX - this.getL() / 2, Const.CENTER_Y + this.getY() - offsetY - this.getL()/2, this.getL() * 2, this.getL() * 2, null);
    }
}