
import java.util.ArrayList;
import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        // create Stage list
        ArrayList<Stage> S1 = Stage.CreateStagelst();
        ArrayList<Stage> S2 = Stage.CreateThaiStagelst();        
        
        // init jframe
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Word Guessing Game");
        
        // add game panel to window
        MainMenuPanel mainMenuPanel = new MainMenuPanel();
        GamePanel gamePanel = new GamePanel(S2, mainMenuPanel);
        window.add(gamePanel);
        window.setGlassPane(mainMenuPanel); // set mainMenuPanel on first layer
        window.getGlassPane().setVisible(true);
        window.pack(); // fit window to gamePanel
        window.setLocationRelativeTo(null); // window will appear on the center
        window.setVisible(true);
    }
    
}
