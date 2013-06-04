package Gra;

/**
 *
 * @author Olek
 */
public class Character {

    /**
     * Nazwa postaci
     */
    public String name;
    /**
     * id postaci
     */
    public short id;
    /**
     * Wspolrzedna X postaci
     */
    public short x;
    /**
     * Wspolrzedna y postaci
     */
    public short y;
    /**
     * Obrazek postaci 1 lub 2
     */
    public short image;
    /**
     * true - uprawnienia admina
     * false - zwykły użytkownik
     */
    public boolean admin;
    /**
     * true - postac atakuje
     */
    public boolean attack;
    /**
     * wyswietlana animacja + klatka
     * 
     */
    public short a;
    /**
     * ilosc życia postaci
     */
    public short hp;
    /**
     * informacja ile razy postac zginela
     */
    public short dead;
    /**
     * informacja o ilosci fragow
     */
    public short frags;
    public boolean immortal;
    public String password;
}
