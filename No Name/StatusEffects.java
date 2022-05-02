public abstract class StatusEffects{
    public static final int STATUS_EFFECT_LENGTH = 1000;
    public static final int STATUS_EFFECT_TYPES = 5;
    // status effects do not stack if they are of the same type. Opponents receive immunity to further same-type
    // status effects when one is already in place.
    private int duration;
    private int id;
    public StatusEffects(int duration, int id){
        this.duration = duration;
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public int getDuration(){
        return this.duration;
    }
    public void countDown(){
        this.duration --;
    }
    abstract public void performOperation(Player player);
}