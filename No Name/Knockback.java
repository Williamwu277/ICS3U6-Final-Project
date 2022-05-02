public class Knockback extends StatusEffects{
    public static final int ID = 5;
    private int moveX;
    private int moveY;
    public Knockback(int moveX, int moveY, int duration){
        super(duration, ID);
        this.moveX = moveX;
        this.moveY = moveY;
    }
    @Override
    // translates velocity of bullet to player and divides this by player's defence
    public void performOperation(Player player){
        player.setKnockback(true);
        player.move(this.moveX / Math.max(1, player.getArmorBuff()), this.moveY / Math.max(1, player.getArmorBuff()));
    }
}