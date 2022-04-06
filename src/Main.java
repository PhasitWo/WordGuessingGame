
import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Word Guessing Game");
        
        // add game panel to window
        GamePanel gamePanel = new GamePanel();
        MainMenuPanel mainPanel = new MainMenuPanel();
        window.add(gamePanel);
        window.add(mainPanel);
        window.pack(); // fit window to gamePanel
        window.setLocationRelativeTo(null); // window will appear on the center
        window.setVisible(true);

        gamePanel.startGameThread();
    }
    
}
