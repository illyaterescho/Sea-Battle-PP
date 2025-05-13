package org.seabattlepp.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonPanel extends JPanel {

    private JButton startButton;
    private JButton resetButton;
    private JButton exitButton;

    public ButtonPanel() {
        createButtonPanel();
    }
    public void createButtonPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel resetPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 100, 0));
        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 100, 0));
        startPanel.setOpaque(false);
        resetPanel.setOpaque(false);
        exitPanel.setOpaque(false);
        startButton = new RoundedButton("Почати Гру", new Color(0x33CC66), new Color(0x388E3C));
        resetButton = new RoundedButton("Скинути", new Color(0x699BF7), new Color(0x3366CC));
        exitButton = new RoundedButton("Вийти", new Color(0xFF8577), new Color(0xD32F2F));
        startButton.setPreferredSize(new Dimension(750, 55));
        resetButton.setPreferredSize(new Dimension(200, 55));
        exitButton.setPreferredSize(new Dimension(200, 55));
        startPanel.add(startButton);
        resetPanel.add(resetButton);
        exitPanel.add(exitButton);
        add(startPanel, BorderLayout.CENTER);
        add(resetPanel, BorderLayout.WEST);
        add(exitPanel, BorderLayout.EAST);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getResetButton() {
        return resetButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}