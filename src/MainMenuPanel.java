import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MainMenuPanel extends JPanel {
    final int screenWidth = 1280;
    final int screenHeight = 720;
    
    public MainMenuPanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true);
        this.setLayout(null); // set layout manager to null in order to set button size and pos
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MainMenuPanel.this.setVisible(false); // click to hide main menu Panel
            }
        });
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // paint default component
        // background image
        try {
            BufferedImage bg = ImageIO.read(getClass().getResourceAsStream("pictures/MainMenuImage.jpeg"));
            g.drawImage(bg, 0, 0, screenWidth, screenHeight, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
