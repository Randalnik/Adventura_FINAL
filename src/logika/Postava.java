/* Soubor je ulozen v kodovani UTF-8.
 * Kontrola kódování: Příliš žluťoučký kůň úpěl ďábelské ódy. */
package logika;

import javafx.scene.image.Image;



/*******************************************************************************
 * @author    Jakub Skála(skaj06)
 * @version   ZS 2016/2017
 */
public class Postava
{
    //== Datové atributy (statické i instancí)======================================
    private String nazev;
    private String popis;
    private Image image;
    //== Konstruktory a tovární metody =============================================

    /***************************************************************************
     *  Konstruktor ....
     */
    public Postava(String nazev, String popis, String image)
    {
        this.nazev = nazev;
        this.popis = popis;
        this.image = new Image(Vec.class.getResourceAsStream("/zdroje/" + image), 75, 75, false, false);
        
    }
    
    /**
     * Vrací image.
     * @return image
     */
    public Image getImage() {
        return image;
    }
    
    /**
     * 
     * @param image 
     */
    public void setImage(Image image) {
        this.image = image;
    }

    //== Nesoukromé metody (instancí i třídy) ======================================
    /**
     * @return String název postavy
     */
    public String getNazev(){
        return nazev;
    }
    
    /**
     * @return String popis postavy
     */
    public String getPopis(){
        return popis;
    }
    
    /**
     * 
     * @param popis 
     */
    public void setPopis(String popis){
        this.popis = popis;
    }
    

    //== Soukromé metody (instancí i třídy) ========================================

}
