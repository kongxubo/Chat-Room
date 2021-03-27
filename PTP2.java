package chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

//1.定义JFrame窗体中的组件
//2.在构造方法总初始化窗体的组件
//3.使用网络编程完成数据的传输（TCP协议）
//4.发送’按钮‘的监听点击事件
//5.保存聊天记录‘按钮’的监听事件
public class PTP2 extends JFrame implements ActionListener {
    public static void main(String[] args){

        //调用构造方法
        new PTP2();
    }
    //属性
    //文本域
    private JTextArea jta;
    //滚动条
    private JScrollPane jsp;
    //面板
    private JPanel jp;
    //文本框
    private JTextField jtf;
    //按钮
    private JButton jb;


    //输出流
    private BufferedWriter bw = null;
    //文本输出流
    private FileWriter fw = null;

    //构造方法
    public PTP2(){
        //初始化组件
        jta = new JTextArea();
        //设置文本域不可编辑
        jta.setEditable(false);
        //注意：需要将文本域添加到滚动条中，实现滚动效果
        jsp = new JScrollPane(jta);
        //面板
        jp = new JPanel();
        jtf = new JTextField(10);
        jb = new JButton("发送");
        //注意：需要将文本框与按钮添加到面板中
        jp.add(jtf);
        jp.add(jb);

        //注意：需要将滚动条与面板添加到窗体中
        this.add(jsp, BorderLayout.CENTER);
        this.add(jp,BorderLayout.SOUTH);

        //注意：需要设置‘标题，大小，位置，关闭，是否可见’
        this.setTitle("Kiki-PTP2");
        this.setSize(300,300);
        this.setLocation(600,300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        /******************** TCP PTP2 Start***************************/
        //给发送按钮绑定一个监听点击事件
        jb.addActionListener(this);
        try {
            //1.创建PTP1端套接字Socket
            Socket socket = new Socket("127.0.0.1", 8888);

            //3.获取socket通道的输入流(输入流是实现读取数据，一行一行读) BufferedReader -> readLine();
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //3.获取socket通道的输出流(输入流是实现写出数据,一行一行写) BufferedWriter -> readLine();
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            //循环读取数据，并拼接到文本域中
            String line = null;
            while((line = br.readLine()) != null){
                //将读取的数据拼接到文本域中显示
                jta.append(line + System.lineSeparator());
            }

            //5.关闭Socket通道
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        /******************** TCP PTP2 end  ***************************/

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
        //1.获取文本框框中内容
        String text = jtf.getText();
        //2.获取时间
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss ");
        Date date = new Date(System.currentTimeMillis());
        //3.拼接数据
        text = "B:   " + text + "      "+formatter.format(date);
        //4.自己也需要显示
        jta.append(text + System.lineSeparator());
        try {
            //5.发送
            bw.write(text);
            bw.newLine();
            bw.flush();
            //6.清空文本框内容
            jtf.setText("");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        //7.把聊天记录写入文档
        pw.println(text);
        pw.flush();
        try {
            fw.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
