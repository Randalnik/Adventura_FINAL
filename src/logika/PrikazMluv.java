/* Soubor je ulozen v kodovani UTF-8.
 * Kontrola kódování: Příliš žluťoučký kůň úpěl ďábelské ódy. */
package logika;

/*******************************************************************************
 * Instance třídy PrikazSeber představují ...
 * @author    Jakub Skála (skaj06)
 * @version   ZS 2016/2017
 */
public class PrikazMluv implements IPrikaz
{
    //== Datové atributy (statické i instancí)======================================
    private static final String NAZEV = "mluv";
    private HerniPlan plan;
    private Batoh batoh;
    //== Konstruktory a tovární metody =============================================

    /***************************************************************************
     *  Konstruktor ....
     */
    public PrikazMluv(HerniPlan plan, Batoh batoh)
    {
        this.plan = plan;
        this.batoh = batoh;
    }

    //== Nesoukromé metody (instancí i třídy) ======================================

    /**
     *  Provádí příkaz "mluv". 
     *  
     *  Mluví se pouze s lidmi, věci mají text k promluvení nastavený na null. To podmínkou neprojde. ?????????
     *  Postava, se kterou chci mluvit musí být instancí Postava, jinak hodí hlášku, že s touto věcí nelze mluvit.
     *  
     *  @param parametry - jako parametr obsahuje postavu, se kterou si chce hráč promluvit.
     *  @return zpráva, kterou vypíše hra hráči
     *  
     *  
     *  
     */ 
    public String proved(String... parametry){
        String vracenyText = null;
        String nazevPostavy = null;
        
        if(parametry.length == 0){        
            vracenyText =  "S kym si chceš promluvit?";
        } else {
            if(parametry.length == 1){        
                nazevPostavy = parametry[0];
            }
            if(parametry.length == 2){        
                nazevPostavy = parametry[0] + " " + parametry[1];
            }
            
            Prostor aktualni = plan.getAktualniProstor();
            Postava postavaSeKterouMluvim = aktualni.ziskejPostavu(nazevPostavy);

            if (postavaSeKterouMluvim instanceof Postava) {
                if(postavaSeKterouMluvim.getPopis() == null){
                    vracenyText = "S touto postavu mluvit nemůžeš!";
                } else {
                    switch (nazevPostavy) {
                        case "Šaman":                                
                            if (batoh.ziskejVec("Banány") != null) {                            
                                if (batoh.velikostBatohu() < batoh.getObjemBatohu()) {                                
                                    batoh.vlozVecDoBatohu(batoh.ziskejVec("Banány").vecZiskanaPouzitim());
                                    batoh.odeberVecZBatohu("Banány");
                                    vracenyText = "Mmmm...Banánky miluju! Tady máš oštěp, ať se v té divočině neztratíš.";
                                    postavaSeKterouMluvim.setPopis("Všechny banány jsem snědl, děkuji, ale více ti již nenabídnu.");
                                } else {
                                    System.out.println("Tvůj baťůžek pro přežití je plný!");
                                }
                            } else {
                                vracenyText = postavaSeKterouMluvim.getPopis();
                            }                                                        
                            break;
                            
                        case "Mluvící opice":   
                            if (batoh.ziskejVec("Voda") != null) {                            
                                if (batoh.velikostBatohu() < batoh.getObjemBatohu()) {                                
                                    batoh.vlozVecDoBatohu(batoh.ziskejVec("Voda").vecZiskanaPouzitim());
                                    batoh.odeberVecZBatohu("Voda");
                                    vracenyText = "Mmmm...Osvěžení bodne! Tady máš klíč, zkus ho použít ve vesnici.";
                                    postavaSeKterouMluvim.setPopis("Všechnu vodu jsem již vypila, děkuji, ale více ti již nenabídnu.");
                                } else {
                                    System.out.println("Tvůj baťůžek pro přežití je plný!");
                                }
                            } else {
                                vracenyText = postavaSeKterouMluvim.getPopis();
                            }  
                            break; 
                        
                        
                    }
                }
            } else {
                vracenyText =  "S tímhle mluvit nemůžeš!";
            }
        }
        return vracenyText;
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
