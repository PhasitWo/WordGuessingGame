
import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        // create Stage list
        Stage[] S1 = Stage.CreateStagelst();
        Stage.Displaystagelst(S1);
        
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Word Guessing Game");
        
        // add game panel to window
        GamePanel gamePanel = new GamePanel(S1);
        MainMenuPanel mainPanel = new MainMenuPanel();
        gamePanel.add(mainPanel);
        window.add(gamePanel);
        window.pack(); // fit window to gamePanel
        window.setLocationRelativeTo(null); // window will appear on the center
        window.setVisible(true);
        
        gamePanel.startGameThread();
        
        
    }
    
}
