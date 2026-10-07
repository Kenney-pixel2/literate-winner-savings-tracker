package cloud.cloudberry.savingstracker;

import com.formdev.flatlaf.FlatLightLaf;
import cloud.cloudberry.savingstracker.ui.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
