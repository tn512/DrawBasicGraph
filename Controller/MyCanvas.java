/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Edge;
import Model.Vertice;
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import static java.awt.image.ImageObserver.HEIGHT;
import static java.awt.image.ImageObserver.WIDTH;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MyCanvas extends Canvas {

    private final int ARR_SIZE = 6;
    int width;
    int height;
    ArrayList<Node> nodes;
    ArrayList<edge> edges;
    int check;

    public MyCanvas() {
        nodes = new ArrayList<Node>();
        edges = new ArrayList<edge>();
        setBackground(Color.white);
        setSize(300, 700);
        width = 50;
        height = 50;
        check = 0;
    }

    class Node {

        int x, y;
        Vertice v;

        public Node(Vertice myV, int myX, int myY) {
            x = myX;
            y = myY;
            v = myV;
        }
    }

    class edge {

        int i, j;
        Edge e;

        public edge(Edge e, int ii, int jj) {
            this.e = e;
            i = ii;
            j = jj;
        }
    }

    public void drawNode(Vertice v, int x, int y) {
        //Add a node at pixel (x,y)
        nodes.add(new Node(v, x, y));
        this.repaint();
    }

    public void drawEdge(Edge e, int i, int j) {
        //Add an edge between nodes i and j
        edges.add(new edge(e, i, j));
        this.repaint();
    }

    public void paint(Graphics g) {
        update(g);
    }

    @Override
    public void update(Graphics g) {    
        Graphics2D g2 = (Graphics2D) g;
        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, this.getWidth(), this.getHeight());
        FontMetrics f = g2.getFontMetrics();
        int nodeHeight = Math.max(height, f.getHeight());
        g2.setColor(Color.black);
        //Draw edges with arrow and label 
        for (edge e : edges) {
            int halfLenNode = 0;
            //Check the y coordinate of two vertices of edge is similar
            if ((nodes.get(e.i).y - nodes.get(e.j).y) == 0) {
                halfLenNode = Math.max(width, f.stringWidth(nodes.get(e.j).v.getLabel()) + width / 2) / 2;
                drawArrow(g2, nodes.get(e.i).x, nodes.get(e.i).y,
                        nodes.get(e.j).x, nodes.get(e.j).y, halfLenNode);
                //Draw number label
                g2.drawString(e.e.getLabel(), (nodes.get(e.i).x + nodes.get(e.j).x) / 2 - 5,
                        (nodes.get(e.i).y + nodes.get(e.j).y) / 2 - 8);
            } else {
                halfLenNode = Math.max(height, f.getHeight()) / 2;
                drawArrow(g2, nodes.get(e.i).x, nodes.get(e.i).y,
                        nodes.get(e.j).x, nodes.get(e.j).y, halfLenNode);
                //Draw number label
                g2.drawString(e.e.getLabel(), (nodes.get(e.i).x + nodes.get(e.j).x) / 2 + 10,
                        (nodes.get(e.i).y + nodes.get(e.j).y) / 2);
            }
        }
        //Draw the nodes with the label and color
        for (Node n : nodes) {
            int nodeWidth = Math.max(width, f.stringWidth(n.v.getLabel()) + width / 2);
            g2.setColor(Color.white);
            g2.fillOval(n.x - nodeWidth / 2, n.y - nodeHeight / 2,
                    nodeWidth, nodeHeight);
            g2.setColor(n.v.getColor());
            g2.drawOval(n.x - nodeWidth / 2, n.y - nodeHeight / 2,
                    nodeWidth, nodeHeight);
            g2.drawString(n.v.getLabel(), n.x - f.stringWidth(n.v.getLabel()) / 2,
                    n.y + f.getHeight() / 2);
            g2.setColor(Color.black);
        }

    }

    void drawArrow(Graphics g1, int x1, int y1, int x2, int y2, int halfLenNode) {
        Graphics2D g = (Graphics2D) g1.create();
        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy) - halfLenNode;
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[]{len, len - ARR_SIZE, len - ARR_SIZE, len},
                new int[]{0, -ARR_SIZE, ARR_SIZE, 0}, 4);
    }

}
