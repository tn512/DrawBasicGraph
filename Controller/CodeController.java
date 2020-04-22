/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Edge;
import Model.Vertice;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Admin
 */
public class CodeController {

    private final String label = "GREEN|RED|YELLOW|ORANGE|PINK|BLUE|MAGENTA|WHITE|BLACK|GRAY";

    private final String comment = "(//[A-Za-z]*(\\s*[a-zA-Z]*\\s*)*(\\n)*)*";

    private final String ver = "(([A-Za-z])\\s*\\[label\\s*=\\s*\"([a-zA-Z]*)\",\\s*color =\\s*\"(\\w+)\"\\](\\n*))";

    private final String edge = "(([A-Za-z])->([A-Za-z])\\s*\\[label\\s*=\\s*\"(\\d+)\"\\](\\n*))";
    
    private final String pattern = "(\\w+)\\{(\\n)*"
            + comment
            + ver
            + ver
            + ver
            + "(\\n)*"
            + comment
            + edge
            + edge
            + edge
            + "\\}";

    Map<String, Color> mapColor = new HashMap<String, Color>() {
        {
            put("black", Color.black);
            put("white", Color.white);
            put("green", Color.green);
            put("red", Color.red);
            put("blue", Color.blue);
            put("yellow", Color.yellow);
            put("pink", Color.pink);
            put("orange", Color.orange);
            put("magenta", Color.magenta);
            put("gray", Color.gray);
            put("cyan", Color.cyan);
        }
    };

    MyCanvas cvaGraph;
    String nameGraph;

    public boolean checkCode(JTextArea txaCode) {
        Pattern p = Pattern.compile(pattern);
        //Check the code in textarea if match the regex or not
        Matcher m = p.matcher(txaCode.getText());
        if (m.find()) {
            ArrayList<String> arVersName = new ArrayList<String>();
            arVersName.add(m.group(7));
            arVersName.add(m.group(12));
            arVersName.add(m.group(17));
            //Create edges with info from the regex
            ArrayList<Edge> arEdges = new ArrayList<Edge>();
            Edge e1 = new Edge(m.group(26), m.group(27), m.group(28));
            Edge e2 = new Edge(m.group(31), m.group(32), m.group(33));
            Edge e3 = new Edge(m.group(36), m.group(37), m.group(38));
            arEdges.add(e1);
            arEdges.add(e2);
            arEdges.add(e3);
            //Check names of three vertice is not similar
            if (!m.group(7).equals(m.group(12)) && !m.group(7).equals(m.group(17))
                    && !m.group(17).equals(m.group(12))) {
                //Check two vertices of each edges if is one of initialized vertices above
                if (arVersName.contains(e1.getVer1()) && arVersName.contains(e2.getVer1())
                        && arVersName.contains(e3.getVer1())
                        && arVersName.contains(e1.getVer2()) && arVersName.contains(e2.getVer2())
                        && arVersName.contains(e3.getVer2())
                        //Check two vertices of each edges is not similar
                        && !m.group(26).equals(m.group(27)) && !m.group(31).equals(m.group(32))
                        && !m.group(36).equals(m.group(37))) {
                    //Check each vertice does not appear more than two times
                    ArrayList<String> check = new ArrayList<String>();
                    check.add(m.group(26));
                    check.add(m.group(27));
                    check.add(m.group(31));
                    check.add(m.group(32));
                    check.add(m.group(36));
                    check.add(m.group(37));
                    int count1 = Collections.frequency(check, m.group(7));
                    int count2 = Collections.frequency(check, m.group(12));
                    int count3 = Collections.frequency(check, m.group(17));
                    if (count1 == 2 && count2 == 2 && count3 == 2) {
                        return true;
                    } else {
                        System.out.println("0");
                        return false;
                    }
                } else {
                    System.out.println("1");
                    return false;
                }
            } else {
                System.out.println("2");
                return false;
            }
        } else {
            System.out.println("NO MATCH");
            return false;
        }
    }

    public MyCanvas drawGraph(JTextArea txaCode, JPanel pnName, JPanel pnGraph) {
        pnGraph.removeAll();
        cvaGraph = new MyCanvas();
        //Check the code in textarea if match the regex and all conditions
        if (checkCode(txaCode)) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(txaCode.getText());
            if (m.find()) {
                nameGraph = m.group(1);
                //Create edges and vertices with info from the regex
                ArrayList<Vertice> arVers = new ArrayList<>();
                ArrayList<Edge> arEdges = new ArrayList<Edge>();
                Vertice v0 = new Vertice(m.group(8), mapColor.get(m.group(9)), m.group(7));
                Vertice v1 = new Vertice(m.group(13), mapColor.get(m.group(14)), m.group(12));
                Vertice v2 = new Vertice(m.group(18), mapColor.get(m.group(19)), m.group(17));
                arVers.add(v0);
                arVers.add(v1);
                arVers.add(v2);
                Edge e1 = new Edge(m.group(26), m.group(27), m.group(28));
                Edge e2 = new Edge(m.group(31), m.group(32), m.group(33));
                Edge e3 = new Edge(m.group(36), m.group(37), m.group(38));
                arEdges.add(e1);
                arEdges.add(e2);
                arEdges.add(e3);
                cvaGraph.drawNode(v0, pnGraph.getWidth() / 3, pnGraph.getHeight() / 3);
                cvaGraph.drawNode(v1, pnGraph.getWidth() * 2 / 3, pnGraph.getHeight() / 3);
                cvaGraph.drawNode(v2, pnGraph.getWidth() / 3, pnGraph.getHeight() * 2 / 3);
                //Get the direction of arrows
                for (Edge e : arEdges) {
                    int ver1 = 0, ver2 = 0;
                    for (Vertice v : arVers) {
                        if (e.getVer1().equals(v.getName())) {
                            ver1 = arVers.indexOf(v);
                        }
                        if (e.getVer2().equals(v.getName())) {
                            ver2 = arVers.indexOf(v);
                        }
                    }
                    cvaGraph.drawEdge(e, ver1, ver2);
                }
                pnGraph.add(cvaGraph);
                cvaGraph.revalidate();
                cvaGraph.repaint();
                pnName.setBorder(new TitledBorder(m.group(1)));
                return cvaGraph;
            } else {
                System.out.println("NO MATCH");
            }
        } else {
            pnGraph.remove(cvaGraph);
        }
        return null;
    }

    public void saveMyCanvas(JTextArea txaCode, JPanel pnName, JPanel pnGraph) {
        Canvas canvas = cvaGraph;
        BufferedImage image = new BufferedImage(canvas.getWidth(),
                canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        canvas.paint(g2);
        File idc = new File("C:\\temp\\" + nameGraph + "Graph.png");
        if (!idc.exists()) {
            try {
                ImageIO.write(image, "png", new File("C:\\temp\\" + nameGraph + "Graph.png"));
            } catch (Exception e) {
            }
        } else {
            int number = 1;
            File idk;
            while (true) {
                idk = new File("C:\\temp\\" + nameGraph + "Graph" + "(" + number + ")" + ".png");
                if (!idk.exists()) {
                    break;
                }
                number = number + 1;
            }
            try {
                ImageIO.write(image, "png", idk);
            } catch (Exception e) {
            }
        }

    }

}
