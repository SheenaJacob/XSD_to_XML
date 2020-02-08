

import com.advantest.tcct.recipe.Recipe;
import java.awt.Desktop;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author SheenaJacob
 */
public class recipe extends Application {
    
    //initiliazing the layout elements
    Stage window;
    Scene scene1,scene2,scene3;
    private final Desktop desktop = Desktop.getDesktop();
    
    //creating an instance of the Recipe file
    Recipe r=new Recipe();  
    Recipe r1=new Recipe();
    
     // creating an instance of the Tabbed Pane class
    reflection reflect=new reflection();
    
    // creating an instance of the Layout class
    Layout layout_re=new Layout(r,reflect);
    //creating an instance of the marshall_unmarshall class
    MarshallUnmarshall jb = new MarshallUnmarshall();
    
    // creating the main frame: scene 1
    Scene display_scene1()
    {
        Label l1=new Label(" Welcome to your XML generator");
        //Label l2=new Label("Upload the XSD File");
                       
         // Using File Chooser to get the File Path of the XSD file
         //FileChooser fileChooser = new FileChooser();
         //TextArea textArea = new TextArea();
         //textArea.setMinHeight(10);
         //Button b= new Button("Upload");
         //allowing only XSD files to be accepted
         //FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XSD", "*.xsd");
         //fileChooser.getExtensionFilters().add(extFilter);
         //b.setOnAction((ActionEvent event) -> {
           //  textArea.clear();
             //File file = fileChooser.showOpenDialog(window);
             //if (file != null)
             //{
               //  String g=file.getAbsolutePath();
                 //textArea.appendText(g + "\n");
                 //System.out.println(g);
             //}
         //});
         
        Button create = new Button();
        create.setText("Create an XML document");
        Button edit = new Button();
        edit.setText(" Edit an XML Document");
        //Actions on setting the button
        create.setOnAction(e -> window.setScene(scene2));
        edit.setOnAction(e -> {r1=jb.edit(r);layout_re.r=r1;scene3=display_scene3();window.setScene(scene3);});
        //Creating the layout for the first scene: Initial window of the application
        HBox hb=new HBox(10);
        hb.getChildren().addAll(create,edit);
        VBox layout1= new VBox(20);
        layout1.setPadding(new Insets(50, 50, 50, 50)); 
        //layout1.getChildren().addAll(l1,l2,textArea,b,hb);
        layout1.getChildren().addAll(l1,hb);
        scene1=new Scene(layout1,500,500);
        return scene1;
    }
    //creating the scene where an xml document is created
     Scene display_scene2()
    {
      
        Button finish2=new Button("Finish");
        Button home=new Button("Back to Home");
        home.setOnAction(e -> window.setScene(scene1));
        HBox h2=new HBox(5,finish2,home);
        AnchorPane layout3=new AnchorPane();
        //layout3.setPadding(new Insets(50, , 10, 20)); 
        AnchorPane.setRightAnchor(h2,10d);
        AnchorPane.setBottomAnchor(h2,10d);
        layout3.getChildren().addAll(h2);
        TabPane tp2=new TabPane();
        Class classname;
        classname = r.getClass();
        Object obj;
        obj = r;
        String[] fields=reflect.get_Fields(classname);
        String[] types=reflect.getTypes(classname);
        String[] methods=reflect.get_Methods(classname);
        String node="Recipe";
        TabPane tabs=new TabPane();
        VBox v1=new VBox(20);
        tabs=layout_re.createTabs(node, fields, types, methods, tp2, classname, obj,v1);
        AnchorPane layout=new AnchorPane();
        layout.getChildren().addAll(tabs);
        layout3.getChildren().addAll(layout);
        finish2.setOnAction(e -> {jb.printXML(r);System.out.println("The XML document has been sucessfully edited");});
        scene2=new Scene(layout3,1050,900);
        return scene2;
              
      
    }
     
      //creating the scene where the XML document is edited
      Scene display_scene3()
    {
      
       Button finish2=new Button("Finish");
       Button home=new Button("Back to Home");
       home.setOnAction(e -> window.setScene(scene1));
       HBox h2=new HBox(5,finish2,home);
       AnchorPane layout3=new AnchorPane();
       AnchorPane.setRightAnchor(h2,10d);
       AnchorPane.setBottomAnchor(h2,10d);
       layout3.getChildren().addAll(h2);
       TabPane tp2=new TabPane();
       Class classname;
        classname = r1.getClass();
        Object obj;
        obj = r1;
        String[] fields=reflect.get_Fields(classname);
        String[] types=reflect.getTypes(classname);
        String[] methods=reflect.get_Methods(classname);
        String node="Recipe";
        TabPane tabs=new TabPane();
        VBox v1=new VBox(20);
        tabs=layout_re.createTabs(node, fields, types, methods, tp2, classname, obj,v1);
        AnchorPane layout=new AnchorPane();
        layout.getChildren().addAll(tabs);
        layout3.getChildren().addAll(layout);
       finish2.setOnAction(e -> {jb.printXML(r1);System.out.println("The XML document has been sucessfully edited");});
       scene3=new Scene(layout3,1050,900);
       return scene3;
              
      
    }
    
    
    @Override
    public void start(Stage primaryStage)
    {
       window=primaryStage;
       scene1=display_scene1();
       scene2=display_scene2();
       window.setTitle("XML EDITOR");
       window.setScene(scene1);
       window.centerOnScreen();
       window.show();
    }
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
