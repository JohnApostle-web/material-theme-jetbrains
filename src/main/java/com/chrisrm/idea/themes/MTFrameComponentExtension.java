package com.chrisrm.idea.themes;

import com.intellij.ide.ui.UISettings;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.IdeRootPaneNorthExtension;
import com.intellij.util.ui.JBInsets;
import com.intellij.util.ui.JBSwingUtilities;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

public class MTFrameComponentExtension extends IdeRootPaneNorthExtension {
  private JComponent myWrapperPanel;
  private Project myProject;
  private JPanel myBar;

  public MTFrameComponentExtension(Project project) {
    myProject = project;
    Disposer.register(myProject, this);
  }

  @Override
  public String getKey() {
    return "MTFrame";
  }

  @Override
  public JComponent getComponent() {
    if (myWrapperPanel == null) {
      myWrapperPanel = new MTWrapperPanel(new BorderLayout()) {
        @Override
        public Insets getInsets() {
          return new JBInsets(0, 0, 0, 0);
        }

        @Override
        protected void paintComponent(Graphics g) {
          super.paintComponent(g);
        }

      };
      myWrapperPanel.add(buildMTPanel(), BorderLayout.CENTER);
    }
    return myWrapperPanel;
  }

  @Override
  public void uiSettingsChanged(UISettings settings) {

  }

  @Override
  public IdeRootPaneNorthExtension copy() {
    return new MTFrameComponentExtension(myProject);
  }

  @Override
  public void dispose() {
    myWrapperPanel = null;
    myProject = null;
    myBar = null;
  }

  private Component buildMTPanel() {
    myBar = new JPanel(true);
    myWrapperPanel.putClientProperty("MTBarPanel", myBar);

    JPanel panel = new JPanel(new BorderLayout()) {
      @Override
      public void doLayout() {
        // align vertically
        final Rectangle r = getBounds();
        final Insets insets = getInsets();
        int x = insets.left;

        final Component navBar = myBar;

        navBar.setBounds(x, insets.top, r.width, r.height);
      }

      @Override
      public void updateUI() {
        super.updateUI();
        setOpaque(true);

        myBar.setOpaque(false);
        myBar.setBorder(null);
      }

      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Component navBar = myBar;
        Insets insets = getInsets();
        Rectangle r = navBar.getBounds();

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(r.x, r.y);

        Rectangle rectangle = new Rectangle(0, 0, r.width + insets.left + insets.right, r.height + insets.top + insets.bottom);
        //        g.setColor(getBackgroundColor());
        g.setColor(UIManager.getColor("ToolBar.background"));
        g.fillRect(0, 0, r.width, r.height * JBUI.scale(2));
        g2d.dispose();
      }
    };

    panel.add(myBar, BorderLayout.CENTER);
    panel.updateUI();
    return panel;
  }

  private class MTWrapperPanel extends JPanel {
    public MTWrapperPanel(LayoutManager layout) {
      super(layout);
      setName("navbar");
    }

    @Override
    protected Graphics getComponentGraphics(Graphics graphics) {
      return JBSwingUtilities.runGlobalCGTransform(this, super.getComponentGraphics(graphics));
    }
  }
}
