//Imported to use images
import java.awt.*;
public class RangedEnemy{
    //The images for the enemy
    Image leftMove, rightMove;
    Image leftAttack, rightAttack;
    //The image for the bullet/s
    Image bulletImage;
    //The image for the bounds
    Image boundsMiddleImage;
    //The main and bounds middle sprites
    Sprite enemy, boundsMiddle;
    //The sprite that is displayed for a short time after the enemy is damaged
    Sprite damagedStars;
    //The sprite for the bullets
    CircleSprite bullet[];
    //The stats which have to do with the character
    int speed, speedX, speedY, bulletSpeed, bulletSpeedX[], bulletSpeedY[], noOfBullets, bulletDamage, hp, maxhp;
    //The boolean which is true when the bullet is travvelling
    //and false when it is still inside the enemy
    boolean bulletFired[];
    //the boolean which determines when the enemy can change the direction of his movement
    //The direction is changed if the enemy hits a wall or the timer for it is activated
    boolean canChangeDirection = true;
    long directionChangeTimer;
    boolean canResetTimer = true;
    //The timer for when the character can shoot his bullet
    boolean firstTime = true;
    long firstTimeShootingTimer;
    //The duration of the attack animation
    long attackAnimationTimer;
    //The timer for each bullet determining whether it can be fired or not
    long shootBulletTimer[];
    //The duration that the damaged stars are displayed on screen
    long damagedStarsVisible;
    //The background image
    Image background;
    
    //Move the bounds and damaged stars to the same position as the enemy sprite
    void resetBounds(int x, int y){
        boundsMiddle.moveTo(enemy.getX() + x, enemy.getY() + y);
        damagedStars.moveTo(boundsMiddle.getX() - 17,boundsMiddle.getY() - 10);
    }
    //When the enemy is hit, reset this timer to show the damaged stars
    void damageStars(){
        damagedStarsVisible = System.currentTimeMillis();
    }
    //Show the damaged stars depending on the above timr
    void showDamageStars(){
        if(System.currentTimeMillis() - damagedStarsVisible < 200){
            damagedStars.setVisible(true);
        }else{
            damagedStars.setVisible(false);
        }
    }
    //Set all the bullet Fired to false(made in the beginning of the program)
    void setBulletFiredFalse(){
        for(int i = 0; i<bulletFired.length;i++){
            bulletFired[i] = false;
        }
    }
    //Setting the bullet speeds 
    void moveNormal(int widthLimit, int heightLimit){
        //calculating the diagonal direction speed
        double move = Math.sqrt((Math.pow(speed,2)/2));
        int mmove;
        int normalDirection = (int)(1+Math.random()*8);
        if((int)move+0.5>move){
            mmove = ((int)move);
        }else{
            mmove = ((int)move+1);
        }
        //The direction chosen is random. 1 from 8
        //Up Left
        if(normalDirection == 1){
            if(enemy.getX()>5 && enemy.getY()>27){
                speedY = -mmove;
                speedX = -mmove;
            }
        }
        //Up Right
        else if(normalDirection == 2){
            if(enemy.getX()+enemy.getWidth()<widthLimit && enemy.getY()>27){
                speedY = -mmove;
                speedX = mmove;
            }
        }
        //Down Left
        else if(normalDirection == 3){
            if(enemy.getX()>5 && enemy.getY()+enemy.getHeight()<heightLimit){
                speedY = mmove;
                speedX = -mmove;
            }
        }
        //Down Right
        else if(normalDirection == 4){
            if(enemy.getX()+enemy.getWidth()<widthLimit && enemy.getY()+enemy.getHeight()<heightLimit){
                speedY = mmove;
                speedX = mmove;
            }
        }
        //Right
        else if(normalDirection == 5){
            if(enemy.getX()+enemy.getWidth()<widthLimit){
                speedY = 0;
                speedX = speed;
            }
        }
        //Left
        else if(normalDirection == 6){
            if(enemy.getX()>5){
                speedY = 0;
                speedX = -(speed);
            }
        }
        //Up
        else if(normalDirection == 7){
            if(enemy.getY()>27){
                speedY = -(speed);
                speedX = 0;
            }
        }
        //Down
        else if(normalDirection == 8){
            if(enemy.getY()+enemy.getHeight()<heightLimit){
                speedY = speed;
                speedX = 0;
            }
        }
        //Setting the bounds
        if(boundsMiddle.getX()<3){
            enemy.moveRight(Math.abs(speed));
        }
        if(boundsMiddle.getX() + boundsMiddle.getWidth()>widthLimit){
            enemy.moveLeft(Math.abs(speed));
        }
        if(boundsMiddle.getY()<25){
            enemy.moveDown(Math.abs(speed));
        }
        if(boundsMiddle.getY() + boundsMiddle.getHeight()>heightLimit){
            enemy.moveUp(speed);
        }
    }
    //This is the method where the directions of the bullet are decided based on a coordinate set in the main program
    void setBulletDirections(Sprite other, int k, int x, int y){
        //This method does not run if the bullet is still travelling or the bullet's timer hasn't finished yet
        if(bulletFired[k] == false && System.currentTimeMillis() - shootBulletTimer[k]>1200 * (k+2)){
            resetBounds(x,y);
            //Set the bullet to be in the middle of the enemy
            //The "+17+ part is custom for the KH:MoM game
            bullet[k].midpointX = boundsMiddle.getX() + boundsMiddle.getWidth()/2+17;
            bullet[k].midpointY = boundsMiddle.getY() + boundsMiddle.getHeight()/2+17;
            //The length between the points is calculated
            double hyp = Math.sqrt(Math.pow(other.getX()+other.getWidth()/2-bullet[k].midpointX,2)+Math.pow(other.getY()+other.getHeight()/2-bullet[k].midpointY,2));
            //The hyp is divided by the bullet speed and the answer is stored in dSpeed
            double dSpeed = hyp/bulletSpeed;
            //This is then used to divide the length between vertical and horizontal distances by it
            //to determine how many points the bullet must travel in each frame
            double xx = (other.getX()+other.getWidth()/2-bullet[k].midpointX)/dSpeed;
            double yy = (other.getY()+other.getHeight()/2-bullet[k].midpointY)/dSpeed;
            //The speed is then rounded to the nearest integer. This is unfortunate as the angles may not be perfect
            //and may miss if the target is very thin.
            if(xx<0){
                xx = -xx;
            }
            if(yy<0){
                yy = -yy;
            }
            
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
            //The direction of the bullet is Down and Right, therefore we ened to set the variables to negative
            //if they need to go Up or Left.
            if(other.getX()+other.getWidth()/2<=bullet[k].midpointX && other.getY()+other.getHeight()/2<=bullet[k].midpointY){
                bulletSpeedX[k] = -(bulletSpeedX[k]);
                bulletSpeedY[k] = -(bulletSpeedY[k]);
            }else if(other.getX()+other.getWidth()/2>=bullet[k].midpointX && other.getY()+other.getHeight()/2<=bullet[k].midpointY){
                bulletSpeedY[k] = -(bulletSpeedY[k]);
            }else if(other.getX()+other.getWidth()/2<=bullet[k].midpointX && other.getY()+other.getHeight()/2>=bullet[k].midpointY){
                bulletSpeedX[k] = -(bulletSpeedX[k]);
            }
            //Set the bullet travelling to true and can reset timer to true
            bulletFired[k] = true;
            canResetTimer = true;
            //The animation of the enemy attacking starts
            attackAnimationTimer = System.currentTimeMillis();
        }
    }
    //The method to moving the bullet
    void moveBullet(int widthLimit, int heightLimit, Sprite other, int k){
        //if the bullet is in 'travelling mode', move it
        if(bulletFired[k] == true){
            //The bullet is set as 'not travelling' if it hits the character or the edge of the screen
            //and the shooting timer is reset
            if(bullet[k].midpointX-bullet[k].radius<0 || bullet[k].midpointX+bullet[k].radius>widthLimit || bullet[k].midpointY-bullet[k].radius<20 || bullet[k].midpointY+bullet[k].radius>heightLimit || bullet[k].hasCollidedWith(other)){
                bulletFired[k] = false;
                shootBulletTimer[k] = System.currentTimeMillis();
            }
            //Else, move the bullet
            else{
                bullet[k].moveDown(bulletSpeedY[k]);
                bullet[k].moveRight(bulletSpeedX[k]);
            }
        }
        //If the bullet iss not in 'travelling mode', set it to be in the enemy
        else if(bulletFired[k] == false){
            bullet[k].moveTo((enemy.getX()+enemy.getWidth())/2+bullet[k].radius,(enemy.getY()+enemy.getHeight())/2+bullet[k].radius);
        }
    }
    //The AI of how the enemy moves
    void move(int widthLimit, int heightLimit, Sprite other, int x, int y){
        //Every fixed time, the enemy switches it's direction and at times it also stops temporarily
        if(canChangeDirection){
            moveNormal(widthLimit, heightLimit);
            canChangeDirection = false;
            if(canResetTimer){
                directionChangeTimer = System.currentTimeMillis();
                canResetTimer = false;
            }
        }
        //The enemy moves 
        if(canChangeDirection == false){
            enemy.moveRight(speedX);
            enemy.moveDown(speedY);
        }
        //If the enemy hits a wall, he changes his direction of travel, but not reset the timer of moving/stopping
        if(enemy.getX()<5 || enemy.getY()<27 || enemy.getX()+enemy.getWidth()>widthLimit || enemy.getY()+enemy.getHeight()>heightLimit){
            canChangeDirection = true;
        }
        //Here is the part where the enemy stops temporarily
        if(System.currentTimeMillis() - directionChangeTimer>2500){
            speedX = 0;
            speedY = 0;
        }
        //After an amount of time, the direction changes automatically, after the stop
        if(System.currentTimeMillis() - directionChangeTimer>4000){
            canChangeDirection = true;
            canResetTimer = true;
        }
        //When the enemy attacks he has an animation. Further more, the enemy always faces
        //the direction that the character is in.
        if(System.currentTimeMillis() - attackAnimationTimer < 490){
            if(enemy.getX()+enemy.getWidth()/2>other.getX()+other.getWidth()/2){
                enemy.setImage(leftAttack);
            }else{
                enemy.setImage(rightAttack);
            }
        }else{ 
            if(enemy.getX()+enemy.getWidth()/2>other.getX()+other.getWidth()/2){
                enemy.setImage(leftMove);
            }else{
                enemy.setImage(rightMove);
            }
        }
        resetBounds(x,y);
    }
    //The method to set the bullet directions and then leave the rest to the other move bullet
    //to actualy move the bullet
    void moveBullet(int widthLimit, int heightLimit, Sprite other, int x, int y){
        if(firstTime == true){
            //Here the timer starts for the bullets to start shooting
            firstTimeShootingTimer = System.currentTimeMillis();
            firstTime = false;
        }
        for(int i = 0;i<noOfBullets;i++){
            //After waiting a bit, start shooting the bullets
            if(System.currentTimeMillis() - firstTimeShootingTimer > i*1300){
                setBulletDirections(other, i, x, y);
            }else{
                //If the time to start shooting hasn't come up yet, set the bullets positions in the enmy
                bullet[i].midpointX = enemy.getX() + enemy.getWidth()/2+17;
                bullet[i].midpointY = enemy.getY() + enemy.getHeight()/2+17;
            }
        }
        //Move the bullet
        for(int i = 0;i<noOfBullets;i++){
            moveBullet(widthLimit, heightLimit, other, i);
        }
    }
}