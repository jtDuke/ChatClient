/**
 * Created by jtryg on 3/31/2017.
 */

import java.net.*;  // socket
import java.io.*;
import java.util.Date;


public class ChatServer {

    public static void main(String args[]) throws java.io.IOException
    {
        int port = 1300;
        ServerSocket ss = new ServerSocket(port);
        Socket connection = ss.accept();
        System.out.println("I connected!!!!");
        connection.close();

    }
}
