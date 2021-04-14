import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream; 
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client_Side{
    public static void main(String[] args){
        try{
            Socket s = new Socket("localhost", 50000);

            InputStream dis = new DataInputStream(s.getInputStream());
            OutputStream dout = new DataOutputStream(s.getOutputStream());

            //STEP 1 (Protocol)
            //Make a string, convert it to a byte array then snd to a server 
            String myString = "HELO";
            dout.write(myString.getBytes());
            dout.flush();

            System.out.println("Client has sent \"" + myString + "\" to the server......" );

            //Read the byte stream from the server 
            byte[] byteArray = new byte[dis.available()];
            dis.read(byteArray);

            //Make a string using the recieved byte and print the line
            myString = new String(byteArray, StandardCharsets.UTF_8);
            System.out.println(myString + " recieved from server");
            
            //STEP 3 (Protocol)
            //Authenticate with the username (jono) -- not sure if this is will stay as jono --
            myString = "AUTH jono";
            dout.write(myString.getBytes());
            dout.write(byteArray);

            //Read the byte stream from the server
            byte[] byteArray0 =new byte[2];
            dis.read(byteArray0);

            //Make a string using the recieved byte and print the line 
            myString = new String(byteArray0, StandardCharsets.UTF_8);
            System.out.println("Second OK from the server: " + myString);

            //STEP 5 (Protocol)
            //Make a string, convert it to a byte array and send to the server
            myString = "REDY";
            byteArray = myString.getBytes();
            dout.write(byteArray);
            dout.flush();

            //Read the byte stream from the server 
            byte[] byteArray1 =new byte[5];
            dis.read(byteArray1);

            //Make a string using the recieved byte and print the line 
            myString = new String(byteArray1, StandardCharsets.UTF_8);
            System.out.println("String:" + myString);                                               //all good

            //STEP 7 (protocol)
            //Believe have to implement a GETS command to get the data from the server
            //Tell the server to get all
            myString ="GETS ALL";
            byteArray = myString.getBytes();
            dout.write(byteArray);
            dout.flush();

            //Read the byte stream from the server
            byte[] byteArray2 = new byte[12+2];
            dis.read(byteArray2);

            //make a string using the recieved byte and print the line
            myString = new String(byteArray2, StandardCharsets.UTF_8);
            System.out.println("GETS all reply:" + myString);

            //STEP 9 (Protocol)
            //Sends ok back to the server
            myString = "OK";
            dout.write(myString.getBytes());
            dout.flush();

            byte[] byteArray3 = new byte[184*124];
            dis.read(byteArray3);

            //STEPS 11 and 12 (protocol)
            //make a string using the recieved byte and print the line
            myString = new String(byteArray3, StandardCharsets.UTF_8);
            System.out.println("Second GETS all reply:" + myString);

            myString = "QUIT";
            dout.write(myString.getBytes());
            dout.flush();
        } catch(Exception e) {System.out.println(e);}
    }
}
