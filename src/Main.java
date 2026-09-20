package src;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HotelReservationGUI gui = new HotelReservationGUI();
            gui.setVisible(true);
        });
    }
}
