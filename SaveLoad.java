//Imported for using images and backgrounds
import java.awt.*;
public class SaveLoad{
    //The buttons to be used in this class
    Button btnSave, btnLoad, btnExit;
    //The background image to be used when running this class' part of the main algorythm
    Image background;
    //Show the buttons
    void makeButtonsVisible(){
        btnSave.setVisible(true);
        btnLoad.setVisible(true);
        btnExit.setVisible(true);
    }
    //Hide the buttons
    void btnExit(){
        btnSave.setVisible(false);
        btnLoad.setVisible(false);
        btnExit.setVisible(false);
    }
}