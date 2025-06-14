package com.demo;

import java.awt.*;
import javax.swing.*;

public class Main extends JPanel {
	static private int windowWidth = 1024;
	static private int windowHeight = 768;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(Main::createAndShowGUI);
	}

	public static void createAndShowGUI() {
		JFrame frame = new JFrame();
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(windowWidth, windowHeight);

		// Create main content panel
		Main demo = new Main();

		// Create wrapper panel with BorderLayout
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		wrapper.setBackground(new Color(36, 37, 38));

		// Create and add the custom title bar
		CustomTitleBar titleBar = new CustomTitleBar(
				frame,
				"Demo",
				"/demo/images/logo/logo_24x24.png", // Make sure this path is valid!
				new Color(36, 37, 38),
				40);
		wrapper.add(titleBar, BorderLayout.NORTH);

		// Add main content
		wrapper.add(demo, BorderLayout.CENTER);

		// Set wrapper as the content pane
		frame.setContentPane(wrapper);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setMinimumSize(new Dimension(475, 300));

		ComponentResizer cr = new ComponentResizer();
		cr.registerComponent(frame);
	}
}
