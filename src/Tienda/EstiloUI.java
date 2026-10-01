package Tienda;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.BorderFactory;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Utilidad centralizada para dar a todos los botones de la aplicación
 * el mismo look-and-feel: esquinas redondeadas, colores de la marca
 * Galapa Express y efecto "hover" al pasar el mouse.
 *
 * Se usa desde cualquier ventana llamando, por ejemplo:
 *
 *     EstiloUI.estilizarBoton(btnGuardar, EstiloUI.ROJO, EstiloUI.ROJO_HOVER);
 *
 * o directamente con los helpers de conveniencia:
 *
 *     EstiloUI.botonPrimario(btnGuardar);
 *     EstiloUI.botonSecundario(btnCancelar);
 *     EstiloUI.botonPeligro(btnEliminar);
 *     EstiloUI.botonAcento(btnHacerAdmin);
 *
 * @author juanc
 */
public final class EstiloUI {

    private EstiloUI() {
        // Clase de utilidades: no se instancia
    }

    // ---------- Paleta de colores de la marca ----------

    /** Rojo principal del logo (acciones principales: Guardar, Comprar, Ingresar...) */
    public static final Color ROJO = new Color(214, 40, 40);
    public static final Color ROJO_HOVER = new Color(170, 25, 30);

    /** Rojo oscuro (acciones de salir / cancelar / volver) */
    public static final Color ROJO_OSCURO = new Color(120, 35, 42);
    public static final Color ROJO_OSCURO_HOVER = new Color(85, 24, 30);

    /** Rojo intenso para acciones destructivas (Eliminar) */
    public static final Color ROJO_PELIGRO = new Color(176, 20, 20);
    public static final Color ROJO_PELIGRO_HOVER = new Color(140, 12, 12);

    /** Amarillo/dorado del logo (acciones secundarias: Crear Cuenta, Hacer Admin...) */
    public static final Color AMARILLO = new Color(255, 201, 40);
    public static final Color AMARILLO_HOVER = new Color(235, 170, 15);

    /** Gris neutro (acciones de navegación: Volver, Limpiar) */
    public static final Color GRIS = new Color(120, 120, 120);
    public static final Color GRIS_HOVER = new Color(95, 95, 95);

    /** Color de texto oscuro, usado sobre fondos claros como el amarillo */
    public static final Color TEXTO_OSCURO = new Color(48, 35, 38);

    // ---------- Helpers de conveniencia ----------

    /** Botón de acción principal: fondo rojo, texto blanco. */
    public static void botonPrimario(JButton boton) {
        estilizarBoton(boton, ROJO, ROJO_HOVER, Color.WHITE);
    }

    /** Botón de navegación / cancelar: fondo rojo oscuro, texto blanco. */
    public static void botonSecundario(JButton boton) {
        estilizarBoton(boton, ROJO_OSCURO, ROJO_OSCURO_HOVER, Color.WHITE);
    }

    /** Botón destructivo (Eliminar, Cerrar Sesión): rojo intenso, texto blanco. */
    public static void botonPeligro(JButton boton) {
        estilizarBoton(boton, ROJO_PELIGRO, ROJO_PELIGRO_HOVER, Color.WHITE);
    }

    /** Botón de acento amarillo (Crear Cuenta, Hacer Admin): texto oscuro. */
    public static void botonAcento(JButton boton) {
        estilizarBoton(boton, AMARILLO, AMARILLO_HOVER, TEXTO_OSCURO);
    }

    /** Botón neutro gris (Limpiar, acciones menores). */
    public static void botonNeutro(JButton boton) {
        estilizarBoton(boton, GRIS, GRIS_HOVER, Color.WHITE);
    }

    // ---------- Método principal ----------

    /**
     * Aplica el estilo redondeado con texto blanco (uso más común).
     */
    public static void estilizarBoton(JButton boton, Color colorNormal, Color colorHover) {
        estilizarBoton(boton, colorNormal, colorHover, Color.WHITE);
    }

    /**
     * Aplica el estilo redondeado con color de texto personalizado.
     *
     * El fondo se pinta dentro del ButtonUI (paint), ANTES de que se
     * dibuje el texto (super.paint), para que el texto quede siempre
     * encima y nunca se oculte detrás del fondo.
     */
    public static void estilizarBoton(
            JButton boton,
            Color colorNormal,
            Color colorHover,
            Color colorTexto) {

        boton.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(c.getBackground());
                g2.fillRoundRect(
                        0, 0,
                        c.getWidth() - 1,
                        c.getHeight() - 1,
                        25, 25);

                g2.dispose();

                // El texto se dibuja después, encima del fondo
                super.paint(g, c);
            }
        });

        boton.setForeground(colorTexto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));

        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);

        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Solo padding interno, no pinta nada (el fondo ya lo pinta el UI)
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        boton.setBackground(colorNormal);

        // Evita registrar el mismo listener dos veces si el método
        // se llama más de una vez sobre el mismo botón
        for (var listener : boton.getMouseListeners()) {
            if (listener instanceof HoverListener) {
                boton.removeMouseListener(listener);
            }
        }

        boton.addMouseListener(new HoverListener(boton, colorNormal, colorHover));
    }


    // ---------- Tablas con logo de fondo (marca de agua) ----------

    /**
     * Pone el logo como marca de agua en gris neutro detrás de una tabla.
     *
     * Uso (después de initComponents()):
     *
     *     EstiloUI.tablaConLogo(tblCarrito, jScrollPane1);
     *
     * Para ajustar la claridad del logo cambia OPACIDAD_LOGO_TABLA
     * (0.0 = invisible, 1.0 = sólido).
     */
    public static final float OPACIDAD_LOGO_TABLA = 0.16f;

    public static void tablaConLogo(JTable tabla, JScrollPane scroll) {
        tablaConLogo(tabla, scroll, "/Tienda/Imagenes/logo_galapa.png", OPACIDAD_LOGO_TABLA);
    }

    public static void tablaConLogo(JTable tabla, JScrollPane scroll,
            String rutaLogo, float opacidad) {

        BufferedImage logo = cargarLogoGris(rutaLogo);
        Color fondo = tabla.getBackground();

        // Viewport que pinta el fondo y el logo ANTES de las celdas
        Component vista = scroll.getViewport().getView();
        JViewport visor = new VisorConLogo(logo, fondo, opacidad);
        scroll.setViewport(visor);
        visor.setView(vista);
        scroll.setColumnHeaderView(tabla.getTableHeader());

        // La tabla deja ver lo que hay detrás
        tabla.setOpaque(false);
        tabla.setFillsViewportHeight(true);

        // Celdas sin fondo propio (salvo las seleccionadas)
        CeldaTransparente renderer = new CeldaTransparente();
        tabla.setDefaultRenderer(Object.class, renderer);
        tabla.setDefaultRenderer(Number.class, renderer);
        tabla.setDefaultRenderer(Integer.class, renderer);
        tabla.setDefaultRenderer(Double.class, renderer);
        tabla.setDefaultRenderer(Float.class, renderer);
        tabla.repaint();
    }

    /** Carga el logo y lo convierte a escala de grises (color neutro). */
    private static BufferedImage cargarLogoGris(String ruta) {
        try {
            java.net.URL url = EstiloUI.class.getResource(ruta);
            if (url == null) {
                return null;
            }
            BufferedImage original = ImageIO.read(url);
            BufferedImage gris = new BufferedImage(
                    original.getWidth(), original.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < original.getHeight(); y++) {
                for (int x = 0; x < original.getWidth(); x++) {
                    int argb = original.getRGB(x, y);
                    int a = (argb >> 24) & 0xFF;
                    int r = (argb >> 16) & 0xFF;
                    int g = (argb >> 8) & 0xFF;
                    int b = argb & 0xFF;
                    int lum = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                    gris.setRGB(x, y, (a << 24) | (lum << 16) | (lum << 8) | lum);
                }
            }
            return gris;
        } catch (Exception e) {
            return null; // sin logo, la tabla funciona igual
        }
    }

    /** Viewport que dibuja fondo + logo centrado (fijo, no se mueve al hacer scroll). */
    private static final class VisorConLogo extends JViewport {

        private final BufferedImage logo;
        private final float opacidad;

        VisorConLogo(BufferedImage logo, Color fondo, float opacidad) {
            this.logo = logo;
            this.opacidad = opacidad;
            setOpaque(true);
            setBackground(fondo);
            // Sin "blit": así el logo no se corre al desplazar la tabla
            setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (logo == null) {
                return;
            }

            // Nunca se agranda (evita que se vea borroso); solo se reduce si no cabe
            double escala = Math.min(1.0, Math.min(
                    getWidth() * 0.8 / logo.getWidth(),
                    getHeight() * 0.8 / logo.getHeight()));
            int w = (int) (logo.getWidth() * escala);
            int h = (int) (logo.getHeight() * escala);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setComposite(AlphaComposite.SrcOver.derive(opacidad));
            g2.drawImage(logo, (getWidth() - w) / 2, (getHeight() - h) / 2, w, h, null);
            g2.dispose();
        }
    }

    /** Renderer sin fondo propio en filas normales; filas alternas con velo gris suave. */
    private static final class CeldaTransparente extends DefaultTableCellRenderer {

        private boolean franja;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setOpaque(isSelected);
            franja = !isSelected && row % 2 == 1;
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (franja) {
                g.setColor(new Color(120, 110, 110, 28));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
            super.paintComponent(g);
        }
    }

    /** MouseListener identificable, para poder quitarlo si se re-estiliza el botón. */
    private static final class HoverListener extends MouseAdapter {

        private final JButton boton;
        private final Color colorNormal;
        private final Color colorHover;

        HoverListener(JButton boton, Color colorNormal, Color colorHover) {
            this.boton = boton;
            this.colorNormal = colorNormal;
            this.colorHover = colorHover;
        }

        @Override
        public void mouseEntered(MouseEvent evt) {
            boton.setBackground(colorHover);
            boton.repaint();
        }

        @Override
        public void mouseExited(MouseEvent evt) {
            boton.setBackground(colorNormal);
            boton.repaint();
        }
    }
}
