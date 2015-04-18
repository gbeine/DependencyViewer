/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dependencyviewer.ui;

import dependencyviewer.util.PackageFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractListModel;

/**
 *
 * @author gbeine
 */
public class FilterListModel extends AbstractListModel {

    private final List<String> filterStrings = new ArrayList<String>();
    private final String[] defaultFilterStrings = {
        "java.*",
        "javax.*",
        "com.sun*",
        "com.ibm.*",
        "org.omg.*",
        "org.w3c.*",
        "org.xml.*",
        "org.apache.*",
        "org.slf4j.*",
        "org.jdesktop.*",
        "org.jaxen.*",
        "org.dom4j.*",
        "org.jdom.*",
        "org.junit.*",
        "org.hamcrest.*",
        "org.objectweb.*",
        "edu.uci.*",
        "junit.*"
    };

    public FilterListModel() {
        this.filterStrings.addAll(Arrays.asList(defaultFilterStrings));
    }

    public FilterListModel(FilterListModel filterListModel) {
        this.filterStrings.addAll(filterListModel.filterStrings);
    }

    public void addElement(String element) {
        this.filterStrings.add(element);
    }

    public int getSize() {
        return this.filterStrings.size();
    }

    public Object getElementAt(int i) {
        return this.filterStrings.get(i);
    }

    public void removeElementAt(int i) {
        this.filterStrings.remove(i);
    }

    public void removeElement(String s) {
        this.filterStrings.remove(s);
    }

    public Set<String> getAllElements() {
        return Collections.unmodifiableSet(new HashSet<String>(this.filterStrings));
    }

    public PackageFilter getPackageFilter() {
        return new PackageFilter(this.getAllElements());
    }
}
