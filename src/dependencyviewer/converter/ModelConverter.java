package dependencyviewer.converter;

import dependencyviewer.model.converter.PackageConverter;
import dependencyviewer.model.PackageElement;
import dependencyviewer.util.NodeListIterator;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Transform a DOM tree of dependencies into a Dependency Viewer model
 *
 * @author gbeine
 */
public class ModelConverter {

    private static final Logger logger = LoggerFactory.getLogger(ModelConverter.class);

    private final Set<PackageElement> packages = new HashSet<PackageElement>();

    // Set to true after converting the document into the model
    private boolean converted = false;

    private Document document;

    public ModelConverter(Document document) {
        if (ModelConverter.logger.isDebugEnabled()) {
            ModelConverter.logger.debug("New ModelConverter");
        }
        this.document = document;
    }

    public Set<PackageElement> getPackages() {
        if (!this.converted) {
            if (ModelConverter.logger.isDebugEnabled()) {
                ModelConverter.logger.debug("Model not converted yet");
            }
            this.convert();
        }
        return Collections.unmodifiableSet(this.packages);
    }

    private void convert() {
        if (ModelConverter.logger.isDebugEnabled()) {
            ModelConverter.logger.debug("Start converting model");
        }
        final NodeList nl = this.document.getElementsByTagName("package");
        final Iterator<Node> nli  = new NodeListIterator(nl);
        while (nli.hasNext()) {
            final Node n = (Node) nli.next();
            this.packages.add(PackageConverter.convert(n));
        }
        if (ModelConverter.logger.isDebugEnabled()) {
            ModelConverter.logger.debug("Model converted successful");
        }
        this.converted = true;
    }

}
