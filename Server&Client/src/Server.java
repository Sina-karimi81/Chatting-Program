import java.net.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame {
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private ServerSocket server;
    private Socket socket;

    public Server()
    {
        super("Server");
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
         add(new JScrollPane(chatWindow));
         setSize(1280 , 720);
         setVisible(true);
    }

    public void Run()
    {
        try
        {
            server = new ServerSocket(4000 , 20);
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
            finally
            {
                closeAll();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private void Connection() throws IOException
    {
        showMessage("Waiting for a conncetion....");
        socket = server.accept();
        showMessage("Connected to: " + socket.getInetAddress().getHostName());
    }

    private void Setup() throws IOException
    {
        output = new ObjectOutputStream(socket.getOutputStream());
        output.flush();
        input = new ObjectInputStream(socket.getInputStream());
        showMessage("EveryThing is done!");
    }

    private void Chatting() throws  IOException
    {
        String message = "Connected";
        sendMessage(message);
        canType(true);

        do
        {
           try
           {
               message = (String) input.readObject();
           }
           catch(IOException | ClassNotFoundException e)
           {
               showMessage("Wrong command!!");
               e.printStackTrace();
           }
        }while (!message.equals("Client - End"));
    }

    private void closeAll()
    {
        showMessage("Closing the Connections");
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

    private void sendMessage(String message)
    {
        try
        {
            output.writeObject("Server: " + message);
            output.flush();
            showMessage("Server: " + message);
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
