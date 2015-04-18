package dependencyviewer.converter;

import dependencyviewer.model.Element;
import dependencyviewer.model.PackageElement;
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
public class PackageGraphConverter implements GraphConverter {

    private static final Logger logger = LoggerFactory.getLogger(PackageGraphConverter.class);

    private final Map<String, Set<String>> dependencies = new HashMap<String, Set<String>>();
    private Set<PackageElement> packages;
    private PackageFilter filter = new PackageFilter(new HashSet<String>());

    public PackageGraphConverter(final Set<PackageElement> packages) {
        if (PackageGraphConverter.logger.isDebugEnabled()) {
            PackageGraphConverter.logger.debug("New GraphConverter");
        }
        this.packages = packages;
    }

    public PackageGraphConverter(final Set<PackageElement> packages, final PackageFilter filter) {
        this(packages);
        this.filter = filter;
    }

    public Graph<Element, Number> createGraph() {
        if (PackageGraphConverter.logger.isDebugEnabled()) {
            PackageGraphConverter.logger.debug("Generating graph");
        }
        final Graph<Element, Number> graph = new DirectedSparseMultigraph<Element,Number>();
        int counter = 0;
        for (final PackageElement pe : this.packages) {
            if (this.filter.isFiltered(pe.getName())) {
                if (PackageGraphConverter.logger.isDebugEnabled()) {
                    PackageGraphConverter.logger.debug("Not adding package " + pe.getName() + " - it is filtered");
                }
                continue;
            }
            if (PackageGraphConverter.logger.isDebugEnabled()) {
                PackageGraphConverter.logger.debug("Add package " + pe.getName());
            }
            for (final PackageElement dep : pe.outboundDependencies()) {
                if (PackageGraphConverter.logger.isDebugEnabled()) {
                    PackageGraphConverter.logger.debug("Found dependency to " + dep.getName());
                }
                if (!this.filter.isFiltered(dep.getName()) && !this.dependencyExists(pe, dep)) {
                    graph.addEdge(counter, pe, dep);
                    counter++;
                }
            }
        }
        return graph;
    }

    private boolean dependencyExists(PackageElement pe, PackageElement dep) {
        boolean exists = false;
        final String key = pe.getName();
        final String value = dep.getName();

        if (this.dependencies.containsKey(key)) {
            if (PackageGraphConverter.logger.isDebugEnabled()) {
                PackageGraphConverter.logger.debug("Source already added " + key);
            }
            if (this.dependencies.get(key).contains(value)) {
                if (PackageGraphConverter.logger.isDebugEnabled()) {
                    PackageGraphConverter.logger.debug("Depdendency from " + key + " to " + value + " already exists");
                }
                exists = true;
            } else {
                if (PackageGraphConverter.logger.isDebugEnabled()) {
                    PackageGraphConverter.logger.debug("Adding dependency from " + key + " to " + value);
                }
                this.dependencies.get(key).add(value);
            }
        } else {
            Set<String> values = new HashSet<String>();
            values.add(value);
            if (PackageGraphConverter.logger.isDebugEnabled()) {
                PackageGraphConverter.logger.debug("Adding dependency from " + key + " to " + value);
            }
            this.dependencies.put(key, values);
        }

        return exists;
    }
}
