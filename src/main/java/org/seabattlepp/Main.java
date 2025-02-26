package org.seabattlepp;
import org.seabattlepp.gui.MainFrame;
import javax.swing.SwingUtilities;
import org.seabattlepp.gui.MainFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}