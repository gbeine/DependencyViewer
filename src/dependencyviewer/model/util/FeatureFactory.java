/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dependencyviewer.model.util;

import dependencyviewer.model.FeatureElement;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gbeine
 */
public class FeatureFactory {

    private static final Logger logger = LoggerFactory.getLogger(FeatureFactory.class);
    private static final Map<String,FeatureElement> features = new HashMap<String,FeatureElement>();

    public static FeatureElement createFeature(final String name) {
        FeatureElement fe;
        if (FeatureFactory.features.containsKey(name)) {
            if (FeatureFactory.logger.isDebugEnabled()) {
                FeatureFactory.logger.debug("FeatureElement " + name + " already exists.");
            }
            fe = FeatureFactory.features.get(name);
        } else {
            if (FeatureFactory.logger.isDebugEnabled()) {
                FeatureFactory.logger.debug("Creating new FeatureElement: " + name);
            }
            fe = new FeatureElement(name);
            FeatureFactory.features.put(name, fe);
        }
        return fe;
    }
}
