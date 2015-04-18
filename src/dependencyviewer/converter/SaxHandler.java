package dependencyviewer.converter;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dependencyviewer.model.ClassElement;
import dependencyviewer.model.Element;
import dependencyviewer.model.FeatureElement;
import dependencyviewer.model.PackageElement;
import dependencyviewer.model.util.ClassFactory;
import dependencyviewer.model.util.FeatureFactory;
import dependencyviewer.model.util.PackageFactory;
import dependencyviewer.util.ElementType;

/**
 *
 * @author gbeine
 */
public class SaxHandler extends DefaultHandler {

    /**
     *
     */
    private static final Logger logger = LoggerFactory.getLogger(SaxHandler.class);

    private ElementType currentElementType;
    private Element currentElement;
    private String currentName;
    private PackageElement currentPackage;
    private ClassElement currentClass;
    private FeatureElement currentFeature;
    private Attributes currentDependency;

    private Stack<ElementType> elementStack = new Stack<ElementType>();

    private Set<PackageElement> packages = new HashSet<PackageElement>();

    public Set<PackageElement> getPackages() {
        return this.packages;
    }

    @Override
    public void startElement(final String namespaceURI, final String sName, final String qName, final Attributes attrs) throws SAXException {
        if (SaxHandler.logger.isDebugEnabled()) {
            SaxHandler.logger.debug("Starting with: " + qName);
            for (int i = 0; i < attrs.getLength(); i++) {
//                SaxHandler.logger.debug("    " + attrs.getQName(i) + ": " + attrs.getValue(i));
            }
        }

        if (null != this.currentElementType) {
            this.elementStack.push(this.currentElementType);
        }

        if ("name".equals(qName)) {
            this.currentElementType = ElementType.NAME;
        } else if ("package".equals(qName)) {
            this.startPackageElement(attrs);
        } else if ("class".equals(qName)) {
            this.startClassElement(attrs);
        } else if ("feature".equals(qName)) {
            this.startFeatureElement(attrs);
        } else if ("inbound".equals(qName)) {
            this.startInboundDependency(attrs);
        } else if ("outbound".equals(qName)) {
            this.startOutboundDependency(attrs);
        } else {
            this.currentElementType = ElementType.UNKOWN;
        }
    }

    @Override
    public void endElement(final String namespaceURI, final String sName, final String qName) throws SAXException {
        if (SaxHandler.logger.isDebugEnabled()) {
            SaxHandler.logger.debug("Ending with: " + qName);
        }

        // save current element
        final ElementType realCurrentElement = this.currentElementType;
        // we need to pop next element now from stack for making decisions in all operations
        if (!this.elementStack.empty()) {
            this.currentElementType = this.elementStack.pop();
        }

        switch (realCurrentElement) {
            case NAME:
                this.endNameElement();
                break;
            case INBOUND:
                this.endInboundDependency();
                break;
            case OUTBOUND:
                this.endOutboundDependency();
                break;
            case PACKAGE:
                this.endPackageElement();
                break;
            case CLASS:
                this.endClassElement();
                break;
            case FEATURE:
                this.endFeatureElement();
                break;
            default:
                // Nothing to do here for other elements
                break;
        }

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        final String name = String.valueOf(ch, start, length);
        if (SaxHandler.logger.isDebugEnabled()) {
            SaxHandler.logger.debug("Current element name: " + name);
        }
        if (ElementType.NAME.equals(this.currentElementType)) {
            this.currentName = name;
        } else if (ElementType.INBOUND.equals(this.currentElementType)) {
            this.currentName = name;
        } else if (ElementType.OUTBOUND.equals(this.currentElementType)) {
            this.currentName = name;
        }
    }

    /**
     * Starts handling a package element
     * @param attrs
     */
    private void startPackageElement(final Attributes attrs) {
        this.currentElementType = ElementType.PACKAGE;
    }

    /**
     * Starts handling a class element
     * @param attrs
     */
    private void startClassElement(final Attributes attrs) {
        this.currentElementType = ElementType.CLASS;
    }

    /**
     * Starts handling a feature element (seen as a method)
     * @param attrs
     */
    private void startFeatureElement(final Attributes attrs) {
        this.currentElementType = ElementType.FEATURE;
    }

    /**
     * Starts handling an inbound dependency
     * @param attrs
     */
    private void startInboundDependency(final Attributes attrs) {
        this.currentElementType = ElementType.INBOUND;
        this.currentDependency = attrs;
    }

    /**
     * Starts handling an outbound dependency
     * @param attrs
     */
    private void startOutboundDependency(final Attributes attrs) {
        this.currentElementType = ElementType.OUTBOUND;
        this.currentDependency = attrs;
    }

    /**
     * Ends handling an outbound dependency
     *
     * Creates
     */
    private void endInboundDependency() {
        final String type = this.currentDependency.getValue("type");
        if (SaxHandler.logger.isDebugEnabled()) {
            SaxHandler.logger.debug("Inbound dependency: " + this.currentName + ", type: " + type);
        }

        if ("package".equals(type)) {
            final PackageElement pe = PackageFactory.createPackage(this.currentName);
            this.currentElement.getDependencies().addInboundDependency(pe);
        } else if ("class".equals(type)) {
            final ClassElement ce = ClassFactory.createClass(this.currentName);
            this.currentElement.getDependencies().addInboundDependency(ce);
        } else if ("feature".equals(type)) {
            final FeatureElement fe = FeatureFactory.createFeature(this.currentName);
            this.currentElement.getDependencies().addInboundDependency(fe);
        }

        this.currentName = null;
        this.currentDependency = null;
    }

    private void endOutboundDependency() {
        final String type = this.currentDependency.getValue("type");
        if (SaxHandler.logger.isDebugEnabled()) {
            SaxHandler.logger.debug("Outbound dependency: " + this.currentName + ", type: " + type);
        }
        if ("package".equals(type)) {
            final PackageElement pe = PackageFactory.createPackage(this.currentName);
            this.currentElement.getDependencies().addOutboundDependency(pe);
        } else if ("class".equals(type)) {
            final ClassElement ce = ClassFactory.createClass(this.currentName);
            this.currentElement.getDependencies().addOutboundDependency(ce);
        } else if ("feature".equals(type)) {
            final FeatureElement fe = FeatureFactory.createFeature(this.currentName);
            this.currentElement.getDependencies().addOutboundDependency(fe);
        }
        this.currentName = null;
        this.currentDependency = null;
    }

    private void endNameElement() {
        switch (this.currentElementType) {
            case PACKAGE:
                this.createPackageElement();
                break;
            case CLASS:
                this.createClassElement();
                break;
            case FEATURE:
                this.createFeatureElement();
                break;
            default:
                // Nothing to do for other elements
                break;
        }
        this.currentName = null;
    }

    private void endPackageElement() {
        this.packages.add(this.currentPackage);
        this.currentPackage = null;
    }

    private void endClassElement() {
        this.currentPackage.addClass(this.currentClass);
        this.currentClass = null;
        this.currentElement = this.currentPackage;
    }

    private void endFeatureElement() {
        this.currentClass.addFeature(this.currentFeature);
        this.currentFeature = null;
        this.currentElement = this.currentClass;
    }

    private void createPackageElement() {
        this.currentPackage = PackageFactory.createPackage(this.currentName);
        this.currentElement = this.currentPackage;
    }

    private void createClassElement() {
        this.currentClass = ClassFactory.createClass(this.currentName);
        this.currentElement = this.currentClass;
    }

    private void createFeatureElement() {
        this.currentFeature = FeatureFactory.createFeature(this.currentName);
        this.currentElement = this.currentFeature;
    }
}
