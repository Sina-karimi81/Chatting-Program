import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame {
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket socket;
    private String IP;

    public Client(String hostIP)
    {
        super("Client");
        IP = hostIP;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        showMessage(e.getActionCommand());
                        userText.setText(" ");
                    }
                }
        );
        add(userText , BorderLayout.SOUTH);
        chatWindow = new JTextArea();
        chatWindow.setEditable(false);
        add(new JScrollPane(chatWindow) , BorderLayout.CENTER);
        setSize(720 , 600);
        setVisible(true);
    }

    public void Run() throws IOException
    {
        try
        {
            while(true)
            {
                Connection();
                Setup();
                Chatting();
            }
        }
        catch(EOFException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeAll();
        }
    }

    private void Connection() throws IOException
    {
        showMessage("Connecting...." + "\n");
        socket = new Socket(InetAddress.getByName(IP) , 4000);
        showMessage("Connected to: " + socket.getInetAddress().getHostName() + "\n");
    }

    private void Setup() throws IOException
    {
        output = new ObjectOutputStream(socket.getOutputStream());
        output.flush();
        input = new ObjectInputStream(socket.getInputStream());
        showMessage("EveryThing is done!" + "\n");
    }

    private void closeAll()
    {
        showMessage("Closing the Connections" + "\n");
        canType(false);
        try
        {
            socket.close();
            input.close();
            output.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void Chatting() throws  IOException
    {
        String message = "Connected\n";
        sendMessage(message);
        canType(true);

        do
        {
            try
            {
                message = (String) input.readObject();
                showMessage(message + "\n");
            }
            catch(IOException | ClassNotFoundException e)
            {
                showMessage("Wrong command!!" + "\n");
                e.printStackTrace();
            }
        }while (!message.equals("Server - End"));
    }

    private void sendMessage(String message)
    {
        try
        {
            output.writeObject("Client: " + message + "\n");
            output.flush();
            showMessage("Client: " + message + "\n");
        }
        catch (IOException e)
        {
            chatWindow.append("Cannot show this message");
            e.printStackTrace();
        }
    }

    private void showMessage(final String message)
    {
        SwingUtilities.invokeLater(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        chatWindow.append(message);
                    }
                });
    }

    private void canType(final boolean permission)
    {
        SwingUtilities.invokeLater(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        userText.setEditable(permission);
                    }
                });
    }
}
