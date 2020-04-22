/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Admin
 */
public class Edge {
    private String ver1, ver2;
    private String label;

    public Edge(String ver1, String ver2, String label) {
        this.ver1 = ver1;
        this.ver2 = ver2;
        this.label = label;
    }

    public Edge() {
    }

    public String getVer1() {
        return ver1;
    }

    public void setVer1(String ver1) {
        this.ver1 = ver1;
    }

    public String getVer2() {
        return ver2;
    }

    public void setVer2(String ver2) {
        this.ver2 = ver2;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
}
