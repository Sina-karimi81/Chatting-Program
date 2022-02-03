import javax.swing.*;
import java.io.IOException;

public class MultipleClients extends Thread {
    @Override
    public void run()
    {
        Client client = new Client("127.0.0.1");
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try
        {
            client.Run();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
