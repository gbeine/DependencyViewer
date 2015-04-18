package dependencyviewer.model.converter;

import dependencyviewer.model.util.PackageFactory;
import dependencyviewer.model.PackageElement;
import dependencyviewer.util.NodeListIterator;
import java.util.Iterator;
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
public class PackageConverter {

    private static final Logger logger = LoggerFactory.getLogger(PackageConverter.class);
    private static final XPath xpath = XPathFactory.newInstance().newXPath();
    private static XPathExpression nameExpression;
    private static XPathExpression classExpression;

    public static PackageElement convert(final Node packageNode) {
        PackageElement pe = null;
        if (PackageConverter.logger.isDebugEnabled()) {
            PackageConverter.logger.debug("Converting node to PackageElement");
        }

        try {
            if (PackageConverter.logger.isDebugEnabled()) {
                PackageConverter.logger.debug("Fetching package name");
            }
            final XPathExpression nameExpr = PackageConverter.getNameExpression();
            final Node nameNode = (Node) nameExpr.evaluate(packageNode, XPathConstants.NODE);
            final String name = nameNode.getTextContent();
            pe = PackageFactory.createPackage(name);

            // unfortunately dependency finder includes package-info file in the class list
            // this is handled by excluding these nodes
            if (PackageConverter.logger.isDebugEnabled()) {
                PackageConverter.logger.debug("Fetching node's children");
            }
            final XPathExpression classExpr = PackageConverter.getClassExpression();
            final NodeList classNodeList = (NodeList) classExpr.evaluate(packageNode, XPathConstants.NODESET);
            if (PackageConverter.logger.isDebugEnabled()) {
                PackageConverter.logger.debug("Found " + classNodeList.getLength() + " children");
            }
            final Iterator<Node> classNodeListIterator  = new NodeListIterator(classNodeList);
            while (classNodeListIterator.hasNext()) {
                final Node classNode = (Node) classNodeListIterator.next();
                pe.addClass(ClassConverter.convert(classNode, pe));
            }
            
        } catch (XPathExpressionException ex) {
            PackageConverter.logger.error(ex.getMessage());
        }

        return pe;
    }

    private static XPathExpression getNameExpression() {
        if (null == PackageConverter.nameExpression) {
            try {
                PackageConverter.nameExpression = xpath.compile("name");
            } catch (XPathExpressionException ex) {
                PackageConverter.logger.error(ex.getMessage());
            }
        }
        return PackageConverter.nameExpression;
    }

    private static XPathExpression getClassExpression() {
        if (null == PackageConverter.classExpression) {
            try {
                PackageConverter.classExpression = xpath.compile("class[not(contains(child::name/text(), 'package-info'))]");
            } catch (XPathExpressionException ex) {
                PackageConverter.logger.error(ex.getMessage());
            }
        }
        return PackageConverter.classExpression;
    }
}
