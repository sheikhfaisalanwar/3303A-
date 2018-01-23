import java.io.IOException;
import java.net.*;
import java.util.Arrays;

/**
 * Created by Faisal on 2018-01-16.
 */
public class Host {
    private DatagramPacket sendPacket, receivePacket;
    private DatagramSocket receiveSocket, sendSocket, sendReceiveSocket;
    int incomingPacketPort;
    int incomingPacketLength;
    InetAddress incomingPacketAddress;


    public Host(){
        try{
            receiveSocket = new DatagramSocket(23);
            sendReceiveSocket = new DatagramSocket();
        } catch(SocketException e){
            System.out.println(e);
            System.exit(1);
        }
    }

    public void send(byte[] packetData, String type){
        if(type=="client"){
            //forward to client
            sendPacket = new DatagramPacket(packetData, incomingPacketLength, incomingPacketAddress, incomingPacketPort);
            try{
                sendSocket = new DatagramSocket();
            } catch(IOException e){
                System.out.println(e);
            }
            try{
                System.out.println("--------forwarding to client-------");
                sendSocket.send(sendPacket);
                System.out.println("Packet forwarded to client:" + Arrays.toString(sendPacket.getData()));
                System.out.println(new String(sendPacket.getData()));
            } catch(IOException e){
                System.out.println(e);
            }
            //log
            sendSocket.close();
        } else{

            //forward to server
            sendPacket = new DatagramPacket(packetData, incomingPacketLength, incomingPacketAddress, 69);
            try{
                System.out.println("--------forwarding to server-------");
                sendReceiveSocket.send(sendPacket);
                System.out.println("Packet forwarded to Server(byte):" + Arrays.toString(sendPacket.getData()));
                System.out.println("(String)" + new String(sendPacket.getData()));
            } catch(IOException e){
                System.out.println(e);
                System.exit(1);
            }
        }
    }

    public void receive(byte[] packetData, String type){

        if(type == "client"){
            // receiving from client
            DatagramPacket receivePacket = new DatagramPacket(packetData, packetData.length);
            try {
                System.out.println("--------waiting for client packet-------");
                receiveSocket.receive(receivePacket);
                System.out.println(Arrays.toString(receivePacket.getData()));
                System.out.println(new String(receivePacket.getData()));
                setClientInfo(receivePacket.getPort(),receivePacket.getLength(), receivePacket.getAddress());
            } catch(IOException e){
                System.out.println(e);
                System.exit(1);
            }
        } else {
            //receiving from server
            DatagramPacket receivePacket = new DatagramPacket(packetData, packetData.length);
            try{
                System.out.println("--------waiting for server packet-------");
                sendReceiveSocket.receive(receivePacket);
                System.out.println(Arrays.toString(receivePacket.getData()));
                System.out.println(new String(receivePacket.getData()));
            } catch (IOException e){
                System.out.println(e);
                System.exit(1);
            }
        }

    }

    public void setClientInfo(int port, int length, InetAddress address){
        this.incomingPacketPort = port;
        this.incomingPacketLength = length;
        this.incomingPacketAddress = address;
    }

    public static void main(String[] args){
        Host h = new Host();
        byte[] packetData = new byte[100];

        while(true){
            //Receive packet from client
            h.receive(packetData, "client");
            //forward to server
            h.send(packetData,"server");
            //receive packet from server
            packetData = new byte[100];
            h.receive(packetData, "server");
            //forward to client
            h.send(packetData, "client");
        }
    }
}
