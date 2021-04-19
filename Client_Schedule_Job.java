import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

class Servers{                  //class for the Arraylist of servers
    String serverName;          //the servers name 
    int serverID;               //the servers ID
    String state;               //the servers state 
    int currStartTime;          //the servers start time 
    int cores;                  //the number of core the server has 
    int mem;                    // the amount of memory on the server
    int disk;                   //the server disk number
}

public class Client_Schedule_Job {

    public void MsgSender (String s, BufferedOutputStream bout){        //function called when a comunication from the client to the server needs to happen
        try{
            bout.write(s.getBytes());                                   //turn the passed string to bytes 
            System.out.println(s +" has been sent to server");          //print the line "message" has been sent to server
            bout.flush();                                               //flush the message to the server 
            return;                                                     //returning to call, as this is a void funtion no value is returned
        }catch (Exception e){
           System.out.println(e);
        }
    }

    public String MsgConverter (byte[] x, BufferedInputStream bin){     //function called when a comunication from the server that needs to be converted to a string
        try{
            bin.read(x);                                                //read the input stream of data and store it in value x
            String reply = new String(x, StandardCharsets.UTF_8);       //convert the byte array in x to a string 
            return reply;                                               //return the string value to the call
        }catch (Exception e){
            System.out.println(e);
        }
        return "Failed";                                                //as this function returns a string value has to reutrn a string value at all time  
    }

    public void MsgReciever (byte[] x, BufferedInputStream bin){        //function called when a communication is recieved from the server
        try{
            bin.read(x);                                                //read the input stream of data and store it in the value x 
            String reply = new String(x, StandardCharsets.UTF_8);       //convert the byte array to a string 
            System.out.println("RCVD Response: "+ reply);               //print the line "RCVD response " Plus the recived message
            return;                                                     //return to call. as this is a void funtion no value is returned
        }catch (Exception e){
            System.out.println(e);
        }
    }

   public ArrayList<Servers> serverList= new ArrayList<Servers>();      //initalize an arraylist to store the servers, use the class servers
   public Servers biggestServer;                                        //initalise a server called bigestserver, which will sotre the largest server
   public int coreCount = -1;                                           //initalise a counter to use in the determining of the largest server 
   
   public static void main(String args[]){
        try{
            Socket s = new Socket("127.0.0.1", 50000);                              //declare the socket to be on localhost and port 50000
            DataInputStream din = new DataInputStream(s.getInputStream());          //declare the input stream
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());      //declare the output stream
            BufferedOutputStream bout = new BufferedOutputStream(dout);             //declare the buffered output stream 
            BufferedInputStream bin = new BufferedInputStream(din);                 //declare the buffered inout stream

            Client_Schedule_Job cjs = new Client_Schedule_Job();                    //declare what cjs, to be called

            System.out.println("connected");                                        //to state to the client that the server has connected
            
            //Send HELO
            cjs.MsgSender("HELO", bout);                                            //call the msg sender function with string helo

            //Receive reply from HELO            
            cjs.MsgReciever(new byte[32], bin);                                     //call the msg reciever function for the helo reply 

            //Send AUTH
            cjs.MsgSender("AUTH Jono", bout);                                       //call the msg sender function with the string AUTH Jono

            //Recieve reply from AUTH
            cjs.MsgReciever(new byte[32], bin);                                     //call the msg reciever function for the AUTH Jono reply

            //Send REDY
            cjs.MsgSender("REDY", bout);                                            //call the msg sender function with string REDY 
        
            //Recieve reply from REDY
            cjs.MsgReciever(new byte[32], bin);                                     //call the msg reciever function for the REDY reply 

            //Send GETS ALL
            cjs.MsgSender("GETS All", bout);                                        //call the msg sender function for the string GETS All

            //Recieves reply from GETS ALL

            String reply = new String(cjs.MsgConverter(new byte[32], bin));         //call the function message converter on the input stream
            System.out.println("RCVD Response: "+ reply);                           //print the reply from the function 
            String[] temp = reply.split(" ");                                       //split the string into an array of strings on spaces 
            System.out.println("Temp 1 is: " +temp[1]);                             //print the first value in the string array
            System.out.println("Temp 2 is: " +temp[2]);                             //print the second value in the string array

           //Send OK (NEEDS TO BE HERE)
            cjs.MsgSender("OK", bout);                                              //call the msg sender function for the string ok 
            cjs.MsgSender("OK", bout);                                              //call the msg sender function for the string ok

            int num = Integer.parseInt(temp[1])*Integer.parseInt(temp[2]);          //multiple the two temp values as intergers and store in the int num 

            String Reply = new String(cjs.MsgConverter(new byte[num], bin));        //call the msg convertor function and story the reply in a string
            String[] array = Reply.split("\n");                                     //create a string array called array and fill with the server response but split on each new line 

            for(String server: array){                                              // Creation of a new server list to find specific individual server attributes
            String[] indiServer = server.split(" ");                                // such as coreCount. Split servers into individual servers
                Servers tempIndividualServer = new Servers();                       // create a temporary server list to input any attribute to the server
                tempIndividualServer.serverName= indiServer[0];                     
                tempIndividualServer.serverID = Integer.parseInt(indiServer[1]);
                tempIndividualServer.state = indiServer[2];
                tempIndividualServer.currStartTime = Integer.parseInt(indiServer[3]);
                tempIndividualServer.cores = Integer.parseInt(indiServer[4]);
                tempIndividualServer.mem = Integer.parseInt(indiServer[5]);
                tempIndividualServer.disk = Integer.parseInt(indiServer[6]);
                cjs.serverList.add(tempIndividualServer);                           // Add attributes into the array list and repeat the steps with the next servers
            }


            for(Servers serverToInspect: cjs.serverList){                         //for servers in serverlist
                if(cjs.coreCount < serverToInspect.cores){                        //check the severtoinspects.core is larger the the corecount
                    cjs.biggestServer = serverToInspect;                          //if so set biggest server to the serverto inspect 
                    cjs.coreCount = serverToInspect.cores;                        //set corecount to the cores of the servertoinspect.cores
                }
            }
            
            //send the SCHD 
            String Largest = "SCHD 110 "; //+ cjs.biggestServer.serverName + " " + Integer.toString(cjs.biggestServer.serverID)+ " " + cjs.biggestServer.state + " " +Integer.toString(cjs.biggestServer.currStartTime) + " " +Integer.toString(cjs.biggestServer.cores) + " " +Integer.toString(cjs.biggestServer.mem) + " " +Integer.toString(cjs.biggestServer.disk) ;
            System.out.println ("Largest server is: " + Largest);       //print the largest server details
            cjs.MsgSender(Largest, bout);                               //call msg sender function with th input of the string largest 

            //SCH Reply
            cjs.MsgReciever(new byte[32], bin);                         //call the msg reciever function       

            //send REDY msg 
            cjs.MsgSender("REDY", bout);                                //call the msg sender function with the string REDY

            //Reply from REDY
            cjs.MsgReciever(new byte[32], bin);                         //call the msg reciever function on the reply from redy

            //tell the server u are ready to QUIT
            cjs.MsgSender("QUIT", bout);                                //call the msg sender function with the string QUIT

            //read reply
            String REPLY = new String(cjs.MsgConverter(new byte[32], bin));     //call the msg converter function on the quit reply 
            System.out.println("The server wants to: "+ REPLY);                 //print the reply 

            if(REPLY.equals("QUIT")){                                           //if the reply equalt the strgin quit we know the server wants to quit to
                bout.close();                                                   //close the buffered output stream
                dout.close();                                                   //close the data output stream
                s.close();                                                      //close the socket
            }

        }catch (Exception e){
           System.out.println(e);
        }
    }
}
