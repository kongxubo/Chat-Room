package chat;

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
public class PTP1 extends JFrame implements ActionListener {
    public static void main(String[] args){

        //Call the constructor
        new PTP1();

    }
    //attribute
    //Text Area
    private JTextArea jta;
    //Scroll
    private JScrollPane jsp;
    //panel
    private JPanel jp;
    //Text Field
    private JTextField jtf;
    //Button
    private JButton jb;

    //Output stream
    private BufferedWriter bw = null;
    //File output stream
    private FileWriter fw = null;


    //construction method
    public PTP1(){
        //Initialize component
        jta = new JTextArea();
        //Sets the text field to be uneditable
        jta.setEditable(false);
        //Note: Need to add the text field to the scroll bar to achieve the scroll effect
        jsp = new JScrollPane(jta);
        //Panel
        jp = new JPanel();
        jtf = new JTextField(10);
        jb = new JButton("发送");
        //Note: Need to add text boxes and buttons to the panel
        jp.add(jtf);
        jp.add(jb);

        //Note: Need to add scrollbars and panels to the form
        this.add(jsp, BorderLayout.CENTER);
        this.add(jp,BorderLayout.SOUTH);

        //Note: Need to set 'Title, Size, Position, Close, Visible'.
        this.setTitle("Kiki-PTP1");
        this.setSize(300,300);
        this.setLocation(300,300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        /******************** TCP PTP1 Start***************************/
        // Bind the send button with a listener click event
        jb.addActionListener(this);
        try {
            //1.Create a PTP2 end socket (attempt to connect)
            ServerSocket serverSocket = new ServerSocket(8888);

            //2.Gets the input stream for the Socket channel
            Socket socket = serverSocket.accept();

            //3.Gets the output stream of the Socket channel
            InputStream in = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            //4.Loop through the data and concatenate it into a text field
            OutputStream out = socket.getOutputStream();
            bw = new BufferedWriter(new OutputStreamWriter(out));

            //4.Loop through the data and concatenate it into a text field
            String line = null;
            while((line = br.readLine()) != null){
                // Stitch the read data into a text field for display
                jta.append(line + System.lineSeparator());
            }


            //5.Close the Socket channel
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        /******************** TCP PTP1 end  ***************************/

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        File f = new File("E:\\improve\\idea\\chat room\\src\\chat\\data.txt");
        try {
            fw = new FileWriter(f,true);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fw);
        //1.Gets the contents of the text box
        String text = jtf.getText();
        //2.To get the time
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss ");
        Date date = new Date(System.currentTimeMillis());
        //3.Splicing data
        text = "A:   " + text + "      "+formatter.format(date);
        //4.Need to show yourself
        jta.append(text + System.lineSeparator());
        try {
            //5.send
            bw.write(text);
            bw.newLine();
            bw.flush();
            //6.Clear the text box contents
            jtf.setText("");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        //7. Write the chat record into a document
        pw.println(text);
        pw.flush();
        try {
            fw.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
