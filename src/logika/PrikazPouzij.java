/* Soubor je ulozen v kodovani UTF-8.
 * Kontrola kódování: Příliš žluťoučký kůň úpěl ďábelské ódy. */
package logika;

/*******************************************************************************
 * Instance třídy PrikazSeber představují ...
 *
 * @author    Jakub Skála (skaj06)
 * @version   ZS 2016/2017
 */
public class PrikazPouzij implements IPrikaz
{
    //== Datové atributy (statické i instancí)======================================
    private static final String NAZEV = "pouzij";
    private HerniPlan plan;
    private Batoh batoh;
    //== Konstruktory a tovární metody =============================================

    /***************************************************************************
     *  Konstruktor ....
     */
    public PrikazPouzij(HerniPlan plan, Batoh batoh)
    {
        this.plan = plan;
        this.batoh = batoh; 
    }

    //== Nesoukromé metody (instancí i třídy) ======================================

    /**
     *  Provádí příkaz "pouzij". 
     *  Příkaz použij využívá dvou parametrů:
     *  první parametr je věc, která se má použít a druhý parametr je věc, na kterou se má předchozí věc použít
     *  příklad: pouzij Oštěp Piraňa (použije se Oštěp na Piraňa)
     *  
     *  @param Zadávají se dva parametry P1 a P2
     *  @return vrací text hráči 
     */ 

    public String proved(String... parametry){
        String vracenyText = null;        
        Prostor aktualni = plan.getAktualniProstor();  
        String nazevPouzivaneVeci = null;
        Vec pouzivanaVec = null;
        String nazevVeciPouziti = null;
        Vec vecPouziti = null;

        if (parametry.length == 0){        
            vracenyText =  "Co mám použít?"; // v případě nenapsání ani jednoho parametru
        }

        if (parametry.length == 1){        
            vracenyText =  "Nedostatek zadaných parametrů! Zadej:  1)'příkaz'  2)'používanou věc' 3)'věc, na které se provede použití'";
            // vysvětlení aplikace metody pouzij během hry.
        }

        if (parametry.length == 2) {     
            nazevPouzivaneVeci = parametry[0];
            
            if (nazevPouzivaneVeci.equals("Start")) {
                pouzivanaVec = plan.getStart();
            } else {
                pouzivanaVec = batoh.ziskejVec(nazevPouzivaneVeci);
            }
            nazevVeciPouziti = parametry[1];
            vecPouziti = aktualni.ziskejVec(nazevVeciPouziti);
            
            batoh.vlozVecDoBatohu(plan.getStart());
            
            if ((batoh.ziskejVec(nazevPouzivaneVeci) != null) && pouzivanaVec.vecPouziti().equals(vecPouziti) && pouzivanaVec.jePouzitelna() && aktualni.existenceVeciVProstoru(vecPouziti)) { 
                if (pouzivanaVec instanceof Vec && vecPouziti instanceof Vec) {
                    if (pouzivanaVec.prostorPouziti().equals(aktualni)) {                        
                        switch (nazevVeciPouziti) {
                            case "Lano":
                                vecPouziti.setMuzuZvednout(true); // po použití nože na lano se stav předmětu lano-seber změní na true (možno sebrat)
                                vracenyText =  "Právě jsi se nožem odřízl z lana. Jsi volný a můžeš se tu trochu porozhlédnout.";
                            break;    
                            
                            case "Banánovník":
                                if (batoh.velikostBatohu() < batoh.getObjemBatohu()) {
                                    batoh.vlozVecDoBatohu(pouzivanaVec.vecZiskanaPouzitim());
                                    vracenyText =  "Vyšplhal jsi po laně na banánovník a nasbíral pár banánů. Máš je v batohu.";
                                } else {
                                    vracenyText = "Tvůj baťůžek pro přežití je plný!"; // v případě plného baťůžku se banány nevloží
                                }
                            break; 
                            
                            case "Piraňa":
                                vecPouziti.setJePouzitelna(true);
                                aktualni.odeberVec(nazevVeciPouziti);    // po použití oštěpu na piraňu se piraňa odebere z prostoru - je možné bezpčně sebrat vodu                            
                                vracenyText =  "Zabil jsi zubatou piraňu. Teď už se zdá být jezero klidné a bezpečné.";                                
                            break; 
                            
                            case "Jezero":
                                if (plan.getPirana().jePouzitelna()) {
                                    batoh.vlozVecDoBatohu(pouzivanaVec.vecZiskanaPouzitim());
                                    batoh.odeberVecZBatohu(nazevPouzivaneVeci);                                
                                    vracenyText =  "Nabral jsi vodu. Byla ti přidána do batohu."; 
                                } else {
                                    plan.getPirana().setMuzuZvednout(true); // pokud lokace během sebrání vody stále obsahuje piraňu, hra končí prohrou
                                    plan.prohra();
                                    vracenyText = "";
                                }
                            break;
                            
                            case "Chrám":
                                if (batoh.velikostBatohu() < batoh.getObjemBatohu()) {
                                    batoh.vlozVecDoBatohu(plan.getKanystr());
                                    vracenyText = "Odemknul si dveře od chrámu. Po jeho prohledání si našel kanystr s leteckým palivem.";
                                } else {
                                    vracenyText = "Tvůj baťůžek pro přežití je plný!";
                                }
                            break; 
                            
                            case "Pavouk":
                                vecPouziti.setJePouzitelna(true);
                                aktualni.odeberVec(nazevVeciPouziti); // po použití kamene na pavouka se pavouk odebere z lokace - je bezpečné sebrat část rotoru
                                vracenyText =  "Odehnal jsi pavouka, který se schovával v rotoru. Teď už bude sebrání rotoru bezpečné.";                                
                            break; 
                            
                            case "Vor":
                                aktualni.ziskejVec(nazevVeciPouziti).setMuzuZvednout(true);
                                plan.prohra();  // pokud hráč použije Pádla na vor, odpluje z ostrova, hra končí prohrou
                                vracenyText = "";
                            break; 
                            
                            case "Vrtulník":                                                               
                                if (nazevPouzivaneVeci.equals("Start")) {
                                    if (plan.getKanystr().muzuZvednout() == false && plan.getRotor().muzuZvednout() == false && plan.getManual().jePouzitelna()) {
                                        plan.getKanystr().setJePouzitelna(false);
                                        plan.getRotor().setJePouzitelna(false);
                                        plan.vyhra(); // pokud jsou splněny všechny 3 podmínky, hra končí výhrou
                                        vracenyText = "";
                                    } else {
                                        if (plan.getKanystr().muzuZvednout() == false && plan.getRotor().muzuZvednout() == false && plan.getManual().jePouzitelna() == false) {
                                            plan.prohra();
                                            vracenyText = "";
                                        } else {
                                            vracenyText = "Nejde nastartovat.";
                                        }
                                    }
                                }
                                
                                if (nazevPouzivaneVeci.equals("Kanystr")) {
                                    batoh.odeberVecZBatohu(nazevPouzivaneVeci);
                                    pouzivanaVec.setMuzuZvednout(false);
                                    vracenyText = "Doplnil jsi palivo do vrtulníku. Kanystr zmizel z batohu.";
                                }
                                
                                if (nazevPouzivaneVeci.equals("Rotor")) {
                                    batoh.odeberVecZBatohu(nazevPouzivaneVeci);
                                    pouzivanaVec.setMuzuZvednout(false);
                                    vracenyText = "Připevnil jsi rotor k vrtulníku. Vrtulník je opravený.";
                                }
                                break;
                        }
                    } else {
                        vracenyText =  "Tato věc se zde nedá použít!";
                    }
                } else {
                    vracenyText =  "Můžeš použít pouze věc!";
                }
            } else {
                vracenyText =  "Tuto věc nemáš v batohu nebo tuto věc nemůžeš na tento předmět použít!"; 
            }
            batoh.odeberVecZBatohu(plan.getStart().getNazev());
        } 

        if (parametry.length > 2) { 
            vracenyText =  "Nemusíš zadávat tolik parametrů."; // zadávají se přesně 2 parametry
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
