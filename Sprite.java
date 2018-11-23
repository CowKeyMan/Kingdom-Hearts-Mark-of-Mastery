//imported to use images
import java.awt.*;
public class Sprite{
    // Rectangle class whichholds width height and coordinates
    Rectangle area;
    Image img;
    boolean visible = true;
    
    //gets image from main program (constructor)
    public  Sprite(Image i){
        //i = image from program
        img = i;
        //img.getWidth/Height Returns width/height of image
        area = new Rectangle (1,1,img.getWidth(null),img.getHeight(null));
    }
    
    //overloaded constructor to give coordinates
    public Sprite (Image i, int x, int y){
        img = i;
        area = new Rectangle (x, y, img.getWidth(null),img.getHeight(null));
    }
    
    public Sprite (Image i, int x, int y,int width, int height){
        img = i;
        area = new Rectangle (x, y, width, height);
    }
    
    //reduce x to move to left
    public void moveLeft(){
        area.x -= 1;
    }
    //reduce x by number of pixels to move to left
    public void moveLeft(int px){
        area.x -= px;
    }
    //increase x to move to right
    public void moveRight(){
        area.x += 1;
    }
    //increase x by number of pixels to move to right
    public void moveRight(int px){
        area.x += px;
    }
    //reduce y to move up
    public void moveUp(){
        area.y -= 1;
    }
    //reduce y by a number of pixels to move up
    public void moveUp(int px){
        area.y -= px;
    }
    //increase y to move down
    public void moveDown(){
        area.y += 1;
    }
    //increase y by a number of pixels to move down
    public void moveDown(int px){
        area.y += px;
    }
    
    //changes coordinates of image/char
    public void moveTo(int x, int y){
        area.x = x;
        area.y = y;
    }
    
    //draws image/char on screen
    public void paint(Graphics g){
        if(visible == true){
            g.drawImage(img, area.x, area.y, null);
        }
    }
    //draws image in case of bounds for  collision the rectangle is moved
    public void paint(Graphics g, int x, int y){
        if(visible == true){
            g.drawImage(img, area.x - x, area.y - y, null);
        }
    }
    //@Override
    public void update(Graphics g){
       paint(g);
    }
    //collision detection checks if areas overlap if collides returns true else false
    public boolean hasCollidedWith(Sprite other){
        return this.area.intersects (other.area);
    }
    
    public boolean hasCollidedWith(Rectangle rect){
        return this.area.intersects (rect);
    }
    
    public boolean hasCollidedWith(CircleSprite circ){
        if(circ.hasCollidedWith(area)){
            return true;
        }else{
            return false;
        }
    }
    
    public int getX(){
        return area.x;
    }
    
    public int getY(){
        return area.y;
    }
    
    public void setX(int x){
       area.x = x;
    }
    
    public void setY(int y){
       area.y = y;
    }
    
    public int getWidth(){
        return area.width;
    }
    
    public int getHeight(){
        return area.height;
    }
    
    public Image getImage(){
        return img;
    }
    public void setImage(Image newImage){
        img = newImage;  
        //keep old values
        int x = area.x;
        int y = area.y;
    }
    //changes image of sprite
    public void setImage2(Image newImage){
        img = newImage;  
        //keep old values
        int x = area.x;
        int y = area.y;
        //changes size
        area = new Rectangle (x, y, img.getWidth(null), img.getHeight(null) );
    }
    //changes image of sprite and give it it's new width and height
    public void setImage(Image newImage, int width, int height){
        img = newImage;   
        //keep old values
        int x = area.x;
        int y = area.y;
        //changes size
        area = new Rectangle (x, y, width, height);
    }
    //makes sprite invisible/visible
    public void setVisible(boolean v){
        visible = v;
    }
}