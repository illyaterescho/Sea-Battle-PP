package org.seabattlepp.gui;

import javax.swing.border.AbstractBorder;
import java.awt.*;

import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Insets;

public class RoundBorder extends AbstractBorder {
    public final Color color;        // Колір рамки
    public final int thickness;     // Товщина лінії рамки
    public final int arcRadius;     // Радіус округлення кутів

    public RoundBorder(Color color, int thickness, int arcRadius) {
        this.color = color;
        this.thickness = thickness;
        this.arcRadius = arcRadius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int adjustedX = x + thickness / 2;
        int adjustedY = y + thickness / 2;
        int adjustedWidth = width - thickness;
        int adjustedHeight = height - thickness;

        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(thickness));

        g2d.drawRoundRect(adjustedX, adjustedY, adjustedWidth, adjustedHeight, arcRadius, arcRadius);
        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
    }
}
