import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.util.Arrays;

public class Grid{
    // prebuilt maps
    public static String[][] prebuiltMaps;
    public static BufferedImage[] mapImages;

    public static final int GRID_SIZE = Math.min(Const.HEIGHT, Const.WIDTH) / Const.BLOCK_SIZE;
    public static final int LENGTH = GRID_SIZE * Const.BLOCK_SIZE;
    // change in coordinates when moving horizontally and vertically
    public static final int[] CHANGE_X = {0, 0, -1, 1};
    public static final int[] CHANGE_Y = {1, -1, 0, 0};
    
    private Rectangle[][] grid;
    private Item[][] items;
    // stores the direction to travel to next for bots
    private int[][] directions;
    
    private int blocksPerSide;
    
    public static void loadMaps() throws Exception{
        // first line is the number of subsequent maps
        File prebuilt = new File("PrebuiltMaps.txt");
        Scanner reader = new Scanner(prebuilt);
        int mapsToCheck = Integer.parseInt(reader.nextLine());
        prebuiltMaps = new String[mapsToCheck][Grid.GRID_SIZE];
        // if this throws an IO exception, your input is bad
        for(int i=0; i<mapsToCheck; i++){
            // read a blank line. Assumes that every prebuilt map has an image
            reader.nextLine();
            for(int j=0; j<Grid.GRID_SIZE; j++){
                prebuiltMaps[i][j] = reader.nextLine();
            }
        }
        reader.close();
        // load images
        final int PREBUILT_MAP_PICTURES = 6;
        mapImages = new BufferedImage[PREBUILT_MAP_PICTURES];
        for(int i=0; i<mapImages.length; i++){
            try{
                mapImages[i] = ImageIO.read(new File("images/maps/Map" + (i + 1) + ".png"));
            }catch (IOException ex){}
        }
    }
    
    // runs O(N^2) dijkstra's where N is the number of nodes
    // which means it is O(M^4) on a grid
    // only needs to be run once or twice per game
    public static int[][] dijkstra(int targetX, int targetY, int[][] weights){
        int[][] distance = new int[weights.length][weights[0].length];
        // each integer from 0-3 represents a direction
        int[][] direction = new int[weights.length][weights[0].length];
        boolean[][] confirmed = new boolean[weights.length][weights[0].length];
        // prefill arrays
        for(int i=0; i<distance.length; i++){
            Arrays.fill(distance[i], Integer.MAX_VALUE);
            Arrays.fill(direction[i], -1);
        }
        int currentX = targetY;
        int currentY = targetX;
        distance[currentX][currentY] = 0;
        // consider all M^2 nodes
        boolean done = false;
        for(int i=0; i<weights.length && !done; i++){
            for(int j=0; j<weights[0].length && !done; j++){
                // find minimum distance node that hasn't been visited yet
                int minimumX = -1;
                int minimumY = -1;
                int minimumDistance = Integer.MAX_VALUE;
                for(int k=0; k<weights.length; k++){
                    for(int l=0; l<weights[0].length; l++){
                        // if the weight is the maximum value, that means it is a wall
                        if(!confirmed[k][l] && minimumDistance > distance[k][l] && weights[k][l] < Integer.MAX_VALUE){
                            minimumX = k;
                            minimumY = l;
                            minimumDistance = distance[k][l];
                        }
                    }
                }
                // if there are no more to consider; otherwise, mark the node as visited
                if(minimumX == -1){
                    done = true;
                }else{
                    confirmed[minimumX][minimumY] = true;
                }
                // propagate outwards from the selected node and update distances accordingly
                for(int k=0; k<CHANGE_X.length && !done; k++){
                    int nextX = minimumX + CHANGE_X[k];
                    int nextY = minimumY + CHANGE_Y[k];
                    // check if they are possible to travel to
                    if(0 <= nextX && nextX < weights.length && 0 <= nextY && nextY < weights[nextX].length &&
                       minimumDistance + weights[nextX][nextY] < distance[nextX][nextY] && 
                       weights[nextX][nextY] < Integer.MAX_VALUE){
                        // update
                        distance[nextX][nextY] = minimumDistance + weights[nextX][nextY];
                        direction[nextX][nextY] = k;
                    }
                }
            }
        }
        return direction;
    }
    
    // generates weights for the dijkstras—allowing bots to more easily find items
    public static int[][] generateRandomWeights(Rectangle[][] grid, Item[][] items){
        final int EMPTY_SLOT_UPPER_BOUND = 100000;
        final int ITEM_SLOT_UPPER_BOUND = 10;
        // the grid will never be empty
        int[][] weights = new int[grid.length][grid[0].length];
        for(int i=0; i<grid.length; i++){
            for(int j=0; j<grid.length; j++){
                // generate weights accordingly to what each cell contains
                if(grid[i][j] == null && items[i][j] == null){
                    weights[i][j] = (int)(Math.random() * EMPTY_SLOT_UPPER_BOUND) + 1;
                }else if(grid[i][j] == null && items[i][j] != null){
                    weights[i][j] = (int)(Math.random() * ITEM_SLOT_UPPER_BOUND) + 1;
                }else{
                    weights[i][j] = Integer.MAX_VALUE;
                }
            }
        }
        return weights;
    }
    
    public static Rectangle[][] generateMap(int sideLength, int wallChance){
        final int HIGHER_BOUND = 1000;
        // generate random weights for each block
        int[][] weights = new int[sideLength][sideLength];
        for(int i=0; i<sideLength; i++){
            for(int j=0; j<sideLength; j++){
                if(i == 0 || j == 0 || i == sideLength - 1 || j == sideLength - 1){
                    weights[i][j] = Integer.MAX_VALUE;
                }else{
                    weights[i][j] = (int)(Math.random() * HIGHER_BOUND) + 1;
                }
            }
        }
        // run dijkstras on the graph
        int[][] destination = dijkstra(sideLength/2, sideLength/2, weights);
        // we have to guarentee that there is a path from center to all four corners
        
        boolean[][] unblock = new boolean[sideLength][sideLength];
        final int[] CORNER_X = {1, 1, sideLength-2, sideLength-2};
        final int[] CORNER_Y = {1, sideLength-2, 1, sideLength-2};
        // find paths from corners to center and marked them as not having walls
        for(int corner = 0; corner < CORNER_X.length; corner++){
            int currentX = CORNER_X[corner];
            int currentY = CORNER_Y[corner];
            unblock[currentX][currentY] = true;
            while(currentX != sideLength/2 || currentY != sideLength/2){
                int newX = currentX - CHANGE_X[destination[currentX][currentY]];
                currentY -= CHANGE_Y[destination[currentX][currentY]];
                currentX = newX;
                unblock[currentX][currentY] = true;
            }
        }
        Rectangle[][] map = new Rectangle[sideLength][sideLength];
        // generate walls randomly
        for(int i=0; i<map.length; i++){
            for(int j=0; j<map[0].length; j++){
                if(!unblock[i][j] && ((int)(Math.random() * 100) < wallChance || i == 0 || j == 0 || i == sideLength-1 || j == sideLength-1)){
                    map[i][j] = new Rectangle(i * Const.BLOCK_SIZE,j * Const.BLOCK_SIZE, Const.BLOCK_SIZE, Const.BLOCK_SIZE);
                }
            }
        }
        return map;
    }
    
    // generate a grid from prebuilt maps
    // accepts an array of strings
    // make sure that prebuilt maps have walls surrounding and have paths from the center to all four corners
    // W for wall, S for speed, R for reload, A for armor and . for empty block.
    Grid(int blocksPerSide, String[] prebuilt){
        this.blocksPerSide = blocksPerSide;
        this.items = new Item[this.blocksPerSide][this.blocksPerSide];
        this.grid = new Rectangle[this.blocksPerSide][this.blocksPerSide];
        for(int i=0; i<prebuilt.length; i++){
            for(int j=0; j<prebuilt[i].length(); j++){
                char current = prebuilt[i].charAt(j);
                if(current == 'W'){
                    this.grid[i][j] = new Rectangle(i * Const.BLOCK_SIZE, j * Const.BLOCK_SIZE, Const.BLOCK_SIZE, Const.BLOCK_SIZE);
                }else if(current == 'S'){
                    StatusEffects statusEffect = new SpeedBuff(StatusEffects.STATUS_EFFECT_LENGTH, SpeedBuff.SPEED_BUFF);
                    this.items[i][j] = new Item(i * Const.BLOCK_SIZE, j * Const.BLOCK_SIZE, Const.BLOCK_SIZE, statusEffect, Const.speedBuffImage);
                }else if(current == 'R'){
                    StatusEffects statusEffect = new ReloadBuff(StatusEffects.STATUS_EFFECT_LENGTH, ReloadBuff.RELOAD_BUFF);
                    this.items[i][j] = new Item(i * Const.BLOCK_SIZE, j * Const.BLOCK_SIZE, Const.BLOCK_SIZE, statusEffect, Const.reloadBuffImage);
                }else if(current == 'A'){
                    StatusEffects statusEffect = new ArmorBuff(StatusEffects.STATUS_EFFECT_LENGTH, ArmorBuff.ARMOR_BUFF);
                    this.items[i][j] = new Item(i * Const.BLOCK_SIZE, j * Const.BLOCK_SIZE, Const.BLOCK_SIZE, statusEffect, Const.armorBuffImage);
                }
            }
        }
        int[][] weights = Grid.generateRandomWeights(this.grid, this.items);
        this.directions = Grid.dijkstra(blocksPerSide/2, blocksPerSide/2, weights);
    }
    
    Grid(int blocksPerSide){
        this.blocksPerSide = blocksPerSide;
        this.items = new Item[this.blocksPerSide][this.blocksPerSide];
        this.grid = generateMap(this.blocksPerSide, 20);
        // generate random items
        final int ITEM_CHANCE =  5;
        for(int i=0; i<grid.length; i++){
            for(int j=0; j<grid.length; j++){
                if(this.grid[i][j] == null && (int)(Math.random() * 100) < ITEM_CHANCE){
                    int buffType = (int)(Math.random() * StatusEffects.STATUS_EFFECT_TYPES) + 1;
                    StatusEffects statusEffect;
                    BufferedImage picture;
                    if(buffType == ArmorBuff.ID){
                        statusEffect = new ArmorBuff(StatusEffects.STATUS_EFFECT_LENGTH, ArmorBuff.ARMOR_BUFF);
                        picture = Const.armorBuffImage;
                    }else if(buffType == ReloadBuff.ID){
                        statusEffect = new ReloadBuff(StatusEffects.STATUS_EFFECT_LENGTH, ReloadBuff.RELOAD_BUFF);
                        picture = Const.reloadBuffImage;
                    }else{
                        // must be SpeedBuff
                        statusEffect = new SpeedBuff(StatusEffects.STATUS_EFFECT_LENGTH, SpeedBuff.SPEED_BUFF);
                        picture = Const.speedBuffImage;
                    }
                    this.items[i][j] = new Item(i * Const.BLOCK_SIZE,j * Const.BLOCK_SIZE, Const.BLOCK_SIZE, statusEffect, picture);
                }
            }
        }
        // generate random weights
        int[][] weights = Grid.generateRandomWeights(this.grid, this.items);
        // use dijkstras
        this.directions = Grid.dijkstra(blocksPerSide/2, blocksPerSide/2, weights);
    }
    
    public int[][] getDirections(){
        return this.directions;
    }
    
    public int getBlocksPerSide(){
        return this.blocksPerSide;
    }
    public Rectangle[][] getGrid(){
        return this.grid;
    }
    public Item[][] getItems(){
        return this.items;
    }
    
    public void draw(Graphics g, int offsetX, int offsetY){
        for(int i=0; i<this.blocksPerSide; i++){
            for(int j=0; j<this.blocksPerSide; j++){
                int xPosition = Const.CENTER_X - offsetX + i * Const.BLOCK_SIZE;
                int yPosition = Const.CENTER_Y - offsetY + j * Const.BLOCK_SIZE;
                if(this.grid[i][j] != null){
                    g.drawImage(Const.wallImage, xPosition, yPosition, Const.BLOCK_SIZE, Const.BLOCK_SIZE, null);
                }else if((i + j) % 2 == 0){
                    // use manhattan distance to make grid checkerboard
                    g.drawImage(Const.floorImage, xPosition, yPosition, Const.BLOCK_SIZE, Const.BLOCK_SIZE, null);
                }else{
                    g.drawImage(Const.floor2Image, xPosition, yPosition, Const.BLOCK_SIZE, Const.BLOCK_SIZE, null);
                }
                if(this.items[i][j] != null){
                    this.items[i][j].draw(g, offsetX, offsetY);
                }
            }
        }
        int xPosition = Const.CENTER_X - offsetX + Grid.GRID_SIZE/2*Const.BLOCK_SIZE;
        int yPosition = Const.CENTER_Y - offsetY + Grid.GRID_SIZE/2*Const.BLOCK_SIZE;
        // draw chest image in the middle
        g.drawImage(Const.chestImage, xPosition, yPosition, Const.BLOCK_SIZE, Const.BLOCK_SIZE, null);
    }
    
    public void update(Player[] players){
        for(int i=0; i<this.items.length; i++){
            for(int j=0; j<this.items[i].length; j++){
                if(this.items[i][j] != null){
                    boolean result = this.items[i][j].transfer(players);
                    if(result){
                        this.items[i][j] = null;
                    }
                }
            }
        }
    }
    
}