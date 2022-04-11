
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    final int screenWidth = 1280;
    final int screenHeight = 720;
    final int FPS = 30;
    private Thread gameThread; // so gameThread will stay until GamePanel instance is disposed
    // stage
    private Stage[] stageList;
    private Stage currentStage;
    private int stageIndex;
    
    public GamePanel(Stage[] stageList) {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.green);
        this.setDoubleBuffered(true);
        // stage
        this.stageList = stageList;
        this.stageIndex = 0;
        this.currentStage = stageList[stageIndex];
        this.add(new NextStageButton());
       
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
        // Graphic g is the object that display graphic on gamePanel. when repaint() is called, it will automatically pass Graphic g to this method.
        // then we can customize this Graphic g (customize what will display on screen)
        super.paintComponent(g); // paint default component
        Graphics2D g2 = (Graphics2D) g; // downcast to Graphics2D and add our own stuff
        // paint game component
        ArrayList<String> paths = this.currentStage.getimagelst();
        this.drawImage(g2, paths);
        
    }
    
    public void drawImage(Graphics2D g, ArrayList<String> imagePath) {
        BufferedImage bi = null;
        ArrayList<BufferedImage> imageArr = new ArrayList<>();
        for (String path : imagePath) {
            try {
                imageArr.add(ImageIO.read(getClass().getResourceAsStream(path)));
            } catch (Exception ex) {
                try {
                    imageArr.add(ImageIO.read(getClass().getResourceAsStream("pictures/default_error.png")));
                } catch (Exception e) {
                    
                }
            }
        }
        int WIDTH = 200;
        int HEIGHT = 200;
        int INTERVAL = 10;
        int imgCnt = imageArr.size();
        // find total width of all images (include interval)
        int totalWidth = (WIDTH*imgCnt) + INTERVAL *(imgCnt - 1);
        int startPoint = 640 - totalWidth/2;
        // display image
        for (int i = 0; i < imgCnt; i++) {
            g.drawImage(imageArr.get(i), startPoint + (i*(WIDTH+INTERVAL)), 40, WIDTH, HEIGHT, null);
        }
    }
    
        class NextStageButton extends JButton {
            public NextStageButton() {
                super("next stage");
                this.setFont(new Font("Tahoma", Font.PLAIN, 40));
                this.setFocusPainted(false); // remove border around text
                this.setBounds(100, 500, 200, 200); // set pos and size
                this.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        GamePanel.this.currentStage = stageList[++stageIndex];
                        NextStageButton.this.setText("hey");
                    }
                });
            }
    }
}
