package dependencyviewer.model.util;

import dependencyviewer.model.ClassElement;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gbeine
 */
public class ClassFactory {

    private static final Logger logger = LoggerFactory.getLogger(ClassFactory.class);
    private static final Map<String,ClassElement> classes = new HashMap<String,ClassElement>();

    public static ClassElement createClass(final String name) {
        ClassElement ce;
        if (ClassFactory.classes.containsKey(name)) {
            if (ClassFactory.logger.isDebugEnabled()) {
                ClassFactory.logger.debug("ClassElement " + name + " already exists.");
            }
            ce = ClassFactory.classes.get(name);
        } else {
            if (ClassFactory.logger.isDebugEnabled()) {
                ClassFactory.logger.debug("Creating new ClassElement: " + name);
            }
            ce = new ClassElement(name);
            ClassFactory.classes.put(name, ce);
        }
        return ce;
    }
}
