/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Gra;

import Gra.Network.MoveCharacter;

/**
 *
 * @author Maciek
 */
public class Player {

    private short x, vx = 3;     //definicje wspolrzednych, predkosci, szerokosci,
    private short y, vy = 3;    //wysokosci i licznika potrzebnego przy skakaniu
    private short w = 50, h = 74;
    private short counter;
    private short ch_x;
    private short ch_y;
    private boolean ready;  //gotowosc do skoku
    public boolean admin;

    Player() //konstruktory, uzywam drugiego
    {
        x = 0;
        y = 0;
        counter = 0;
        ready = true;
    }

    Player(short x, short y) {
        this.x = x;
        this.y = y;
        counter = 0;
        ready = true;
    }

    /**
     *
     * @param n
     */
    public MoveCharacter move(short n, boolean jump, boolean pCol) //funkcja ktora porusza playera
    {
        MoveCharacter msg = new MoveCharacter();
        short old_x = x;
        short old_y = y;
        msg.x = 0;
        msg.y = 0;

        if (jump && ready) //setka warunkow ktora pozwala nam skakac
        {                         //na okreslona wysokosc, jednoczesnie 
            y -= vy;                //nie pozwalajac na lewitacje. 
            counter += 1;          //Moze sam to ogarniesz, ale jak cos to pytaj
                                //W dodatku moze sie okazac, ze niektore 
            if (counter == 20) //warunki sie dubluja, ale to potem pomysle, 
            {                 //czy da sie to skrocic. Zamiast blokowac klawisz
                ready = false; //zrobilem blokade skakania podczas spadania
            }
        } else if (jump) {
            jump = false;

            if (check_collision_y() || pCol) {
                counter = 0;
                ready = true;
            }
        }

        switch (n) //tutaj poziomie poruszanie sie
        {
            case 0: {
                x -= vx;

                if (check_collision_x()) {
                    x += vx;
                }

            }
            break; //prawo
            case 1: {

                x += vx;

                if (check_collision_x()) {
                    x -= vx;
                }

            }
            break; //lewo

        }

        if (!(check_collision_y() || pCol) && !jump) //spadanie
        {
            y += vy;
            ready = false;
        }
        msg.x = (short) (old_x - x);
        msg.y = (short) (old_y - y);
        new_x();
        new_y();
        return msg;

    }

    public boolean check_collision_x() // nazwy mowia same za siebie,
    {                                 //true gdy nastapila kolizja
        if (x - vx < 0 || x + vx + w > MapI.map_w) {
            return true;
        }

        return false;
    }

    public boolean check_collision_y() {
        if (y + vy + h >= MapI.map_h) {
            return true;
        }

        return false;
    }

    private void new_x() {
        if (x + w / 2 < 400) {
            ch_x = x;
        } else if (x + w / 2 > MapI.map_w - 400) {
            ch_x = (short) (x - (MapI.map_w - 800));
        } else {
            ch_x = (short) (400 - w / 2);
        }
    }

    private void new_y() {
        if (y + h / 2 < 300) {
            ch_y = y;
        } else if (y + h / 2 > MapI.map_h - 300) {
            ch_y = (short) (y - (MapI.map_h - 600));
        } else {
            ch_y = (short) (300 - h / 2);
        }

    }

    public short get_ch_x() {
        return ch_x;
    }

    public short get_ch_y() {
        return ch_y;
    }

    public short get_x() {
        return x;
    }

    public short get_y() {
        return y;
    }

    public short get_h() {
        return h;
    }

    public short get_w() {
        return w;
    }

    public void set_x(short x) {
        this.x = x;
    }

    public void set_y(short y) {
        this.y = y;
    }
}