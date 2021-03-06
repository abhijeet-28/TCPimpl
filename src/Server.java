/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ANKIT
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    
    public static int port;
    public static InetAddress address;
    public static DatagramSocket socket = null;
    public static DatagramPacket packet;
    public static byte[] data;
    public static byte[] convertBytes(String s,int P)
    {
	byte[] requested=new byte[P];
	byte[] data=s.getBytes();
	int i;
	for(i=0;i<data.length;i++)
	{
		requested[i]=data[i];
	}
	return requested;
    }
    public static byte[] convertBytes(String s)
    {
	byte[] data=s.getBytes();
	return data;
    }
    
    public static void main(String[] args) throws IOException{
        port = 4900;
        socket=new DatagramSocket(port);
        byte[] data = new byte[256];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        int cnt =0;
        int cum_ack = 0;
        while(true){
            cnt++;
            socket.receive(packet);
            System.out.println(cnt+" iteration");
            String received[] = new String[2];
            received=(new String(packet.getData())).trim().split(":");
            System.out.println("received[0] = "+received[0]);
            System.out.println("received[1] = "+received[1]);
            socket.setSoTimeout(50000);
            address=InetAddress.getByName("127.0.0.1");
            byte[] data_send;
            if(received[0].equals("2000")&&cnt<6&&false){
                System.out.println("deliberate ack sending failure");
                //data_send=convertBytes("2001"+":"+received[1],100);
            }
            else{
                if(Integer.parseInt(received[0])<=cum_ack){
                    cum_ack += Integer.parseInt(received[1]);
                     data_send = convertBytes(cum_ack+":"+received[1],100);
                InetAddress address_send = packet.getAddress();
                int port_send = packet.getPort();
                packet = new DatagramPacket(data_send, data_send.length, address_send, port_send);
                System.out.println("data_send = "+received[0]);
                socket.send(packet);
                }
                
               
            }
        }
    }
}
