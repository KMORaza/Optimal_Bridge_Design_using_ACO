import javax.swing.*;
import java.awt.*;
import java.util.List;
public class BridgeVisualizer extends JPanel {
    private BridgeDesign design;
    private static final int WIDTH = 1000; 
    private static final int HEIGHT = 500; 
    private static final int SUPPORT_HEIGHT = 50;
    private static final int MARGIN = 50;
    public BridgeVisualizer(BridgeDesign design) {
        this.design = design;
        JFrame frame = new JFrame("Optimized Bridge Design Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double scaleX = (WIDTH - 2 * MARGIN) / design.getSpanLength();
        double maxHeight = design.getComponents().stream().mapToDouble(BridgeComponent::getHeight).max().orElse(5.0);
        double scaleY = (HEIGHT - 2 * MARGIN - SUPPORT_HEIGHT) / maxHeight;
        int baseY = HEIGHT - MARGIN - SUPPORT_HEIGHT;
        g2d.setColor(Color.BLACK);
        g2d.fillRect(MARGIN - 10, baseY, 20, SUPPORT_HEIGHT);
        g2d.fillRect((int) (MARGIN + design.getSpanLength() * scaleX) - 10, baseY, 20, SUPPORT_HEIGHT);
        List<BridgeComponent> components = design.getComponents();
        double x = MARGIN;
        double[] topYs = new double[components.size() / 2 + 1];
        topYs[0] = baseY;
        for (int i = 0; i < components.size(); i++) {
            BridgeComponent comp = components.get(i);
            double thickness = comp.getCrossSectionArea() / 10; 
            g2d.setStroke(new BasicStroke((float) thickness));
            switch (comp.getMaterial()) {
                case STEEL: g2d.setColor(Color.BLUE); break;
                case ALUMINUM: g2d.setColor(Color.GRAY); break;
                case CONCRETE: g2d.setColor(Color.DARK_GRAY); break;
            }
            double nextX = x + comp.getLength() * scaleX;
            int idx = i / 2 + 1;
            if (comp.isCritical()) {
                g2d.drawLine((int) x, baseY, (int) nextX, baseY);
                drawLabel(g2d, (int) (x + nextX) / 2, baseY + 10, comp);
            } else {
                double topY = baseY - comp.getHeight() * scaleY;
                g2d.drawLine((int) x, baseY, (int) nextX, (int) topY);
                topYs[idx] = topY;
                if (i > 1) {
                    g2d.drawLine((int) x, (int) topYs[idx - 1], (int) nextX, (int) topY); // Top chord
                }
                drawLabel(g2d, (int) (x + nextX) / 2, (int) (baseY + topY) / 2, comp);
            }
            x = nextX;
        }
        drawLegend(g2d, MARGIN, MARGIN);
    }
    private void drawLabel(Graphics2D g2d, int x, int y, BridgeComponent comp) {
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        String label = String.format("%s, %.0f cm²", comp.getMaterial(), comp.getCrossSectionArea());
        g2d.drawString(label, x - 20, y + (comp.isCritical() ? 15 : -5));
    }
    private void drawLegend(Graphics2D g2d, int x, int y) {
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.setColor(Color.BLACK);
        g2d.drawString("Legend:", x, y);
        g2d.setStroke(new BasicStroke(2));
        y += 20;
        g2d.setColor(Color.BLUE);
        g2d.drawLine(x, y, x + 20, y);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Steel", x + 25, y + 5);
        y += 20;
        g2d.setColor(Color.GRAY);
        g2d.drawLine(x, y, x + 20, y);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Aluminum", x + 25, y + 5);
        y += 20;
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawLine(x, y, x + 20, y);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Concrete", x + 25, y + 5);
    }
}