package juliaset;

import javax.swing.*;
import java.awt.*;

/**
 * Panel wizualizujący krzywą parametryczną - okrąg po którym porusza się c.
 *
 * Równania parametryczne:
 *   x(t) = R · cos(t)
 *   y(t) = R · sin(t)
 *   t ∈ [0, 2π]
 *
 * Liczba punktów krzywej jest regulowana przez użytkownika.
 */
public class ParametricCurvePanel extends JPanel {

    private static final int SIZE   = 200;
    private static final double R   = 0.7885; // promień okręgu

    private int    numPoints = 60;   // liczba punktów krzywej
    private double currentAngle = 0; // aktualna pozycja c na okręgu

    public ParametricCurvePanel() {
        setPreferredSize(new Dimension(SIZE, SIZE));
        setBackground(new Color(20, 20, 40));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Krzywa parametryczna c(t)",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.PLAIN, 10),
                Color.LIGHT_GRAY));
    }

    /** Ustawia liczbę punktów krzywej. */
    public void setNumPoints(int n) {
        this.numPoints = n;
        repaint();
    }

    /** Aktualizuje aktualny kąt (pozycję c na okręgu). */
    public void setCurrentAngle(double angle) {
        this.currentAngle = angle;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = SIZE / 2;
        int cy = SIZE / 2;
        int scale = 80; // piksele na jednostkę

        // Osie
        g2.setColor(new Color(80, 80, 100));
        g2.drawLine(10, cy, SIZE - 10, cy); // oś X (Re)
        g2.drawLine(cx, 10, cx, SIZE - 10); // oś Y (Im)

        // Etykiety osi
        g2.setColor(Color.GRAY);
        g2.setFont(new Font("Arial", Font.PLAIN, 9));
        g2.drawString("Re", SIZE - 18, cy - 3);
        g2.drawString("Im", cx + 3, 18);

        // Rysowanie krzywej jako połączone punkty
        int[] xPoints = new int[numPoints + 1];
        int[] yPoints = new int[numPoints + 1];
        for (int i = 0; i <= numPoints; i++) {
            double t = 2 * Math.PI * i / numPoints;
            xPoints[i] = cx + (int)(scale * R * Math.cos(t));
            yPoints[i] = cy - (int)(scale * R * Math.sin(t));
        }
        // Linia krzywej
        g2.setColor(new Color(100, 180, 255));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawPolyline(xPoints, yPoints, numPoints + 1);

        // Punkty na krzywej (tylko gdy niezbyt gęste)
        if (numPoints <= 60) {
            g2.setColor(new Color(60, 120, 200));
            for (int i = 0; i < numPoints; i++) {
                g2.fillOval(xPoints[i] - 2, yPoints[i] - 2, 4, 4);
            }
        }

        // Aktualna pozycja c - czerwona kropka
        int cx2 = cx + (int)(scale * R * Math.cos(currentAngle));
        int cy2 = cy - (int)(scale * R * Math.sin(currentAngle));
        g2.setColor(Color.RED);
        g2.fillOval(cx2 - 5, cy2 - 5, 10, 10);

        // Etykieta c
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 9));
        g2.drawString("c", cx2 + 6, cy2 - 3);
    }
}
