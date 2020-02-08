import com.advantest.tcct.recipe.Recipe;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.w3c.dom.Element;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SheenaJacob
 */
// This class creates a tabbed pane with all the eements of the recipe file
public class Layout {
     reflection reflect=new reflection();
     Recipe r;
     public Layout(Recipe r, reflection reflect)
     {
         this.r=r;
         this.reflect=reflect;
     }
     TabPane createTabs(String node,String[] fields,String[] types,String[] methods,TabPane tp,Class classname,Object obj,VBox v1)
    {
            //create Tabs for each node
            Tab tab=new Tab(node);  
            //create Labels for each tab
            
            createLabel(fields,types,methods,tab,tp,classname,obj,v1);   
            // add tab 
            tp.getTabs().add(tab); 
            return tp;        
    }
   // creates Labels and text boxes according to the data type of the xnl elements 
Object createLabel(String[] fields,String[] types,String[] methods, Tab t,TabPane tp2,Class classname,Object obj,VBox v1) 
    {                  
                
        v1.setPadding(new Insets(50, 0, 0, 50)); 
        // for alignment
        int width_difference=maxLength(fields);
        //setter and getter methods for the fields of a node
        String[] getter_methods=reflect.getter_methods(methods);
        String[] setter_methods=reflect.setter_methods(methods);
        String[] is_methods=reflect.is_methods(methods);
        Object object=obj;
            for(int j=0;j<fields.length;j++)
            {
                // difference holds the alignment width fro the VBOX
                int difference=((width_difference-(fields[j].length())))*9+20;
                //creates a new label with the name o the field
                Label l1= new Label(fields[j]);
                // creates an array of strings with the set and get method names
                String method_name="null";
                String method_name_get="get"+fields[j].substring(0,1).toUpperCase()+fields[j].substring(1).toLowerCase();
                String method_name_set="set"+fields[j].substring(0,1).toUpperCase()+fields[j].substring(1).toLowerCase();
                String method_name_isbool="is"+fields[j].substring(0,1).toUpperCase()+fields[j].substring(1).toLowerCase();
                // if the field is of type string then it creates a string        
                if (types[j].equalsIgnoreCase("String"))
                {
                    // first checks if the method belongs to the node's class
                    if(found(method_name_get,getter_methods)==true)
                    {
                        //creates a new TextField for every element of type string
                        TextField text= new TextField();
                        text.setPrefSize(400,30);
                        // The save button is required to update the values in the XML document
                        Button b=new Button("Save");
                        //method_name1 holds the getter Method as stored in the class("required because of changes in the case")
                        String method_name_getString=method_name(method_name_get,getter_methods);
                        // when a document is edited it gets the values from the already existing XML document
                        String text_val=(reflect.getValue(method_name_getString,classname,obj));
                        //sets the retrieved value from the XML document in the textbox
                        text.setText(text_val);
                        String method_name_setString=method_name(method_name_set,setter_methods);
                        b.setOnAction(e->{reflect.setValue(method_name_setString,text.getText(),classname,object);});
                        HBox h1=new HBox(difference,l1,text);
                        HBox h2=new HBox(30,h1,b);
                        v1.getChildren().add(h2);
                    }
                }
                if (types[j].equalsIgnoreCase("Integer"))
                {
                    int value=0;
                    // first checks if the method belongs to the node's class
                    if(found(method_name_get,getter_methods)==true)
                    {
                        //creates a new TextField for every element of type string
                        TextField text= new TextField();
                        // The save button is required to update the values in the XML document
                        Button b=new Button("Save");
                        //method_name1 holds the getter Method as stored in the class("required because of changes in the case")
                        String method_name_getint=method_name(method_name_get,getter_methods);
                        //when a document is edited it gets the values from the already existing XML document
                        value=(reflect.getIntValue(method_name_getint,classname,obj));
                        //sets the retrieved value from the XML document in the textbox
                        String text_val=String.valueOf(value);
                        text.setText(text_val);
                        String method_name_setString=method_name(method_name_set,setter_methods);
                        b.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                int res=0;
                                  try{
                                         res=Integer.parseInt(text.getText());
                                     }
                                  catch(NumberFormatException nfe)
                                    {
                                      Alert alert = new Alert(AlertType.INFORMATION, "Please enter a Number", ButtonType.OK);
                                      alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                      alert.show();
                                    }
                                   int result=res;
                                reflect.setIntValue(method_name_setString,result,classname,object);
                            }
                        });
                      
                       // }
                        HBox h1=new HBox(difference,l1,text);
                        HBox h2=new HBox(30,h1,b);
                        v1.getChildren().add(h2);
                    }
                }
                // creates checkboxes for fields of type boolean
                if(types[j].equals("Boolean"))
                {
                    if(found(method_name_isbool,is_methods)==true)
                    {
                        try {
                           
                            CheckBox cb=new CheckBox();
                            cb.setIndeterminate(false);
                            String method_name3=method_name(method_name_isbool,is_methods);
                            cb.setSelected(reflect.isValue(method_name3,classname,obj));   
                            HBox h1=new HBox(difference,l1,cb);
                            String method_name4=method_name(method_name_set,setter_methods);
                            cb.setOnAction((ActionEvent e) -> {
                            reflect.setBoolValue(method_name4,cb.isSelected(),classname,object);
                             });
                            v1.getChildren().add(h1);
                        } catch (NoSuchMethodException ex) {
                          Logger.getLogger(Layout.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                if(types[j].equals("boolean"))
                {
                    if(found(method_name_isbool,is_methods)==true)
                    {
                        try {
                            CheckBox cb=new CheckBox();
                            cb.setIndeterminate(false);
                            String method_name3=method_name(method_name_isbool,is_methods);
                            cb.setSelected(reflect.isValue(method_name3,classname,obj));
                            HBox h1=new HBox(difference,l1,cb);
                            String method_name4=method_name(method_name_set,setter_methods);
                            //cb.setOnAction(e-> rt.setBoolValue(method_name3,true,r) );
                            cb.setOnAction((ActionEvent e) -> {
                            reflect.setbooleanValue(method_name4,cb.isSelected(),classname,object);
                            });
                            v1.getChildren().add(h1);
                        } catch (NoSuchMethodException ex) {
                            Logger.getLogger(Layout.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
 
                //creates a new tab for fields that have a subclass 
                if(types[j].startsWith("com")) 
                {
                   // when the submit button is pressed the nodes values are sent to the calling class
                   // For example for the smartest node when submit is pressed the getSmartest(Smart value) is given the values of the new object created
                   Button submit=new Button("Save All Fields");
                   // classname is stored in types[j]
                   VBox v2=new VBox(20);
                   v2.setCenterShape(true);
                   String class_name=types[j];
                   // declares all the required fields to create a label
                   Class new_classname;
                   Object new_object;
                    Object new_object1;
                   Object get_new_object;
                   Object new_object_pass;
                   String new_node = null;
                   String[] new_fields;
                   String[] new_methods;
                   String[] new_types;
                   String called_class;
                   String get_class;
                   v2.getChildren().addAll(submit);
                    try {
                        //creates a new class
                        new_classname=Class.forName(class_name);
                        //used for classes that inherit from java.lang.Enum
                        if(new_classname.getSuperclass().getName().equals("java.lang.Enum"))
                                {
                                 Button save1=new Button("Save");
                                 ComboBox cb=new ComboBox();
                                 HBox h1=new HBox(5);
                                 Label enum_label=new Label();
                                 new_fields=reflect.get_Fields(new_classname);
                                if(new_classname.getName().equals("com.advantest.tcct.recipe.TestType"))
                                {
                                    enum_label.setText("Test Type");
                                    String[] enum_values=reflect.get_Enum_values(new_fields);
                                    for(String loop:enum_values)
                                    {
                                        cb.getItems().addAll(loop);
                                    }                         
                                h1.getChildren().addAll(enum_label,cb,save1);
                                v1.getChildren().addAll(h1);
                                try {
                                    save1.setOnAction((ActionEvent e)->{
                                       try {                                        
                                            String value=(String) cb.getValue();
                                            reflect.setTestType(value,object);
                                            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
                                      Logger.getLogger(Layout.class.getName()).log(Level.SEVERE, null, ex);
                                      }
                                    });
                                } catch (IllegalArgumentException ex) {
                                    Logger.getLogger(Layout.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                }
                                if(new_classname.getName().equals("com.advantest.tcct.recipe.WhichTest"))
                                {
                                    enum_label.setText("WhichTest");
                                    String[] enum_values=reflect.get_Enum_values(new_fields);
                                    for(String loop:enum_values)
                                    {
                                        cb.getItems().addAll(loop);
                                    }
                                
                                    h1.getChildren().addAll(enum_label,cb,save1);
                                    v1.getChildren().addAll(h1);
                                  
                                try {
                                    save1.setOnAction((ActionEvent e)->{
                                        try {
                                            String value=(String) cb.getValue();
                                            try {
                                                reflect.setWhichTest(value,object);
                                            } catch (IllegalAccessException | InvocationTargetException ex) {
                                                Logger.getLogger(Layout.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        } catch (IllegalArgumentException ex) {
                                            Logger.getLogger(Layout.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    });
                                } catch (IllegalArgumentException ex) {
                                    Logger.getLogger(Layout.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                }
                            }
                                            else if(new_classname.getSuperclass().getName().equals("com.advantest.tcct.recipe.Action"))
                        {
                            String ex="com.advantest.tcct.recipe.Exec";
                            String var="com.advantest.tcct.recipe.Variable";           
                            try {
                                  Class  variable = Class.forName(var);
                                  Class exec = Class.forName(ex);
                                    try {
                                                Button after_save=new Button("Save");
                                                Object exec_object = exec.newInstance();
                                                Object no = new_classname.newInstance();
                                                Object var_object=variable.newInstance();
                                                String node_baa=class_name.substring(26);
                                                Label node_l=new Label(node_baa);
                                                 v1.getChildren().addAll(node_l,after_save);
                                                String m="set"+node_baa;                                       
                                                String[] fields1=reflect.get_Fields(exec);
                                                String[] types1=reflect.getTypes(exec);
                                                String[] methods1=reflect.get_Methods(exec);
                                                String[] fields2=reflect.get_Fields(variable);
                                                String[] types2=reflect.getTypes(variable);
                                                String[] methods2=reflect.get_Methods(variable);
                                                Object exo=createLabel(fields1, types1, methods1, t, tp2, exec,exec_object , v1);
                                                Object v0=createLabel(fields2, types2, methods2, t, tp2, variable, var_object, v1);
                                                after_save.setOnAction((ActionEvent e)->{
                                                  try {
                                                        Object  o = reflect.Set_After_before1(new_classname,no,exo,v0);
                                                      try {
                                                          reflect.setObject(m, obj, o);
                                                      } catch (InvocationTargetException ex1) {
                                                          Logger.getLogger(Layout.class.getName()).log(Level.SEVERE, null, ex1);
                                                      }
                                                    } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException | ClassNotFoundException | InstantiationException ex1) {
                                                        Logger.getLogger(Layout.class.getName()).log(Level.SEVERE, null, ex1);
                                                    }
                                                    
                                                });
                                             } catch (InstantiationException | IllegalAccessException ex1) {
                                                 Logger.getLogger(Layout.class.getName()).log(Level.SEVERE, null, ex1);
                                             }
                                        
                            } catch (ClassNotFoundException ex1) {
                                Logger.getLogger(Layout.class.getName()).log(Level.SEVERE, null, ex1);
                            }
                    }
                        else
                        {
                        try {               
                             //creates an object of the new class
                            new_object=new_classname.newInstance();
                            new_object1=new_classname.newInstance();
                            get_new_object=new_classname.newInstance();
                            // Assigns the tab the name stored in new_node
                            new_node=class_name.substring(26);                    
                            //gets the fields,methods and tyes of the new subclass
                            new_fields=reflect.get_Fields(new_classname);
                            new_types=reflect.getTypes(new_classname);
                            new_methods=reflect.get_Methods(new_classname);
                            //checks if the Recipe class holds these values
                            if(found(class_name,reflect.getTypes(r.getClass()))== true)
                            {
                                 get_class="get"+new_node;
                                 String method_name7=method_name(get_class,getter_methods);
                                 try {
                                      //When editing a document it is required to get the fields from existing
                                      new_object1=reflect.getObject(method_name7, obj, get_new_object);
                                      } catch (IllegalArgumentException | InvocationTargetException ex) {
                                  Logger.getLogger(Layout.class.getName()).log(Level.SEVERE, null, ex);
                                  }
                                called_class="set"+new_node;
                                String method_getObject=method_name(called_class,reflect.setter_methods(reflect.get_Methods(r.getClass())));
                                // create a new tab
                                createTabs(new_node,new_fields,new_types,new_methods,tp2,new_classname,new_object1,v2);
                                new_object_pass=new_object1; 
                                submit.setOnAction(e-> {
                                try {
                                     reflect.setObject(method_getObject, object, new_object_pass);
                                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                                    Logger.getLogger(Layout.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            });
                            }
                            // if the element is a subclass of a another node bulids a small pane within the tab  
                            else
                            {
                             
                              AnchorPane newpane=new AnchorPane();
                              Label new_label=new Label(new_node);
                              newpane.getChildren().add(new_label);
                              v2.getChildren().addAll(newpane);
                              v1.getChildren().addAll(v2);                              
                              String method_name8=new_object.getClass().getName().substring(26);
                              called_class="set"+method_name8;
                              if(new_classname.getSuperclass().getName().equalsIgnoreCase("java.lang.Object"))
                              {
                                if(new_object.getClass().getName().equalsIgnoreCase("com.advantest.tcct.recipe.EnvType"))
                                {
                                    Label interface_label=new Label(new_fields[0]);
                                    String method_name_interface="get"+new_fields[0].substring(0,1).toUpperCase()+new_fields[0].substring(1).toLowerCase();
                                    Button add=new Button("+");                    
                                    v1.getChildren().addAll(interface_label,add);
                                    List a=reflect.getListValue(method_name_interface, new_classname, new_object);
                                    add.setOnAction((ActionEvent e) -> {
                                    TextField tag_name=new TextField();
                                    TextField content= new TextField();
                                    Button save=new Button("Save");
                                    HBox h2= new HBox(5);
                                    h2.getChildren().addAll(tag_name,content,save);
                                    v1.getChildren().add(9,h2);
                                    save.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent e) {
                                    String tag=tag_name.getText();
                                    String val=content.getText();
                                    Element element=reflect.create_Element(tag, val);
                                    a.add((Object)element);
                                    } });
                                });
                                called_class="setEnv";
                              }
                              else
                              {
                                 {
                                      createLabel(new_fields,new_types,new_methods,t,tp2,new_classname,new_object,v2);                                
                                 }
                              }
                               String method_getObject=method_name(called_class,reflect.setter_methods(reflect.get_Methods(obj.getClass())));
                               new_object_pass=new_object; 
                               submit.setOnAction(e-> {
                                try {
                                      reflect.setObject(method_getObject, object, new_object_pass);
                                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                                  Logger.getLogger(Layout.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }); 
                                }
                              else
                              {  
                                Class super_c=new_classname.getSuperclass();
                                String[] field_super=reflect.get_Fields(super_c);
                                String[] fields_c= combine(new_fields,field_super);
                                String[] methods_super=reflect.get_Methods(super_c);
                                String[] methods_c= combine(new_methods,methods_super);
                                String[] types_super=reflect.getTypes(super_c);
                                String[] types_c= combine(new_types,types_super);
                                //Object check=createLabel(fields_c,types_c,methods_c,t,tp2,new_classname,new_object,v1);
                                createTabs(new_node,fields_c,types_c,methods_c,tp2,new_classname,new_object,v2);
                                String cc=called_class.replace("Type","");
                                String method_getObject=method_name(cc,reflect.setter_methods(reflect.get_Methods(obj.getClass())));                                 
                                submit.setOnAction(e-> {
                                try {
                                      reflect.setObject(method_getObject, object, new_object);
                                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                                    Logger.getLogger(Layout.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }); 
                              }
                            }
                        } catch (InstantiationException | IllegalAccessException ex) {
                            Logger.getLogger(Layout.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        }
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Layout.class.getName()).log(Level.SEVERE, null, ex);
                    }
                 }
                if(types[j].startsWith("interface"))
                {
                    Label interface_label=new Label(fields[j]);
                    String method_name_interface="get"+fields[j].substring(0,1).toUpperCase()+fields[j].substring(1).toLowerCase();
                    Button add=new Button("+");                    
                    v1.getChildren().addAll(interface_label,add); 
                    try {
                         String para_type=reflect.get_parameterizedType(classname);
                         if (para_type.startsWith("com"))
                            { 
                            String class_name_list=para_type;
                            Class new_class=Class.forName(class_name_list);
                            String new_node=classname.getName().substring(26);
                            // gets the fields,methods and tyes of the new subclass
                            String[] new_fields=reflect.get_Fields(new_class);
                            String[] new_types=reflect.getTypes(new_class);
                            String[] new_methods=reflect.get_Methods(new_class);
                            List a=reflect.getListValue(method_name_interface, classname, obj);
                            add.setOnAction((ActionEvent e) -> {
                            try {
                                Object o=new_class.newInstance();
                                VBox v2=new VBox(20);
                                createTabs(new_node,new_fields,new_types,new_methods,tp2,new_class,o,v2);
                                a.add(o);                          
                           } catch (InstantiationException | IllegalAccessException ex) {
                             Logger.getLogger(Layout.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                      }
                       if(para_type.startsWith("Element"))
                       {
                        System.out.println(method_name_interface);
                        List a=reflect.getListValue(method_name_interface, classname, obj);
                        add.setOnAction((ActionEvent e) -> {
                        TextField tag_name=new TextField();
                        TextField content= new TextField();
                        Button save=new Button("Save");
                        HBox h2= new HBox(5);
                        h2.getChildren().addAll(tag_name,content,save);
                        v1.getChildren().add(9,h2);
                        save.setOnAction(new EventHandler<ActionEvent>() {
                           @Override
                           public void handle(ActionEvent e) {
                               String tag=tag_name.getText();
                               String val=content.getText();
                               Element element=reflect.create_Element(tag, val);
                               a.add(e);         
                           }
                           });
                           });
                        }   
                   } catch (ClassNotFoundException ex) {
                   //Logger.getLogger(Tabbedpane.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
             }            
              t.setContent(v1); 
              return obj;
       }
  
  
  //finds the length of the longest String
    int maxLength(String[] array)
    {
        int maxLength = 0;
        String longestString = null;
        for (String s : array) 
        {
          if (s.length() > maxLength) 
          {
              maxLength = s.length();
              longestString = s;
          }
        }
      return longestString.length();
    }
      String[] combine(String[] a1, String[] a2)
    {
        int a=a1.length+a2.length;
        String[] result=new String[a];
        int j=0;
         for (String a11 : a1) {
             result[j] = a11;
             j++;
         }
         for (String a21 : a2) {
             result[j] = a21;
             j++;
         }   
        return result;
    }
    
    // method to find if a string exists in an array
    Boolean found(String a, String[]arr)
    {
        boolean found = false;
        for (String element:arr )
        {
            if ( element.equalsIgnoreCase(a))
            {
            found = true;
            }
        }
        return found;
    }
       
    //method to check if a string is found in an array(Case insensitive) and return the  real name( the required case) found in the array/
    String method_name (String a, String[]arr)
    {
        int index=0;
        int i=0;
        for (String element:arr )
        {
            if ( element.equalsIgnoreCase(a))
            {
                index=i;
            }
            i++;
        }
        return arr[index];
    }
    
}
