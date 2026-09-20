package src;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * Shared UI components to ensure consistency across the application.
 */
public class UIComponents {

    public static final Color COLOR_BG = new Color(245, 247, 251);
    public static final Color COLOR_SIDEBAR = new Color(15, 23, 42);
    public static final Color COLOR_SIDEBAR_HOVER = new Color(30, 41, 59);
    public static final Color COLOR_PRIMARY = new Color(37, 99, 235);
    public static final Color COLOR_SUCCESS = new Color(22, 163, 74);
    public static final Color COLOR_WARNING = new Color(245, 158, 11);
    public static final Color COLOR_DANGER = new Color(220, 38, 38);
    public static final Color COLOR_CARD = Color.WHITE;
    public static final Color COLOR_TEXT_MAIN = new Color(15, 23, 42);
    public static final Color COLOR_TEXT_SEC = new Color(100, 116, 139);
    public static final Color COLOR_BORDER = new Color(226, 232, 240);

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_SECTION = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_KPI = new Font("Segoe UI", Font.BOLD, 28);

    public static class ModernButton extends JButton {
        private boolean isPrimary;
        private boolean isHovered = false;

        public ModernButton(String text, boolean isPrimary) {
            super(text);
            this.isPrimary = isPrimary;
            setFont(FONT_BOLD);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { isHovered = true; repaint(); }
                public void mouseExited(MouseEvent e) { isHovered = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (isPrimary) {
                g2.setColor(isHovered ? COLOR_PRIMARY.darker() : COLOR_PRIMARY);
            } else {
                g2.setColor(isHovered ? new Color(240, 240, 240) : COLOR_CARD);
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

            if (!isPrimary) {
                g2.setColor(COLOR_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            }

            g2.dispose();
            super.paintComponent(g);
            setForeground(isPrimary ? Color.WHITE : COLOR_TEXT_MAIN);
        }
    }

    public static class ModernCard extends JPanel {
        private String title;
        public ModernCard(String title) {
            this.title = title;
            setBackground(COLOR_CARD);
            setBorder(new EmptyBorder(20, 20, 20, 20));
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 15));
            g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 16, 16);
            g2.setColor(COLOR_CARD);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            g2.setColor(COLOR_BORDER);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            g2.dispose();
            super.paintComponent(g);
        }

        public void addComponent(Component comp) {
            if (getComponentCount() == 0 && title != null) {
                JLabel t = new JLabel(title);
                t.setFont(FONT_SECTION);
                t.setForeground(COLOR_TEXT_MAIN);
                t.setBorder(new EmptyBorder(0, 0, 15, 0));
                add(t);
            }
            add(comp);
        }
    }

    public static class StatusBadge extends JLabel {
        private Color color;
        public StatusBadge(String text, Color color) {
            super(text);
            this.color = color;
            setFont(FONT_SMALL);
            setOpaque(false);
            setBorder(new EmptyBorder(2, 8, 2, 8));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 25));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.setColor(color);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class EmptyStatePanel extends JPanel {
        public EmptyStatePanel(String title, String desc, JButton actionBtn) {
            setLayout(new GridBagLayout());
            setOpaque(false);
            setBorder(new EmptyBorder(50, 50, 50, 50));

            JLabel t = new JLabel(title);
            t.setFont(FONT_SECTION);
            t.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel d = new JLabel("<html><center>" + desc + "</center></html>");
            d.setFont(FONT_BODY);
            d.setForeground(COLOR_TEXT_SEC);
            d.setHorizontalAlignment(SwingConstants.CENTER);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(0, 0, 10, 0);
            add(t, gbc);
            gbc.gridy = 1;
            add(d, gbc);
            if (actionBtn != null) {
                gbc.gridy = 2;
                gbc.insets = new Insets(20, 0, 0, 0);
                add(actionBtn, gbc);
            }
        }
    }
}
