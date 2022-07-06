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
 * A custom image icon class that ensures the image icon of chess pieces of high quality
 * @author mortimer
 */
public class CustomImageIconBoard extends ImageIcon {
    /**
     * Creates an ImageIcon using a buffered image. This image is then rendered using the highest quality rendering settings
     * @param f The 56 by 56 pixel buffered image.
     */
    public CustomImageIconBoard(BufferedImage f) {
      super(f);
      //sets a high render quality of the ImageIcon
      BufferedImage i= new BufferedImage(56, 56, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2d = (Graphics2D)i.getGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
                g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
      g2d.drawImage(getImage(), 0, 0, 56, 56, null);//draws the fifty six by fifty six ImageIcon
      setImage(i);
    }
    /**
     * Creates a custom image icon that renders an image at the highes possible quality. 
     * @param f the 56 by 56 pixel image
     */
    public CustomImageIconBoard(Image f) {
      super(f);
      //sets a high render quality of the ImageIcon
      BufferedImage i= new BufferedImage(56, 56, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2d = (Graphics2D)i.getGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
                g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
      g2d.drawImage(getImage(), 0, 0, 56, 56, null);
      setImage(i);
    }
    /**
     * Gets the icon's height.
     * @return The icon's height (this will be 56)
     */
    @Override
    public int getIconHeight() {
      return 56;
    }
    /**
     * Gets the icon's width.
     * @return The icon's width (this will be 56)
     */
    @Override
    public int getIconWidth() {
      return 56;
    }
    /**
     * Paints the icon
     * @param c The component
     * @param g The graphics
     * @param x The x value
     * @param y The y value
     */
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
      g.drawImage(getImage(), x, y, c);
    }   
}
