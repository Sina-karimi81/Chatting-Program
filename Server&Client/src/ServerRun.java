import javax.swing.JFrame;

public class ServerRun {
    public static void main(String[] args)
    {
        Server Host = new Server();
        Host.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Host.Run();
    }
}
