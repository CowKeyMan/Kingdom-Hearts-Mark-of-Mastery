//Imported for images
import java.awt.*;
//Both imported for audio
import sun.audio.*;
import java.io.*;
public class Food{
    //The speed for the items that will be used
    int sheepSpeed, cannonSpeed, bulletSpeed, bulletSpeedX, bulletSpeedY, pointerSpeed, meatSpeed;
    //sheepState: 1 = White, 2 = Black, 3 = Golden
    //pointerState: 1 = moving left, 2 = moving right
    //bulletState: 1 = moving left, 2 = moving right, 3 = moving up
    int sheepState, pointerState, bulletState;
    //The user has an amount of bullets. Once the bullets end, the game finishes
    int bulletsLeft;
    //This is the integer used to determine the direction of where the sheep shall go
    int reroll;
    //The amount of sheep hit
    int sheepHit;
    //The images for the sheep
    Image whiteSheepUp, whiteSheepDown, whiteSheepLeft, whiteSheepRight;
    Image blackSheepUp, blackSheepDown, blackSheepLeft, blackSheepRight;
    Image goldenSheepUp, goldenSheepDown, goldenSheepLeft, goldenSheepRight;
    //The image used for the bullet Sprite
    Image bulletImage;
    //The images used for the cannon Sprite. 
    //'Cannon Move' is the animation the cannon makes when it is fired.
    Image cannonStill, cannonMove;
    //The images used for the cannon Sprite
    Image pointerImage;
    //The images used for the meat Sprite
    Image collectedMeat;
    //The sprites used in the program
    Sprite sheep, cannon, pointer, meat;
    CircleSprite bullet;
    //This boolean determines whether the sheep starts right and moves left or vice versa
    boolean startRight;
    //used for the cannon exploding animation. It's used to determine when and when not the animation can play.
    // it works with the explosion duration which is the timer for the animation. It is like a 'firstTime' for the cannonFired
    //which is true when the bullet is travelling and fals when the bullet is inside the cannon.
    boolean cannonExplode = false;
    long explosionDuration;
    boolean cannonFired = false;
    //The boolean which is true when the meat is falling and false when the me
    boolean meatFalling;
    //The time it takes for the sheep to change it's direction
    long rerollTime = System.currentTimeMillis();
    //The button to exit the situation
    Button btnBack;
    //The audio player
    AudioStream as;
    //The 'disk' to be played in the player
    FileInputStream  music;
    //The background image
    Image background;
    
    //Play the SFX when the sheep is hit and the meat starts falling
    void playSheepHit(boolean canPlay){
        if(canPlay == true){
            try{
                music = new FileInputStream("SheepHit.wav");
                as = new AudioStream(music);
            }catch (Exception e){
                System.err.println(e);
            }
            AudioPlayer.player.start(as);
        }
    }
    //To exit the situation and reset everything
    void btnBack(int i, int widthLimit,int heightLimit){
        btnBack.setVisible(false);
        bulletsLeft = i;
        cannonFired = false;
        meatFalling = false;
        meat.setVisible(false);
        resetBulletPosition();
        sheepHit = 0;
        sheepReset(widthLimit, heightLimit);
    }
    //Reset the sheep position after it is hit by a bullet or it reaches the other side of the screen
    void sheepReset(int widthLimit,int heightLimit){
        //set the starting position
        int h = (int)(1+Math.random()*2);
        if(h == 1){
            startRight = true;
            sheep.setX(widthLimit-sheep.getWidth());
        }else{
            startRight = false;
            sheep.setX(0);
        }
        sheep.setY((int)(25+Math.random()*(heightLimit-sheep.getHeight())));
        //If the amount of sheep hit exceeds a certain number, they start giving more food per meat and more xp
        //This is determined by the sheepState. They also change colour for clarity
        if(sheepHit>=0 && sheepHit<5){
            sheepState = 1;
        }else if(sheepHit>=5 && sheepHit<10){
            sheepState = 2;
        }else{
            sheepState = 3;
        }
        meat.setVisible(false);
        meatFalling = false;
        //Each type of sheep has a different speed
        switch(sheepState){
            case 1:
                sheepSpeed = 7;
            break;
            case 2:
                sheepSpeed = 8;
            break;
            case 3:
                sheepSpeed = 10;
            break;
        }
    }
    
    void sheepMove(int widthLimit,int heightLimit){
        //The sheep changes direction every quarter of a second. This is what the reroll variable is here.
        //However, if the sheep started from the right side of the screen, it never goes back to the right side,
        //it goes left, up or down. Vice versa for starting on the left side.
        if(System.currentTimeMillis()-rerollTime>250){
            rerollTime = System.currentTimeMillis();
            reroll = (int)(Math.random()*4);
        }
        //The image is determined by the direction, the side from which the sheep started and sheep state
        switch(reroll){
            case 1:
                switch(sheepState){
                    case 1:
                        sheep.setImage(whiteSheepUp, sheep.getWidth(), sheep.getHeight());
                    break;
                    case 2:
                        sheep.setImage(blackSheepUp, sheep.getWidth(), sheep.getHeight());
                    break;
                    case 3:
                        sheep.setImage(goldenSheepUp, sheep.getWidth(), sheep.getHeight());
                    break;
                }
                sheep.moveUp(sheepSpeed);
                if(sheep.getY()<=25){
                    sheep.moveDown(sheepSpeed);
                }
            break;
            case 2:
                switch(sheepState){
                    case 1:
                        sheep.setImage(whiteSheepDown, sheep.getWidth(), sheep.getHeight());
                    break;
                    case 2:
                        sheep.setImage(blackSheepDown, sheep.getWidth(), sheep.getHeight());
                    break;
                    case 3:
                        sheep.setImage(goldenSheepDown, sheep.getWidth(), sheep.getHeight());
                    break;
                }
                sheep.moveDown(sheepSpeed);
                if(sheep.getY()+sheep.getHeight()>heightLimit){
                    sheep.moveUp(sheepSpeed);
                }
            break;
            case 3:
            case 4:
                if(startRight == true){
                    switch(sheepState){
                        case 1:
                            sheep.setImage(whiteSheepLeft, sheep.getWidth(), sheep.getHeight());
                        break;
                        case 2:
                            sheep.setImage(blackSheepLeft, sheep.getWidth(), sheep.getHeight());
                        break;
                        case 3:
                            sheep.setImage(goldenSheepLeft, sheep.getWidth(), sheep.getHeight());
                        break;
                    }
                    sheep.moveLeft(sheepSpeed);
                    if(sheep.getX()<0){
                        sheepReset(widthLimit, heightLimit);
                    }
                }else{
                    switch(sheepState){
                        case 1:
                            sheep.setImage(whiteSheepRight, sheep.getWidth(), sheep.getHeight());
                        break;
                        case 2:
                            sheep.setImage(blackSheepRight, sheep.getWidth(), sheep.getHeight());
                        break;
                        case 3:
                            sheep.setImage(goldenSheepRight, sheep.getWidth(), sheep.getHeight());
                        break;
                    }
                    sheep.moveRight(sheepSpeed);
                    if(sheep.getX()-sheep.getWidth()>widthLimit){
                        sheepReset(widthLimit, heightLimit);
                    }
                }
                if(sheep.getY()+sheep.getHeight()>heightLimit){
                    sheep.moveUp(sheepSpeed);
                }
            break;
        }
    }
    //resetting the bullet to be in the cannon
    void resetBulletPosition(){
        bullet.setX(cannon.getX()+16);
        bullet.setY(cannon.getY()+25);
    }
    //moving the cannon to the left while also ensuring it does not go out of the screen
    void cannonMoveLeft(){
        cannon.moveLeft(cannonSpeed);
        if(cannon.getX()<0){
            cannon.moveRight(cannonSpeed);
        }
        if(cannonFired == false){
            resetBulletPosition();
        }
    }
    //moving the cannon to the right while also ensuring it does not go out of the screen
    void cannonMoveRight(int xEnd){
        cannon.moveRight(cannonSpeed);
        if((cannon.getX()+cannon.getWidth())>xEnd){
            cannon.moveLeft(cannonSpeed);
        }
        if(cannonFired == false){
            resetBulletPosition();
        }
    }
    //This method is done everytime the program is run. I
    //It is done once in the beginning or everytime this part of the program is reset
    void setPointerCoordinates(int yPoint,int xEnd){
        pointer.setX((int)(Math.random()*xEnd));
        pointer.setY(yPoint+pointer.getHeight());
        pointerState = (int)(1+Math.random()*2);
    }
    //The pointer moves to the far end of the screen then changes it's direction to go the other side 
    //and loops this movement.
    void movePointer(int xEnd){
        if(pointerState == 1){
            if(pointer.getX()<0){
                pointerState = 2;
            }else{
                pointer.moveLeft(pointerSpeed);
            }
        }else{
             if((pointer.getX()+pointer.getWidth())>xEnd){
                 pointerState = 1;
            }else{
                pointer.moveRight(pointerSpeed);
            }
        }
    }
    //The method for setting the speed for the X and Y coordinate of the bullet.
    void setBulletSpeed(int x,int y){
        if(cannonFired == false){
            if(y<bullet.getY()-100){
                //getting the midpoints of the bullets to get clearer directions
                int bulletX = bullet.midpointX;
                int bulletY = bullet.midpointY;
                double hyp = 0;
                cannonFired = true;
                if(bulletX>x){
                    bulletState = 1;
                    //Bullet moves Up and Left
                }else if(bulletX<x){
                    bulletState = 2;
                    //bullet moves Up and Right
                }else{
                    bulletState = 3;
                    //bullet moves straight up
                }
                //calculating the hypotonuse in the case of diagonal movements
                if(bulletState == 1){
                    hyp = Math.sqrt(Math.pow(bulletX-x,2)+Math.pow(bulletY-y,2));
                }else if(bulletState == 2){
                    hyp = Math.sqrt(Math.pow(x-bulletX,2)+Math.pow(bulletY-y,2));
                }
                //no calculations for bullet state 3 as it's straight up
                if(bulletState == 3){
                    bulletSpeedX = 0;
                    bulletSpeedY = bulletSpeed;
                }else{
                    //The hyp is divided by the bullet speed and the answer is stored in dSpeed
                    double dSpeed = hyp/bulletSpeed;
                    //This is then used to divide the length between vertical and horizontal distances by it
                    //to determine how many points the bullet must travel in each frame
                    double xx = Math.round(((Math.abs(bulletX-x))/dSpeed));
                    double yy = Math.round((Math.abs((bulletY-y)/dSpeed)));
                    //The speed is then rounded to the nearest integer. This is unfortunate as the angles may not be perfect
                    //and may miss if the target is very thin.
                    if((int)xx+0.5>xx){
                        bulletSpeedX = (int)xx;
                    }else{
                        bulletSpeedX = (int)xx++;
                    }
                    if((int)yy+0.5>yy){
                        bulletSpeedY = (int)yy;
                    }else{
                        bulletSpeedY = (int)yy++;
                    }
                }
            }
        }
    }
    //The bullet moves depending on it's direction set by the state
    void shootBullet(int xEnd, int heightLimit, boolean canPlay){
        if(cannonFired == true){
            if(bulletState == 1){
                bullet.moveLeft(bulletSpeedX);
                bullet.moveUp(bulletSpeedY);
                if(bullet.getX()<0){
                    bulletState = 2;
                }
            }else if (bulletState == 2){
                bullet.moveRight(bulletSpeedX);
                bullet.moveUp(bulletSpeedY);
                if(bullet.getX()+bullet.getDiameter()>xEnd){
                    bulletState = 1;
                }
            }else{
                bullet.moveUp(bulletSpeedY);
            }
            //Once the bullet collides with the sheep, the sound is played, the meat starts falling
            //and the sheepHit counter is increased. Further more a new sheep also appears.
            if(bullet.hasCollidedWith(sheep)){
                playSheepHit(canPlay);
                meat.setVisible(true);
                sheepHit++;
                sheepReset(xEnd, heightLimit);
                meatFalling = true;
            }
            //If the bullet hit's the sheep or the ceiling, the bullet goes to its original position
            //and the cannon can be fired once more
            if(bullet.getY()<25 || bullet.hasCollidedWith(sheep)){
                cannonFired = false;
                resetBulletPosition();
            }
        }
    }
    //If the meat is falling it goes straight down for the user to catch
    //It stops falling when it hits the ground or the user catches it(the last part is not here).
    //If it is not falling, the meat returns to it's original position which is in the sheep.
    void dropMeat(int endY){
        if(meatFalling == true){
            meat.setVisible(true);
            meat.moveDown(meatSpeed);
        }else{
            meat.setX(sheep.getX());
            meat.setY(sheep.getY());
        }
        if(meat.hasCollidedWith(cannon) || meat.getY()-meat.getHeight()>endY){
            meat.setVisible(false);
            meatFalling = false;
        }
    }
    //This method is to play the animation for the exploding cannon
    void explodeCannon(){
        //It is like a firstTime method where if is the first movement of the bullet 
        //when the cannon is exploding, start animation
        if(cannonFired == false){
            cannonExplode = true;
        }else if(cannonFired == true){
            if(cannonExplode == true){
                explosionDuration = System.currentTimeMillis();
                cannonExplode = false;
            }
            if(System.currentTimeMillis()-explosionDuration>400){
                cannon.setImage(cannonStill);
            }else{
                cannon.setImage(cannonMove);
            }
        }
    }
}