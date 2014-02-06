package ve.web.chebetos.xsd_analizer.lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.SAXException;

import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.parser.XSOMParser;

public class SchemaLoader {
	
	private static Logger logger =
			 Logger.getLogger(SchemaLoader.class.getName());

	private static final SchemaLoader instance = new SchemaLoader();

	public static SchemaLoader getInstance() {
		return instance;
	}

	public XSSchemaSet parse(File file) {
		XSOMParser parser = new XSOMParser();
		try {
			if (!file.exists()) {
				throw new FileNotFoundException(file.getName() + " does not exists");
			}
			parseFile(file, parser);
			return parser.getResult();
		} catch (SAXException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void parseFile(File file, XSOMParser parser) throws SAXException,
			IOException {
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
				parseFile(subFile, parser);
			}
		} else {
			logger.log(Level.INFO, "parsing {0}", file);
			parser.parse(file);
		}
	}
}
