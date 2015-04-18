package dependencyviewer.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gbeine
 */
public class FeatureElement extends Element {

    private static final Logger logger = LoggerFactory.getLogger(FeatureElement.class);

    private ClassElement parentClass;

    public FeatureElement(final String name) {
        super(name);
        if (FeatureElement.logger.isDebugEnabled()) {
            FeatureElement.logger.debug("New FeatureElement: " + name);
        }
    }

    public void setParentClass(final ClassElement ce) {
        if (FeatureElement.logger.isDebugEnabled()) {
            FeatureElement.logger.debug("Parent ClassElement for FeatureElement: " + this.getName() + " set to " + ce.getName());
        }
        this.parentClass = ce;
    }

    public ClassElement getParentClass() {
        return this.parentClass;
    }

    public Set<FeatureElement> inboundDependencies() {
        return this.getDependencies().getInboundFeatureDependencies();
    }

    public Set<FeatureElement> outboundDependencies() {
        return this.getDependencies().getOutboundFeatureDependencies();
    }

    @Override
    public String toString() {
        return "Feature " + this.getName();
    }
}
