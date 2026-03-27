package juliaset;

import javax.swing.Timer;

/**
 * Kontroler animacji - płynnie zmienia parametr c
 * po okręgu na płaszczyźnie zespolonej.
 *
 * Krzywa parametryczna: x(t) = R·cos(t),  y(t) = R·sin(t)
 */
public class AnimationController {

    private static final double RADIUS      = 0.7885;
    private static final double DEFAULT_C_RE = -0.7;
    private static final double DEFAULT_C_IM =  0.27;
    private static final double STEP        = 0.02;

    private final JuliaSetPanel       juliaPanel;
    private final ParametricCurvePanel curvePanel;

    private Timer  timer;
    private double angle   = 0.0;
    private boolean running = false;

    public AnimationController(JuliaSetPanel juliaPanel, ParametricCurvePanel curvePanel) {
        this.juliaPanel = juliaPanel;
        this.curvePanel = curvePanel;
    }

    /** Uruchamia animację (~20 FPS). */
    public void start() {
        if (running) return;
        running = true;
        timer = new Timer(50, e -> step());
        timer.start();
    }

    /** Zatrzymuje animację. */
    public void stop() {
        if (timer != null) timer.stop();
        running = false;
    }

    /** Resetuje do wartości domyślnych. */
    public void reset() {
        stop();
        angle = 0.0;
        juliaPanel.setC(DEFAULT_C_RE, DEFAULT_C_IM);
        curvePanel.setCurrentAngle(0);
    }

    public boolean isRunning() { return running; }

    public double getAngle() { return angle; }

    /** Jeden krok animacji. */
    private void step() {
        angle += STEP;
        if (angle > 2 * Math.PI) angle -= 2 * Math.PI;

        double newReal = RADIUS * Math.cos(angle);
        double newImag = RADIUS * Math.sin(angle);
        juliaPanel.setC(newReal, newImag);
        curvePanel.setCurrentAngle(angle);
    }
}
