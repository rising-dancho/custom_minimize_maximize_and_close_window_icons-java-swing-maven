package com.demo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TitlebarMover extends MouseAdapter {
  private final JFrame frame;
  private Point mousePressedLocation = null;
  private Point mouseScreenLocation = null;
  private Dimension previousSize = null;
  private boolean isDragging = false;
  private boolean restoredOnDrag = false;

  public TitlebarMover(JFrame frame, Component draggableArea) {
    this.frame = frame;

    draggableArea.addMouseListener(this);
    draggableArea.addMouseMotionListener(this);
  }

  @Override
  public void mousePressed(MouseEvent e) {
    // Save current mouse position and window size
    mousePressedLocation = e.getPoint();
    mouseScreenLocation = e.getLocationOnScreen();

    isDragging = true;
    restoredOnDrag = false;
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    if (!isDragging || mousePressedLocation == null || mouseScreenLocation == null)
      return;

    if ((frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH && !restoredOnDrag) {
      Dimension restoreSize = previousSize != null ? previousSize : new Dimension(1024, 768);

      // Restore frame
      frame.setExtendedState(JFrame.NORMAL);
      frame.setSize(restoreSize);

      // Get cursor location on screen
      int cursorX = e.getXOnScreen();
      int cursorY = e.getYOnScreen();

      // Place window so the cursor lands on the same Y as it did in the title bar
      int newX = cursorX - (restoreSize.width / 2);
      int newY = cursorY - mousePressedLocation.y;

      frame.setLocation(newX, newY);

      // Update mousePressedLocation to continue dragging smoothly
      mousePressedLocation = new Point(restoreSize.width / 2, mousePressedLocation.y);

      restoredOnDrag = true;
      return;
    }

    // Move the window with the cursor
    Point current = e.getLocationOnScreen();
    frame.setLocation(
        current.x - mousePressedLocation.x,
        current.y - mousePressedLocation.y);
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    isDragging = false;
    mousePressedLocation = null;
    mouseScreenLocation = null;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
      if ((frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
        frame.setExtendedState(JFrame.NORMAL);
        if (previousSize != null) {
          frame.setSize(previousSize);
        }
      } else {
        if ((frame.getExtendedState() & JFrame.NORMAL) == JFrame.NORMAL) {
          previousSize = frame.getSize(); // store only if not already maximized
        }
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
      }
    }
  }

}