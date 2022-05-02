import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Graphics;

public class NoName{
    JFrame gameFrame;
    GamePanel gamePanel;
    MyKeyListener keyListener;
    MyMouseListener mouseListener;
    MyMouseMotionListener mouseMotionListener;

    Input input;
    Game game;
    Menu menu;
    boolean gameState;
    
    NoName() throws Exception{
        gameFrame = new JFrame("No Name");
        gamePanel = new GamePanel();
        keyListener = new MyKeyListener(); 
        mouseListener = new MyMouseListener(); 
        mouseMotionListener = new MyMouseMotionListener(); 

        Const.loadImages();
        Menu.loadImages();
        Grid.loadMaps();

        input = new Input();
        menu = new Menu();
        gameState = false;
    }
    
    public void setUp(){
        gameFrame.setSize(Const.WIDTH,Const.HEIGHT); 
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        gameFrame.setResizable(false); 
        gamePanel.addKeyListener(keyListener); 
        gamePanel.addMouseListener(mouseListener); 
        gamePanel.addMouseMotionListener(mouseMotionListener);         
        gameFrame.add(gamePanel);  
        gameFrame.setVisible(true);     
    }
    
    public void runGameLoop() throws Exception{
        while (true) { 
            gameFrame.repaint(); 
            try  {Thread.sleep(Const.FRAME_PERIOD);} catch(Exception e){} 
            
            if(this.gameState && game != null){
                int winner = game.update(input);
                if(winner != -1){
                    gameState = false;
                }
            }else if(game != null){
                // this serves as the ending screen as everything is frozen
                // when space pressed, flip screen back to menu
                if(input.getKeyPress() == KeyEvent.VK_SPACE){
                    game = null;
                }
            }else{
                this.gameState = this.menu.update(input);
                if(this.gameState){
                    this.game = new Game(this.menu.getMapIndex(), this.menu.getCharacterIndex());
                }
            }
            // input reset
            input.setMouseClicked(false);
        } 
    }
    
    public class MyKeyListener implements KeyListener{ 
        
        public void keyPressed(KeyEvent e){ 
            int key = e.getKeyCode(); 
            input.setKeyPress(key);
        } 
        public void keyReleased(KeyEvent e){
            // for smooth player movements
            int key = e.getKeyCode();
            if(input.getKeyPress() == key){
                input.setKeyPress(0);
            }
        }    
        public void keyTyped(KeyEvent e){ 
        }            
    }     
 
    public class MyMouseListener implements MouseListener{ 
        public void mouseClicked(MouseEvent e){  
            input.setMouseClicked(true);
        } 
        public void mousePressed(MouseEvent e){ 
        } 
        public void mouseReleased(MouseEvent e){ 
        } 
        public void mouseEntered(MouseEvent e){ 
        } 
        public void mouseExited(MouseEvent e){ 
        } 
    }     
 
    public class MyMouseMotionListener implements MouseMotionListener{ 
        public void mouseMoved(MouseEvent e){ 
            input.setX(e.getX());
            input.setY(e.getY());
        } 
        public void mouseDragged(MouseEvent e){ 
        }          
    }     
    //draw everything 
    public class GamePanel extends JPanel{ 
        GamePanel(){ 
            setFocusable(true); 
            requestFocusInWindow(); 
        } 
         
        @Override 
        public void paintComponent(Graphics g){  
            super.paintComponent(g);
            if(game != null){
                game.draw(g, input);
            }else{
                menu.draw(g, input);
            }
        }     
    }
}