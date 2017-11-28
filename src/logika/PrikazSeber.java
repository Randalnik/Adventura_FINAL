/* Soubor je ulozen v kodovani UTF-8.
 * Kontrola kódování: Příliš žluťoučký kůň úpěl ďábelské ódy. */
package logika;

import javafx.scene.media.AudioClip;

/*******************************************************************************
 *
 * @author    Jakub Skála (skaj06)
 * @version   ZS 2016/2017
 */
public class PrikazSeber implements IPrikaz
{
    //== Datové atributy (statické i instancí)======================================
    private static final String NAZEV = "seber";
    private HerniPlan plan;
    private Batoh batoh;
    private int konceHry;
    private AudioClip zvukSebrani; //nové
    //== Konstruktory a tovární metody =============================================

    /***************************************************************************
     *  Konstruktor ....
     */
    public PrikazSeber(HerniPlan plan, Batoh batoh, int konceHry)
    {
        this.plan = plan;
        this.batoh = batoh;
        this.konceHry = konceHry;
    }

    //== Nesoukromé metody (instancí i třídy) ======================================

    /**
     *  Metoda pro provedení příkazu "seber"
     *  
     *  Sbíraná věc musí v prostoru existovat a musí být přenositelná.
     *  Velikost batohu je 6. Pokud je plný, věc se nesebere a zůstává v aktuálním prostoru.
     *  Jinak se věc sebere a přidá do batohu.
     *  
     *  @param název věci, která se má sebrat do batohu
     *  @return String vrácený text hráči
     */
    public String proved(String... parametry){
        String nazevSbiraneVeci = null; // = parametry[0] + " " + parametry[1] + " " + parametry[2];
        if (parametry.length == 1) {
            nazevSbiraneVeci = parametry[0];           
        }
        if (parametry.length == 2) {
            nazevSbiraneVeci = parametry[0] + " " + parametry[1];           
        }
        if (parametry.length == 3) {
            nazevSbiraneVeci = parametry[0] + " " + parametry[1] + " " + parametry[2];           
        }
        
        zvukSebrani = new AudioClip(this.getClass().getResource("/zdroje/sebrani.mp3").toString()); //nové
                    zvukSebrani.play(); //nové

        Prostor aktualni = plan.getAktualniProstor();        
        Vec sbiranaVec = aktualni.odeberVec(nazevSbiraneVeci);
        String vracenyText = null;

        if(parametry.length == 0){        
            vracenyText = "Co chceš sebrat?";
        } else {   
            if (sbiranaVec instanceof Vec){                             
                if(sbiranaVec.getNazev().equals("Lano") && sbiranaVec.muzuZvednout() == false){ //lano lze sebrat až po odříznutí: false --> true
                    vracenyText= "Visíš na laně, nemůžeš jej sebrat.";
                } else{
                    if(sbiranaVec.muzuZvednout()){ 
                        if (batoh.velikostBatohu() < batoh.getObjemBatohu()) {
                            if(sbiranaVec.getNazev().equals("Rotor")){
                                if(plan.getPavouk().jePouzitelna()) {
                                     vracenyText = "Sebral sis to do baťůžku: " + nazevSbiraneVeci + " \n";                        
                                     batoh.vlozVecDoBatohu(sbiranaVec);
                                } else {
                                     aktualni.ziskejVec("Pavouk").setMuzuZvednout(false);  //pokud je možné zvednout pavouka během sbírání rotoru v prostoru, hra končí prohrou
                                     konceHry = 2;
                                     vracenyText = "";
                                }
                            } else {
                                vracenyText = "Sebral sis to do baťůžku: " + nazevSbiraneVeci + " \n";       // vloží věc do baťůžku                 
                                batoh.vlozVecDoBatohu(sbiranaVec); 
                            }
                        } else { 
                            vracenyText = "Tvůj baťůžek pro přežití je plný!"; // vypíše v případě plného baťůžku
                        }
                    } else{
                        aktualni.vlozVec(sbiranaVec);
                        vracenyText = "Tuto věc nelze sebrat!"; // pokud není možné zvednout věc, vypíše se tento text
                    }
                }
                if(sbiranaVec.getNazev().equals("Pavouk")){
                    sbiranaVec.setMuzuZvednout(false);
                    konceHry = 2;
                    vracenyText = ""; // pokud se sebere pavouk, hra končí prohrou
                }

                if(sbiranaVec.getNazev().equals("Chrám")){
                    sbiranaVec.setMuzuZvednout(false);
                    konceHry = 6;
                    vracenyText = ""; // pokud se sebere chrám, hra končí výhrou
                }
            }else {
                vracenyText = "Zřejmě si zadal špatně název sbíraného předmětu, zkontroluj proto název předmětu!";
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
