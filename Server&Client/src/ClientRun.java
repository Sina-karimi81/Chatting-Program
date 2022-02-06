import javax.swing.*;
import java.io.IOException;

public class ClientRun {
    public static void main(String[] args) throws IOException {
        MultipleClients mp1 = new MultipleClients();
        MultipleClients mp2 = new MultipleClients();
        MultipleClients mp3 = new MultipleClients();

        mp1.start();
        //mp2.start();
        //mp3.start();
    }
}
