import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class Menu{
    // menu objects
    private Button introduction;
    private Button leftSlide;
    private Button rightSlide;
    private Button upSlide;
    private Button downSlide;
    
    private static BufferedImage[] characterImages;
    private int mapIndex;
    private int characterIndex;

    public static void loadImages(){
        characterImages = new BufferedImage[Player.CHARACTER_TYPES + 1];
        characterImages[0] = Const.paladinImage;
        characterImages[1] = Const.rogueImage;
        characterImages[2] = Const.knightImage;
        characterImages[3] = Const.randomCharacterImage;
    }
    
    Menu(){
        this.mapIndex = 0;
        // change constructors to alter look of menu
        this.introduction = new Button(Const.WIDTH / 2 - Const.BLOCK_SIZE, Const.HEIGHT * 5 / 6 - Const.BLOCK_SIZE, Const.BLOCK_SIZE * 2, Const.BLOCK_SIZE * 2, Color.BLUE, Const.startButtonImage);
        this.leftSlide = new Button(Const.WIDTH / 10 - Const.BLOCK_SIZE / 2, Const.HEIGHT / 2 - Const.BLOCK_SIZE / 2, Const.BLOCK_SIZE, Const.BLOCK_SIZE, Color.BLUE, Const.leftButtonImage);
        this.rightSlide = new Button(Const.WIDTH * 5 / 6 + Const.BLOCK_SIZE / 2, Const.HEIGHT / 2 - Const.BLOCK_SIZE / 2, Const.BLOCK_SIZE, Const.BLOCK_SIZE, Color.BLUE, Const.rightButtonImage);
        this.upSlide = new Button(Const.WIDTH / 2 - Const.BLOCK_SIZE / 2, Const.HEIGHT / 3 - Const.BLOCK_SIZE / 2, Const.BLOCK_SIZE, Const.BLOCK_SIZE, Color.BLUE, Const.upButtonImage);
        this.downSlide = new Button(Const.WIDTH / 2 - Const.BLOCK_SIZE / 2, Const.HEIGHT * 3 / 5 - Const.BLOCK_SIZE / 2, Const.BLOCK_SIZE, Const.BLOCK_SIZE, Color.BLUE, Const.downButtonImage);
    }
    
    public int getMapIndex(){
        return this.mapIndex;
    }

    public int getCharacterIndex(){
        return this.characterIndex;
    }
    
    public boolean update(Input input){
        // the one extra on the end is random map
        if(this.leftSlide.check(input)){
            this.mapIndex = (this.mapIndex + Grid.prebuiltMaps.length) % (Grid.prebuiltMaps.length + 1);
        }
        if(this.rightSlide.check(input)){
            this.mapIndex = (this.mapIndex + 1) % (Grid.prebuiltMaps.length + 1);
        }
        // the one extra on the end is random player choice
        if(this.upSlide.check(input)){
            this.characterIndex = (this.characterIndex + 1) % (Player.CHARACTER_TYPES + 1);
        }
        if(this.downSlide.check(input)){
            this.characterIndex = (this.characterIndex + Player.CHARACTER_TYPES) % (Player.CHARACTER_TYPES + 1);
        }
        return this.introduction.check(input);
    }
    
    public void draw(Graphics g, Input input){
        // styling
        g.drawImage(Grid.mapImages[this.mapIndex], Const.CENTER_X - Grid.LENGTH / 2 + Const.BLOCK_SIZE / 2, Const.CENTER_Y - Grid.LENGTH / 2 + Const.BLOCK_SIZE / 2, Grid.LENGTH, Grid.LENGTH, null);
        this.introduction.draw(g, 0, 0); // button currently has no offset
        this.leftSlide.draw(g, 0, 0);
        this.rightSlide.draw(g, 0, 0);
        this.upSlide.draw(g, 0, 0);
        this.downSlide.draw(g, 0, 0);
        g.drawImage(Menu.characterImages[this.characterIndex], Const.CENTER_X - (int)(Const.BLOCK_SIZE * 1.5), Const.CENTER_Y - Const.BLOCK_SIZE * 2, Const.BLOCK_SIZE * 4, Const.BLOCK_SIZE * 4, null);
        g.drawImage(Const.gameNameImage, 0, -Const.HEIGHT*1/4, Const.WIDTH, Const.HEIGHT*3/4, null);

    }
    // own button. JButton bad.
    public class Button extends Template{
        // background hover color
        private Color color;
        private boolean mouseHover;
        public Button(int x, int y, int length, int width, Color color, BufferedImage picture){
            super(x, y, length, width, picture); 
            this.color = color;
        }
        public boolean check(Input input){
            // check bounds to see if button clicked
            if(this.getX() <= input.getX() && input.getX() <= this.getX()+this.getL() &&
               this.getY() <= input.getY() && input.getY() <= this.getY() + this.getW()){
                this.mouseHover = true;
                if(input.getMouseClicked()){
                    return true;
                }
            }else{
                this.mouseHover = false;
            }
            return false;
        }
        public void draw(Graphics g, int offsetX, int offsetY){
            if(this.mouseHover){
                g.drawImage(this.getP(), this.getX(), this.getY(), this.getL(), this.getW(), this.color, null);
            }else{
                g.drawImage(this.getP(), this.getX(), this.getY(), this.getL(), this.getW(), null);
            }
        }
    }
}