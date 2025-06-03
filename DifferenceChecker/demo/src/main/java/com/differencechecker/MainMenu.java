package com.differencechecker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class MainMenu extends JFrame {

    private JPanel header;
    private JButton closeButton;
    private JButton minimizeButton;
    private JButton maximizeButton;

    private Point initialClick;

    public MainMenu() {
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
        header.setBackground(Color.DARK_GRAY);
        header.setPreferredSize(new Dimension(0, 30));
        header.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));

        // Button size and style
        Dimension btnSize = new Dimension(30, 30);
        closeButton = new JButton(new ImageIcon(getClass().getResource("/differencechecker/images/close.png")));
        minimizeButton = new JButton(new ImageIcon(getClass().getResource("/differencechecker/images/minimize.png")));
        maximizeButton = new JButton(new ImageIcon(getClass().getResource("/differencechecker/images/maximize.png")));

        JButton[] buttons = { closeButton, minimizeButton, maximizeButton };
        for (JButton btn : buttons) {
            btn.setPreferredSize(btnSize);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setContentAreaFilled(false);
        }

        // Add buttons
        header.add(minimizeButton);
        header.add(maximizeButton);
        header.add(closeButton);

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

            URL defaultUrl = getClass().getResource("/differencechecker/images/" + defaultIconName);
            URL hoverUrl = getClass().getResource("/differencechecker/images/" + hoverIconName);

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
            MainMenu f = new MainMenu();
            f.setVisible(true);
            // new ComponentResizer().registerComponent(f);
        });
    }
}
