/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dependencyviewer.converter;

import dependencyviewer.model.ClassElement;
import dependencyviewer.model.PackageElement;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author gbeine
 */
public class ContentProvider {

    // The Elements to show
    private Set<PackageElement> elements;

    /**
     * Create a new ContentProvider for a Graph
     * @param graph
     */
    public ContentProvider(final Set<PackageElement> elements) {
        this.elements = elements;
    }

    /**
     * 
     * @return
     */
    public Set<PackageElement> getPackages() {
        return this.elements;
    }

    public Set<ClassElement> getClasses() {
        final Set<ClassElement> classes = new HashSet<ClassElement>();
        for (PackageElement pe: this.elements) {
            classes.addAll(pe.getClasses());
        }
        return classes;
    }

    public int size() {
        return this.elements.size();
    }
}
