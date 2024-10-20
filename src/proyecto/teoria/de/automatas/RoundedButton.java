package proyecto.teoria.de.automatas;

import javax.swing.*;
import java.awt.*;

class RoundedButton extends JButton {
    private Color backgroundColor;

    public RoundedButton(String text, Color bgColor) {
        super(text);
        this.backgroundColor = bgColor;
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    public void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
        g2.dispose();
    }

    @Override
    public boolean contains(int x, int y) {
        int width = getWidth();
        int height = getHeight();
        return (x >= 0 && x < width && y >= 0 && y < height);
    }
}