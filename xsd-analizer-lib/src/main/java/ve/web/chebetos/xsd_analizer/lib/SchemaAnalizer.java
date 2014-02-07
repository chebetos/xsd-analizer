/**
 * 
 */
package ve.web.chebetos.xsd_analizer.lib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSType;

/**
 * @author Cesar Augusto Delgado Rodriguez
 * 
 */
public class SchemaAnalizer {

	private final static String standardSchema = "http://www.w3.org/2001/XMLSchema";
	private final static Logger logger = Logger.getLogger(SchemaAnalizer.class
			.getName());

	private final XSSchemaSet schemaSet;

	private final Map<XSElementDecl, Set<XSType>> mapElementTypes = new LinkedHashMap<XSElementDecl, Set<XSType>>();

	private final Map<XSType, Set<XSElementDecl>> mapTypesElements = new LinkedHashMap<XSType, Set<XSElementDecl>>();

	public SchemaAnalizer(XSSchemaSet schemaset) {
		this.schemaSet = schemaset;
		
		Collection<XSElementDecl> elements = getElements();
		for (XSElementDecl xsElementDecl : elements) {
			if (mapElementTypes.containsKey(xsElementDecl)) {
				continue;
			}
			Set<XSType> types = getTypesInElement(xsElementDecl);
			mapElementTypes.put(xsElementDecl, types);
		}
		
		Collection<XSType> types = getTypes();
		for (XSType xsType : types) {
			for (XSElementDecl xsElementDecl : elements) {
				Set<XSType> typesInElement = mapElementTypes.get(xsElementDecl);
				if (typesInElement.contains(xsType)) {
					Set<XSElementDecl> elementsHadType = mapTypesElements.get(xsType);
					if (null == elementsHadType) {
						elementsHadType = new LinkedHashSet<XSElementDecl>();
						mapTypesElements.put(xsType, elementsHadType);
					}
					elementsHadType.add(xsElementDecl);
				}
			}
		}
		
		for (XSType xsType : types) {
			Set<XSElementDecl> elementUseTypes = mapTypesElements.get(xsType);
			logger.log(Level.ALL, "{0} \t {1} ", new Object[] { xsType,
					elementUseTypes });
		}
	}

	public Collection<XSType> getTypes() {
		Collection<XSSchema> schema = this.schemaSet.getSchemas();

		List<XSType> types = new ArrayList<XSType>();
		for (XSSchema xsSchema : schema) {
			if (xsSchema.getTargetNamespace().equals(standardSchema)) {
				continue;
			}
			Collection<XSType> schemaTypes = xsSchema.getTypes().values();
			for (XSType xsType : schemaTypes) {
				if (xsType.getTargetNamespace().equals(
						xsSchema.getTargetNamespace())) {
					types.add(xsType);
				}
			}
			//logger.log(Level.INFO, "ns: {0}", xsSchema.getTargetNamespace());
		}
		//logger.log(Level.INFO, "types: {0}", types);
		return types;
	}

	public Collection<XSElementDecl> getElements() {
		Collection<XSSchema> schema = this.schemaSet.getSchemas();

		Collection<XSElementDecl> elements = new ArrayList<XSElementDecl>();
		for (XSSchema xsSchema : schema) {
			Collection<XSElementDecl> schemaElements = xsSchema
					.getElementDecls().values();
			elements.addAll(schemaElements);
		}

		//logger.log(Level.INFO, "elements: {0}", elements);
		return elements;
	}

	private Set<XSType> getTypesInElement(XSElementDecl element) {
		logger.log(Level.INFO, "Searching types for {0}", element);
		
		Set<XSType> elementTypes = new LinkedHashSet<XSType>();
		XSType elementMainType = element.getType();
		if (elementMainType.isGlobal()
				&& !elementMainType.getTargetNamespace().equals(standardSchema)) {
			elementTypes.add(elementMainType);
		}
		if (elementMainType.isComplexType()) {
			XSComplexType complexType = elementMainType.asComplexType();
			List<XSElementDecl> subElements = complexType.getElementDecls();
			logger.log(Level.INFO, "subElements {0}", subElements);

			for (XSElementDecl xsElementDecl : subElements) {
				if (this.mapElementTypes.containsKey(xsElementDecl)
						|| xsElementDecl.equals(element)) {
					continue;
				}
				elementTypes.addAll(getTypesInElement(xsElementDecl));
				this.mapElementTypes.put(xsElementDecl, elementTypes);
			}
		}
		return elementTypes;
	}

	public Map<XSElementDecl, Set<XSType>> getMapElementTypes() {
		return mapElementTypes;
	}

	public Map<XSType, Set<XSElementDecl>> getMapTypesElements() {
		return mapTypesElements;
	}
	
	
}
