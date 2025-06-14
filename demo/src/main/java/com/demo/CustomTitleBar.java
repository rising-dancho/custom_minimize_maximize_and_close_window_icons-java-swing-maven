package com.demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CustomTitleBar extends JPanel {
  private final JFrame frame;
  private final JLabel titleLabel;
  private final JButton closeButton;
  private final JButton minimizeButton;
  private final JButton maximizeButton;
  private final JPanel controlPanel;

  private Dimension previousSize;

  public CustomTitleBar(JFrame frame, String title, String iconPath, Color background, int height) {
    this.frame = frame;
    this.previousSize = frame.getSize();

    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(300, height));
    setBackground(background);

    // Title with optional icon
    titleLabel = new JLabel(title);
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
    titleLabel.setForeground(Color.WHITE);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

    if (iconPath != null) {
      ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
      titleLabel.setIcon(icon);
      titleLabel.setIconTextGap(10);
    }

    // Button Panel
    controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));
    controlPanel.setOpaque(false);

    minimizeButton = createButton("minimize_def.png", "minimize_hover.png", e -> frame.setState(JFrame.ICONIFIED));
    maximizeButton = createButton("maximize_def.png", "maximize_hover.png", e -> toggleMaximize());
    closeButton = createButton("close_def.png", "close_hover.png", e -> System.exit(0));

    controlPanel.add(minimizeButton);
    controlPanel.add(maximizeButton);
    controlPanel.add(closeButton);

    add(titleLabel, BorderLayout.WEST);
    add(controlPanel, BorderLayout.EAST);

    // Enable dragging
    new TitlebarMover(frame, this);
  }

  private JButton createButton(String defIcon, String hoverIcon, ActionListener action) {
    JButton button = new JButton(new ImageIcon(getClass().getResource("/demo/images/" + defIcon)));
    button.setPreferredSize(new Dimension(30, 30));
    button.setBorderPainted(false);
    button.setFocusPainted(false);
    button.setContentAreaFilled(false);
    button.setToolTipText(defIcon.split("_")[0].substring(0, 1).toUpperCase() + defIcon.split("_")[0].substring(1));

    button.addActionListener(action);
    button.addMouseListener(new MouseAdapter() {
      @Override public void mouseEntered(MouseEvent e) {
        button.setIcon(new ImageIcon(getClass().getResource("/demo/images/" + hoverIcon)));
      }
      @Override public void mouseExited(MouseEvent e) {
        button.setIcon(new ImageIcon(getClass().getResource("/demo/images/" + defIcon)));
      }
    });

    return button;
  }

  private void toggleMaximize() {
    if ((frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
      frame.setExtendedState(JFrame.NORMAL);
      frame.setSize(previousSize);
    } else {
      previousSize = frame.getSize();
      frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    updateMaximizeIcon();
  }

  private void updateMaximizeIcon() {
    boolean maximized = (frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH;
    String def = maximized ? "collapse_def.png" : "maximize_def.png";
    String hover = maximized ? "collapse_hover.png" : "maximize_hover.png";
    maximizeButton.setIcon(new ImageIcon(getClass().getResource("/demo/images/" + def)));

    for (MouseListener l : maximizeButton.getMouseListeners()) {
      if (l instanceof MouseAdapter) maximizeButton.removeMouseListener(l);
    }
    maximizeButton.addMouseListener(new MouseAdapter() {
      @Override public void mouseEntered(MouseEvent e) {
        maximizeButton.setIcon(new ImageIcon(getClass().getResource("/demo/images/" + hover)));
      }
      @Override public void mouseExited(MouseEvent e) {
        maximizeButton.setIcon(new ImageIcon(getClass().getResource("/demo/images/" + def)));
      }
    });
  }
}
