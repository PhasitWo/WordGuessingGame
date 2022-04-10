/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.ArrayList;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class Stage {
    private String Correctword;
    private int wordcnt;
    private ArrayList imagelst = new ArrayList<String>();
    private String Guessedword = "";

    public Stage(String a,int b,ArrayList c){
        this.Correctword = a;
        this.wordcnt = b;
        this.imagelst = c;
    }
    
    //method set
    public void setCorrectword(String a){
        this.Correctword = a;
    }
    public void setwordcnt(int a){
        this.wordcnt = a;
    }  
    public void setimagelst(ArrayList a){
        this.imagelst = a;
    }
    public void setGuessedword(String a){
        this.Guessedword = a;
    }
    
    //method get
    public String getCorrectword(){
        return this.Correctword;
    }
    public int getwordcnt(){
        return this.wordcnt;
    }
    public ArrayList getimagelst(){
        return this.imagelst;
    }
    public String getGuessedword(){
        return this.Guessedword;
    }
    
    //method toString
    public String toString(){
        return "Correct word: "+this.Correctword+
                "\n"+"word count: "+this.wordcnt+
                "\n"+"image files: "+this.imagelst.toString();
    }
    
    //method check word
    public boolean RightorWrong(){
        return this.Correctword.equals(this.Guessedword);
    }
    
    //method check entireword is guess or not
    public boolean EntireWordOrNot(){
        return this.Correctword.length()==this.Guessedword.length();
    }
    
    //method create stagelst from text file
    public static Stage[] CreateStagelst(){
        Stage[] Stagelst = new Stage[5];
        int Stagecnt = 0;
        String Correctword="";int wordcnt=0;ArrayList imagelst = new ArrayList<String>();
        
        try {
        File myfile = new File("Stagelist.txt");
        Scanner S = new Scanner(myfile);
        while (S.hasNextLine()) {
        String data = S.nextLine();
        String[] datasplit = data.split(" ");
        for (int i=0;i<datasplit.length;i++){
            if (i==0) Correctword = datasplit[i];
            if (i==1) wordcnt = Integer.parseInt(datasplit[i]);
            else imagelst.add(datasplit[i]);
            }
        Stagelst[Stagecnt] = new Stage(Correctword,wordcnt,imagelst);
        Stagecnt++;
        imagelst.clear();
        }
        S.close();
        } catch (FileNotFoundException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
        }
        return Stagelst;
    }
    
    //method display stage list
    public static void Displaystagelst(Stage[] a){
        for (Stage x: a){
            System.out.println(x.toString());
        }
    }
}

