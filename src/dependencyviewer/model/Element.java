package dependencyviewer.model;

/**
 *
 * @author gbeine
 */
public abstract class Element {

    private String name;
    private Dependencies dependencies;

    public Element(final String name) {
        this.name = name;
        this.dependencies = new Dependencies(this);
    }

    public String getName() {
        return this.name;
    }

    public Dependencies getDependencies() {
        return this.dependencies;
    }
}
