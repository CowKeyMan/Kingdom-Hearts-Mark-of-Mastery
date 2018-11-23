//Imported for use of buttons
import java.awt.*;
public class MeleeEnemy{
    //The enemy statistics
    int damage, enemySpeed, defaultEnemySpeed, enemySpeedX, enemySpeedY, hp, maxhp;
    //The images for the enemy moving
    Image moveUpLeft,moveDownLeft,moveUpRight,moveDownRight;
    //The images for the enemy standing still
    Image attackLeft,attackRight,sinkUpLeft;
    //The images for the enemy Being invulnerable
    Image sinkUpRight,sinkDownLeft,sinkDownRight;
    //The images for the bounds of the enemy
    Image boundsMiddleImage,boundsLeftImage,boundsRightImage;
    //The sprites for the enemy and the damaged stars 
    //that appear for a short amount of time after the enemy is damaged.
    Sprite enemy, damagedStars;
    //The sprites for the enemy's bounds
    Sprite boundsMiddle,boundsLeft,boundsRight;
    //The direction of the enemy when he is in the random direction state
    int normalDirection;
    //This is used so that the enemy may change it's direction of travel
    boolean firstTimeNormal = true;
    //State 3 is moving away from the bullet
    boolean firstTimeState3 = true;
    //This boolan is used so that when the enemy is attacking, the image changes to an attacking one
    boolean enemyHitting = false;
    //This is used as a random counter so when the enemy is moving around the character
    //the program can keep a track whether he's going left or right
    boolean clockwise;
    //The amount of time spent dodging the bullet
    long enemyState3Timer;
    //The timer that determines when the enemy can stop wheat he is doing
    //(example: moving towards char) and do something else(example: move around the char)
    long normalTimer;
    //This timer is used in the main program to determine when he starts changing his image
    //to an attacking one and when he can hit the character
    long hitTimer;
    //The duration of how long the damaged stars stay on screen
    long damagedStarsVisible;
    //The name of the enemy
    String name;
    //The background image
    Image background;
    
    //Start the damaged stars visible tiemr
    void damageStars(){
        damagedStarsVisible = System.currentTimeMillis();
    }
    //Show the damaged stars for a fifth of a second and then hide them again
    void showDamageStars(){
        if(System.currentTimeMillis() - damagedStarsVisible < 200){
            damagedStars.setVisible(true);
        }else{
            damagedStars.setVisible(false);
        }
    }
    //Set the bounds around the character
    void resetBounds(){
        boundsLeft.moveTo(enemy.getX(), enemy.getY());
        boundsRight.moveTo(enemy.getX()+enemy.getWidth()/2,enemy.getY());
        boundsMiddle.moveTo((enemy.getX()+(enemy.getWidth()/2)-(boundsMiddle.getWidth()/2)),enemy.getY()+enemy.getHeight()-boundsMiddle.getHeight());
        damagedStars.moveTo(boundsMiddle.getX(),boundsMiddle.getY());
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
            if(enemyHitting == false){
                enemy.setImage(moveUpLeft);
            }
            enemySpeedX = -(enemySpeedX);
            enemySpeedY = -(enemySpeedY);
        }else if(charMidPointX<=enemyMidPointX && charMidPointY>=enemyMidPointY){
            if(enemyHitting == false){
                enemy.setImage(moveDownLeft);
            }
            enemySpeedX = -(enemySpeedX);
        }else if(charMidPointX>=enemyMidPointX && charMidPointY<=enemyMidPointY){
            if(enemyHitting == false){
                enemy.setImage(moveUpRight);
            }
            enemySpeedY = -(enemySpeedY);
        }else if(charMidPointX>=enemyMidPointX && charMidPointY>=enemyMidPointY){
            if(enemyHitting == false){
                enemy.setImage(moveDownRight);
            }
        }
        //move down right
    }
    //set the enemy direction as perpendicular to the bullet
    void setEnemyAwayFromBullet(int bulletSpeedX, int bulletSpeedY){
        int i = (int)(1+Math.random()*4);
        if(i == 1){
            enemySpeedX = bulletSpeedY;
            enemySpeedY = -bulletSpeedX;
        }else if(i == 2){
            enemySpeedX = -bulletSpeedY;
            enemySpeedY = bulletSpeedX;
        }else{
        }
    }   
    //Set the speedX and speedY in a random diagonal direction(1 out of 4)
    //determined by a predetermined randomly generated number "nomralDirection"
    void moveNormal(Sprite other){
        //Here the speed of the horizontal distance is calculated
        double move = Math.round(Math.sqrt((Math.pow(enemySpeed,2)/2)));
        int mmove;
        if((int)move+0.5>move){
            mmove = ((int)move);
        }else{
            mmove = ((int)move+1);
        }
        //Up Left
        if(normalDirection == 1){
            if(enemyHitting == false){
                enemy.setImage(moveUpLeft);
            }
            enemySpeedY = -mmove;
            enemySpeedX = -mmove;
        }
        //Up Right
        else if(normalDirection == 2){
            if(enemyHitting == false){
                enemy.setImage(moveUpRight);
            }
            enemySpeedY = -mmove;
            enemySpeedX = mmove;
        }
        //Down Left
        else if(normalDirection == 3){
            if(enemyHitting == false){
                enemy.setImage(moveDownLeft);
            }
            enemySpeedY = mmove;
            enemySpeedX = -mmove;
        }
        //Down Right
        else if(normalDirection == 4){
            if(enemyHitting == false){
                enemy.setImage(moveDownRight);
            }
            enemySpeedY = mmove;
            enemySpeedX = mmove;
        }
    }
    //Move the enemy around the character
    //During this stage, the enemy is also invulnerable, hence the "sink" images
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
                    enemy.setImage(sinkUpRight);
                }
                enemySpeedX = mmove;
                enemySpeedY = -mmove;
            }else{
                if(enemyHitting == false){
                    enemy.setImage(sinkDownLeft);
                }
                enemySpeedX = -mmove;
                enemySpeedY = mmove;
            }
        }else if(charMidPointX<=enemyMidPointX && charMidPointY>=enemyMidPointY){
            if(clockwise == false){
                if(enemyHitting == false){
                    enemy.setImage(sinkUpLeft);
                }
                enemySpeedX = -mmove;
                enemySpeedY = -mmove;
            }else{
                if(enemyHitting == false){
                    enemy.setImage(sinkDownRight);
                }
                enemySpeedX = mmove;
                enemySpeedY = mmove;
            }
        }else if(charMidPointX>=enemyMidPointX && charMidPointY<=enemyMidPointY){
            if(clockwise == false){
                if(enemyHitting == false){
                    enemy.setImage(sinkDownRight);
                }
                enemySpeedX = mmove;
                enemySpeedY = mmove;
            }else{
                if(enemyHitting == false){
                    enemy.setImage(sinkUpLeft);
                }
                enemySpeedX = -mmove;
                enemySpeedY = -mmove;
            }
        }else if(charMidPointX>=enemyMidPointX && charMidPointY>=enemyMidPointY){
            if(clockwise == false){
                if(enemyHitting == false){
                    enemy.setImage(sinkDownLeft);
                }
                enemySpeedX = -mmove;
                enemySpeedY = mmove;
            }else{
                if(enemyHitting == false){
                    enemy.setImage(sinkUpRight);
                }
                enemySpeedX = mmove;
                enemySpeedY = -mmove;
            }
        }
    }
    //The main moving method
    void moveEnemy(Sprite other, int widthLimit, int heightLimit, int speed, int bulletSpeedX, int bulletSpeedY){
        //if the enemyState 3 Timer is active(because a bullet has just been shot),
        //set the enemy directions to dodge the bullet. In the main method it is
        //preferable to make this random whether he dodges it or not.
        if(System.currentTimeMillis()-enemyState3Timer<350){
            if(firstTimeState3 == true){
                setEnemyAwayFromBullet(bulletSpeedX, bulletSpeedY);
                firstTimeNormal = true;
                firstTimeState3 = false;
            }
        }else{
            //For 2.5 seconds, make the "move normal" method
            firstTimeState3 = true;
            if(System.currentTimeMillis()-normalTimer<2500){
                if(firstTimeNormal == true){
                    normalDirection = (int)(1+Math.random()*4);
                    firstTimeNormal = false;
                }
                moveNormal(other);
            }else{
                firstTimeNormal = true;
            }
            //for the next 2.5 seconds make the enemy move towards the character
            if(System.currentTimeMillis()-normalTimer>2500 && System.currentTimeMillis()-normalTimer<5000){
                enemySpeed += (int)(Math.random()*enemySpeed);
                setEnemyDirectionTowardsChar(other);
            }
            //For the next 2.5 seconds, make the enemy rotate around the character while being invulnerable
            //before starting the loop again.
            if(System.currentTimeMillis()-normalTimer>5000 && System.currentTimeMillis()-normalTimer<7500){
                int i = (int)(Math.random()*1);
                if(i == 0){
                    clockwise = true;
                }else{
                    clockwise = false;
                }
                moveAroundChar(other);
            }
            //start the loop again
            if(System.currentTimeMillis()-normalTimer>7500){
                normalTimer = System.currentTimeMillis();
            }
        }
        //Move the enemy
        enemy.moveRight(enemySpeedX);
        enemy.moveDown(enemySpeedY);
        //Set the enemy image to attacking if he is attacking
        if(enemyHitting == true){
            if((other.getX()+other.getWidth())/2 > (enemy.getX()+enemy.getWidth())/2){
                enemy.setImage(attackRight);
            }else{
                enemy.setImage(attackLeft);
            }
        }
        //Set the bounds for him
        if(boundsMiddle.getX()<3){
            enemy.setX(3);
        }
        if(boundsMiddle.getX() + boundsMiddle.getWidth()>widthLimit){
            enemy.setX(widthLimit - enemy.getWidth());
        }
        if(boundsMiddle.getY()<25){
            enemy.setY((25-enemy.getHeight())+boundsMiddle.getHeight());
        }
        if(boundsMiddle.getY() + boundsMiddle.getHeight()>heightLimit){
            enemy.setY(heightLimit - enemy.getHeight());
        }
        //Set the bounds to their set positions
        resetBounds();
    }
}