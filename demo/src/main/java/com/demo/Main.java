package com.demo;

import java.awt.*;
import javax.swing.*;

public class Main extends JFrame {

    // ─── Static Resources ──────────────────────────────────────────────────────
    private static final String PACKAGE_NAME = "demo";
    private static final ImageIcon LOGO = new ImageIcon(
            Main.class.getResource("/" + PACKAGE_NAME + "/images/logo/logo.png"));

    private static final JMenuBar menuBar = new JMenuBar();
    private static final JMenu fileMenu = new JMenu("File");
    private static final JMenuItem newItem = new JMenuItem("New");
    private static final JMenuItem openItem = new JMenuItem("Open");
    private static final JMenuItem exitItem = new JMenuItem("Exit");

    // ─── Instance UI Parts ─────────────────────────────────────────────────────
    private final JPanel container = new JPanel();
    private final JLabel label1 = new JLabel("Welcome To The Homepage!");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    public Main() {
        // ── 1. Frame ----------------------------------------------------------------
        setTitle("demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(LOGO.getImage());
        setSize(1080, 720);
        setUndecorated(true);
        getContentPane().setBackground(new Color(0x1F1F1F));
        setSize(1080, 720);

        // 🟢 Initial rounded shape
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

        // 🟢 Listen for resizes to reapply rounded shape
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            }
        });

        // ── 2. Root Wrapper ---------------------------------------------------------
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BorderLayout());
        wrapper.setBackground(new Color(36, 37, 38));
        wrapper.setOpaque(true);

        // ── 3. Custom Title Bar -----------------------------------------------------
        CustomTitleBar titleBar = new CustomTitleBar(
                this,
                "demo",
                PACKAGE_NAME,
                "/" + PACKAGE_NAME + "/images/logo/logo_24x24.png",
                new Color(36, 37, 38),
                40);

        // FOR DEBUGGING PURPOSES ONLY
        // titleBar.setBorder(BorderFactory.createLineBorder(Color.RED));

        // small transparent insets so the resize “hot zone” is reachable
        JPanel titleWrapper = new JPanel(new BorderLayout());
        titleWrapper.setOpaque(false);
        titleWrapper.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        titleWrapper.add(titleBar, BorderLayout.CENTER);

        // ── 4. Menu Bar -------------------------------------------------------------
        menuBar.setBackground(new Color(36, 37, 38));
        menuBar.setBorder(BorderFactory.createEmptyBorder());

        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        fileMenu.setBorder(BorderFactory.createEmptyBorder());
        fileMenu.setForeground(new Color(157, 157, 157));
        menuBar.add(fileMenu);

        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBackground(new Color(36, 37, 38));
        menuPanel.add(menuBar, BorderLayout.CENTER);
        menuPanel.setPreferredSize(new Dimension(0, 30));
        menuPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        menuPanel.setMinimumSize(new Dimension(0, 30));

        // FOR DEBUGGING PURPOSES ONLY
        // menuPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN));

        // ── 5. Main Content Panel ---------------------------------------------------
        container.setBackground(new Color(0x1F1F1F));
        label1.setForeground(new Color(0xEEEEEE));
        container.add(label1);

        // FOR DEBUGGING PURPOSES ONLY
        // container.setBorder(BorderFactory.createLineBorder(Color.BLUE));

        // ── 6. Compose --------------------------------------------------------------
        setContentPane(wrapper);

        // vertical box to hold both menu and container
        JPanel centerContent = new JPanel();

        centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));
        centerContent.setBackground(new Color(36, 37, 38));
        centerContent.add(menuPanel);
        centerContent.add(container);

        // compose final layout
        wrapper.add(titleWrapper, BorderLayout.NORTH);
        wrapper.add(centerContent, BorderLayout.CENTER);

        // ── 7. Center on screen -----------------------------------------------------
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);

        // ── 8. Enable edge resizing -------------------------------------------------
        ComponentResizer resizer = new ComponentResizer();
        resizer.registerComponent(this);

        setVisible(true);
    }
}