package src;
import java.io.*;
import java.util.*;

public class FileManager {
    private static final String DATA_FILE = "data/hotel_data.dat";

    public static void save(ApplicationData data) throws IOException {
        File directory = new File("data");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(data);
        }
    }

    public static ApplicationData load() throws IOException, ClassNotFoundException {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            return (ApplicationData) ois.readObject();
        }
    }
}
