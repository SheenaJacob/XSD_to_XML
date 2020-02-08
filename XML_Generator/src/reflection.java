
import com.advantest.tcct.recipe.TestType;
import com.advantest.tcct.recipe.WhichTest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 Research Project: Using XSD Schema to Create and Modify XML FIles
 Class: reflection
 Aim:This class is created so as to use Java Reflections to get the various Fields and to use Methods of a class at runtime
 Last Updated: 06/01/20
 @author SheenaJacob
 */

//This class extracts fields, field types, methods(getter methods, setter methods,boolean ismethods),and functions to set and get values from a another class
public class reflection {
    
     // Pass in the class name and extract fields elements as a String array
    String[] get_Fields(Class classname)
        {
            List<String> a= new ArrayList<>();
            int i=0;
            for(Field f:classname.getDeclaredFields())
            {
                a.add(i,f.getName());
                i++;
            }
            String[] result=List_to_Array(a);
            return result;   
        }
    
     /*  This method returns a string with all the field types. Uses the class substring because field types are returned as class java.String
        or class package name, so we first remove class from the String and check if the rest of the String starts with java.lang .
        If so it retrives only the datatypes ex String Boolean otherwise it saves the package name.*/
    String[] getTypes(Class classname)
          {
            List<String> b= new ArrayList<>();
            int i=0;
            for(Field f:classname.getDeclaredFields())
            {
                b.add(i,f.getType().toString());
                i++;
            }
            String[] intermediate=List_to_Array(b);
            String[] result=new String[intermediate.length];
                for(int k=0;k<intermediate.length;k++)
                {
                if(intermediate[k].startsWith("interface")||intermediate[k].startsWith("boolean"))
                result[k]=intermediate[k];
                else 
                {
                    intermediate[k]=intermediate[k].substring(6);
                    if(intermediate[k].startsWith("java"))
                    result[k]=intermediate[k].substring(10);
                    else
                    result[k]=intermediate[k];  
                   
                }
            }
            return result;
            }
    
    //   Used to rertrieve the datatypes wothin a List
    //ex List<Object> would give the result as OBJECT
    String  get_parameterizedType(Class classname)
         {
           String result = null;
            for (Method method :classname.getMethods()) {
            Class returnClass = method.getReturnType();
            if (Collection.class.isAssignableFrom(returnClass)) {
                Type returnType = method.getGenericReturnType();
                if (returnType instanceof ParameterizedType) {
                    ParameterizedType paramType = (ParameterizedType) returnType;
                    Type[] argTypes = paramType.getActualTypeArguments();
                    if (argTypes.length > 0) {
                     result= argTypes[0].toString();
                    }
                }
            }
            }
            if(result.startsWith("class"))
            {
                result=result.substring(6);
            }
            else
            {
                result=result.substring(22);
            }
            return result;
         }
     // Retrun the method name as a String 
      String[] get_Methods(Class classname)
         {
            List<String> a= new ArrayList<>();
            int i=0;
            for(Method m:classname.getDeclaredMethods())
            {
                
                a.add(i,m.getName());
                i++;
            }
            String[] result=List_to_Array(a);
            return result;
       
        }
    
    //return a string array of getter methods    
    String[] getter_methods(String[] methods)
         {
             List<String> getter_methods=new ArrayList<>();
             for(String m: methods)
             {
                 if(m.startsWith("get"))
                 {
                   getter_methods.add(m);
                 }
             }
             String[] result=List_to_Array(getter_methods);
             return result;
         }
     //returns an array of setter methods
    String[] setter_methods(String[] methods)
         {
             List<String> setter_methods=new ArrayList<>();
             for(String m: methods)
             {
                 if(m.startsWith("set"))
                 {
                   setter_methods.add(m);
                 }
             }
             String[] result=List_to_Array(setter_methods);
             return result;
         }
        
         //return a string array of is_methods
    String[] is_methods(String[] methods)
         {
             List<String> is_methods=new ArrayList<>();
             for(String m: methods)
             {
                 if(m.startsWith("is"))
                 {
                  is_methods.add(m);
                 }
             }
             String[] result=List_to_Array(is_methods);
             return result;
         }
    
    //takes only the enum values from the fields
    
    String[] get_Enum_values(String[] fields)
            {
            List<String> enum_values=new ArrayList<>();
            for(String f:fields)
            {
                if(f.equalsIgnoreCase("value") || f.equalsIgnoreCase("$VALUES"))
                {
                 
                }
                else
                {
                    enum_values.add(f);
                }
            }
            String[] result=List_to_Array(enum_values);
            return result;
            }
    
   
    // used for the ENV Variable to store any values and does this in the form of an XML Element
     Element create_Element(String tag_name,String value)
      { 
          Element e = null;
        try {
            DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder icBuilder;
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();
            String s4="hghg";
            e = doc.createElement(tag_name);          
            e.appendChild(doc.createTextNode(value));
            } catch (ParserConfigurationException ex) {
            Logger.getLogger(reflection.class.getName()).log(Level.SEVERE, null, ex);
        }
         return e;
      }

    // Convert from a list to an array
    // Creating a dynamic object to hold an undefined number of elements can be done using List
     String[] List_to_Array(List<String> l)
         {
            String ar[]=new String [l.size()];
            ar=l.toArray(ar);  
            return ar;
         }
    
    // To retieve a String from an object class by passing in the method name as a String
    String getValue(String method_name,Class classname,Object r)
        {
               
        
             String privateReturnVal=null;
             try {
                  Method privateMethod;
             
                 if(classname.getSuperclass().getName().equals("java.lang.Object"))
                 {
                 privateMethod = classname.getDeclaredMethod(method_name, (Class<?>[]) null);
                 }
                 else
                 {
                 privateMethod = classname.getMethod(method_name, (Class<?>[]) null);  
                 }
                // Shuts down security allowing you to access private methods
                privateMethod.setAccessible(true);
                try {
                    // get the return value from the method
                                           
                    privateReturnVal = (String) privateMethod.invoke(r, (Object[]) null);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(reflection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(reflection.class.getName()).log(Level.SEVERE, null, ex);
            }
            return privateReturnVal;   
        }
     
    //Used to set values in a class by passing methods as objects
    void setValue(String method_name,String text,Class classname,Object obj)
    {
        try {
            Class[] methodParameters = new Class[]{String.class};
            Object[] params = new Object[]{text};
            Method protectedMethod;
             if(classname.getSuperclass().getName().equals("java.lang.Object"))
                 {
                 protectedMethod = classname.getDeclaredMethod(method_name, methodParameters);
                 }
                 else
                 {
                 protectedMethod = classname.getMethod(method_name, methodParameters);  
                 }
            // Shuts down security allowing you to access private methods
            protectedMethod.setAccessible(true);
            // get the return value from the method
            try {
                 protectedMethod.invoke(obj, params);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                 Logger.getLogger(reflection.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (NoSuchMethodException | SecurityException ex)
            {
            Logger.getLogger(reflection.class.getName()).log(Level.SEVERE, null, ex);
             }    
    }
     
    //This method is used to add an object to the Recipe class.
    void setObject(String method_name, Object calling, Object called)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        try {
            Class[] methodParameters = null;
            methodParameters = new Class[]{called.getClass()};
            Object[] params = new Object[]{(called)};
            Method privateMethod = calling.getClass().getDeclaredMethod(method_name,methodParameters);
            // Shuts down security allowing you to access private methods
            privateMethod.setAccessible(true);
            // get the return value from the method
            privateMethod.invoke(calling,params);
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(reflection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    // used to get already existing node values and transfer them to the recipe object
    Object getObject(String method_name, Object calling,Object called) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
         {
                       
            Object privateReturn =called;
            try {
                
                Method privateMethod = calling.getClass().getDeclaredMethod(method_name, (Class<?>[]) null);
                // Shuts down security allowing you to access private methods
                privateMethod.setAccessible(true);
                // get the return value from the method
                privateReturn =privateMethod.invoke(calling, (Object[]) null);
                
                
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(reflection.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(privateReturn==null)
         {
             privateReturn=called;
         }
          
       
         return privateReturn;
        }
        
        // This method is used to set the test type from the options hand,handler,
        
        void setTestType(String a, Object calling_class) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
         {
            try {
            String method_name="setTesttype";
            Class[] methodParameters = null;
            methodParameters = new Class[]{TestType.class};
            Object[] params = new Object[]{(TestType.fromValue(a))};
            Method privateMethod = calling_class.getClass().getDeclaredMethod(method_name,methodParameters);
            privateMethod.invoke(calling_class,params);
            privateMethod.setAccessible(true);
            } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(reflection.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
        void setWhichTest(String a, Object calling_class) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
         {
            try {
            String method_name="setWhichtest";
            Class[] methodParameters = null;
            methodParameters = new Class[]{WhichTest.class};
            Object[] params = new Object[]{(WhichTest.valueOf(a))};
            Method privateMethod = calling_class.getClass().getDeclaredMethod(method_name,methodParameters);
           privateMethod.invoke(calling_class,params);
           privateMethod.setAccessible(true);
           } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(reflection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
     

   
    //sets Integer values
    void setIntValue(String method_name,int value,Class classname,Object r)
    {
        try {
            Class[] methodParameters = new Class[]{Integer.class};
            Object[] params = new Object[]{value};
             Method protectedMethod;
             if(classname.getSuperclass().getName().equals("java.lang.Object"))
                 {
                 protectedMethod = classname.getDeclaredMethod(method_name, methodParameters);
                 }
                 else
                 {
                 protectedMethod = classname.getMethod(method_name, methodParameters);  
                 }
           
            // Shuts down security allowing you to access private methods
            protectedMethod.setAccessible(true);
            // get the return value from the method
            try {
                 protectedMethod.invoke(r, params);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                 Logger.getLogger(reflection.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (NoSuchMethodException | SecurityException ex)
            {
            Logger.getLogger(reflection.class.getName()).log(Level.SEVERE, null, ex);
             }    
    }
   
    //retrieves Integer values
    int getIntValue(String method_name,Class classname,Object r)
         {
            int result=0;
            Method privateMethod=null;
            try {
                if(classname.getSuperclass().getName().equals("java.lang.Object"))
                 {
                 privateMethod = classname.getDeclaredMethod(method_name, (Class<?>[]) null);
                 }
                 else
                 {
                 privateMethod = classname.getMethod(method_name, (Class<?>[]) null);  
                 }
                // Shuts down security allowing you to access private methods
                privateMethod.setAccessible(true);
                try {
                    // get the return value from the method
                    Integer re;
                    re = (Integer) privateMethod.invoke(r, (Object[]) null);
                     if(re==null)
                         result=0;
                    else
                     result=re;
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(reflection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(reflection.class.getName()).log(Level.SEVERE, null, ex);
            }
            return result;   
        }
    
    // Used to retrieve Boolean values from an object class bybpassing method _name as a String
    Boolean isValue(String method_name,Class classname,Object r) throws NoSuchMethodException
         {
              Boolean value=false;
             
        try {
            Method privateMethod;
            if(classname.getSuperclass().getName().equals("java.lang.Object"))
                 {
                 privateMethod = classname.getDeclaredMethod(method_name, (Class<?>[]) null);
                 }
                 else
                 {
                 privateMethod = classname.getMethod(method_name, (Class<?>[]) null);  
                 }
            // Shuts down security allowing you to access private methods
            privateMethod.setAccessible(true);
            // get the return value from the method
            value = (Boolean) privateMethod.invoke(r, (Object[]) null);
           
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(reflection.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(value==null)
            {
                value=false;
            }
            return value;
        } 
    // Used to set boolean values from an object class bybpassing method _name as a String
     void setBoolValue(String method_name,Boolean value,Class classname,Object r)
         //void setBoolValue()
        {
        try //void setBoolValue()
            {
            //String text=tf.getText();
            Class[] methodParameters = new Class[]{Boolean.class};
            Object[] params = new Object[]{(value)};
            Method privateMethod;
             if(classname.getSuperclass().getName().equals("java.lang.Object"))
                 {
                 privateMethod = classname.getDeclaredMethod(method_name, methodParameters);
                 }
                 else
                 {
                 privateMethod = classname.getMethod(method_name, methodParameters);  
                 }
            // Shuts down security allowing you to access private methods
            privateMethod.setAccessible(true);
            // get the return value from the method
            try {
                privateMethod.invoke(r,params);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(reflection.class.getName()).log(Level.SEVERE, null, ex);
                }
                } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(reflection.class.getName()).log(Level.SEVERE, null, ex);
                }     
        }
     
    // Used to retrieve boolean values from an object class bybpassing method _name as a String
    void setbooleanValue(String method_name,boolean value,Class classname,Object r)
         //void setBoolValue()
        {
        try //void setBoolValue()
            {
            //String text=tf.getText();
            Class[] methodParameters = new Class[]{boolean.class};
            Object[] params = new Object[]{(value)};
           
            Method privateMethod;
             if(classname.getSuperclass().getName().equals("java.lang.Object"))
                 {
                 privateMethod = classname.getDeclaredMethod(method_name, methodParameters);
                 }
                 else
                 {
                 privateMethod = classname.getMethod(method_name, methodParameters);  
                 }
            // Shuts down security allowing you to access private methods
            privateMethod.setAccessible(true);
            // get the return value from the method
            try {
                privateMethod.invoke(r,params);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(reflection.class.getName()).log(Level.SEVERE, null, ex);
                }
                } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(reflection.class.getName()).log(Level.SEVERE, null, ex);
                }     
        }
    
    //used for the classes After and Before. They inherit their instance members from another class so therfore cannot be directly accesssed.
 
      public Object Set_After_before1(Class classname,Object obj,Object exe,Object variable) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException
      {
           Field f = classname.getSuperclass().getDeclaredField("actionElements");
           f.setAccessible(true); // Especially necessary if the field is not public
           List<Object> ac=new ArrayList<>();
           ac.add(exe);
           ac.add(variable);
           f.set(obj,ac);
          return obj;
      }
     
    
    // used to assign a List to an instance variable from another class
    // ex: List a=  List<OOCRule> bins; 
    List getListValue(String method_name,Class classname,Object obj)
         {
               
        
             List<Object> privateReturnVal=null;
             try {
                Method privateMethod = classname.getDeclaredMethod(method_name, (Class<?>[]) null);
                // Shuts down security allowing you to access private methods
                privateMethod.setAccessible(true);
                try {
                    // get the return value from the method
                    privateReturnVal= (List) privateMethod.invoke(obj, (Object[]) null);
                    
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(reflection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(reflection.class.getName()).log(Level.SEVERE, null, ex);
            }
            return privateReturnVal;   
        }
     
    //to print an array
    void print_array(String[] a)
         {
             System.out.println("Output");
            for (String a1 : a)
            {
             System.out.println(a1);
            }
        }

    void WhichTest(String value, Class classname) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
