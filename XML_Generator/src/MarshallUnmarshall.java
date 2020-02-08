import com.advantest.tcct.recipe.ObjectFactory;
import com.advantest.tcct.recipe.Recipe;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SheenaJacob
 */
public class MarshallUnmarshall {
    Recipe edit(Recipe r) 
    {
        String fileName = "C:\\Users\\SheenaJacob\\Desktop\\embedded systems\\3rd semester\\research project\\java code\\netbeans\\JavaApplication14\\recipe.xml";
        File xmlFile = new File(fileName);
        JAXBContext jaxbContext;
        try
        {
            jaxbContext = JAXBContext.newInstance(r.getClass().getPackage().getName());
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement<Recipe> jaxbElement = (JAXBElement<Recipe>) jaxbUnmarshaller.unmarshal(new StreamSource(xmlFile), Recipe.class);
            r = jaxbElement.getValue();
             
        }
        catch (JAXBException e) 
        {
        }
        return r;
    }
    void printXML(Recipe r)
    {
       
        try {            
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(r.getClass().getPackage().getName());
            javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            ObjectFactory obj=new ObjectFactory();
            File file=new File("recipe.xml");
            JAXBElement<Recipe> je=obj.createRecipe(r);
            marshaller.marshal(je, file);
            marshaller.marshal(je, System.out);
        } catch (javax.xml.bind.JAXBException ex)
            {
             java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
            }
    }
    
}
