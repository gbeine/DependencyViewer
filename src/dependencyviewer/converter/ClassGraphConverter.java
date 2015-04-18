package dependencyviewer.converter;

import dependencyviewer.model.ClassElement;
import dependencyviewer.model.Element;
import dependencyviewer.util.PackageFilter;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transform a DependencyViewer model into a jung2 graph
 *
 * @author gbeine
 */
public class ClassGraphConverter implements GraphConverter {

    private static final Logger logger = LoggerFactory.getLogger(ClassGraphConverter.class);

    private final Map<String, Set<String>> dependencies = new HashMap<String, Set<String>>();
    private Set<ClassElement> classes;
    private PackageFilter filter = new PackageFilter(new HashSet<String>());

    public ClassGraphConverter(final Set<ClassElement> classes) {
        if (ClassGraphConverter.logger.isDebugEnabled()) {
            ClassGraphConverter.logger.debug("New GraphConverter");
        }
        this.classes = classes;
    }

    public ClassGraphConverter(final Set<ClassElement> classes, final PackageFilter filter) {
        this(classes);
        this.filter = filter;
    }

    public Graph<Element, Number> createGraph() {
        if (ClassGraphConverter.logger.isDebugEnabled()) {
            ClassGraphConverter.logger.debug("Generating graph");
        }
        final Graph<Element, Number> graph = new DirectedSparseMultigraph<Element,Number>();
        int counter = 0;
        for (final ClassElement ce : this.classes) {
            if (this.filter.isFiltered(ce.getName())) {
                if (ClassGraphConverter.logger.isDebugEnabled()) {
                    ClassGraphConverter.logger.debug("Not adding class " + ce.getName() + " - it is filtered");
                }
                continue;
            }
            if (ClassGraphConverter.logger.isDebugEnabled()) {
                ClassGraphConverter.logger.debug("Add class " + ce.getName());
            }
            for (final ClassElement dep : ce.outboundDependencies()) {
                if (ClassGraphConverter.logger.isDebugEnabled()) {
                    ClassGraphConverter.logger.debug("Found dependency to " + dep.getName());
                }
                if (!this.filter.isFiltered(dep.getName()) && !this.dependencyExists(ce, dep)) {
                    graph.addEdge(counter, ce, dep);
                    counter++;
                }
            }
        }
        return graph;
    }

    private boolean dependencyExists(ClassElement ce, ClassElement dep) {
        boolean exists = false;
        final String key = ce.getName();
        final String value = dep.getName();

        if (this.dependencies.containsKey(key)) {
            if (ClassGraphConverter.logger.isDebugEnabled()) {
                ClassGraphConverter.logger.debug("Source already added " + key);
            }
            if (this.dependencies.get(key).contains(value)) {
                if (ClassGraphConverter.logger.isDebugEnabled()) {
                    ClassGraphConverter.logger.debug("Depdendency from " + key + " to " + value + " already exists");
                }
                exists = true;
            } else {
                if (ClassGraphConverter.logger.isDebugEnabled()) {
                    ClassGraphConverter.logger.debug("Adding dependency from " + key + " to " + value);
                }
                this.dependencies.get(key).add(value);
            }
        } else {
            Set<String> values = new HashSet<String>();
            values.add(value);
            if (ClassGraphConverter.logger.isDebugEnabled()) {
                ClassGraphConverter.logger.debug("Adding dependency from " + key + " to " + value);
            }
            this.dependencies.put(key, values);
        }

        return exists;
    }
}
