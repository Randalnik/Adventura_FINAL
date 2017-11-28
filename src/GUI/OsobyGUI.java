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
import logika.Postava;
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

/*******************************************************************************
 * Instance třídy OsobyGUI představují...
 * @author    Jakub Skála
 * @version   1.0
 */
public class OsobyGUI extends AnchorPane implements Observer {
    
    private IHra hra;
    private Map<String, Postava> person = new HashMap<String, Postava>();
    private List<ImageView> obrazkyObjektu = new ArrayList<ImageView>();
    private List<HBox> radky = new ArrayList<HBox>();

    private VBox vbox;
    private Main main;

    /**
    *  Konstruktor třídy
    *  
    *  @param hra hra, která obsahuje objekty celé adventury, které se dále z této třídy volají
    *  @param main main, který obsahuje borderPane, na který se zobrazují osoby a text z interakce
    */   
    public OsobyGUI(IHra hra, Main main) {
        
        this.hra = hra;
        this.main = main;
        hra.getHerniPlan().registerObserver(this);
        hra.getHerniPlan().getBatoh().registerObserver(this);

         // Vbox
        vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);
        vbox.setMinWidth(100);

        // Label
        Label zadejPrikazLabel = new Label("Postavy v prostoru:");
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
        
        person = hra.getHerniPlan().getAktualniProstor().ziskejPostavy();
        
        int i = 0;
        
        for (String polozka : person.keySet()) {
            Postava osoba = person.get(polozka);
            
            
            HBox hbox = new HBox();
            hbox.setPadding(new Insets(0));
            hbox.setSpacing(10);
            hbox.setMinWidth(100);
        
            ImageView obrazek = new ImageView(osoba.getImage());
            
            double top = i * 75;
            this.setTopAnchor(obrazek, top);            

            Button btn = new Button("Mluv");
            
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                     
                    if (hra.konecHry()) { return; }
                    String promluva = osoba.getPopis();
                    main.getCenterText().appendText ("\n\n");
                    main.getCenterText().appendText(hra.zpracujPrikaz("mluv " + osoba.getNazev()));      
                    
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Informace");
                    alert.setHeaderText("Postava říká:");
                    alert.setContentText(promluva);

                    alert.showAndWait();
                }
            });

            hbox.getChildren().addAll(obrazek, btn);
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