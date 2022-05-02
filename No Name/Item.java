import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Item extends GameObject{
    StatusEffects statusEffect;
    public Item(int x, int y, int length, StatusEffects statusEffect, BufferedImage picture){
        super(x, y, length, length, picture);
        this.statusEffect = statusEffect;
    }
    @Override
    public void draw(Graphics g, int offsetX, int offsetY){
        g.drawImage(this.getP(), Const.CENTER_X + this.getX() - offsetX, Const.CENTER_Y + this.getY() - offsetY, this.getL(), this.getL(), null);
    }
    // transfer status effects to player and hide item
    public boolean transfer(Player[] players){
        GameObject gameObject = this.inGameObject(players, null);
        if(gameObject != null){
            ((Player)gameObject).addStatusEffect(this.statusEffect);
            return true;
        }
        return false;
    }
}