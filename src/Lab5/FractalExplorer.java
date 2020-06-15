package Lab5;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.BorderLayout;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


public class FractalExplorer {
    private int dispSize;
    private JImageDisplay image;
    private FractalGenerator fGen;
    private Rectangle2D.Double range;
    JComboBox box;

    public static void main(String[] args) {
        FractalExplorer fracExp = new FractalExplorer(800);
        fracExp.createAndShowGUI();
        fracExp.drawFractal();
    }

    /**
     * Конструктор FractalExplorer
     **/
    public FractalExplorer(int dispSize) {
        this.dispSize = dispSize;
        this.fGen = new Mandelbrot();
        //this.fGen = new Tricorn();
        //this.fGen = new BurningShip();
        this.range = new Rectangle2D.Double(0, 0, 0, 0);
        fGen.getInitialRange(this.range);
    }

    /**
     * Инициализация графического интерфейса Swing
     **/
    public void createAndShowGUI() {
        JFrame frame = new JFrame("Fractal");
        JButton resetBtn = new JButton("Reset Display");
        resetBtn.setActionCommand("Reset");
        image = new JImageDisplay(dispSize, dispSize);

        JButton saveBtn = new JButton("Save Image");
        saveBtn.setActionCommand("Save");

        box = new JComboBox();
        box.addItem(new Mandelbrot());
        box.addItem(new Tricorn());
        box.addItem(new BurningShip());
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JLabel label = new JLabel("Fractal:");


        ActionHandler actHand = new ActionHandler();
        MouseHandler mouseHand = new MouseHandler();
        resetBtn.addActionListener(actHand);
        image.addMouseListener(mouseHand);

        saveBtn.addActionListener(actHand);
        panel1.add(label, java.awt.BorderLayout.CENTER);
        panel1.add(box, java.awt.BorderLayout.CENTER);
        panel2.add(resetBtn, java.awt.BorderLayout.CENTER);
        panel2.add(saveBtn, java.awt.BorderLayout.CENTER);
        box.addActionListener(actHand);
        frame.setLayout(new BorderLayout());

        frame.add(image, BorderLayout.CENTER);

        frame.add(panel1, BorderLayout.NORTH);
        frame.add(panel2, BorderLayout.SOUTH);

        //frame.add(resetBtn, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    /**
     * Метод для вывода фрактала
     **/
    private void drawFractal() {
        for (int x = 0; x < dispSize; x++) {
            for (int y = 0; y < dispSize; y++) {
                int nIter = fGen.numIterations(FractalGenerator.getCoord(range.x, range.x +
                        range.width, dispSize, x), FractalGenerator.getCoord(range.y, range.y +
                        range.width, dispSize, y));
                if (nIter == -1) image.drawPixel(x, y, 0);
                else {
                    double hue = 0.7f + nIter / 200f;
                    int rgbColor = Color.HSBtoRGB((float) hue, 1f, 1f);
                    image.drawPixel(x, y, rgbColor);
                }
            }
        }
        image.repaint();
    }

    /** Метод для обработки событий **/
    public class ActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Reset")) {
             fGen.getInitialRange(range);
                drawFractal();
            }
            else if (e.getActionCommand().equals("Save")) {

                JFileChooser chooser = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");

                chooser.setFileFilter(filter);
                chooser.setAcceptAllFileFilterUsed(false);

                int im = chooser.showSaveDialog(image);
                if (im == JFileChooser.APPROVE_OPTION) {
                    try {
                        ImageIO.write(image.getBufferedImage(), "png", chooser.getSelectedFile());
                    }
                    catch (NullPointerException | IOException err) {
                        JOptionPane.showMessageDialog(image, err.getMessage(), "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            else {
                fGen = (FractalGenerator) box.getSelectedItem();
                range = new Rectangle2D.Double(0, 0, 0, 0);
                fGen.getInitialRange(range);
                drawFractal();
            }
        }
    }

    /**
     * Обработка событий от мыши
     **/
    public class MouseHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, dispSize, x);
            int y = e.getY();
            double yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, dispSize, y);
            fGen.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }


}