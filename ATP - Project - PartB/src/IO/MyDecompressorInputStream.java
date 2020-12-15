package IO;

import java.io.IOException;
import java.io.InputStream;

public class MyDecompressorInputStream extends InputStream {

    InputStream in;
    public MyDecompressorInputStream(InputStream input) {
        this.in = input;
    }

    public int read() throws IOException {
        return in.read();
    }

    public int read(byte[] b) throws IOException {
        int rowC = read() & (0xff);
        int RestRow = read() & (0xff);
        int columnC = read() & (0xff);
        int Restcol = read() & (0xff);
        int startRowC = read() & (0xff);
        int RestStartRow = read() & (0xff);
        int startColumnC = read() & (0xff);
        int RestStartCol = read() & (0xff);
        int goalRowC = read() & (0xff);
        int RestGoalRow = read() & (0xff);
        int goalColumnC = read() & (0xff);
        int RestGoalCol = read() & (0xff);
        b[0] = ((Integer) rowC).byteValue();
        b[1] = ((Integer) RestRow).byteValue();
        b[2] = ((Integer) columnC).byteValue();
        b[3] = ((Integer) Restcol).byteValue();
        b[4] = ((Integer) startRowC).byteValue();
        b[5] = ((Integer) RestStartRow).byteValue();
        b[6] = ((Integer) startColumnC).byteValue();
        b[7] = ((Integer) RestStartCol).byteValue();
        b[8] = ((Integer) goalRowC).byteValue();
        b[9] = ((Integer) RestGoalRow).byteValue();
        b[10] = ((Integer) goalColumnC).byteValue();
        b[11] = ((Integer) RestGoalCol).byteValue();
        int index=12;
        byte[] array;
        while (index < b.length) {
            int count = read() & (0xff);
            if(b.length-index<8){
                array=decToBin(count,b.length-index);
            }
            else {
                array = decToBin(count, 8);
            }
            for(int i=0;i<array.length;i++){
                b[index]=array[i];
                index++;
            }
        }
        return -1;
    }
    private byte[] decToBin(int b,int size) {
        byte[] toRet = new byte[size];
        for(int i = size -1 ; i >= 0; i--) {
            toRet[i] = (byte)(b%2);
            b = (b/2);
        }
        return toRet;
    }
}