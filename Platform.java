/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Gra;

/**
 *
 * @author Maciek
 */
public class Platform {
    
    private int x, y;
    private int w, h;
    private int type;
    
     Platform()
    {
        x=0;
        y=0;
        
        w=0;
        h=0;
        type = 0;
    }
    
    Platform(int x, int y, int w, int h, int type)
    {
        this.x=x;
        this.y=y;
        
        this.w=w;
        this.h=h;
        this.type=type;
    }
    
    public int get_x()
    {
        return x;
    }
    
    public int get_y()
    {
        return y;
    }
    
    public int get_w()
    {
        return w;
    }
    
    public int get_h()
    {
        return h;
    }
    
    public int get_type()
    {
        return type;
    }
    
    public String get_everything()
    {
        String string=x + " " + y + " " + w + " " + h + " " + type;
        
        return string;
    }
    
    public boolean check_collision(int xP, int yP, int hP, int wP)
    {
        if(yP+hP>y && yP+hP<y+h && xP+wP>x && xP<x+w)
            return true;
        
        return false;
    }
    
    public void change_position(int x, int y)
    {
        this.x=x;
        this.y=y;
    }
    
    public void change_width(int w)
    {
        x-=w/2;
        this.w+=w;
        
    }
}