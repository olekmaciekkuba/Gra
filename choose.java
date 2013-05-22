/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Gra;

/**
 *Classa z funkcjami czekajacymi na siebie wzajemnie (wielowatkowosc).
 * 
 * Wywolujemy metode {@link choose#get} lub {@link choose#getString} , ktore czekaja odpowiednio na ustawienie zmiennych wybor lub napis
 * przez metody {@link choose#put}
 * 
 * @author Olek
 */
public class choose {
        
         private int wybor;
         private String napis;
         /**
          * 
          * @return zwraca wartosc zmiennej wybor 
          */
        synchronized  public int get(){
          
                try {
                    wait();
                }
                catch(InterruptedException e){
                }
             return wybor;
        }
        /**
         * 
         * @return zwraca wartosc zmiennej napis 
         */
        synchronized public String getString(){
           
                try {
                    wait();
                }
                catch(InterruptedException e){
                 }
            
            return napis;
        }
        /**
         * 
         * @param a ustawia zmienna wybor 
         */
        synchronized  public void put(int a){
            wybor=a;
            notify();
        }
        /**
         * 
         * @param a ustawia zmienna napis 
         */
        synchronized public void put(String a){
            napis=a;
            notify();
        }
    
}
