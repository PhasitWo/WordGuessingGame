
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.Collections;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GamePanel extends JPanel {
    final int screenWidth = 1280;
    final int screenHeight = 720;
    final int FPS = 30;
    // stage
    private Stage[] stageList;
    private Stage currentStage;
    private int stageIndex;
    private String currentUserAnswer = "";
    // guess buttons array
    private GuessButton[] guessButtonsArr = new GuessButton[18];
    private ArrayList<GuessButton> lastPressButtonList = new ArrayList<>();
    // letter array
    private ArrayList<Character> allLetter = new ArrayList<>();
    // Answer Field
    private answerField ansField;
    // image arrayList
    private ArrayList<JLabel> imageList = new ArrayList<>();
    //Crystal Owned
    private CrystalSys Crystalcnt; //Text display crystal left
    
    public GamePanel(Stage[] stageList) {
        this.setLayout(null);
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
        // buttons
        this.placeComp(new NextStageButton(), screenWidth/2, 600, 250, 50, true);
        this.createGuessButtons();
        this.labelingGuessButtons();
        this.placeComp(new deleteButton(), 850, 290, 80, 50, true);
        this.placeComp(new HintButton(), 430, 290, 80, 50, true);
        //label --> Crystals
        Crystalcnt = new CrystalSys();
        this.placeComp(Crystalcnt, 1100, 400, 300, 150, true);
        // answerfield
        ansField = new answerField();
        this.placeComp(ansField, screenWidth/2, 290, 330, 60, true);

    }
     @Override
    public void paintComponent(Graphics g) {
        // Graphic g is the object that display graphic on gamePanel. when repaint() is called, it will automatically pass Graphic g to this method.
        // then we can customize this Graphic g (customize what will display on screen)
        super.paintComponent(g); // paint default component
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
        int BUTTON_WIDTH = 70;
        int BUTTON_HEIGHT = 70;
        int INTERVAL = 10;
        int X_START_POINT = (screenWidth/2) - ((BUTTON_WIDTH*6) + (INTERVAL*5))/2;
        int Y_START_POINT = 330;
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
    
    private void changeStage() {
        this.currentStage = stageList[++stageIndex]; // change current stage and increase stageIndex by 1
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
                super("next stage");
                this.setFont(new Font("Tahoma", Font.PLAIN, 40));
                this.setFocusPainted(false); // remove border around text
                this.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        GamePanel.this.changeStage();
                    }
                });
            }
    }
        
        private class GuessButton extends JButton {
            private String guessLetter;
            
            public GuessButton() {
                this.setFont(new Font("Tahoma", Font.PLAIN, 40));
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
                                GamePanel.this.changeStage();
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
                this.setText("del");
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
        
        private class CrystalSys extends JTextArea{
            private static int Crystalplus = 300; // Add 300 crystals when answer correctly
            private int CrystalOwned = 500;//inital 500 crystals when start a game
            
            public CrystalSys(){
                this.CrystalUpdate();
                this.setFont(new Font("Tahoma", Font.PLAIN, 28));
                this.setForeground(Color.blue);
            }
            public void CrystalUpdate(){ //Update crystal remain
                this.setText("Crystals Remain: "+this.CrystalOwned);
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
        }
        
        private class HintButton extends JButton {
            private int CrystalUsePerHint = 100; // 100 Crystals need per 1 hint
            
            public HintButton() {
                this.setText("Hint");
                this.setFont(new Font("Tahoma", Font.PLAIN, 20));
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
                                                    GamePanel.this.changeStage();}
                                            else {
                                                    GamePanel.this.setEnableAllGuessButtons(false); // disable all buttons after the length matches
                                                    ansField.setForeground(Color.red); // if answer is false then set text color to red
                                                        }
                                                    }
                                                }
                                             }
                           // Not Enough Crystal Owned
                           else
                               Crystalcnt.setText("Crystals Remain: "+Crystalcnt.Crystalremain()+"\n"+"Not Enough Crystal!");
                            }
                    });
                }
            }
        
        private class answerField extends JTextField {
            public answerField() {
                this.setFont(new Font("Tahoma", Font.PLAIN, 40));
                this.updateField();
                this.setEditable(false);
            }
            
            public void updateField() {
                this.setText(GamePanel.this.currentUserAnswer);
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
