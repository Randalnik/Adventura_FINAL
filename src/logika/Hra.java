package logika;

/**
 *  Třída Hra - třída představující logiku adventury.
 * 
 *  Toto je hlavní třída  logiky aplikace.  Tato třída vytváří instanci třídy HerniPlan, která inicializuje mistnosti hry
 *  a vytváří seznam platných příkazů a instance tříd provádějící jednotlivé příkazy.
 *  Vypisuje uvítací a ukončovací text hry.
 *  Také vyhodnocuje jednotlivé příkazy zadané uživatelem.
 *
 *@author     Michael Kolling, Lubos Pavlicek, Jarmila Pavlickova, použil Jakub Skála (skaj06)
 *@version    ZS2016/2017
 */

public class Hra implements IHra {
    private SeznamPrikazu platnePrikazy;    // obsahuje seznam přípustných příkazů
    private HerniPlan herniPlan;
    private boolean konecHry = false;
    private Batoh batoh;
    private int konceHry;

    /**
     *  Vytváří hru a inicializuje místnosti (prostřednictvím třídy HerniPlan) a seznam platných příkazů.
     */
    public Hra() {
        batoh = new Batoh();
        herniPlan = new HerniPlan(batoh);
        int konceHry = 0;
        platnePrikazy = new SeznamPrikazu();
        platnePrikazy.vlozPrikaz(new PrikazNapoveda(platnePrikazy));
        platnePrikazy.vlozPrikaz(new PrikazJdi(herniPlan, batoh));
        platnePrikazy.vlozPrikaz(new PrikazKonec(this));
        platnePrikazy.vlozPrikaz(new PrikazSeber(herniPlan, batoh, konceHry));
        platnePrikazy.vlozPrikaz(new PrikazUkazBatoh(batoh));
        platnePrikazy.vlozPrikaz(new PrikazOdhod(herniPlan, batoh));
        platnePrikazy.vlozPrikaz(new PrikazPouzij(herniPlan, batoh));
        platnePrikazy.vlozPrikaz(new PrikazProhledej(herniPlan, batoh));
        platnePrikazy.vlozPrikaz(new PrikazCti(herniPlan, batoh));
        platnePrikazy.vlozPrikaz(new PrikazMluv(herniPlan, batoh));
    }

    /**
     *  Vrátí úvodní zprávu pro hráče.
     *  @return Uvítání do hry a prostor, ve kterém hráč začíná.
     */
    public String vratUvitani() {
        return "Vítej!\n" +
        "Jednoho krásného dne si se svým kámošem miliardářem rozhodl, že se proletíte v jeho novém vrtulníku nad blízký oceán.\n" +
        "Jak už to tak bývá, tak se něco nepovedlo a poslední co si pamatuješ je, že jste docela tvrdě dopadli s vrtulníkem na nějakou pláž nějakého neznámého ostrova.\n" +
        "Zapoj všechny své zkušenosti získané při hraní survival her, sledování katastrofických seriálů a filmů a dostaň se z ostrava pokud možno živý.\n" +
        "Čeká na tebe spousta důležitých, skoro až životních, rozhodnutí. Musíš si věřit!\n" +
        "Napiš 'napoveda', pokud si fakt nevíš rady.\n" +
        "\n" +
        herniPlan.getAktualniProstor().dlouhyPopis();
    }

    /**
     *  Vrátí závěrečnou zprávu pro hráče.
     *  @return Vrátí závěrečnou zprávu (výhra, prohra, konec).
     */
    public String vratEpilog() {
        String konecText = "Rozhodl jsi se ukončit svoji snahu o záchranu. Do pár dní umřeš na hlad nebo žízeň a nebo něco jiného. Prostě jsi dohrál!";
        if (konceHry == 1) {
            konecText = "Je sice pěkné, že si se pokusil nabrat si vodu, ale najednou tě zakousla obří piraňa! Dohrál jsi...\n";
        }
        if (konceHry == 2) {
            konecText = "Příště by sis měl kromě popisu lokace detailněji nastudovat i atlas pavouků, tenhle byl totiž jedovatý! Dohrál jsi...\n";
        }
        if (konceHry == 3) {
            konecText = "Gratuluji! Dostal jsi se z ostrova na voru, kde ale důsledkem pozdější dehydratace zemřeš, to znamená jediné. Dohrál jsi...\n";
        }
        if (konceHry == 4) {
            konecText = "Gratuluji! Zprovoznil si vrtulník a vzlětěl. Kvůli neznalosti páček a kotrolek si však spadnul ještě tvrději než předtím. Dohrál jsi...\n";
        } // tento konec není ve hře zanese, každopádně jsem jej zde ponechal
        if (konceHry == 5) {
            konecText = "Gratuluji Zprovoznil si vrtulník a vzletěl. Se znalostmi získanými z manuálu se ti podařilo bezpečně odletět. Zachránil si se!\n";
        }
        if (konceHry == 6) {
            konecText = "Objevil si svoji tajnou sílu! Bohové tě obdařili nadpřirozenou silou a stal si se pánem ostrova, kde si v klidu umřel ve svých 286765 letech.\nNašel jsi alternativní konec!\n";
        }
        return konecText;
    }

    /** 
     * Vrací true, pokud hra skončila.
     * @return Vrací true, pokud hra skončila, false pokud pokračuje.
     */
    public boolean konecHry() {
        return konecHry;
    }

    /**
     *  Metoda zpracuje řetězec uvedený jako parametr, rozdělí ho na slovo příkazu a další parametry.
     *  Pak otestuje zda příkaz je klíčovým slovem  např. jdi.
     *  Pokud ano spustí samotné provádění příkazu.
     *
     *@param  radek  text, který zadal uživatel jako příkaz do hry.
     *@return          vrací se řetězec, který se má vypsat na obrazovku
     */
    public String zpracujPrikaz(String radek) {
        String [] slova = radek.split("[ \t]+");
        String slovoPrikazu = slova[0];
        String []parametry = new String[slova.length-1];
        for(int i=0 ;i<parametry.length;i++){
            parametry[i]= slova[i+1];   
        }
        String textKVypsani=" .... ";
        if (platnePrikazy.jePlatnyPrikaz(slovoPrikazu)) {
            IPrikaz prikaz = platnePrikazy.vratPrikaz(slovoPrikazu);
            textKVypsani = prikaz.proved(parametry);
            if(herniPlan.vyhra() == 5 || herniPlan.vyhra() == 6){
                konecHry = true;
                konceHry = herniPlan.vyhra();
                textKVypsani += "\n\n" + vratEpilog();
            }
            if(herniPlan.prohra() == 1 || herniPlan.prohra() == 2 || herniPlan.prohra() == 3 || herniPlan.prohra() == 4){
                konecHry = true;
                konceHry = herniPlan.prohra();
                textKVypsani += "\n\n" + vratEpilog();
            }
        }
            else {
                textKVypsani="Nevím co mám dělat. Tento příkaz neznám. Nahlédni do nápovědy nebo zkontroluj text. ";
            }
        return textKVypsani;
    }

    /**
     *  Nastaví, že je konec hry, metodu využívá třída PrikazKonec,
     *  mohou ji použít i další implementace rozhraní Prikaz.
     *  
     *  @param  konecHry  hodnota false= konec hry, true = hra pokračuje
     */
    void setKonecHry(boolean konecHry) {
        this.konecHry = konecHry;
    }

    /**
     *  Metoda vrátí odkaz na herní plán, je využita hlavně v testech,
     *  kde se jejím prostřednictvím získává aktualní místnost hry.
     *  
     *  @return     odkaz na herní plán
     */
    public HerniPlan getHerniPlan(){
        return herniPlan;
    }
    
    /**
     *  Metoda vrátí odkaz na batoh, je využita hlavně v testech,
     *  kde se jejím prostřednictvím získává aktuální obsah batohu.
     *  
     *  @return     odkaz na batoh
     */
     public Batoh getBatoh(){
        return batoh;
    }
     
     /**
     *  Metoda, která nastaví číslo konce hry, aby se v metodě vratEpilog mohl vypsat správný text podle konce hry.
     *  
     *  @param     int číslo daného konce hry (výhrou, prohrou, příkazem)
     */
     
     public void setKonceHry(int cisloKonceHry){
        this.konceHry = cisloKonceHry;
     }
    

}

