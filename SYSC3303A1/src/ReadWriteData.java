import java.io.ByteArrayOutputStream;

/**
 * Created by Faisal on 2018-01-20.
 */
public class ReadWriteData {
    public static byte[] readData;
    public static byte[] writeData;
    public static byte[] errorData;
    String sampleFile = "test.txt";
    String mode = "octet";

    public ReadWriteData(){
        this.readData = this.createReadRequestMessage();
        this.writeData = this.createWriteRequestMessage();
        this.errorData = this.createErrorRequestMessage();
    }


    public byte[] createReadRequestMessage(){
        //https://stackoverflow.com/questions/1774651/growing-bytebuffer
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        data.write(0);
        data.write(1);
        data.write(sampleFile.getBytes(), 0, sampleFile.length());
        data.write(0);
        data.write(mode.getBytes(), 0, mode.length());
        data.write(0);
        return data.toByteArray();
    }
    public byte[] createWriteRequestMessage(){
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        data.write(0);
        data.write(2);
        data.write(sampleFile.getBytes(), 0, sampleFile.length());
        data.write(0);
        data.write(mode.getBytes(), 0, mode.length());
        data.write(0);
        return data.toByteArray();
    }

    public byte[] createErrorRequestMessage(){
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        data.write(0);
        data.write(7);
        data.write(sampleFile.getBytes(), 0, sampleFile.length());
        data.write(0);
        data.write(mode.getBytes(), 0, mode.length());
        data.write(0);
        return data.toByteArray();
    }
}
