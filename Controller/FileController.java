/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import GUI.GraphGUI;
import java.awt.FileDialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;


/**
 *
 * @author Admin
 */
public class FileController {

    String checkSaveContent;
    File currentFile;
    GraphGUI frame;
    JTextArea textArea;
    FileDialog fileDialog;
    JFileChooser fileChooser;

    public FileController(GraphGUI frame) {
        this.frame = frame;
        textArea = frame.getTextArea();
        setFrame();
        setFileDialog();
        setFileChooser();
        checkSaveContent = "";
        currentFile = null;
    }

    void setFrame() {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                boolean doExit = true;
                //If user has not save or have change content from last save, ask to save
                if (!checkSaveContent.equals(textArea.getText())) {
                    doExit = showAskSaveDialog();
                }
                //If user choose yes and save or no, exit program
                if (doExit) {
                    System.exit(0);
                }
            }
        });
    }

    void setFileChooser() {
        fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                return f.getName().toLowerCase().endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "Text Documents (*.txt)";
            }
        });
        fileChooser.setCurrentDirectory(new File("Desktop"));
    }

    void setFileDialog() {
        fileDialog = new FileDialog(frame);
        fileDialog.setDirectory("Desktop");
    }

    boolean showAskSaveDialog() {
        int reply = JOptionPane.showConfirmDialog(frame, "Do you want to save change to this text document?");
        //If user choose yes, do save
        if (reply == JOptionPane.YES_OPTION) {
            //Continue before operation if user choose save, cancel before
            //Operation if user choose cancel or close save dialog
            return itemSave();
            //If user choose no, do not save and continue before operation
        } else if (reply == JOptionPane.NO_OPTION) {
            return true;
        } //If user choose cancel or close dialog, cancel before operation
        else {
            return false;
        }
    }

    public void itemNew() {
        boolean doNewOperation = true;
        //If user has not save or have change content from last save, ask to save
        if (!checkSaveContent.equals(textArea.getText())) {
            doNewOperation = showAskSaveDialog();
        }
        //If user not choose cancel, continue do new operation
        if (doNewOperation) {
            checkSaveContent = "";
            currentFile = null;
            textArea.setText("");
        }
    }

    public void itemOpen() {
        boolean doOpen = true;
        if (!checkSaveContent.equals(textArea.getText())) {
            doOpen = showAskSaveDialog();
        }
        if (doOpen) {
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile();
                try {
                    //Get input stream of file
                    FileInputStream fins = new FileInputStream(currentFile);
                    DataInputStream din = new DataInputStream(fins);
                    //Create byte array to contain data of file
                    byte data[] = new byte[fins.available()];
                    StringBuilder allData = new StringBuilder();
                    //Read data in file to array
                    din.read(data);
                    //For each character in data array, append it to string builder
                    for (byte character : data) {
                        allData.append((char) character);
                    }
                    //Set text in text area to string builder
                    textArea.setText(allData.toString());
                    din.close();
                    checkSaveContent = textArea.getText();

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Cannot open file!");
                    ex.printStackTrace();
                }
            }
        }
    }

    public boolean itemSave() {
        //If there is no file to save, do save as
        if (currentFile == null) {
            return itemSaveNewFile();
        } else {
            try {
                FileWriter fout = new FileWriter(currentFile);
                //Write text in text area to file
                fout.write(textArea.getText());
                checkSaveContent = textArea.getText();
                fout.close();
                return true;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Cannot save file!");
                ex.printStackTrace();
            }
            return false;
        }
    }

    public boolean itemSaveNewFile() {
        int choice;
        File checkExistFile;
        //Break when file name not existed or existed and user want to replace
        while (true) {
            choice = fileChooser.showSaveDialog(frame);
            //if user not save
            if (choice != JFileChooser.APPROVE_OPTION) {
                return false;
            }
            checkExistFile = fileChooser.getSelectedFile();
            if (checkExistFile.exists()) {
                int confirm = JOptionPane.showConfirmDialog(frame
                        , "This file has existed, do you want to replace it?", "Replace",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    break;
                }
            } else {
                break;
            }
        }
        currentFile = fileChooser.getSelectedFile();
        if (!currentFile.getName().contains(".")) {
            String dir = currentFile.getParent();
            String name = currentFile.getName();
            name += ".txt";
            currentFile = new File(dir + File.separator + name);
        }
        try {
            FileWriter fout = new FileWriter(currentFile);
            //Write content in text area to file
            fout.write(textArea.getText());
            checkSaveContent = textArea.getText();
            fout.close();
            //Save successful
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Cannot save file!");
            ex.printStackTrace();
        }
        return false;
    }

    public void btnClose() {
        boolean doExit = true;
        //If user has not save or have change content from last save, ask to save
        if (!checkSaveContent.equals(textArea.getText())) {
            doExit = showAskSaveDialog();
        }
        if (doExit) {
            System.exit(0);
        }
    }
}
