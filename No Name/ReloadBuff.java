public class ReloadBuff extends StatusEffects{
    public static final int RELOAD_BUFF = 10;
    public static final int ID = 1;
    private int boost;
    public ReloadBuff(int duration, int boost){
        super(duration, ID);
        this.boost = boost;
    }
    @Override
    public void performOperation(Player player){
        player.setReloadBuff(this.boost);
    }
}