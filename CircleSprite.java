//imported for using images
import java.awt.*;
public class CircleSprite{
    Image img;
    int radius;
    int midpointX, midpointY;
    boolean visible = true;
    //setting up the sprite
    public CircleSprite(Image i, int xCoordinate, int yCoordinate, int r){
        img = i;
        radius = r;
        midpointX = xCoordinate+r;
        midpointY = yCoordinate+r;
    }
    
    public CircleSprite(Image i, int r){
        img = i;
        radius = r;
    }
    //collision detection
    public boolean hasCollidedWith(Rectangle other){
        double a = Math.sqrt(Math.pow((other.x-midpointX),2)+Math.pow((other.y-midpointY),2));
        double b = Math.sqrt(Math.pow((other.x-midpointX),2)+Math.pow((other.y+other.height-midpointY),2));
        double c = Math.sqrt(Math.pow((other.x+other.width-midpointX),2)+Math.pow((other.y-midpointY),2));
        double d = Math.sqrt(Math.pow((other.x+other.width-midpointX),2)+Math.pow((other.y+other.height-midpointY),2));
        
        if(a<=radius || b<=radius || c<=radius || d<=radius){
            return true;
        }else if(other.x<=midpointX && other.x+other.width>=midpointX && other.y>=midpointY && other.y<=midpointY){
            return true;
        }else if(other.x<=midpointX+radius && other.x>=midpointX-radius && other.y>=midpointY-radius && other.y+other.getHeight()<=midpointY+radius){
            return true;
        }else if(other.x+other.getWidth()<=midpointX+radius && other.x+other.getWidth()>=midpointX-radius && other.y>=midpointY-radius && other.y+other.getHeight()<=midpointY+radius){
            return true;
        }else if(other.x<=midpointX-radius && other.x>=midpointX+radius && other.y>=midpointY-radius && other.y<=midpointY+radius){
            return true;
        }else if(other.x<=midpointX-radius && other.x>=midpointX+radius && other.y+other.getHeight()>=midpointY-radius && other.y+other.getHeight()<=midpointY+radius){
            return true;
        }else{
            return false;
        }
    }
    public boolean hasCollidedWith(Sprite other){
        double a = Math.sqrt(Math.pow((other.area.x-midpointX),2)+Math.pow((other.area.y-midpointY),2));
        double b = Math.sqrt(Math.pow((other.area.x-midpointX),2)+Math.pow((other.area.y+other.area.height-midpointY),2));
        double c = Math.sqrt(Math.pow((other.area.x+other.area.width-midpointX),2)+Math.pow((other.area.y-midpointY),2));
        double d = Math.sqrt(Math.pow((other.area.x+other.area.width-midpointX),2)+Math.pow((other.area.y+other.area.height-midpointY),2));
        if(a<=radius || b<=radius || c<=radius || d<=radius){
            return true;
        }else if(other.area.x<=midpointX && other.area.x+other.area.width>=midpointX && other.area.y>=midpointY && other.area.y<=midpointY){
            return true;
        }else if(other.area.x<=midpointX-radius && other.area.x+other.getWidth()>=midpointX+radius && other.area.y<=midpointY+radius && other.area.y+other.getHeight()>=midpointY-radius){
            return true;
        }else if(other.area.x+other.getWidth()<=midpointX+radius && other.area.x+other.getWidth()>=midpointX-radius && other.area.y<=midpointY-radius && other.area.y+other.getHeight()>=midpointY+radius){
            return true;
        }else if(other.area.x>=midpointX-radius && other.area.x<=midpointX+radius && other.area.y<=midpointY-radius && other.area.y+other.getHeight()>=midpointY+radius){
            return true;
        }else if(other.area.x<=midpointX-radius && other.area.x+other.getWidth()>=midpointX+radius && other.area.y+other.getHeight()<=midpointY+radius && other.area.y+other.getHeight()>=midpointY-radius){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean hasCollidedWith(CircleSprite other){
        int radii = this.radius+other.radius;
        if(midpointX == other.midpointX){
            if((radii - Math.abs(midpointY-other.midpointY))>0){
                return true;
            }else{
                return false;
            }
        }else if(midpointY == other.midpointY) {
            if((radii - Math.abs(midpointX-other.midpointX))>0){
                return true;
            }else{
                return false;
            }
        }else{
            if(radii -(Math.sqrt(Math.pow(midpointX-other.midpointX,2)+Math.pow(midpointY-other.midpointY,2)))  > 0){
                return true;
            }else{
                return false;
            }
        }
    }
    //decrease x to move to the left
    public void moveLeft(){
        midpointX -= 1;
    }
    //reduce x by number of pixels to move to left
    public void moveLeft(int px){
        midpointX -= px;
    }
    //increase x to move to right
    public void moveRight(){
        midpointX += 1;
    }
    //increase x by number of pixels to move to right
    public void moveRight(int px){
        midpointX += px;
    }
    //reduce y to move up
    public void moveUp(){
        midpointY -= 1;
    }
    //reduce y by a number of pixels to move up
    public void moveUp(int px){
        midpointY -= px;
    }
    //increase y to move down
    public void moveDown(){
        midpointY += 1;
    }
    //increase y by a number of pixels to move down
    public void moveDown(int px){
        midpointY += px;
    }
    
    //changes coordinates of image/char
    public void moveTo(int px, int py){
        midpointX = radius+px;
        midpointY = py+radius;
    }
    //This paint method is designed to adapt to situations where the collision place is not the whole sprite but rather a 
    //part of it
    public void paint(Graphics g, int distanceXFromMidpoint, int distanceYFromMidpoint){
        if(visible == true){
            g.drawImage(img, (int)(midpointX-distanceXFromMidpoint), (int)(midpointY-distanceYFromMidpoint), null);
        }
    }
    //the paint method to draw the image on screen
    public void paint(Graphics g){
        if(visible == true){
            g.drawImage(img, (int)(midpointX-radius), (int)(midpointY-radius), null);
        }
    }
    //@Override
    public void update(Graphics g){
       paint(g);
    }
    //Get the x co-ordinate 
    public int getX(){
        return midpointX-radius;
    }
    //Get the y co-ordinate 
    public int getY(){
        return midpointY-radius;
    }
    //Make the x co-ordinate go to set point
    public void setX(int px){
       midpointX = px+radius;
    }
    //Make the y co-ordinate go to set point
    public void setY(int py){
       midpointY = py+radius;
    }
    //getting the radius
    public int getRadius(){
        return radius;
    }
    //getting the diameter
    public int getDiameter(){
        return radius*2;
    }
    //getting the image
    public Image getImage(){
        return img;
    }
    //setting a new image without touching it's dimension
    public void setImage(Image newImage){
        img = newImage;
    }
    //changing an image and dimensions
    public void setImage(Image newImage, int r){
        img = newImage;   
        //changes size
        radius = r;
    }
    //set the image in/visible
    public void setVisible(boolean v){
        visible = v;
    }
}