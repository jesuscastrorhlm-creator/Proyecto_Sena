package Tienda;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class PanelRedondeado extends JPanel {

    private Color colorFondo;
    private int radio;

    public PanelRedondeado() {
        colorFondo = new Color(255, 255, 255, 120);
        radio = 30;

        setOpaque(false);
    }

    public PanelRedondeado(Color colorFondo, int radio) {
        this.colorFondo = colorFondo;
        this.radio = radio;

        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(colorFondo);

        g2.fillRoundRect(
            0,
            0,
            getWidth() - 1,
            getHeight() - 1,
            radio,
            radio
        );

        g2.dispose();

        super.paintComponent(g);
    }
}