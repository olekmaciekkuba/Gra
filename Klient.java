package Gra;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import Gra.Network.*;
import com.esotericsoftware.minlog.Log;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Klient extends JFrame {

    private boolean klawisze[];
    private Timer zegar;
    private Timer hideChat;
    private Timer move;
    private Timer attack;
    private Timer checkAttack;
    private Timer newChat;
    private Timer immortal;
    
    private Image tlo;
    private Image platformIm;
    private Image life;
    private Image dead;
    private Image frag;
    private Image staty;
    
    private Image characters[][][];
    
    private UI ui;
    private Client client;
    private String name;
    private chat Chat;
    private boolean openChat = false;
    private String napis = "";
    private String[] czat;
    private Player player;
    private MapI map;
    private int klatka = 0;
    
    private boolean lastMove = true;
    
    class Immortal extends TimerTask{

        @Override
        public void run() {
            ui.characters.get(ui.ID).immortal = false;
        }
        
    }
    
    class NewChat extends TimerTask{

        @Override
        public void run() {
            openChat = false;
        }
        
    }

    class Move extends TimerTask {

        @Override
        public void run() {
            klatka++;
            if (klatka > 5) {
                klatka = 0;
            }
            player.set_b(klatka);
        }
    }

    class Zadanie extends TimerTask {

        boolean spr = false;
        boolean pCol;

        @Override
        public void run() {
            MoveCharacter msg = new MoveCharacter();

            for (Platform platform : map.platforms) {
                pCol = platform.check_collision(player.get_x(), player.get_y(), player.get_h(), player.get_w());
                if (pCol) {
                    break;
                }
            }

            if (klawisze[2]) {
                msg = player.move(0, klawisze[0], pCol);;
                spr = true;
            } else if (klawisze[3]) {
                msg = player.move(1, klawisze[0], pCol);;
                spr = true;
            } else {
                msg = player.move(3, klawisze[0], pCol);
                spr = true;
            }

            if (klawisze[5] && !openChat) {
                Chat.show();
                openChat = true;
                setVisible(true); //ustawia focus na okno glowne
            }
            if (klawisze[6]) {
                attack = new Timer();
                attack.schedule(new Move(), 150);
                
            }
            chooseImage(msg.x,klawisze[6]);

            if (spr) {
                msg.attack = klawisze[6];
                client.sendTCP(msg);
                spr = false;
                return;
            }

        }
    }

    class hide extends TimerTask {

        @Override
        public void run() {
            for (int i = 0; i < 6; i++) {
                if (czat[i] != null) {
                    czat[i] = null;
                    repaint();
                    break;
                }
            }
        }
    }

    //nie jest używana chyba teraz
    class atak extends TimerTask {

        @Override
        public void run() {
            Combat combat = new Combat();
            combat.attack = false;
            client.sendTCP(combat);
        }
    }

    class CheckAttack extends TimerTask{

        @Override
        public void run() {
            if(ui.characters.get(ui.ID).immortal) return;
            Iterator<Map.Entry<Integer, Character>> it = ui.characters.entrySet().iterator();
        
            while (it.hasNext()) {
                
                Character character = ui.characters.get(it.next().getKey());
                if(character.id == ui.ID) continue;
                if(!character.attack) continue;
                
                
                int width = characters[character.image-1][character.a][character.b%4].getWidth(null);
                int height = characters[character.image-1][character.a][character.b%4].getHeight(null);
                int playerWidth =characters[ui.characters.get(ui.ID).image-1][player.get_a()][player.get_b()%4].getWidth(null);
                
                    
                    if(character.a%2 == 0){
                        if(character.x + width > 30+player.get_x()+playerWidth/2-player.get_w()/2 && character.x+width<30+player.get_x()+player.get_w()+playerWidth/2-player.get_w()/2)
                            if(character.y+height/10>player.get_y() && character.y+height/2<player.get_y()+player.get_h())
                                ui.characters.get(ui.ID).hp--;
                        
                        
                    }else{
                        if(character.x > player.get_x()+playerWidth/2-player.get_w()/2 && character.x+width/2 - player.get_w()/2<player.get_x()+player.get_w()+playerWidth/2)
                            if(character.y+height/10>player.get_y() && character.y+height/2<player.get_y()+player.get_h())
                                ui.characters.get(ui.ID).hp--;
                    }
                    if( ui.characters.get(ui.ID).hp == 0){
                        dead(character);
                        ui.characters.get(ui.ID).immortal = true;
                        immortal = new Timer();
                        immortal.schedule(new Immortal(), 15000);
                        
                    }
                    
                }

            }

        
        
    }
    
    public Klient() {
        super("kolko");

        map = new MapI();
        hideChat = new Timer();
        client = new Client();
        client.start();
        Network.register(client);
        player = new Player(320, 500);
        client.addListener(new ThreadedListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                JOptionPane.showMessageDialog(null, "Połączono z serwerem.", null, JOptionPane.INFORMATION_MESSAGE);
            }

            @Override
            public void received(Connection connection, Object object) {

                if (object instanceof AddCharacter) {
                    AddCharacter msg = (AddCharacter) object;
                    ui.addCharacter(msg.character);

                    return;
                }
                if (object instanceof CharacterID) {
                    CharacterID msg = (CharacterID) object;
                    ui.setCharacterID(msg);
                    player.admin = msg.admin;
                    return;
                }

                if (object instanceof UpdateCharacter) {
                    ui.updateCharacter((UpdateCharacter) object);
                    repaint();
                    return;
                }

                if (object instanceof RemoveCharacter) {
                    RemoveCharacter msg = (RemoveCharacter) object;
                    ui.removeCharacter(msg.id);
                    repaint();
                    return;
                }
                if (object instanceof NewPosition){
                    ui.newPosition((NewPosition)object);
                    return;
                }
                if (object instanceof SendChat) {

                    hideChat.cancel();
                    hideChat.purge();
                    hideChat = new Timer();

                    SendChat info = (SendChat) object;
                    SetChat(info.napis);

                    hideChat.scheduleAtFixedRate(new hide(), 10000, 10000);


                    repaint();
                    return;
                }
                if (object instanceof SetMap) {

                    SetMap info = (SetMap) object;
                    map.set(info.id);
                    return;
                }
                if (object instanceof Kick){
                    Kick kick = (Kick) object;
                    
                    if(kick.name.equals(name)){
                        setAlwaysOnTop(false);
                        JOptionPane.showMessageDialog(null, "Utracono połączenie z serwerem.", "KICK!", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }
                }
            }

            @Override
            public void disconnected(Connection connection) {
                setAlwaysOnTop(false);
                JOptionPane.showMessageDialog(null, "Utracono połączenie z serwerem.", "Błąd", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }));

        ui = new UI();
        String host = ui.inputHost();
        try {
            client.connect(5000, host, Network.port, Network.port + 2);
        } catch (IOException ex) {
            System.out.println("Connection error");
            JOptionPane.showMessageDialog(null, "Nie można się połączyć z serwerem", "Connection error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        name = ui.inputName();
        Register register = new Register();
        register.name = name;
        register.image = ui.chooseCharacter();
        client.sendTCP(register);
        setBounds(100, 50, 800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        setResizable(false);
        setVisible(true);
        setAlwaysOnTop(true);
        createBufferStrategy(2);
        setFocusTraversalKeysEnabled(false);
        Chat = new chat();




        klawisze = new boolean[7];

        loadImage();
        
        
        
        tlo = new ImageIcon("images/tlo.jpg").getImage();
        life = new ImageIcon("images/life.png").getImage();
        platformIm = new ImageIcon("images/platform.jpg").getImage();
        dead = new ImageIcon("images/dead.png").getImage();
        frag = new ImageIcon("images/frag.png").getImage();
        staty = new ImageIcon("images/staty.png").getImage();

        czat = new String[6];

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                Rectangle rct = getBounds();
                Chat.move(rct.x, rct.y + 600);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowDeiconified(java.awt.event.WindowEvent evt) {         //przywrocenie okna
                Chat.setState(Frame.NORMAL);
                setVisible(true); //przywrócenie focusa na okno glowne
            }

            public void windowIconified(java.awt.event.WindowEvent evt) {           // minimalizacja okna
                Chat.setState(Frame.ICONIFIED);
            }
        });

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (openChat) {
                    if ((e.getKeyCode() > 43 && e.getKeyCode() < 94) || e.getKeyCode() == KeyEvent.VK_SPACE) {
                        napis += e.getKeyChar();
                        Chat.setText(napis);
                    }

                    if (e.getKeyCode() == KeyEvent.VK_ENTER && napis != "") {

                        if (napis.startsWith(".") && player.admin) {
                            commands(napis);
                            napis = "";
                            Chat.setText("");
                            return;
                        }
                        napis = name + ": " + napis;
                        SendChat info = new SendChat();
                        info.napis = napis;
                        client.sendTCP(info);
                        napis = "";
                        Chat.setText(napis);
                        Chat.hide();
                        newChat = new Timer();
                        newChat.schedule(new NewChat(), 300);
                    }
                    if (e.getKeyCode() == 8) { //backspace
                        if (napis.length() > 0) {
                            napis = napis.substring(0, napis.length() - 1);
                            Chat.setText(napis);
                        }
                    }
                }
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        klawisze[0] = true;
                        break;
                    case KeyEvent.VK_DOWN:
                        klawisze[1] = true;
                        break;
                    case KeyEvent.VK_LEFT:
                        klawisze[2] = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        klawisze[3] = true;
                        break;
                    case KeyEvent.VK_TAB:
                        klawisze[4] = true;
                        break;
                    case KeyEvent.VK_ENTER:
                        klawisze[5] = true;
                        break;
                    case KeyEvent.VK_ESCAPE:
                        Chat.hide();
                        napis = "";
                        openChat = false;
                        break;
                    case KeyEvent.VK_SPACE:
                        klawisze[6] = true;
                        break;

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        klawisze[0] = false;
                        break;
                    case KeyEvent.VK_DOWN:
                        klawisze[1] = false;
                        break;
                    case KeyEvent.VK_LEFT:
                        klawisze[2] = false;
                        break;
                    case KeyEvent.VK_RIGHT:
                        klawisze[3] = false;
                        break;
                    case KeyEvent.VK_TAB:
                        klawisze[4] = false;
                        break;
                    case KeyEvent.VK_ENTER:
                        klawisze[5] = false;
                        break;
                    case KeyEvent.VK_SPACE:
                        klawisze[6] = false;
                        break;
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        });

        zegar = new Timer();
        zegar.scheduleAtFixedRate(new Zadanie(), 0, 40);
        move = new Timer();
        move.scheduleAtFixedRate(new Move(), 0, 300);
        
        checkAttack = new Timer();
        checkAttack.scheduleAtFixedRate(new CheckAttack(), 0, 300);
        repaint();
    }

    class UI {

        HashMap<Integer, Character> characters = new HashMap();
        int ID;

        /**
         * Otworzenie okna wyboru serwera.
         *
         * @return zwraca wybrany adres
         */
        public String inputHost() {

            ServerList lista = new ServerList();
            lista.setVisible(true);
            lista.setBounds(600, 300, 375, 230);

            String input = lista.abc.getString();

            System.out.println(input);
            if (input == null || input.length() == 0) {
                System.exit(1);
            }

            return input;
        }

        /**
         * Wczytanie nicku postaci.
         *
         * @return zwrot nicku malymi literami
         */
        public String inputName() {
            String input = (String) JOptionPane.showInputDialog(null, "Nick:", "Connect to server", JOptionPane.QUESTION_MESSAGE,
                    null, null, "Wpisz imię");
            if (input == null || input.trim().length() == 0) {
                System.exit(1);
            }
            return input.trim();
        }

        /**
         * Otworzenie okna wyboru grafiki postaci.
         *
         * @return zwraca numer wygladu postaci
         */
        public int chooseCharacter() {

            chooseCharacter okno = new chooseCharacter();
            okno.setBounds(600, 300, 220, 200);
            okno.setVisible(true);
            int input = okno.wybierz.get();
            if (input == 0) {
                System.exit(1);
            }
            return input;

        }

        /**
         * Dodanie postaci do kontenera
         *
         * @param character odebrana postac
         */
        public void addCharacter(Character character) {
            characters.put(character.id, character);

            System.out.println(character.name + " added at " + character.x + ", " + character.y);
        }

        /**
         * Ustawienie id postaci
         *
         * @param msg odebrana wiadomosc z ID
         */
        public void setCharacterID(CharacterID msg) {
            ID = msg.id;
        }

        /**
         * Uaktualnienie pozycji postaci.
         *
         * @param msg Dane do uaktualnienia
         */
        public void updateCharacter(UpdateCharacter msg) {
            Character character = characters.get(msg.id);
            if (character == null) {
                return;
            }
            character.x = msg.x;
            character.y = msg.y;
            character.a = msg.a;
            character.b = msg.b;
            character.attack = msg.attack;
            character.dead = msg.dead;
            character.frags = msg.frags;
            
        }
        
        
        public void newPosition(NewPosition msg){
            player.set_x(msg.x);
            player.set_y(msg.y);
            characters.get(ID).hp = msg.hp;
        }

        /**
         * Usuniecie postaci z kontenera.
         *
         * @param id id postaci do usuniecia
         */
        public void removeCharacter(int id) {
            Character character = characters.remove(id);
            if (character != null) {
                System.out.println(character.name + " removed");
            }
        }
    }

    public static void main(String[] args) {
        Log.set(Log.LEVEL_DEBUG);
        new Klient();
    }

    /**
     * Nadaje efekt przewijania chatu.
     *
     * @param napis nowy napis dodawany na koniec tablicy
     */
    private void SetChat(String napis) {

        for (int i = 0; i < 5; i++) {
            czat[i] = czat[i + 1];
        }
        czat[5] = napis;

    }

    private void commands(String napis) {

        if (napis.contains(".setMap")) {
            napis = napis.replace(".setMap ", "");

            int i;
            i = Integer.decode(napis);
            SetMap setMap = new SetMap();
            setMap.id = i;

            client.sendTCP(setMap);
            return;
        }
        if (napis.contains(".kick")){
            napis = napis.replace(".kick ", "");
            Kick kick = new Kick();
            kick.name = napis;
            client.sendTCP(kick);
           // System.out.println(napis);
            return;
        }

    }
    private void dead(Character character){
        Dead dead = new Dead();
        dead.characterID = character.id;
        client.sendTCP(dead);
        
    }

    public void chooseImage(int x,boolean attack) {
        
        
        if(x>0) lastMove = true;
        else if(x<0) lastMove = false;
        

//        if (i == 1) {
            if (lastMove) {

                if (attack) {
                    player.set_a(4);
                } else if (x!=0) {
                    player.set_a(2);
                } else {
                    player.set_a(0);
                }
            } else {

                if (attack) {
                    player.set_a(5);
                } else if (x!=0) {
                    player.set_a(3);
                } else {
                    player.set_a(1);
                }
            }
//        } else if (i == 2) {
//            image = kolko2;
//        } else {
//            image = null;
//        }

    }
    /**
     * 
     *  pierwszy index odpowiada za numer postaci  
     *  drugi index rodzaj ruchu
     *   0 - stoi prawo
     *   1 - stoi lewo
     *   2 - ruch w prawo
     *   3 - ruch w lewo
     *   4 - atak prawo
     *   5 - atak w lewo
     *  trzeci index nr klatki 
     * 
     */
    
    void loadImage(){
        
        characters = new Image[2][][];                  
        characters[0] = new Image[6][]; 
        characters[1] = new Image[6][];
        
        for(int i=0;i<4;i++){
            characters[0][i] = new Image[4];
            characters[1][i] = new Image[4];
        }
        for(int i=4;i<6;i++){
            characters[0][i] = new Image[4];
            characters[1][i] = new Image[6];
        }
        
        characters[0][0][0] = new ImageIcon("images/character1/bezruch_r/1.png").getImage();
        characters[0][0][1] = new ImageIcon("images/character1/bezruch_r/2.png").getImage();
        characters[0][0][2] = new ImageIcon("images/character1/bezruch_r/3.png").getImage();
        characters[0][0][3] = new ImageIcon("images/character1/bezruch_r/4.png").getImage();

        characters[0][1][0] = new ImageIcon("images/character1/bezruch_l/1.png").getImage();
        characters[0][1][1] = new ImageIcon("images/character1/bezruch_l/2.png").getImage();
        characters[0][1][2] = new ImageIcon("images/character1/bezruch_l/3.png").getImage();
        characters[0][1][3] = new ImageIcon("images/character1/bezruch_l/4.png").getImage();

        characters[0][2][0] = new ImageIcon("images/character1/ruch_r/1.png").getImage();
        characters[0][2][1] = new ImageIcon("images/character1/ruch_r/2.png").getImage();
        characters[0][2][2] = new ImageIcon("images/character1/ruch_r/3.png").getImage();
        characters[0][2][3] = new ImageIcon("images/character1/ruch_r/4.png").getImage();

        characters[0][3][0] = new ImageIcon("images/character1/ruch_l/1.png").getImage();
        characters[0][3][1] = new ImageIcon("images/character1/ruch_l/2.png").getImage();
        characters[0][3][2] = new ImageIcon("images/character1/ruch_l/3.png").getImage();
        characters[0][3][3] = new ImageIcon("images/character1/ruch_l/4.png").getImage();

        characters[0][4][0] = new ImageIcon("images/character1/atak_r/1.png").getImage();
        characters[0][4][1] = new ImageIcon("images/character1/atak_r/2.png").getImage();
        characters[0][4][2] = new ImageIcon("images/character1/atak_r/3.png").getImage();
        characters[0][4][3] = new ImageIcon("images/character1/atak_r/4.png").getImage();

        characters[0][5][0] = new ImageIcon("images/character1/atak_l/1.png").getImage();
        characters[0][5][1] = new ImageIcon("images/character1/atak_l/2.png").getImage();
        characters[0][5][2] = new ImageIcon("images/character1/atak_l/3.png").getImage();
        characters[0][5][3] = new ImageIcon("images/character1/atak_l/4.png").getImage();

        characters[1][0][0] = new ImageIcon("images/character2/bezruch_r/1.png").getImage();
        characters[1][0][1] = new ImageIcon("images/character2/bezruch_r/2.png").getImage();
        characters[1][0][2] = new ImageIcon("images/character2/bezruch_r/3.png").getImage();
        characters[1][0][3] = new ImageIcon("images/character2/bezruch_r/4.png").getImage();

        characters[1][1][0] = new ImageIcon("images/character2/bezruch_l/1.png").getImage();
        characters[1][1][1] = new ImageIcon("images/character2/bezruch_l/2.png").getImage();
        characters[1][1][2] = new ImageIcon("images/character2/bezruch_l/3.png").getImage();
        characters[1][1][3] = new ImageIcon("images/character2/bezruch_l/4.png").getImage();

        characters[1][2][0] = new ImageIcon("images/character2/ruch_r/1.png").getImage();
        characters[1][2][1] = new ImageIcon("images/character2/ruch_r/2.png").getImage();
        characters[1][2][2] = new ImageIcon("images/character2/ruch_r/3.png").getImage();
        characters[1][2][3] = new ImageIcon("images/character2/ruch_r/4.png").getImage();

        characters[1][3][0] = new ImageIcon("images/character2/ruch_l/1.png").getImage();
        characters[1][3][1] = new ImageIcon("images/character2/ruch_l/2.png").getImage();
        characters[1][3][2] = new ImageIcon("images/character2/ruch_l/3.png").getImage();
        characters[1][3][3] = new ImageIcon("images/character2/ruch_l/4.png").getImage();

        characters[1][4][0] = new ImageIcon("images/character2/atak_r/1.png").getImage();
        characters[1][4][1] = new ImageIcon("images/character2/atak_r/2.png").getImage();
        characters[1][4][2] = new ImageIcon("images/character2/atak_r/3.png").getImage();
        characters[1][4][3] = new ImageIcon("images/character2/atak_r/4.png").getImage();
        characters[1][4][4] = new ImageIcon("images/character2/atak_r/5.png").getImage();
        characters[1][4][5] = new ImageIcon("images/character2/atak_r/6.png").getImage();

        characters[1][5][0] = new ImageIcon("images/character2/atak_l/1.png").getImage();
        characters[1][5][1] = new ImageIcon("images/character2/atak_l/2.png").getImage();
        characters[1][5][2] = new ImageIcon("images/character2/atak_l/3.png").getImage();
        characters[1][5][3] = new ImageIcon("images/character2/atak_l/4.png").getImage();
        characters[1][5][4] = new ImageIcon("images/character2/atak_l/5.png").getImage();
        characters[1][5][5] = new ImageIcon("images/character2/atak_l/6.png").getImage();
       
        
    }
    

    /**
     * Rysowanie
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {

        BufferStrategy bstrategy = this.getBufferStrategy();
        Graphics2D g2d;
        int ch_x;
        int ch_y;


        try {
            g2d = (Graphics2D) bstrategy.getDrawGraphics();
        } catch (NullPointerException e) {
            return;
        }
        if(player == null) return;

        //Wyswietlanie tla

        int bg_x = map.setBg_x(player);
        int bg_y = map.setBg_y(player);

        g2d.drawImage(tlo, 0, 0, 800, 600, bg_x - 400, bg_y - 300, bg_x + 400, bg_y + 300, null);
        
        //Wyswietlanie platform
        for (Platform platform : map.platforms) {
            g2d.drawImage(platformIm, player.get_ch_x() - (player.get_x() - platform.get_x()), player.get_ch_y() - (player.get_y() - platform.get_y()), null);
        }
        Image postac = null;
        //Wyswietlenie wszystkich postaci
        Iterator<Map.Entry<Integer, Character>> it = ui.characters.entrySet().iterator();
        
        while (it.hasNext()) {

            Character character = ui.characters.get(it.next().getKey());
            int klatka;
            if(character.image == 2 && character.attack)
                klatka = character.b%6;
            else
                klatka = character.b%4;
            
            try{
                postac = characters[character.image-1][character.a][klatka];
            }
            catch (ArrayIndexOutOfBoundsException e){
                postac = characters[character.image-1][character.a][character.b%4];
            }
            catch (NullPointerException e){
                return;
            }
            if(postac == null) return;
            
            if (character.x == player.get_x() && character.y == player.get_y()) {
                ch_x = player.get_ch_x();
                ch_y = player.get_ch_y();

            } else {
                ch_x = player.get_ch_x() - (player.get_x() - character.x);
                ch_y = player.get_ch_y() - (player.get_y() - character.y);
            }

            g2d.drawImage(postac, ch_x-(postac.getWidth(null)/2-player.get_w()/2), ch_y, null);
        }

        try{
            for(int i=0;
                i<ui.characters.get(ui.ID).hp;i++){
            g2d.drawImage(life,i*25,30,null);
        }
        }
        catch (NullPointerException e){
            return;
        }
      
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.setPaint(Color.BLUE);

        

        if (czat != null) {
            for (int i = 0; i < 6; i++) {
                if (czat[i] != null) {
                    g2d.drawString(czat[i], 10, 500 + i * 15);
                }
            }
        }
        
        g2d.drawImage(frag, 600, 25, null);
        g2d.drawImage(dead, 700, 25, null);
        
        g2d.setFont(new Font("Arial", Font.BOLD, 25));
        g2d.setPaint(Color.black);
        g2d.drawString(Integer.toString(ui.characters.get(ui.ID).frags),660,50);
        g2d.setPaint(Color.RED);
        g2d.drawString(Integer.toString(ui.characters.get(ui.ID).dead),740,50);
     
        if(klawisze[4]){
            g2d.drawImage(staty, 0, 0, null);
             Iterator<Map.Entry<Integer, Character>> it2 = ui.characters.entrySet().iterator();
             g2d.drawString("ID", 150, 150);
             g2d.drawString("nick", 250, 150);
             g2d.drawString("kills", 450, 150);
             g2d.drawString("deaths", 600, 150);
             int i = 1;
             g2d.setFont(new Font("Arial", Font.BOLD, 18));
             g2d.setPaint(Color.black);
            while (it2.hasNext()) {

                Character character = ui.characters.get(it2.next().getKey());
                g2d.drawString(Integer.toString(i), 150, 160+30*i);
                g2d.drawString(character.name, 250, 160+30*i);
                g2d.drawString(Integer.toString(character.frags), 450, 160+30*i);
                g2d.drawString(Integer.toString(character.dead), 600, 160+30*i);
                i++;
                

            }
            
        }
        
        
        g2d.dispose();

        bstrategy.show();


    }
}
