package dependencyviewer.util;

import java.util.Iterator;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author gbeine
 */
public class NodeListIterator implements Iterator<Node> {

    private NodeList nodeList;
    private int length = 0;
    private int current = 0;

    public NodeListIterator(NodeList nl) {
        this.nodeList = nl;
        this.length = nl.getLength();
    }

    public boolean hasNext() {
        return this.current < this.length;
    }

    public Node next() {
        Node node = this.nodeList.item(current);
        this.current++;
        return node;
    }

    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
