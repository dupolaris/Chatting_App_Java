

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.net.*;
import java.io.*;

public class Client implements ActionListener{

    JTextField servertextinput;
    static JPanel textingarea;
    static Box verticalalign = Box.createVerticalBox();
    static JFrame f = new JFrame();
    static DataOutputStream dout;

    Client(){
        f.setLayout(null);
        JPanel frameheader = new JPanel();
        frameheader.setBackground(new Color(0, 102, 255));
        frameheader.setBounds(0, 0, 450, 70);
        frameheader.setLayout(null);
        f.add(frameheader);

        //client title in header
        JLabel clienttitle = new JLabel("Client");
        clienttitle.setBounds(20, 20, 100, 30);
        clienttitle.setForeground(Color.WHITE);
        clienttitle.setFont(new Font("SAN-SERIF",Font.BOLD, 30));
        frameheader.add(clienttitle);


        ImageIcon iclose = new ImageIcon(ClassLoader.getSystemResource("icons/close.png"));
        Image iclosescaled = iclose.getImage().getScaledInstance(35, 35, Image.SCALE_DEFAULT);
        ImageIcon iclosescaledicon = new ImageIcon(iclosescaled);
        JLabel closebutton = new JLabel(iclosescaledicon);
        closebutton.setBounds(390, 20, 25, 25);
        frameheader.add(closebutton);

        closebutton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent ae){
                System.exit(0);
            }
        });



        textingarea = new JPanel();
        textingarea.setBounds(5, 75, 425, 445);
        f.add(textingarea);

        servertextinput = new JTextField();
        servertextinput.setFont(new Font("MONOSPACED", Font.BOLD, 16));
        servertextinput.setBounds(5, 522, 300, 40);
        f.add(servertextinput);

        JButton sendbutton = new JButton("Send");
        sendbutton.addActionListener(this);
        sendbutton.setFont(new Font("SANS SERIF", Font.BOLD, 16));
        sendbutton.setBounds(310, 522, 120, 40);
        f.add(sendbutton);

        //Server chat frame
        f.setSize(450,605);
        f.setLocation(900,80);
//        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.white);
        f.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae){

        try {
            String gettedserverinput = servertextinput.getText();


            JPanel gettedserverinputLP = formatLabel(gettedserverinput);


            textingarea.setLayout(new BorderLayout());

            JPanel forwardedmsgbox = new JPanel(new BorderLayout());
            forwardedmsgbox.add(gettedserverinputLP, BorderLayout.LINE_END);
            verticalalign.add(forwardedmsgbox);
            verticalalign.add(Box.createVerticalStrut(15));

            textingarea.add(verticalalign, BorderLayout.PAGE_START);

            dout.writeUTF(gettedserverinput);

            servertextinput.setText("");

            f.repaint();
            f.invalidate();
            f.validate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static JPanel formatLabel(String gettedserverinput){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style=\"width: 150px\">" + gettedserverinput + "</p></html>");
        output.setFont(new Font("Thoma", Font.PLAIN, 16));
        output.setBackground(new Color(37, 211, 102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 50));


        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));

        panel.add(time);

        return panel;

    }

    public static void main(String[] args){
        new Client();

        try {
            Socket s = new Socket("127.0.0.1", 6001);
            DataInputStream din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());

            while (true) {
                textingarea.setLayout(new BorderLayout());
                String msg = din.readUTF();
                JPanel panel = formatLabel("Server: " + msg);

                JPanel left = new JPanel(new BorderLayout());
                left.add(panel, BorderLayout.LINE_START);
                verticalalign.add(left);

                verticalalign.add(Box.createVerticalStrut(15));
                textingarea.add(verticalalign, BorderLayout.PAGE_START);

                f.validate();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
