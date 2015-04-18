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
public class ClassElement extends Element {

    private static final Logger logger = LoggerFactory.getLogger(ClassElement.class);

    private PackageElement parentPackage;
    private final Set<FeatureElement> features = new HashSet<FeatureElement>();

    // Set to true after building the dependencies
    private boolean dependenciesBuild = false;

    public ClassElement(final String name) {
        super(name);
        if (ClassElement.logger.isDebugEnabled()) {
            ClassElement.logger.debug("New ClassElement: " + name);
        }
    }

    public void setParentPackage(final PackageElement pe) {
        if (ClassElement.logger.isDebugEnabled()) {
            ClassElement.logger.debug("Parent PackageElement for ClassElement: " + this.getName() + " set to " + pe.getName());
        }
        this.parentPackage = pe;
    }

    public PackageElement getParentPackage() {
        return this.parentPackage;
    }
    
    public void addFeature(final FeatureElement fe) {
        this.features.add(fe);
        fe.setParentClass(this);
    }

    public Set<FeatureElement> getFeatures() {
        return Collections.unmodifiableSet(this.features);
    }

    public Set<ClassElement> inboundDependencies() {
        return this.getDependencies().getInboundClassDependencies();
    }

    public Set<ClassElement> outboundDependencies() {
        return this.getDependencies().getOutboundClassDependencies();
    }

    @Override
    public Dependencies getDependencies() {
        if (!this.dependenciesBuild) {
            if (ClassElement.logger.isDebugEnabled()) {
                ClassElement.logger.debug("No dependencies calculated yet for " + this.getName());
            }
            this.buildDependencies();
        }
        return super.getDependencies();
    }

    @Override
    public String toString() {
        return "Class " + this.getName();
    }

    private void buildDependencies() {
        if (ClassElement.logger.isDebugEnabled()) {
            ClassElement.logger.debug("Building dependencies");
        }
        for (FeatureElement fe : this.features) {
            if (ClassElement.logger.isDebugEnabled()) {
                ClassElement.logger.debug("Adding Dependencies for FeatureElement: " + fe.getName());
            }
            // We need to make sure to get the dependencies from super class
            // This is to avoid endless recursion
            super.getDependencies().addDependencies(fe.getDependencies());

//          Just a one-liner now :-)
//            for (FeatureElement dep : fe.inboundDependencies()) {
//                if (ClassElement.logger.isDebugEnabled()) {
//                    ClassElement.logger.debug("Dependency from FeatureElement: " + dep.getName());
//                }
//                ClassElement ce = dep.getParentClass();
//                if (!ce.equals(this)) {
//                    if (ClassElement.logger.isDebugEnabled()) {
//                        ClassElement.logger.debug("Dependency from ClassElement: " + ce.getName());
//                    }
//                    this.getDependencies().addInboundDependency(ce);
//                }
//            }
//            for (FeatureElement dep : fe.outboundDependencies()) {
//                if (ClassElement.logger.isDebugEnabled()) {
//                    ClassElement.logger.debug("Dependency to FeatureElement: " + dep.getName());
//                }
//                ClassElement ce = dep.getParentClass();
//                if (!ce.equals(this)) {
//                    if (ClassElement.logger.isDebugEnabled()) {
//                        ClassElement.logger.debug("Dependency to ClassElement: " + ce.getName());
//                    }
//                    this.getDependencies().addOutboundDependency(ce);
//                }
//            }
        }
//        this.dependenciesBuild = true;
    }
}
