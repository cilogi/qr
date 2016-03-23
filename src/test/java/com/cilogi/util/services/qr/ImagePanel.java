package com.cilogi.util.services.qr;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private BufferedImage image;
    private int imageWidth;
    private int imageHeight;

    public ImagePanel() {}

    public ImagePanel(BufferedImage image) {
        set(image);
    }

    public ImagePanel(BufferedImage image, int width, int height) {
        set(image, width, height);
    }

    public void set(BufferedImage image) {
        set(image, image.getWidth(), image.getHeight());
    }

    public void set(BufferedImage image, int width, int height) {
        this.image = image;
        this.imageWidth = width;
        this.imageHeight = height;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(imageWidth, imageHeight);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
}
