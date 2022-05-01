
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.Collections;
import javax.swing.ImageIcon;
import javax.swing.JTextField;

public class GamePanel extends JPanel {
    final int screenWidth = 1280;
    final int screenHeight = 720;
    final static Color shareColor = new Color(255, 234, 234);
    final static Color shareTextColor = new Color(255, 249, 178);
    private MainMenuPanel mainMenu;
    // stage
    private ArrayList<Stage> stageList;
    private Stage currentStage;
    private int stageIndex;
    private String currentUserAnswer = "";
    // guess buttons array
    private GuessButton[] guessButtonsArr = new GuessButton[18];
    private ArrayList<GuessButton> lastPressButtonList = new ArrayList<>();
    // next and back button
    private NextStageButton nextButton;
    private BackToMainMenuButton backButton;
    // letter array
    private ArrayList<Character> allLetter = new ArrayList<>();
    // Answer Field
    private answerField ansField;
    // Crystal Owned
    private CrystalSys Crystalcnt; //Text display crystal left
    // stageIndex
    private StageIndexLabel stageIndexLabel;
    // syllableCount
    private SyllableCountLabel syllableCountLabel;
    // between stage image
    private JLabel correctImage;
    
    public GamePanel(ArrayList<Stage> stageList, MainMenuPanel mainMenuObject) {
        this.mainMenu = mainMenuObject;
        this.setLayout(null);
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.green);
        this.setDoubleBuffered(true);
        // stage
        Collections.shuffle(stageList); // shuffle
        this.stageList = stageList;
        this.stageIndex = 0;
        this.currentStage = stageList.get(stageIndex);
        // fill letter array
        String str = "ผปแอิืทมใฝงวสา่้เดกหฟๆไำพะัีรนยบลชขจตคึุถภๅฉฮ์ฒฬฦซศษ๋็ฌโฏฆฤฐญฯณ๊ธฑูฎ";
        for (int i = 0; i < str.length(); i++) {
            this.allLetter.add(str.charAt(i));
        }
        // buttons
        this.nextButton = new NextStageButton();
        this.backButton = new BackToMainMenuButton();
        this.placeComp(this.nextButton, 750, 660, 150, 50, true);
        this.placeComp(this.backButton, 520, 660, 150, 50, true);
        this.createGuessButtons();
        this.labelingGuessButtons();
        this.placeComp(new deleteButton(), 850, 350, 80, 50, true);
        this.placeComp(new HintButton(), 430, 350, 80, 50, true);
        // label --> Crystals
        Crystalcnt = new CrystalSys();
        this.placeComp(Crystalcnt, 1100, 290, 300, 150, true);
        this.placeComp(new JLabel(new ImageIcon("src/pictures/crystal.png")), 1060, 290, 54, 47, true);
        // label --> CrystalUsageLabel
        this.placeComp(new CrystalUsageLabel(), 50, 240, 300, 150, false);
        this.placeComp(new JLabel(new ImageIcon("src/pictures/crystal.png")), 250, 315, 54, 47, true);
        // label --> stageIndex
        this.stageIndexLabel = new StageIndexLabel();
        this.placeComp(this.stageIndexLabel, 50, 200, 300, 150, false);
        // label --> syllableCount
        this.syllableCountLabel = new SyllableCountLabel();
        this.placeComp(this.syllableCountLabel, screenWidth/2, 280, 120, 150, true);
        // answerfield
        ansField = new answerField();
        this.placeComp(ansField, screenWidth/2, 350, 330, 60, true);
        // between stage image
        this.correctImage = new JLabel(new ImageIcon("src/pictures/correctImage.png"));
        this.correctImage.setVisible(false);
        this.placeComp(this.correctImage, 640, 540, 500, 500, true);
    }
     @Override
    public void paintComponent(Graphics g) {
        // Graphic g is the object that display graphic on gamePanel. when repaint() is called, it will automatically pass Graphic g to this method.
        // then we can customize this Graphic g (customize what will display on screen)
        super.paintComponent(g); // paint default component
        // back ground image
        try {
            BufferedImage bg = ImageIO.read(getClass().getResourceAsStream("pictures/BackGroundImage.jpg"));
            g.drawImage(bg, 0, 0, screenWidth, screenHeight, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // paint game component
        ArrayList<String> paths = this.currentStage.getimagelst();
        this.drawImage(g, paths);
    }
    
    private void drawImage(Graphics g, ArrayList<String> imagePath) {
        ArrayList<BufferedImage> imageArr = new ArrayList<>();
        for (String path : imagePath) {
            try {
                imageArr.add(ImageIO.read(getClass().getResourceAsStream(path)));
            } catch (Exception ex) {
                try {
                    imageArr.add(ImageIO.read(getClass().getResourceAsStream("pictures/default_error.png")));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        int width = 0;
        int HEIGHT = 200;
        int INTERVAL = 10;
        int totalWidth = 0;
        int startPoint = 0;
        int imgCnt = imageArr.size();
        ArrayList<Integer> widthArr = new ArrayList<>();
        // find total width of all images (include interval)
        for (BufferedImage img : imageArr) {
            width = (int) ((double) img.getWidth() / img.getHeight() * HEIGHT); // convert image to have 200 height but the same ratio.
            widthArr.add(width);
            totalWidth += width;
        }
        totalWidth += INTERVAL*(imgCnt-1);
        startPoint = 640 - totalWidth/2;
        // display image
        int posX = startPoint;
        for (int i = 0; i < imgCnt; i++) {
            BufferedImage toDrawImg = imageArr.get(i);
            g.drawImage(toDrawImg, posX, 40, widthArr.get(i), HEIGHT, null);
            posX += widthArr.get(i) + INTERVAL;
        }
    }
    
    private void createGuessButtons() {
        GuessButton gb;
        int BUTTON_WIDTH = 70;
        int BUTTON_HEIGHT = 70;
        int INTERVAL = 10;
        int X_START_POINT = (screenWidth/2) - ((BUTTON_WIDTH*6) + (INTERVAL*5))/2;
        int Y_START_POINT = 390;
        int cnt = 0;
        for (int y = 0; y <= 2; y++) {
            for (int x = 0; x <= 5; x++) {
                gb = new GuessButton();
                guessButtonsArr[cnt++] = gb;
                this.placeComp(gb, X_START_POINT + ((INTERVAL + BUTTON_WIDTH) * x), Y_START_POINT + ((INTERVAL + BUTTON_HEIGHT) * y), BUTTON_WIDTH, BUTTON_HEIGHT, false);
            }
        }
    }
    
    private void setEnableAllGuessButtons(boolean bool) {
        boolean isInLastPressButtonList;
        if (bool) {
            // in case that there are buttons that have been pressed
            if (lastPressButtonList.size() != 0) {
                for (GuessButton gb : guessButtonsArr) {
                    isInLastPressButtonList = false;
                    // if this gb is in last press button, don't enable it yet
                    for (GuessButton lastPressed : lastPressButtonList) {
                        if (gb == lastPressed) {
                            isInLastPressButtonList = true;
                            break;
                        }
                    }
                    if (!isInLastPressButtonList)
                        gb.setEnabled(true);
                }
                // enable 'lastest' pressed button
                GuessButton lastestButton = lastPressButtonList.get(lastPressButtonList.size() - 1);
                lastestButton.setEnabled(true);
                lastPressButtonList.remove(lastestButton);
            // in case no buttons is pressed
            } else {
                for (GuessButton gb : guessButtonsArr) {
                    gb.setEnabled(true);
                }
            }
        } else {
            for (GuessButton gb : guessButtonsArr) {
                gb.setEnabled(false);
            }
        }
    }
    
    private void labelingGuessButtons() {
        String correctWord = this.currentStage.getCorrectword();
        ArrayList<Character> toLabeling = new ArrayList<>();
        String char_;
        // add correct character to toLabeling array
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
            char_ = Character.toString(toLabeling.get(j));
            guessButtonsArr[j].setText(char_);
            guessButtonsArr[j].setGuessLetter(char_);
        }
    }
    
    private void placeComp(Component component,int x, int y, int w, int h, boolean centerAlignment) {
        if (centerAlignment)
            component.setBounds(x-w/2, y-h/2, w, h);
        else
            component.setBounds(x, y, w, h);
        this.add(component);
    }
    
    private void betweenStage() {
        Crystalcnt.setForeground(GamePanel.shareTextColor);
        this.nextButton.setVisible(true);
        this.correctImage.setVisible(true);
        // hide all guessButtons
        for (GuessButton gb : guessButtonsArr) {
            gb.setVisible(false);
        }
    }
    
    private void changeStage() {
        // hide next and back button, setVisible all guessButtons
        this.nextButton.setVisible(false);
        this.correctImage.setVisible(false);
        for (GuessButton gb : guessButtonsArr) {
            gb.setVisible(true);
        }
        this.currentStage = stageList.get(++stageIndex); // change current stage and increase stageIndex by 1
        this.stageIndexLabel.update(); // update stageIndexLabel
        this.syllableCountLabel.update(); // update syllableCount
        this.labelingGuessButtons(); // label guess button with new stage infomation
        // reset currentUserAnswer and reset ansField
        this.currentUserAnswer = "";
        this.ansField.updateField();
        // clear lastPressButtonlist and enable all buttons
        this.lastPressButtonList.clear();
        this.setEnableAllGuessButtons(true);
        // draw new images
        this.repaint(); // this method will call paintcomponent()
        Crystalcnt.CrystalUpdate(); // Update Crystals remaining
    }
    
    private boolean checkAnswer() {
            if (this.currentUserAnswer.equals(this.currentStage.getCorrectword()))
                return true;
            else
                return false;
    }
    
        private class NextStageButton extends JButton {
            public NextStageButton() {
                super("ถัดไป");
                this.setBackground(GamePanel.shareColor);
                this.setFont(new Font("Tahoma", Font.PLAIN, 40));
                this.setFocusPainted(false); // remove border around text
                this.setVisible(false);
                this.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        GamePanel.this.changeStage();
                    }
                });
            }
    }
        
        private class BackToMainMenuButton extends JButton {
            public BackToMainMenuButton() {
                super("กลับ");
                this.setBackground(GamePanel.shareColor);
                this.setFont(new Font("Tahoma", Font.PLAIN, 40));
                this.setFocusPainted(false); // remove border around text
                this.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        GamePanel.this.mainMenu.setVisible(true);
                        // shuffle stagelst, change stage, reset crystal
                        Collections.shuffle(GamePanel.this.stageList);
                        GamePanel.this.stageIndex = -1;
                        GamePanel.this.changeStage();
                        GamePanel.this.Crystalcnt.resetCrystals();
                        GamePanel.this.Crystalcnt.setForeground(GamePanel.shareTextColor);
                        GamePanel.this.ansField.setForeground(Color.BLACK);
                    }
                });        
            }
        }
        
        private class GuessButton extends JButton {
            private String guessLetter;
            
            public GuessButton() {
                this.setFont(new Font("Tahoma", Font.PLAIN, 40));
                this.setBackground(GamePanel.shareColor);
                this.setFocusPainted(false); // remove border around text
                this.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        GamePanel.this.currentUserAnswer += GuessButton.this.guessLetter;
                        GamePanel.this.ansField.updateField();
                        GamePanel.this.lastPressButtonList.add(GuessButton.this);
                        GuessButton.this.setEnabled(false); // disable after getting pressed
                        // checkAnswer
                        if (GamePanel.this.currentUserAnswer.length() == GamePanel.this.currentStage.getCorrectword().length()) { // if length of currentUserAnswer matches correct word length
                            GamePanel.this.setEnableAllGuessButtons(false); // disable all buttons after the length matches
                            if (GamePanel.this.checkAnswer()) {
                                Crystalcnt.addCrystals(CrystalSys.getCrystalplus());
                                Crystalcnt.CrystalUpdate();
                                GamePanel.this.betweenStage();
                            } else {
                                ansField.setForeground(Color.red); // if answer is false then set text color to red
                            }
                        }
                    }
                });
            }
            
            public void setGuessLetter(String letter) {
                this.guessLetter = letter;
            }
            public String getGuessLetter(){
                return this.guessLetter;
            }
        }
        
        private class deleteButton extends JButton {
            public deleteButton() {
                this.setBackground(GamePanel.shareColor);
                this.setText("ลบ");
                this.setFont(new Font("Tahoma", Font.PLAIN, 20));
                this.setFocusPainted(false); // remove border around text
                this.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                           // if this statement is true that means all button have been disabled, so we need to enable it back after user press del button
                           if (GamePanel.this.currentUserAnswer.length() == GamePanel.this.currentStage.getCorrectword().length()) {
                               GamePanel.this.setEnableAllGuessButtons(true);
                               ansField.setForeground(Color.BLACK); // since the answer is false and text is red, turn it back to black
                           }
                           // if above statement is false that means not all buttons have been disabled. so we need to enable it back in order
                           else if (GamePanel.this.lastPressButtonList.size() != 0) {
                                int lastIndex = GamePanel.this.lastPressButtonList.size() - 1;
                                GuessButton lastPressedButton = GamePanel.this.lastPressButtonList.get(lastIndex);
                                lastPressedButton.setEnabled(true);
                                GamePanel.this.lastPressButtonList.remove(lastPressedButton); // remove lastPressedButton from list
                           }
                           // delete last letter and updateField()
                           int len = GamePanel.this.currentUserAnswer.length();
                           if (len != 0) {
                                GamePanel.this.currentUserAnswer = GamePanel.this.currentUserAnswer.substring(0, len-1);
                                ansField.updateField();
                           }
                    }
                });
            }
        }
        
        private class CrystalSys extends JLabel {
            private final int INITIALCRYSTAL = 500; 
            private static int Crystalplus = 300; // Add 300 crystals when answer correctly
            private int CrystalOwned = INITIALCRYSTAL;//inital 500 crystals when start a game
            
            public CrystalSys(){
                this.CrystalUpdate();
                this.setFont(new Font("Tahoma", Font.PLAIN, 35));
                this.setForeground(GamePanel.shareTextColor);
            }
            public void CrystalUpdate(){ //Update crystal remain
                this.setText(this.CrystalOwned + "");
            }
            public int Crystalremain(){
                return this.CrystalOwned;
            }
            public static int getCrystalplus(){
                return Crystalplus;
            }
            public void addCrystals(int a){
                this.CrystalOwned += a;
            }
            public void useCrystals(int a){
                this.CrystalOwned -= a;
            }
            
            public void resetCrystals() {
                this.CrystalOwned = INITIALCRYSTAL;
                this.CrystalUpdate();
            }
        }
        
        private class HintButton extends JButton {
            private int CrystalUsePerHint = 100; // 100 Crystals needed per 1 hint
            
            public HintButton() {
                this.setText("เฉลย");
                this.setFont(new Font("Tahoma", Font.PLAIN, 20));
                this.setBackground(GamePanel.shareColor);
                this.setFocusPainted(false); // remove border around text
                this.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                           // Enough Crystal - Use it and Add letter to current User Answer
                           if (Crystalcnt.Crystalremain() >= CrystalUsePerHint) { 
                               String correctletter = GamePanel.this.currentStage.getCorrectword();
                               int len = GamePanel.this.currentUserAnswer.length();
                               if (len == GamePanel.this.currentStage.getCorrectword().length()) { //Full ansField can't hint more
                                      Crystalcnt.setText("Crystals Remain: "+Crystalcnt.Crystalremain()+"\n"+"Please del first!");
                               }
                               else{ //ansField not full can hint more
                                      Crystalcnt.useCrystals(CrystalUsePerHint);
                                      Crystalcnt.CrystalUpdate();
                                      if (len != GamePanel.this.currentStage.getCorrectword().length() - 1){
                                            GamePanel.this.currentUserAnswer += Character.toString(correctletter.charAt(len));
                                            GamePanel.this.ansField.updateField();
                                            GuessButton Correctone = new GuessButton();
                                            for (GuessButton gb: guessButtonsArr){//get Guessbutton object with same letter and disable it
                                                if (Character.toString(correctletter.charAt(len)).equals(gb.getGuessLetter())){
                                                    Correctone = gb;
                                                    if (gb.isEnabled()){
                                                        gb.setEnabled(false);   // disable after getting pressed
                                                        break;
                                                    }
                                                }
                                            }
                                            GamePanel.this.lastPressButtonList.add(Correctone);
                                      }
                                      else{ // check answer too if hint to the full length
                                            GamePanel.this.currentUserAnswer += Character.toString(correctletter.charAt(len));
                                            GamePanel.this.ansField.updateField();
                                            GuessButton Correctone = new GuessButton();
                                            for (GuessButton gb: guessButtonsArr){
                                                if (Character.toString(correctletter.charAt(len)).equals(gb.getGuessLetter())){
                                                    Correctone = gb;
                                                    if (gb.isEnabled()){
                                                        gb.setEnabled(false);   // disable after getting pressed
                                                        break;
                                                    }
                                                }
                                            }
                                            GamePanel.this.lastPressButtonList.add(Correctone);
                                            
                                            if (GamePanel.this.checkAnswer()) {//Check answer after hint to full length
                                                    Crystalcnt.addCrystals(CrystalSys.getCrystalplus());
                                                    Crystalcnt.CrystalUpdate();
                                                    GamePanel.this.betweenStage();}
                                            else {
                                                    GamePanel.this.setEnableAllGuessButtons(false); // disable all buttons after the length matches
                                                    ansField.setForeground(Color.red); // if answer is false then set text color to red
                                                        }
                                                    }
                                                }
                                             }
                           // Not Enough Crystal Owned
                           else
                               Crystalcnt.setForeground(Color.red);
                            }
                    });
                }
            }
        
        private class answerField extends JTextField {
            public answerField() {
                this.setFont(new Font("Tahoma", Font.PLAIN, 40));
                this.updateField();
                this.setEditable(false);
                this.setBackground(GamePanel.shareColor);
            }
            
            public void updateField() {
                this.setText(GamePanel.this.currentUserAnswer);
            }
        }
        
        private class StageIndexLabel extends JLabel {
            public StageIndexLabel() {
                this.setFont(new Font("Tahoma", Font.PLAIN, 30));
                this.setText("Stage : " + (GamePanel.this.stageIndex + 1));
                this.setForeground(GamePanel.shareTextColor);
            }
            
            public void update() {
                this.setText("Stage : " + (GamePanel.this.stageIndex + 1));
            }
        }
        
        private class CrystalUsageLabel extends JLabel {
            public CrystalUsageLabel() {
                this.setFont(new Font("Tahoma", Font.PLAIN, 30));
                this.setText("เฉลย : -100");
                this.setForeground(GamePanel.shareTextColor);
            }
        }
        
        private class SyllableCountLabel extends JLabel {
            public SyllableCountLabel() {
                this.setFont(new Font("Tahoma", Font.PLAIN, 30));
                this.setText("" + GamePanel.this.currentStage.getwordcnt() + " พยางค์");
                this.setForeground(GamePanel.shareTextColor);
            }
            
            public void update() {
                this.setText("" + GamePanel.this.currentStage.getwordcnt() + " พยางค์");
            }
        }
        

    
    /*    public void startGameThread() {
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
        
    }*/
}
