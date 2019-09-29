/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.LineNumberReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Arsalan Farrell
 */
public class NoteController {

    private NoteGUI view;

    private List<Integer> list = new ArrayList<>();

    public NoteController(NoteGUI view) {
        this.view = view;

        this.view.getReadBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                process();
            }
        });

        this.view.getSaveBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
    }

    private void save() {
        JFileChooser loadFile = view.getLoadFile();
        if (JFileChooser.APPROVE_OPTION == loadFile.showSaveDialog(view)) {
            BufferedWriter writer = null;
            try {
                String contents = view.getTxtPane().getText();
                if (contents != null && !contents.isEmpty()) {
                    writer = new BufferedWriter(new FileWriter(loadFile.getSelectedFile()));
                    writer.write(contents);
                }
                JOptionPane.showMessageDialog(null, "Data berhasil di-save");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(NoteGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Data gagal di-save");
            } catch (IOException ex) {
                Logger.getLogger(NoteGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Data gagal di-save");
            } finally {
                if (writer != null) {
                    try {
                        writer.flush();
                        writer.close();
                        view.getTxtPane().setText("");
                    } catch (IOException ex) {
                        Logger.getLogger(NoteGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    private void process() {
        JFileChooser loadFile = view.getLoadFile();
        StyledDocument doc = view.getTxtPane().getStyledDocument();
        if (JFileChooser.APPROVE_OPTION == loadFile.showOpenDialog(view)) {
            LineNumberReader reader = null;
            try {
                int characterCount = 0;
                int wordCount = 0;
                reader = new LineNumberReader(new FileReader(loadFile.getSelectedFile()));
                String data = null;
                doc.insertString(0, "", null);
                while ((data = reader.readLine()) != null) {
                    String[] wordList = data.split("\\s+");

                    characterCount += data.length();

                    wordCount += wordList.length;

                    doc.insertString(doc.getLength(), data + "\n", null);
                }
                JOptionPane.showMessageDialog(null, "Data berhasil dibaca "
                        + "\nJumlah Baris : " + reader.getLineNumber()
                        + "\nJumlah Kata : " + wordCount
                        + "\nJumlah Huruf : " + characterCount
                );
            } catch (FileNotFoundException ex) {
                Logger.getLogger(NoteGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Data gagal dibaca");
            } catch (IOException | BadLocationException ex) {
                Logger.getLogger(NoteGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Data gagal dibaca");
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ex) {
                        Logger.getLogger(NoteGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
}
