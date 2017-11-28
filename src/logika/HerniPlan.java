package logika;

import GUI.MapaGUI;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import utils.Observer;
import utils.Subject;

/**
 *  Class HerniPlan - třída představující mapu a stav adventury.
 * 
 *  Tato třída inicializuje prvky ze kterých se hra skládá:
 *  vytváří všechny prostory,
 *  propojuje je vzájemně pomocí východů 
 *  a pamatuje si aktuální prostor, ve kterém se hráč právě nachází.
 *
 *@author     Michael Kolling, Lubos Pavlicek, Jarmila Pavlickova, použil Jakub Skála (skaj06)
 *@version    ZS 2016/2017
 */
public class HerniPlan implements Subject{

    private Prostor aktualniProstor;
    private Batoh batoh;
    private Vec chram;
    private Vec pavouk;
    private Vec pirana;
    private Vec kanystr;
    private Vec vor;
    private Vec vrtulnik;
    private Vec rotor;
    private Vec manual;
    private Vec start;
    private Map<String, Prostor> prostory;
    
    private List<Observer> listObservers = new ArrayList<Observer>();

    /**
     *  Konstruktor který vytváří jednotlivé prostory a propojuje je pomocí východů.
     *  Jako výchozí aktuální prostor nastaví halu.
     */
    public HerniPlan(Batoh batoh) {
        prostory = new HashMap<>();
        zalozProstoryHry();
        this.batoh = batoh;

    }

    /**
     *  Vytváří jednotlivé prostory a propojuje je pomocí východů.
     *  Jako výchozí aktuální prostor nastaví Jeskyni.
     */
    private void zalozProstoryHry() {
        // vytvářejí se jednotlivé prostory
        Prostor jeskyne = new Prostor("Jeskyně","Chladná jeskyně,taková, ve které by ani naši předci z pravěku nepřežili. Jako bonus do ní dopadá pár slunečních paprsků skrze východ do neznáma.", 30, 90);
        Prostor vesnice = new Prostor("Domorodá vesnice", "Tak přesně takovou vesnici znáš z každého hororového filmu, určitě si ji chceš prohlédnout.", 32, 200);
        Prostor vodopad = new Prostor("Vodopád s jezírkem","Poklidný vodopád s jezírkem, ve kterém však žije místní hrozba.", 265, 90);
        Prostor jungle = new Prostor("Hustá džungle","Není to klasický les, je to hustá a skoro nepropustná jungle ve které se nachází i kus rotoru... Jen na něm sedí obrovský pavouk!", 179, 350);
        Prostor plaz = new Prostor("Písečná pláž","Pláž skoro jak v Miami, akorát místo slunečníků a drinků tu na tebe čekají jiná překvapení.", 100, 450); //je to pláž
        
        prostory.put("jeskyne", jeskyne);
        prostory.put("vesnice", vesnice);
        prostory.put("vodopad", vodopad);
        prostory.put("jungle", jungle);
        prostory.put("plaz", plaz);
        
        //ted se budou vytvářet věci
        //(String nazev, Prostor prostorPouziti,boolean jeCitelna, boolean muzuZvednout, boolean jePouzitelna, boolean jeProhledatelna, Vec vecPouziti, Vec vecZiskanaProhledanim, Vec vecZiskanaPouzitim, Image img)

        pirana = new Vec("Piraňa",null, false, false, false, false, null, null, null, "pirana.png");
        Vec ostep = new Vec("Oštěp",vodopad, false, true, true, false, pirana, null, null, "ostep.png");
        Vec banany = new Vec("Banány",vesnice, false, true, true, false, null, null, ostep, "banany.png");
        Vec bananovnik = new Vec("Banánovník",null, false, false, false, false, null, null, null, "bananovnik.png");
        Vec lano = new Vec("Lano",jungle,false, false, true, false, bananovnik, null, banany, "lano.png"); //po použití nože na lano muzuZvednout = true
        Vec nuz = new Vec("Nůž",jeskyne,false, true, true, false, lano, null, null, "nuz.png");
        Vec zem = new Vec("Zem",jeskyne,false, false, false, true, null, nuz, null, "denik.png");
        Vec denik = new Vec("Deník",null, true, true, false, false, null, null, null, "denik.png");
        Vec mrtvola = new Vec("Mrtvola", null, false, false, false, true, null, denik, null, "mrtvola.png");
        Vec jezero = new Vec("Jezero",null, false, false, false, false, null, null, null, "jezero.png");
        chram = new Vec("Chrám",vesnice, false, true, false, false, null, null, null, "chram.png"); // sebereš chrám a vyhraješ
        start = new Vec("Start",plaz, false, false, true, false, null, null, null, "start.png");
        vrtulnik = new Vec("Vrtulník",plaz, false, false, true, true, null, start, null, "vrtulnik.png");
        start.setVecPouziti(vrtulnik);
        kanystr = new Vec("Kanystr",plaz, false, true, true, false, vrtulnik, null, null, "kanystr.png");
        Vec klic = new Vec("Klíč",vesnice, false, true, true, false, chram, null, kanystr, "klic.png");
        Vec voda = new Vec("Voda",jungle, false, true, true, false, null, null, klic, "voda.png");
        Vec miska = new Vec("Miska",vodopad, false, true, true, false, jezero, null, voda, "miska.png");
        pavouk = new Vec("Pavouk",null, false, true, false, false, null, null, null, "pavouk.png"); //sebereš pavouka, umřeš
        Vec kamen = new Vec("Kámen",jungle, false, true, true, false, pavouk, null, null, "kamen.png");
        manual = new Vec("Manuál",null, true, true, false, true, null, kamen, null, "manual.png"); // pokud přečtu pak jePouzitelna = true 
        Vec balvan = new Vec("Balvan",null, false, false, false, true, null, manual, null, "balvan.png");                        
        rotor = new Vec("Rotor",plaz, false, true, true, false, vrtulnik, null, null, "rotor.png");
        vor = new Vec("Vor",plaz, false, false, true, false, null, null, null, "vor.png"); //odpluješ a chsípneš
        Vec padla = new Vec("Pádla",plaz, false, true, true, false, vor, null, null, "padla.png");        
        Vec dopis = new Vec("Dopis",null, true, true, false, false, null, null, null, "dopis.png");

        //Tvoření postav
        Postava saman = new Postava("Šaman", "Hej ty! Mám hlad, sežeň mi něco k jídlu a dostaneš oštěp!", "saman.png");
        Postava opice = new Postava("Mluvící opice", "Hu Hu! Umírám žízní, dones mi něco k pití a odměna tě nemine. Vrať se a pak pokecáme.", "opice.png");

        // přiřazují se průchody mezi prostory (sousedící prostory)
        jeskyne.setVychod(vesnice);
        vesnice.setVychod(jeskyne);
        vesnice.setVychod(vodopad);
        vesnice.setVychod(jungle);
        vodopad.setVychod(vesnice);
        jungle.setVychod(vesnice);
        jungle.setVychod(plaz);
        plaz.setVychod(jungle);

        //Přiřazení věcí k prostorům
        jeskyne.vlozVec(nuz);
        jeskyne.vlozVec(mrtvola);
        jeskyne.vlozVec(lano);

        vesnice.vlozVec(miska);
        vesnice.vlozVec(chram);

        vodopad.vlozVec(pirana);
        vodopad.vlozVec(jezero);
        vodopad.vlozVec(balvan);

        jungle.vlozVec(bananovnik);
        jungle.vlozVec(rotor);
        jungle.vlozVec(pavouk);

        plaz.vlozVec(vrtulnik);
        plaz.vlozVec(vor);
        plaz.vlozVec(padla);
        plaz.vlozVec(dopis);
        plaz.vlozVec(kamen);

        //Přiřazení postav k prostorům

        vesnice.vlozPostavu(saman);
        jungle.vlozPostavu(opice);

        aktualniProstor = jeskyne;  // hra začíná v jeskyni      
    }

    public Map<String, Prostor> getProstory() {
        return prostory;
    }

    /**
     *  Metoda vrací odkaz na aktuální prostor, ve ktetém se hráč právě nachází.
     *
     *@return     aktuální prostor
     */

    public Prostor getAktualniProstor() {
        return aktualniProstor;
    }
    
   

    /**
     *  Metoda nastaví aktuální prostor, používá se nejčastěji při přechodu mezi prostory
     *
     *@param  prostor nový aktuální prostor
     */
    public void setAktualniProstor(Prostor prostor) {
        aktualniProstor = prostor;
        notifyAllObservers();
    }


    // Dvě výhry (vrtulník + chrám)
    public int vyhra(){
        int vyhra = 0;
        if (chram.muzuZvednout() == false){
            vyhra = 6;
        }
        if (kanystr.jePouzitelna() == false && rotor.jePouzitelna() == false && manual.jePouzitelna()){
            vyhra = 5;
        }
        return vyhra;
    }

 
    // 4 prohry (Piraňa, pavouk, neopravený vrtulní, vor)
    public int prohra(){
        int prohra = 0;
        if (pavouk.muzuZvednout() == false){
            prohra = 2;
        }
        if (pirana.muzuZvednout() == true){
            prohra = 1;
        }
        if (vor.muzuZvednout() == true){
            prohra = 3;
        }
        if (kanystr.jePouzitelna() == false && rotor.jePouzitelna() == false && manual.jePouzitelna() == false){
            prohra = 4;
        }
        return prohra;
    }
    
    /** Metoda vrací věc v prostoru
     * @return Věc - Piraňa
     */
    public Vec getPirana () {
        return pirana;
    }
    
    /** Metoda vrací věc v prostoru
     * @return Věc - Pavouk
     */
    public Vec getPavouk () {
        return pavouk;
    }
    
    /** Metoda vrací věc v prostoru
     * @return Věc - Kanystr
     */
    public Vec getKanystr () {
        return kanystr;
    }
    
    /** Metoda vrací věc v prostoru
     * @return Věc - Vor
     */
    public Vec getVor () {
        return vor;
    }
    
    /** Metoda vrací věc v prostoru
     * @return Věc - Rotor
     */
    public Vec getRotor () {
        return rotor;
    }
    
    /** Metoda vrací věc v prostoru
     * @return Věc - Manuál
     */
    public Vec getManual () {
        return manual;
    }
    
    /** Metoda vrací věc v prostoru
     * @return Věc - Start
     */
    public Vec getStart () {
        return start;
    }
    
    /** Metoda vrací věc v prostoru
     * @return Batoh - batoh
     */
    public Batoh getBatoh (){
        return batoh;
    }
    
    /**
     * Zaregistuje nový observer
     * @param observer 
     */
    @Override
    public void registerObserver(Observer observer) {
        listObservers.add(observer);
    }
    
    /**
     * Smaže daný observer
     * @param observer 
     */
    @Override
    public void deleteObserver(Observer observer) {
        listObservers.remove(observer);
    }
    
    /**
     * Upozorní všechny observery
     */
    @Override
    public void notifyAllObservers() {
        for (Observer listObserver : listObservers) {
            listObserver.update();
        }
    }


}
