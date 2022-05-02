import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public abstract class Player extends GameObject{
    
    public static final int CHARACTER_TYPES = 3;
    public static final int PLAYER_SIZE = 30;
    public static final int BOT_RANGE = 300;
    public static final int BOT_ACCURACY = 25;
    
    private int velocityX;
    private int velocityY;
    private int targetX;
    private int targetY;
    private Gun gun;
    
    private int baseSpeed;
    private int speed;
    
    // only matters for bots since humans can't possibly see across the map
    private int range;
    private int accuracy;
    private int reloadSpeed;
    // accuracy is how much deviation the bot aiming has from the original
    
    // status variables
    private boolean canShoot;
    private boolean knockedBack;
    private int armorBoost;
    private int baseArmor;
    private int reloadBoost;
    
    // status effects
    private StatusEffects[] statusEffects;
    // scoreboard
    private Scoreboard scoreboard;
    
    // if you want to build a bot
    Player(int x, int y, int length, int speed, int reloadSpeed, int armor, int range, int accuracy, Scoreboard scoreboard, BufferedImage picture){
        super(x, y, length, length, picture);
        this.velocityX = 0;
        this.velocityY = 0;
        this.targetX = x;
        this.targetY = y;
        this.baseSpeed = speed;
        this.reloadSpeed = reloadSpeed;
        this.baseArmor = armor;
        this.range = range;
        this.accuracy = accuracy;
        this.gun = new Gun(Gun.CARTRIDGE_SIZE);
        this.statusEffects = new StatusEffects[StatusEffects.STATUS_EFFECT_TYPES];
        this.scoreboard = scoreboard;
    }
    
    // if you want to build a player
    Player(int x, int y, int length, int speed, int reloadSpeed, int armor, Scoreboard scoreboard, BufferedImage picture){
        super(x, y, length, length, picture);
        this.velocityX = 0;
        this.velocityY = 0;
        this.targetX = x;
        this.targetY = y;
        this.baseSpeed = speed;
        this.reloadSpeed = reloadSpeed;
        this.baseArmor = armor;
        this.gun = new Gun(Gun.CARTRIDGE_SIZE);
        this.statusEffects = new StatusEffects[StatusEffects.STATUS_EFFECT_TYPES];
        this.scoreboard = scoreboard;
    }
    
    public Gun getGun(){
        return this.gun;
    }
    
    public int getVelocityX(){
        return this.velocityX;
    }
    
    public int getVelocityY(){
        return this.velocityY;
    }
    
    public int getArmorBuff(){
        return this.armorBoost;
    }
    
    public int getSpeed(){
        return this.speed;
    }
    
    public Scoreboard getScoreboard(){
        return this.scoreboard;
    }
    
    public void setSpeed(int value){
        this.speed = value;
    }
    
    public void setKnockback(boolean value){
        this.knockedBack = value;
    }
    
    public void setCanShoot(boolean value){
        this.canShoot = value;
    }
    
    public void setReloadBuff(int value){
        this.reloadBoost = value;
    }
    
    public void setArmorBuff(int value){
        this.armorBoost = value;
    }
    
    @Override
    public void draw(Graphics g, int offsetX, int offsetY){
        this.gun.drawBullets(g, offsetX, offsetY);
        g.drawImage(this.getP(), Const.CENTER_X + this.getX() - offsetX - this.getL() / 2, Const.CENTER_Y + this.getY() - offsetY - this.getL() / 2, (int)(this.getL() * 2), (int)(this.getL() * 2), null);
        // note that the original picture is too small. So, we need to expand it 
        // can't draw scoreboard here because they should be drawn last
    }
    
    // resets everything
    public void reset(){
        this.speed = this.baseSpeed;
        this.canShoot = true;
        this.knockedBack = false;
        this.armorBoost = this.baseArmor;
        this.reloadBoost = 0;
        this.velocityX = 0;
        this.velocityY = 0;
    }
    
    public void move(int xValue, int yValue){
        this.velocityX += xValue;
        this.velocityY += yValue;
    }
    public void transition(Grid grid){
        int previousX = this.getX();
        int previousY = this.getY();
        this.setX(this.getX() + this.velocityX);
        this.setBox();
        Rectangle intersect = this.inWall(grid);
        // operates under the assumption that the player won't already be in a wall
        // if there is an intersecting rectangle, move this player next to it, but not touching.
        if(intersect != null){
            if(previousX + this.getL() <= intersect.getX()){
                this.setX(Math.min((int)intersect.getX() - this.getL() - 1 - previousX, this.velocityX) + previousX);
            }else if(previousX >= intersect.getX() + Const.BLOCK_SIZE){
                this.setX(Math.max((int)intersect.getX() + Const.BLOCK_SIZE + 1 - previousX, this.velocityX) + previousX);
            }
            this.setBox();
        }
        // dp this for Y too
        this.setY(this.getY() + this.velocityY);
        this.setBox();
        intersect = this.inWall(grid);
        if(intersect != null){
            if(previousY + this.getL() <= intersect.getY()){
                this.setY(Math.min((int)intersect.getY() - this.getL() - 1 - previousY, this.velocityY) + previousY);
            }else if(previousY >= intersect.getY() + Const.BLOCK_SIZE){
                this.setY(Math.max((int)intersect.getY() + Const.BLOCK_SIZE + 1 - previousY, this.velocityY) + previousY);
            }
            this.setBox();
        }
    }
    // only applies for bots
    public void relocateTarget(int[][] directions){
        // pick a new destination as a target to move to
        int currentX = this.getX() / Const.BLOCK_SIZE;
        int currentY = this.getY() / Const.BLOCK_SIZE;
        int newIndex = directions[currentX][currentY];
        if(newIndex == -1){
            // this means that you are inside of a wall or have already made it
            return;
        }
        this.targetX = (-Grid.CHANGE_X[newIndex] + currentX) * Const.BLOCK_SIZE + (Const.BLOCK_SIZE - this.getL()) / 2;
        this.targetY = (-Grid.CHANGE_Y[newIndex] + currentY) * Const.BLOCK_SIZE + (Const.BLOCK_SIZE - this.getL()) / 2;
    }
    // only applies for bots
    public void relocateStart(int[][] directions){
        // when bot knocked out of path, relocate a starting position and target
        int currentX = this.getX() / Const.BLOCK_SIZE;
        int currentY = this.getY() / Const.BLOCK_SIZE;
        int bestX = currentX;
        int bestY = currentY;
        int distance = Integer.MAX_VALUE;
        // check nearest blocks for shortest distance
        for(int i=0; i<Grid.CHANGE_X.length; i++){
            int newX = currentX + Grid.CHANGE_X[i];
            int newY = currentY + Grid.CHANGE_Y[i];
            if(0 <= newX && newX < directions.length && 0 <= newY && newY < directions[0].length && directions[newX][newY] != -1){
                int newDistance = (int)Math.sqrt(Math.pow(newX * Const.BLOCK_SIZE - this.getX(),2) + Math.pow(newY * Const.BLOCK_SIZE - this.getY(), 2));
                if(distance > newDistance){
                    distance = newDistance;
                    bestX = newX;
                    bestY = newY;
                }
            }
        }
        this.targetX = bestX * Const.BLOCK_SIZE + (Const.BLOCK_SIZE - this.getL()) / 2;
        this.targetY = bestY * Const.BLOCK_SIZE + (Const.BLOCK_SIZE - this.getL()) / 2;
    }
    // using something like a breadth-first-search. Operates in O(N) time complexity
    public boolean intersectingWall(int startX, int startY, int endX, int endY, Grid grid){
        // go from top to bottom
        if(endX < startX){
            int tempX = startX;
            int tempY = startY;
            startX = endX;
            startY = endY;
            endX = tempX;
            endY = tempY;
        }
        int[] addX = new int[2];
        int[] addY = new int[2];
        // remember that these are different from math coordinates
        // if (1, 0) and (0, 1) are blocked, (1, 1) is also blocked
        if(endY - startY <= 0){
            // OOO
            // XOO
            // OXO
            addX[0] = 0; addX[1] = 1;
            addY[0] = -1; addY[1] = 0; 
        }else{
            // OOO
            // OOX
            // OXO
            addX[0] = 1; addX[1] = 0;
            addY[0] = 0; addY[1] = 1;
        }
        Line2D line = new Line2D.Double(startX, startY, endX, endY);
        int currentX = startX / Const.BLOCK_SIZE;
        int currentY = startY / Const.BLOCK_SIZE;
        Rectangle[][] map = grid.getGrid();
        boolean found = true;
        // boogy rectangle to check line intersection
        Rectangle check = new Rectangle(0, 0, Const.BLOCK_SIZE, Const.BLOCK_SIZE);
        // isn't an infinite loop since bullets always hit a wall in the end
        while(found){
            found = false;
            for(int i=0; i<addX.length && !found; i++){
                int newX = currentX + addX[i];
                int newY = currentY + addY[i];
                check.setLocation(newX * Const.BLOCK_SIZE, newY * Const.BLOCK_SIZE);
                // see which neigboring grid line travels through and if there is a wall there
                if(0 <= newX && newX < map.length && 0 <= newY && newY < map[0].length && map[newX][newY] == null &&
                line.intersects(check) && !found){
                    found = true;
                    currentX = newX;
                    currentY = newY;
                }
            }
        }
        if(currentX == endX / Const.BLOCK_SIZE && currentY == endY / Const.BLOCK_SIZE){
            return false;
        }else{
            return true;
        }
    }
    // for bots to find a player to shoot. Check if there is a wall between, and find the closest player
    public Player findTarget(Player[] players, Grid grid){
        int index = -1;
        int distance = this.range + 1;
        for(int i=0; i<players.length; i++){
            // distance formula
            int newDistance = (int)Math.sqrt(Math.pow(players[i].getX() - this.getX(), 2) + Math.pow(players[i].getY() - this.getY(), 2));
            if(this != players[i] && !this.intersectingWall(this.getX() + this.getL() / 2, this.getY() + this.getL() / 2, 
        players[i].getX() + players[i].getL() / 2, players[i].getY() + players[i].getL() / 2, grid) && distance > newDistance){
                distance = newDistance;
                index = i;
            }
        }
        if(index == -1){
            return null;
        }else{
            return players[index];
        }
    }
    // for bots
    public void makeDecision(Player[] players, Grid grid, Input input){
        // same concept for floating point arithmetic, since points may come close to each other but not 
        // be the exact same, it only needs to be "close enough"
        // check if the start is still the same
        if(this.knockedBack){
            this.relocateStart(grid.getDirections());
        }else if((Math.abs(this.getX() - this.targetX) <= this.speed && Math.abs(this.getY() - this.targetY) <= this.speed)){
            // move exactly to the position
            if(this.getX() != this.targetX || this.getY() != this.targetY){
                // return since the this.move on the bottom overrides this otherwise
                this.move(this.targetX - this.getX(), this.targetY - this.getY());
                return;
            }
            this.relocateTarget(grid.getDirections());
        }
        int moveX = 0;
        int moveY = 0;
        if(this.getX() + this.speed < this.targetX){
            moveX = this.speed;
        }else if(this.getX() - this.speed > this.targetX){
            moveX = -this.speed;
        }else if(this.getY() + this.speed < this.targetY){
            moveY = this.speed;
        }else if(this.getY() - this.speed > this.targetY){
            moveY = -this.speed;
        }
        this.move(moveX, moveY);
        // make shooting decisions
        // idea: loop through all targets and construct a line2D to each of them. Do intersections with walls
        // if they don't intersect with any walls, mark that target as available. If multiple are available, pick
        // one with shortest distance to travel
        if(this.canShoot){
            Player target = this.findTarget(players, grid);
            if(target != null){
                // bots are too overpowered since they always hit accurately
                int xDeviation = (int)(Math.random() * this.accuracy);
                int yDeviation = (int)(Math.random() * this.accuracy);
                if(Math.random() < 0.5){
                    xDeviation *= -1;
                }
                if(Math.random() < 0.5){
                    yDeviation *= -1;
                }
                this.shoot(target.getX() + xDeviation, target.getY() + yDeviation, 0, 0);
            }
        }
    }
    // for user input
    public void makeDecision(Input input){
        if(input.getMouseClicked() && this.canShoot){
            this.shoot(input.getX(), input.getY(), this.getX() + input.getOffsetX() - Const.CENTER_X,
                       this.getY() + input.getOffsetY() - Const.CENTER_Y);
        }
        if (input.getKeyPress() == KeyEvent.VK_W){ 
            this.move(0, -this.speed);
        } else if (input.getKeyPress() == KeyEvent.VK_A){ 
            this.move(-this.speed, 0);
        } else if (input.getKeyPress() == KeyEvent.VK_S){ 
            this.move(0, this.speed);
        } else if (input.getKeyPress() == KeyEvent.VK_D){ 
            this.move(this.speed, 0);
        }
    }
    // set offsetX and offsetY to 0 if the target coordinates are not affected by offset
    public void shoot(int targetX, int targetY, int offsetX, int offsetY){
        // remove the offset. This is only done if aiming at mouse
        targetX += offsetX;
        targetY += offsetY;
        // note that the slower the bullet speed, the less freedom of the angles the bullet can move at since we're not
        // using doubles here
        int rise = targetY - this.getY() - this.getL() / 2;
        int run = targetX - this.getX() - this.getL() / 2;
        // limit the speed to 40. Otherwise, bullets can literally skip through walls
        double resize = (Math.abs(rise) + Math.abs(run)) / (double)Bullet.BULLET_SPEED;
        rise = (int)(rise / resize);
        run  = (int)(run / resize);
        // add bullet
        Bullet bullet = new Bullet(this.getX() + this.getL() / 2, this.getY() + this.getL() / 2, run, rise, Bullet.BULLET_SIZE, Const.bulletImage);
        this.gun.addBullet(bullet);
        // add reloading status effect
        Reload reload = new Reload(Math.max(1, this.reloadSpeed - this.reloadBoost));
        this.addStatusEffect(reload);
    }
    
    public void addStatusEffect(StatusEffects statusEffect){
        if(this.statusEffects[statusEffect.getId()-1] == null){
            this.statusEffects[statusEffect.getId()-1] = statusEffect;
        }
    }
    // status effects don't stack. Otherwise it would be ... not fun
    public void processStatusEffects(){
        for(int i=0; i<this.statusEffects.length; i++){
            if(this.statusEffects[i] != null){
                this.statusEffects[i].performOperation(this);
                this.statusEffects[i].countDown();
                if(this.statusEffects[i].getDuration() <= 0){
                    this.statusEffects[i] = null;
                }
            }
        }
    }
    
    public void update(Player[] players, Grid grid){
        this.scoreboard.update(this);
        this.gun.removeBullets(players, grid, this);
        this.gun.moveBullets();
    }
}