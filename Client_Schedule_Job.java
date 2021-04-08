import java.util.ArrayList;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Client_Schedule_Job {
    public static void main(String args[]){
        try{
            Socket s = new Socket("127.0.0.1", 50000);
            DataInputStream din = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            BufferedOutputStream bout = new BufferedOutputStream(bout);
            BufferedInputStream bin = new BufferedInputStream(bin);


            bout.close();
            s.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
//Send HELO
//Receive reply 

//Send AUTH
//Recieve reply 

//Send REDY
//Recieve reply

//Send GETS ALL
//Recieves reply from GETS ALL
//Send OK

//The reply will be infomation data from the server