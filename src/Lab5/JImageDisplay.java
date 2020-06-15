package Lab5;

import javax.swing.JComponent;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

public class JImageDisplay extends JComponent {
    private BufferedImage image;

    /** Инициация нового изображения с типом TYPE_INT_RGB **/
    public JImageDisplay(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        super.setPreferredSize(new Dimension(width, height));
    }
    /** Метод устанавливает пиксель в определенный цвет **/
    public void drawPixel(int x, int y, int rgbColor)
    {
        image.setRGB(x, y, rgbColor);
    }

    /** Метод  устанавливает все пиксели изображения в черный цвет **/
    public void clearImage()
    {
        for(int i = 0; i < image.getHeight(); i++)
            for(int j = 0; i < image.getWidth(); j++)
                image.setRGB(j, i,0);
    }
    public void paintComponent(Graphics g)
    {
        g.drawImage(image,0,0,image.getWidth(), image.getHeight(),null);
    }

    public RenderedImage getBufferedImage() {
        return image;
    }
}