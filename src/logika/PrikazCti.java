/* Soubor je ulozen v kodovani UTF-8.
 * Kontrola kódování: Příliš žluťoučký kůň úpěl ďábelské ódy. */
package logika;

/*******************************************************************************
 * Instance třídy PrikazSeber představují ...
 *
 * @author    Jakub Skála (skaj06)
 * @version   ZS 2016/2017
 */
public class PrikazCti implements IPrikaz
{
    //== Datové atributy (statické i instancí)======================================
    private static final String NAZEV = "cti";
    private HerniPlan plan;
    private Batoh batoh;
    private Vec vec; //přidáno 25112017
    
    //== Konstruktory a tovární metody =============================================

    /***************************************************************************
     *  Konstruktor ....
     */
    public PrikazCti(HerniPlan plan, Batoh batoh)
    {
        this.plan = plan;
        this.batoh = batoh; 
    }

    //== Nesoukromé metody (instancí i třídy) ======================================

    /**
     *  Provádí příkaz "cti". 
     *  Příkaz "cti" lze použít jen na pár věcí, které jsou k tomu určené.
     *  Lze možné přečíst pouze věci obsažené v baťůžku.
     *  
     *  @param parametry Věc, která se má přečíst.
     *  @return Vypíše text dané věci.
     */ 

    public String proved(String... parametry){
        String vracenyText = null;        
        Prostor aktualni = plan.getAktualniProstor();  

        String nazevVeciKPrecteni = null;
        Vec vecKPrecteni = null;

        if (parametry.length == 0){        
            vracenyText =  "Co mám přečíst?";
        }

        if (parametry.length == 1) {     
            nazevVeciKPrecteni = parametry[0];            
            if (batoh.ziskejVec(nazevVeciKPrecteni) != null) { 
                vecKPrecteni = batoh.ziskejVec(nazevVeciKPrecteni);
                if (vecKPrecteni instanceof Vec) {
                    switch (nazevVeciKPrecteni) {
                        case "Deník":
                            vracenyText =  "Už nikdy nebudu zkoušet létat s novým vrtulníkem do neznáma!Pro případ, že by někoho potkalo to samé, co mě, jsem v rychlosti sepsal manuál. Obsahuje základní informace o tom, jak zprovoznit vrtulník a bezpečně odletět.Najdete ho poblíž vodopádu. Ať vám slouží, mě nepomohl.";
                            break; 
                        
                        case "Manuál":
                            vracenyText =  "\nMANUÁL PRO ŘÍZENÍ VRTULNÍKU\n1) Nejdříve musíte doplnit palivo do stroje.\n2) Pak budete potřebovat chybějící část rotoru, kterou se mi bohužel nepodařilo najít.\n3) Vrtulník ovládáte kniplem uprostřed.\n4) Pro start motoru zadejte: pouzij Start Vrtulník\n\nTeď už víte, co máte dělat, abyste se dostali z ostrova.\nHodně štěstí.\n";
                            vecKPrecteni.setJePouzitelna(true);
                        break; 
                        
                        case "Dopis":
                            vracenyText = "\nZnal jsem člověka, který kdysi dávno napsal tuto hru. Byl šťastný a plný života do chvíle, než byl nucen začít používat debuger. Od té chvíle se jeho zdravotní stav prudce zhoršoval, zbláznil se a zmizel neznámo kam. Čest jeho památce\n\nZS2016/17\n";
                        break;
                    }
                } else {
                    vracenyText =  "Můžeš použít pouze věc!";
                }
            } else {
                vracenyText =  "Tuto věc nemáš v batohu!";
            }

        }

        if (parametry.length > 1) { 
            vracenyText =  "Nemusíš zadávat tolik parametrů.";
        } 
        
//        if (vec.jeCitelna() = false){
//            vracenyText = "Tento předmět nelze přečíst.";
//        }
        
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
