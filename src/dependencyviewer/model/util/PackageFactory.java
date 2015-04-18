/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dependencyviewer.model.util;

import dependencyviewer.model.PackageElement;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gbeine
 */
public class PackageFactory {

    private static final Logger logger = LoggerFactory.getLogger(PackageFactory.class);
    private static final Map<String,PackageElement> packages = new HashMap<String,PackageElement>();

    public static PackageElement createPackage(final String name) {
        PackageElement pe;
        if (PackageFactory.packages.containsKey(name)) {
            if (PackageFactory.logger.isDebugEnabled()) {
                PackageFactory.logger.debug("PackageElement " + name + " already exists.");
            }
            pe = PackageFactory.packages.get(name);
        } else {
            if (PackageFactory.logger.isDebugEnabled()) {
                PackageFactory.logger.debug("Creating new PackageElement: " + name);
            }
            pe = new PackageElement(name);
            PackageFactory.packages.put(name, pe);
        }
        return pe;
    }
}
