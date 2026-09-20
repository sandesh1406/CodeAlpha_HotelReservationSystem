package src;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.io.IOException;

/**
 * ELITE STAY - Professional Hotel Property Management System
 * Complete Product-Grade Redesign.
 */
public class HotelReservationGUI extends JFrame {
    private RoomManager roomManager;
    private ReservationManager reservationManager;
    private PaymentManager paymentManager;
    private final AuthService authService;
    private final AuthSession authSession;

    // ====================================================================================================
    // DESIGN SYSTEM: COLORS & TYPOGRAPHY
    // ====================================================================================================
    private final Color COLOR_BG             = new Color(248, 250, 252); // #F8FAFC
    private final Color COLOR_SIDEBAR        = new Color(15, 23, 42);   // #0F172A
    private final Color COLOR_SIDEBAR_HOVER  = new Color(30, 41, 59);    // #1E293B
    private final Color COLOR_PRIMARY       = new Color(37, 99, 235);   // #2563EB
    private final Color COLOR_SUCCESS       = new Color(22, 163, 74);   // #16A34A
    private final Color COLOR_WARNING      = new Color(245, 158, 11);   // #F59E0B
    private final Color COLOR_DANGER       = new Color(220, 38, 38);    // #DC2626
    private final Color COLOR_CARD         = Color.WHITE;              // #FFFFFF
    private final Color COLOR_TEXT_MAIN     = new Color(15, 23, 42);    // #0F172A
    private final Color COLOR_TEXT_SEC      = new Color(100, 116, 139); // #64748B
    private final Color COLOR_BORDER       = new Color(226, 232, 240); // #E2E8F0

    private final Font FONT_TITLE    = new Font("Segoe UI", Font.BOLD, 26);
    private final Font FONT_SECTION  = new Font("Segoe UI", Font.BOLD, 18);
    private final Font FONT_BODY     = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_BOLD     = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FONT_SMALL    = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font FONT_KPI      = new Font("Segoe UI", Font.BOLD, 28);

    // Navigation & Layout
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JLabel pageTitleLabel;
    private JLabel pageSubtitleLabel;
    private ModernSidebarItem activeNavItem;
    private JPanel mainShellPanel;

    // Dashboard Labels
    private JLabel totalRoomsLabel, availableRoomsLabel, occupiedRoomsLabel, maintenanceRoomsLabel;
    private JLabel activeBookingsLabel, todayCheckInsLabel, todayCheckOutsLabel, totalRevenueLabel;
    private JPanel roomStatusGrid;

    // Table references for refresh
    private DefaultTableModel resTableModel;
    private JTable resTable;
    private DefaultTableModel dashboardActivityModel;
    private DefaultTableModel customersTableModel;
    private DefaultTableModel paymentsTableModel;
    private DefaultTableModel reportsActivityModel;
    private JLabel adminRoomsLabel, adminReservationsLabel, adminCustomersLabel, adminPaymentsLabel;
    private JLabel adminRoomCountLabel, adminCustomerCountLabel, adminReservationCountLabel, adminPaymentCountLabel;

    public HotelReservationGUI() {
        this.authService = new AuthService();
        this.authSession = AuthSession.getInstance();

        roomManager = new RoomManager();
        roomManager.initializeDefaultInventory();
        reservationManager = new ReservationManager();
        paymentManager = new PaymentManager();

        setTitle("ELITE STAY | Hotel Property Management System");
        setSize(1440, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        showLoginScreen();
    }

    private void showLoginScreen() {
        LoginPanel loginPanel = new LoginPanel(authService, user -> {
            authSession.login(user);
            initMainShell();
        });
        setContentPane(loginPanel);
        revalidate();
        repaint();
    }

    private void initMainShell() {
        mainShellPanel = new JPanel(new BorderLayout());
        mainShellPanel.setBackground(COLOR_BG);

        // --- TOP HEADER ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(COLOR_CARD);
        topBar.setPreferredSize(new Dimension(0, 80));
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER),
            new EmptyBorder(0, 30, 0, 30)
        ));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        pageTitleLabel = new JLabel("Dashboard");
        pageTitleLabel.setFont(FONT_TITLE);
        pageTitleLabel.setForeground(COLOR_TEXT_MAIN);
        pageSubtitleLabel = new JLabel("Overview of today's operations");
        pageSubtitleLabel.setFont(FONT_BODY);
        pageSubtitleLabel.setForeground(COLOR_TEXT_SEC);
        titlePanel.add(pageTitleLabel);
        titlePanel.add(pageSubtitleLabel);
        topBar.add(titlePanel, BorderLayout.WEST);

        JPanel userArea = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        userArea.setOpaque(false);

        StatusBadge statusBadge = new StatusBadge("● Online", COLOR_SUCCESS);

        // Dynamic User Profile
        User currentUser = authSession.getCurrentUser().orElseThrow();
        JPanel userProfile = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        userProfile.setOpaque(false);

        JLabel avatar = new JLabel(currentUser.getFullName().substring(0, 1).toUpperCase()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_PRIMARY);
                g2.fillOval(0, 0, 32, 32);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                int x = (32 - fm.stringWidth(currentUser.getFullName().substring(0, 1))) / 2;
                int y = (32 + fm.getAscent()) / 2 - 2;
                g2.drawString(currentUser.getFullName().substring(0, 1), x, y);
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(32, 32));

        JLabel userName = new JLabel(currentUser.getFullName());
        userName.setFont(FONT_BOLD);
        userName.setForeground(COLOR_TEXT_MAIN);

        JLabel userRole = new JLabel(currentUser.getRole().name());
        userRole.setFont(FONT_SMALL);
        userRole.setForeground(COLOR_TEXT_SEC);
        userRole.setBorder(new EmptyBorder(0, 0, 0, 5));

        userProfile.add(avatar);
        userProfile.add(userName);
        userProfile.add(userRole);

        userArea.add(statusBadge);
        userArea.add(userProfile);
        topBar.add(userArea, BorderLayout.EAST);

        // --- SIDEBAR ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(COLOR_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(new EmptyBorder(30, 0, 30, 0));

        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        brand.setOpaque(false);
        brand.setBorder(new EmptyBorder(0, 20, 26, 20));
        JLabel brandIcon = new JLabel("ES") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_PRIMARY); g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                g2.setColor(Color.WHITE); g2.setFont(new Font("Segoe UI",Font.BOLD,15));
                FontMetrics fm=g2.getFontMetrics(); String t="ES";
                g2.drawString(t,(getWidth()-fm.stringWidth(t))/2,(getHeight()+fm.getAscent()-fm.getDescent())/2); g2.dispose();
            }
        };
        brandIcon.setPreferredSize(new Dimension(44,44));
        JPanel brandText=new JPanel(); brandText.setOpaque(false); brandText.setLayout(new BoxLayout(brandText,BoxLayout.Y_AXIS));
        JLabel logo=new JLabel("ELITE STAY"); logo.setForeground(Color.WHITE); logo.setFont(new Font("Segoe UI",Font.BOLD,19));
        JLabel logoSub=new JLabel("HOTEL PMS"); logoSub.setForeground(new Color(148,163,184)); logoSub.setFont(new Font("Segoe UI",Font.BOLD,10));
        brandText.add(logo); brandText.add(Box.createVerticalStrut(3)); brandText.add(logoSub);
        brand.add(brandIcon); brand.add(brandText); sidebar.add(brand);

        sidebar.add(createSectionHeader("MAIN"));
        sidebar.add(createNavItem("Dashboard", "DASHBOARD", "Overview of operations", UIIcons.IconType.DASHBOARD));
        sidebar.add(createNavItem("Reservations", "RESERVATIONS", "Manage bookings", UIIcons.IconType.RESERVATIONS));
        sidebar.add(createNavItem("Rooms", "ROOMS", "Manage inventory", UIIcons.IconType.ROOMS));
        sidebar.add(createNavItem("Customers", "CUSTOMERS", "Guest database", UIIcons.IconType.CUSTOMERS));
        sidebar.add(createNavItem("Payments", "PAYMENTS", "Financial records", UIIcons.IconType.PAYMENTS));

        if (authSession.hasRole(User.Role.ADMIN)) {
            sidebar.add(Box.createVerticalStrut(20));
            sidebar.add(createSectionHeader("ADMINISTRATION"));
            sidebar.add(createNavItem("Admin Center", "ADMIN_CENTER", "System control", UIIcons.IconType.ADMIN));
            sidebar.add(createNavItem("Reports", "REPORTS", "Business analytics", UIIcons.IconType.DASHBOARD));
            sidebar.add(createNavItem("Users", "USERS", "Account management", UIIcons.IconType.CUSTOMERS));
        }

        sidebar.add(Box.createVerticalGlue());

        // Logout Button
        ModernButton logoutBtn = new ModernButton("Logout", false);
        logoutBtn.setMaximumSize(new Dimension(210, 40));
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.addActionListener(e -> {
            authSession.logout();
            showLoginScreen();
        });
        sidebar.add(logoutBtn);
        sidebar.add(Box.createVerticalStrut(20));

        // Save/Load Data (Admin only)
        if (authSession.hasRole(User.Role.ADMIN)) {
            JPanel footer = new JPanel(new GridLayout(2, 1, 0, 10));
            footer.setOpaque(false);
            footer.setBorder(new EmptyBorder(0, 20, 0, 20));
            ModernButton loadBtn = new ModernButton("Load Data", false);
            loadBtn.addActionListener(e -> loadApplicationData());
            ModernButton saveBtn = new ModernButton("Save Data", true);
            saveBtn.addActionListener(e -> saveApplicationData());
            footer.add(loadBtn);
            footer.add(saveBtn);
            sidebar.add(footer);
        }

        // --- CONTENT AREA ---
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(COLOR_BG);
        cardPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        cardPanel.add(createDashboardPanel(), "DASHBOARD");
        cardPanel.add(createReservationsPanel(), "RESERVATIONS");
        cardPanel.add(createRoomsPanel(), "ROOMS");
        cardPanel.add(createCustomersPanel(), "CUSTOMERS");
        cardPanel.add(createPaymentsPanel(), "PAYMENTS");

        if (authSession.hasRole(User.Role.ADMIN)) {
            cardPanel.add(createAdminCenterPanel(), "ADMIN_CENTER");
            cardPanel.add(createReportsPanel(), "REPORTS");
            cardPanel.add(createUsersPanel(), "USERS");
        }

        mainShellPanel.add(topBar, BorderLayout.NORTH);
        mainShellPanel.add(sidebar, BorderLayout.WEST);
        mainShellPanel.add(cardPanel, BorderLayout.CENTER);

        setContentPane(mainShellPanel);
        revalidate();
        repaint();

        cardLayout.show(cardPanel, "DASHBOARD");
        refreshAllUI();
    }

    private JLabel createSectionHeader(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(COLOR_TEXT_SEC);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(15, 25, 10, 25));
        return lbl;
    }

    private ModernSidebarItem createNavItem(String text, String cardName, String subtitle, UIIcons.IconType icon) {
        ModernSidebarItem item = new ModernSidebarItem(text, icon);
        item.addActionListener(e -> {
            if (activeNavItem != null) activeNavItem.setActive(false);
            item.setActive(true);
            activeNavItem = item;
            cardLayout.show(cardPanel, cardName);
            pageTitleLabel.setText(text);
            pageSubtitleLabel.setText(subtitle);
            refreshAllUI();
        });
        return item;
    }

    // ====================================================================================================
    // DASHBOARD IMPLEMENTATION
    // ====================================================================================================
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(25, 25));
        panel.setOpaque(false);

        JPanel greeting = new JPanel(new GridLayout(2, 1));
        greeting.setOpaque(false);
        User user = authSession.getCurrentUser().orElseThrow();
        JLabel welcome = new JLabel("Good morning, " + user.getFullName());
        welcome.setFont(FONT_TITLE);
        welcome.setForeground(COLOR_TEXT_MAIN);
        JLabel subWelcome = new JLabel("Here's today's hotel operations overview.");
        subWelcome.setFont(FONT_BODY);
        subWelcome.setForeground(COLOR_TEXT_SEC);
        greeting.add(welcome);
        greeting.add(subWelcome);
        JPanel greetingWrap=new JPanel(new BorderLayout()); greetingWrap.setOpaque(false);
        greetingWrap.add(greeting,BorderLayout.WEST);
        ModernButton quickBook=new ModernButton("+ New reservation",true); quickBook.addActionListener(e->showNewReservationDialog());
        greetingWrap.add(quickBook,BorderLayout.EAST);
        panel.add(greetingWrap, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(25, 25));
        center.setOpaque(false);

        JPanel kpiGrid = new JPanel(new GridLayout(2, 4, 20, 20));
        kpiGrid.setOpaque(false);
        totalRoomsLabel = new JLabel();
        availableRoomsLabel = new JLabel();
        occupiedRoomsLabel = new JLabel();
        maintenanceRoomsLabel = new JLabel();
        activeBookingsLabel = new JLabel();
        todayCheckInsLabel = new JLabel();
        todayCheckOutsLabel = new JLabel();
        totalRevenueLabel = new JLabel();

        kpiGrid.add(new MetricCard("TOTAL ROOMS", totalRoomsLabel, "Hotel inventory", UIIcons.IconType.ROOMS));
        kpiGrid.add(new MetricCard("AVAILABLE", availableRoomsLabel, "Ready for guests", UIIcons.IconType.DASHBOARD));
        kpiGrid.add(new MetricCard("OCCUPIED", occupiedRoomsLabel, "Currently occupied", UIIcons.IconType.ROOMS));
        kpiGrid.add(new MetricCard("MAINTENANCE", maintenanceRoomsLabel, "System check", UIIcons.IconType.ADMIN));
        kpiGrid.add(new MetricCard("ACTIVE BOOKINGS", activeBookingsLabel, "Current reservations", UIIcons.IconType.RESERVATIONS));
        kpiGrid.add(new MetricCard("TODAY CHECK-INS", todayCheckInsLabel, "Arrivals expected", UIIcons.IconType.DASHBOARD));
        kpiGrid.add(new MetricCard("TODAY CHECK-OUTS", todayCheckOutsLabel, "Departures expected", UIIcons.IconType.DASHBOARD));
        kpiGrid.add(new MetricCard("TOTAL REVENUE", totalRevenueLabel, "Total earned", UIIcons.IconType.PAYMENTS));

        JPanel bottom = new JPanel(new GridLayout(1, 2, 25, 0));
        bottom.setOpaque(false);

        // Room Status Visualizer
        ModernCard statusCard = new ModernCard("ROOM STATUS");
        statusCard.setLayout(new BorderLayout());
        roomStatusGrid = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        roomStatusGrid.setOpaque(false);
        statusCard.add(roomStatusGrid, BorderLayout.CENTER);

        // Recent Activity
        ModernCard activityCard = new ModernCard("RECENT RESERVATIONS");
        activityCard.setLayout(new BorderLayout());
        String[] cols = {"ID", "Guest", "Room", "Stay", "Amount", "Status"};
        DefaultTableModel activityModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        dashboardActivityModel = activityModel;
        JTable activityTable = new JTable(activityModel);
        styleModernTable(activityTable);
        activityCard.add(new JScrollPane(activityTable), BorderLayout.CENTER);

        bottom.add(statusCard);
        bottom.add(activityCard);

        center.add(kpiGrid, BorderLayout.NORTH);
        center.add(bottom, BorderLayout.CENTER);

        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    private void updateRoomStatusMap() {
        if (roomStatusGrid == null) return;
        roomStatusGrid.removeAll();
        LocalDate today=LocalDate.now();
        for (Room r : roomManager.getAllRooms()) {
            boolean occupied = isRoomOccupiedToday(r, today);
            JPanel tile=new JPanel(new BorderLayout(4,2));
            tile.setPreferredSize(new Dimension(116,70));
            tile.setBorder(new EmptyBorder(9,10,8,10));
            tile.setBackground(occupied ? new Color(13,148,136) : new Color(22,163,74));
            JLabel room=new JLabel("ROOM "+r.getRoomId()); room.setForeground(Color.WHITE); room.setFont(new Font("Segoe UI",Font.BOLD,11));
            JLabel state=new JLabel(occupied ? "OCCUPIED" : "READY"); state.setForeground(Color.WHITE); state.setFont(new Font("Segoe UI",Font.BOLD,10));
            tile.add(room,BorderLayout.NORTH); tile.add(state,BorderLayout.SOUTH);
            roomStatusGrid.add(tile);
        }
        roomStatusGrid.revalidate(); roomStatusGrid.repaint();
    }

    private boolean isRoomOccupiedToday(Room room, LocalDate date) {
        return reservationManager.getAllReservations().stream().anyMatch(res ->
            res.getRoom()!=null && res.getRoom().getRoomId().equals(room.getRoomId()) &&
            (res.getStatus()==ReservationStatus.CONFIRMED || res.getStatus()==ReservationStatus.CHECKED_IN) &&
            !date.isBefore(res.getCheckInDate()) && date.isBefore(res.getCheckOutDate())
        );
    }

    // ====================================================================================================
    // MANAGEMENT SCREENS
    // ====================================================================================================
    private JPanel createRoomsPanel() {
        return createManagementPanel("ROOMS", "Manage hotel inventory and room availability", () -> {
            String[] cols = {"Room", "Category", "Price/Night", "Capacity", "Status", "Actions"};
            DefaultTableModel model = new DefaultTableModel(cols, 0);
            JTable table = new JTable(model);
            styleModernTable(table);
            refreshRoomTable(model);
            return new JScrollPane(table);
        }, () -> {
            JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            toolbar.setOpaque(false);
            JTextField search = new JTextField("🔍 Search rooms...");
            search.setPreferredSize(new Dimension(200, 35));
            ModernButton addBtn = new ModernButton("+ Add Room", true);
            addBtn.addActionListener(e -> showAddRoomDialog());
            toolbar.add(search);
            toolbar.add(addBtn);
            return toolbar;
        });
    }

    private JPanel createReservationsPanel() {
        return createManagementPanel("RESERVATIONS", "Manage bookings, arrivals and departures", () -> {
            String[] cols = {"ID", "Guest", "Room", "Category", "Check-in", "Check-out", "Guests", "Amount", "Status", "Payment"};
            resTableModel = new DefaultTableModel(cols, 0);
            resTable = new JTable(resTableModel);
            styleModernTable(resTable);
            refreshReservationsTable();
            return new JScrollPane(resTable);
        }, () -> {
            JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            toolbar.setOpaque(false);
            JTextField search = new JTextField("🔍 Search booking...");
            search.setPreferredSize(new Dimension(200, 35));
            ModernButton newBtn = new ModernButton("+ New Reservation", true);
            newBtn.addActionListener(e -> showNewReservationDialog());
            ModernButton viewBtn = new ModernButton("View Details", false);
            viewBtn.addActionListener(e -> viewReservationDetails());
            ModernButton checkInBtn = new ModernButton("Check In", false);
            checkInBtn.addActionListener(e -> checkInWorkflow());
            ModernButton checkOutBtn = new ModernButton("Check Out", false);
            checkOutBtn.addActionListener(e -> checkOutWorkflow());
            ModernButton cancelBtn = new ModernButton("Cancel", false);
            cancelBtn.addActionListener(e -> cancelReservationWorkflow());
            toolbar.add(search); toolbar.add(newBtn); toolbar.add(viewBtn); toolbar.add(checkInBtn); toolbar.add(checkOutBtn); toolbar.add(cancelBtn);
            return toolbar;
        });
    }

    private JPanel createCustomersPanel() {
        return createManagementPanel("CUSTOMERS", "Manage guest information and stay history", () -> {
            String[] cols = {"Customer", "Phone", "Email", "Booking(s)", "Room(s)", "Stay Dates", "Status"};
            customersTableModel = new DefaultTableModel(cols, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            JTable table = new JTable(customersTableModel);
            styleModernTable(table);
            refreshCustomersTable();
            return new JScrollPane(table);
        }, null);
    }

    private JPanel createPaymentsPanel() {
        return createManagementPanel("PAYMENTS", "Track hotel transactions and revenue", () -> {
            String[] cols = {"TX ID", "Booking ID", "Guest", "Amount", "Method", "Status"};
            paymentsTableModel = new DefaultTableModel(cols, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            JTable table = new JTable(paymentsTableModel);
            styleModernTable(table);
            refreshPaymentsTable();
            return new JScrollPane(table);
        }, null);
    }

    private JPanel createManagementPanel(String title, String subtitle, java.util.function.Supplier<Component> contentSupplier, java.util.function.Supplier<Component> toolbarSupplier) {
        JPanel panel = new JPanel(new BorderLayout(25, 25));
        panel.setOpaque(false);

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        JLabel t = new JLabel(title);
        t.setFont(FONT_TITLE);
        t.setForeground(COLOR_TEXT_MAIN);
        JLabel s = new JLabel(subtitle);
        s.setFont(FONT_BODY);
        s.setForeground(COLOR_TEXT_SEC);
        header.add(t); header.add(s);
        panel.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(20, 20));
        center.setOpaque(false);
        if (toolbarSupplier != null) {
            JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
            toolbar.setOpaque(false);
            toolbar.add(toolbarSupplier.get());
            center.add(toolbar, BorderLayout.NORTH);
        }
        center.add(contentSupplier.get(), BorderLayout.CENTER);

        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAdminCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(25, 25));
        panel.setOpaque(false);

        JLabel title = new JLabel("ADMIN CONTROL CENTER");
        title.setFont(FONT_TITLE);
        title.setForeground(COLOR_TEXT_MAIN);
        panel.add(title, BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout(25, 25));
        main.setOpaque(false);

        ModernCard metricsCard = new ModernCard("SYSTEM OVERVIEW");
        metricsCard.setLayout(new GridLayout(1, 4, 20, 0));

        adminRoomsLabel = new JLabel("0");
        adminReservationsLabel = new JLabel("0");
        adminCustomersLabel = new JLabel("0");
        adminPaymentsLabel = new JLabel("0");
        metricsCard.add(new MetricCard("Rooms", adminRoomsLabel, "Total inventory", UIIcons.IconType.ROOMS));
        metricsCard.add(new MetricCard("Reservations", adminReservationsLabel, "All bookings", UIIcons.IconType.RESERVATIONS));
        metricsCard.add(new MetricCard("Customers", adminCustomersLabel, "Unique guests", UIIcons.IconType.CUSTOMERS));
        metricsCard.add(new MetricCard("Payments", adminPaymentsLabel, "Transactions", UIIcons.IconType.PAYMENTS));
        main.add(metricsCard, BorderLayout.NORTH);

        JPanel modulesGrid = new JPanel(new GridLayout(2, 2, 20, 20));
        modulesGrid.setOpaque(false);
        adminRoomCountLabel = new JLabel();
        adminCustomerCountLabel = new JLabel();
        adminReservationCountLabel = new JLabel();
        adminPaymentCountLabel = new JLabel();
        modulesGrid.add(createAdminModuleCard("Room Management", "Manage inventory, pricing and availability", adminRoomCountLabel, "ROOMS", UIIcons.IconType.ROOMS, "Manage hotel inventory and room availability"));
        modulesGrid.add(createAdminModuleCard("Customer Management", "Manage guest profiles and stay history", adminCustomerCountLabel, "CUSTOMERS", UIIcons.IconType.CUSTOMERS, "Guest database"));
        modulesGrid.add(createAdminModuleCard("Reservation Management", "Manage bookings and cancellations", adminReservationCountLabel, "RESERVATIONS", UIIcons.IconType.RESERVATIONS, "Manage bookings"));
        modulesGrid.add(createAdminModuleCard("Payment Management", "Track transactions and receipts", adminPaymentCountLabel, "PAYMENTS", UIIcons.IconType.PAYMENTS, "Financial records"));

        main.add(modulesGrid, BorderLayout.CENTER);
        panel.add(main, BorderLayout.CENTER);
        refreshAdminCenterUI();
        return panel;
    }

    private JPanel createAdminModuleCard(String title, String desc, JLabel countLabel, String cardName, UIIcons.IconType icon, String subtitle) {
        ModernCard card = new ModernCard(title);
        card.setLayout(new BorderLayout(10, 10));

        JPanel info = new JPanel(new BorderLayout(10, 8));
        info.setOpaque(false);
        JLabel iconLbl = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                UIIcons.drawIcon((Graphics2D)g, icon, 24, 24, COLOR_PRIMARY);
            }
        };
        iconLbl.setPreferredSize(new Dimension(24, 24));
        JLabel dLbl = new JLabel("<html>" + desc + "</html>");
        dLbl.setFont(FONT_SMALL); dLbl.setForeground(COLOR_TEXT_SEC);
        JPanel text = new JPanel(); text.setOpaque(false); text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(dLbl);
        text.add(Box.createVerticalStrut(7));
        countLabel.setFont(FONT_BOLD); countLabel.setForeground(COLOR_TEXT_MAIN);
        text.add(countLabel);
        info.add(iconLbl, BorderLayout.WEST);
        info.add(text, BorderLayout.CENTER);

        card.add(info, BorderLayout.CENTER);
        ModernButton btn = new ModernButton("Open →", true);
        btn.addActionListener(e -> {
            cardLayout.show(cardPanel, cardName);
            pageTitleLabel.setText(title);
            pageSubtitleLabel.setText(subtitle);
            refreshAllUI();
        });
        card.add(btn, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setName("REPORTS");
        panel.setOpaque(false);
        rebuildReportsPanel(panel);
        return panel;
    }

    private void rebuildReportsPanel(JPanel panel) {
        panel.removeAll();
        JPanel head = new JPanel(new GridLayout(2, 1)); head.setOpaque(false);
        JLabel t = new JLabel("REPORTS & INSIGHTS"); t.setFont(FONT_TITLE); t.setForeground(COLOR_TEXT_MAIN);
        JLabel sub = new JLabel("Live occupancy, reservations, revenue and guest activity"); sub.setFont(FONT_BODY); sub.setForeground(COLOR_TEXT_SEC);
        head.add(t); head.add(sub); panel.add(head, BorderLayout.NORTH);

        LocalDate today = LocalDate.now();
        List<Reservation> all = reservationManager.getAllReservations();
        long occupied = all.stream().filter(r -> isRoomOccupiedToday(r.getRoom(), today)).count();
        long active = all.stream().filter(r -> r.getStatus() == ReservationStatus.CONFIRMED || r.getStatus() == ReservationStatus.CHECKED_IN).count();
        long cancelled = all.stream().filter(r -> r.getStatus() == ReservationStatus.CANCELLED).count();
        long checkedOut = all.stream().filter(r -> r.getStatus() == ReservationStatus.CHECKED_OUT).count();
        double paidRevenue = all.stream().filter(r -> r.getPayment() != null && r.getPayment().getPaymentStatus() == PaymentStatus.PAID).mapToDouble(Reservation::getTotalAmount).sum();
        long arrivals = all.stream().filter(r -> r.getCheckInDate().equals(today) && r.getStatus() != ReservationStatus.CANCELLED && r.getStatus() != ReservationStatus.CHECKED_OUT).count();
        long departures = all.stream().filter(r -> r.getCheckOutDate().equals(today) && r.getStatus() != ReservationStatus.CANCELLED && r.getStatus() != ReservationStatus.CHECKED_OUT).count();

        JPanel grid = new JPanel(new GridLayout(2, 4, 18, 18)); grid.setOpaque(false);
        grid.add(reportCard("OCCUPANCY TODAY", occupied + " / " + roomManager.getTotalRoomsCount(), "Rooms occupied today", COLOR_SUCCESS));
        grid.add(reportCard("ACTIVE BOOKINGS", String.valueOf(active), "Confirmed + checked in", COLOR_PRIMARY));
        grid.add(reportCard("PAID REVENUE", formatCurrency(paidRevenue), "From completed payments", COLOR_SUCCESS));
        grid.add(reportCard("ARRIVALS TODAY", String.valueOf(arrivals), "Scheduled check-ins", COLOR_WARNING));
        grid.add(reportCard("DEPARTURES TODAY", String.valueOf(departures), "Scheduled check-outs", COLOR_DANGER));
        grid.add(reportCard("CHECKED OUT", String.valueOf(checkedOut), "Completed stays", COLOR_TEXT_SEC));
        grid.add(reportCard("CANCELLED", String.valueOf(cancelled), "Cancelled bookings", COLOR_DANGER));
        grid.add(reportCard("TOTAL BOOKINGS", String.valueOf(all.size()), "All reservation records", COLOR_PRIMARY));

        ModernCard activity = new ModernCard("RESERVATION ACTIVITY"); activity.setLayout(new BorderLayout(0, 12));
        String[] cols = {"Booking", "Guest", "Room", "Stay", "Status", "Amount"};
        reportsActivityModel = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        for (Reservation r : all) {
            reportsActivityModel.addRow(new Object[]{r.getReservationId(), r.getGuest().getName(), r.getRoom().getRoomId(), r.getCheckInDate() + " → " + r.getCheckOutDate(), prettyStatus(r.getStatus()), formatCurrency(r.getTotalAmount())});
        }
        JTable table = new JTable(reportsActivityModel); styleModernTable(table); activity.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel center = new JPanel(new BorderLayout(0, 18)); center.setOpaque(false); center.add(grid, BorderLayout.NORTH); center.add(activity, BorderLayout.CENTER); panel.add(center, BorderLayout.CENTER);
        panel.revalidate(); panel.repaint();
    }

    private JPanel reportCard(String title,String value,String desc,Color accent){
        JPanel c=new JPanel(new BorderLayout(10,8)); c.setBackground(COLOR_CARD); c.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER),new EmptyBorder(15,18,15,18)));
        JPanel bar=new JPanel(); bar.setBackground(accent); bar.setPreferredSize(new Dimension(5,50)); c.add(bar,BorderLayout.WEST);
        JPanel text=new JPanel(new GridLayout(3,1)); text.setOpaque(false);
        JLabel a=new JLabel(title); a.setFont(new Font("Segoe UI",Font.BOLD,11)); a.setForeground(COLOR_TEXT_SEC);
        JLabel b=new JLabel(value); b.setFont(FONT_KPI); b.setForeground(COLOR_TEXT_MAIN);
        JLabel d=new JLabel(desc); d.setFont(FONT_SMALL); d.setForeground(COLOR_TEXT_SEC); text.add(a);text.add(b);text.add(d); c.add(text,BorderLayout.CENTER); return c;
    }

    private String prettyStatus(ReservationStatus s){ return s.name().replace('_',' '); }

    private JPanel createUsersPanel() {
        return createManagementPanel("USERS & ROLES", "Manage system access and permissions", () -> {
            String[] cols = {"Name", "Username", "Role", "Status"};
            DefaultTableModel model = new DefaultTableModel(cols, 0);
            JTable table = new JTable(model);
            styleModernTable(table);

            User current=authSession.getCurrentUser().orElse(null);
            String currentUsername=current==null?"":current.getUsername();
            model.addRow(new Object[]{"Administrator", "admin", "ADMIN", currentUsername.equals("admin") ? "SIGNED IN" : "OFFLINE"});
            model.addRow(new Object[]{"Hotel Manager", "manager", "MANAGER", currentUsername.equals("manager") ? "SIGNED IN" : "OFFLINE"});
            model.addRow(new Object[]{"Front Desk Staff", "receptionist", "RECEPTIONIST", currentUsername.equals("receptionist") ? "SIGNED IN" : "OFFLINE"});

            return new JScrollPane(table);
        }, null);
    }

    // ====================================================================================================
    // STYLING & DATA BINDING
    // ====================================================================================================
    private void styleModernTable(JTable table) {
        table.setRowHeight(46);
        table.setFont(FONT_BODY);
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(COLOR_TEXT_MAIN);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        table.getTableHeader().setBackground(COLOR_SIDEBAR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(FONT_BOLD);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setPreferredSize(new Dimension(0, 44));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBorder(new EmptyBorder(0, 15, 0, 15));
                label.setHorizontalAlignment(JLabel.LEFT);
                if (!isSelected) {
                    label.setBackground(row % 2 == 0 ? COLOR_CARD : new Color(248, 250, 252));
                }
                if (value instanceof String) {
                    String s = (String) value;
                    if (s.equals("Available") || s.equals("Confirmed") || s.equals("Paid")) {
                        label.setForeground(COLOR_SUCCESS);
                    } else if (s.equals("Cancelled")) {
                        label.setForeground(COLOR_DANGER);
                    } else if (s.equals("Pending")) {
                        label.setForeground(COLOR_WARNING);
                    }
                }
                return label;
            }
        });
    }

    private String formatCurrency(double amount) {
        return String.format("₹%,.2f", amount);
    }

    private long getUniqueCustomerCount() {
        Set<String> guestIds = new LinkedHashSet<>();
        for (Reservation r : reservationManager.getAllReservations()) {
            if (r.getGuest() != null) guestIds.add(r.getGuest().getGuestId());
        }
        return guestIds.size();
    }

    private long getPaymentTransactionCount() {
        return reservationManager.getAllReservations().stream().filter(r -> r.getPayment() != null).count();
    }

    private void refreshCustomersTable() {
        if (customersTableModel == null) return;
        customersTableModel.setRowCount(0);
        Map<String, List<Reservation>> byGuest = new LinkedHashMap<>();
        for (Reservation r : reservationManager.getAllReservations()) {
            if (r.getGuest() != null) byGuest.computeIfAbsent(r.getGuest().getGuestId(), k -> new ArrayList<>()).add(r);
        }
        for (List<Reservation> guestReservations : byGuest.values()) {
            Guest g = guestReservations.get(0).getGuest();
            String bookings = guestReservations.stream().map(Reservation::getReservationId).reduce((a,b) -> a + ", " + b).orElse("-");
            String rooms = guestReservations.stream().map(r -> r.getRoom().getRoomId()).distinct().reduce((a,b) -> a + ", " + b).orElse("-");
            String stays = guestReservations.stream().map(r -> r.getCheckInDate() + " to " + r.getCheckOutDate()).reduce((a,b) -> a + " | " + b).orElse("-");
            String status = guestReservations.get(guestReservations.size() - 1).getStatus().name().replace('_', ' ');
            customersTableModel.addRow(new Object[]{g.getName(), g.getPhone(), g.getEmail(), bookings, rooms, stays, status});
        }
    }

    private void refreshPaymentsTable() {
        if (paymentsTableModel == null) return;
        paymentsTableModel.setRowCount(0);
        for (Reservation res : reservationManager.getAllReservations()) {
            Payment p = res.getPayment();
            if (p != null) {
                paymentsTableModel.addRow(new Object[]{p.getTransactionId(), res.getReservationId(), res.getGuest().getName(), formatCurrency(p.getAmount()), p.getPaymentMethod(), p.getPaymentStatus()});
            }
        }
    }

    private void refreshAdminCenterUI() {
        if (adminRoomsLabel == null) return;
        long customers = getUniqueCustomerCount();
        long payments = getPaymentTransactionCount();
        int reservations = reservationManager.getAllReservations().size();
        int rooms = roomManager.getTotalRoomsCount();
        adminRoomsLabel.setText(String.valueOf(rooms));
        adminReservationsLabel.setText(String.valueOf(reservations));
        adminCustomersLabel.setText(String.valueOf(customers));
        adminPaymentsLabel.setText(String.valueOf(payments));
        adminRoomCountLabel.setText(rooms + (rooms == 1 ? " room" : " rooms"));
        adminCustomerCountLabel.setText(customers + (customers == 1 ? " customer" : " customers"));
        adminReservationCountLabel.setText(reservations + (reservations == 1 ? " reservation" : " reservations"));
        adminPaymentCountLabel.setText(payments + (payments == 1 ? " transaction" : " transactions"));
    }

    private void refreshAllUI() {
        if (totalRoomsLabel != null) updateStats();
        updateRoomStatusMap();
        refreshReservationsTable();
        refreshCustomersTable();
        refreshPaymentsTable();
        refreshDashboardActivity();
        refreshAdminCenterUI();
        if (cardPanel != null) {
            // Reports are intentionally rebuilt from current manager data so they never show stale numbers.
            for (Component c : cardPanel.getComponents()) {
                if (c instanceof JPanel && "REPORTS".equals(c.getName())) {
                    rebuildReportsPanel((JPanel) c);
                }
            }
            cardPanel.revalidate();
            cardPanel.repaint();
        }
    }

    private void refreshDashboardActivity(){
        if(dashboardActivityModel==null) return; dashboardActivityModel.setRowCount(0);
        List<Reservation> list=new ArrayList<>(reservationManager.getAllReservations());
        list.sort(Comparator.comparing(Reservation::getCheckInDate).reversed());
        for(int i=0;i<Math.min(8,list.size());i++){ Reservation r=list.get(i); dashboardActivityModel.addRow(new Object[]{r.getReservationId(),r.getGuest().getName(),r.getRoom().getRoomId(),r.getCheckInDate()+" → "+r.getCheckOutDate(),formatCurrency(r.getTotalAmount()),prettyStatus(r.getStatus())}); }
    }

    private void updateStats() {
        LocalDate today=LocalDate.now();
        int total=roomManager.getTotalRoomsCount();
        int occupied=(int)roomManager.getAllRooms().stream().filter(r->isRoomOccupiedToday(r,today)).count();
        totalRoomsLabel.setText(String.valueOf(total)); availableRoomsLabel.setText(String.valueOf(Math.max(0,total-occupied))); occupiedRoomsLabel.setText(String.valueOf(occupied));
        maintenanceRoomsLabel.setText("0"); activeBookingsLabel.setText(String.valueOf(reservationManager.getActiveReservationsCount()));
        todayCheckInsLabel.setText(String.valueOf(reservationManager.getAllReservations().stream().filter(r->r.getCheckInDate().equals(today)&&r.getStatus()!=ReservationStatus.CANCELLED&&r.getStatus()!=ReservationStatus.CHECKED_OUT).count()));
        todayCheckOutsLabel.setText(String.valueOf(reservationManager.getAllReservations().stream().filter(r->r.getCheckOutDate().equals(today)&&r.getStatus()!=ReservationStatus.CANCELLED&&r.getStatus()!=ReservationStatus.CHECKED_OUT).count()));
        double revenue=reservationManager.getAllReservations().stream().filter(r->r.getPayment()!=null&&r.getPayment().getPaymentStatus()==PaymentStatus.PAID).mapToDouble(Reservation::getTotalAmount).sum();
        totalRevenueLabel.setText(formatCurrency(revenue));
    }

    private void refreshRoomTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Room r : roomManager.getAllRooms()) {
            model.addRow(new Object[]{r.getRoomId(), r.getCategory(), formatCurrency(r.getPricePerNight()), r.getCapacity(), r.isAvailable() ? "Available" : "Occupied"});
        }
    }

    private void refreshReservationsTable() {
        if (resTableModel == null) return;
        resTableModel.setRowCount(0);
        for (Reservation res : reservationManager.getAllReservations()) {
            resTableModel.addRow(new Object[]{
                res.getReservationId(), res.getGuest().getName(), res.getRoom().getRoomId(),
                res.getRoom().getCategory(), res.getCheckInDate(), res.getCheckOutDate(),
                res.getNumberOfGuests(), formatCurrency(res.getTotalAmount()), res.getStatus(),
                (res.getPayment() != null ? res.getPayment().getPaymentStatus() : "PENDING")
            });
        }
    }

    private void showAddRoomDialog() {
        JDialog dialog = new JDialog(this, "Add New Room", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(COLOR_BG);

        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 15, 5);

        gbc.gridx = 0; gbc.gridy = 0; p.add(new JLabel("Room ID:"), gbc);
        JTextField idF = new JTextField(10); gbc.gridx = 1; p.add(idF, gbc);
        gbc.gridx = 0; gbc.gridy = 1; p.add(new JLabel("Category:"), gbc);
        JComboBox<RoomCategory> catB = new JComboBox<>(RoomCategory.values()); gbc.gridx = 1; p.add(catB, gbc);
        gbc.gridx = 0; gbc.gridy = 2; p.add(new JLabel("Price/Night:"), gbc);
        JTextField prF = new JTextField("0.0", 10); gbc.gridx = 1; p.add(prF, gbc);
        gbc.gridx = 0; gbc.gridy = 3; p.add(new JLabel("Capacity:"), gbc);
        JTextField cpF = new JTextField("1", 10); gbc.gridx = 1; p.add(cpF, gbc);

        ModernButton save = new ModernButton("Add Room", true);
        save.addActionListener(e -> {
            try {
                Room r = new Room(idF.getText().trim(), (RoomCategory)catB.getSelectedItem(),
                    Double.parseDouble(prF.getText()), Integer.parseInt(cpF.getText()), true);
                roomManager.addRoom(r);
                refreshAllUI();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        p.add(save, gbc);

        dialog.add(p);
        dialog.setVisible(true);
    }

    private void showNewReservationDialog() {
        JDialog dialog = new JDialog(this, "New Reservation", true);
        dialog.setSize(680, 650);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(COLOR_BG);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(COLOR_CARD);
        p.setBorder(new EmptyBorder(22, 28, 22, 28));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(7, 7, 7, 7);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        JTextField name = new JTextField();
        JTextField phone = new JTextField();
        JTextField email = new JTextField();

        // Room selection is intentionally split into Category + Available Room.
        // This prevents the raw Room.toString() value from being shown to the user.
        JComboBox<RoomCategory> category = new JComboBox<>();
        category.addItem(null);
        for (RoomCategory rc : RoomCategory.values()) category.addItem(rc);
        category.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                            boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(value == null ? "Select room category" : prettyRoomCategory((RoomCategory) value));
                return this;
            }
        });

        JComboBox<Room> room = new JComboBox<>();
        room.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                            boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Room) {
                    Room r = (Room) value;
                    setText("Room " + r.getRoomId() + "  •  " + prettyRoomCategory(r.getCategory())
                            + "  •  " + formatCurrency(r.getPricePerNight()) + "/night");
                } else {
                    setText("Select an available room");
                }
                return this;
            }
        });

        JComboBox<Integer> guests = new JComboBox<>();
        for (int i = 1; i <= 8; i++) guests.addItem(i);

        JTextField in = new JTextField(LocalDate.now().toString());
        JTextField out = new JTextField(LocalDate.now().plusDays(1).toString());
        JLabel availabilityInfo = new JLabel("Select a category to see available rooms.");
        availabilityInfo.setFont(FONT_SMALL);
        availabilityInfo.setForeground(COLOR_TEXT_SEC);

        Runnable refreshRoomChoices = () -> {
            RoomCategory selectedCategory = (RoomCategory) category.getSelectedItem();
            Integer guestCount = (Integer) guests.getSelectedItem();
            LocalDate checkIn = null;
            LocalDate checkOut = null;

            try {
                checkIn = LocalDate.parse(in.getText().trim());
                checkOut = LocalDate.parse(out.getText().trim());
            } catch (Exception ignored) {
                // Keep the room list empty until valid dates are entered.
            }

            DefaultComboBoxModel<Room> model = new DefaultComboBoxModel<>();
            if (selectedCategory != null && guestCount != null && checkIn != null && checkOut != null
                    && checkOut.isAfter(checkIn)) {
                List<Room> available = reservationManager.searchAvailableRooms(
                        roomManager.getAllRooms(), checkIn, checkOut, selectedCategory, guestCount);
                for (Room r : available) model.addElement(r);

                if (available.isEmpty()) {
                    availabilityInfo.setText("No rooms available for this category, guest count and dates.");
                    availabilityInfo.setForeground(COLOR_WARNING);
                } else {
                    availabilityInfo.setText(available.size() + " room" + (available.size() == 1 ? "" : "s") + " available.");
                    availabilityInfo.setForeground(COLOR_SUCCESS);
                }
            } else if (selectedCategory == null) {
                availabilityInfo.setText("Select a room category to see available rooms.");
                availabilityInfo.setForeground(COLOR_TEXT_SEC);
            } else {
                availabilityInfo.setText("Enter valid check-in and check-out dates.");
                availabilityInfo.setForeground(COLOR_WARNING);
            }

            room.setModel(model);
        };

        category.addActionListener(e -> refreshRoomChoices.run());
        guests.addActionListener(e -> refreshRoomChoices.run());
        in.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { refreshRoomChoices.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { refreshRoomChoices.run(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { refreshRoomChoices.run(); }
        });
        out.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { refreshRoomChoices.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { refreshRoomChoices.run(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { refreshRoomChoices.run(); }
        });

        String[] labels = {
                "Guest name", "Phone", "Email", "Room category", "Available room",
                "Guests", "Check-in (YYYY-MM-DD)", "Check-out (YYYY-MM-DD)"
        };
        Component[] fields = {name, phone, email, category, room, guests, in, out};

        for (int i = 0; i < labels.length; i++) {
            g.gridx = 0;
            g.gridy = i;
            g.weightx = 0;
            p.add(new JLabel(labels[i]), g);
            g.gridx = 1;
            g.weightx = 1;
            p.add(fields[i], g);
        }

        g.gridx = 1;
        g.gridy = 8;
        g.weightx = 1;
        p.add(availabilityInfo, g);

        ModernButton create = new ModernButton("Create Reservation", true);
        g.gridx = 0;
        g.gridy = 9;
        g.gridwidth = 2;
        g.insets = new Insets(18, 7, 4, 7);
        p.add(create, g);

        create.addActionListener(e -> {
            try {
                if (name.getText().trim().isEmpty()) throw new IllegalArgumentException("Guest name is required.");
                if (phone.getText().trim().isEmpty()) throw new IllegalArgumentException("Phone number is required.");

                LocalDate d1 = LocalDate.parse(in.getText().trim());
                LocalDate d2 = LocalDate.parse(out.getText().trim());
                Room selected = (Room) room.getSelectedItem();
                Integer gc = (Integer) guests.getSelectedItem();

                if (d2.isBefore(d1) || d2.equals(d1)) {
                    throw new IllegalArgumentException("Check-out date must be after check-in date.");
                }
                if (selected == null) throw new IllegalArgumentException("Please select an available room.");
                if (gc == null || gc > selected.getCapacity()) {
                    throw new IllegalArgumentException("Guest count exceeds the selected room capacity.");
                }

                Guest guest = new Guest(
                        "G" + (reservationManager.getAllReservations().size() + 1),
                        name.getText().trim(),
                        phone.getText().trim(),
                        email.getText().trim()
                );

                Reservation r = reservationManager.createReservation(guest, selected, d1, d2, gc);
                refreshAllUI();
                dialog.dispose();

                // Payment is now a separate step instead of jumping directly to the receipt.
                showPaymentDialog(r);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Reservation", JOptionPane.WARNING_MESSAGE);
            }
        });

        refreshRoomChoices.run();
        dialog.add(p);
        dialog.setVisible(true);
    }

    private String prettyRoomCategory(RoomCategory category) {
        if (category == null) return "";
        String value = category.name().toLowerCase(Locale.ROOT);
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }

    private void showPaymentDialog(Reservation reservation) {
        JDialog dialog = new JDialog(this, "Payment", true);
        dialog.setSize(520, 470);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(COLOR_BG);

        JPanel main = new JPanel(new BorderLayout(18, 18));
        main.setBorder(new EmptyBorder(24, 28, 24, 28));
        main.setBackground(COLOR_CARD);

        JLabel title = new JLabel("PAYMENT", SwingConstants.CENTER);
        title.setFont(FONT_TITLE);
        title.setForeground(COLOR_TEXT_MAIN);
        main.add(title, BorderLayout.NORTH);

        JPanel details = new JPanel(new GridLayout(4, 2, 10, 12));
        details.setOpaque(false);
        addPaymentDetail(details, "Booking ID", reservation.getReservationId());
        addPaymentDetail(details, "Guest", reservation.getGuest().getName());
        addPaymentDetail(details, "Room", "Room " + reservation.getRoom().getRoomId()
                + " • " + prettyRoomCategory(reservation.getRoom().getCategory()));
        addPaymentDetail(details, "Total", formatCurrency(reservation.getTotalAmount()));

        JPanel center = new JPanel(new BorderLayout(12, 18));
        center.setOpaque(false);
        center.add(details, BorderLayout.NORTH);

        JPanel paymentBox = new JPanel(new GridBagLayout());
        paymentBox.setBackground(new Color(248, 250, 252));
        paymentBox.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER),
                new EmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints pg = new GridBagConstraints();
        pg.insets = new Insets(5, 5, 5, 5);
        pg.fill = GridBagConstraints.HORIZONTAL;
        pg.gridx = 0;
        pg.gridy = 0;
        pg.weightx = 0;
        paymentBox.add(new JLabel("Payment method"), pg);

        JComboBox<PaymentMethod> methodBox = new JComboBox<>(PaymentMethod.values());
        methodBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                            boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(value == null ? "Select payment method" : prettyPaymentMethod((PaymentMethod) value));
                return this;
            }
        });
        pg.gridx = 1;
        pg.weightx = 1;
        paymentBox.add(methodBox, pg);

        JLabel pending = new JLabel("Payment is currently PENDING. You can pay now or pay later.");
        pending.setFont(FONT_SMALL);
        pending.setForeground(COLOR_WARNING);
        pg.gridx = 0;
        pg.gridy = 1;
        pg.gridwidth = 2;
        paymentBox.add(pending, pg);

        center.add(paymentBox, BorderLayout.CENTER);
        main.add(center, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttons.setOpaque(false);

        ModernButton payNow = new ModernButton("Pay Now", true);
        payNow.addActionListener(e -> {
            try {
                PaymentMethod method = (PaymentMethod) methodBox.getSelectedItem();
                Payment payment = paymentManager.processPayment(reservation, method);
                refreshAllUI();
                dialog.dispose();
                showConfirmationDialog(reservation);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Payment", JOptionPane.WARNING_MESSAGE);
            }
        });

        ModernButton payLater = new ModernButton("Pay Later", false);
        payLater.addActionListener(e -> {
            refreshAllUI();
            dialog.dispose();
            showConfirmationDialog(reservation);
        });

        buttons.add(payNow);
        buttons.add(payLater);
        main.add(buttons, BorderLayout.SOUTH);

        dialog.add(main);
        dialog.setVisible(true);
    }

    private void addPaymentDetail(JPanel panel, String label, String value) {
        JLabel l = new JLabel(label);
        l.setFont(FONT_BOLD);
        l.setForeground(COLOR_TEXT_SEC);
        JLabel v = new JLabel(value);
        v.setFont(FONT_BODY);
        v.setForeground(COLOR_TEXT_MAIN);
        panel.add(l);
        panel.add(v);
    }

    private String prettyPaymentMethod(PaymentMethod method) {
        if (method == null) return "";
        String value = method.name().toLowerCase(Locale.ROOT);
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }

    private Reservation getSelectedReservation(){ int row=resTable==null?-1:resTable.getSelectedRow(); if(row<0)return null; return reservationManager.getReservation((String)resTableModel.getValueAt(row,0)); }
    private void checkInWorkflow(){ Reservation r=getSelectedReservation(); if(r==null){JOptionPane.showMessageDialog(this,"Select a reservation first.");return;} try{reservationManager.checkIn(r.getReservationId());refreshAllUI();}catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Check In",JOptionPane.WARNING_MESSAGE);} }
    private void checkOutWorkflow(){ Reservation r=getSelectedReservation(); if(r==null){JOptionPane.showMessageDialog(this,"Select a reservation first.");return;} try{reservationManager.checkOut(r.getReservationId());refreshAllUI();JOptionPane.showMessageDialog(this,"Guest checked out. Room "+r.getRoom().getRoomId()+" is now available for new bookings.");}catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Check Out",JOptionPane.WARNING_MESSAGE);} }

    private void viewReservationDetails() {
        int row = resTable.getSelectedRow();
        if (row == -1) return;
        String id = (String) resTableModel.getValueAt(row, 0);
        Reservation res = reservationManager.getReservation(id);
        if (res != null) showConfirmationDialog(res);
    }

    private void cancelReservationWorkflow() {
        int row = resTable.getSelectedRow();
        if (row == -1) return;
        String id = (String) resTableModel.getValueAt(row, 0);
        Reservation res = reservationManager.getReservation(id);
        if (res != null) showCancellationConfirmationDialog(res);
    }

    private void showCancellationConfirmationDialog(Reservation res) {
        JDialog dialog = new JDialog(this, "Cancel Reservation", true);
        dialog.setSize(450, 300);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(COLOR_CARD);

        JPanel p = new JPanel(new BorderLayout(20, 20));
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel title = new JLabel("Cancel Reservation?", SwingConstants.CENTER);
        title.setFont(FONT_SECTION);
        title.setForeground(COLOR_DANGER);
        p.add(title, BorderLayout.NORTH);

        JLabel info = new JLabel("Are you sure you want to cancel booking " + res.getReservationId() + "?");
        info.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(info, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btns.setOpaque(false);
        ModernButton yes = new ModernButton("Yes, Cancel", true);
        yes.setBackground(COLOR_DANGER);
        yes.addActionListener(e -> {
            reservationManager.cancelReservation(res.getReservationId());
            refreshAllUI();
            dialog.dispose();
        });
        ModernButton no = new ModernButton("No, Keep", false);
        no.addActionListener(e -> dialog.dispose());
        btns.add(yes); btns.add(no);
        p.add(btns, BorderLayout.SOUTH);

        dialog.add(p);
        dialog.setVisible(true);
    }

    private void showConfirmationDialog(Reservation res) {
        JDialog dialog = new JDialog(this, "Booking Details", true);
        dialog.setSize(500, 700);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(COLOR_BG);

        JPanel main = new JPanel(new BorderLayout(20, 20));
        main.setBorder(new EmptyBorder(20, 20, 20, 20));
        main.setOpaque(false);

        JLabel title = new JLabel("RESERVATION DETAILS", SwingConstants.CENTER);
        title.setFont(FONT_SECTION);
        main.add(title, BorderLayout.NORTH);

        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setOpaque(false);

        details.add(createDetailSection("Reservation", new String[]{
            "Booking ID: " + res.getReservationId(), "Status: " + res.getStatus()
        }));
        details.add(Box.createRigidArea(new Dimension(0, 15)));
        details.add(createDetailSection("Guest", new String[]{
            "Name: " + res.getGuest().getName(), "Phone: " + res.getGuest().getPhone()
        }));
        details.add(Box.createRigidArea(new Dimension(0, 15)));
        details.add(createDetailSection("Room", new String[]{
            "Room: " + res.getRoom().getRoomId(), "Category: " + res.getRoom().getCategory()
        }));
        details.add(Box.createRigidArea(new Dimension(0, 15)));
        details.add(createDetailSection("Stay", new String[]{
            "Check-in: " + res.getCheckInDate(), "Check-out: " + res.getCheckOutDate()
        }));
        details.add(Box.createRigidArea(new Dimension(0, 15)));
        details.add(createDetailSection("Payment", new String[]{
            "Total: " + formatCurrency(res.getTotalAmount()),
            "Status: " + (res.getPayment() != null ? res.getPayment().getPaymentStatus() : "PENDING")
        }));

        main.add(new JScrollPane(details), BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btns.setOpaque(false);
        ModernButton print = new ModernButton("Print Receipt", true);
        print.addActionListener(e -> printReceipt(res));
        ModernButton close = new ModernButton("Close", false);
        close.addActionListener(e -> dialog.dispose());
        btns.add(print); btns.add(close);
        main.add(btns, BorderLayout.SOUTH);

        dialog.add(main);
        dialog.setVisible(true);
    }

    private JPanel createDetailSection(String title, String[] details) {
        JPanel section = new JPanel(new BorderLayout());
        section.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(COLOR_BORDER), title));
        section.setBackground(COLOR_CARD);
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(COLOR_CARD);
        for (String d : details) {
            JLabel l = new JLabel(d);
            l.setBorder(new EmptyBorder(5, 10, 5, 10));
            content.add(l);
        }
        section.add(content, BorderLayout.CENTER);
        return section;
    }

    private void saveApplicationData() {
        try {
            List<Guest> guests = new ArrayList<>();
            List<Payment> payments = new ArrayList<>();
            for (Reservation res : reservationManager.getAllReservations()) {
                guests.add(res.getGuest());
                if (res.getPayment() != null) payments.add(res.getPayment());
            }
            ApplicationData data = new ApplicationData(
                roomManager.getAllRooms(), reservationManager.getAllReservations(),
                guests, payments, reservationManager.getReservationCounter(), paymentManager.getTransactionCounter()
            );
            FileManager.save(data);
            JOptionPane.showMessageDialog(this, "Data saved successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void loadApplicationData() {
        try {
            ApplicationData data = FileManager.load();
            if (data == null) return;
            roomManager = new RoomManager();
            for (Room r : data.getRooms()) roomManager.addRoom(r);
            reservationManager = new ReservationManager();
            reservationManager.setReservationCounter(data.getReservationCounter());
            for (Reservation res : data.getReservations()) reservationManager.addReservation(res);
            paymentManager = new PaymentManager();
            paymentManager.setTransactionCounter(data.getTransactionCounter());
            for (Payment p : data.getPayments()) paymentManager.addPayment(p);
            refreshAllUI();
            JOptionPane.showMessageDialog(this, "Data loaded successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void printReceipt(Reservation res) {
        Payment p=res.getPayment(); java.awt.print.PrinterJob job=java.awt.print.PrinterJob.getPrinterJob(); job.setPrintable(new ReceiptPrintable(res,p));
        if(job.printDialog()){ try{job.print();}catch(Exception ex){JOptionPane.showMessageDialog(this,"Printing failed: "+ex.getMessage());} }
    }

    private class ReceiptPrintable implements java.awt.print.Printable {
        private final Reservation res; private final Payment payment;
        ReceiptPrintable(Reservation r,Payment p){res=r;payment=p;}
        @Override public int print(Graphics graphics,java.awt.print.PageFormat pf,int pageIndex){
            if(pageIndex>0)return NO_SUCH_PAGE; Graphics2D g=(Graphics2D)graphics; g.translate(pf.getImageableX(),pf.getImageableY());
            int width=(int)pf.getImageableWidth(); int y=25;
            g.setColor(Color.BLACK); g.setFont(new Font("Segoe UI",Font.BOLD,22)); g.drawString("ELITE STAY HOTEL",30,y);
            g.setFont(new Font("Segoe UI",Font.PLAIN,10)); g.drawString("HOTEL PROPERTY MANAGEMENT SYSTEM",30,y+18); g.drawString("Reservation Receipt / Invoice",width-180,y+10);
            y+=38; g.drawLine(20,y,width-20,y); y+=24;
            g.setFont(new Font("Segoe UI",Font.BOLD,11)); g.drawString("BOOKING DETAILS",30,y); y+=18;
            g.setFont(new Font("Segoe UI",Font.PLAIN,11));
            g.drawString("Booking ID: "+res.getReservationId(),30,y); g.drawString("Status: "+prettyStatus(res.getStatus()),width/2,y); y+=18;
            g.drawString("Guest: "+res.getGuest().getName(),30,y); g.drawString("Room: "+res.getRoom().getRoomId()+" / "+res.getRoom().getCategory(),width/2,y); y+=18;
            g.drawString("Phone: "+res.getGuest().getPhone(),30,y); g.drawString("Guests: "+res.getNumberOfGuests(),width/2,y); y+=18;
            g.drawString("Check-in: "+res.getCheckInDate(),30,y); g.drawString("Check-out: "+res.getCheckOutDate(),width/2,y); y+=28;
            g.setFont(new Font("Segoe UI",Font.BOLD,11)); g.drawString("CHARGES",30,y); y+=18;
            g.setFont(new Font("Segoe UI",Font.PLAIN,11)); long nights=java.time.temporal.ChronoUnit.DAYS.between(res.getCheckInDate(),res.getCheckOutDate());
            g.drawString("Room rate",30,y); g.drawString(nights+" night(s) × "+formatCurrency(res.getRoom().getPricePerNight()),width/2,y); y+=20;
            g.drawLine(30,y,width-30,y); y+=22; g.setFont(new Font("Segoe UI",Font.BOLD,14)); g.drawString("TOTAL",30,y); g.drawString(formatCurrency(res.getTotalAmount()),width-150,y); y+=25;
            g.setFont(new Font("Segoe UI",Font.PLAIN,11)); g.drawString("Payment: "+(payment==null?"PENDING":payment.getPaymentStatus()+" / "+payment.getPaymentMethod()),30,y); y+=35;
            g.drawLine(20,y,width-20,y); y+=20; g.setFont(new Font("Segoe UI",Font.PLAIN,9)); g.drawString("Thank you for choosing ELITE STAY HOTEL.",30,y); g.drawString("Computer-generated receipt • No signature required",30,y+14);
            return PAGE_EXISTS;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {}
            new HotelReservationGUI().setVisible(true);
        });
    }

    // ====================================================================================================
    // CUSTOM UI COMPONENTS
    // ====================================================================================================

    class ModernButton extends JButton {
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

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

            if (!isPrimary) {
                g2.setColor(COLOR_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            }

            g2.dispose();
            super.paintComponent(g);

            setForeground(isPrimary ? Color.WHITE : COLOR_TEXT_MAIN);
        }
    }

    class ModernCard extends JPanel {
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

            g2.setColor(new Color(0, 0, 0, 20));
            g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 15, 15);

            g2.setColor(COLOR_CARD);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

            g2.setColor(COLOR_BORDER);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

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

    class MetricCard extends ModernCard {
        public MetricCard(String label, JLabel valueLabel, String subtext, UIIcons.IconType icon) {
            super(null);
            setLayout(new BorderLayout(0, 8));
            setBorder(new EmptyBorder(18, 18, 16, 18));

            JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
            top.setOpaque(false);
            JPanel iconPanel = new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    UIIcons.drawIcon((Graphics2D) g, icon, 20, 20, COLOR_PRIMARY);
                }
            };
            iconPanel.setPreferredSize(new Dimension(24, 24));
            iconPanel.setOpaque(false);
            JLabel lbl = new JLabel(label);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lbl.setForeground(COLOR_TEXT_SEC);
            top.add(iconPanel);
            top.add(lbl);

            valueLabel.setFont(FONT_KPI);
            valueLabel.setForeground(COLOR_TEXT_MAIN);
            valueLabel.setBorder(new EmptyBorder(4, 0, 0, 0));

            JLabel sub = new JLabel(subtext);
            sub.setFont(FONT_SMALL);
            sub.setForeground(COLOR_TEXT_SEC);

            add(top, BorderLayout.NORTH);
            add(valueLabel, BorderLayout.CENTER);
            add(sub, BorderLayout.SOUTH);
        }
    }

    class ModernSidebarItem extends JButton {
        private UIIcons.IconType icon;
        private boolean active = false;

        public ModernSidebarItem(String text, UIIcons.IconType icon) {
            super(text);
            this.icon = icon;
            setMaximumSize(new Dimension(230, 46));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setFont(FONT_BODY);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        public void setActive(boolean active) {
            this.active = active;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (active) {
                g2.setColor(COLOR_PRIMARY);
                g2.fillRoundRect(15, 0, getWidth() - 30, getHeight(), 10, 10);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, 4, getHeight(), 2, 2);
            } else {
                g2.setColor(COLOR_SIDEBAR);
            }

            g2.dispose();
            super.paintComponent(g);

            Graphics2D gIcon = (Graphics2D) g.create();
            gIcon.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gIcon.setColor(active ? Color.WHITE : COLOR_TEXT_SEC);
            UIIcons.drawIcon(gIcon, icon, 20, 20, gIcon.getColor());
            gIcon.dispose();

            setForeground(active ? Color.WHITE : new Color(180, 180, 180));
        }
    }

    class StatusBadge extends JLabel {
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
            g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.setColor(color);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            g2.dispose();
            setForeground(color);
            super.paintComponent(g);
        }
    }

    class EmptyStatePanel extends JPanel {
        public EmptyStatePanel(String title, String desc) {
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
        }
    }
}
