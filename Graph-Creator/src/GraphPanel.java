package graphcreator;

import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GraphPanel extends JPanel{
	
	ArrayList<Node> nodeList = new ArrayList<Node>();
	ArrayList<Edge> edgeList = new ArrayList<Edge>();
	int circleRadius = 20;

	ArrayList<ArrayList<Boolean>> adjacency = new ArrayList<ArrayList<Boolean>>();
	
	public GraphPanel() {
		super();
	}
	
	public ArrayList<String> getConnectedLabels(String label) {
		ArrayList<String> toReturn = new ArrayList<String>();
		int b = getIndex(label);
		for (int a = 0; a < adjacency.size(); a++) {
			if ((adjacency.get(b).get(a) == true) && nodeList.get(a).getLabel().equals(label) == false) {
				toReturn.add(nodeList.get(a).getLabel());
			}
		}
		return toReturn;
	}
	
	public void printAdjacency() {
		System.out.println();
		for (int a = 0; a < adjacency.size(); a++) {
			for (int b = 0; b < adjacency.get(0).size(); b++ ) {
				System.out.println(adjacency.get(a).get(b) + "\t");
			}
			System.out.println();
		}
	}
	
	public void addNode(int newx, int newy, String newlabel) {
		nodeList.add(new Node(newx, newy,  newlabel));
		adjacency.add(new ArrayList<Boolean>());
		for (int a = 0; a < adjacency.size() - 1; a++) {
			adjacency.get(a).add(false);
		}
		for (int a = 0; a < adjacency.size(); a++) {
			adjacency.get(adjacency.size() - 1).add(false);
		}
		printAdjacency();
	}
	
	
	public void addEdge(Node first, Node second, String newlabel) {
		edgeList.add(new Edge(first, second, newlabel));
		int firstIndex = 0;
		int secondIndex = 0;
		for (int a = 0; a < nodeList.size(); a++) {
			if (first.equals(nodeList.get(a))) {
				firstIndex = a;
			}
			if (second.equals(nodeList.get(a))) {
				secondIndex = a;
			}
		}
		adjacency.get(firstIndex).set(secondIndex, true);
		adjacency.get(secondIndex).set(firstIndex, true); 
		printAdjacency();
	}
	
	public Node getNode(int x, int y) {
		for (int a = 0; a < nodeList.size(); a++) {
			Node node = nodeList.get(a);
			//a squared plus b squared = c squared
			double radius = Math.sqrt(Math.pow(x-node.getX(), 2) + Math.pow(y-node.getY(), 2));
			if (radius < circleRadius) {
				return node;
			}
		}
		return null;
	}
	
	public Node getNode(String s) {
		for (int a = 0; a < nodeList.size(); a++) {
			Node node = nodeList.get(a);
			if (s.equals(node.getLabel())) {
				return node;
			}
		}
		return null;
	}
	
	public int getIndex(String s) {
		for (int a = 0; a < nodeList.size(); a++) {
			Node node = nodeList.get(a);
			if (s.equals(node.getLabel())) {
				return a;
			}
		}
		return -1;
	}
	
	public boolean nodeExists(String s) {
		for (int a = 0; a < nodeList.size(); a++) {
			if (s.equals(nodeList.get(a).getLabel())) {
				return true;
			}
		}
		return false;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//draw my stuff
		for (int a = 0; a < nodeList.size(); a++) {
			if (nodeList.get(a).getHighlighted() == true) {
				g.setColor(Color.RED);
			}
			else {
				g.setColor(Color.BLACK);
			}
			g.drawOval(nodeList.get(a).getX() - circleRadius, nodeList.get(a).getY() - circleRadius, circleRadius * 2, circleRadius * 2);
			g.drawString(nodeList.get(a).getLabel(), nodeList.get(a).getX(), nodeList.get(a).getY());
		}
		for (int a = 0; a < edgeList.size(); a++) {
			g.drawLine(edgeList.get(a).getFirst().getX(),
					   edgeList.get(a).getFirst().getY(),
					   edgeList.get(a).getSecond().getX(),
					   edgeList.get(a).getSecond().getY());
			int fx = edgeList.get(a).getFirst().getX();
			int fy = edgeList.get(a).getFirst().getY();
			int sx = edgeList.get(a).getSecond().getX();
			int sy = edgeList.get(a).getSecond().getY();
			g.drawString(edgeList.get(a).getLabel(), 
					Math.min(fx, sx) + (Math.abs(sx - fx) / 2),
					Math.min(fy, sy) + (Math.abs(sy - fy) / 2));
		}
	}

	public void stopHighlighting() {
		for (int a = 0; a < nodeList.size(); a++) {
			nodeList.get(a).setHighlighted(false);
		}
	}
}