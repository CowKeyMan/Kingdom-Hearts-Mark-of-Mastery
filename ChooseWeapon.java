//Imported for using Buttons, labels and Images
import java.awt.*;
public class ChooseWeapon{
    //state: 1 = fight/flight, 2 = choose melee weapon, 3 = ranged weapon, 4 = ready?
    int state = 1;
    //The question to be asked to the user
    //Example: "Which melee weapon would you like to pick?"
    String question;
    //The buttons used in this part
    Button btnFight,btnFlight, btnContinue, btnExit, btnBack;
    //The buttons to pick each individual item
    Button btnPick[];
    //The boolean which shows if the item is picked or not
    boolean picked[];
    //The boolean which determines whether the enemy's image can be visible or not
    boolean enemyImageVisible = false;
    //The timer for how long the error message saying 
    //that the user may not continue as he has not picked an item yet
    long noItemPickedTimer;
    //The image to show the enemy the player will be facing
    Image enemyImage;
    //The background image to be used
    Image background;
    
    //Set a selected group of items as not picked
    void setPickedFalse(int start, int end){
        for(int i = start;i<end;i++){
            picked[i] = false;
            btnPick[i].setEnabled(true);
        }
    }
    //The button to procee with the class
    void btnContinue(boolean owned[], boolean melee[], boolean ranged[], boolean booleanArmor[], Label name[], Label mindmg[], Label maxdmg[], Label armor[], int meleeStart, int noOfMeleeItems, int rangedStart, int noOfRangedItems, int weaponChoice){
        boolean allow = false;
        if(state == 2){
            //Check if the player has picked a melee item
            allow = false;
            for(int i = meleeStart;i<meleeStart+noOfMeleeItems;i++){
                if(picked[i] == true){
                    allow = true;
                }
            }
            if(allow == true){
                //Check if the user owns any ranged item
                allow = false;
                for(int i = rangedStart;i<rangedStart+noOfRangedItems;i++){
                    if(owned[i] == true){
                        allow = true;
                    }
                }
                if(allow == true){
                    //If he does own a ranged item, show them and set state to 3
                    state = 3;
                    hideLabels(name,mindmg,maxdmg,armor);
                    displayRanged(owned, ranged, name, mindmg, maxdmg, armor);
                    btnBack.setVisible(true);
                }else{
                    //If he does not own a ranged item, instantly skip to the ready or not phase(state 4)
                    state = 4;
                    hideLabels(name,mindmg,maxdmg,armor);
                    readyOrNot(name, mindmg, maxdmg, armor, booleanArmor, owned);
                }
                btnBack.setVisible(true);
            }else{
                //If the user presses continue without having picked an item yet, 
                //he is given an error message whose timer is started below.
                noItemPickedTimer = System.currentTimeMillis();
            }
        }else if(state == 3){
            //Check if the player has picked a melee item
            allow = false;
            for(int i = rangedStart;i<rangedStart+noOfRangedItems;i++){
                if(picked[i] == true){
                    allow = true;
                }
            }
            if(allow == true){
                //Go to the ready or not phase(state 4)
                state = 4;
                hideLabels(name,mindmg,maxdmg,armor);
                readyOrNot(name, mindmg, maxdmg, armor, booleanArmor, owned);
            }else{
                //If the user presses continue without having picked an item yet, 
                //he is given an error message whose timer is started below.
                noItemPickedTimer = System.currentTimeMillis();
            }
            btnBack.setVisible(true);
        }else if(state == 4){
            //The user accepts his decisions and goes to battle with the items he picked.
            btnExit(name,mindmg,maxdmg,armor);
            state = 1;
        }
    }
    //The button to go back and arrange any misclicks the user might have want to redo
    void btnBack(boolean owned[], boolean melee[], boolean ranged[], Label name[], Label mindmg[], Label maxdmg[], Label armor[], int rangedStart, int noOfRangedItems){
        boolean allow = false;
        if(state == 3){
            //hide the current ranged labels and buttons and display the melee item's ones
            hideLabels(name, mindmg, maxdmg, armor);
            displayMelee(owned,melee,name, mindmg, maxdmg, armor);
            state = 2;
            btnBack.setVisible(false);
        }else if(state == 4){
            //Check if there are any ranged items
            allow = false;
            for(int i = rangedStart;i<rangedStart+noOfRangedItems;i++){
                if(owned[i] == true){
                    allow = true;
                }
            }
            if(allow == true){
                //If the player owns ranged items, he is shown them once again to change his decisions
                state = 3;
                hideLabels(name,mindmg,maxdmg,armor);
                displayRanged(owned, ranged, name, mindmg, maxdmg, armor);
            }else{
                //If he doesn't own any ranged items, he is instead transferred to 
                //change his decisions about the melee items
                state = 2;
                hideLabels(name,mindmg,maxdmg,armor);
                displayMelee(owned, melee, name, mindmg, maxdmg, armor);
                btnBack.setVisible(false);
            }
        }
    }
    //Hide every iamge, label and btnPick[]
    void hideLabels(Label name[], Label mindmg[], Label maxdmg[], Label armor[]){
        enemyImageVisible = false;
        for(int i = 0; i<name.length;i++){
            name[i].setVisible(false);
            mindmg[i].setVisible(false);
            maxdmg[i].setVisible(false);
            armor[i].setVisible(false);
            try{
                btnPick[i].setVisible(false);
            }catch(Exception e){
            }
        }
    }
    //Show the 2 buttons "fight" and "flight" and the enemy name and Image
    void fightOrFlight(String enemyName){
        question = "A " + enemyName + " has appeared!";
        btnFight.setVisible(true);
        btnFlight.setVisible(true);
        btnExit.setVisible(false);
        enemyImageVisible = true;
    }
    //Hide everything
    void btnFlight(){
        btnFight.setVisible(false);
        btnFlight.setVisible(false);
        btnExit.setVisible(false);
        enemyImageVisible = false;
    }
    //Hide all the previous items, increase the state and display the owned melee items for the user to pick
    void btnFight(boolean owned[], boolean melee[], Label name[], Label mindmg[], Label maxdmg[], Label armor[]){
        btnExit.setVisible(true);
        btnFight.setVisible(false);
        btnFlight.setVisible(false);
        btnBack.setVisible(false);
        btnContinue.setVisible(true);
        state = 2;
        displayMelee(owned, melee, name, mindmg, maxdmg, armor);
        enemyImageVisible = false;
    }
    //Display the owned melee items for the user to pick
    void displayMelee(boolean owned[], boolean melee[], Label name[], Label mindmg[], Label maxdmg[], Label armor[]){
        question = "Choose your melee weapon";
        for(int i = 0;i<name.length;i++){
            if(owned[i] == true && melee[i] == true){
                name[i].setVisible(true);
                mindmg[i].setVisible(true);
                maxdmg[i].setVisible(true);
                armor[i].setVisible(true);
                btnPick[i].setVisible(true);
                if(picked[i] == true){
                    btnPick[i].setEnabled(false);
                }
            }
        }
    }
    //Display the owned ranged items for the user to pick
    void displayRanged(boolean owned[], boolean ranged[], Label name[], Label mindmg[], Label maxdmg[], Label armor[]){
        question = "Choose your ranged weapon";
        for(int i = 0;i<name.length;i++){
            if(owned[i] == true && ranged[i] == true){
                name[i].setVisible(true);
                mindmg[i].setVisible(true);
                maxdmg[i].setVisible(true);
                armor[i].setVisible(true);
                btnPick[i].setVisible(true);
                if(picked[i] == true){
                    btnPick[i].setEnabled(false);
                }
            }
        }
    }
    //Shows the labels of the picked and armor items which are also owned
    void readyOrNot(Label name[], Label mindmg[], Label maxdmg[], Label armor[], boolean booleanArmor[], boolean owned[]){
        question = "These are your items";
        for(int i = 0;i<picked.length;i++){
            if(picked[i] == true){
                name[i].setVisible(true);
                mindmg[i].setVisible(true);
                maxdmg[i].setVisible(true);
                armor[i].setVisible(true);
            }
        }
        for(int i = 0; i<name.length;i++){
            if(owned[i] == true && booleanArmor[i] == true){
                name[i].setVisible(true);
                mindmg[i].setVisible(true);
                maxdmg[i].setVisible(true);
                armor[i].setVisible(true);
            }
        }
        btnContinue.setVisible(true);
        btnBack.setVisible(true);
        btnExit.setVisible(true);
    }
    //Set everything invisible and reset picked items to false and state to 1
    void btnExit(Label name[], Label mindmg[], Label maxdmg[], Label armor[]){
        state = 1;
        setPickedFalse(0, picked.length);
        hideLabels(name,mindmg,maxdmg,armor);
        btnFight.setVisible(false);
        btnFlight.setVisible(false);
        btnContinue.setVisible(false);
        btnExit.setVisible(false);
        btnBack.setVisible(false);
        for(int i = 0;i<btnPick.length;i++){
            btnPick[i].setVisible(false);
        }
    }
    //Pick one item from a selected few in the array while setting it's button unpressable
    //and making the others from it's group pressable
    void pickThis(int arrayNo, int start, int numberOfItems){
        setPickedFalse(start,start+numberOfItems);
        for(int i = 0;i<btnPick.length;i++){
            btnPick[i].setEnabled(true);
        }
        picked[arrayNo] = true;
        btnPick[arrayNo].setEnabled(false);
    }
}