package org.seabattlepp.gui;

import javax.swing.*;
import java.awt.*;

public class ShipButton extends JButton {
    public ShipButton() {
        setPreferredSize(new Dimension(50, 50));
        setContentAreaFilled(false);
        setOpaque(false);
        setBackground(Color.WHITE);
        setBorder(new RoundBorder(Color.BLACK, 4, 45));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 45, 45);
        super.paintComponent(g);
        g2d.dispose();
    }
}
//