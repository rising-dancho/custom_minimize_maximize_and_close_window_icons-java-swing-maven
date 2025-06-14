package com.demo;

import java.awt.*;
import javax.swing.*;

public class Main extends JFrame {
    static String PACKAGE_NAME = "demo";
    static ImageIcon logo = new ImageIcon(Main.class.getResource("/" + PACKAGE_NAME + "/images/logo/logo.png"));
    static JMenuBar menuBar = new JMenuBar();
    static JMenu fileMenu = new JMenu("File");
    static JMenuItem newItem = new JMenuItem("New");
    static JMenuItem openItem = new JMenuItem("Open");
    static JMenuItem exitItem = new JMenuItem("Exit");

    JPanel container = new JPanel();
    JLabel label1 = new JLabel();

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        // Window setup
        this.setTitle("Demo");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(logo.getImage());
        this.setSize(1080, 720);
        this.setUndecorated(true);
        this.getContentPane().setBackground(new Color(0x1F1F1F));

        // Wrapper panel (acts as root layout manager)
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(new Color(36, 37, 38));

        // ============ TITLE BAR ============
        CustomTitleBar titleBar = new CustomTitleBar(
                this,
                "Demo",
                PACKAGE_NAME,
                "/" + PACKAGE_NAME + "/images/logo/logo_24x24.png",
                new Color(36, 37, 38),
                40);
        wrapper.add(titleBar);

        // ============ MENU BAR ============
        menuBar.setBackground(new Color(36, 37, 38));
        menuBar.setBorder(BorderFactory.createEmptyBorder());
        menuBar.setForeground(new Color(36, 37, 38));

        // Menu panel wraps the menu bar so it stretches fully
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBackground(new Color(36, 37, 38));
        menuPanel.add(menuBar, BorderLayout.CENTER);

        // Add "File" menu and items
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // REMOVE ALL BORDER FROM THE MENU (Flat design)
        fileMenu.setBorder(BorderFactory.createEmptyBorder());
        newItem.setBorder(BorderFactory.createEmptyBorder());
        openItem.setBorder(BorderFactory.createEmptyBorder());
        exitItem.setBorder(BorderFactory.createEmptyBorder());

        // SET THE FONT COLOR OF THE MENU
        fileMenu.setForeground(new Color(157, 157, 157));
        newItem.setForeground(new Color(36, 37, 38));
        openItem.setForeground(new Color(36, 37, 38));
        exitItem.setForeground(new Color(36, 37, 38));

        menuBar.add(fileMenu);

        // Set fixed height for the menu panel
        menuPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 30));
        menuPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); // restrict vertical size
        menuPanel.setMinimumSize(new Dimension(0, 30));
        wrapper.add(menuPanel);

        // ============ MAIN CONTENT ============
        container.setBackground(new Color(0x1F1F1F));
        label1.setText("Welcome To The Homepage!");
        label1.setForeground(new Color(0xEEEEEE));
        container.add(label1);
        wrapper.add(container);

        // Set wrapper as content pane
        this.setContentPane(wrapper);

        // Center the window
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int xPos = (dim.width - this.getWidth()) / 2;
        int yPos = (dim.height - this.getHeight()) / 2;
        this.setLocation(xPos, yPos);

        // Enable resizing
        ComponentResizer cr = new ComponentResizer();
        cr.registerComponent(this);

        // Show the window
        this.setVisible(true);
    }
}
