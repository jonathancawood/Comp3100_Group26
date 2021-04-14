import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
class Servers{
    String serverName;
    int serverID;
    String state;
    int currStartTime;
    int cores;
    int mem;
    int disk;
}
public String MsgSender (String , BufferedOutputStream bout){
    try{
        bout.write(s.getBytes());
        System.out.println(s + " has been sent to server");
        bout.flush();
        return;
    }catch (Exception e){
        System.out.println(e);
    }

}

public class Client_Schedule_Job {
    public ArrayList<Servers> serverList= new ArrayList<Servers>();
    public static void main(String args[]){
        try{
            Socket s = new Socket("127.0.0.1", 50000);
            DataInputStream din = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            BufferedOutputStream bout = new BufferedOutputStream(dout);
            BufferedInputStream bin = new BufferedInputStream(din);
        
            //Send HELO
            cjs.MsgSender("HELO", bout);

            //Receive reply from HELO
            byte[] serverReplyHELO = new byte[32];
            bin.read(serverReplyHELO);
            String ServerReplyHELO = new String(serverReplyHELO, StandardCharsets.UTF_8);
            System.out.println("RCVD in response to HELO: "+ ServerReplyHELO);
            
            //Send AUTH
            cjs.MsgSender("AUTH Jono", bout);

            //Recieve reply from AUTH
            byte[] serverReplyAUTH = new byte[32];
            bin.read(serverReplyAUTH);
            String ServerReplyAUTH = new String(serverReplyAUTH, StandardCharsets.UTF_8);
            System.out.println("RCVD in response to AUTH: " + ServerReplyAUTH);

            //Send REDY
            cjs.MsgSender("REDY", bout);

            //Recieve reply from REDY
            byte[] serverReplyREDY = new byte[32];
            bin.read(serverReplyREDY);
            String ServerReplyREDY = new String(serverReplyREDY, StandardCharsets.UTF_8);
            System.out.println("RCVD in reponse to REDY: " +ServerReplyREDY);

            //Send GETS ALL
            cjs.MsgSender("GETS All", bout);

            //Recieves reply from GETS ALL
            byte[] serverReplyGETS = new byte[32];
            bin.read(serverReplyGETS);
            String ServerReplyGETS = new String(serverReplyGETS, StandardCharsets.UTF_8);
            System.out.println("RCVD in reponse to GETS ALL: " +ServerReplyGETS);
            
            //Send OK (after GETS All)
            cjs.MsgSender("OK", bout);

            //get the server reply and convert to string spliting along the way
            String[] temp = ServerReplyGETS.split(" ");
            byte[] serverReplyGETS1 = new byte[Integer.parseInt(temp[1])*Integer.parseInt(temp[2])];
            String ServerReplyGETS1 = new String(serverReplyGETS1, StandardCharsets.UTF_8);
            
            //convert the strings intro an array of strings
            String[] arrofstr = ServerReplyGETS1.split("\n");   
            Client_Schedule_Job cjs = new Client_Schedule_Job();

            // go throught the array of strings and for each reply create a server and import the right values into the right fields, repeating this for all servers in the reply
            for(String server: arrofstr){
                String[] indiServer = server.split(" ");
                Servers Indi = new Servers();
                Indi.serverName= indiServer[0];
                Indi.serverID = Integer.parseInt(indiServer[1]);
                Indi.state = indiServer[2];
                Indi.currStartTime = Integer.parseInt(indiServer[3]);
                Indi.cores = Integer.parseInt(indiServer[4]);
                Indi.mem = Integer.parseInt(indiServer[5]);
                Indi.disk = Integer.parseInt(indiServer[6]);
                cjs.serverList.add(Indi);
            }
            //send ok to the server
            cjs.MsgSender("OK", bout);

            //The reply will be infomation data from the server
            bout.close();
            s.close();
        }catch (Exception e){
            System.out.println(e);
        }

    }

}
