
import java.util.ArrayList;
import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        // init jframe
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Word Guessing Game");
        
        // add game panel to window
        MainMenuPanel mainMenuPanel = new MainMenuPanel(window);
        window.add(mainMenuPanel);
        window.pack(); // fit window to gamePanel
        window.setLocationRelativeTo(null); // window will appear on the center
        window.setVisible(true);
    }
    
}
