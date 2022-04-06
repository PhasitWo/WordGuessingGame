
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    final int screenWidth = 1280;
    final int screenHeight = 720;
    final int FPS = 30;
    private Thread gameThread; // so gameThread will stay until GamePanel instance is disposed
    
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
       
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start(); // this will execute overridden run method
    }
    
    @Override // game loop
    public void run() {
        double drawInterval = 1000000000/FPS; // Interval between frames in nanosecond
        double delta = 0; // delta indicates how many frames needed to paint
        long lastTime = System.nanoTime();
        long currentTime;
        
        while (gameThread != null) { 
            // fix game loop to run 30 FPS
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                // when there are more than 1 frame to paint (reach draw interval)
                update(); // update information
                repaint(); // draw the new information, repaint() method will call paintComponent()
                delta--; // decrease 1 delta as it has painted 1 frame

            }
        }
    }
    
    public void update() {
        
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // paint default component
        Graphics2D g2 = (Graphics2D) g; // downcast to Graphics2D and add our own stuff
        // paint game component
        
        
    }
}
