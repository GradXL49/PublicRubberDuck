/*
 * Grady Landers
 * Master's Project - Code name Rubber Duck
 * Node class for A* Search implementation
 * heavily inspired by https://stackabuse.com/graphs-in-java-a-star-algorithm/
 */

package backend;

import java.util.ArrayList;

public class Node implements Comparable<Node> {
    // Id for readability of result purposes
    public String id;

    // Parent in the path
    public Node parent = null;

    public ArrayList<Edge> neighbors;

    // Evaluation functions
    public double f = Double.MAX_VALUE;
    public double g = Double.MAX_VALUE;
    // Hardcoded heuristic
    public double h; 

    Node(String id){
          this.h = 0;
          this.id = id;
          this.neighbors = new ArrayList<>();
    }
    
    public void setH(double h){
    	  this.h = h;
    }

    @Override
    public int compareTo(Node n) {
          return Double.compare(this.f, n.f);
    }

    public static class Edge {
          Edge(int weight, Node node){
                this.weight = weight;
                this.node = node;
          }

          public int weight;
          public Node node;
    }

    public void addBranch(int weight, Node node){
          Edge newEdge = new Edge(weight, node);
          neighbors.add(newEdge);
    }

    public double calculateHeuristic(Node target){
          return this.h;
    }
}
