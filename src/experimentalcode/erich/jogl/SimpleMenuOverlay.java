package experimentalcode.erich.jogl;

/*
 This file is part of ELKI:
 Environment for Developing KDD-Applications Supported by Index-Structures

 Copyright (C) 2013
 Ludwig-Maximilians-Universität München
 Lehr- und Forschungseinheit für Datenbanksysteme
 ELKI Development Team

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.awt.TextRenderer;

/**
 * Simple menu overlay.
 * 
 * TODO: Hover effects?
 * 
 * @author Erich Schubert
 */
public abstract class SimpleMenuOverlay extends AbstractSimpleOverlay implements MouseListener {
  /**
   * Text renderer
   */
  TextRenderer renderer;

  /**
   * Options to display.
   */
  ArrayList<String> options = new ArrayList<>();

  /**
   * Font size.
   */
  int fontsize;

  /**
   * Constructor.
   */
  public SimpleMenuOverlay() {
    super();
    fontsize = 18;
    renderer = new TextRenderer(new Font(Font.SANS_SERIF, Font.PLAIN, fontsize));
  }

  @Override
  void renderContents(GL2 gl) {
    final int numopt = options.size();

    double maxwidth = 0.;
    Rectangle2D[] bounds = new Rectangle2D[numopt];
    for (int i = 0; i < numopt; i++) {
      bounds[i] = renderer.getBounds(options.get(i));
      maxwidth = Math.max(bounds[i].getWidth(), maxwidth);
    }
    final double padding = .5 * fontsize;
    final double margin = padding * .3;
    final float bx1 = (float) (.5 * (width - maxwidth - padding));
    final float bx2 = (float) (.5 * (width + maxwidth + padding));
    double totalheight = numopt * fontsize + (numopt - 1) * padding;

    // Render background buttons:
    gl.glBegin(GL2.GL_QUADS);
    gl.glColor4f(0f, 0f, 0f, .75f);
    for (int i = 0; i < numopt; i++) {
      final double pos = (height - totalheight) * .5 + fontsize * i + padding * i;

      // Render a background button:
      gl.glVertex2f(bx1, (float) (pos - margin));
      gl.glVertex2f(bx1, (float) (pos + fontsize + margin));
      gl.glVertex2f(bx2, (float) (pos + fontsize + margin));
      gl.glVertex2f(bx2, (float) (pos - margin));
    }
    gl.glEnd();

    // Render text labels:
    renderer.beginRendering(width, height);
    renderer.setColor(1f, 1f, 1f, 1f);
    // NOTE: renderer uses (0,0) as BOTTOM left!
    for (int j = 0; j < numopt; j++) {
      final int i = numopt - j - 1;
      // Extra offset .17 * fontsize because of text baseline!
      final double pos = (height - totalheight) * .5 + fontsize * i + padding * i + .17 * fontsize;
      renderer.setColor(1f, 1f, 1f, 1f);
      renderer.draw(options.get(j), (width - (int) bounds[j].getWidth()) >> 1, (int) pos);
    }
    renderer.endRendering();
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (e.getButton() != MouseEvent.BUTTON1) {
      return;
    }
    final int mx = e.getX(), my = e.getY();

    final int numopt = options.size();
    double maxwidth = 0.;
    Rectangle2D[] bounds = new Rectangle2D[numopt];
    for (int i = 0; i < numopt; i++) {
      bounds[i] = renderer.getBounds(options.get(i));
      maxwidth = Math.max(bounds[i].getWidth(), maxwidth);
    }
    final double padding = .5 * fontsize;
    final double margin = padding * .3;
    final float bx1 = (float) (.5 * (width - maxwidth - padding));
    final float bx2 = (float) (.5 * (width + maxwidth + padding));
    if (mx < bx1 || mx > bx2) {
      return;
    }

    double totalheight = numopt * fontsize + (numopt - 1) * padding;
    for (int i = 0; i < numopt; i++) {
      final double pos = (height - totalheight) * .5 + fontsize * i + padding * i;
      if (my < pos - margin) {
        return;
      }
      if (my < pos + fontsize + margin) {
        menuItemClicked(i);
        return;
      }
    }
  }

  abstract void menuItemClicked(int item);

  @Override
  public void mousePressed(MouseEvent e) {
    // Ignore
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // Ignore
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // Ignore
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // Ignore
  }
}
