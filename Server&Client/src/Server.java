import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.print.DocFlavor;
import javax.swing.*;

public class Server extends JFrame {
    private JTextField userText;
    private JTextArea chatWindow;
    private OutputStreamWriter output;
    private InputStreamReader input;
    private BufferedWriter writer;
    private BufferedReader reader;
    private ServerSocket server;
    private Socket socket;

    public Server(){
        super("Server");
        userText = new JTextField();
        userText.setEditable(false);
         userText.addActionListener(
                 new ActionListener() {
                     @Override
                     public void actionPerformed(ActionEvent e)
                     {
                         sendMessage(e.getActionCommand());
                         userText.setText(" ");
                     }
                 }
         );
         add(userText , BorderLayout.SOUTH);
         chatWindow = new JTextArea();
         chatWindow.setEditable(false);
         add(new JScrollPane(chatWindow));
         setSize(720 , 600);
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
        showMessage("Waiting for a connection...." + "\n");
        socket = server.accept();
        if(socket.isConnected())
        {
            showMessage("Connected to: " + socket.getInetAddress().getHostName() + "\n");
        }
    }

    private void Setup() throws IOException
    {
        output = new OutputStreamWriter(socket.getOutputStream());
        writer = new BufferedWriter(output);
        output.flush();
        writer.flush();
        input = new InputStreamReader(socket.getInputStream());
        reader = new BufferedReader(input);
        showMessage("EveryThing is done!" + "\n");
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
               message = reader.readLine();
               showMessage(message + "\n");
           }
           catch(IOException e)
           {
               showMessage("Wrong command or Connection closed!!!" + "\n");
               e.printStackTrace();
           }
        }while (!message.equalsIgnoreCase("Client: End"));
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

    private void sendMessage(String message)
    {
        try
        {
            writer.write("Server: " + message + "\n");
            writer.flush();
            showMessage("Server: " + message + "\n");
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
