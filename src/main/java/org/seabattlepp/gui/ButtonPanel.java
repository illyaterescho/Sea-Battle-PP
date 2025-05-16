package org.seabattlepp.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// верхня панель з 3 кнопок
public class ButtonPanel extends JPanel {
    private JButton resetButton;
    private JButton startButton;
    private JButton exitButton;

    public ButtonPanel() {
        createButtonPanel();
    }

    public void createButtonPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);

        // Панелі для кнопок з різним вирівнюванням
        JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel resetPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 100, 0));
        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 100, 0));

        // Прозорість внутрішніх панелей
        startPanel.setOpaque(false);
        resetPanel.setOpaque(false);
        exitPanel.setOpaque(false);

        // Ініціалізація кнопок із кастомними кольорами
        startButton = new RoundedButton("Почати Гру", new Color(0x33CC66), new Color(0x388E3C));
        resetButton = new RoundedButton("Скинути", new Color(0x699BF7), new Color(0x3366CC));
        exitButton = new RoundedButton("Вийти", new Color(0xFF8577), new Color(0xD32F2F));

        // Розміри кнопок
        startButton.setPreferredSize(new Dimension(750, 55));
        resetButton.setPreferredSize(new Dimension(200, 55));
        exitButton.setPreferredSize(new Dimension(200, 55));

        // Додаємо кнопки до відповідних панелей
        startPanel.add(startButton);
        resetPanel.add(resetButton);
        exitPanel.add(exitButton);

        // Додаємо внутрішні панелі до основної
        add(resetPanel, BorderLayout.WEST);
        add(startPanel, BorderLayout.CENTER);
        add(exitPanel, BorderLayout.EAST);

        // Обробник події для кнопки виходу
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }


    // Геттери для доступу до кнопок з інших класів
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
