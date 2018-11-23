//Imported for using buttons and images
import java.awt.*;
public class EnemyDefeatedScreen{
    //The button to proceed witht he main algorythm
    Button btnContinue;
    //The labels to be displayed to show the user what he ahs gained
    Label lblXpGain[], lblGoldGain[];
    //The background when the user is in this state
    Image background;
    //show the necessary components for this class. 
    //The paramater is the number in the array of the items.
    void displayLabels(int x){
        lblXpGain[x].setVisible(true);
        lblGoldGain[x].setVisible(true);
        btnContinue.setVisible(true);
    }
    //The method to be used when btnContinue is pressed.
    //It hides all the components related to this class
    void btnContinue(){
        for(int i = 0;i<lblXpGain.length;i++){
            lblXpGain[i].setVisible(false);
            lblGoldGain[i].setVisible(false);
            btnContinue.setVisible(false);
        }
    }
}