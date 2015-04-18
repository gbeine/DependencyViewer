package dependencyviewer.model.util;

import dependencyviewer.model.Element;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author gbeine
 */
public class ElementLabeller implements Transformer<Element, String> {

    public String transform(final Element i) {
        return i.getName();
    }
    
}
