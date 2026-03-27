package juliaset;

import javax.swing.*;

/**
 * Wizualizacja i Animacja Zbiorów Julii
 * Projekt Mini I - Algorytmy i Struktury Danych
 * Uniwersytet w Białymstoku
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JuliaSetFrame frame = new JuliaSetFrame();
            frame.setVisible(true);
        });
    }
}
