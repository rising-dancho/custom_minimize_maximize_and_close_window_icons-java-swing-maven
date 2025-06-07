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
		setTitle("Demo");
		setUndecorated(true);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initComponents();

		// Listen for window state changes (maximize/restore)
		addWindowStateListener(e -> updateMaximizeButtonIcon());
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

		minimizeButton.setIcon(new ImageIcon(getClass().getResource("/demo/images/minimize_def.png")));
		maximizeButton.setIcon(new ImageIcon(getClass().getResource("/demo/images/maximize_def.png")));
		closeButton.setIcon(new ImageIcon(getClass().getResource("/demo/images/close_def.png")));

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
				"close_def.png", "close_hover.png"));

		minimizeButton.setToolTipText("Minimize");
		minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED));
		minimizeButton.addMouseListener(new HoverIconAdapter(minimizeButton,
				"minimize_def.png", "minimize_hover.png"));

		maximizeButton.setToolTipText("Maximize / Restore");
		// Add a default dummy listener before setting up hover behavior
		maximizeButton.addActionListener(e -> {
			if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
				setExtendedState(JFrame.NORMAL);
			} else {
				setExtendedState(JFrame.MAXIMIZED_BOTH);
			}
		});
		maximizeButton.addMouseListener(new HoverIconAdapter(maximizeButton,
				"maximize_def.png", "maximize_hover.png"));

		updateMaximizeButtonIcon(); // set correct icon on startup
	}

	private void updateMaximizeButtonIcon() {

		boolean isMaximized = (getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH;
		String defIcon = isMaximized ? "collapse_def.png" : "maximize_def.png";
		String hoverIcon = isMaximized ? "collapse_hover.png" : "maximize_hover.png";

		maximizeButton.setIcon(new ImageIcon(getClass().getResource("/demo/images/" + defIcon)));

		// Remove previous hover listeners to prevent stacking
		for (MouseListener l : maximizeButton.getMouseListeners()) {
			if (l instanceof HoverIconAdapter) {

				maximizeButton.removeMouseListener(l);

			}
		}
		maximizeButton.addMouseListener(new HoverIconAdapter(maximizeButton, defIcon, hoverIcon));

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

			if (defaultUrl == null) {
				System.err.println("⚠️ Missing default icon: " + defaultIconName);
			}
			if (hoverUrl == null) {
				System.err.println("⚠️ Missing hover icon: " + hoverIconName);
			}

			defaultIcon = defaultUrl != null ? new ImageIcon(defaultUrl) : new ImageIcon();
			hoverIcon = hoverUrl != null ? new ImageIcon(hoverUrl) : new ImageIcon();
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
			ComponentResizer cr = new ComponentResizer();
			cr.registerComponent(f);
			cr.setMinimumSize(new Dimension(400, 300)); // optional
			cr.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize()); // optional
			cr.setSnapSize(new Dimension(20, 20)); // optional
		});
	}
}
