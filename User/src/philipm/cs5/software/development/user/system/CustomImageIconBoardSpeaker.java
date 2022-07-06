/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.user.system;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 * A custom image icon class that ensures the image icon of the speaker is of high quality
 * @author mortimer
 */
public class CustomImageIconBoardSpeaker extends ImageIcon {
    /**
     * Creates an image icon for the speaker which is of high visual quality.
     * @param f The image of the speaker (which must be 15 by 15 pixels).
     */
    public CustomImageIconBoardSpeaker(Image f) {
      super(f);
      //sets a high render quality of the ImageIcon
      BufferedImage i= new BufferedImage(15, 15, BufferedImage.TYPE_INT_ARGB);
      //sets the image to a high rendering quality
      Graphics2D g2d = (Graphics2D)i.getGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
                g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
      g2d.drawImage(getImage(), 0, 0, 15, 15, null);
      setImage(i);
    }
    /**
     * Gets the height of the icon.
     * @return The height of the icon  (which is always 15)
     */
    @Override
    public int getIconHeight() {
      return 15;
    }
    /**
     * Gets the width of the icon.
     * @return The width of the icon  (which is always 15)
     */
    @Override
    public int getIconWidth() {
      return 15;
    }
    /**
     * Paints the icon
     * @param c The component
     * @param g The graphics
     * @param x The x coordinate
     * @param y The y coordinate
     */
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
      g.drawImage(getImage(), x, y, c);
    }   
}
