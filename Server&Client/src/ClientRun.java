import javax.swing.*;
import java.io.IOException;

public class ClientRun {
    public static void main(String[] args) throws IOException {
        Client c = new Client("127.0.0.1");
        c.Run();
    }
}
