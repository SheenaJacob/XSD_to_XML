# XSD_to_XML
Contains Files that allow a user to convert an XSD File to an XML.
This is done by the creating a generic GUI that allows users to enter the information that is to be included within the XML File.
The nodes within the GUI are created based on the XSD FIle specified 

Information about classes present with src in XML Generator:

1. reflection: Contains source code required to retrive important information from classes such as the dattypes and names of variables available. Also allows for these values of variables to be edited and retrived.

2. Unmarshall_marshall : The XSD File is converted into Java classes by using a binding compiler provided by JaxB. This allows for every complec datatype to be created as a seperate Java class.
Useing JaxB  we can marshall and unmarshall the classes created into an XML File. 

3. Layout: This File describes how the layout of the GUI is created dynamically using Java Reflections.

4. recipe: Main class that where the GUI can be run from. 
