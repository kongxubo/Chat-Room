package Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

//1.Define the components in the JFrame form
//2.Initializes the components of the form in the constructor total
//3.Data transfer using network programming (TCP protocol)
//4.Send 'button' listener click event
//5.Save the listening event for the chat log 'button'
public class PointToPointA extends JFrame implements ActionListener {
    public static void main(String[] args){

        //Call the constructor
        new PointToPointA();
    }

    //attribute
    private JPanel jPanel1;
    private JScrollPane jScrollPane;
    private JPanel jPanel2;
    private JTextArea jTextArea;
    private JButton jButton;
    private BufferedWriter bufferedWriter = null;
    private FileWriter fileWriter = null;

    //construction method
    public PointToPointA(){
        jPanel1 = new JPanel();
        jScrollPane = new JScrollPane(jPanel1);
        jPanel2 = new JPanel();
        jPanel2.setPreferredSize(new Dimension(500,100));
        jTextArea = new JTextArea();
        jTextArea.setPreferredSize(new Dimension(400,100));
        jTextArea.setLineWrap(true);
        jButton = new JButton("发送");
        jPanel2.add(jTextArea);
        jPanel2.add(jButton);

        this.add(jScrollPane, BorderLayout.CENTER);
        jPanel1.setLayout(new GridLayout(100,1));
        this.add(jPanel2, BorderLayout.SOUTH);

        this.setTitle("PointToPointA");
        this.setSize(500,500);
        this.setLocation(300,300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        jButton.addActionListener(this);
        try {
            //Create a socket (attempt to connect)
            ServerSocket serverSocket = new ServerSocket(8888);


            Socket socket = serverSocket.accept();

            //Gets the input stream for the Socket channel
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            //Gets the output stream of the Socket channel
            OutputStream outputStream = socket.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));


            //Loop through the data and concatenate it into a textarea
            String line = null;
            while((line = bufferedReader.readLine()) != null){
                JLabel jLabel = new JLabel();
                jLabel.setText(line);
                jPanel1.add(jLabel);

            }

            //Close the Socket channel
            bufferedWriter.close();
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File file = new File("E:\\improve\\idea\\PointToPoint\\src\\Point\\data.txt");
        try {
            fileWriter = new FileWriter(file, true);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        PrintWriter printWriter = new PrintWriter(fileWriter);

        //To get the time
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        //Gets the contents of the text box
        String text = jTextArea.getText();
        send(text);
        show(text);

        //Write the chat record into a document
        printWriter.println("     A : " + text + "  " +format.format(date));
        printWriter.flush();
        try {
            fileWriter.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void send(String string){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        //Splicing data
        JLabel tempLabel = new JLabel(format.format(date) + "   " +string + "  : A     ");
        tempLabel.setHorizontalAlignment(JLabel.RIGHT);
        jPanel1.add(tempLabel);
        tempLabel.revalidate();
    }

    public void show(String string){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        //Splicing data
        String text ="     A ： " + string + "   " + format.format(date);
        try {
            bufferedWriter.write(text);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            jTextArea.setText("");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
