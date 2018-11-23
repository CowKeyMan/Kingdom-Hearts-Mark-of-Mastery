//Imported for using buttons and images
import java.awt.*;
public class DefeatScreen{
    //The buttons to be used in this class
    Button btnContinue, btnLoad;
    //The background when the user is in this state
    Image background;
    //Show the buttons
    void displayButtons(){
        btnContinue.setVisible(true);
        btnLoad.setVisible(true);
    }
    //When either button is pressed they both get hidden.
    //In the main program more actions will be made.
    void hideButtons(){
        btnContinue.setVisible(false);
        btnLoad.setVisible(false);
    }
}