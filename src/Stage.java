/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileReader;
import java.io.IOException;

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
                "\n"+"image files: "+this.imagelst;
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
    public static ArrayList<Stage> CreateStagelst(){
        ArrayList<Stage> Stagelst = new ArrayList<>();
        int Stagecnt = 0;
        String Correctword="";
        int wordcnt=0;
        BufferedReader br = null;        
        
        try {
            br = new BufferedReader (new FileReader("src/Stagelist.txt"));
            String fileLine = "";
            while ((fileLine = br.readLine()) != null) {
            String data = fileLine;
            String[] datasplit = data.split(" ");
            ArrayList IMG = new ArrayList<String>();
            for (int i=0;i<datasplit.length;i++){
                if (i==0) Correctword = datasplit[i];
                if (i==1) wordcnt = Integer.parseInt(datasplit[i]);
                if (i>=2) IMG.add("WordGuessImage/"+datasplit[i]);
                }
            Stagelst.add(new Stage(Correctword,wordcnt,IMG));
            Stagecnt++;
            }
            br.close();
        } catch (IOException ioe) {
            ioe.getMessage();
        }
        return Stagelst;
    }
    //method create Thai stagelist
    public static ArrayList<Stage> CreateThaiStagelst(){
        ArrayList<Stage> Stagelst = new ArrayList<>();
        int Stagecnt = 0;
        String Correctword="";
        int wordcnt=0;
        BufferedReader br = null;        
        
        try {
            br = new BufferedReader (new FileReader("src/StagelistThai.txt"));
            String fileLine = "";
            while ((fileLine = br.readLine()) != null) {
            String data = fileLine;
            String[] datasplit = data.split(" ");
            ArrayList IMG = new ArrayList<String>();
            for (int i=0;i<datasplit.length;i++){
                if (i==0) Correctword = datasplit[i];
                if (i==1) wordcnt = Integer.parseInt(datasplit[i]);
                if (i>=2) IMG.add("WordGuessImage/"+datasplit[i]);
                }
            Stagelst.add(new Stage(Correctword,wordcnt,IMG));
            Stagecnt++;
            }
            br.close();
        } catch (IOException ioe) {
            ioe.getMessage();
        }
        return Stagelst;
    }
    
    //method display stage list
    public static void Displaystagelst(ArrayList<Stage> a){
        for (Stage x : a){
            System.out.println(x.toString());
        }
    }
    
    //test method read txt file
    public static String readTextFile () {
          String fileContent = "";
          BufferedReader br = null;
        try {
            br = new BufferedReader (new FileReader("Stagelist.txt"));
            StringBuilder sb = new StringBuilder();
            String fileLine = "";
            while ((fileLine = br.readLine()) != null) {
                sb.append(fileLine);
                sb.append(System.lineSeparator());
            }
            fileContent = sb.toString();
            br.close();
        } catch (IOException ioe) {
            ioe.getMessage();
        }
        return fileContent;
}
}

