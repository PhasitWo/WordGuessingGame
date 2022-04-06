import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;

public class MainMenuPanel extends JPanel {
    final int screenWidth = 1280;
    final int screenHeight = 720;
    
    public MainMenuPanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true);
        this.setLayout(null); // set layout manager to null in order to set button size and pos
        this.add(new StartButton());
    }
    
    class StartButton extends JButton {
        public StartButton() {
            super("Start");
            this.setFont(new Font("Arial", Font.PLAIN, 40));
            this.setFocusPainted(false); // remove border around text
            this.setBounds(540, 200, 200, 200); // set pos and size
            this.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MainMenuPanel.this.setVisible(false); // click to hide main menu Panel
                }
            });
        }
    }
}
