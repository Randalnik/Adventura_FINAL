/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import logika.IHra;
import logika.Prostor;
import utils.Observer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.Main;

/*******************************************************************************
 * Instance třídy VychodyGUI představují...
 * @author    Jakub Skála
 * @version   1.0
 */
public class VychodyGUI extends AnchorPane implements Observer {
    
    private Main main;
    private IHra hra;
    private VBox vbox;
    private List<Button> buttons = new ArrayList<Button>();
    
    
    /**
    *  Konstruktor třídy
    *  
    *  @param hra hra, která obsahuje objekty celé adventury, které se dále z této třídy volají
    *  @param main main, který obsahuje borderPane, na který se zobrazují předměty a text z interakce
    */   
    public VychodyGUI(IHra hra, Main main) {
        
        this.main = main;
        this.hra = hra;
        hra.getHerniPlan().registerObserver(this);
        
        // Vbox
        vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);
        vbox.setMinWidth(100);

        // Label
        Label label = new Label("Východy:");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
    
        vbox.getChildren().add(label);
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
        
        vbox.getChildren().removeAll(buttons);
        buttons.clear();
        
        Collection<Prostor> vychody = hra.getHerniPlan().getAktualniProstor().getVychody();
        
        for (Prostor prostor: vychody) {
            
            Button btn = new Button(prostor.getNazev());
            vbox.getChildren().add(btn);
            
            buttons.add(btn);
            
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    if (hra.konecHry()) { return; }
                    main.getCenterText().appendText ("\n\n");
                    main.getCenterText().appendText(hra.zpracujPrikaz("jdi " + prostor.getNazev()));
                }
            });
        }
    }

         /**
     * Metoda na obnovení hry
     *  @param hra hra, která obsahuje objekty celé adventury, které se dále z této třídy volaji
     */
    //@Override
    public void novaHra(IHra hra) {

        hra.getHerniPlan().deleteObserver(this);
        
        this.hra = hra;
        
        hra.getHerniPlan().registerObserver(this);
        update();
    }
    
}

