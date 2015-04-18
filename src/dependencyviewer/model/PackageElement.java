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
public class PackageElement extends Element {

    private static final Logger logger = LoggerFactory.getLogger(PackageElement.class);

    private final Set<ClassElement> classes = new HashSet<ClassElement>();

    // Set to true after building the dependencies
    private boolean dependenciesBuild = false;

    public PackageElement(final String name) {
        super(name);
        if (PackageElement.logger.isDebugEnabled()) {
            PackageElement.logger.debug("New PackageElement: " + name);
        }
    }
    
    public void addClass(final ClassElement ce) {
        this.classes.add(ce);
        ce.setParentPackage(this);
    }

    public Set<ClassElement> getClasses() {
        return Collections.unmodifiableSet(this.classes);
    }

    public Set<PackageElement> inboundDependencies() {
        return this.getDependencies().getInboundPackageDependencies();
    }

    public Set<PackageElement> outboundDependencies() {
        return this.getDependencies().getOutboundPackageDependencies();
    }

    @Override
    public Dependencies getDependencies() {
        if (!this.dependenciesBuild) {
            if (PackageElement.logger.isDebugEnabled()) {
                PackageElement.logger.debug("No dependencies calculated yet for " + this.getName());
            }
            this.buildDependencies();
        }
        return super.getDependencies();
    }

    @Override
    public String toString() {
        return "Package: " + this.getName() + " Inbound Dependencies: " + this.getDependencies().getInboundPackageDependencies().size() + " Outbound dependencies: " + this.getDependencies().getOutboundPackageDependencies().size() + " Classes: " + this.classes.size();
    }

    private void buildDependencies() {
        if (PackageElement.logger.isDebugEnabled()) {
            PackageElement.logger.debug("Building dependencies");
        }
        for (ClassElement ce : this.classes) {
            if (PackageElement.logger.isDebugEnabled()) {
                PackageElement.logger.debug("Dependencies for ClassElement: " + ce.getName());
            }
            // We need to make sure to get the dependencies from super class
            // This is to avoid endless recursion
            super.getDependencies().addDependencies(ce.getDependencies());

//          Just a one-liner now :-)
//            for (ClassElement dep : ce.getDepedencies()) {
//                if (PackageElement.logger.isDebugEnabled()) {
//                    PackageElement.logger.debug("Dependency to ClassElement: " + dep.getName());
//                }
//                PackageElement pe = dep.getParentPackage();
//                if (!pe.equals(this)) {
//                    if (PackageElement.logger.isDebugEnabled()) {
//                        PackageElement.logger.debug("Dependency to PackageElement: " + pe.getName());
//                    }
//                    this.getDependencies();
//                }
//            }
        }
    }
}
