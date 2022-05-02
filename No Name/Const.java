import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public final class Const{ 
    // coordinates
    public static final int WIDTH = 600; 
    public static final int HEIGHT = 600; 
    public static final int FRAME_PERIOD = 10; 
    public static final int BLOCK_SIZE = 40;
    public static final int CENTER_X = WIDTH / 2 - BLOCK_SIZE / 2;
    public static final int CENTER_Y = HEIGHT / 2 - BLOCK_SIZE / 2;
    public static final int MOUSE_OFFSET = 40;
    // images
    public static BufferedImage armorBuffImage;
    public static BufferedImage bulletImage;
    public static BufferedImage downButtonImage;
    public static BufferedImage knightImage;
    public static BufferedImage leftButtonImage;
    public static BufferedImage paladinImage;
    public static BufferedImage reloadBuffImage;
    public static BufferedImage rightButtonImage;
    public static BufferedImage rogueImage;
    public static BufferedImage speedBuffImage;
    public static BufferedImage upButtonImage;
    public static BufferedImage floorImage;
    public static BufferedImage floor2Image;
    public static BufferedImage wallImage;
    public static BufferedImage randomCharacterImage;
    public static BufferedImage startButtonImage;
    public static BufferedImage victoryImage;
    public static BufferedImage defeatImage;
    public static BufferedImage chestImage;
    public static BufferedImage gameNameImage;

    private Const(){ 
    } 

    // load one copy of all the images
    public static void loadImages(){
        try{
            armorBuffImage = ImageIO.read(new File("images/ArmorBuff.png"));
            bulletImage = ImageIO.read(new File("images/Bullet.png"));
            downButtonImage = ImageIO.read(new File("images/DownButton.png"));
            knightImage = ImageIO.read(new File("images/Knight.png"));
            leftButtonImage = ImageIO.read(new File("images/LeftButton.png"));
            paladinImage = ImageIO.read(new File("images/Paladin.png"));
            reloadBuffImage = ImageIO.read(new File("images/ReloadBuff.png"));
            rightButtonImage = ImageIO.read(new File("images/RightButton.png"));
            rogueImage = ImageIO.read(new File("images/Rogue.png"));
            speedBuffImage = ImageIO.read(new File("images/SpeedBuff.png"));
            upButtonImage = ImageIO.read(new File("images/UpButton.png"));
            floorImage = ImageIO.read(new File("images/Floor.png"));
            floor2Image = ImageIO.read(new File("images/Floor2.png"));
            wallImage = ImageIO.read(new File("images/Wall.png"));
            randomCharacterImage = ImageIO.read(new File("images/RandomCharacter.png"));
            startButtonImage = ImageIO.read(new File("images/StartButton.png"));
            victoryImage = ImageIO.read(new File("images/Victory.png"));
            defeatImage = ImageIO.read(new File("images/Defeat.png"));
            chestImage = ImageIO.read(new File("images/Chest.png"));
            gameNameImage = ImageIO.read(new File("images/GameName.png"));
        } catch(IOException ex){}
    }
}