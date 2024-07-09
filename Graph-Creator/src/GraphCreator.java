//Kevin Yu
//5-16-22
//find the shortest path, got help from other classmates.

package graphcreator;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Color;

import java.awt.event.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.JFrame; 
import javax.swing.JButton;
import javax.swing.JTextField;

import java.util.*;
import java.util.ArrayList;

public class GraphCreator implements ActionListener, MouseListener {

  //Creation of frame, buttons, containers, etc.
  JFrame frame = new JFrame();
  GraphPanel panel = new GraphPanel();
  JButton nodeB = new JButton("Node");
  JButton edgeB = new JButton("Edge");
  JTextField labelsTF = new JTextField("A");
  JTextField firstNode = new JTextField("First");
  JTextField secondNode = new JTextField("Second");
  JButton connectedB = new JButton("Test Connected");
  Container west = new Container();
  Container east = new Container();
  Container south = new Container();
  JTextField salesmanStartTF = new JTextField("A");
  JButton salesmanB = new JButton("Traveling Salesmen");
  final int NODE_CREATE = 0;
  final int EDGE_FIRST = 1;
  final int EDGE_SECOND = 2;
  int state = NODE_CREATE;
  Node first = null;
  ArrayList<ArrayList<Node>> completed = new ArrayList<ArrayList<Node>>();

  public GraphCreator() {
    //Setting up frame, adding action listeners, and putting in the buttons/containers
    frame.setSize(800,600);
    frame.setLayout(new BorderLayout());
    frame.add(panel, BorderLayout.CENTER);
    west.setLayout(new GridLayout(3,1));
    west.add(nodeB);
    nodeB.addActionListener(this);
    nodeB.setBackground(Color.GREEN);
    west.add(edgeB);
    edgeB.addActionListener(this);
    edgeB.setBackground(Color.LIGHT_GRAY);
    west.add(labelsTF);
    frame.add(west, BorderLayout.WEST);
    east.setLayout(new GridLayout(3,1));
    east.add(firstNode);
    east.add(secondNode);
    east.add(connectedB);
    connectedB.addActionListener(this);
    frame.add(east, BorderLayout.EAST);
    panel.addMouseListener(this);
    south.setLayout(new GridLayout(1,2));
    south.add(salesmanStartTF);
    south.add(salesmanB);
    salesmanB.addActionListener(this);
    frame.add(south, BorderLayout.SOUTH);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    new GraphCreator();
  }

  public void mouseClicked(MouseEvent e) { 
  }

  public void mouseEntered(MouseEvent e) {
  }

  public void mouseExited(MouseEvent e) {
  }
  
  public void mousePressed(MouseEvent e) {
  }

  public void mouseReleased(MouseEvent e) {
    //Node creation
    if (state == NODE_CREATE) {
      panel.addNode(e.getX(), e.getY(), labelsTF.getText());
    }
    //Edge creation
    else if (state == EDGE_FIRST) {
      Node n = panel.getNode(e.getX(), e.getY());
      if (n != null) {
        first = n;
        state = EDGE_SECOND;
        n.setHighlighted(true);
      }
    }
    else if (state == EDGE_SECOND) {
      Node n = panel.getNode(e.getX(), e.getY());
      if (n != null && first.equals(n) == false) {
        String s = labelsTF.getText();
        boolean valid = true;
        for (int a = 0; a < s.length(); a++) {
          if (Character.isDigit(s.charAt(a)) == false) {
            valid = false;
          }
        }
        if (valid == true) {
          first.setHighlighted(false);
          panel.addEdge(first, n, labelsTF.getText());
          first = null;
          state = EDGE_FIRST;
        }
        else {
          JOptionPane.showMessageDialog(frame, "Can only have digits in edge labels.");
        }
      }
    }
    frame.repaint();
  }

  public void actionPerformed(ActionEvent e) {
    //Node button action performed
    if (e.getSource().equals(nodeB)) {
      nodeB.setBackground(Color.GREEN);
      edgeB.setBackground(Color.LIGHT_GRAY);
      state = NODE_CREATE;
    }
    //Edge button action performed
    if (e.getSource().equals(edgeB)) {
      edgeB.setBackground(Color.GREEN);
      nodeB.setBackground(Color.LIGHT_GRAY);
      state = EDGE_FIRST;
      panel.stopHighlighting();
       frame.repaint();
    }
    if (e.getSource().equals(connectedB)) {
      if (panel.nodeExists(firstNode.getText()) == false) {
        JOptionPane.showMessageDialog(frame, "First node is not in your graph.");
      }
      else if (panel.nodeExists(secondNode.getText()) == false) {
        JOptionPane.showMessageDialog(frame, "Second node is not in your graph.");
      }
      else {
        Queue queue = new Queue();
        ArrayList<String> connectedList = new ArrayList<String>();
        connectedList.add(panel.getNode(firstNode.getText()).getLabel());
        ArrayList<String> edges = panel.getConnectedLabels(firstNode.getText());
        for (int a = 0; a < edges.size(); a++) {
          queue.enqueue(edges.get(a));
        }
        while (queue.isEmpty() == false) {
          String currentNode = queue.dequeue();
          if (connectedList.contains(currentNode) == false) {
              connectedList.add(currentNode);
          }
          edges = panel.getConnectedLabels(currentNode);
          for (int a = 0; a < edges.size(); a++) {
            if (connectedList.contains(edges.get(a)) == false) {
              queue.enqueue(edges.get(a));
            }
          }
        }
        if (connectedList.contains(secondNode.getText())) {
          JOptionPane.showMessageDialog(frame, "Connected!");
        }
        else {
          JOptionPane.showMessageDialog(frame, "Not Connected.");
        }
      }
    }
    //Traveling Salesman
    if (e.getSource().equals(salesmanB)) {
      if (panel.getNode(salesmanStartTF.getText()) != null) {
        ArrayList<Node> path = new ArrayList<Node>();
        path.add(panel.getNode(salesmanStartTF.getText()));
        travelling(panel.getNode(salesmanStartTF.getText()), new ArrayList<Node>(), 0);
        if (completed.size() == 0){
	        JOptionPane.showMessageDialog(frame, "No Valid Path Found.");
	      }
        else {
	        int lowCost = Integer.MAX_VALUE;
	        int index = -1;
	        for(int i = 0; i < completed.size(); i++){
	          ArrayList<Node> currentPath = completed.get(i);
	          int cost = Integer.parseInt(currentPath.get(currentPath.size() - 1).getLabel());
	          if(cost < lowCost){
	            lowCost = cost;
	            index = i;
	          }
	        }
	        String result = "";
	        for (int i = 0; i < completed.get(index).size() - 1; i++) {
	            Node n = completed.get(index).get(i);
	            result += "--> " + n.getLabel();
	          }
	        result += "\nCost: " + completed.get(index).get(completed.get(index).size() - 1).getLabel();
	        JOptionPane.showMessageDialog(panel, result);
	      }
      }
      else {
        JOptionPane.showMessageDialog(frame, "Not a valid starting node!");
      }
    }

  }

  //Traveling salesman 
  public void travelling(Node n, ArrayList<Node> path, int total) {
    if (path.size() == panel.nodeList.size()) {
      ArrayList<Node> alternate = new ArrayList<Node>();
	    for (Node node: path) {
	      alternate.add(node);
	    }
      alternate.add(new Node(0, 0, total + ""));
      completed.add(alternate);
      path.remove(path.size() - 1);
      return;
    }
    else {
      for (int a = 0; a < panel.edgeList.size(); a++) {
        Edge e = panel.edgeList.get(a);
        if (e.getOtherEnd(n) != null) {
          if (path.contains(e.getOtherEnd(n)) == false) {
            path.add(e.getOtherEnd(n));
            travelling(e.getOtherEnd(n), path, total + Integer.parseInt(e.getLabel()));
          }
        }
      }
    }
  }
}