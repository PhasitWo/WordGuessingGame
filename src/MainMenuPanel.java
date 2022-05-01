import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JFrame;

public class MainMenuPanel extends JPanel {
    final int screenWidth = 1280;
    final int screenHeight = 720;
    private JFrame window;
    private JComboBox comboBox;
    
    public MainMenuPanel(JFrame window) {
        this.window = window;
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true);
        this.setLayout(null);
        // combo box
        String[] items = {"Thai", "English"};
        this.comboBox = new JComboBox(items);
        this.comboBox.setBounds(590, 500, 100, 50);
        this.comboBox.setLightWeightPopupEnabled(false);
        this.comboBox.setBackground(new Color(255, 234, 234));
        this.add(comboBox);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // create Stage list
                ArrayList<Stage> S1 = Stage.CreateThaiStagelst();         
                ArrayList<Stage> S2 = Stage.CreateStagelst();
                GamePanel gamePanel = new GamePanel(S1, S2, MainMenuPanel.this, MainMenuPanel.this.window);
                MainMenuPanel.this.window.add(gamePanel);
                MainMenuPanel.this.setVisible(false); // click to hide main menu Panel
            }
        });
    }
    
    public String getLanguage() {
        return String.valueOf(this.comboBox.getSelectedItem());
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
