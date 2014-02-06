package ve.web.chebetos.xsd_analizer.lib;

import java.io.File;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSSchemaSet;

public class SchemaLoaderTest {
	private static Logger logger = 
			Logger.getLogger(SchemaLoaderTest.class.getName());

	@Test
	public void canParseAFile() {
		final String schemaTestFile = "src/test/resources/ve/web/chebetos/xsd_analizer/lib/schema-test.xsd";
		XSSchemaSet schemaSet = SchemaLoader.getInstance().parse(
				new File(schemaTestFile));
		Assert.assertNotNull(schemaSet);
	}

	@Test(expected = RuntimeException.class)
	public void exceptionIfNotFileFound() {
		final String schemaTestFile = "schema-test.xsd";
		XSSchemaSet schemaSet = SchemaLoader.getInstance().parse(new File(schemaTestFile));
		Assert.assertNotNull(schemaSet);
	}

	@Test
	public void canParseAFolder() {
		final String schemaTestFile = "./src";
		XSSchemaSet schemaSet = SchemaLoader.getInstance().parse(new File(schemaTestFile));
		Assert.assertNotNull(schemaSet);
	}

	@Test
	public void listElements() {
		final String schemaTestFile = "./src";
		XSSchemaSet schemaSet = SchemaLoader.getInstance().parse(new File(schemaTestFile));
		Iterator<XSElementDecl> itElements = schemaSet.iterateElementDecls();
		while (itElements.hasNext()) {
			XSElementDecl element = itElements.next();
			logger.log(Level.INFO, "elements: {0}", element);
		}
	}

    //leer un esquema (comprobar que trae un esquema y probar que pasa si tiene import)
	// leer un directorio (comprobar que carga varios esquemas)

	// listar elementos en un esquema
	// listar tipos
    //OBJETIVO: poner por cada tipo (global) el elemento (global) del esquema donde se usa.
}
