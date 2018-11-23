//Used to import Buttons and Images classes
import java.awt.*;
public class Shop extends Frame{
    //The state of the shop. Determines what is being shown on screen regarding Labels, buttons and images
    //state: 0 = shop/sell, 1 = [buy]item type choice, 2 = [sell] items,3 = [buy] items
    int state;
    /*The details of each item.
      If it is an armor item it will have it's min/max damage as 0
      and melee and ranged items/weapons have their armor as 0*/
    String name[];
    int price[];
    int maxdmg[];
    int mindmg[];
    int armor[];
    //BooleanOwned is used to show the items owned when the user plans to sell any of them
    //Only the items which are 'true' will be displayed.
    boolean booleanOwned[];
    //The following are used to show the items each particular type of item 
    //when the user plans to buy. Different types of items are displayed seperately.
    boolean booleanMelee[];
    boolean booleanRanged[];
    boolean booleanArmor[];
    //The buttons to be used in the shop
    Button btnBack;
    //These 2 are displayed first, they determine whether the user wants to buy or sell items.
    Button btnShop, btnSell;
    //These 3 are displayed when the user decides that he wants to BUY an item
    //They determine what TYPE of item the user wants to buy
    Button btnMelee, btnRanged, btnArmor;
    //These are displayed next to each item. 
    //They determine which item the user wants to buy/sell accordingly
    Button btnBuyItem[], btnSellItem[];
    //These are the labels for each item.
    Label lblName[];
    Label lblPrice[];
    Label lblMaxdmg[];
    Label lblMindmg[];
    Label lblArmor[];
    //This is the question displayed for the user, for him to answer by pressing buttons
    //Example: "which item would you like to buy".
    String question;
    //These are the images displayed next to each item
    Image weaponImage[];
    //These are booleans to be used in the paint method to display the images
    //Example: if(meleeVisible[1] == true){drawImage(meleeImage[1])}
    boolean meleeVisible[], rangedVisible[], armorVisible[];
    //The background used for the shop
    Image background;
    
    //Set all the images invisible
    void setInvisibleBooleans(){
        for(int i = 0; i<meleeVisible.length;i++){
            meleeVisible[i] = false;
        }
        for(int i = 0; i<rangedVisible.length;i++){
            rangedVisible[i] = false;
        }
        for(int i = 0; i<armorVisible.length;i++){
            armorVisible[i] = false;
        }
    }
    //set only melee images invisible
    void setMeleeImagesVisible(){
        for(int i = 0; i<meleeVisible.length;i++){
            meleeVisible[i] = true;
        }
    }
    //set only ranged images invisible
    void setRangedImagesVisible(){
        for(int i = 0; i<rangedVisible.length;i++){
            rangedVisible[i] = true;
        }
    }
    //set only armor items invisible
    void setArmorImagesVisible(){
        for(int i = 0; i<armorVisible.length;i++){
            armorVisible[i] = true;
        }
    }
    //gets a number from the mindmg - max dmg
    //All that is needed is the weapon number
    int getDamage(int i){
        return (int)(mindmg[i]+Math.random()*maxdmg[i]);
    }
    //The welcoming screen
    void shopInterfaceShopOrSell(){
         question = "What would you like to do:";
         btnBack.setVisible(true);
         btnShop.setVisible(true);
         btnSell.setVisible(true);
         requestFocus();
    }
    //The methods for the back button
    void btnBack(boolean booleanOwned2[]){
        if(this.state == 0){
            //exit the shop
            btnBack.setVisible(false);
            btnShop.setVisible(false);
            btnSell.setVisible(false);
        }else if(this.state == 1){
            //state 1 is when the user presses btnBuy and the item types are displayed on screen
            //Therefore he needs to go back to the opening screen
            //Goes back to state 0
            state = 0;
            question = "What would you like to do:";
            btnMelee.setVisible(false);
            btnRanged.setVisible(false);
            btnArmor.setVisible(false);
            btnShop.setVisible(true);
            btnSell.setVisible(true);
        }else if(this.state == 2){
            //state 2 is when the user has pressed an item type and now has items displayed in front of him
            //Therefore we need to hide the items and the btnBuyItem and display they item types again
            //Go back to state 1
            state = 1;
            question = "Which type would you like to buy:";
            btnMelee.setVisible(true);
            btnRanged.setVisible(true);
            btnArmor.setVisible(true);
            for(int i=0;i<name.length;i++){
                btnBuyItem[i].setVisible(false);
                btnBuyItem[i].setEnabled(false);
                lblName[i].setVisible(false);
                lblPrice[i].setVisible(false);
                lblMindmg[i].setVisible(false);
                lblMaxdmg[i].setVisible(false);
                lblArmor[i].setVisible(false);
            }
            setInvisibleBooleans();
        }else if(this.state == 3){
            //state 3 is when the user presses btnSell and now has his owned items displayed in front of him
            //Therefore we need to go to the welcoming screen again and hide the current labels and buttons
            //Go back to state 0
            state = 0;
            question = "What would you like to do:";
            btnShop.setVisible(true);
            btnSell.setVisible(true);
            for(int i=0;i<name.length;i++){
                lblName[i].setVisible(false);
                lblPrice[i].setVisible(false);
                lblMindmg[i].setVisible(false);
                lblMaxdmg[i].setVisible(false);
                lblArmor[i].setVisible(false);
            }
            for(int i=0;i<btnSellItem.length;i++){
                btnSellItem[i].setVisible(false);
                btnSellItem[i].setEnabled(false);
            }
            for(int i = 0;i<booleanOwned.length;i++){
                booleanOwned[i] = booleanOwned2[i];
            }
        }
    }
    //Shows the item types and hides btnBuy and btnSell
    void btnShop(){
        question = "Which type would you like to buy:";
        this.state = 1;
        btnShop.setVisible(false);
        btnSell.setVisible(false);
        btnMelee.setVisible(true);
        btnRanged.setVisible(true);
        btnArmor.setVisible(true);
    }
    //Shows the owned items and hides btnBuy and btnSellItem[]
    void btnSell(){
        this.state = 3;
        question = "What would you like to sell:";
        btnSell.setVisible(false);
        btnShop.setVisible(false);
        int counter=0;
        for(int i=0;i<name.length;i++){
            if(booleanOwned[i] == true){
                btnSellItem[counter].setVisible(true);
                btnSellItem[counter].setEnabled(true);
                lblName[i].setVisible(true);
                lblPrice[i].setVisible(true);
                lblMindmg[i].setVisible(true);
                lblMaxdmg[i].setVisible(true);
                lblArmor[i].setVisible(true);
                counter++;
            }
        }
    }
    //Shows the melee items labels and btnBuyItem[] for each item
    void btnMelee(){
        setMeleeImagesVisible();
        this.state = 2;
        question = "What would you like to buy:";
        btnMelee.setVisible(false);
        btnRanged.setVisible(false);
        btnArmor.setVisible(false);
        boolean firstTime = true;
        int startValue = 0, finishValue, counter = 0;
        for(int i = 0;i<name.length;i++){
            if(booleanMelee[i] == true){
                if(firstTime == true){
                    firstTime = false;
                    startValue = i;
                }
                counter++;
            }
        }
        finishValue = counter+startValue;
        for(int i=startValue;i<finishValue;i++){
            lblName[i].setVisible(true);
            lblPrice[i].setVisible(true);
            lblMindmg[i].setVisible(true);
            lblMaxdmg[i].setVisible(true);
            lblArmor[i].setVisible(true);
            if(booleanMelee[i] == true){
                if(booleanOwned[i] == false){
                    btnBuyItem[i].setVisible(true);
                    btnBuyItem[i].setEnabled(true);
                }else{
                    btnBuyItem[i].setVisible(true);
                    btnBuyItem[i].setEnabled(false);
                }
            }
        }
    }
    //Shows the ranged items labels and btnBuyItem[] for each item
    void btnRanged(){
        setRangedImagesVisible();
        this.state = 2;
        question = "What would you like to buy:";
        btnMelee.setVisible(false);
        btnRanged.setVisible(false);
        btnArmor.setVisible(false);
        boolean firstTime = true;
        int startValue = 0, finishValue, counter = 0;
        for(int i = 0;i<name.length;i++){
            if(booleanRanged[i] == true){
                if(firstTime == true){
                    firstTime = false;
                    startValue = i;
                }
                counter++;
            }
        }
        finishValue = counter+startValue;
        for(int i=startValue;i<finishValue;i++){
            lblName[i].setVisible(true);
            lblPrice[i].setVisible(true);
            lblMindmg[i].setVisible(true);
            lblMaxdmg[i].setVisible(true);
            lblArmor[i].setVisible(true);
            if(booleanRanged[i] == true){
                if(booleanOwned[i] == false){
                    btnBuyItem[i].setVisible(true);
                    btnBuyItem[i].setEnabled(true);
                }else{
                    btnBuyItem[i].setVisible(true);
                    btnBuyItem[i].setEnabled(false);
                }
            }
        }
    }
    //Shows the armor items labels and btnBuyItem[] for each item
    void btnArmor(){
        setArmorImagesVisible();
        this.state = 2;
        question = "What would you like to buy:";
        btnMelee.setVisible(false);
        btnRanged.setVisible(false);
        btnArmor.setVisible(false);
        boolean firstTime = true;
        int startValue = 0, finishValue, counter = 0;
        for(int i = 0;i<name.length;i++){
            if(booleanArmor[i] == true){
                if(firstTime == true){
                    firstTime = false;
                    startValue = i;
                }
                counter++;
            }
        }
        finishValue = counter+startValue;
        for(int i=startValue;i<finishValue;i++){
            lblName[i].setVisible(true);
            lblPrice[i].setVisible(true);
            lblMindmg[i].setVisible(true);
            lblMaxdmg[i].setVisible(true);
            lblArmor[i].setVisible(true);
            if(booleanArmor[i] == true){
                if(booleanOwned[i] == false){
                    btnBuyItem[i].setVisible(true);
                    btnBuyItem[i].setEnabled(true);
                }else{
                    btnBuyItem[i].setVisible(true);
                    btnBuyItem[i].setEnabled(false);
                }
            }
        }
    }
}