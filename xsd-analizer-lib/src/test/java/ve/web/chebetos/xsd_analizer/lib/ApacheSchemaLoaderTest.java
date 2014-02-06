package ve.web.chebetos.xsd_analizer.lib;

import java.io.File;
import java.util.logging.Logger;

import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaCollection;
import org.junit.Assert;
import org.junit.Test;

public class ApacheSchemaLoaderTest {
	private static Logger logger =
			 Logger.getLogger(ApacheSchemaLoaderTest.class.getName());
	
    @Test
    public void canParseAFile() {
    	final String schemaTestFile = "./src";
    	XmlSchemaCollection schemaCollection = ApacheSchemaLoader.getInstance().parse(new File(schemaTestFile));
    	Assert.assertNotNull(schemaCollection);
    	
    	XmlSchema[] schemas =  schemaCollection.getXmlSchemas();
    	Assert.assertTrue(schemas.length > 0);
    }
    
    //leer un esquema (comprobar que trae un esquema y probar que pasa si tiene import)
    //leer un directorio (comprobar que carga varios esquemas)
    
    //listar elementos en un esquema
    //listar tipos
    //OBJETIVO: poner por cada tipo (global) el elemento (global) del esquema donde se usa.
}
