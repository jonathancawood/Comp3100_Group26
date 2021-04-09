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
            String AUTH = "AUTH BLANK";
            bout.write(AUTH.getBytes());
            System.out.println("AUTH has been sent to server");
            bout.flush();

            //Recieve reply from AUTH
            byte[] serverReplyAUTH = new byte[32];
            bin.read(serverReplyAUTH);
            String ServerReplyAUTH = new String(serverReplyAUTH, StandardCharsets.UTF_8);
            System.out.println("RCVD in response to AUTH: " + ServerReplyAUTH);

            //Send REDY
            String REDY ="REDY";
            bout.write(REDY.getBytes());
            System.out.println("REDY has been sent to server");
            bout.flush();

            //Recieve reply from REDY
            byte[] serverReplyREDY = new byte[32];
            bin.read(serverReplyREDY);
            String ServerReplyREDY = new String(serverReplyREDY, StandardCharsets.UTF_8);
            System.out.println("RCVD in reponse to REDY: " +ServerReplyREDY);

            //Send GETS ALL
            String GETS_ALL ="GETS All";
            bout.write(GETS_ALL.getBytes());
            System.out.println("GETS ALL has been sent to server");
            bout.flush();

            //Recieves reply from GETS ALL
            byte[] serverReplyGETS = new byte[32];
            bin.read(serverReplyGETS);
            String ServerReplyGETS = new String(serverReplyGETS, StandardCharsets.UTF_8);
            System.out.println("RCVD in reponse to GETS ALL: " +ServerReplyGETS);
            
            //Send OK
            String OK ="OK";
            bout.write(OK.getBytes());
            bout.flush();

            //The reply will be infomation data from the server
            bout.close();
            s.close();
        }catch (Exception e){
            System.out.println(e);
        }

    }

}
