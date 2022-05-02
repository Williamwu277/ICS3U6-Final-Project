import java.awt.Graphics;
import java.awt.Color;

public class Game{
    public static final int NUMBER_OF_PLAYERS = 4;
    // game objects
    private Grid grid;
    private Player[] players;
    private int winner;
    
    Game(int mapIndex, int characterIndex){
        this.winner = -1;
        this.players = new Player[Game.NUMBER_OF_PLAYERS];
        // create your player
        final Scoreboard SCOREBOARD = new Scoreboard(10, 10, Const.BLOCK_SIZE * 3, Const.BLOCK_SIZE / 2, 250, Color.MAGENTA, Color.CYAN);
        // random character
        if(characterIndex == Player.CHARACTER_TYPES){
            characterIndex = (int)(Math.random() * Player.CHARACTER_TYPES);
        }
        // you chose a character
        if(characterIndex == Knight.ID - 1){
            this.players[0] = new Knight(SCOREBOARD);
        }else if(characterIndex == Rogue.ID - 1){
            this.players[0] = new Rogue(SCOREBOARD);
        }else{
            this.players[0] = new Paladin(SCOREBOARD);
        }
        // create the bots
        final int[] START_X = {Grid.LENGTH - Const.BLOCK_SIZE * 2, Grid.LENGTH - Const.BLOCK_SIZE * 2, Const.BLOCK_SIZE};
        final int[] START_Y = {Const.BLOCK_SIZE, Grid.LENGTH - Const.BLOCK_SIZE * 2, Grid.LENGTH - Const.BLOCK_SIZE * 2};
        final Scoreboard[] SCOREBOARDS = {
            new Scoreboard(Const.WIDTH - Const.BLOCK_SIZE * 3 - 10, 10, Const.BLOCK_SIZE * 3,
                                    Const.BLOCK_SIZE / 2, 250, Color.MAGENTA, Color.BLUE),
            new Scoreboard(10, 20 + Const.BLOCK_SIZE / 2, Const.BLOCK_SIZE * 3, Const.BLOCK_SIZE / 2, 
                                    250, Color.MAGENTA, Color.GREEN),
            new Scoreboard(Const.WIDTH - Const.BLOCK_SIZE * 3 - 10, 20 + Const.BLOCK_SIZE / 2, 
                                    Const.BLOCK_SIZE * 3, Const.BLOCK_SIZE / 2, 250, Color.MAGENTA, Color.ORANGE)
        };
        for(int i=1; i<=NUMBER_OF_PLAYERS-1; i++){
            int characterChoice = (int)(Math.random() * Player.CHARACTER_TYPES);
            if(characterChoice == Knight.ID - 1){
                this.players[i] = new Knight(START_X[i-1], START_Y[i-1], SCOREBOARDS[i-1]);
            }else if(characterChoice == Rogue.ID - 1){
                this.players[i] = new Rogue(START_X[i-1], START_Y[i-1], SCOREBOARDS[i-1]);
            }else{
                this.players[i] = new Paladin(START_X[i-1], START_Y[i-1], SCOREBOARDS[i-1]);
            }
        }
        // which maps
        if(mapIndex == Grid.prebuiltMaps.length){
            this.grid = new Grid(Grid.GRID_SIZE);
        }else{
            this.grid = new Grid(Grid.GRID_SIZE, Grid.prebuiltMaps[mapIndex]);
        }
    }
    
    public int update(Input input){
        // do stuff here
        this.grid.update(this.players);
        
        for(int i=0; i<this.players.length; i++){
            this.players[i].reset();
        }
        
        // the order goes: reset, decisions, transition, update
        for(int i=0; i<this.players.length; i++){
            this.players[i].processStatusEffects();
        }
        
        this.players[0].makeDecision(input);
        for(int i=1; i<this.players.length; i++){
            this.players[i].makeDecision(this.players, this.grid, input);
        }
        
        for(int i=0; i<this.players.length; i++){
            this.players[i].transition(grid);
            this.players[i].update(players, grid);
        }

        // check for any winning players
        for(int i=0; i<this.players.length; i++){
            if(this.players[i].getScoreboard().checkStatus()){
                this.winner = i;
                return i;
            }
        }
        return -1;
    }
    
    public void draw(Graphics g, Input input){
        // color background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Const.HEIGHT, Const.WIDTH);
        // grid
        grid.draw(g, players[0].getX() + input.getOffsetX(), players[0].getY() + input.getOffsetY());
        // players
        for(int i=0; i<players.length; i++){
            players[i].draw(g, players[0].getX() + input.getOffsetX(), players[0].getY() + input.getOffsetY());
        }
        // draw static things last
        for(int i=0; i<players.length; i++){
            players[i].getScoreboard().draw(g, 0, 0); // scoreboards have no offset 
        }

        // if there is a winner, draw the sign
        if(this.winner == 0){
            g.drawImage(Const.victoryImage, 0, 0, Const.WIDTH, Const.HEIGHT, null);
        }else if(this.winner != -1){
            g.drawImage(Const.defeatImage, 0, 0, Const.WIDTH, Const.HEIGHT, null);
        }
    }
}
