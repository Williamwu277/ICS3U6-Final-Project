public class Knight extends Player{
    public static final int ID = 3;
    public static final int KNIGHT_SPEED = 2;
    public static final int KNIGHT_RELOAD = 40;
    public static final int KNIGHT_ARMOR = 2;
    // user initialization
    public Knight(Scoreboard scoreboard){
        super(Const.BLOCK_SIZE, Const.BLOCK_SIZE, Player.PLAYER_SIZE, KNIGHT_SPEED, KNIGHT_RELOAD, KNIGHT_ARMOR, scoreboard, Const.knightImage);
    }
    // bot initialization
    public Knight(int x, int y, Scoreboard scoreboard){
        super(x, y, Player.PLAYER_SIZE, KNIGHT_SPEED, KNIGHT_RELOAD, KNIGHT_ARMOR, Player.BOT_RANGE, Player.BOT_ACCURACY, scoreboard, Const.knightImage);
    }
}