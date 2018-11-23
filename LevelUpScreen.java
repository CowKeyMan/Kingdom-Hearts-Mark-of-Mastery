//Imported for using buttons and images
import java.awt.*;
public class LevelUpScreen{
    //These are the labels which will be visible in order to tell the player what he has obtained
    Label lblHPIncrease[], lblSpeedIncrease[], lblAbilityUnlock[];
    //The button so the user confirms his gains
    Button btnContinue;
    //The bacground for the situation
    Image background;
    //show the necessary components for this class. 
    //The paramater is the number in the array of the items.
    void showLabels(int i){
        lblHPIncrease[i].setVisible(true);
        lblSpeedIncrease[i].setVisible(true);
        lblAbilityUnlock[i].setVisible(true);
        btnContinue.setVisible(true);
    }
    //The method to be used when btnContinue is pressed.
    //It hides all the components related to this class
    void btnContinue(){
        for(int i = 0;i<lblHPIncrease.length;i++){
            lblHPIncrease[i].setVisible(false);
            lblSpeedIncrease[i].setVisible(false);
            lblAbilityUnlock[i].setVisible(false);
            btnContinue.setVisible(false);
        }
    }
}