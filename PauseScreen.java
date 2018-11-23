//Imported for using buttons and images
import java.awt.*;
public class PauseScreen{
    //The point of this button is to stop items from moving 
    //in the main program
    Button btnContinue;
    //The background image
    Image background;
    //Setting the btnContinue visible
    void pause(){
        btnContinue.setVisible(true);
    }
    //Hiding the btnContinue
    void btnContinue(){
        btnContinue.setVisible(false);
    }
}