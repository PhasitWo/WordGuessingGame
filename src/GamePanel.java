
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.Arrays;
import java.util.Collections;

public class GamePanel extends JPanel implements Runnable {
    final int screenWidth = 1280;
    final int screenHeight = 720;
    final int FPS = 30;
    private Thread gameThread; // so gameThread will stay until GamePanel instance is disposed
    private GridBagConstraints gbc = new GridBagConstraints(); // for placing component to grid
    // stage
    private Stage[] stageList;
    private Stage currentStage;
    private int stageIndex;
    // guess buttons array
    private GuessButton[] guessButtonsArr = new GuessButton[18];
    // letter array
    private ArrayList<Character> allLetter = new ArrayList<>();
    
    public GamePanel(Stage[] stageList) {
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.green);
        this.setDoubleBuffered(true);
        // stage
        this.stageList = stageList;
        this.stageIndex = 0;
        this.currentStage = stageList[stageIndex];
        // fill letter array
        String str = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < str.length(); i++) {
            this.allLetter.add(str.charAt(i));
        }
        // create all guess buttons
        this.createGuessButtons();
        this.labelingGuessButtons();
        // place stage changing button
        this.placeComp(Box.createGlue(), 0, 0, 1, 1);
        this.placeComp(Box.createGlue(), 0, 1, 1, 1);
        this.placeComp(Box.createGlue(), 0, 2, 1, 1);
        this.placeComp(Box.createGlue(), 0, 3, 1, 1);
        this.placeComp(new NextStageButton(), 1, 4, 4, 1);
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
    
    private void update() {
        
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
    
    private void drawImage(Graphics2D g, ArrayList<String> imagePath) {
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
    
    private void createGuessButtons() {
        GuessButton gb;
        gbc.insets = new Insets(5, 5, 5, 5); // gap between button
        int cnt = 0;
        for (int col = 0; col <= 5; col++) {
            for (int row = 5; row <= 7; row++) {
                gb = new GuessButton();
                guessButtonsArr[cnt++] = gb;
                this.placeComp(gb, col, row, 1, 1);
            }
        }
    }
    
    private void labelingGuessButtons() {
        String correctWord = this.currentStage.getCorrectword();
        ArrayList<Character> toLabeling = new ArrayList<>();
        for (int i = 0; i < correctWord.length(); i++) {
            toLabeling.add(correctWord.charAt(i));
        }
        // add other character aside from correct character
        Collections.shuffle(allLetter); // shuffle
        int k = 0;
        while (toLabeling.size() != 18) {
            toLabeling.add(allLetter.get(k++));
        }
        Collections.shuffle(toLabeling); // shuffle before assign to buttons
        // assign toLabeling to guessButtons
        for (int j = 0; j <= 17; j++) {
            guessButtonsArr[j].setText(Character.toString(toLabeling.get(j)));
        }
    }
    
    private void placeComp(Component component,int x, int y, int w, int h) { // for placing component in grid
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        this.add(component, gbc);
    }
    
        private class NextStageButton extends JButton {
            public NextStageButton() {
                super("next stage");
                this.setFont(new Font("Tahoma", Font.PLAIN, 40));
                this.setFocusPainted(false); // remove border around text
                this.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        GamePanel.this.currentStage = stageList[++stageIndex];
                        GamePanel.this.labelingGuessButtons();
                    }
                });
            }
    }
        
        private class GuessButton extends JButton {
            public GuessButton() {
                super("---");
                this.setFont(new Font("Tahoma", Font.PLAIN, 40));
                this.setFocusPainted(false); // remove border around text
                /* this.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        GamePanel.this.currentStage = stageList[++stageIndex];
                        NextStageButton.this.setText("hey");
                    }
                }); */
            }
        }
}
