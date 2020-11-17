package Ex1n;

import ex0.node_data;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms{
    public weighted_graph g;

    @Override
    public void init(weighted_graph g) {
        this.g = g;
    }

    @Override
    public weighted_graph getGraph() {
        return this.g;
    }

    @Override
    public weighted_graph copy() {
        weighted_graph graph = new WGraph_DS();
        //deep copy of vertexes
        for(node_info vertex: this.g.getV()){
            graph.addNode(vertex.getKey());
        }
        //connecting edges the same as the original graph
        for (node_info vertex: this.g.getV()) {
            node_info current = graph.getNode(vertex.getKey());
            Collection<node_info> pointer = g.getV(vertex.getKey());//pointer to a vertex neighbors list
            Iterator<node_info> it = pointer.iterator();
            while(it.hasNext()){
                node_info neighbor = it.next();
                double edge = this.g.getEdge(neighbor.getKey(), vertex.getKey());//copy the edge between original vertex and neighbor
                graph.connect(neighbor.getKey(), current.getKey(), edge);
            }
        }
        return graph;
    }

    @Override
    public boolean isConnected() {
        weighted_graph g = this.g;
        if (g.nodeSize() == 0) return true; //Vacuously true
        int counter = 1;//for later compare
        Queue<node_info> q = new LinkedList<>();
        //tagging each node
        for(node_info node:g.getV()){
            node.setInfo("V");
        }
        Iterator<node_info> it = g.getV().iterator();
        node_info stratingNode = it.next();//starting from this node to reach all other node
        stratingNode.setInfo("");
        q.add(stratingNode);
        while(!q.isEmpty()){
            node_info current = q.poll();
            //adding all neighbors to q
            for(node_info neighbor: g.getV(current.getKey())) {
                //if neighbor wasn't in q
                if (neighbor.getInfo() == "V") {
                    neighbor.setInfo("");//remove tag
                    q.add(neighbor);
                    counter++;
                }
            }
        }
        return counter==g.nodeSize();
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        weighted_graph g = this.g;
        if(g.getNode(src)==null || g.getNode(dest)==null){
            throw new NoSuchElementException("one of the nodes isn't in the graph!");
        }
        Queue<node_info> q = new LinkedList<>();
        double[] distance = new double[g.nodeSize()];
        int i = 0;
        //tagging each node for later use
        for(node_info vertex : g.getV()){
            vertex.setTag(i);
            vertex.setInfo("V");
            distance[i] = -1;
            i++;
        }
        node_info source = g.getNode(src);
        node_info destination = g.getNode(dest);
        distance[(int)source.getTag()] = 0;
        q.add(source);
        while (!q.isEmpty()){
            node_info current = q.poll();
            current.setInfo("");//preventing node to be added to q twice
            for (node_info neighbor : g.getV(current.getKey())) {//adding current's neighbors to q
                double newEdge = g.getEdge(neighbor.getKey(), current.getKey()) + distance[(int) current.getTag()];
                double currentEdge = distance[(int) neighbor.getTag()];
                if (currentEdge == -1 || newEdge < currentEdge) {
                    distance[(int) neighbor.getTag()] = newEdge;
                }
                if (neighbor.getInfo() == "V") {//if neighbor is marked)
                    q.add(neighbor);
                }
            }
        }

        double ans = distance[(int) destination.getTag()];
        //removing the tags and marks
        for (node_info vertex: g.getV()){
            vertex.setInfo("");
            vertex.setTag(0);
        }
        return ans;
    }

    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if(g.getNode(src)==null || g.getNode(dest)==null){
            throw new NoSuchElementException("one of the nodes isn't in the graph!");
        }
        Queue<node_info> q = new LinkedList<>();
        double[] distance = new double[g.nodeSize()];
        node_info[] prev = new node_info[g.nodeSize()];//array of predecessors
        List<node_info> path = new ArrayList<>();//final result
        node_info source = g.getNode(src);
        node_info destination = g.getNode(dest);
        if(source.equals(destination)){
            path.add(source);
            return path;
        }
        int i = 0;
        for(node_info vertex : g.getV()){
            vertex.setTag(i);
            vertex.setInfo("V");
            distance[i] = -1;
            i++;
        }
        distance[(int)source.getTag()] = 0;
        q.add(source);
        while (!q.isEmpty()) {
            node_info current = q.poll();
            current.setInfo("");
            for (node_info neighbor : g.getV(current.getKey())) {
                double newEdge = g.getEdge(neighbor.getKey(), current.getKey()) + distance[(int) current.getTag()];
                double currentEdge = distance[(int) neighbor.getTag()];//existing edge
                if (currentEdge == -1 || newEdge < currentEdge) {
                    distance[(int) neighbor.getTag()] = newEdge;
                    prev[(int) neighbor.getTag()] = current;//point to the previous in its location at the array
                }
                if (neighbor.getInfo() == "V") {
                    q.add(neighbor);
                }
            }
        }

        //if there's no path
        if(distance[destination.getKey()]==-1){
            throw new NullPointerException("No path!");
        }
        else{
            path.add(destination);
            node_info parent = prev[(int)destination.getTag()];
            //adding nodes course to path from end to start
            while(parent!=null){
                path.add(parent);
                parent=prev[(int)parent.getTag()];
            }
            Collections.reverse(path);//rearranging from start to end
            return path;
        }
    }

    @Override
    public boolean save(String file) {
        boolean flag = false;
        try {
            File files = new File(file);
            PrintWriter pw = new PrintWriter(files);
            StringBuilder sb = new StringBuilder();
            //writing down this graph components
            for (node_info vertex: this.g.getV()) {
                sb.append(vertex.getKey());
                sb.append("- neighbors -");
                //adding all of current's neighbors
                for (node_info neighbor: g.getV(vertex.getKey())) {
                    sb.append(neighbor.getKey()+"/"+g.getEdge(vertex.getKey(), neighbor.getKey()));
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length()-1);
                sb.append("\n");
                pw.write(sb.toString());
                sb.setLength(0);
            }
            pw.close();
            flag = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean load(String file) {
        weighted_graph g = new WGraph_DS();
        boolean flag = false;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null){
                String[] nodeInfo = line.split("- neighbors -");
                int nodeKey = Integer.parseInt(nodeInfo[0]);
                g.addNode(nodeKey);
                line = nodeInfo[1];
                nodeInfo = line.split(",");//separating to each neighbor and it's edge
                for (int i = 0; i < nodeInfo.length ; i++) {
                    String innerSub[] = nodeInfo[i].split("/");//separating between current neighbor and it's edge
                    int neighborKey = Integer.parseInt(innerSub[0]);
                    double neighborEdge = Double.parseDouble(innerSub[1]);
                    g.addNode(neighborKey);
                    g.connect(neighborKey, nodeKey, neighborEdge);
                }
                this.g = g;
                flag = true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
