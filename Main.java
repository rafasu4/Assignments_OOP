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
        weighted_graph g1 = new WGraph_DS();
        g1.addNode(2);
        g1.addNode(3);
        g1.addNode(4);
        g1.connect(2,3,4);
        weighted_graph g2 = new WGraph_DS();
        g2.addNode(2);
        g2.addNode(3);
        g2.addNode(4);
        g2.connect(3,2,5);
        System.out.println(g1.equals(g2));
        }
    }

