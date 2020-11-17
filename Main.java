package Ex1n;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
//        weighted_graph gr = new WGraph_DS();
//        gr.addNode(2);
//        gr.addNode(3);
//        gr.connect(2,3,3);
//        WGraph_Algo k = new WGraph_Algo();
//        k.init(gr);
//        k.save("pa");
//        System.out.println(k.load("pa"));
//        Collection<node_info> po = k.g.getV();
//        for(node_info node: po){
//            System.out.println(node.getKey());
//        }
        weighted_graph g = new WGraph_DS();
        g.addNode(2);
        g.addNode(3);
        g.connect(2,3,1);
        weighted_graph gr = new WGraph_DS();
        gr.addNode(2);
        gr.addNode(3);
        gr.connect(2,3,1);
        System.out.println(g.equals(gr));
    }
}
