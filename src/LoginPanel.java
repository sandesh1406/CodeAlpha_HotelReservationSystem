package src;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * ELITE STAY - Clean professional authentication screen.
 * Authentication logic is unchanged; this class only improves the login UI.
 */
public class LoginPanel extends JPanel {
    private final AuthService authService;
    private final LoginCallback callback;

    public interface LoginCallback {
        void onLoginSuccess(User user);
    }

    private final Color COLOR_NAVY = new Color(15, 23, 42);
    private final Color COLOR_BLUE = new Color(37, 99, 235);
    private final Color COLOR_BLUE_DARK = new Color(29, 78, 216);
    private final Color COLOR_BG = new Color(248, 250, 252);
    private final Color COLOR_TEXT_MAIN = new Color(15, 23, 42);
    private final Color COLOR_TEXT_SEC = new Color(100, 116, 139);
    private final Color COLOR_BORDER = new Color(226, 232, 240);

    private final Font FONT_BRAND = new Font("Segoe UI", Font.BOLD, 30);
    private final Font FONT_SUB_BRAND = new Font("Segoe UI", Font.BOLD, 11);
    private final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 13);
    private final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FONT_FOOTER = new Font("Segoe UI", Font.PLAIN, 11);

    public LoginPanel(AuthService authService, LoginCallback callback) {
        this.authService = authService;
        this.callback = callback;
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        setBackground(COLOR_BG);

        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(42, 54, 34, 54)
        ));
        card.setPreferredSize(new Dimension(500, 600));
        card.setMinimumSize(new Dimension(500, 600));

        // Header
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);

        JLabel brand = new JLabel("ELITE STAY");
        brand.setFont(FONT_BRAND);
        brand.setForeground(COLOR_NAVY);
        brand.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subBrand = new JLabel("HOTEL PROPERTY MANAGEMENT");
        subBrand.setFont(FONT_SUB_BRAND);
        subBrand.setForeground(COLOR_TEXT_SEC);
        subBrand.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel accent = new JPanel();
        accent.setBackground(COLOR_BLUE);
        accent.setMaximumSize(new Dimension(48, 3));
        accent.setPreferredSize(new Dimension(48, 3));
        accent.setMinimumSize(new Dimension(48, 3));
        accent.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(brand);
        header.add(Box.createVerticalStrut(5));
        header.add(subBrand);
        header.add(Box.createVerticalStrut(18));
        header.add(accent);

        card.add(header, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        JLabel usernameLabel = createLabel("Username");
        JTextField usernameField = createTextField();

        JLabel passwordLabel = createLabel("Password");
        JPasswordField passwordField = createPasswordField();

        g.gridy = 0;
        g.insets = new Insets(38, 0, 8, 0);
        form.add(usernameLabel, g);

        g.gridy = 1;
        g.insets = new Insets(0, 0, 22, 0);
        form.add(usernameField, g);

        g.gridy = 2;
        g.insets = new Insets(0, 0, 8, 0);
        form.add(passwordLabel, g);

        g.gridy = 3;
        g.insets = new Insets(0, 0, 14, 0);
        form.add(passwordField, g);

        JCheckBox rememberMe = new JCheckBox("Remember me");
        rememberMe.setFont(FONT_INPUT);
        rememberMe.setForeground(COLOR_TEXT_SEC);
        rememberMe.setOpaque(false);
        rememberMe.setFocusPainted(false);
        rememberMe.setBorderPainted(false);
        rememberMe.setContentAreaFilled(false);
        rememberMe.setCursor(new Cursor(Cursor.HAND_CURSOR));

        g.gridy = 4;
        g.insets = new Insets(0, 0, 24, 0);
        form.add(rememberMe, g);

        JButton signInBtn = createSignInButton();

        g.gridy = 5;
        g.insets = new Insets(0, 0, 0, 0);
        g.ipady = 10;
        form.add(signInBtn, g);

        card.add(form, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setOpaque(false);

        JLabel footerLine = new JLabel("SECURE LOCAL PMS");
        footerLine.setFont(FONT_FOOTER);
        footerLine.setForeground(COLOR_TEXT_SEC);
        footerLine.setAlignmentX(Component.CENTER_ALIGNMENT);

        footer.add(Box.createVerticalStrut(28));
        footer.add(footerLine);

        card.add(footer, BorderLayout.SOUTH);

        GridBagConstraints outer = new GridBagConstraints();
        outer.gridx = 0;
        outer.gridy = 0;
        outer.insets = new Insets(20, 20, 20, 20);
        add(card, outer);

        // Authentication behavior
        Runnable login = () -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter your username.",
                        "Login",
                        JOptionPane.WARNING_MESSAGE
                );
                usernameField.requestFocusInWindow();
                return;
            }

            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter your password.",
                        "Login",
                        JOptionPane.WARNING_MESSAGE
                );
                passwordField.requestFocusInWindow();
                return;
            }

            authService.authenticate(username, password).ifPresentOrElse(
                    user -> callback.onLoginSuccess(user),
                    () -> {
                        JOptionPane.showMessageDialog(
                                this,
                                "Invalid username or password.",
                                "Login Failed",
                                JOptionPane.ERROR_MESSAGE
                        );
                        passwordField.selectAll();
                        passwordField.requestFocusInWindow();
                    }
            );
        };

        signInBtn.addActionListener(e -> login.run());
        passwordField.addActionListener(e -> login.run());
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_TEXT_MAIN);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        styleInput(field);
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        styleInput(field);
        return field;
    }

    private void styleInput(JTextField field) {
        field.setFont(FONT_INPUT);
        field.setForeground(COLOR_TEXT_MAIN);
        field.setBackground(Color.WHITE);
        field.setCaretColor(COLOR_BLUE);
        field.setPreferredSize(new Dimension(390, 46));
        field.setMinimumSize(new Dimension(390, 46));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        field.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(9, 12, 9, 12)
        ));
    }
        private JButton createSignInButton() {
        JButton button = new JButton("SIGN IN") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bg = getModel().isPressed()
                        ? COLOR_BLUE_DARK
                        : getModel().isRollover()
                        ? COLOR_BLUE_DARK
                        : COLOR_BLUE;

                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 9, 9);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(FONT_BUTTON);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(390, 48));
        button.setMinimumSize(new Dimension(390, 48));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        return button;
    }
}