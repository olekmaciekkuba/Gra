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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Klient extends JFrame {

    private boolean klawisze[];
    private boolean mysz[];
    private Timer zegar;
    private Timer hideChat;
    private Timer move;
    private Timer attack;
    private Timer checkAttack;
    private Timer newChat;
    private Timer immortal;
    private Timer combat;
    private Timer chckMouse;
    private Image bgMenu;
    private Image platformIm;
    private Image life;
    private Image dead;
    private Image frag;
    private Image staty;
    private Image characters[][][];
    private UI ui;
    private Client client;
    private chat Chat;
    private boolean openChat = false;
    private String name = "";
    private String napis = "";
    private String[] czat;
    private String[] nameList;
    private String[] adresy;
    private String ip = "";
    private String password = "";
    private Player player;
    private MapI map;
    private int klatka = 0;
    private int klatkaCombat = 0;
    private boolean lastMove = true;
    private choose setName;
    private choose chckRegister;
    private boolean chckReg = false;
    private boolean menu = true;
    private MouseListener mouseListener;
    private KeyListener keyListener;
    private ThreadedListener threadedListener;
    private int menuPhase = 0;
    private Point point;
    private Register register;
    private int phase = 0;
    public List<InetAddress> address;
    // 0 - OK
    // 1 - Cancel
    // 2 - start
    // 3 - exit
    // 4 - WAN
    // 5 - LAN
    // 6 - Powrót
    private boolean clicked[];

    class ChckMouse extends TimerTask {

        @Override
        public void run() {

            if (mysz[0]) {
                mysz[0] = false;

                if (menuPhase == 0) {
                    Rectangle rctStart = new Rectangle(150, 230, 100, 20);

                    if (rctStart.contains(point)) {
                        menuPhase = 1;
                        repaint();
                        //  init();
                        return;


                    }
                    Rectangle rctExit = new Rectangle(150, 278, 70, 20);
                    if (rctExit.contains(point)) {
                        System.exit(0);

                    }
                }

                if (menuPhase == 1) {
                    Rectangle rctLAN = new Rectangle(150, 230, 60, 20);
                    if (rctLAN.contains(point)) {
                        menuPhase = 7;
                        repaint();
                        address = client.discoverHosts(Network.port + 2, 5000);
                        if (address == null) {
                            return;
                        }



                        String bufor = address.toString(); // format Stringa np. [/127.0.0.1, /127.0.0.1, /192.168.1.101]

                        char[] buf = bufor.toCharArray();

                        short a = 2;
                        short index = 0;

                        adresy = new String[address.size()];

                        for (short i = 0; i < address.size(); i++) {
                            adresy[i] = "";
                        }

                        while (buf[a] != ']') {
                            if (buf[a] == ',') {
                                a += 3;
                                index++;
                                continue;
                            }

                            adresy[index] += buf[a];
                            a++;
                        }
                        menuPhase = 2;

                        repaint();
                        return;
                    }

                    Rectangle rctWAN = new Rectangle(150, 278, 70, 20);
                    if (rctWAN.contains(point)) {
                        menuPhase = 3;
                        repaint();
                        return;
                    }

                    Rectangle rctReturn = new Rectangle(150, 330, 135, 20);

                    if (rctReturn.contains(point)) {
                        menuPhase = 0;
                        repaint();
                        return;
                    }
                }
                if (menuPhase == 2) {
                    Rectangle rctAnuluj = new Rectangle(150, 275, 90, 20);
                    if (rctAnuluj.contains(point)) {
                        menuPhase = 1;
                        ip = "";
                        repaint();
                        return;
                    }

                    Rectangle rctOK = new Rectangle(300, 275, 40, 20);
                    if (rctOK.contains(point)) {


                        client.start();
                        Network.register(client);
                        client.addListener(threadedListener);
                        ui = new UI();

                        try {
                            client.connect(5000, ip, Network.port, Network.port + 2);
                            menuPhase = 4;
                        } catch (IOException ex) {
                            System.out.println("Connection error");
                            JOptionPane.showMessageDialog(null, "Nie można się połączyć z serwerem", "Connection error", JOptionPane.ERROR_MESSAGE);
                            menuPhase = 2;

                        }


                        ip = "";
                        repaint();
                        return;
                    }
                    Rectangle rctList = new Rectangle(450, 225, 150, 30 * adresy.length - 15);
                    if (rctList.contains(point)) {
                        int y = point.y - 225;
                        y /= 30;
                        ip = adresy[y];
                        client.start();
                        Network.register(client);
                        client.addListener(threadedListener);
                        ui = new UI();

                        try {
                            client.connect(5000, ip, Network.port, Network.port + 2);
                            menuPhase = 4;
                        } catch (IOException ex) {
                            System.out.println("Connection error");
                            JOptionPane.showMessageDialog(null, "Nie można się połączyć z serwerem", "Connection error", JOptionPane.ERROR_MESSAGE);
                            menuPhase = 2;

                        }


                        ip = "";
                        repaint();
                    }
                    repaint();
                    return;
                }
                if (menuPhase == 3) {
                    Rectangle rctAnuluj = new Rectangle(150, 275, 90, 20);
                    if (rctAnuluj.contains(point)) {
                        menuPhase = 1;
                        ip = "";
                        repaint();
                        return;
                    }

                    Rectangle rctOK = new Rectangle(300, 275, 40, 20);
                    if (rctOK.contains(point)) {

                        client = new Client();
                        client.start();
                        Network.register(client);
                        client.addListener(threadedListener);
                        ui = new UI();

                        try {
                            client.connect(5000, ip, Network.port, Network.port + 2);
                            menuPhase = 4;
                        } catch (IOException ex) {
                            System.out.println("Connection error");
                            JOptionPane.showMessageDialog(null, "Nie można się połączyć z serwerem", "Connection error", JOptionPane.ERROR_MESSAGE);
                            menuPhase = 2;

                        }


                        ip = "";
                        repaint();
                        return;
                    }

                }
                if (menuPhase == 4) {
                    Rectangle rctAnuluj = new Rectangle(150, 275, 90, 20);
                    if (rctAnuluj.contains(point)) {
                        menuPhase = 1;
                        name = "";
                        repaint();
                        return;
                    }

                    Rectangle rctOK = new Rectangle(300, 275, 40, 20);
                    if (rctOK.contains(point)) {

                        client.sendTCP(new GetNameList());
                        setName.czekaj();
                        if (chckName(nameList, name)) {
                            name = "";
                        } else {
                            register.name = name;
                            menuPhase = 5;
                        }
                        repaint();
                        return;
                    }
                }
                if (menuPhase == 5) {
                    Rectangle rctAnuluj = new Rectangle(150, 275, 90, 20);
                    if (rctAnuluj.contains(point)) {
                        menuPhase = 1;
                        password = "";
                        repaint();
                        return;
                    }

                    Rectangle rctOK = new Rectangle(300, 275, 40, 20);
                    if (rctOK.contains(point)) {
                        menuPhase = 6;
                        register.password = password;
                        repaint();
                        return;
                    }
                }
                if (menuPhase == 6) {
                    Rectangle char1 = new Rectangle(300, 250, characters[0][0][0].getWidth(null), characters[0][0][0].getHeight(null));
                    Rectangle char2 = new Rectangle(500, 250, characters[1][1][0].getWidth(null), characters[1][1][0].getHeight(null));

                    if (char1.contains(point)) {
                        register.image = 1;
                    } else if (char2.contains(point)) {
                        register.image = 2;
                    } else {
                        return;
                    }

                    client.sendTCP(register);
                    chckRegister.czekaj();
                    if (!chckReg) {
                        menuPhase = 4;
                    } else {
                        menu = false;
                        init();

                    }
                    return;

                }


            }

        }
    }

    class ImmortalTimer extends TimerTask {

        @Override
        public void run() {
            ui.characters.get(ui.ID).immortal = false;
            Immortal immortal = new Immortal();
            immortal.id = ui.ID;
            immortal.immortal = false;

            client.sendTCP(immortal);
        }
    }

    class NewChat extends TimerTask {

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

        }
    }

    class Zadanie extends TimerTask {

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
            int old_x = player.get_x();
            if (klawisze[2]) {
                msg = player.move((short) 0, klawisze[0], pCol);

            } else if (klawisze[3]) {
                msg = player.move((short) 1, klawisze[0], pCol);

            } else {
                msg = player.move((short) 3, klawisze[0], pCol);

            }

            if (klawisze[5] && !openChat) {
                Chat.show();
                openChat = true;
                setVisible(true); //ustawia focus na okno glowne
            }
            chooseImage(player.get_x() - old_x, klawisze[6]);


            msg.attack = klawisze[6];
            client.sendTCP(msg);

            return;


        }
    }

    class hide extends TimerTask {

        @Override
        public void run() {
            for (short i = 0; i < 6; i++) {
                if (czat[i] != null) {
                    czat[i] = null;
                    repaint();
                    break;
                }
            }
        }
    }

    class atak extends TimerTask {

        @Override
        public void run() {
            klatkaCombat++;
            if (klatka > 5) {
                klatka = 0;
            }

        }
    }

    class CheckAttack extends TimerTask {

        @Override
        public void run() {
            if (ui.characters.get(ui.ID).immortal) {
                return;
            }
            Iterator<Map.Entry<Short, Character>> it = ui.characters.entrySet().iterator();

            while (it.hasNext()) {

                Character character = ui.characters.get(it.next().getKey());
                if (character.id == ui.ID) {
                    continue;
                }
                if (!character.attack) {
                    continue;
                }


                short width = (short) characters[character.image - 1][character.a][klatka % 4].getWidth(null);
                short height = (short) characters[character.image - 1][character.a][klatka % 4].getHeight(null);
                short playerWidth = (short) characters[ui.characters.get(ui.ID).image - 1][ui.characters.get(ui.ID).a][klatka % 4].getWidth(null);


                if (character.a % 2 == 0) {
                    if (character.x + width > 30 + player.get_x() + playerWidth / 2 - player.get_w() / 2 && character.x + width < 30 + player.get_x() + player.get_w() + playerWidth / 2 - player.get_w() / 2) {
                        if (character.y + height / 10 > player.get_y() && character.y + height / 2 < player.get_y() + player.get_h()) {
                            ui.characters.get(ui.ID).hp--;
                        }
                    }


                } else {
                    if (character.x > player.get_x() + playerWidth / 2 - player.get_w() / 2 && character.x + width / 2 - player.get_w() / 2 < player.get_x() + player.get_w() + playerWidth / 2) {
                        if (character.y + height / 10 > player.get_y() && character.y + height / 2 < player.get_y() + player.get_h()) {
                            ui.characters.get(ui.ID).hp--;
                        }
                    }
                }
                if (ui.characters.get(ui.ID).hp == 0) {
                    dead(character);
                    ui.characters.get(ui.ID).immortal = true;
                    immortal = new Timer();
                    immortal.schedule(new ImmortalTimer(), 5000);
                    Immortal immortal = new Immortal();
                    immortal.id = ui.ID;
                    immortal.immortal = true;
                    client.sendTCP(immortal);

                }

            }

        }
    }

    Klient() {
        super("Gra");
        setBounds(100, 50, 800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

        createBufferStrategy(2);
        setFocusTraversalKeysEnabled(false);
        loadImage();
        repaint();

        client = new Client();
        setName = new choose();
        register = new Register();
        chckRegister = new choose();
        player = new Player((short) 320, (short) 500);
        mysz = new boolean[2];
        clicked = new boolean[7];
        for (int i = 0; i < 7; i++) {
            clicked[i] = false;
        }

        this.addMouseListener(mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getButton() == 1) {
                    mysz[0] = true;
                    point = e.getPoint();
                    System.out.println(point);
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        chckMouse = new Timer();
        chckMouse.scheduleAtFixedRate(new ChckMouse(), 0, 100);

        this.addKeyListener(keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // System.out.println(e.getKeyChar());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    ip = "";
                    name = "";
                    repaint();
                    return;
                }
                if (menuPhase == 3 || menuPhase == 2) {
                    if ((e.getKeyCode() > 43 && e.getKeyCode() < 94)) {
                        ip += e.getKeyChar();
                        repaint();
                    }
                } else {
                    ip = "";
                }
                if (menuPhase == 4) {
                    if ((e.getKeyCode() > 43 && e.getKeyCode() < 94)) {
                        name += e.getKeyChar();
                        repaint();
                    }
                } else {
                    name = "";
                }
                if (menuPhase == 5) {
                    if ((e.getKeyCode() > 43 && e.getKeyCode() < 94)) {

                        password += e.getKeyChar();
                        repaint();
                    }
                } else {
                    password = "";
                }


            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });


        threadedListener = new ThreadedListener(new Listener() {
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
                    ui.setCharacterID(msg.id);
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
                if (object instanceof NewPosition) {
                    ui.newPosition((NewPosition) object);
                    return;
                }
                if (object instanceof Dead) {
                    ui.setDead((Dead) object);
                    return;
                }
                if (object instanceof Frag) {
                    ui.setFrag((Frag) object);
                    return;
                }
                if (object instanceof Klatka) {
                    try {
                        ui.setFrame((Klatka) object);
                    } catch (NullPointerException e) {
                    }

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
                    map.set(info.name);
                    return;
                }
                if (object instanceof Immortal) {

                    ui.immortal((Immortal) object);
                    return;
                }

                if (object instanceof Kick) {
                    Kick kick = (Kick) object;

                    if (kick.name.equals(name)) {
                        setAlwaysOnTop(false);
                        JOptionPane.showMessageDialog(null, "Utracono połączenie z serwerem.", "KICK!", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }
                    return;
                }
                if (object instanceof SetGM) {
                    SetGM setGM = (SetGM) object;
                    if (setGM.id == ui.ID) {
                        player.admin = true;
                    }
                    return;
                }
                if (object instanceof NameList) {
                    NameList list = (NameList) object;
                    nameList = list.name;

                    setName.kontynuuj();
                    return;
                }
                if (object instanceof RegisterOK) {
                    chckReg = true;
                    chckRegister.kontynuuj();
                    return;
                }
                if (object instanceof RegisterAgain) {
                    chckRegister.kontynuuj();
                    return;
                }

            }

            @Override
            public void disconnected(Connection connection) {
                setAlwaysOnTop(false);
                JOptionPane.showMessageDialog(null, "Utracono połączenie z serwerem.", "Błąd", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        });

    }

    public void init() {
        // super("Gra");

        //  menu = false;
        map = new MapI();
        hideChat = new Timer();
        player = new Player((short) 320, (short) 500);
        this.removeKeyListener(keyListener);
        this.removeMouseListener(mouseListener);

        Chat = new chat();




        klawisze = new boolean[7];

        //loadImage();

        czat = new String[6];

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                Rectangle rct = getBounds();
                Chat.move((short) rct.x, (short) (rct.y + 600));
            }
        });
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
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
        setAlwaysOnTop(true);
        zegar = new Timer();
        zegar.scheduleAtFixedRate(new Zadanie(), 0, 40);
        move = new Timer();
        move.scheduleAtFixedRate(new Move(), 0, 300);

        checkAttack = new Timer();
        checkAttack.scheduleAtFixedRate(new CheckAttack(), 0, 300);

        combat = new Timer();
        combat.schedule(new atak(), 0, 50);
        repaint();
        this.removeMouseListener(mouseListener);
        menu = false;
    }

    class UI {

        HashMap<Short, Character> characters = new HashMap();
        short ID;

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

        public String inputPassword() {
            String input = (String) JOptionPane.showInputDialog(null, "hasło:", "Connect to server", JOptionPane.QUESTION_MESSAGE,
                    null, null, "Wpisz hasło");
            if (input == null || input.trim().length() == 0) {
                System.exit(1);
            }
            return input.trim();

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
        public short chooseCharacter() {

            chooseCharacter okno = new chooseCharacter();
            okno.setBounds(600, 300, 220, 200);
            okno.setVisible(true);
            short input = okno.wybierz.get();
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

            //System.out.println(character.name + " added at " + character.x + ", " + character.y);
        }

        /**
         * Ustawienie id postaci
         *
         * @param msg odebrana wiadomosc z ID
         */
        public void setCharacterID(short id) {
            ID = id;
        }

        public void setDead(Dead msg) {
            characters.get(msg.characterID).dead++;
        }

        public void setFrag(Frag msg) {
            characters.get(msg.id).frags++;
        }

        public void setFrame(Klatka msg) {
            characters.get(msg.id).a = msg.a;
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
            character.attack = msg.attack;

        }

        /**
         * Ustawia nowa pozycje postaci
         *
         * @param msg - zawiera informacje o nowej pozycji oraz o ilosci hp
         */
        public void newPosition(NewPosition msg) {
            player.set_x(msg.x);
            player.set_y(msg.y);
            characters.get(ID).hp = msg.hp;
        }

        public void immortal(Immortal msg) {
            Character character = characters.get(msg.id);
            if (character == null) {
                return;
            }
            character.immortal = msg.immortal;
        }

        /**
         * Usuniecie postaci z kontenera.
         *
         * @param id id postaci do usuniecia
         */
        public void removeCharacter(short id) {
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

    public void register() {
        client.sendTCP(new GetNameList());
        setName.czekaj();
        do {
            name = ui.inputName();

        } while (chckName(nameList, name));



        Register register = new Register();
        register.password = ui.inputPassword();
        register.name = name;
        register.image = ui.chooseCharacter();
        client.sendTCP(register);

    }

    public boolean chckName(String[] nameList, String name) {
        for (int i = 0; i < nameList.length; i++) {
            if (nameList[i].equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Nadaje efekt przewijania chatu.
     *
     * @param napis nowy napis dodawany na koniec tablicy
     */
    private void SetChat(String napis) {

        for (short i = 0; i < 5; i++) {
            czat[i] = czat[i + 1];
        }
        czat[5] = napis;

    }

    private void dead(Character character) {
        Dead dead = new Dead();
        dead.characterID = character.id;
        client.sendUDP(dead);

    }

    public void chooseImage(int x, boolean attack) {


        if (x > 0) {
            lastMove = true;
        } else if (x < 0) {
            lastMove = false;
        }

        short a;
        if (lastMove) {

            if (attack) {
                a = ((short) 4);
            } else if (x != 0) {
                a = ((short) 2);
            } else {
                a = ((short) 0);
            }
        } else {

            if (attack) {
                a = ((short) 5);
            } else if (x != 0) {
                a = ((short) 3);
            } else {
                a = ((short) 1);
            }
        }

        Klatka klatka = new Klatka();
        klatka.a = a;
        klatka.id = ui.ID;
        client.sendUDP(klatka);
    }

    /**
     *
     * pierwszy index odpowiada za numer postaci drugi index rodzaj ruchu 0 -
     * stoi prawo 1 - stoi lewo 2 - ruch w prawo 3 - ruch w lewo 4 - atak prawo
     * 5 - atak w lewo trzeci index nr klatki
     *
     */
    void loadImage() {


        bgMenu = new ImageIcon("images/background/menu.jpg").getImage();
        life = new ImageIcon("images/UI/life.png").getImage();
        platformIm = new ImageIcon("images/platforms/platform.jpg").getImage();
        dead = new ImageIcon("images/UI/dead.png").getImage();
        frag = new ImageIcon("images/UI/frag.png").getImage();
        staty = new ImageIcon("images/UI/staty.png").getImage();


        characters = new Image[3][][];
        characters[0] = new Image[6][];
        characters[1] = new Image[6][];
        characters[2] = new Image[1][];
        characters[2][0] = new Image[1];
        characters[2][0][0] = new ImageIcon("images/blink.png").getImage();

        for (short i = 0; i < 4; i++) {
            characters[0][i] = new Image[4];
            characters[1][i] = new Image[4];
        }
        for (short i = 4; i < 6; i++) {
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
     * wyświetlenie tła
     *
     * @param g2d
     */
    public void bgShow(Graphics2D g2d) {
        //Wyswietlanie tla

        short bg_x = map.setBg_x(player);
        short bg_y = map.setBg_y(player);

        g2d.drawImage(MapI.backgroundIm, 0, 0, 800, 600, bg_x - 400, bg_y - 300, bg_x + 400, bg_y + 300, null);

    }

    /**
     * wyświetlenie platform
     *
     * @param g2d
     */
    public void platformShow(Graphics2D g2d) {
        //Wyswietlanie platform
        for (Platform platform : map.platforms) {
            g2d.drawImage(platformIm, player.get_ch_x() - (player.get_x() - platform.get_x()), player.get_ch_y() - (player.get_y() - platform.get_y()), platform.get_w(),
                    platform.get_h(), null);
        }
    }

    /**
     * wyświetlenie wszystkich postaci
     *
     * @param g2d
     */
    public void characterShow(Graphics2D g2d) {
        Image postac = null;
        //Wyswietlenie wszystkich postaci
        Iterator<Map.Entry<Short, Character>> it = ui.characters.entrySet().iterator();

        while (it.hasNext()) {
            short ch_x;
            short ch_y;
            Character character = ui.characters.get(it.next().getKey());
            int klatka;
            if (character.attack) {
                klatka = klatkaCombat;
            } else {
                klatka = this.klatka;
            }

            if (character.image == 2 && character.attack) {
                klatka %= 6;
            } else {
                klatka %= 4;
            }

            try {
                if (character.immortal && klatka % 2 == 0) {
                    postac = characters[2][0][0];
                } else {
                    postac = characters[character.image - 1][character.a][klatka];
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                postac = characters[character.image - 1][character.a][this.klatka % 4];
            } catch (NullPointerException e) {
                return;
            }
            if (postac == null) {
                return;
            }

            if (character.id == ui.ID) {
                ch_x = player.get_ch_x();
                ch_y = player.get_ch_y();


            } else {
                ch_x = (short) (player.get_ch_x() - (player.get_x() - character.x));
                ch_y = (short) (player.get_ch_y() - (player.get_y() - character.y));
            }

            g2d.drawImage(postac, ch_x - (postac.getWidth(null) / 2 - player.get_w() / 2), ch_y, null);
        }
    }

    /**
     * wyświetlenie chatu
     *
     * @param g2d
     */
    public void chatShow(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.setPaint(Color.BLUE);



        if (czat != null) {
            for (short i = 0; i < 6; i++) {
                if (czat[i] != null) {
                    g2d.drawString(czat[i], 10, 500 + i * 15);
                }
            }
        }
    }

    /**
     * wyświetlenie statystyk
     *
     * @param g2d
     */
    public void statsShow(Graphics2D g2d) {


        g2d.drawImage(frag, 600, 25, null);
        g2d.drawImage(dead, 700, 25, null);

        g2d.setFont(new Font("Arial", Font.BOLD, 25));
        g2d.setPaint(Color.black);
        g2d.drawString(Short.toString(ui.characters.get(ui.ID).frags), 660, 50);
        g2d.setPaint(Color.RED);
        g2d.drawString(Short.toString(ui.characters.get(ui.ID).dead), 740, 50);

        try { //HP
            for (short i = 0;
                    i < ui.characters.get(ui.ID).hp; i++) {
                g2d.drawImage(life, i * 25, 30, null);
            }
        } catch (NullPointerException e) {
            return;
        }

        if (klawisze[4]) { //tab
            g2d.drawImage(staty, 0, 0, null);
            Iterator<Map.Entry<Short, Character>> it2 = ui.characters.entrySet().iterator();
            g2d.drawString("ID", 150, 150);
            g2d.drawString("nick", 250, 150);
            g2d.drawString("kills", 450, 150);
            g2d.drawString("deaths", 600, 150);
            short i = 1;
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.setPaint(Color.black);
            while (it2.hasNext()) {

                Character character = ui.characters.get(it2.next().getKey());
                g2d.drawString(Short.toString(i), 150, 160 + 30 * i);
                g2d.drawString(character.name, 250, 160 + 30 * i);
                g2d.drawString(Short.toString(character.frags), 450, 160 + 30 * i);
                g2d.drawString(Short.toString(character.dead), 600, 160 + 30 * i);
                i++;


            }

        }

    }

    public void menuPhase0(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.setPaint(Color.BLUE);
        g2d.drawString("START", 150, 250);
        g2d.drawString("EXIT", 150, 300);


    }

    public void menuPhase1(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.setPaint(Color.BLUE);
        g2d.drawString("LAN", 150, 250);
        g2d.drawString("WAN", 150, 300);
        g2d.drawString("POWRÓT", 150, 350);
    }

    public void menuPhase2(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.setPaint(Color.BLUE);
        g2d.drawString("Wpisz adres ip", 150, 190);
        g2d.drawString("Anuluj", 150, 300);
        g2d.drawString("OK", 300, 300);
        g2d.setPaint(Color.WHITE);
        g2d.fillRect(150, 220, 200, 30);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.setPaint(Color.BLACK);
        g2d.drawString(ip, 152, 248);

        g2d.drawString("Lista serwerów:", 450, 190);

        for (int i = 0; i < adresy.length; i++) {
            g2d.drawString(adresy[i], 450, 240 + i * 30);
        }

    }

    public void menuPhase3(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.setPaint(Color.BLUE);
        g2d.drawString("Wpisz adres ip", 150, 190);
        g2d.drawString("Anuluj", 150, 300);
        g2d.drawString("OK", 300, 300);
        g2d.setPaint(Color.WHITE);
        g2d.fillRect(150, 220, 200, 30);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.setPaint(Color.BLACK);
        g2d.drawString(ip, 152, 248);


    }

    public void menuPhase4(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.setPaint(Color.BLUE);
        g2d.drawString("Wpisz nick", 150, 190);
        g2d.drawString("Anuluj", 150, 300);
        g2d.drawString("OK", 300, 300);
        g2d.setPaint(Color.WHITE);
        g2d.fillRect(150, 220, 200, 30);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.setPaint(Color.BLACK);
        g2d.drawString(name, 152, 248);
    }

    public void menuPhase5(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.setPaint(Color.BLUE);
        g2d.drawString("Wpisz hasło", 150, 190);
        g2d.drawString("Anuluj", 150, 300);
        g2d.drawString("OK", 300, 300);
        g2d.setPaint(Color.WHITE);
        g2d.fillRect(150, 220, 200, 30);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.setPaint(Color.BLACK);
        String gwiazdki = "";
        for (int i = 0; i < password.length(); i++) {
            gwiazdki += "*";
        }
        g2d.drawString(gwiazdki, 152, 248);
    }

    public void menuPhase6(Graphics2D g2d) {
        g2d.drawImage(characters[0][0][0], 300, 250, null);

        g2d.drawImage(characters[1][1][0], 500, 250, null);
    }

    public void menuPhase7(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.setPaint(Color.BLACK);
        g2d.drawString("Szukanie serwerów...", 300, 300);
    }

    public void OKClicked(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.setPaint(Color.YELLOW);
        g2d.drawString("OK", 300, 300);
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
        try {
            g2d = (Graphics2D) bstrategy.getDrawGraphics();
        } catch (NullPointerException e) {
            return;
        }

        if (!menu) {

            if (player == null) {
                return;
            }

            bgShow(g2d);
            platformShow(g2d);

            characterShow(g2d);
            chatShow(g2d);
            statsShow(g2d);



        } else {
            g2d.drawImage(bgMenu, 0, 0, null);
            switch (menuPhase) {
                case 0:
                    menuPhase0(g2d);
                    break;
                case 1:
                    menuPhase1(g2d);
                    break;
                case 2:
                    menuPhase2(g2d);
                    break;
                case 3:
                    menuPhase3(g2d);
                    break;
                case 4:
                    menuPhase4(g2d);
                    break;
                case 5:
                    menuPhase5(g2d);
                    break;
                case 6:
                    menuPhase6(g2d);
                    break;
                case 7:
                    menuPhase7(g2d);
                    break;
            }



        }
        g2d.dispose();

        bstrategy.show();

    }
}
