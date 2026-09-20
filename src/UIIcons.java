package src;

import java.awt.*;
import java.awt.geom.*;

/**
 * Vector icons used by the PMS. drawIcon is kept stable for the content area;
 * drawSidebarIcon provides the cleaner navigation glyphs used only in the left sidebar.
 */
public class UIIcons {
    public enum IconType { DASHBOARD, RESERVATIONS, ROOMS, CUSTOMERS, PAYMENTS, ADMIN }

    public static void drawIcon(Graphics2D g, IconType type, int w, int h, Color color) {
        g.setColor(color);
        switch (type) {
            case DASHBOARD:
                g.fillRect(0, 0, w/2, h/2);
                g.fillRect(w/2, 0, w/2, h/2);
                g.fillRect(0, h/2, w/2, h/2);
                g.fillRect(w/2, h/2, w/2, h/2);
                break;
            case RESERVATIONS:
                g.drawRect(0, 0, w, h);
                g.drawLine(0, h/3, w, h/3);
                g.drawLine(w/4, 0, w/4, h/3);
                g.drawLine(w/2, 0, w/2, h/3);
                g.drawLine(3*w/4, 0, 3*w/4, h/3);
                break;
            case ROOMS:
                g.drawRect(2, 2, w-4, h-4);
                g.drawLine(w/2, 2, w/2, h-2);
                g.drawLine(2, h/2, w-2, h/2);
                break;
            case CUSTOMERS:
                g.drawOval(w/4, 0, w/2, h/2);
                g.drawArc(w/6, h/2, 2*w/3, h, 0, 180);
                break;
            case PAYMENTS:
                g.drawRoundRect(0, 2, w, h-4, 4, 4);
                g.drawLine(0, h/3, w, h/3);
                break;
            case ADMIN:
                g.drawOval(w/4, h/4, w/2, h/2);
                g.drawLine(0, 0, w/4, h/4);
                g.drawLine(3*w/4, 0, w, h/4);
                g.drawLine(0, h, w/4, 3*h/4);
                g.drawLine(3*w/4, h, w, 3*h/4);
                break;
        }
    }

    public static void drawSidebarIcon(Graphics2D g, IconType type, int w, int h, Color color) {
        g.setColor(color);
        g.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        double sx = w / 20.0, sy = h / 20.0;
        g.scale(sx, sy);
        switch (type) {
            case DASHBOARD -> {
                g.drawRoundRect(2, 2, 6, 6, 2, 2);
                g.drawRoundRect(12, 2, 6, 6, 2, 2);
                g.drawRoundRect(2, 12, 6, 6, 2, 2);
                g.drawRoundRect(12, 12, 6, 6, 2, 2);
            }
            case RESERVATIONS -> {
                g.drawRoundRect(2, 3, 16, 15, 2, 2);
                g.drawLine(2, 7, 18, 7);
                g.drawLine(6, 2, 6, 5);
                g.drawLine(14, 2, 14, 5);
                g.drawLine(6, 10, 8, 10);
                g.drawLine(10, 10, 12, 10);
                g.drawLine(14, 10, 16, 10);
                g.drawLine(6, 14, 8, 14);
                g.drawLine(10, 14, 12, 14);
            }
            case ROOMS -> {
                g.drawRoundRect(2, 8, 16, 8, 2, 2);
                g.drawRoundRect(3, 5, 7, 5, 2, 2);
                g.drawLine(3, 16, 3, 19);
                g.drawLine(17, 16, 17, 19);
                g.drawLine(2, 12, 18, 12);
            }
            case CUSTOMERS -> {
                g.drawOval(7, 3, 6, 6);
                g.drawArc(4, 10, 12, 9, 0, 180);
            }
            case PAYMENTS -> {
                g.drawRoundRect(2, 4, 16, 12, 2, 2);
                g.drawLine(2, 8, 18, 8);
                g.drawLine(5, 12, 9, 12);
                g.drawOval(14, 11, 2, 2);
            }
            case ADMIN -> {
                g.drawOval(7, 7, 6, 6);
                for (int i = 0; i < 8; i++) {
                    double a = Math.PI * 2 * i / 8.0;
                    double x1 = 10 + Math.cos(a) * 5.2;
                    double y1 = 10 + Math.sin(a) * 5.2;
                    double x2 = 10 + Math.cos(a) * 7.5;
                    double y2 = 10 + Math.sin(a) * 7.5;
                    g.drawLine((int)Math.round(x1), (int)Math.round(y1), (int)Math.round(x2), (int)Math.round(y2));
                }
                g.drawOval(3, 3, 14, 14);
            }
        }
        g.scale(1.0 / sx, 1.0 / sy);
    }
    public static class SidebarIcon implements javax.swing.Icon {
        private final IconType type;
        private final Color color;
        private final int size;
        public SidebarIcon(IconType type, Color color, int size) {
            this.type = type; this.color = color; this.size = size;
        }
        @Override public int getIconWidth() { return size; }
        @Override public int getIconHeight() { return size; }
        @Override public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.translate(x, y);
            drawSidebarIcon(g2, type, size, size, color);
            g2.dispose();
        }
    }

}
