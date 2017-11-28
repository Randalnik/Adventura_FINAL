/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import logika.IHra;
import logika.Vec;
import main.Main;
import utils.Observer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


/*******************************************************************************
 * Instance třídy BatohGUI představují...
 * @author    Jakub Skála
 * @version   1.0
 */
public class BatohGUI extends AnchorPane implements Observer {

    private IHra hra;
    private Map<String, Vec> polozky;
    private List<HBox> radky = new ArrayList<HBox>();
    
    private Main main;
    private VBox vbox;

    /**
    *  Konstruktor třídy
    *  
    *  @param hra hra, která obsahuje objekty celé adventury, které se dále z této třídy volají
    *  @param main main, který obsahuje borderPane, na který se zobrazují předměty a text z interakce
    */   
    public BatohGUI (IHra hra, Main main) {
        
        this.hra = hra;
        this.main = main;
        hra.getHerniPlan().registerObserver(this);
        hra.getHerniPlan().getBatoh().registerObserver(this);
        polozky = new HashMap<String, Vec>();
        
        // Vbox
        vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);
        vbox.setMinWidth(100);

        // Label
        Label zadejPrikazLabel = new Label("Tvoje vybavení:");
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
        
        polozky = hra.getHerniPlan().getBatoh().getSeznamVeci();

        double i = 0;
        
          for (String polozka : polozky.keySet()) {
            Vec vec = polozky.get(polozka);
            
            boolean jeCitelna = vec.jeCitelna();
        
            HBox hbox = new HBox();
            hbox.setPadding(new Insets(0));
            hbox.setSpacing(10);
            hbox.setMinWidth(100);
            
            ImageView obrazek = new ImageView(vec.getImg());
            
            double top = i * 75;
            this.setTopAnchor(obrazek, top);
            
            Button btn = new Button("Vyhoď");
            
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    if (hra.konecHry()) { return; }
                    main.getCenterText().appendText ("\n\n");
                    main.getCenterText().appendText(hra.zpracujPrikaz("odhod "+ vec.getNazev()));
                    if (!hra.getHerniPlan().getBatoh().getSeznamVeci().containsValue(vec)) {
                     
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Informace");
                        alert.setHeaderText("Odhodil jsi předmět!");
                        
                        alert.showAndWait();
                    }
                }
            });
            Button btn1 = new Button("Čti");
            
            btn1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    if (hra.konecHry()) { return; }
                    main.getCenterText().appendText ("\n\n");
                    main.getCenterText().appendText(hra.zpracujPrikaz("cti "+ vec.getNazev()));
                }
            });
            
            // podle čitelnosti předmětu se předmětu přiřazují tlačítka
            if (jeCitelna) {
                hbox.getChildren().addAll(obrazek, btn, btn1);
            }
            else {
                hbox.getChildren().addAll(obrazek, btn);
            }
            radky.add(hbox);
            
            i++;
        }
        
        vbox.getChildren().addAll(radky);
//        hra.getHerniPlan().notifyAllObservers();
    }

     /**
     * Metoda na obnovení hry
     *  @param hra hra, která obsahuje objekty celé adventury, které se dále z této třídy volaji
     */
   
    //@Override
    public void novaHra(IHra hra) {
        
        hra.getHerniPlan().getBatoh().deleteObserver(this);
        hra.getHerniPlan().deleteObserver(this);
        
        this.hra = hra;
        
        hra.getHerniPlan().getBatoh().registerObserver(this);
        hra.getHerniPlan().registerObserver(this);
        update();
    }
    
}
