public class SpeedBuff extends StatusEffects{
    public static final int SPEED_BUFF = 1;
    public static final int ID = 3;
    private int boost;
    public SpeedBuff(int duration, int boost){
        super(duration, ID);
        this.boost = boost;
    }
    @Override
    public void performOperation(Player player){
        player.setSpeed(player.getSpeed() + this.boost);
    }
}