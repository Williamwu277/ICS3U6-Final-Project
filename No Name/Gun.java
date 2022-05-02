import java.awt.Graphics;

public class Gun{
    public static final int RELOAD_SPEED = 25;
    public static final int CARTRIDGE_SIZE = 7;
    // won't actually be displayed inside the game
    
    private Bullet[] ammo;
    
    public Gun(int cartridgeSize){
        ammo = new Bullet[cartridgeSize];
    }
    
    public int getCartridgeSize(){
        return this.getCartridgeSize();
    }
    
    public void removeBullets(Player[] players, Grid grid, Player whitelist){
        for(int i=0; i<this.ammo.length; i++){
            if(this.ammo[i] != null && this.ammo[i].checkStatus(players, grid, whitelist)){
                this.ammo[i] = null;
            }
        }
    }
    
    public boolean addBullet(Bullet bullet){
        int index = -1;
        for(int i=0; i<this.ammo.length && index == -1; i++){
            if(this.ammo[i] == null){
                index = i;
            }
        }
        if(index != -1){
            this.ammo[index] = bullet;
            return true;
        }
        return false;
    }
    
    public void moveBullets(){
        for(int i=0; i<this.ammo.length; i++){
            if(this.ammo[i] != null){
                ammo[i].move();
            }
        }
    }
    
    public void drawBullets(Graphics g, int offsetX, int offsetY){
        for(int i=0; i<this.ammo.length; i++){
            if(this.ammo[i] != null){
                ammo[i].draw(g, offsetX, offsetY);
            }
        }
    }
}