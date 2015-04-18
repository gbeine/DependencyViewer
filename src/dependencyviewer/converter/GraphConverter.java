/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dependencyviewer.converter;

import dependencyviewer.model.Element;
import edu.uci.ics.jung.graph.Graph;

/**
 *
 * @author gbeine
 */
public interface GraphConverter {

    Graph<Element, Number> createGraph();

}
