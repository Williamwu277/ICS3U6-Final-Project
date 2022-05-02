public class Paladin extends Player{
    public static final int ID = 1;
    public static final int PALADIN_SPEED = 2;
    public static final int PALADIN_RELOAD = 20;
    public static final int PALADIN_ARMOR = 1;
    // user constructor
    public Paladin(Scoreboard scoreboard){
        super(Const.BLOCK_SIZE, Const.BLOCK_SIZE, Player.PLAYER_SIZE, PALADIN_SPEED, PALADIN_RELOAD, PALADIN_ARMOR, scoreboard, Const.paladinImage);
    }
    // bot  constructor
    public Paladin(int x, int y, Scoreboard scoreboard){
        super(x, y, Player.PLAYER_SIZE, PALADIN_SPEED, PALADIN_RELOAD, PALADIN_ARMOR, Player.BOT_RANGE, Player.BOT_ACCURACY, scoreboard, Const.paladinImage);
    }
}