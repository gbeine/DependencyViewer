package dependencyviewer.model.converter;

import dependencyviewer.model.ClassElement;
import dependencyviewer.model.FeatureElement;
import dependencyviewer.model.util.FeatureFactory;
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
public class MethodConverter {

    private static final Logger logger = LoggerFactory.getLogger(MethodConverter.class);
    private static final XPath xpath = XPathFactory.newInstance().newXPath();
    private static final String xpathPatterns[] = {
        "inbound[@type='feature']",
        "outbound[@type='feature']"
    };
    private static final Map<String, XPathExpression> expressions = new HashMap<String, XPathExpression>();

    private FeatureElement methodElement;
    private Node methodNode;

    public static FeatureElement convert(final Node methodNode) {
        MethodConverter cc = new MethodConverter();

        cc.setMethodNode(methodNode);
        cc.startConversion();

        return cc.getMethodElement();
    }

    public static FeatureElement convert(final Node methodNode, final ClassElement ce) {
        final FeatureElement me = MethodConverter.convert(methodNode);
        me.setParentClass(ce);
        return me;
    }

    private MethodConverter() {
        super();
    }

    private FeatureElement getMethodElement() {
        return this.methodElement;
    }

    private void setMethodNode(final Node methodNode) {
        this.methodNode = methodNode;
    }

    private void startConversion() {
        if (MethodConverter.logger.isDebugEnabled()) {
            MethodConverter.logger.debug("Converting node to MethodElement");
        }

        this.createMethodElement();
        for (String xpathPattern: MethodConverter.xpathPatterns) {
            this.fetchDependencies(xpathPattern);
        }

        if (MethodConverter.logger.isDebugEnabled()) {
            MethodConverter.logger.debug("Conversion finished");
        }
    }

    private void createMethodElement() {
        try {
            if (MethodConverter.logger.isDebugEnabled()) {
                MethodConverter.logger.debug("Fetching method name");
            }
            final String xpathPattern = "name";
            // just a little speedup, needs some refactoring
            XPathExpression expr;
            if (MethodConverter.expressions.containsKey(xpathPattern)) {
                expr = MethodConverter.expressions.get(xpathPattern);
            } else {
                expr = MethodConverter.xpath.compile(xpathPattern);
                MethodConverter.expressions.put(xpathPattern, expr);
            }
            final Node nameNode = (Node) expr.evaluate(this.methodNode, XPathConstants.NODE);
            final String methodName = nameNode.getTextContent();
            this.methodElement = FeatureFactory.createFeature(methodName);
        } catch (XPathExpressionException ex) {
            MethodConverter.logger.error(ex.getMessage());
        }
    }

    private void fetchDependencies(final String xpathPattern) {
        try {
            if (MethodConverter.logger.isDebugEnabled()) {
                MethodConverter.logger.debug("Fetching dependencies: " + xpathPattern);
            }
            // just a little speedup, needs some refactoring
            XPathExpression expr;
            if (MethodConverter.expressions.containsKey(xpathPattern)) {
                expr = MethodConverter.expressions.get(xpathPattern);
            } else {
                expr = MethodConverter.xpath.compile(xpathPattern);
                MethodConverter.expressions.put(xpathPattern, expr);
            }
            final NodeList nodeList = (NodeList) expr.evaluate(this.methodNode, XPathConstants.NODESET);
            if (MethodConverter.logger.isDebugEnabled()) {
                MethodConverter.logger.debug("Found " + nodeList.getLength() + " dependencies for " + xpathPattern);
            }
            this.addDependencies(nodeList);
        } catch (XPathExpressionException ex) {
            MethodConverter.logger.error(ex.getMessage());
        }
    }

    private void addDependencies(final NodeList nodeList) {
        final Iterator<Node> nodeListIterator  = new NodeListIterator(nodeList);
        while (nodeListIterator.hasNext()) {
            final Node node = (Node) nodeListIterator.next();
            final String name = node.getTextContent();
            final FeatureElement dep = FeatureFactory.createFeature(name);
//          TODO: Fix dom parser
//            this.methodElement.addDependency(dep);
        }
    }
}
