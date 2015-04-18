package dependencyviewer.model.converter;

import dependencyviewer.model.ClassElement;
import dependencyviewer.model.util.ClassFactory;
import dependencyviewer.model.PackageElement;
import dependencyviewer.util.NodeListIterator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author gbeine
 */
public class ClassConverter {

    private static final Logger logger = LoggerFactory.getLogger(ClassConverter.class);
    private static final XPath xpath = XPathFactory.newInstance().newXPath();
    private static final String xpathPatterns[] = {
        "inbound[@type='class']",
        "feature/inbound[@type='class']",
        "outbound[@type='class']",
        "feature/outbound[@type='class']"
    };
    private static final Map<String, XPathExpression> expressions = new HashMap<String, XPathExpression>();

    private ClassElement classElement;
    private Node classNode;

    public static ClassElement convert(final Node classNode) {
        ClassConverter cc = new ClassConverter();

        cc.setClassNode(classNode);
        cc.startConversion();

        return cc.getClassElement();
    }

    public static ClassElement convert(final Node classNode, final PackageElement pe) {
        final ClassElement ce = ClassConverter.convert(classNode);
        ce.setParentPackage(pe);
        return ce;
    }

    private ClassConverter() {
        super();
    }

    private ClassElement getClassElement() {
        return this.classElement;
    }

    private void setClassNode(final Node classNode) {
        this.classNode = classNode;
    }

    private void startConversion() {
        if (ClassConverter.logger.isDebugEnabled()) {
            ClassConverter.logger.debug("Converting node to ClassElement");
        }

        this.createClassElement();
        this.fetchMethods();

        for (String xpathPattern: ClassConverter.xpathPatterns) {
            this.fetchDependencies(xpathPattern);
        }
        
        if (ClassConverter.logger.isDebugEnabled()) {
            ClassConverter.logger.debug("Conversion finished");
        }
    }

    private void createClassElement() {
        try {
            if (ClassConverter.logger.isDebugEnabled()) {
                ClassConverter.logger.debug("Fetching class name");
            }
           final String xpathPattern = "name";
            // just a little speedup, needs some refactoring
            XPathExpression expr;
            if (ClassConverter.expressions.containsKey(xpathPattern)) {
                expr = ClassConverter.expressions.get(xpathPattern);
            } else {
                expr = ClassConverter.xpath.compile(xpathPattern);
                ClassConverter.expressions.put(xpathPattern, expr);
            }
            final Node nameNode = (Node) expr.evaluate(this.classNode, XPathConstants.NODE);
            final String className = nameNode.getTextContent();
            this.classElement = ClassFactory.createClass(className);
        } catch (XPathExpressionException ex) {
            ClassConverter.logger.error(ex.getMessage());
        }
    }

    private void fetchDependencies(final String xpathPattern) {
        try {
            if (ClassConverter.logger.isDebugEnabled()) {
                ClassConverter.logger.debug("Fetching dependencies: " + xpathPattern);
            }
            // just a little speedup, needs some refactoring
            XPathExpression expr;
            if (ClassConverter.expressions.containsKey(xpathPattern)) {
                expr = ClassConverter.expressions.get(xpathPattern);
            } else {
                expr = ClassConverter.xpath.compile(xpathPattern);
                ClassConverter.expressions.put(xpathPattern, expr);
            }
            final NodeList nodeList = (NodeList) expr.evaluate(this.classNode, XPathConstants.NODESET);
            if (ClassConverter.logger.isDebugEnabled()) {
                ClassConverter.logger.debug("Found " + nodeList.getLength() + " dependencies for " + xpathPattern);
            }
            this.addDependencies(nodeList);
        } catch (XPathExpressionException ex) {
            ClassConverter.logger.error(ex.getMessage());
        }
    }

    private void fetchMethods() {
        try {
            if (ClassConverter.logger.isDebugEnabled()) {
                ClassConverter.logger.debug("Fetching methods");
            }
            final String xpathPattern = "feature";
            // just a little speedup, needs some refactoring
            XPathExpression expr;
            if (ClassConverter.expressions.containsKey(xpathPattern)) {
                expr = ClassConverter.expressions.get(xpathPattern);
            } else {
                expr = ClassConverter.xpath.compile(xpathPattern);
                ClassConverter.expressions.put(xpathPattern, expr);
            }
            final NodeList methodNodeList = (NodeList) expr.evaluate(this.classNode, XPathConstants.NODESET);
            if (ClassConverter.logger.isDebugEnabled()) {
                ClassConverter.logger.debug("Found " + methodNodeList.getLength() + " methods");
            }
            final Iterator<Node> methodNodeListIterator  = new NodeListIterator(methodNodeList);
            while (methodNodeListIterator.hasNext()) {
                final Node methodNode = (Node) methodNodeListIterator.next();
                this.classElement.addFeature(MethodConverter.convert(methodNode));
            }
        } catch (XPathExpressionException ex) {
            ClassConverter.logger.error(ex.getMessage());
        }
    }

    private void addDependencies(final NodeList nodeList) {
        final Iterator<Node> nodeListIterator  = new NodeListIterator(nodeList);
        while (nodeListIterator.hasNext()) {
            final Node node = (Node) nodeListIterator.next();
            final String name = node.getTextContent();
            final ClassElement dep = ClassFactory.createClass(name);
//          TODO: fix Dom Parser
//            this.classElement.addDependency(dep);
        }
    }
}
