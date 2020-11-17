package Ex1n;

import java.lang.reflect.Array;
import java.util.*;

public class WGraph_DS implements weighted_graph {
    private HashMap<Integer, node_info> vertexes;
    private int nodesSize, modeCount;
    private double edgesSize;
    private static int i = 0;

    /**constructor**/
    public WGraph_DS(){
        vertexes = new HashMap<>();
        nodesSize = 0;
        edgesSize = 0;
        modeCount = 0;
    }

    @Override
    public node_info getNode(int key) {
        if(!vertexes.containsKey(key)) return null;
        return vertexes.get(key);
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        //if one of the nodes isn't in the graph
        if(this.getNode(node1)==null || this.getNode(node2)==null) return false;
        else{
            WGraph_DS.NodeInfo n1 = (NodeInfo) this.getNode(node1);
            WGraph_DS.NodeInfo n2 = (NodeInfo) this.getNode(node2);
            //search in the nodes neighbors listen
            return n1.neighbors.containsKey(n2);
        }
    }

    @Override
    public double getEdge(int node1, int node2) {
        if(!this.hasEdge(node1, node2)) return -1;
        WGraph_DS.NodeInfo n1 = (WGraph_DS.NodeInfo) this.getNode(node1);
        WGraph_DS.NodeInfo n2 = (WGraph_DS.NodeInfo) this.getNode(node2);
        //return the edge weight which associate to this node in the neighbors list
        return n1.neighbors.get(n2);
    }

    @Override
    public void addNode(int key) {
        //if node with the same key already exist in this graph
        if(this.getNode(key)!=null) return;
        WGraph_DS.NodeInfo newNode = new NodeInfo(key);
        //adding to the vertexes list
        vertexes.put(key, newNode);
        nodesSize++;
        modeCount++;
    }

    @Override
    public void connect(int node1, int node2, double w) {
        //if one of the nodes isn't in the graph
        if(this.getNode(node1)==null || this.getNode(node2)==null){
            return;
        }
        //if input weight is negative or 0, or if edge with the same weight already exist
        if(w<=0 ||w == this.getEdge(node1, node2) ) return;

        //if node1 equals node2 - do nothing
        if(node1==node2) return;

        else {
            WGraph_DS.NodeInfo n1 = (NodeInfo) this.getNode(node1);
            WGraph_DS.NodeInfo n2 = (NodeInfo) this.getNode(node2);

            //if there's existing edge - update
            if (this.getEdge(node1, node2) != -1) {
                n1.neighbors.replace(n2, w);
                n2.neighbors.replace(n1, w);
            }
            else {
                n1.neighbors.put(n2, w);
                n2.neighbors.put(n1, w);
                edgesSize++;
            }
            modeCount++;
        }
    }

    @Override
    public Collection<node_info> getV() {
        //creating shallow copy
        Collection<node_info> pointer = vertexes.values();
        return pointer;
    }

    @Override
    public Collection<node_info> getV(int node_id) {
        //getting the node
        WGraph_DS.NodeInfo node = (NodeInfo) this.getNode(node_id);
        //shallow copy to the node's neighbors
        Collection<node_info> pointer = node.neighbors.keySet();
        return pointer;
    }

    @Override
    public node_info removeNode(int key) {
        if(this.getNode(key) == null) return null;
        WGraph_DS.NodeInfo node = (NodeInfo) this.getNode(key);
        //going over all of nodes neighbors
        Iterator<node_info> it =  this.getV(key).iterator();
        while (it.hasNext()){
            node_info neighbor = it.next();//current neighbor
            int neighborKey = neighbor.getKey();
            it.remove();
            this.removeEdge(key, neighbor.getKey());
            modeCount++;
        }
        this.vertexes.remove(key);
        nodesSize--;
        modeCount++;
        return node;
        }

    @Override
    public void removeEdge(int node1, int node2) {
        if(this.getNode(node1)==null || this.getNode(node2)==null) return;
        WGraph_DS.NodeInfo n1 = (NodeInfo) this.getNode(node1);
        WGraph_DS.NodeInfo n2 = (NodeInfo) this.getNode(node2);
        n1.neighbors.remove(n2);
        n2.neighbors.remove(n1);
        edgesSize--;
        modeCount++;
    }

    @Override
    public boolean equals(Object graph){
        //making sure the input object is weighted_graph object
        if(!(graph instanceof weighted_graph)){
            return false;
        }
        weighted_graph g = (WGraph_DS) graph;
        //basic condition to be equals
        if(this.edgeSize() != g.edgeSize() || this.nodeSize() != g.nodeSize()) return false;

        boolean flag = true;
        Collection<node_info> gPointer = g.getV();
        Collection<node_info> thisPointer = this.getV();
        int[] gVertex = new int[gPointer.size()];
        int[] thisVertex = new int[thisPointer.size()];
        int i = 0;
        //copying all the vertexes keys of each graph to it's own array for simplification use
        for (node_info node: gPointer){
            gVertex[i++] = node.getKey();
        }
        i = 0;
        for(node_info node:thisPointer){
            thisVertex[i++] = node.getKey();
        }
        Arrays.sort(gVertex);
        Arrays.sort(thisVertex);
        for (int k = 0; k < thisVertex.length; k++) {
            if (flag == false) break;
            //if the nodes doesn't have the same key
            if(gVertex[k] != thisVertex[k]){
                flag = false;
                break;
            }
            Collection<node_info> gNodeNeighbors = g.getV(gVertex[k]);//current g's node's neighbors
            Collection<node_info> thisNodeNeighbors = this.getV(thisVertex[k]);//current graph current node's neighbors
            int[] gNeighbors = new int[gNodeNeighbors.size()];
            int[] tNeighbors = new int[thisNodeNeighbors.size()];
            int t = 0;
            //copying all the current nodes neighbors keys it's an array for simplification use
            for (node_info n : gNodeNeighbors) {
                gNeighbors[t++] = n.getKey();
            }
            t = 0;
            for (node_info n : thisNodeNeighbors) {
                tNeighbors[t++] = n.getKey();
            }
            Arrays.sort(tNeighbors);
            Arrays.sort(gNeighbors);
            //comparing between all the neighbors of the current nodes
            for (int l = 0; l < tNeighbors.length; l++) {
                double gEdge = g.getEdge(gVertex[k], gNeighbors[l]);
                double thisEdge = this.getEdge(thisVertex[k], tNeighbors[l]);
                //if neighbor's keys or the existing edge between them to current nodes are different
                if (gNeighbors[l] != tNeighbors[l] || gEdge != thisEdge) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    @Override
    public int nodeSize() { return nodesSize; }

    @Override
    public int edgeSize() {
        return (int)edgesSize;
    }

    @Override
    public int getMC() {
        return modeCount;
    }

     public class NodeInfo implements node_info {
        /** the node ID**/
        private int key;
        /** used in graphs algorithms **/
        private double tag;
        /** used in graphs algorithms **/
        private String info;
        /** Holds neighbors which connected to this node and the weight of their edge.**/
        private HashMap<node_info, Double> neighbors;

        /** constructor **/
        public NodeInfo(int key) {
             this.key =key;
             info = "";
             neighbors = new HashMap<>();
         }

         /** constructor **/
         public NodeInfo() {
             this.key =i;
             i++;
             info = "";
             neighbors = new HashMap<>();
         }

        @Override
        public int getKey() {
            return key;
        }

        @Override
        public String getInfo() {
            return info;
        }

        @Override
        public void setInfo(String s) {
            info = s;
        }

        @Override
        public double getTag() {
            return tag;
        }

        @Override
        public void setTag(double t) {
            tag = t;
        }
    }
}