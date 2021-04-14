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

public class Client_Schedule_Job {
    public void MsgSender (String s, BufferedOutputStream bout){
        try{
            bout.write(s.getBytes());
            System.out.println(s + " has been sent to server");
            bout.flush();
            return;
        }catch (Exception e){
           System.out.println(e);
        }
    }

    public void MsgReciever (byte[] x, BufferedInputStream bin){
        try{
            bin.read(x);
            String reply = new String(x,StandardCharsets.UTF_8);
            System.out.println("RCVD Response: "+ reply);
            return;
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public ArrayList<Servers> serverList= new ArrayList<Servers>();
   
    public static void main(String args[]){
        try{
            Socket s = new Socket("127.0.0.1", 50000);
            DataInputStream din = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            BufferedOutputStream bout = new BufferedOutputStream(dout);
            BufferedInputStream bin = new BufferedInputStream(din);

            Client_Schedule_Job cjs = new Client_Schedule_Job();
        
            //Send HELO
            cjs.MsgSender("HELO", bout);

            //Receive reply from HELO
            cjs.MsgReciever(new byte[32], bin);
            
            //Send AUTH
            cjs.MsgSender("AUTH Jono", bout);

            //Recieve reply from AUTH
            cjs.MsgReciever(new byte[32], bin);

            //Send REDY
            cjs.MsgSender("REDY", bout);

            //Recieve reply from REDY
            cjs.MsgReciever(new byte[32], bin);

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
