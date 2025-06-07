package com.demo;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.net.URL;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class Main extends JPanel {
	private ComponentMover componentMover;

	private JPanel header;
	private JButton closeButton;
	private JButton minimizeButton;
	private JButton maximizeButton;
	private JFrame parentFrame;
	private boolean wasMaximizedOnDrag = false;
	private Point initialClickPoint = null;
	private Dimension previousSize = new Dimension(1080, 720); // default window size fallback

	public void setParentFrame(JFrame frame) {
		this.parentFrame = frame;
	}

	Main() {
		componentMover = new ComponentMover(Window.class);

		setLayout(new BorderLayout());

		// Only add the title bar at the top
		JComponent top = createTopPanel();
		add(top, BorderLayout.NORTH);

		// No other components are added!
	}

	private JComponent createTopPanel() {
		JPanel titleBar = new JPanel(new BorderLayout());
		componentMover.registerComponent(titleBar);

		JLabel title = new JLabel("Demo");
		ImageIcon icon = new ImageIcon(getClass().getResource("/demo/images/logo/logo_24x24.png"));
		title.setForeground(new Color(0xEEEEEE));
		// add padding
		title.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		title.setFont(new Font("Segoe UI", Font.BOLD, 16));
		title.setForeground(new Color(0xEEEEEE));
		title.setIcon(icon);
		title.setIconTextGap(10); // spacing between icon and text
		title.setHorizontalTextPosition(SwingConstants.RIGHT); // text to the right of icon
		title.setVerticalTextPosition(SwingConstants.CENTER); // vertical alignments

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
		header.add(title, BorderLayout.LINE_START);
		header.add(iconminmaxclose, BorderLayout.LINE_END);
		titleBar.add(header, BorderLayout.NORTH);
		titleBar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (parentFrame != null && (parentFrame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
					wasMaximizedOnDrag = true;
					initialClickPoint = e.getPoint(); // capture where the user clicked
				} else {
					wasMaximizedOnDrag = false;
					initialClickPoint = null;
				}
			}
		});

		titleBar.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (parentFrame != null && wasMaximizedOnDrag) {
					// Calculate ratio of click point to frame width
					double ratioX = (double) initialClickPoint.x / parentFrame.getWidth();

					// Restore window
					parentFrame.setExtendedState(JFrame.NORMAL);

					// Set new window size to the default window size fallback
					int width = previousSize.width;
					int height = previousSize.height;
					parentFrame.setSize(width, height);

					// Move window to match the cursor's position
					Point mouseScreen = e.getLocationOnScreen();
					int newX = (int) (mouseScreen.x - ratioX * width);
					int newY = mouseScreen.y - initialClickPoint.y;

					parentFrame.setLocation(newX, newY);

					wasMaximizedOnDrag = false;
				}
			}
		});

		// Listeners
		attachControlListeners();

		return titleBar;
	}

	private void attachControlListeners() {
		closeButton.setToolTipText("Close");
		closeButton.addActionListener(e -> System.exit(0));
		closeButton.addMouseListener(new HoverIconAdapter(closeButton,
				"close_def.png", "close_hover.png"));

		minimizeButton.setToolTipText("Minimize");
		minimizeButton.addActionListener(e -> {
			if (parentFrame != null) {
				parentFrame.setState(JFrame.ICONIFIED);
			}
		});
		minimizeButton.addMouseListener(new HoverIconAdapter(minimizeButton,
				"minimize_def.png", "minimize_hover.png"));

		maximizeButton.setToolTipText("Maximize / Restore");
		// Save previous size before maximizing (optional, improve behavior)
		maximizeButton.addActionListener(e -> {
			if (parentFrame != null) {
				if ((parentFrame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
					parentFrame.setExtendedState(JFrame.NORMAL);
				} else {
					previousSize = parentFrame.getSize();
					parentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				}
				updateMaximizeButtonIcon();
			}
		});
		maximizeButton.addMouseListener(new HoverIconAdapter(maximizeButton,
				"maximize_def.png", "maximize_hover.png"));

		updateMaximizeButtonIcon(); // set correct icon on startup
	}

	private void updateMaximizeButtonIcon() {
		if (parentFrame == null)
			return;

		boolean isMaximized = (parentFrame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH;
		String defIcon = isMaximized ? "collapse_def.png" : "maximize_def.png";
		String hoverIcon = isMaximized ? "collapse_hover.png" : "maximize_hover.png";

		maximizeButton.setIcon(new ImageIcon(getClass().getResource("/demo/images/" + defIcon)));

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
		SwingUtilities.invokeLater(Main::createAndShowGUI);
	}

	public static void createAndShowGUI() {
		JFrame frame = new JFrame();
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1080, 720);

		Main demo = new Main();
		demo.setParentFrame(frame);

		// Add invisible padding around the main panel for resize detection
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 3)); // Acts like visible resize margin
		wrapper.setBackground(new Color(36, 37, 38));
		wrapper.add(demo, BorderLayout.CENTER);

		frame.setContentPane(wrapper);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		ComponentResizer cr = new ComponentResizer();
		cr.registerComponent(frame);
	}

}
