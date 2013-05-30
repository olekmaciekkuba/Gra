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
    public int id;
    /**
     * Wspolrzedna X postaci
     */
    public int x;
    /**
     * Wspolrzedna y postaci
     */
    public int y;
    /**
     * Obrazek postaci 1 lub 2
     */
    public int image;
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
    public int a, b;
    /**
     * ilosc życia postaci
     */
    public int hp;
    /**
     * informacja ile razy postac zginela
     */
    public int dead;
    /**
     * informacja o ilosci fragow
     */
    public int frags;
    public boolean immortal;
}
