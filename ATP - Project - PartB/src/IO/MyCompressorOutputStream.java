package IO;

import java.io.IOException;
import java.io.OutputStream;

public class MyCompressorOutputStream extends OutputStream {

    OutputStream out;

    public MyCompressorOutputStream(OutputStream output) {
        out=output;
    }

    public void write(int b) throws IOException {
        out.write(b);
    }
    public void write(byte[] b) throws IOException{
        int index;
        for(index=0;index<12;index++){
            int num=b[index] & (0xff);
            write(num);
        }
        while(index<b.length) {
            if(index+7>b.length){
                write(binToDec(b, index, b.length-1));
            }
            else{
                write(binToDec(b, index, index + 7));
            }
            index+=8;
        }
    }
    private byte binToDec(byte[] b ,int start, int end) {
        int pow = 0;
        byte num = 0;
        for(int i = end ; i >= start ;i--) {
            int toAdd = b[i];
            if(toAdd>1)
                toAdd = 0;
            num+=(byte)(Math.pow(2,pow)*toAdd);
            pow++;
        }
        return num;
    }
}
