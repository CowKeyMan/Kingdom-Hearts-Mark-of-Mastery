import java.awt.*;
import java.util.ArrayList;
import java.io.*;
import sun.audio.*;

public class FinalBoss{
    //state: 1 = walking towards char, 2 = around char, 3 = same direction as char, 4 = Opposite direction of char, 5 = Dark aura,  6 = DM, 7 = standing still, 8 = teleport behind char
    int state;
    //The enemy statistics
    int hp, maxHP, enemySpeed, enemySpeedX, enemySpeedY, bulletSpeed, bulletSpeedX[], bulletSpeedY[], bulletDamage, meleeDamage;
    //The images of the enemy standing still
    Image upStill, downStill, leftStill, rightStill;
    Image upLeftStill, upRightStill, downLeftStill, downRightStill;
    //The images of the enemy moving
    Image upMove, downMove, leftMove, rightMove;
    Image upLeftMove, upRightMove, downLeftMove, downRightMove;
    //The images of the enemy atacking
    Image upAttack, downAttack, leftAttack, rightAttack;
    Image upLeftAttack, upRightAttack, downLeftAttack, downRightAttack;
    //The image to display when he is doing the special ability
    Image DMImage;
    //The image that is made when he is standing still(state 7)
    Image tired;
    //The image to be used for all the bullets
    Image bulletImage;
    //The image to be used for the teleport animation
    Image teleportImage;
    //The main enemy sprite
    Sprite enemy;
    //The bounds sprites
    Sprite boundsUpLeft, boundsUp, boundsUpRight;
    Sprite boundsLeft, boundsMiddle, boundsRight;
    Sprite boundsDownLeft,boundsDown, boundsDownRight;
    //the sprite for the teleport animation
    Sprite teleport;
    //The sprites to be used as bullets by the enemy
    CircleSprite bullet[];
    //The direction that the enemy is going(1 of 8)
    int direction;
    
    //This is the boolean which when true, means that the character maybe damaged by the enemy
    boolean enemyHitting = false;
    //This is the boolean which determines the direction of the enmey when in state 2(moving around char
    boolean clockwise = true;
    //This is the boolean that determines that the clockwise boolean may be rerolled
    boolean firstTimeDirectionChange = true;
    //This is the boolean which is true when the bullets are travelling and false otherwise
    boolean bulletFired[];
    //This is the boolean to determine that when the enemy has his special ability unlocked,
    //he uses it first thing instead of waiting for the random generator to get it
    boolean firstDM = true;
    //This is the boolean that says that the state may be changed and a new number gets generated
    boolean canChangeState = true;
    //This boolean is to make sure that the enemy starts at state 7(standing still)
    boolean firstTime = true;
    //This boolean is to only say the quote once when attacking
    boolean firstTimeAttacking = true;
    //The timers for stopping at diagonal distances
    long upTimer, downTimer, leftTimer, rightTimer;
    //The timer for changing states and make the enemy do something else
    long stateTimer;
    //The cooldown for the abilities
    long teleportCooldown, darkAuraCooldown;
    //The delay before the enemy attacks
    long hitTimer;
    //The duration that the teleport animation stays on screen
    long teleportTimer;
    //the random number that determines the state, as the states don't have the same chance to occur
    int r = 0;
    //The audio player
    AudioStream as;
    //The 'disk' input int he player
    FileInputStream  music;
    //The background images. DM background is displayed when the special ability is active
    Image normalBackground, DMBackground;
    
    //These quotes are only played if the "canPlayVoices" boolean is true
    //Play a random quote when the enemy attacks
    void playBattleQuote(boolean canPlay){
        if(canPlay == true){
            try{
                int i = (int)(Math.random()*6);
                switch(i){
                    case 0:
                        music = new FileInputStream("RikuBattleQuote1.wav");
                    break;
                    case 1:
                        music = new FileInputStream("RikuBattleQuote2.wav");
                    break;
                    case 2:
                        music = new FileInputStream("RikuBattleQuote3.wav");
                    break;
                    case 3:
                        music = new FileInputStream("RikuBattleQuote4.wav");
                    break;
                    case 4:
                        music = new FileInputStream("RikuBattleQuote5.wav");
                    break;
                    case 5:
                        music = new FileInputStream("RikuBattleQuote6.wav");
                    break;
                    case 6:
                        music = new FileInputStream("RikuBattleQuote7.wav");
                    break;
                }
                as = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(as);
        }
    }
    //Play a quote when the special move starts
    void playDMQuote(boolean canPlay){
        if(canPlay == true){
            try{
                int i = (int)(Math.random()*4);
                switch(i){
                    case 0:
                        music = new FileInputStream("RikuDMQuote1.wav");
                    break;
                    case 1:
                        music = new FileInputStream("RikuDMQuote2.wav");
                    break;
                    case 2:
                        music = new FileInputStream("RikuDMQuote3.wav");
                    break;
                    case 3:
                        music = new FileInputStream("RikuDMQuote4.wav");
                    break;
                    case 4:
                        music = new FileInputStream("RikuDMQuote5.wav");
                    break;
                }
                as = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(as);
        }
    }
    //Play one of the following laughs after a teleport
    void playLaugh(boolean canPlay){
        if(canPlay == true){
            try{
                int i = (int)(Math.random()*1);
                switch(i){
                    case 0:
                        music = new FileInputStream("RikuLaugh1.wav");
                    break;
                    case 1:
                        music = new FileInputStream("RikuLaugh2.wav");
                    break;
                }
                as = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(as);
        }
    }
    //Play a quote when the enemy hits after a special move
    void playDMHitQuote(boolean canPlay){
        if(canPlay == true){
            try{
                int i = (int)(Math.random()*2);
                switch(i){
                    case 0:
                        music = new FileInputStream("RikuDMHitQuote1.wav");
                    break;
                    case 1:
                        music = new FileInputStream("RikuDMHitQuote2.wav");
                    break;
                    case 2:
                        music = new FileInputStream("RikuDMHitQuote3.wav");
                    break;
                }
                as = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(as);
        }
    }
    //Make the teleport animation visible for 4/5 of a second
    void teleportVisible(){
        if(System.currentTimeMillis() - teleportTimer<800){
            teleport.setVisible(true);
        }else{
            teleport.setVisible(false);
        }
    }
    //Put the bounds sprites corresponding to the main enemy sprite
    void resetBounds(){
        boundsUpLeft.moveTo(enemy.getX(),enemy.getY()+9);
        boundsUp.moveTo(enemy.getX()+boundsUpLeft.getWidth(),enemy.getY()+9);
        boundsUpRight.moveTo(enemy.getX()+boundsUpLeft.getWidth()+boundsUp.getWidth(),enemy.getY()+9);
        boundsLeft.moveTo(enemy.getX(),enemy.getY()+boundsUpLeft.getHeight()+9);
        boundsMiddle.moveTo(enemy.getX()+boundsUpLeft.getWidth(),enemy.getY()+boundsUpLeft.getHeight()+9);
        boundsRight.moveTo(enemy.getX()+boundsUpLeft.getWidth()+boundsUp.getWidth(), enemy.getY()+boundsUpLeft.getHeight()+9);
        boundsDownLeft.moveTo(enemy.getX(),enemy.getY()+boundsUpLeft.getHeight()+boundsLeft.getHeight()+9);
        boundsDown.moveTo(enemy.getX()+boundsUpLeft.getWidth(), enemy.getY()+boundsUpLeft.getHeight()+boundsLeft.getHeight()+9);
        boundsDownRight.moveTo(enemy.getX()+boundsUpLeft.getWidth()+boundsUp.getWidth(),enemy.getY()+boundsUpLeft.getHeight()+boundsLeft.getHeight()+9);
    }
    //Move the enemy and reset the bounds afterwards
    void moveLeft(){
        enemy.moveLeft(enemySpeed);
        resetBounds();
    }
    void moveUp(){
        enemy.moveUp(enemySpeed);
        resetBounds();
    }
    void moveRight(){
        enemy.moveRight(enemySpeed);
        resetBounds();
    }
    void moveDown(){
        enemy.moveDown(enemySpeed);
        resetBounds();
    }
    //Set speedX and speedY in a way to face the character
    void setEnemyDirectionTowardsChar(Sprite other){
        //Get the midpoint of both sprites
        int charMidPointX = other.getX()+other.getWidth()/2;
        int charMidPointY = other.getY()+other.getHeight()/2;
        int enemyMidPointX = boundsMiddle.getX()+boundsMiddle.getWidth()/2;
        int enemyMidPointY = boundsMiddle.getY()+boundsMiddle.getHeight()/2;
        //Find the hypotenuse
        double hyp = Math.sqrt(Math.pow(charMidPointX-enemyMidPointX,2)+Math.pow(charMidPointY-enemyMidPointY,2));
        //divide the hypotenuse by the speed
        double dSpeed = hyp/enemySpeed;
        //divide the horizontal and vertical distances by the dSpeed
        double xx = Math.round(((Math.abs(charMidPointX-enemyMidPointX))/dSpeed));
        double yy = Math.round((Math.abs(charMidPointY-enemyMidPointY)/dSpeed));
        //round to the nearest integer
        if((int)xx+0.5>xx){
            enemySpeedX = (int)xx;
        }else{
            enemySpeedX = (int)xx++;
        }
        if((int)yy+0.5>yy){
            enemySpeedY = (int)yy;
        }else{
            enemySpeedY = (int)yy++;
        }
        //Set the speeds and images to face the character
        if(charMidPointX<=enemyMidPointX && charMidPointY<=enemyMidPointY){
            enemy.setImage(upLeftMove);
            enemySpeedX = -(enemySpeedX);
            enemySpeedY = -(enemySpeedY);
        }else if(charMidPointX<=enemyMidPointX && charMidPointY>=enemyMidPointY){
            enemy.setImage(downLeftMove);
            enemySpeedX = -(enemySpeedX);
        }else if(charMidPointX>=enemyMidPointX && charMidPointY<=enemyMidPointY){
            enemy.setImage(upRightMove);
            enemySpeedY = -(enemySpeedY);
        }else if(charMidPointX>=enemyMidPointX && charMidPointY>=enemyMidPointY){
            enemy.setImage(downRightMove);
        }
        //If X or Y = 0
        if(charMidPointX == enemyMidPointX && charMidPointY > enemyMidPointY){
            enemy.setImage(downMove);
        }else if(charMidPointX == enemyMidPointX && charMidPointY < enemyMidPointY){
            enemy.setImage(upMove);
        }else if(charMidPointX < enemyMidPointX && charMidPointY == enemyMidPointY){
            enemy.setImage(leftMove);
        }else if(charMidPointX > enemyMidPointX && charMidPointY == enemyMidPointY){
            enemy.setImage(rightMove);
        }
        //move down right
    }
    //Move the enemy around the character
    void moveAroundChar(Sprite other){
        //get the midpoints of both characters
        int charMidPointX = other.getX()+other.getWidth()/2;
        int charMidPointY = other.getY()+other.getHeight()/2;
        int enemyMidPointX = boundsMiddle.getX()+boundsMiddle.getWidth()/2;
        int enemyMidPointY = boundsMiddle.getY()+boundsMiddle.getHeight()/2;
        //Find the diagonal speed 
        double move = Math.round(Math.sqrt((Math.pow(enemySpeed,2)/2)));
        int mmove;
        //round to the nearest integer
        if((int)move+0.5>move){
            mmove = ((int)move);
        }else{
            mmove = ((int)move+1);
        }
        //set the images to rotate around the character
        //The clockwise and anti-clockwise are predetermined in the move method of this class
        if(charMidPointX<=enemyMidPointX && charMidPointY<=enemyMidPointY){
            if(clockwise == false){
                if(enemyHitting == false){
                    enemy.setImage(upRightMove);
                }
                enemySpeedX = mmove;
                enemySpeedY = -mmove;
            }else{
                if(enemyHitting == false){
                    enemy.setImage(downLeftMove);
                }
                enemySpeedX = -mmove;
                enemySpeedY = mmove;
            }
        }else if(charMidPointX<=enemyMidPointX && charMidPointY>=enemyMidPointY){
            if(clockwise == false){
                if(enemyHitting == false){
                    enemy.setImage(upLeftMove);
                }
                enemySpeedX = -mmove;
                enemySpeedY = -mmove;
            }else{
                if(enemyHitting == false){
                    enemy.setImage(downRightMove);
                }
                enemySpeedX = mmove;
                enemySpeedY = mmove;
            }
        }else if(charMidPointX>=enemyMidPointX && charMidPointY<=enemyMidPointY){
            if(clockwise == false){
                if(enemyHitting == false){
                    enemy.setImage(downRightMove);
                }
                enemySpeedX = mmove;
                enemySpeedY = mmove;
            }else{
                if(enemyHitting == false){
                    enemy.setImage(upLeftMove);
                }
                enemySpeedX = -mmove;
                enemySpeedY = -mmove;
            }
        }else if(charMidPointX>=enemyMidPointX && charMidPointY>=enemyMidPointY){
            if(clockwise == false){
                if(enemyHitting == false){
                    enemy.setImage(downLeftMove);
                }
                enemySpeedX = -mmove;
                enemySpeedY = mmove;
            }else{
                if(enemyHitting == false){
                    enemy.setImage(upRightMove);
                }
                enemySpeedX = mmove;
                enemySpeedY = -mmove;
            }
        }
    }
    //Teleport 100 units to the other side of the character
    void teleportBehindChar(Sprite other, int widthLimit){
        //Get the midpointX of both sprites
        int charMidPointX = other.getX()+other.getWidth()/2;
        int enemyMidPointX = boundsMiddle.getX()+boundsMiddle.getWidth()/2;
        //If the character is currently on the left of the enemy,
        //put the enemy to the left of the char, and vice versa.
        if(charMidPointX > enemyMidPointX){
            enemy.setX(other.getX()+other.getWidth()+100);
        }else{
            enemy.setX(other.getX() - enemy.getWidth()-100);
        }
        //If the character is at the edge of the screen such that when the enemy teleports
        //he ends up out of the screen, he instead teleports directly on top of the character
         if(boundsMiddle.getX()<3 || boundsMiddle.getX() + boundsMiddle.getWidth()>widthLimit){
            enemy.setX(other.getX());
        }
        enemy.setY(other.getY()-35);
    }
    //This sets the bullet direction to fire a whole line, perpendicular to the character
    //of bullets
    void darkAura(Sprite other, boolean canPlay){
        //Reset the enemy's bounds and stop his speed untill he shoots
        resetBounds();
        enemySpeedX = 0;
        enemySpeedY = 0;
        //Get the midpoint of both sprites
        int charMidPointX = other.getX()+other.getWidth()/2;
        int charMidPointY = other.getY()+other.getHeight()/2;
        int enemyMidPointX = boundsMiddle.getX()+boundsMiddle.getWidth()/2;
        int enemyMidPointY = boundsMiddle.getY()+boundsMiddle.getHeight()/2;
        //Set the enemy sprite to attacking
        if(charMidPointX > enemyMidPointX && charMidPointY > enemyMidPointY){
            enemy.setImage(downRightAttack);
        }else if(charMidPointX < enemyMidPointX && charMidPointY > enemyMidPointY){
            enemy.setImage(downLeftAttack);
        }else if(charMidPointX < enemyMidPointX && charMidPointY < enemyMidPointY){
            enemy.setImage(upLeftAttack);
        }else if(charMidPointX >= enemyMidPointX && charMidPointY <= enemyMidPointY){
            enemy.setImage(upRightAttack);
        }
        //For the amount of bullets, shoot them and space them out
        for(int k = 0;k<bulletFired.length;k++){
            if(bulletFired[k] == false){
                double hyp;
                //reset the bullet's poition
                bullet[k].midpointX = boundsMiddle.getX() + boundsMiddle.getWidth()/2;
                bullet[k].midpointY = boundsMiddle.getY() + boundsMiddle.getHeight()/2;
                double dSpeed;
                double xx, yy;
                if((charMidPointX<enemyMidPointX && charMidPointY<enemyMidPointY) || (charMidPointX>enemyMidPointX && charMidPointY>enemyMidPointY)){
                    //If the hypotenuse has a negative gradient
                    //Calculate hypotenuse
                    if(k%2 == 0){
                        hyp = Math.sqrt(Math.pow(charMidPointX - k*30 -bullet[k].midpointX,2)+Math.pow(charMidPointY + k*15 -bullet[k].midpointY,2));
                    }else{
                        hyp = Math.sqrt(Math.pow(charMidPointX + k*30  -bullet[k].midpointX,2)+Math.pow(charMidPointY - k*15 -bullet[k].midpointY,2));
                    }
                    dSpeed = hyp/bulletSpeed;
                    //Set the bullet Speeds in a way that they are spaced out(30 unites from eachother)
                    if(k%2 == 0){
                        xx = (charMidPointX - k*30*Math.abs(hyp)/500 -bullet[k].midpointX)/dSpeed;
                        yy = (charMidPointY + k*30*Math.abs(hyp)/500 -bullet[k].midpointY)/dSpeed;
                    }else{
                        xx = (charMidPointX + k*30*Math.abs(hyp)/500 -bullet[k].midpointX)/dSpeed;
                        yy = (charMidPointY - k*30*Math.abs(hyp)/500 -bullet[k].midpointY)/dSpeed;
                    }
                }else{
                    //If the hypotenuse has a positive gradient
                    //Calculate hypotenuse
                     if(k%2 == 0){
                        hyp = Math.sqrt(Math.pow(charMidPointX + k*30 -bullet[k].midpointX,2)+Math.pow(charMidPointY + k*15 -bullet[k].midpointY,2));
                    }else{
                        hyp = Math.sqrt(Math.pow(charMidPointX - k*30  -bullet[k].midpointX,2)+Math.pow(charMidPointY - k*15 -bullet[k].midpointY,2));
                    }
                    dSpeed = hyp/bulletSpeed;
                    //Set the bullet Speeds in a way that they are spaced out(30 unites from eachother)
                    if(k%2 == 0){
                        xx = (charMidPointX - k*30*Math.abs(hyp)/500 -bullet[k].midpointX)/dSpeed;
                        yy = (charMidPointY - k*30*Math.abs(hyp)/500 -bullet[k].midpointY)/dSpeed;
                    }else{
                        xx = (charMidPointX + k*30*Math.abs(hyp)/500 -bullet[k].midpointX)/dSpeed;
                        yy = (charMidPointY + k*30*Math.abs(hyp)/500 -bullet[k].midpointY)/dSpeed;
                    }
                }
                //round bulletSpeedX and Y to the nearest integer
                if((int)xx+0.5>xx){
                    bulletSpeedX[k] = (int)xx;
                }else{
                    bulletSpeedX[k] = (int)xx++;
                }
                if((int)yy+0.5>yy){
                    bulletSpeedY[k] = (int)yy;
                }else{
                    bulletSpeedY[k] = (int)yy++;
                }
                bulletFired[k] = true;
                bullet[k].setVisible(true);
                if(bulletSpeedY[k] == bulletSpeedX[k] && bulletSpeedX[k] == 0){
                    bulletSpeedY[k] = bulletSpeed;
                }
            }
        }
        //Make an attacking sound
        playBattleQuote(canPlay);
    }
    //move the bullet to the specified directions
    void moveBullet(int widthLimit, int heightLimit, Sprite other){
        for(int k = 0;k<bulletFired.length;k++){
            if(bulletFired[k] == true){
                //Stop the bullets if they hit a wall or the character's sprite
                if(bullet[k].midpointX-bullet[k].radius<5 || bullet[k].midpointX+bullet[k].radius>widthLimit || bullet[k].midpointY-bullet[k].radius<25 || bullet[k].midpointY+bullet[k].radius>heightLimit || bullet[k].hasCollidedWith(other)){
                    bulletFired[k] = false;
                }else{
                    bullet[k].moveDown(bulletSpeedY[k]);
                    bullet[k].moveRight(bulletSpeedX[k]);
                }
            }
            //If the bullet is not travelling, set it's position on top of the enemy and make it invisible
            else if(bulletFired[k] == false){
                bullet[k].setVisible(false);
                bullet[k].moveTo(boundsMiddle.getX()+boundsMiddle.getWidth()/2+bullet[k].radius,boundsMiddle.getY()+boundsMiddle.getHeight()/2+bullet[k].radius);
            }
        }
    }
    //this methosis customly made for the "KHMoM" application as the monster has certain forms and it changes forms
    //when it loses a certain amount of HP.
    
    /*long randomTimer = System.currentTimeMillis();
    void move(Sprite other, int charSpeed, int widthLimit, int heightLimit){
        darkAura(other);
        moveBullet(widthLimit,heightLimit, other);
        //desperationMove(other,charSpeed,widthLimit/2-25,heightLimit/2-25);
        /*if(System.currentTimeMillis() - randomTimer>5000){
            teleportBehindChar(other,widthLimit);
            randomTimer = System.currentTimeMillis();
        }
        //setEnemyDirectionTowardsChar(other);
        enemy.moveRight(enemySpeedX);
        enemy.moveDown(enemySpeedY);
        resetBounds();
    }*/
    
    
    
    //The main method to move the enemy Sprite
    void move(Sprite other, int widthLimit, int heightLimit, boolean canPlay){
        //get the midpoints of both the character and enemy sprites
        int charMidPointX = other.getX()+other.getWidth()/2;
        int charMidPointY = other.getY()+other.getHeight()/2;
        int enemyMidPointX = boundsMiddle.getX()+boundsMiddle.getWidth()/2;
        int enemyMidPointY = boundsMiddle.getY()+boundsMiddle.getHeight()/2;
        //The state is determined by the following random figures
        //The less health the enemy has, the more forms it unlocks
        if(canChangeState){
            if(hp<=maxHP && hp>maxHP-2000){
                r = (int)(Math.random()*59);
            }else if(hp<=maxHP-2000 && hp>maxHP-4000){
                r = (int)(Math.random()*69);
            }else if(hp<=maxHP-4000 && hp>maxHP-5000){
                r = (int)(Math.random()*79);
            }else if(hp<=maxHP-5000 && hp>maxHP-7000){
                r = (int)(Math.random()*89);
            }
            //When it unlocks the special ability,  it does it before doing any other ability
            else if(hp<=maxHP-7000 && hp>0){
                if(firstDM == true){
                    firstDM = false;
                    r = 90;
                }else{
                    r = (int)(Math.random()*104);
                }
            }
            stateTimer = System.currentTimeMillis();
            canChangeState = false;
            if(r>=89){
                playDMQuote(canPlay);
            }
        }
        //The first thing that the final boss does is that it stays still
        //This is to give the player some breathing room in the beginning of the fight
        if(firstTime == true){
            r = 40;
            firstTime = false;
        }
        //1 = walking towards char
        if(r>=0 && r<32){
            state = 1;
            setEnemyDirectionTowardsChar(other);
            if(System.currentTimeMillis() - stateTimer>2500){
                canChangeState = true;
            }
        }
        //2 = around char
        else if(r>=32 && r<39){
            state = 2;
            int m = 0;
            if(firstTimeDirectionChange == true){
                m = (int)(Math.random()*2);
                firstTimeDirectionChange = false;
            }
            if(m == 0){
                clockwise = true;
            }else{
                clockwise = false;
            }
            moveAroundChar(other);
            if(System.currentTimeMillis() - stateTimer>2500){
                canChangeState = true;
                firstTimeDirectionChange = true;
            }
        }
        //7 = stand still
        else if(r>=39 && r<49){
            state = 7;
            enemySpeedX = 0;
            enemySpeedY = 0;
            enemy.setImage(tired);
            if(System.currentTimeMillis() - stateTimer>3500){
                canChangeState = true;
            }
        }
        //8 = teleport oncharacter
        else if(r>=49 && r<59){
            if(System.currentTimeMillis() - teleportCooldown > 10000){
                playLaugh(canPlay);
                teleportTimer = System.currentTimeMillis();
                teleportCooldown = System.currentTimeMillis();
                state = 8;
                teleportBehindChar(other, widthLimit);
                teleport.moveTo(boundsMiddle.getX(),boundsMiddle.getY());
            }
            state = 1;
            setEnemyDirectionTowardsChar(other);
            if(System.currentTimeMillis() - stateTimer>500){
                canChangeState = true;
            }
        }
        //4 = opposite direction char is moving
        //This methods movements are made in the main program
        else if(r>=59 && r<69){
            state = 4;
            enemySpeedX = 0;
            enemySpeedY = 0;
            if(System.currentTimeMillis() - stateTimer>2500){
                canChangeState = true;
            }
        }
        //3 = same direction char is moving
        //This methods movements are made in the main program
        else if(r>=69 && r<79){
            state = 3;
            enemySpeedX = 0;
            enemySpeedY = 0;
            if(System.currentTimeMillis() - stateTimer>2500){
                canChangeState = true;
            }
        }
        //5 = Dark Aura
        else if(r>=79 && r<89){
            state = 5;
            if(System.currentTimeMillis() - darkAuraCooldown > 3000){
                darkAuraCooldown = System.currentTimeMillis();
                enemySpeedX = 0;
                enemySpeedY = 0;
                darkAura(other, canPlay);
            }
            canChangeState = true;
        }
        // 6 = Desperation Move
        //This methods movements are made in the main program
        
        /*The special/Desperation move is when the enemy turns invulnerable, 
          his movement speed decreases, and he moves towards the character 
          while dragging the character towards him. If he hits the character during this state, 
          he deals 3 times the normal damage and knocks the character to the edge of the screen. 
          This is all coded in the main class*/
        else if(r>=89 && r<104){
            state = 6;
            if(System.currentTimeMillis() - stateTimer>7500){
                canChangeState = true;
            }
            setEnemyDirectionTowardsChar(other);
            enemy.setImage(DMImage);
            enemySpeedX = (int)enemySpeedX / 2;
            enemySpeedY = (int)enemySpeedY / 2;
        }
        //Actually move the enemy
        enemy.moveRight(enemySpeedX);
        enemy.moveDown(enemySpeedY);
        //reset it's bounds after moving
        resetBounds();
        //Move the bullet
        moveBullet(widthLimit, heightLimit, other);
        //Don't let the enemy move past the edge of the screen
        if(boundsMiddle.getX()<3){
            enemy.moveRight(Math.abs(enemySpeed));
        }
        if(boundsMiddle.getX() + boundsMiddle.getWidth()>widthLimit){
            enemy.moveLeft(Math.abs(enemySpeed));
        }
        if(boundsMiddle.getY()<25){
            enemy.moveDown(Math.abs(enemySpeed));
        }
        if(boundsMiddle.getY() + boundsMiddle.getHeight()>heightLimit){
            enemy.moveUp(enemySpeed);
        }
        //Make the image to right or left attack if the enemy is attacking, depending on the direction of the character
        if(enemyHitting == true){
            if(state != 6){
                //Play the battle quote once
                if(firstTimeAttacking == true){
                    playBattleQuote(canPlay);
                    firstTimeAttacking = false;
                }
                if(charMidPointX > enemyMidPointX && charMidPointY > enemyMidPointY){
                    enemy.setImage(downRightAttack);
                }else if(charMidPointX < enemyMidPointX && charMidPointY > enemyMidPointY){
                    enemy.setImage(downLeftAttack);
                }else if(charMidPointX < enemyMidPointX && charMidPointY < enemyMidPointY){
                    enemy.setImage(upLeftAttack);
                }else if(charMidPointX >= enemyMidPointX && charMidPointY <= enemyMidPointY){
                    enemy.setImage(upRightAttack);
                }
            }else{
                //Instead a special quote is heard if the enemy is in the special ability state
                if(firstTimeAttacking == true){
                    playDMHitQuote(canPlay);
                    firstTimeAttacking = false;
                }
            }
        }else{
            //This is reset when the sprites are not colliding anymore
            firstTimeAttacking = true;
        }
        resetBounds();
    }
} 