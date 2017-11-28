package logika;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import utils.Subject;

/**
 *
 * "Prostor" reprezentuje jedno místo (místnost, prostor, ..) ve scénáři hry.
 * Prostor může mít sousední prostory připojené přes východy. Pro každý východ
 * si prostor ukládá odkaz na sousedící prostor.
 *
 * @author    Jakub Skála (skaj06)
 * @version   ZS 2016/2017
 */
public class Prostor  implements Subject{

    private String nazev;
    private String popis;
    private Set<Prostor> vychody;   // obsahuje sousední místnosti
    private Map<String, Vec> veci;
    private Map<String, Postava> postavy;
    private double posX;
    private double posY;
    
    private List<utils.Observer> listObservers;
    

    /**
     * Vytvoření prostoru se zadaným popisem, např. "kuchyň", "hala", "trávník
     * před domem"
     *
     * @param nazev nazev prostoru, jednoznačný identifikátor, jedno slovo nebo
     * víceslovný název bez mezer.
     * @param popis Popis prostoru.
     * @param posX určuje X souřadnici.
     * @param posY určuje Y souřadnici.
     */
    public Prostor(String nazev, String popis, double posX, double posY) { 
        this.nazev = nazev;
        this.popis = popis;
        this.posX = posX;
        this.posY = posY;
        vychody = new HashSet<>();
        veci = new HashMap<>();
        postavy = new HashMap<>();
        listObservers = new ArrayList<>();
    }

    /**
     * Definuje východ z prostoru (sousední/vedlejsi prostor). Vzhledem k tomu,
     * že je použit Set pro uložení východů, může být sousední prostor uveden
     * pouze jednou (tj. nelze mít dvoje dveře do stejné sousední místnosti).
     * Druhé zadání stejného prostoru tiše přepíše předchozí zadání (neobjeví se
     * žádné chybové hlášení). Také lze zadat cestu z prostoru do stejného prostoru.
     *
     * @param vedlejsi prostor, který sousedi s aktualnim prostorem.
     *
     */
    public void setVychod(Prostor vedlejsi) {
        vychody.add(vedlejsi);
    }

    /**
     * Metoda equals pro porovnání dvou prostorů. Překrývá se metoda equals ze
     * třídy Object. Dva prostory jsou shodné, pokud mají stejný název. Tato
     * metoda je důležitá z hlediska správného fungování seznamu východů (Set).
     *
     * Bližší popis metody equals je u třídy Object.
     *
     * @param o object, který se má porovnávat s aktuálním
     * @return hodnotu true, pokud má zadaný prostor stejný název, jinak false
     */  
      @Override
    public boolean equals(Object o) {
        // porovnáváme zda se nejedná o dva odkazy na stejnou instanci
        if (this == o) {
            return true;
        }
        // porovnáváme jakého typu je parametr 
        if (!(o instanceof Prostor)) {
            return false;    // pokud parametr není typu Prostor, vrátíme false
        }
        // přetypujeme parametr na typ Prostor 
        Prostor druhy = (Prostor) o;

        //metoda equals třídy java.util.Objects porovná hodnoty obou názvů. 
        //Vrátí true pro stejné názvy a i v případě, že jsou oba názvy null,
        //jinak vrátí false.

       return (java.util.Objects.equals(this.nazev, druhy.nazev));       
    }

    /**
     * metoda hashCode vraci ciselny identifikator instance, ktery se pouziva
     * pro optimalizaci ukladani v dynamickych datovych strukturach. Pri
     * prekryti metody equals je potreba prekryt i metodu hashCode. Podrobny
     * popis pravidel pro vytvareni metody hashCode je u metody hashCode ve
     * tride Object
     */
    @Override
    public int hashCode() {
        int vysledek = 3;
        int hashNazvu = java.util.Objects.hashCode(this.nazev);
        vysledek = 37 * vysledek + hashNazvu;
        return vysledek;
    }
      

    /**
     * Vrací název prostoru (byl zadán při vytváření prostoru jako parametr
     * konstruktoru)
     *
     * @return název prostoru
     */
    public String getNazev() {
        return nazev;       
    }
    
    /**
     * Vrací "dlouhý" popis prostoru, který může vypadat následovně: Jsi v
     * mistnosti/prostoru vstupni hala budovy VSE na Jiznim meste. vychody:
     * chodba bufet ucebna
     *
     * @return Dlouhý popis prostoru
     */
    public String dlouhyPopis() {
        return "Nacházíš se v lokaci: " + nazev + "\n"
                + popis + "\n"
                + popisVychodu()+ "\n"
                + popisVeci()+ "\n"
                + popisPostav();
    }

    /**
     * Vrací textový řetězec, který popisuje sousední východy, například:
     * "vychody: hala ".
     *
     * @return Popis východů - názvů sousedních prostorů
     */
    private String popisVychodu() {
        String vracenyText = "Východy: ";
        for (Prostor sousedni : vychody) {
            vracenyText += sousedni.getNazev() + " | ";
        }
        return vracenyText;
    }
    
    
    /**
     * Vrací textový řetězec, který popisuje věci v prostoru.
     *
     * @return Popis věcí v aktuálním prostoru
     */
    private String popisVeci(){
        String vracenyText;
        if (veci.isEmpty()) {
           vracenyText = "V lokaci |" + nazev + "| nespatřilo tvé oko žádné zajímavé předměty.";
        } else {        
           vracenyText = "Tvé oko spatřilo tyto předměty: ";
           for (String nazev : veci.keySet()) {
               vracenyText += nazev + " | ";            
           }           
        }
        return vracenyText;
    }
    
    /**
     * Vrací textový řetězec, který popisuje postavy v prostoru.
     *
     * @return Popis postav v aktuálním prostoru
     */
    private String popisPostav(){
        String vracenyText;
        if (postavy.isEmpty()) {
           vracenyText = "V lokaci |" + nazev + "| není nic, s čím by se dalo bavit.";
        } else {        
           vracenyText = "Pro zkrácení dlouhé chvíle si můžeš popovídat s těmito postavami: ";
           for (String nazev : postavy.keySet()) {
               vracenyText += nazev + " | ";            
           }           
        }
        return vracenyText;
    }

    /**
     * Vrací prostor, který sousedí s aktuálním prostorem a jehož název je zadán
     * jako parametr. Pokud prostor s udaným jménem nesousedí s aktuálním
     * prostorem, vrací se hodnota null.
     *
     * @param nazevSouseda Jméno sousedního prostoru (východu)
     * @return Prostor, který se nachází za příslušným východem, nebo hodnota
     * null, pokud prostor zadaného jména není sousedem.
     */
    public Prostor vratSousedniProstor(String nazevSouseda) {
        List<Prostor>hledaneProstory = 
            vychody.stream()
                   .filter(sousedni -> sousedni.getNazev().equals(nazevSouseda))
                   .collect(Collectors.toList());
        if(hledaneProstory.isEmpty()){
            return null;
        }
        else {
            return hledaneProstory.get(0);
        }
    }

    /**
     * Vrací kolekci obsahující prostory, se kterými tento prostor sousedí.
     * Takto získaný seznam sousedních prostor nelze upravovat (přidávat,
     * odebírat východy) protože z hlediska správného návrhu je to plně
     * záležitostí třídy Prostor.
     *
     * @return Nemodifikovatelná kolekce prostorů (východů), se kterými tento
     * prostor sousedí.
     */
    public Collection<Prostor> getVychody() {
        return Collections.unmodifiableCollection(vychody);
    }
    
    
    /**
     * Vloží věc do prostoru
     *
     * @param vkládaná věc
     */
    public void vlozVec(Vec vkladanaVec){
        veci.put(vkladanaVec.getNazev(),vkladanaVec);
        notifyAllObservers();
    }
    
    /**
     * odebere věc z prostoru
     *
     * @param odebíraná věc
     */
    public Vec odeberVec(String nazevOdebiraneVeci){
        Vec odebirana = veci.remove(nazevOdebiraneVeci);
        notifyAllObservers();
        return odebirana;
    }
    
    /**
     * Vloží postavu do prostoru
     *
     * @param vkládaná postava
     */
    public void vlozPostavu(Postava vkladanaPostava){
        postavy.put(vkladanaPostava.getNazev(),vkladanaPostava);
    }
    
    /**
     * vrátí postavu prostoru
     *
     * @param název vrácené postavy
     * @return hledaná postava
     */
    public Postava ziskejPostavu(String nazevPostavy){   
        Postava hledanaPostava = null;
           for (String nazev : postavy.keySet()) {
               hledanaPostava = postavy.get(nazevPostavy);         
           }          
           return hledanaPostava;
    }
    
    /**
     * vrátí věc prostoru
     *
     * @param název vrácené věci
     * @return hledaná věc
     */
    public Vec ziskejVec(String nazevVeci){   
        Vec hledanaVec = null;
           for (String nazev : veci.keySet()) {
               hledanaVec = veci.get(nazevVeci);         
           }          
           return hledanaVec;
    }  
    
    /**
     * vrátí seznam věcí
     * @return veci 
     */
    public Map<String, Vec> ziskejVeci () {
        return this.veci;
    }
    
    /**
     * vrátí seznam postav
     * @return psotavy
     */
    public Map<String, Postava> ziskejPostavy (){
        return this.postavy;
    }
    
    /**
     * @return true/false pro test věci v lokaci
     */
    public boolean existenceVeciVProstoru (Vec testVec) {
        String nazevTestVec = testVec.getNazev();
        boolean navratExistenci;
        if (veci.get(nazevTestVec).getNazev().equals(nazevTestVec)) {
            navratExistenci = true;
        } else {
            navratExistenci = false;
        }
        return navratExistenci;
    }

    /**
     * 
     * @return posY
     */
    public Double getPosY() {
        return posY;
    }

    /**
     * 
     * @return posX
     */
    public Double getPosX() {
        return posX;
    }
    
    /**
     * 
     * @param posX 
     */
    public void setPosX(double posX) {
        this.posX = posX;
    }

    /**
     * 
     * @param posY 
     */
    public void setPosY(double posY) {
        this.posY = posY;
    }

    /**
     * 
     * @param posX
     * @return 
     */
    public double getPosX(double posX) {
        return posX;
    }

    /**
     * 
     * @param posY
     * @return 
     */
    public double getPosY(double posY) {
        return posY;
    }

    /**
     * 
     * @param observer 
     */
    public void registerObserver(utils.Observer observer) {
        listObservers.add(observer);
    }

    public void notifyAllObservers() {
        for (utils.Observer listObserver : listObservers) {
            listObserver.update();
        }
    }

    /**
     * 
     * @param observer 
     */
    @Override
    public void deleteObserver(utils.Observer observer) {
        listObservers.remove(observer);
    }
    
}
