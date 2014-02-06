package ve.web.chebetos.xsd_analizer.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.stream.StreamSource;

import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaCollection;
import org.xml.sax.SAXException;

public class ApacheSchemaLoader {

	private static Logger logger = Logger.getLogger(ApacheSchemaLoader.class
			.getName());

	private static final ApacheSchemaLoader instance = new ApacheSchemaLoader();

	public static ApacheSchemaLoader getInstance() {
		return instance;
	}

	public XmlSchemaCollection parse(File file) {
		XmlSchemaCollection schemaCol = new XmlSchemaCollection();
		try {
			parseFile(file, schemaCol);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return schemaCol;
	}

	private void parseFile(File file, XmlSchemaCollection schemaCol) throws FileNotFoundException {
		if (file.isDirectory()) {
			File[] subFiles = file.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					File subFile = new File(dir, name);
					boolean isSchemaFile = subFile.isFile()
							&& name.toLowerCase().endsWith(".xsd");
					return isSchemaFile || subFile.isDirectory();
				}
			});
			for (File subFile : subFiles) {
				parseFile(subFile, schemaCol);
			}
		} else {
			logger.log(Level.INFO, "parsing {0}", file);
			FileInputStream is = new FileInputStream(file);
			XmlSchema schema = schemaCol.read(new StreamSource(is), null);
		}
	}
}
