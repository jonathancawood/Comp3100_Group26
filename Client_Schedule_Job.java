import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;

public class Client_Schedule_Job {
    public static void main(String args[]){
        try{
            Socket s = new Socket("127.0.0.1", 50000);
            DataInputStream din = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            BufferedOutputStream bout = new BufferedOutputStream(dout);
            BufferedInputStream bin = new BufferedInputStream(din);
        
            //Send HELO
            String HELO ="HELO";
            bout.write(HELO.getBytes());
            System.out.println("HELO has been sent to server");
            bout.flush();

            //Receive reply from HELO
            byte[] serverReplyHELO = new byte[32];
            bin.read(serverReplyHELO);
            String ServerReplyHELO = new String(serverReplyHELO, StandardCharsets.UTF_8);
            System.out.println("RCVD in response to HELO: "+ ServerReplyHELO);
            
            //Send AUTH

            //Recieve reply from AUTH

            //Send REDY

            //Recieve reply from REDY

            //Send GETS ALL

            //Recieves reply from GETS ALL

            //Send OK

            
            //The reply will be infomation data from the server
        
            bout.close();
            s.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}