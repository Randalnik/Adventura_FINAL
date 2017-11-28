/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import logika.IHra;
import utils.Observer;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.AnchorPane;
import logika.Vec;
import main.Main;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import logika.Prostor;

/*******************************************************************************
 * Instance třídy ObjektyGUI představují...
 * @author    Jakub Skála
 * @version   1.0
 */
public class ObjektyGUI extends AnchorPane implements Observer {
    
    private IHra hra;
    private Map<String, Vec> veci = new HashMap<String, Vec>();
    private List<ImageView> obrazkyObjektu = new ArrayList<ImageView>();
    private List<HBox> radky = new ArrayList<HBox>();

    private VBox vbox;
    private Main main;
    private Map<String, Prostor> prostory;

    /**
    *  Konstruktor třídy
    *  
    *  @param hra hra, která obsahuje objekty celé adventury, které se dále z této třídy volají
    *  @param main main, který obsahuje borderPane, na který se zobrazují předměty a text z interakce
    */   
    public ObjektyGUI(IHra hra, Main main) {
        
        this.hra = hra;
        this.main = main;
        hra.getHerniPlan().registerObserver(this);
        hra.getHerniPlan().getBatoh().registerObserver(this);
        
        prostory = hra.getHerniPlan().getProstory();
        
        for (String nazev : prostory.keySet()) {
            Prostor prostor = prostory.get(nazev);
            prostor.registerObserver(this);
        }

         // Vbox
        vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);
        vbox.setMinWidth(100);

        // Label
        Label zadejPrikazLabel = new Label("Objekty v prostoru:");
        zadejPrikazLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
    
        vbox.getChildren().add(zadejPrikazLabel);
        this.getChildren().add(vbox);  
        
        update();
    }

     /**
     * Metoda bez parametru
     * tato metoda je zavolána, když je ze Subjectu zavoláno notifyAllObservers();
     *
     */
    @Override
    public void update() {
        
        vbox.getChildren().removeAll(radky);
        radky.clear();
        
        veci = hra.getHerniPlan().getAktualniProstor().ziskejVeci();
        
        int i = 0;
        
        for (String polozka : veci.keySet()) {
            Vec vec = veci.get(polozka);
                        
            boolean jdeZvednout = vec.muzuZvednout();
            
            HBox hbox = new HBox();
            hbox.setPadding(new Insets(0));
            hbox.setSpacing(10);
            hbox.setMinWidth(100);
        
            ImageView obrazek = new ImageView(vec.getImg());
            
            double top = i * 75;
            this.setTopAnchor(obrazek, top);            
            
            Button hled = new Button ("Prohledej");
            Button btn = new Button("Seber");
                
            //btn = new Button("Seber");

            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    if (hra.konecHry()) { return; }
                    main.getCenterText().appendText ("\n\n");
                    main.getCenterText().appendText(hra.zpracujPrikaz("seber " + vec.getNazev()));      

                    if (!hra.getHerniPlan().getBatoh().getSeznamVeci().containsValue(vec)) {

                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Informace");
                        alert.setHeaderText("Tento objekt nelze přidat");
                        alert.setContentText("Tento objekt není přenositelný");

                        alert.showAndWait();
                    }
                }
            });
            
            hled.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    if (hra.konecHry()) { return; }
                    main.getCenterText().appendText ("\n\n");
                    main.getCenterText().appendText(hra.zpracujPrikaz("prohledej " + vec.getNazev()));      
                    
                    
//                    if (!hra.getHerniPlan().getBatoh().getSeznamVeci().containsValue(vec)) {
//                     
//                        Alert alert = new Alert(AlertType.INFORMATION);
//                        alert.setTitle("Informace");
//                        alert.setHeaderText("Tento objekt nelze prohledat.");
//                        alert.setContentText("Tento objekt není možné prohledat.");
//                        
//                        alert.showAndWait();
//                    }
                }
            });
            
            // na základě hodnoty jdeZvednout se generují tlačítka
            if(jdeZvednout){
                hbox.getChildren().addAll(obrazek, btn); 
            }
            else{
                hbox.getChildren().addAll(obrazek, hled);
            }
            
            radky.add(hbox);
            i++;
            
        }
        
        vbox.getChildren().addAll(radky);
    }
    
     /**
     * Metoda na obnovení hry
     *  @param hra hra, která obsahuje objekty celé adventury, které se dále z této třídy volaji
     */
    //@Override
    public void novaHra(IHra hra) {

        hra.getHerniPlan().deleteObserver(this);
        hra.getHerniPlan().getBatoh().registerObserver(this);

        this.hra = hra;
        
        hra.getHerniPlan().registerObserver(this);
        hra.getHerniPlan().getBatoh().registerObserver(this);
        update();
    }
}