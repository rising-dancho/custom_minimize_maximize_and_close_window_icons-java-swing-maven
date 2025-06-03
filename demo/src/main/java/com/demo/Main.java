package com.demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class Main extends JFrame {

    private JPanel header;
    private JButton closeButton;
    private JButton minimizeButton;
    private JButton maximizeButton;
    private Point initialClick;

    public Main() {
        setTitle("Difference Checker");
        setUndecorated(true);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        // Header
        header = new JPanel();
        header.setBackground(new java.awt.Color(36, 37, 38));
        header.setMinimumSize(new java.awt.Dimension(400, 40));
        header.setPreferredSize(new java.awt.Dimension(300, 40));
        header.setLayout(new BorderLayout());

        JPanel iconminmaxclose = new JPanel();
        iconminmaxclose.setBackground(new java.awt.Color(36, 37, 38));
        iconminmaxclose.setMinimumSize(new java.awt.Dimension(0, 0));

        // Initialize buttons before using them
        minimizeButton = new JButton();
        maximizeButton = new JButton();
        closeButton = new JButton();

        minimizeButton.setIcon(new ImageIcon(getClass().getResource("/demo/images/defaults/minimize_def.png")));
        maximizeButton.setIcon(new ImageIcon(getClass().getResource("/demo/images/defaults/collapse_def.png")));
        closeButton.setIcon(new ImageIcon(getClass().getResource("/demo/images/defaults/close_def.png")));

        // Button size and style
        Dimension btnSize = new Dimension(30, 30);
        JButton[] buttons = { minimizeButton, maximizeButton, closeButton };
        for (JButton btn : buttons) {
            btn.setPreferredSize(btnSize);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setContentAreaFilled(false);
            iconminmaxclose.add(btn);
        }

        // Add the buttons to the header
        header.add(iconminmaxclose, BorderLayout.LINE_END);

        contentPane.add(header, BorderLayout.NORTH);

        // Listeners
        attachDragListener(header);
        attachControlListeners();
    }

    private void attachDragListener(Component dragTarget) {
        dragTarget.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }
        });

        dragTarget.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int thisX = getLocation().x;
                int thisY = getLocation().y;

                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                setLocation(thisX + xMoved, thisY + yMoved);
            }
        });
    }

    private void attachControlListeners() {
        closeButton.setToolTipText("Close");
        closeButton.addActionListener(e -> System.exit(0));
        closeButton.addMouseListener(new HoverIconAdapter(closeButton,
                "close.png", "close.png"));

        minimizeButton.setToolTipText("Minimize");
        minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED));
        minimizeButton.addMouseListener(new HoverIconAdapter(minimizeButton,
                "minimize.png", "minimize.png"));

        maximizeButton.setToolTipText("Maximize / Restore");
        maximizeButton.addActionListener(e -> {
            if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                setExtendedState(JFrame.NORMAL);
            } else {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
        maximizeButton.addMouseListener(new HoverIconAdapter(maximizeButton,
                "maximize.png", "maximize.png"));
    }

    // ✅ Inner class (not a method!)
    private class HoverIconAdapter extends MouseAdapter {
        private final ImageIcon defaultIcon;
        private final ImageIcon hoverIcon;
        private final JButton button;

        public HoverIconAdapter(JButton button, String defaultIconName, String hoverIconName) {
            this.button = button;

            URL defaultUrl = getClass().getResource("/demo/images/" + defaultIconName);
            URL hoverUrl = getClass().getResource("/demo/images/" + hoverIconName);

            if (defaultUrl == null || hoverUrl == null) {
                throw new IllegalArgumentException("Icon not found: " + defaultIconName + " or " + hoverIconName);
            }

            defaultIcon = new ImageIcon(defaultUrl);
            hoverIcon = new ImageIcon(hoverUrl);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            button.setIcon(hoverIcon);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            button.setIcon(defaultIcon);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main f = new Main();
            f.setVisible(true);
            // new ComponentResizer().registerComponent(f);
        });
    }
}
