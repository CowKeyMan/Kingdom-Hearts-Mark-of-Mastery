import java.awt.*;
public class GoldCollection{
    //The speed for the bombs and relics
    int bombSpeed, relicSpeed;
    //These are the variables that have to do with the relics that the player must collect.
    //dropDirection: 1 = up, 2 = right, 3 = left, 4 = down
    // dropState: 1 = bronze, 2 = silver, 3 = gold, 4 = diamond, 5 = platinum, 6 = ruby, 7 = immortalityReic, 8 = speedRelic
    //The bigger the dropState, the bigger the dropGain, which is how much in-game money the user gets
    int dropDirection[], dropState[], dropGain[];
    //Images to do with the coins/relics/drops
    Image bronzeBlock, silverBlock, goldBlock, diamondBlock, platinumBlock, rubyBlock, immortalityRelic, speedRelic;
    //Images todo witht he bombs which the user must avoid
    Image bombLeft, bombRight, bombExplode;
    //the circle sprites for the bombs and coins
    CircleSprite drop[], bomb[];
    //The time it takes for the bombs to change their direction
    long bombDirectionChangeTimer = System.currentTimeMillis();;
    //The time taken for the exploding animation when user hits the bomb
    long animeTime = System.currentTimeMillis();
    //The timer fr how long the user stays immortal or with increased speed
    long immortalityTime, speedTime;
    //Before ending, the bomb that is exploding needs to do an animation and this is the variable
    //that lets it do the animation
    boolean exploding = false;
    //The direction that the bombs are going in. Each bomb goes to a different direction
    //which is why it's an array and the direction is random.
    int r[];
    //The button to exit the room
    Button btnBack;
    //The background image
    Image background;
    //The method that moves the bombs
    void bombMove(int widthLimit, int heightLimit){
        //The bombs' speed is changed every 2 seconds
        for(int i = 0;i<bomb.length;i++){
            if(System.currentTimeMillis()-bombDirectionChangeTimer>2000){
                for(int x = 0;x<bomb.length;x++){
                    r[x] = (int)(1+Math.random()*4);
                }
                bombDirectionChangeTimer = System.currentTimeMillis();
            }
            //Each bomb is moved depending on it's direction every frame
            //If the bomb hits a wall, it's direction is reset
            switch(r[i]){
                case 1:
                    bomb[i].moveUp(bombSpeed);
                    if(bomb[i].getY()<25){
                        bomb[i].moveDown(bombSpeed);
                        r[i] = (int)(1+Math.random()*4);
                    }
                break;
                case 2:
                    bomb[i].moveLeft(bombSpeed);
                    if(bomb[i].getX()<3){
                        bomb[i].moveRight(bombSpeed);
                        r[i] = (int)(1+Math.random()*4);
                    }
                break;
                case 3:
                    bomb[i].moveRight(bombSpeed);
                    if(bomb[i].getX()>widthLimit){
                        bomb[i].moveLeft(bombSpeed);
                        r[i] = (int)(1+Math.random()*4);
                    }
                break;
                case 4:
                    bomb[i].moveDown(bombSpeed);
                    if(bomb[i].getY()>heightLimit){
                        bomb[i].moveUp(bombSpeed);
                        r[i] = (int)(1+Math.random()*4);
                    }
                break;
            }
        }
    }
    //This method resets the gold drop by putting it on one of the 4 edges of the screen and
    //giving it a direction opposite of whatever edge it was placed at.
    //The more gold it gives, the less chance it has of appearing
    void resetGoldDrop(int widthLimit, int heightLimit, CircleSprite dropItem, int a){
        for(int c = 0;c<drop.length;c++){
            int i = (int)(1+Math.random()*100);
            dropDirection[a] = (int)(1+Math.random()*4);
            if(i>=1 && i<=24){
                dropItem.setImage(bronzeBlock);
                dropState[a] = 1;
            }else if(i>=25 && i<=44){
                dropItem.setImage(silverBlock);
                dropState[a] = 2;
            }else if(i>=45 && i<=59){
                dropItem.setImage(goldBlock);
                dropState[a] = 3;
            }else if(i>=60 && i<=71){
                dropItem.setImage(diamondBlock);
                dropState[a] = 4;
            }else if(i>=72 && i<=80){
                dropItem.setImage(platinumBlock);
                dropState[a] = 5;
            }else if(i>=81 && i<=84){
                dropItem.setImage(rubyBlock);
                dropState[a] = 6;
            }else if(i>=85 && i<=92){
                dropItem.setImage(immortalityRelic);
                dropState[a] = 7;
            }else if(i>=93 && i<=100){
                dropItem.setImage(speedRelic);
                dropState[a] = 8;
            }
        }
        switch(dropDirection[a]){
            case 1:
                dropItem.setX((int)(3+Math.random()*(widthLimit-dropItem.getDiameter())));
                dropItem.setY(heightLimit+50);
            break;
            case 2:
                dropItem.setX(3);
                dropItem.setY((int)(25+Math.random()*(heightLimit-dropItem.getDiameter())));
            break;
            case 3:
                dropItem.setX(widthLimit+50);
                dropItem.setY((int)(25+Math.random()*(heightLimit-dropItem.getDiameter())));
            break;
            case 4:
                dropItem.setX((int)(3+Math.random()*(widthLimit-dropItem.getDiameter()-20)));
                dropItem.setY(25);
            break;
        }
    }
    /*The drop is moved in the direction set before. It stops and resets itself
      once it hits a wall.
      In the main program it should also stop and resetwhen it hits the character sprite.
      It should also give the player gold. */
    void moveDrop(int widthLimit, int heightLimit){
        for(int i=0; i<drop.length;i++){
            switch(dropDirection[i]){
                case 1:
                    if(drop[i].getY()<25){
                        resetGoldDrop(widthLimit, heightLimit, drop[i], i);
                    }else{
                        drop[i].moveUp(relicSpeed);
                    }
                break;
                case 2:
                    if(drop[i].getX()>widthLimit){
                        resetGoldDrop(widthLimit, heightLimit, drop[i], i);
                    }else{
                        drop[i].moveRight(relicSpeed);
                    }
                break;
                case 3:
                    if(drop[i].getX()<3){
                        resetGoldDrop(widthLimit, heightLimit, drop[i], i);
                    }else{
                        drop[i].moveLeft(relicSpeed);
                    }
                break;
                case 4:
                    if(drop[i].getY()>heightLimit){
                        resetGoldDrop(widthLimit, heightLimit, drop[i], i);
                    }else{
                        drop[i].moveDown(relicSpeed);
                    }
                break;
            }
        }
    }
    //When the program is exited
    void btnBack(){
        btnBack.setVisible(false);
    }
}