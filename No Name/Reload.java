public class Reload extends StatusEffects{
    public static final int ID = 4;
    public Reload(int duration){
        super(duration, ID);
    }
    @Override
    public void performOperation(Player player){
        player.setCanShoot(false);
    }
}