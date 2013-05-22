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
    private int w;
    
    Platform()
    {
        x=0;
        y=0;
        
        w=0;
    }
    
    Platform(int x1, int y1)
    {
        x=x1;
        y=y1;
        
        w=211;
    }
    
    public int get_x()
    {
        return x;
    }
    
    public int get_y()
    {
        return y;
    }
    
    public boolean check_collision(int xP, int yP, int hP, int wP)
    {
        if(yP+hP>y && yP+hP<y+5 && xP+wP>x && xP<x+w)
            return true;
        
        return false;
    }
}
