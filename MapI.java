/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Gra;

import java.util.HashSet;

/**
 *
 * @author Olek
 */
public class MapI {
   HashSet<Platform> platforms;
   public static int map_h;
   public static int map_w;
   MapI(){
       platforms = new HashSet();
       map_h = 700;
       map_w = 1800;
     
       platforms.add(new Platform(60, 500));
       platforms.add(new Platform(20, 550));
       platforms.add(new Platform(800, 500));
       platforms.add(new Platform(400, 550));
       platforms.add(new Platform(300, 480));
       platforms.add(new Platform(200, 410));
       platforms.add(new Platform(100, 430));
       platforms.add(new Platform(300, 450));
       platforms.add(new Platform(400, 600));
       platforms.add(new Platform(500, 650));
       platforms.add(new Platform(550, 550));
       platforms.add(new Platform(600, 450));
       platforms.add(new Platform(700, 450));
       platforms.add(new Platform(750, 400));
       platforms.add(new Platform(750, 360));
       platforms.add(new Platform(700, 320));
       platforms.add(new Platform(600, 280));
       platforms.add(new Platform(500, 240));
       platforms.add(new Platform(400, 200));
       platforms.add(new Platform(600, 160));
       platforms.add(new Platform(350, 120));
       platforms.add(new Platform(100, 70));
       
       
   }
   
   public void set(int i){
       switch(i){
           case 1: map1();break;
           case 2: map2();break;
       
   }
   }
   
   
   private void map1(){
       platforms.clear();
       platforms.add(new Platform(700, 280));
       platforms.add(new Platform(600, 240));
       platforms.add(new Platform(500, 200));
       platforms.add(new Platform(700, 160));
       platforms.add(new Platform(450, 120));
       platforms.add(new Platform(200, 70));
   }
   private void map2(){
       platforms.clear();
       platforms.add(new Platform(60, 500));
       platforms.add(new Platform(40, 550));
       platforms.add(new Platform(500, 500));
       platforms.add(new Platform(100, 550));
       platforms.add(new Platform(400, 480));
       platforms.add(new Platform(30, 410));
       
   }
   
   public int setBg_x(Player player){
       if(player.get_x()+player.get_w()/2<400)          return 400;
          else if (player.get_x()+player.get_w()/2>map_w-400)   return map_w-400;
          else     return player.get_x()+player.get_w()/2;
   }
   
   public int setBg_y(Player player){
       if(player.get_y()+player.get_h()/2<300) return 300;
       else if (player.get_y()+player.get_h()/2>map_h-300) return map_h-300;
       else return player.get_y()+player.get_h()/2;
   }
    
    
    
    
}
