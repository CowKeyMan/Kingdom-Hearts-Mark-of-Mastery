//Imported for using images
import java.awt.*;
public class CharacterStats{
    //The character details
    String name;
    int hp, maxhp, food, maxFood, armor, gold, speed, defaultSpeed, ownedItems, deathCounter = 0;
    //while bulletSpeed is constant, bulletSpeedX and Y change depending on the direction of the bullet.
    //They are used for non-perfect angles.
    int bulletSpeed, bulletSpeedX, bulletSpeedY;
    int xp, level;
    //The direction the character is facing. 8 in total
    int direction;
    //The amount of xp needed in order to gain a new level
    int xpForNextLevel[];
    //The amount of units the character teleports to when he uses the dash ability
    int dashRange;
    //The amount of units the enemy teleports to when it's hit by the homerunBlow
    int homerunBlowDistance;
    //The state of the bullet whether it's travelling(true) or not(false)
    boolean bulletFired = false;
    //These are the booleans used to store whether the next bullet/attack will be
    //a stun bullet or a homerun blow.
    boolean stunBullet = false, homerunBlow = false;
    //The boolean which determines whether lightSeeker is active
    boolean lightSeeker = false;
    //The enemy takes damage from light seeker at the very end of it, therefore this boolean
    //is meant to be true for 1 frame after light seeker has finished and then turned off again after
    //damage is applied
    boolean lightSeekerHit = false;
    //These 4 timers are used for stopping in diagonal positions
    long upTimer, downTimer, leftTimer, rightTimer;
    //The longs set when the game is paused
    long flashTimer, homeRunBlowTimer, lightSeekerTimer2, stunBulletTimer, healTimer;
    //the following are the cooldown timers for the abilities, which means the amount of time
    //the user has to wait before using the ability again.
    long flashCooldownTimer = System.currentTimeMillis(), homerunBlowCooldownTimer = System.currentTimeMillis() - 15000;
    long stunBulletCooldownTimer = System.currentTimeMillis()- 20000, lightSeekerCooldownTimer = System.currentTimeMillis() - 31000;
    long healCooldownTimer = System.currentTimeMillis()- 20000;
    //The duration of the stun
    long stunTimer = System.currentTimeMillis()- 10000;
    //The duration of the light seeker animation
    long lightSeekerTimer = System.currentTimeMillis() - 10000;
    //the duration of the heal animation
    long healAnimationTimer;
    //The duration of the hitting animation untill another attack is launched
    long hitTimer = System.currentTimeMillis() - 400;
    //Setting the non-moving images of the character
    Image upStill, downStill, leftStill, rightStill;
    Image upLeftStill, upRightStill, downLeftStill, downRightStill;
    //Setting the moving images of the character
    Image upMove, downMove, leftMove, rightMove;
    Image upLeftMove, upRightMove, downLeftMove, downRightMove;
    //Setting the attacking images of the character
    Image upAttack[], downAttack[], leftAttack[], rightAttack[];
    Image upLeftAttack[], upRightAttack[], downLeftAttack[], downRightAttack[];
    //Setting the dead and healing animations of the character
    Image dead, healAnimation;
    //These are basically squares which are used in the testing phase to ensure that the bounds are working well
    Image boundsUpLeft, boundsUp, boundsUpRight;
    Image boundsLeft, boundsMiddle, boundsRight;
    Image boundsDownLeft,boundsDown, boundsDownRight;
    Sprite s_boundsUpLeft, s_boundsUp, s_boundsUpRight, s_boundsLeft, s_boundsMiddle, s_boundsRight, s_boundsDownLeft, s_boundsDown, s_boundsDownRight;
    //The images for the bullet
    Image bulletImage[], stunBulletImage;
    //The image used as animations for the light seeker ability
    Image lightSeekerImage, starExplosionImage;
    //The image used as animations for the flash ability
    Image flashImage;
    //The sprites for the main character, the flash image and the light seeker explosion
    //which should appear behind the character
    Sprite charSora, flash, starExplosion;
    //The sprite for the bullet
    CircleSprite bullet;
    
    //Keep the timers when paused, ready for unpausing
    void pause(){
        flashTimer = System.currentTimeMillis() - flashCooldownTimer;
        homeRunBlowTimer = System.currentTimeMillis() - homerunBlowCooldownTimer;
        lightSeekerTimer2 = System.currentTimeMillis() - lightSeekerCooldownTimer;
        healTimer = System.currentTimeMillis() - healCooldownTimer;
        stunBulletTimer = System.currentTimeMillis() - stunBulletCooldownTimer;
    }
    
    void unPause(){
        flashCooldownTimer = System.currentTimeMillis() - flashTimer;
        homerunBlowCooldownTimer = System.currentTimeMillis() - homeRunBlowTimer;
        lightSeekerCooldownTimer = System.currentTimeMillis() - lightSeekerTimer2;
        healCooldownTimer = System.currentTimeMillis() - healTimer;
        stunBulletCooldownTimer = System.currentTimeMillis() - stunBulletTimer;
    }
    
    //After an enemy is defeated the cooldowns must be reset
    void resetCooldowns(){
        flashCooldownTimer = System.currentTimeMillis() - 5000;
        homerunBlowCooldownTimer = System.currentTimeMillis() - 15000;
        stunBulletCooldownTimer = System.currentTimeMillis()- 20000;
        lightSeekerCooldownTimer = System.currentTimeMillis() - 31000;
        healCooldownTimer = System.currentTimeMillis()- 20000;
    }
    
    //The moving methods. Sora is the name of the character
    void moveSoraUp(){
        charSora.moveUp(speed);
        resetBounds();
    }
    
    void moveSoraLeft(){
        charSora.moveLeft(speed);
        resetBounds();
    }
    
    void moveSoraRight(){
        charSora.moveRight(speed);
        resetBounds();
    }
    
    void moveSoraDown(){
        charSora.moveDown(speed);
        resetBounds();
    }
    //This method ensures that the bounds are repositioned after moving the character
    //The "-10" are custom made for the case of the KHMoM algorythm. Change at preference
    void resetBounds(){
        s_boundsUpLeft.moveTo(charSora.getX(),charSora.getY()-10);
        s_boundsUp.moveTo(charSora.getX()+s_boundsUpLeft.getWidth(),charSora.getY()-10);
        s_boundsUpRight.moveTo(charSora.getX()+s_boundsUpLeft.getWidth()+s_boundsUp.getWidth(),charSora.getY()-10);
        s_boundsLeft.moveTo(charSora.getX(),charSora.getY()+s_boundsUpLeft.getHeight()-10);
        s_boundsMiddle.moveTo(charSora.getX()+s_boundsUpLeft.getWidth(),charSora.getY()+s_boundsUpLeft.getHeight()-10);
        s_boundsRight.moveTo(charSora.getX()+s_boundsUpLeft.getWidth()+s_boundsUp.getWidth(), charSora.getY()+s_boundsUpLeft.getHeight()-10);
        s_boundsDownLeft.moveTo(charSora.getX(),charSora.getY()+s_boundsUpLeft.getHeight()+s_boundsLeft.getHeight()-10);
        s_boundsDown.moveTo(charSora.getX()+s_boundsUpLeft.getWidth(), charSora.getY()+s_boundsUpLeft.getHeight()+s_boundsLeft.getHeight()-10);
        s_boundsDownRight.moveTo(charSora.getX()+s_boundsUpLeft.getWidth()+s_boundsUp.getWidth(),charSora.getY()+s_boundsUpLeft.getHeight()+s_boundsLeft.getHeight()-10);
    }
    //This is the method where the directions of the bullet are decided based on a coordinate set in the main program
    void setBulletDirections(int x, int y, int rangedStart, int noOfRangedItems, boolean owned[]){
        //here the program checks whether the user actualyl owns a ranged item
        //ranged start is the number where ranged items' position in the booleanOwned[] array
        boolean allow = false;
        for(int i = rangedStart;i<rangedStart+noOfRangedItems;i++){
            if(owned[i] == true){
                //If a ranged item is owned, set 'allow' to true
                allow = true;
            }
        }
        //This method does not run if the bulet is still travelling or the user has no ranged item
        if(bulletFired == false && allow == true){
            //The length between the points is calculated
            double hyp = Math.sqrt(Math.pow(x-bullet.midpointX,2)+Math.pow(y-bullet.midpointY,2));
            //The hyp is divided by the bullet speed and the answer is stored in dSpeed
            double dSpeed = hyp/bulletSpeed;
            //This is then used to divide the length between vertical and horizontal distances by it
            //to determine how many points the bullet must travel in each frame
            double xx = (x-bullet.midpointX)/dSpeed;
            double yy = (y-bullet.midpointY)/dSpeed;
            //The speed is then rounded to the nearest integer. This is unfortunate as the angles may not be perfect
            //and may miss if the target is very thin.
            if(xx<0){
                xx = -xx;
            }
            if(yy<0){
                yy = -yy;
            }
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
            //The direction of the bullet is Down and Right, therefore we ened to set the variables to negative
            //if they need to go Up or Left.
            if(x<=bullet.midpointX && y<=bullet.midpointY){
                bulletSpeedX = -(bulletSpeedX);
                bulletSpeedY = -(bulletSpeedY);
            }else if(x>=bullet.midpointX && y<=bullet.midpointY){
                bulletSpeedY = -(bulletSpeedY);
            }else if(x<=bullet.midpointX && y>=bullet.midpointY){
                bulletSpeedX = -(bulletSpeedX);
            }
            bulletFired = true;
        }
        //The stun bullet is reset if it was active
        if(stunBullet == true){
            stunBulletCooldownTimer = System.currentTimeMillis();
            bullet.setImage(stunBulletImage);
        }
    }
    //The method that controls the bullet's direction
    void moveBullet(int widthLimit, int heightLimit, Sprite other){
        //The bullet only moves if it is fired
        if(bulletFired == true){
            //If the bullet hits another sprite or the edge of the screen it is reset to it's original position
            if(bullet.midpointX-bullet.radius<5 || bullet.midpointX+bullet.radius>widthLimit || bullet.midpointY-bullet.radius<25 || bullet.midpointY+bullet.radius>heightLimit || bullet.hasCollidedWith(other)){
                bulletFired = false;
                stunBullet = false;
            }else{
                bullet.moveDown(bulletSpeedY);
                bullet.moveRight(bulletSpeedX);
            }
        }else{
            //The original position of the bullet in this case is In the middle of bounds middle,
            //which should be approximately in the belly of the character
            bullet.moveTo((s_boundsMiddle.getX()+(s_boundsMiddle.getWidth()/2)-bullet.radius),s_boundsMiddle.getY()+s_boundsMiddle.getHeight()/2-bullet.radius);
        }
    }
    //This is the overloaded method that doesn't include the Sprite collision
    void moveBullet(int widthLimit, int heightLimit){
        if(bulletFired == true){
            if(bullet.midpointX-bullet.radius<5 || bullet.midpointX+bullet.radius>widthLimit || bullet.midpointY-bullet.radius<25 || bullet.midpointY+bullet.radius>heightLimit){
                bulletFired = false;
                stunBullet = false;
            }else{
                bullet.moveDown(bulletSpeedY);
                bullet.moveRight(bulletSpeedX);
            }
        }else if(bulletFired == false){
            bullet.moveTo((s_boundsMiddle.getX()+(s_boundsMiddle.getWidth()/2)-bullet.radius),s_boundsMiddle.getY()+s_boundsMiddle.getHeight()/2-bullet.radius);
        }
    }
    //This method controls the duration of the flash animation
    void flashVisible(){
        if(System.currentTimeMillis() - flashCooldownTimer<800){
            flash.setVisible(true);
        }else{
            flash.setVisible(false);
        }
    }
    //Special abilities!
    //This ability teleports the character [flashRange] units to the direction the character is facing
    void flash(int widthLimit, int heightLimit){
        if(System.currentTimeMillis() - flashCooldownTimer > 4000){
            //moves the flash animation to the current position of the character and resets the cooldown
            flash.moveTo(s_boundsMiddle.getX()-3,s_boundsMiddle.getY()-3);
            flashCooldownTimer = System.currentTimeMillis();
            //Diagonal directions are calculated and the character is moved
            double xx = Math.sqrt(Math.pow(dashRange,2)/2);
            int diagonalDashRange;
            if((int)xx+0.5>xx){
                diagonalDashRange = (int)xx;
            }else{
                diagonalDashRange = (int)xx++;
            }
            switch(direction){
                case 1:
                    charSora.moveUp(dashRange);
                break;
                case 2:
                    charSora.moveLeft(dashRange);
                break;
                case 3:
                    charSora.moveDown(dashRange);
                break;
                case 4:
                    charSora.moveRight(dashRange);
                break;
                case 5:
                    charSora.moveUp(diagonalDashRange);
                    charSora.moveLeft(diagonalDashRange);
                break;
                case 6:
                    charSora.moveUp(diagonalDashRange);
                    charSora.moveRight(diagonalDashRange);
                break;
                case 7:
                    charSora.moveDown(diagonalDashRange);
                    charSora.moveLeft(diagonalDashRange);
                break;
                case 8:
                    charSora.moveDown(diagonalDashRange);
                    charSora.moveRight(diagonalDashRange);
                break;
            }
            //The character stops at the edge of the screen
            if(charSora.getY() <= 0){
                charSora.setY(0);
            }
            if(charSora.getX() <= 0){
                charSora.setX(0);
            }
            if(charSora.getY() >= heightLimit - charSora.getHeight()){
                charSora.setY(heightLimit - charSora.getHeight());
            }
            if(charSora.getX() >= widthLimit - charSora.getWidth()){
                charSora.setX(widthLimit - charSora.getWidth());
            }
            resetBounds();
        }
    }
    //The character jumps on top of the enemy and performs a number of attacks for 2 seconds while dealing
    //tons of damage and being invulnerable.
    void lightSeeker(Sprite other){
        if(lightSeeker == true){
            //Here the character is transported on top of the enemy and does an animation
            if(System.currentTimeMillis() - lightSeekerTimer < 2000){
                charSora.setImage(lightSeekerImage);
                charSora.moveTo(other.getX()+other.getWidth()/2 - 87, other.getY()+other.getHeight()/2 - 78);
                starExplosion.moveTo(other.getX()+other.getWidth()/2 - 315, other.getY()+other.getHeight()/2 - 180);
                starExplosion.setVisible(true);
                resetBounds();
            }else{
                //When light seeker ends
                lightSeeker = false;
                //The animation stops
                charSora.setImage(downRightStill);
                starExplosion.setVisible(false);
                resetBounds();
                //The damage is dealt. This boolean is turned off after damage is dealt in the main program
                lightSeekerHit = true;
            }
        }
    }
    //This ability basically makes your next melee attack knock back the enemy.
    void homerunBlowKnocback(Sprite other, int widthLimit,int heightLimit){
        //Diagonal distance is calculated
        double xx = Math.sqrt(Math.pow(homerunBlowDistance,2)/2);
        int diagonalDistance;
        if((int)xx+0.5>xx){
            diagonalDistance = (int)xx;
        }else{
            diagonalDistance = (int)xx++;
        }
        //The enemy is moved [homerunBlowDistance] units from his original position 
        //to the direction that the character is facing
        if(direction == 1){
            other.moveUp(homerunBlowDistance);
        }else if(direction == 2){
            other.moveLeft(homerunBlowDistance);
        }else if(direction == 3){
            other.moveDown(homerunBlowDistance);
        }else if(direction == 4){
            other.moveRight(homerunBlowDistance);
        }else if(direction == 5){
            other.moveUp(diagonalDistance);
            other.moveLeft(diagonalDistance);
        }else if(direction == 6){
            other.moveUp(diagonalDistance);
            other.moveRight(diagonalDistance);
        }else if(direction == 7){
            other.moveDown(diagonalDistance);
            other.moveLeft(diagonalDistance);
        }else if(direction == 8){
            other.moveDown(diagonalDistance);
            other.moveRight(diagonalDistance);
        }
        //The enemy stops at edges of the screen
        if(other.getY() <= 0){
            other.setY(0);
        }
        if(other.getX() <= 0){
            other.setX(0);
        }
        if(other.getY() >= heightLimit - other.getHeight()){
            other.setY(heightLimit - other.getHeight());
        }
        if(other.getX() >= widthLimit - other.getWidth()){
            other.setX(widthLimit - other.getWidth());
        }
    }
}