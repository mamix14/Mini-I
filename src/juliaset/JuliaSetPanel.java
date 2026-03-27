package juliaset;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Panel rysujący zbiór Julii.
 *
 * Renderowanie odbywa się w osobnym wątku (nie blokuje UI).
 * Jeśli nadejdzie nowe żądanie renderowania zanim poprzednie się skończy,
 * stare jest anulowane i startuje nowe.
 */
public class JuliaSetPanel extends JPanel {

    private static final int WIDTH  = 700;
    private static final int HEIGHT = 700;

    private volatile double cReal = -0.7;
    private volatile double cImag =  0.27;
    private volatile int    maxIterations = 100;

    private volatile BufferedImage image;

    // Wątek renderujący - tylko jeden na raz
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Future<?> currentRender = null;

    public JuliaSetPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        renderAsync();
    }

    /** Ustawia parametr c i zleca nowe renderowanie. */
    public void setC(double real, double imag) {
        this.cReal = real;
        this.cImag = imag;
        renderAsync();
    }

    /** Ustawia maksymalną liczbę iteracji i zleca nowe renderowanie. */
    public void setMaxIterations(int iterations) {
        this.maxIterations = iterations;
        renderAsync();
    }

    public double getCReal() { return cReal; }
    public double getCImag() { return cImag; }

    // -------------------------------------------------------------------------
    // Renderowanie w tle
    // -------------------------------------------------------------------------

    private synchronized void renderAsync() {
        // Anuluj poprzednie renderowanie jeśli jeszcze trwa
        if (currentRender != null && !currentRender.isDone()) {
            currentRender.cancel(true);
        }
        currentRender = executor.submit(this::renderTask);
    }

    private void renderTask() {
        // Snapshot parametrów (mogą się zmienić w trakcie)
        double cr = cReal;
        double ci = cImag;
        int    mi = maxIterations;

        BufferedImage buf = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        for (int px = 0; px < WIDTH; px++) {
            if (Thread.currentThread().isInterrupted()) return; // anulowane
            for (int py = 0; py < HEIGHT; py++) {
                double zReal = mapX(px);
                double zImag = mapY(py);
                int iter = iterate(zReal, zImag, cr, ci, mi);
                buf.setRGB(px, py, colorize(iter, mi));
            }
        }

        image = buf;
        SwingUtilities.invokeLater(this::repaint);
    }

    // -------------------------------------------------------------------------
    // Algorytm Julia Set
    // -------------------------------------------------------------------------

    /** Mapuje piksel X na Re ∈ [-1.5, 1.5]. */
    private double mapX(int px) {
        return -1.5 + 3.0 * px / WIDTH;
    }

    /** Mapuje piksel Y na Im ∈ [-1.5, 1.5]. */
    private double mapY(int py) {
        return -1.5 + 3.0 * py / HEIGHT;
    }

    /**
     * Iteruje z = z² + c aż do ucieczki lub maxIter.
     * @return liczba kroków do ucieczki (maxIter = należy do zbioru)
     */
    private int iterate(double zReal, double zImag, double cr, double ci, int maxIter) {
        for (int i = 0; i < maxIter; i++) {
            double zr2 = zReal * zReal - zImag * zImag + cr;
            double zi2 = 2.0 * zReal * zImag + ci;
            zReal = zr2;
            zImag = zi2;
            if (escaped(zReal, zImag)) return i;
        }
        return maxIter;
    }

    /** Warunek ucieczki: |z|² > 4. */
    private boolean escaped(double zReal, double zImag) {
        return zReal * zReal + zImag * zImag > 4.0;
    }

    /**
     * Koloruje piksel na podstawie liczby iteracji.
     * Piksele należące do zbioru są czarne.
     */
    private int colorize(int iter, int maxIter) {
        if (iter == maxIter) return 0x000000;
        double t = (double) iter / maxIter;
        int r = clamp((int)(9.0  * (1 - t) * t * t * t * 255));
        int g = clamp((int)(15.0 * (1 - t) * (1 - t) * t * t * 255));
        int b = clamp((int)(8.5  * (1 - t) * (1 - t) * (1 - t) * t * 255));
        return (r << 16) | (g << 8) | b;
    }

    /** Ogranicza wartość do [0, 255]. */
    private int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }

    // -------------------------------------------------------------------------

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }
    }
}
