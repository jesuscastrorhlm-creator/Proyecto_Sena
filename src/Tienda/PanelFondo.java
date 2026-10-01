package Tienda;

import javax.swing.*;
import java.awt.*;

public class PanelFondo extends JPanel {

    private Image fondo;

    public PanelFondo(String rutaImagen) {
        fondo = new ImageIcon(getClass().getResource(rutaImagen)).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int anchoPanel = getWidth();
        int altoPanel = getHeight();
        int anchoImagen = fondo.getWidth(this);
        int altoImagen = fondo.getHeight(this);

        if (anchoImagen <= 0 || altoImagen <= 0) return;

        double escala = Math.max(
            (double) anchoPanel / anchoImagen,
            (double) altoPanel / altoImagen
        );

        int nuevoAncho = (int) (anchoImagen * escala);
        int nuevoAlto = (int) (altoImagen * escala);

        int x = (anchoPanel - nuevoAncho) / 2;
        int y = (altoPanel - nuevoAlto) / 2;

        g.drawImage(fondo, x, y, nuevoAncho, nuevoAlto, this);
    }
}
