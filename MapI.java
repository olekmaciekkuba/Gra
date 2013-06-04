/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Gra;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author Olek
 */
public class MapI {
   HashSet<Platform> platforms;
   public static int map_h;
   public static int map_w;
   public static Image backgroundIm;
   
   MapI() {
       platforms = new HashSet();
       set("lvl1");
     
       
   }
   
   public void set(String name ) {
       try {
           File file = new File("levels/" + name +".txt");
           Scanner in = new Scanner(file);
            platforms.clear();
            String param[];
            int params[];
     
            String zdanie = in.nextLine();
            backgroundIm = new ImageIcon(zdanie).getImage();
            map_h = backgroundIm.getHeight(null);
            map_w = backgroundIm.getWidth(null);
            //System.out.println(zdanie);
            if(in.hasNextLine())
            {
                do
                {
                    zdanie = in.nextLine();
                    param = zdanie.split(" ");
                    in.hasNextLine();

                    params=new int[5];
                    for(int i=0; i<5; i++)
                    {
                        params[i] = Integer.parseInt(param[i]);
                        //System.out.println(params[i]);
                    }

                    platforms.add( new Platform(params[0], params[1], params[2], params[3], params[4]));
                }while(in.hasNextLine());
            }
       } catch (FileNotFoundException ex) {
           Logger.getLogger(MapI.class.getName()).log(Level.SEVERE, null, ex);
       }
       
       
     
   }
   
   
 
   public short setBg_x(Player player){
       if(player.get_x()+player.get_w()/2<400)          return 400;
          else if (player.get_x()+player.get_w()/2>map_w-400)   return (short)(map_w-400);
          else     return (short)(player.get_x()+player.get_w()/2);
   }
   
   public short setBg_y(Player player){
       if(player.get_y()+player.get_h()/2<300) return 300;
       else if (player.get_y()+player.get_h()/2>map_h-300) return (short)(map_h-300);
       else return (short)(player.get_y()+player.get_h()/2);
   }
    
    
    
    
}
