package ve.web.chebetos.xsd_analizer.lib;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSType;

public class SchemaAnalizerTest {
	private final static Logger logger = Logger.getLogger(SchemaAnalizerTest.class
			.getName());
	
	private static SchemaAnalizer sut;
	
	@BeforeClass
	public static void initClass() {
		sut = new SchemaAnalizer(SchemaLoader.getInstance().parse(new File("./src")));
	}
	
	
	@Test
	public void testGetTypes() {
		Collection<XSType> types = sut.getTypes();
		Assert.assertTrue(!types.isEmpty());
	}

	@Test
	public void testGetElements() {
		Collection<XSElementDecl> elements = sut.getElements();
		Assert.assertTrue(!elements.isEmpty());
	}
	
	
	@Test
	public void testGetMapTypeElements() {
		Map<XSType, Set<XSElementDecl>> mapTypeElements = sut.getMapTypesElements();
		Assert.assertTrue(!mapTypeElements.isEmpty());
		logger.log(Level.INFO, "mapTypeElements: {0}", mapTypeElements);
	}
	
}
