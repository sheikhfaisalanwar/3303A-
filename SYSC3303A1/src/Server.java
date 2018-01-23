
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;

/**
 * Created by Faisal on 2018-01-16.
 */
public class Server {
    private DatagramPacket sendPacket, receivePacket;
    private DatagramSocket sendSocket, receiveSocket;
    private byte[] validReadData;
    private byte[] validWriteData;
    int incomingPacketPort;
    int incomingPacketLength;
    InetAddress incomingPacketAddress;

    public Server(){
        try{
            receiveSocket = new DatagramSocket(68);
        } catch(SocketException e){
            System.out.println(e);
        }
    }


    public void receive(byte[] packetData){
        //Receive from
        receivePacket = new DatagramPacket(packetData, packetData.length);
        try{
            receiveSocket.receive(receivePacket);
            System.out.println("Received from host");
            setClientInfo(receivePacket.getPort(),receivePacket.getLength(),receivePacket.getAddress());
        } catch(IOException e){
            System.out.println(e);
            System.exit(1);
        }
        System.out.println(Arrays.toString(receivePacket.getData()));
    }

    public void send(byte packetData[]){
        sendPacket =  new DatagramPacket(packetData, packetData.length, incomingPacketAddress, incomingPacketPort );
        System.out.println("Sending response");
        try{
            sendSocket = new DatagramSocket();
        } catch(SocketException e){
            System.out.println(e);
            System.exit(1);
        }

        try {
            System.out.println(Arrays.toString(sendPacket.getData()));
            System.out.println(new String(sendPacket.getData()));
            sendSocket.send(sendPacket);
            System.out.println("sent to host");
        }catch(IOException e){
            System.out.println(e);
            System.exit(1);
        }
        sendSocket.close();
    }

    public boolean validateIncomingPacket(byte packetData[]){
        System.out.println("---VALIDATING---");

        if(packetData[0] != (byte) 0){
            System.out.println("Invalid first byte");
            System.exit(1);
        }
        if(packetData[1] != (byte) 1 && packetData[1] != (byte) 2){
            System.out.println("Invalid second byte");
            System.exit(1);
        }
        int counter = 0;
        for(int i=2; i < packetData.length -1; i++){
            if(packetData[i] != (byte) 0){
                counter++;
            } else{
                break;
            }
        }
        if(packetData[1+counter+1] != (byte) 0){
            System.out.println("No middle 0 byte");
            System.exit(1);
        }
        int counter2= 0;
        for(int i=2+counter+1; i < packetData.length -1; i++){
            if(packetData[i] != (byte) 0){
                counter2++;
            } else{
                break;
            }
        }
        if(packetData[1+counter+1+counter2+1] != (byte) 0){
            System.out.println("No last 0 byte");
            System.exit(1);
        }
        System.out.println("---END VALIDATING---");
        return true;

    }

    public byte[] createValidReadMessage(){
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        data.write(0);
        data.write(3);
        data.write(0);
        data.write(1);
        validReadData = data.toByteArray();
        return validReadData;
    }
    public byte[] createValidWriteMessage(){
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        data.write(0);
        data.write(4);
        data.write(0);
        data.write(0);
        validWriteData = data.toByteArray();
        return validWriteData;
    }
    public void setClientInfo(int port, int length, InetAddress address){
        this.incomingPacketPort = port;
        this.incomingPacketLength = length;
        this.incomingPacketAddress = address;
    }

    public static void main(String[] args){
        Server s = new Server();
        while(true){
            System.out.println("----Waiting on host packet----");
            byte[] packetData = new byte[100];
            //Receive packet from host
            s.receive(packetData);
            System.out.println("Received packet from host");
            //validate packet
            boolean valid = s.validateIncomingPacket(packetData);

            //forward to host
            if(valid && packetData[1] == (byte) 1){
                packetData = s.createValidReadMessage();
                s.send(packetData);
            } else if(valid && packetData[1] == (byte) 2){
                packetData = s.createValidWriteMessage();
                s.send(packetData);
            }
        }
    }
}
