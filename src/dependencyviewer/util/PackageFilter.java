/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dependencyviewer.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 *
 * @author gbeine
 */
public class PackageFilter {

    private Set<String> filterStrings;
    private Set<Pattern> patterns = new HashSet<Pattern>();

    public PackageFilter(Set<String> filterStrings) {
        this.filterStrings = filterStrings;
    }

    private void compilePatterns() {
        for (String patternString: this.filterStrings) {
            this.patterns.add(Pattern.compile(patternString));
        }
    }

    public boolean isFiltered(String name) {
        // This is a little bit ugly...
        // Unfortunately there are some files with null values in package names
        // The default package should be dropped out from Java
        boolean matched = null == name;
        if (this.patterns.isEmpty()) {
            this.compilePatterns();
        }
        if (!matched) {
            for (Pattern pattern: this.patterns) {
                matched |= pattern.matcher(name).matches();
            }
        }
        return matched;
    }
}
