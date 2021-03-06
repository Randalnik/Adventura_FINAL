package logika;

/**
 *  Třída PrikazJdi implementuje pro hru příkaz jdi.
 *  Tato třída je součástí jednoduché textové hry.
 *  
 *@author     Jarmila Pavlickova, Luboš Pavlíček, použil Jakub Skála (skaj06, ZS 2016/17)
 *@version    pro školní rok 2015/2016
 */
class PrikazJdi implements IPrikaz {
    private static final String NAZEV = "jdi";
    private HerniPlan plan;
    private Batoh batoh;

    /**
     *  Konstruktor třídy
     *  
     *  @param plan herní plán, ve kterém se bude ve hře "chodit" 
     */    
    public PrikazJdi(HerniPlan plan, Batoh batoh) {
        this.plan = plan;
        this.batoh = batoh;
    }

    /**
     *  Provádí příkaz "jdi". Zkouší se vyjít do zadaného prostoru. Pokud prostor
     *  existuje, vstoupí se do nového prostoru. Pokud zadaný sousední prostor
     *  (východ) není, vypíše se chybové hlášení.
     *
     *@param parametry - jako  parametr obsahuje jméno prostoru (východu),
     *                         do kterého se má jít.
     *@return zpráva, kterou vypíše hra hráči
     */ 
    @Override
    public String proved(String... parametry) {
        if (parametry.length == 0) {
            // pokud chybí druhé slovo (sousední prostor), tak ....
            return "Pro přechod do další lokace zadej správně její název.";
        }

        String smer = null;
        if (parametry.length == 1) {
            smer = parametry[0];           
        }
        if (parametry.length == 2) {
            smer = parametry[0] + " " + parametry[1];           
        }
        if (parametry.length == 3) {
            smer = parametry[0] + " " + parametry[1] + " " + parametry[2];           
        }

        // zkoušíme přejít do sousedního prostoru
        Prostor sousedniProstor = plan.getAktualniProstor().vratSousedniProstor(smer);

        if (sousedniProstor == null) {
            return "Z této lokace se tam nedostaneš, prohlédni si východy dané místnosti.";
        }
        else {            
            if ((plan.getAktualniProstor().ziskejVec("Lano") != null) && plan.getAktualniProstor().ziskejVec("Lano").muzuZvednout() == false) {
                return "Visíš na laně hlavou dolu a proto nemůžeš chodit. Nejdříve musíš přeříznout lano, pak budeš volný";
            } else {
                plan.setAktualniProstor(sousedniProstor);
                return sousedniProstor.dlouhyPopis();
            }
        }
    }

    /**
     *  Metoda vrací název příkazu (slovo které používá hráč pro jeho vyvolání)
     *  
     *  @ return nazev prikazu
     */
    @Override
    public String getNazev() {
        return NAZEV;
    }

}
