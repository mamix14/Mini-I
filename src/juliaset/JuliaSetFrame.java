package juliaset;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * Główne okno aplikacji z panelem sterowania.
 */
public class JuliaSetFrame extends JFrame {

    private final JuliaSetPanel        juliaPanel;
    private final ParametricCurvePanel curvePanel;
    private final AnimationController  animation;

    private JSlider iterSlider;
    private JLabel  iterLabel;
    private JSlider pointsSlider;
    private JLabel  pointsLabel;
    private JTextField cRealField;
    private JTextField cImagField;
    private JButton animBtn;
    private JLabel  cValueLabel;

    private final DecimalFormat df = new DecimalFormat("0.0000");

    public JuliaSetFrame() {
        super("Zbiory Julii - Wizualizacja i Animacja | UWB");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        juliaPanel = new JuliaSetPanel();
        curvePanel = new ParametricCurvePanel();
        animation  = new AnimationController(juliaPanel, curvePanel);

        add(juliaPanel, BorderLayout.CENTER);
        add(buildControls(), BorderLayout.EAST);

        // Timer odświeżający etykietę c podczas animacji
        new javax.swing.Timer(100, e -> {
            if (animation.isRunning()) {
                refreshCLabel();
                curvePanel.setCurrentAngle(animation.getAngle());
            }
        }).start();

        pack();
        setLocationRelativeTo(null);
    }

    // -------------------------------------------------------------------------
    // Budowanie panelu sterowania
    // -------------------------------------------------------------------------

    private JPanel buildControls() {
        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        controls.setPreferredSize(new Dimension(230, 800));

        addTitle(controls);
        addCurvePanel(controls);
        addCurvePointsSlider(controls);
        addCValueDisplay(controls);
        addManualCControls(controls);
        addIterationsSlider(controls);
        addAnimationButtons(controls);
        addFooter(controls);

        return controls;
    }

    private void addTitle(JPanel p) {
        JLabel title = new JLabel("<html><b>Zbiory Julii (Julia Set)</b></html>");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setAlignmentX(CENTER_ALIGNMENT);
        p.add(title);
        p.add(Box.createVerticalStrut(4));
        JLabel sub = new JLabel("Fraktal zespolony: z = z² + c");
        sub.setFont(new Font("Monospaced", Font.PLAIN, 11));
        sub.setAlignmentX(CENTER_ALIGNMENT);
        p.add(sub);
        p.add(Box.createVerticalStrut(10));
    }

    private void addCurvePanel(JPanel p) {
        curvePanel.setAlignmentX(CENTER_ALIGNMENT);
        p.add(curvePanel);
        p.add(Box.createVerticalStrut(5));
    }

    private void addCurvePointsSlider(JPanel p) {
        pointsLabel = new JLabel("Punkty krzywej: 60");
        pointsLabel.setAlignmentX(CENTER_ALIGNMENT);
        pointsLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        p.add(new JLabel("Dokładność krzywej parametrycznej:"));
        pointsSlider = new JSlider(4, 200, 60);
        pointsSlider.setMaximumSize(new Dimension(210, 35));
        pointsSlider.addChangeListener((ChangeEvent e) -> {
            int n = pointsSlider.getValue();
            curvePanel.setNumPoints(n);
            pointsLabel.setText("Punkty krzywej: " + n);
        });
        p.add(pointsSlider);
        p.add(pointsLabel);
        p.add(Box.createVerticalStrut(10));
    }

    private void addCValueDisplay(JPanel p) {
        cValueLabel = new JLabel(formatC());
        cValueLabel.setAlignmentX(CENTER_ALIGNMENT);
        cValueLabel.setFont(new Font("Monospaced", Font.BOLD, 11));
        p.add(cValueLabel);
        p.add(Box.createVerticalStrut(8));
    }

    private void addManualCControls(JPanel p) {
        p.add(new JLabel("Re(c):"));
        cRealField = new JTextField(df.format(juliaPanel.getCReal()));
        cRealField.setMaximumSize(new Dimension(210, 28));
        p.add(cRealField);
        p.add(Box.createVerticalStrut(3));

        p.add(new JLabel("Im(c):"));
        cImagField = new JTextField(df.format(juliaPanel.getCImag()));
        cImagField.setMaximumSize(new Dimension(210, 28));
        p.add(cImagField);
        p.add(Box.createVerticalStrut(5));

        JButton applyBtn = new JButton("Zastosuj c");
        applyBtn.setAlignmentX(CENTER_ALIGNMENT);
        applyBtn.addActionListener(e -> applyManualC());
        p.add(applyBtn);
        p.add(Box.createVerticalStrut(10));
    }

    private void addIterationsSlider(JPanel p) {
        p.add(new JLabel("Maks. iteracje:"));
        iterLabel = new JLabel("Iteracje: 100");
        iterSlider = new JSlider(10, 500, 100);
        iterSlider.setMaximumSize(new Dimension(210, 35));
        iterSlider.addChangeListener((ChangeEvent e) -> {
            juliaPanel.setMaxIterations(iterSlider.getValue());
            iterLabel.setText("Iteracje: " + iterSlider.getValue());
        });
        p.add(iterSlider);
        iterLabel.setAlignmentX(CENTER_ALIGNMENT);
        p.add(iterLabel);
        p.add(Box.createVerticalStrut(10));
    }

    private void addAnimationButtons(JPanel p) {
        // Start / Stop
        animBtn = new JButton("▶ Start animacji");
        animBtn.setAlignmentX(CENTER_ALIGNMENT);
        animBtn.setMaximumSize(new Dimension(210, 32));
        animBtn.addActionListener(e -> toggleAnimation());
        p.add(animBtn);
        p.add(Box.createVerticalStrut(5));

        // Reset
        JButton resetBtn = new JButton("↺ Reset");
        resetBtn.setAlignmentX(CENTER_ALIGNMENT);
        resetBtn.setMaximumSize(new Dimension(210, 32));
        resetBtn.addActionListener(e -> resetAnimation());
        p.add(resetBtn);
        p.add(Box.createVerticalStrut(6));

        JLabel animDesc = new JLabel(
                "<html><small>Animacja: c(t) = R·cos(t) + R·sin(t)·i<br>R = 0.7885</small></html>");
        animDesc.setAlignmentX(CENTER_ALIGNMENT);
        p.add(animDesc);
        p.add(Box.createVerticalGlue());
    }

    private void addFooter(JPanel p) {
        // Logo UWB
        try {
            java.net.URL imgUrl = getClass().getClassLoader().getResource("uwb_logo.png");
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image scaled = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                JLabel logoLabel = new JLabel(new ImageIcon(scaled));
                logoLabel.setAlignmentX(CENTER_ALIGNMENT);
                p.add(logoLabel);
                p.add(Box.createVerticalStrut(4));
            }
        } catch (Exception ignored) {}

        JLabel footer = new JLabel(
                "<html><center><small>Uniwersytet w Białymstoku<br>Projekt Mini I</small></center></html>");
        footer.setAlignmentX(CENTER_ALIGNMENT);
        p.add(footer);
    }

    // -------------------------------------------------------------------------
    // Akcje
    // -------------------------------------------------------------------------

    private void toggleAnimation() {
        if (animation.isRunning()) {
            animation.stop();
            animBtn.setText("▶ Start animacji");
        } else {
            animation.start();
            animBtn.setText("⏸ Stop animacji");
        }
    }

    private void resetAnimation() {
        animation.reset();
        animBtn.setText("▶ Start animacji");
        iterSlider.setValue(100);
        juliaPanel.setMaxIterations(100);
        refreshCLabel();
    }

    private void applyManualC() {
        try {
            double re = Double.parseDouble(cRealField.getText().replace(",", "."));
            double im = Double.parseDouble(cImagField.getText().replace(",", "."));
            animation.stop();
            animBtn.setText("▶ Start animacji");
            juliaPanel.setC(re, im);
            refreshCLabel();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Podaj poprawne liczby dla Re i Im!", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshCLabel() {
        cValueLabel.setText(formatC());
        cRealField.setText(df.format(juliaPanel.getCReal()));
        cImagField.setText(df.format(juliaPanel.getCImag()));
    }

    private String formatC() {
        return "c = " + df.format(juliaPanel.getCReal())
                + " + " + df.format(juliaPanel.getCImag()) + "i";
    }
}
