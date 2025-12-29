import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Add Guest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);

        // Show your AddGuestPanel
        frame.setContentPane(new AddGuestM());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

