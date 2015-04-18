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
public class Dependencies {

    private static final Logger logger = LoggerFactory.getLogger(Dependencies.class);

    private Set<PackageElement> inboundPackages = new HashSet<PackageElement>();
    private Set<PackageElement> outboundPackages = new HashSet<PackageElement>();
    private Set<ClassElement> inboundClasses = new HashSet<ClassElement>();
    private Set<ClassElement> outboundClasses = new HashSet<ClassElement>();
    private Set<FeatureElement> inboundFeatures = new HashSet<FeatureElement>();
    private Set<FeatureElement> outboundFeatures = new HashSet<FeatureElement>();

    private boolean inboundPackagesBuild = Boolean.FALSE;
    private boolean outboundPackagesBuild = Boolean.FALSE;
    private boolean inboundClassesBuild = Boolean.FALSE;
    private boolean outboundClassesBuild = Boolean.FALSE;

    private Element element;

    public Dependencies(Element element) {
        this.element = element;
    }

    public void addInboundDependency(final PackageElement pe) {
        if (null != pe && !this.element.equals(pe)) {
            this.inboundPackages.add(pe);
            this.needRebuild();
        }
    }

    public void addOutboundDependency(final PackageElement pe) {
        if (null != pe && !this.element.equals(pe)) {
            this.outboundPackages.add(pe);
            this.needRebuild();
        }
    }

    public void addInboundDependency(final FeatureElement fe) {
        if (null != fe && !this.element.equals(fe)) {
            this.inboundFeatures.add(fe);
            this.needRebuild();
        }
    }

    public void addOutboundDependency(final FeatureElement fe) {
        if (null != fe && !this.element.equals(fe)) {
            this.outboundFeatures.add(fe);
            this.needRebuild();
        }
    }

    public void addInboundDependency(final ClassElement ce) {
        if (null != ce && !this.element.equals(ce)) {
            this.inboundClasses.add(ce);
            this.needRebuild();
        }
    }

    public void addOutboundDependency(final ClassElement ce) {
        if (null != ce && !this.element.equals(ce)) {
            this.outboundClasses.add(ce);
            this.needRebuild();
        }
    }

    void addDependencies(Dependencies d) {
        this.inboundClasses.addAll(d.inboundClasses);
        this.outboundClasses.addAll(d.outboundClasses);
        this.inboundFeatures.addAll(d.inboundFeatures);
        this.outboundFeatures.addAll(d.outboundFeatures);
        this.inboundPackages.addAll(d.inboundPackages);
        this.outboundPackages.addAll(d.outboundPackages);
        this.needRebuild();
    }

    Set<PackageElement> getInboundPackageDependencies() {
        if (!this.inboundPackagesBuild) {
            for(FeatureElement fe: this.inboundFeatures) {
                if (null != fe.getParentClass()) {
                    this.addInboundDependency(fe.getParentClass().getParentPackage());
                }
            }
            for(ClassElement ce: this.inboundClasses) {
                this.addInboundDependency(ce.getParentPackage());
            }
            this.inboundPackagesBuild = true;
        }
        return Collections.unmodifiableSet(this.inboundPackages);
    }

    Set<PackageElement> getOutboundPackageDependencies() {
        if (!this.outboundPackagesBuild) {
            for(FeatureElement fe: this.outboundFeatures) {
                if (null != fe.getParentClass()) {
                    this.addOutboundDependency(fe.getParentClass().getParentPackage());
                }
            }
            for(ClassElement ce: this.outboundClasses) {
                this.addOutboundDependency(ce.getParentPackage());
            }
            this.outboundPackagesBuild = true;
        }
        return Collections.unmodifiableSet(this.outboundPackages);
    }

    Set<ClassElement> getInboundClassDependencies() {
        if (!this.inboundClassesBuild) {
            for(FeatureElement fe: this.inboundFeatures) {
                this.addInboundDependency(fe.getParentClass());
            }
            this.inboundClassesBuild = true;
        }
        return Collections.unmodifiableSet(this.inboundClasses);
    }

    Set<ClassElement> getOutboundClassDependencies() {
        if (!this.outboundClassesBuild) {
            for(FeatureElement fe: this.outboundFeatures) {
                this.addOutboundDependency(fe.getParentClass());
            }
            this.outboundClassesBuild = true;
        }
        return Collections.unmodifiableSet(this.outboundClasses);
    }

    Set<FeatureElement> getInboundFeatureDependencies() {
        return Collections.unmodifiableSet(this.inboundFeatures);
    }

    Set<FeatureElement> getOutboundFeatureDependencies() {
        return Collections.unmodifiableSet(this.outboundFeatures);
    }

    private void needRebuild() {
        this.inboundClassesBuild = false;
        this.outboundClassesBuild = false;
        this.inboundPackagesBuild = false;
        this.outboundPackagesBuild = false;
    }
}
