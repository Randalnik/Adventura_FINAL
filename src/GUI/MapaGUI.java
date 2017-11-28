/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import logika.IHra;
import main.Main;
import utils.Observer;

/*******************************************************************************
 * Instance třídy MapaGUI představují...
 * @author    Jakub Skála
 * @version   1.0
 */
public class MapaGUI extends AnchorPane implements Observer{

    private IHra hra;
    private Circle tecka;
    
    public MapaGUI(IHra hra){
        this.hra = hra;
        hra.getHerniPlan().registerObserver(this);
        init();
    }
    
    // vrací @param obrazek s teckou
    private void init(){
        ImageView obrazek = new ImageView(new Image(Main.class.getResourceAsStream("/zdroje/mapa.png"),300,500,false,false));
        tecka = new Circle(10, Paint.valueOf("red"));
        this.getChildren().addAll(obrazek, tecka);
        update();
    }
    
//    private void show() {
//        
//        Alert alert = new Alert(Alert.AlertType.NONE);
//        alert.setTitle("Mapa");
//        alert.setGraphic(this);
//        alert.getDialogPane().setMinWidth(520);
//        alert.getDialogPane().setMaxWidth(520);
//        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
//
//        alert.showAndWait();
//    }
    
    // aktualizuje polohu tečky na mapě dle aktuální lokace
    @Override
    public void update() {
        this.setTopAnchor(tecka, hra.getHerniPlan().getAktualniProstor().getPosY());
        this.setLeftAnchor(tecka, hra.getHerniPlan().getAktualniProstor().getPosX());
        
//        show();
    }
    
    // při zavolání metody novaHra se zaregistruje nový observer a tecka se vrátí na startovní pozici
    public void novaHra(IHra hra) {
        hra.getHerniPlan().deleteObserver(this);
        this.hra = hra;
        hra.getHerniPlan().registerObserver(this);
        update();
        
    }
    
}
