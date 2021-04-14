import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

class Servers{ //class for the Arraylist of servers
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
            System.out.println(s +" has been sent to server");
            bout.flush();
            return;
        }catch (Exception e){
            System.out.println(e);
        }
    }  

    public String MsgConverter (byte[] x, BufferedInputStream bin){
        try{
            bin.read(x);
            String reply = new String(x, StandardCharsets.UTF_8);
            return reply;
        }catch (Exception e){
            System.out.println(e);
        }
        return "Failed";
    }

    public void MsgReciever (byte[] x, BufferedInputStream bin){
        try{
            bin.read(x);
            String reply = new String(x, StandardCharsets.UTF_8);
            System.out.println("RCVD Response: "+ reply);
            return;
        }catch (Exception e){
            System.out.println(e);
        }
    }
    
    public ArrayList<Servers> serverList= new ArrayList<Servers>();
    public Servers biggestServer;
    public int coreCount = -1;
 
    public static void main(String args[]){
        try{
            Socket s = new Socket("127.0.0.1", 50000);

            DataInputStream din = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            BufferedOutputStream bout = new BufferedOutputStream(dout);
            BufferedInputStream bin = new BufferedInputStream(din);
            Client_Schedule_Job cjs = new Client_Schedule_Job();
            System.out.println("connected");

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
            String reply = new String(cjs.MsgConverter(new byte[32], bin));
            System.out.println("RCVD Response: "+ reply);
            String[] temp = reply.split(" ");
            System.out.println("Temp 1 is: " +temp[1]);
            System.out.println("Temp 2 is: " +temp[2]);

            //Send OK (NEEDS TO BE HERE)
            cjs.MsgSender("OK", bout);
            //cjs.MsgSender("OK", bout);

            //everything up to here seems to be working
            //cjs.MsgReciever(new byte[3000000], bin); // this needs to be replaced with what is gonna put it into the array
            cjs.MsgSender("OK", bout);
            String Reply = new String();
            //int num = Integer.parseInt(temp[1])*Integer.parseInt(temp[2]);
            Reply = cjs.MsgConverter(new byte[552], bin); // need to make this temp1 times temp 2
            String[] array = Reply.split("\n");

            for(String server: array){
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

            for(Servers serverToInspect: cjs.serverList){
                if(cjs.coreCount < serverToInspect.cores){
                    cjs.biggestServer = serverToInspect;
                }
            }

            cjs.MsgReciever(new byte[1], bin);
            String Largest = "SCHD 110 " + cjs.biggestServer.serverName;// + " "+ Integer.toString(cjs.biggestServer.serverID)+ " " + cjs.biggestServer.state + " " +Integer.toString(cjs.biggestServer.currStartTime) + " " +Integer.toString(cjs.biggestServer.cores) + " " +Integer.toString(cjs.biggestServer.mem) + " " +Integer.toString(cjs.biggestServer.disk) ;
            System.out.println ("Largest server is: " + Largest);
            cjs.MsgSender(Largest, bout);
 
            //SCH Reply
            //byte[] serverReplySCHD = new byte[100];

            //bin.read(serverReplySCHD);

            //String ServerReplySCHD = new String(serverReplySCHD,StandardCharsets.UTF_8);
            //System.out.println("RCVD in response to SCHD: " + ServerReplySCHD);
            
            cjs.MsgReciever(new byte[1000], bin);
 
            //send REDY msg
            //bout.write("REDY".getBytes()); //2nd job dispatch
            //bout.flush();
            cjs.MsgSender("REDY", bout);
 
            //Reply from REDY
            cjs.MsgReciever(new byte[32], bin);
 
            //tell the server u are ready to QUIT
            cjs.MsgSender("QUIT", bout);
 
            //read reply
            String REPLY = new String(cjs.MsgConverter(new byte[32], bin));
            System.out.println("The server wants to: "+ REPLY);
 
            if(REPLY.equals("QUIT")){
                bout.close();
                dout.close();
                s.close();
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
