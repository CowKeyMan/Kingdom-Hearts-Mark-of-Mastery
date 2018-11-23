//Imported for using images
import java.awt.*;
public class Conversation{
    String sentence[];
    Image characterImage[];
    Label lblSentence[];
    int sentenceByCharacter;
    int sentenceRolled[];
    int labelNumber = 0;
    boolean sentenceFinished;
    Button btnContinue;
    long effectTimer = System.currentTimeMillis();
    
    void setEverySentenceToRolled(){
        sentenceFinished = false;
        for(int i = 0;i<sentenceRolled.length;i++){
            sentenceRolled[i] = 0;
        }
    }
    
    void rollSentence(){
        if(sentenceFinished == false){
            String sent = "";
            for(int i = 0;i<sentenceRolled[labelNumber];i++){
                sent += sentence[labelNumber].charAt(i);
            }
            sentenceRolled[labelNumber]++;
            lblSentence[labelNumber].setText(sent);
            effectTimer = System.currentTimeMillis();
        }
        if(sentenceRolled[labelNumber] == sentence[labelNumber].length()+1){
            sentenceFinished = true;
        }
    }
    
    void rollFullSentence(){
        sentenceFinished = true;
        lblSentence[labelNumber].setText(sentence[labelNumber]);
    }
    
    void setNewLabelVisible(){
        lblSentence[labelNumber].setVisible(false);
        lblSentence[labelNumber+1].setVisible(true);
        labelNumber++;
        sentenceFinished = false;
    }
    
    void btnContinue(){
        if(sentenceFinished == false){
            rollFullSentence();
        }else{
            setNewLabelVisible();
        }
    }
}