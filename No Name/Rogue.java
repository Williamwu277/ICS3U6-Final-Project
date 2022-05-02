public class Rogue extends Player{
    public static final int ID = 2;
    public static final int ROGUE_SPEED = 3;
    public static final int ROGUE_RELOAD = 30;
    public static final int ROGUE_ARMOR = 1;
    // user constructor
    public Rogue(Scoreboard scoreboard){
        super(Const.BLOCK_SIZE, Const.BLOCK_SIZE, Player.PLAYER_SIZE, ROGUE_SPEED, ROGUE_RELOAD, ROGUE_ARMOR, scoreboard, Const.rogueImage);
    }
    // bot constructor
    public Rogue(int x, int y, Scoreboard scoreboard){
        super(x, y, Player.PLAYER_SIZE, ROGUE_SPEED, ROGUE_RELOAD, ROGUE_ARMOR, Player.BOT_RANGE, Player.BOT_ACCURACY, scoreboard, Const.rogueImage);
    }
}