/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Gra;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

/**
 *
 * @author Olek
 */
public class chat {
    
    JFrame frame;
    JTextField Chat;
    public void show(){
        frame.setVisible(true);
       
        //frame.setFocus(true);
    }
    public void hide(){
        frame.setVisible(false);
    }
    public void setText(String text){
        Chat.setText(text);
    }
    public void move(int x,int y){
        frame.setBounds(x, y, 800, 30);
    }
    public void setState(int i){
        frame.setState(i);
    }
    
    chat(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        Chat = new JTextField();
       // JPanel panel = new JPanel();
        frame.setBounds(100, 650, 800, 30);
        //panel.setBounds(0, 0, 800, 30);
        //chat.setBounds(0, 0, 800, 30);
       
       // panel.add(chat);
        frame.add(Chat);
        frame.setAlwaysOnTop(true);
        frame.setAutoRequestFocus(false);
        frame.setFocusable(false);
        Chat.setFocusable(false);
        //frame.pack();
       
       frame.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()){
                    case KeyEvent.VK_ESCAPE:    hide(); break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
       });
    }
    
    
}
