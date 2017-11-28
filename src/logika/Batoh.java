/* Soubor je ulozen v kodovani UTF-8.
 * Kontrola kódování: Příliš žluťoučký kůň úpěl ďábelské ódy. */
package logika;

import java.util.Map;
import java.util.HashMap;
import utils.Subject;
import java.util.*;


/*******************************************************************************
 * @author    Jakub Skála (skaj06)
 * @version   ZS 2016/2017
 */
public class Batoh implements Subject
{
    private Map<String, Vec> batoh;
    private int objemBatohu;
    
    private List<utils.Observer> listObservers = new ArrayList<> ();

    /***************************************************************************
     *  Konstruktor ....
     */
    public Batoh ()
    {
        this.objemBatohu = 6;
        this.batoh = new HashMap<>(); 
    }
    
    /**
     *  Zobrazuje velikost batohu.
     *  
     *  @return int aktuální velikost (počet věcí v batohu) batohu
     */ 
    public int  velikostBatohu(){
        return batoh.size();
    }
    /**
     * vrací maximální velikost batohu (6)
     * @return objemBatohu 
     */
    public int getObjemBatohu (){
        return this.objemBatohu;
    }
    
    /**
     *  Vkládá věc do batohu.
     *  
     *  @param název vkládané věci
     */     
    public void vlozVecDoBatohu(Vec neco){
        batoh.put(neco.getNazev(),neco);
        notifyAllObservers();
    }        
    
    /**
     *  Odebírá věc z batohu.
     *  
     *  @param název odebírané věci
     *  @return Vec odebíraná věc
     */ 
    public Vec odeberVecZBatohu(String nazev){
        Vec result = batoh.remove(nazev);
        notifyAllObservers();
        return result;
    }
    
    /**
     *  Hledá věc v batohu.
     *  
     *  @param Vec hledaná věc
     *  @return true, pokud taková věc v batohu je.
     */
    public boolean hledejVec(Vec hledanaVec){ 
        boolean jeTam;
        jeTam = batoh.containsKey(hledanaVec);
        return jeTam;
    }        
    
    /**
     *  Vrátí věc z batohu
     *  
     *  @param String název věci k vrácení
     *  @return věc, která se má vrátit
     */
    public Vec ziskejVec(String nazevVeci){   
        Vec hledanaVec = null;
        for (String nazev : batoh.keySet()) {
            hledanaVec = batoh.get(nazevVeci);         
        }          
        return hledanaVec;
    }
    
    /**
     *  Zobrazí obsah batohu.
     *  
     *  @return String text obsahu
     */
    public String obsahBatohu(){
        String vracenyText = null;
        if (batoh.isEmpty()) {
            vracenyText = "Tvůj baťůžek pro přežití je prázdný!";
        } else {
            vracenyText = "Tvůj baťůžek obsahuje následující předměty: | ";
            for (String nazev : batoh.keySet()) {                
                vracenyText += nazev + " | ";            
            }
        }
        return vracenyText;
    }
    /**
     * Vrací seznam věcí batohu
     * 
     * @return batoh
     */
    public Map<String, Vec> getSeznamVeci(){
        return batoh;
    }
    /**
     * Zaregistuje nový observer
     * @param observer 
     */
    public void registerObserver(utils.Observer observer) {
        listObservers.add(observer);
    }
    /**
     * Upozorní všechny observery
     */
    public void notifyAllObservers() {
        for (utils.Observer listObserver : listObservers) {
            listObserver.update();
        }
    }
    /**
     * Smaže daný observer
     * @param observer 
     */
    @Override
    public void deleteObserver(utils.Observer observer) {
        listObservers.remove(observer);
    }
}
