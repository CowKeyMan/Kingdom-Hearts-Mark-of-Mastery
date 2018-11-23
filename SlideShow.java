//Imported for using butons and images
import java.awt.*;
public class SlideShow{
    //The buttons to be used in this class
    Button btnPrevious, btnNext, btnExit;
    //The images to be viewed as slideshows
    Image img[];
    //The caption to be displayed with each image
    String caption[];
    //The current image to be displayed(the array number)
    int imgCounter = 0;
    //The image which changes and which is to be displayed on the screen
    Image background;
    //Go back 1 image
    void btnPrevious(){
        imgCounter--;
        background = img[imgCounter];
        //If there are no images before it, this button is hidden
        if(imgCounter == 0){
            btnPrevious.setVisible(false);
        }
        btnNext.setVisible(true);
    }
    //Go forward 1 image
    void btnNext(){
        imgCounter++;
        background = img[imgCounter];
        //If there are no images after it, this button is hidden
        if(imgCounter == img.length-1){
            btnNext.setVisible(false);
        }
        btnPrevious.setVisible(true);
    }
    //Hide everything and reset the image counter
    void btnExit(){
        btnExit.setVisible(false);
        btnNext.setVisible(false);
        btnPrevious.setVisible(false);
        imgCounter = 0;
    }
}