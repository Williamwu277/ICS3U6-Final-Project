// divides the player's knockback by this.boost
public class ArmorBuff extends StatusEffects{
    public static final int ARMOR_BUFF = 2;
    public static final int ID = 2;
    private int boost;
    public ArmorBuff(int duration, int boost){
        super(duration, ID);
        this.boost = boost;
    }
    @Override
    public void performOperation(Player player){
        player.setArmorBuff(this.boost);
    }
}