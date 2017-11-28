/* Soubor je ulozen v kodovani UTF-8.
 * Kontrola kódování: Příliš žluťoučký kůň úpěl ďábelské ódy. */
package logika;


/*******************************************************************************
 * Instance třídy PrikazSeber představují ...
 * @author    Jakub Skála (skaj06)
 * @version   ZS 2016/2017
 */
public class PrikazUkazBatoh implements IPrikaz
{
    //== Datové atributy (statické i instancí)======================================
    private static final String NAZEV = "ukazBatoh";
    private Batoh batoh;
    //== Konstruktory a tovární metody =============================================

    /***************************************************************************
     *  Konstruktor ....
     */
    public PrikazUkazBatoh(Batoh batoh)
    {        
        this.batoh = batoh; 
    }

    //== Nesoukromé metody (instancí i třídy) ======================================

    /**
     *  Provádí příkaz "batoh". 
     *  
     *  Zobrazuje aktuální obsah batohu.
     *  
     *  @param parametry - nejsou potřeba žádné
     *  @return zpráva, kterou vypíše hra hráči
     */ 
    public String proved(String... parametry){
        if (parametry.length >= 1) {
            return "Zadej pouze název příkazu!";
        } else {
           return batoh.obsahBatohu();
        }
        }
    

    /**
     *  Metoda vrací název příkazu (slovo které používá hráč pro jeho vyvolání)
     *  
     *  @return nazev prikazu
     */
    public String getNazev(){
        return NAZEV;
    }
    //== Soukromé metody (instancí i třídy) ========================================

}
