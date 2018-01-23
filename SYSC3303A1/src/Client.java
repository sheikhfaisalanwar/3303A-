
import java.io.IOException;
import java.net.*;
import java.util.Arrays;

/**
 * Created by Faisal on 2018-01-16.
 */
public class Client {

    DatagramPacket sendPacket, receivePacket;
    private static DatagramSocket sendReceiveSocket;

    public Client(){
            try {
                sendReceiveSocket = new DatagramSocket();
            } catch (SocketException e){
                System.out.println(e);
            }
    }

    public void send(byte[] packetData){
        try {
            sendPacket = new DatagramPacket(packetData, packetData.length, InetAddress.getLocalHost(), 23);
        } catch(UnknownHostException e){
            System.out.println(e);
            System.exit(1);
        }
        //send with socket
        try{
            System.out.println("Sending packet with Info:");
            System.out.println(Arrays.toString(sendPacket.getData()));
            System.out.println(new String(sendPacket.getData()));
            sendReceiveSocket.send(sendPacket);
            System.out.println("---------SENT-----------");
        } catch(IOException e){
            System.out.println(e);
        }

    }

    public void receive(byte[] packetData){
        receivePacket = new DatagramPacket(packetData,packetData.length);
        try {
            sendReceiveSocket.receive(receivePacket);
            System.out.println("Packet from Host forwarded by server");
            System.out.println(Arrays.toString(receivePacket.getData()));
            System.out.println(new String(receivePacket.getData()));
        } catch(IOException e){
            System.out.println(e);
            System.exit(1);
        }
        //print received packet details

    }

    public void sendReceive(){
        ReadWriteData data = new ReadWriteData();
        System.out.println(data.readData);
        System.out.println(data.writeData);
        byte[] packetData = new byte[100];
        for(int i = 0; i < 10; i++){

            send(i % 2 == 0 ? data.readData : data.writeData);
            receive(packetData);
        }
        send(data.errorData);
        receive(packetData);
        sendReceiveSocket.close();
    }
    public static void main(String[] args){
        //Add for loop
        Client c = new Client();
        c.sendReceive();

    }



}
