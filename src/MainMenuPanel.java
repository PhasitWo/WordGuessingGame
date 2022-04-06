
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

public class MainMenuPanel extends JPanel {
    final int screenWidth = 1280;
    final int screenHeight = 720;
    
    public MainMenuPanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
       
    }
}
