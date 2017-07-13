package com.accuvally.hdtui.utils.chunk;

import com.accuvally.hdtui.utils.Trace;

import org.apache.http.entity.mime.content.AbstractContentBody;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * Created by xiehui on 2016/10/13.
 */
public class CustomFileBody extends AbstractContentBody {
    private File file = null;
    private int chunk = 0;  //第几个分片
    private int chunks = 1;  //总分片数
//    private int chunkLength = 1024 * 1024 * 1; //分片大小1MB

    private long chunkSize;

    private ChunkInfo chunkInfo;

    public CustomFileBody(File file) {
        this(file, "application/octet-stream");
    }

    public CustomFileBody(ChunkInfo chunkInfo) {

        this(new File(chunkInfo.getFilePath()), "application/octet-stream");
        this.chunk = chunkInfo.getChunk();
        this.chunks = chunkInfo.getChunks();
        this.chunkInfo=chunkInfo;
        this.file = new File(chunkInfo.getFilePath());
        //3 0 1 2
        if(chunk+1<chunks){

            chunkSize=ChunkInfo.chunkLength;
        }else {
            chunkSize=file.length()-ChunkInfo.chunkLength*(chunks-1);
            Trace.e("","最后一块的 chunkSize:"+chunkSize);
        }

        if (this.chunk == this.chunks) {
            //先不判断，固定1M
            //this.chunkLength=this.file.length()-(this)
        }
    }

    public CustomFileBody(File file, String mimeType) {
        super(mimeType);
        if (file == null) {
            throw new IllegalArgumentException("File may not be null");
        } else {
            this.file = file;
        }
    }


    @Override
    public String getFilename() {
//        return this.file.getName();
        return chunkInfo.getChunkName();
    }


    public String getCharset() {
        return null;
    }


    public String getTransferEncoding() {
        return "binary";
    }

    public long getContentLength() {
//        return this.file.length();
        return chunkSize;
    }


    public void writeTo(OutputStream out) throws IOException {
      /*  String DIR = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/huodongxing2/";//
        Trace.e("",DIR);

        File file1=new File(DIR);
        if(!file1.exists()){
           boolean ret= file1.mkdir();
            Trace.e("","ret:"+ret);
        }

        FileOutputStream fileOutputStream = new FileOutputStream(DIR+chunkInfo.getChunkName());*/



        if (out == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        } else {
            //不使用FileInputStream
            RandomAccessFile randomAccessFile = new RandomAccessFile(this.file, "r");
            try {
                //int size = 1024 * 1;//1KB缓冲区读取数据
                byte[] tmp = new byte[1024];
                //randomAccessFile.seek(chunk * chunkLength);
                if (chunk+1<chunks){//中间分片
                    randomAccessFile.seek(chunk*ChunkInfo.chunkLength);
                    int n = 0;
                    long readLength = 0;//记录已读字节数
                    while (readLength <= ChunkInfo.chunkLength - 1024) {
                        n = randomAccessFile.read(tmp, 0, 1024);
                        readLength += 1024;
                        out.write(tmp, 0, n);
//                        fileOutputStream.write(tmp, 0, n);
                    }
                    if (readLength <= ChunkInfo.chunkLength) {
                        n = randomAccessFile.read(tmp, 0, (int)(ChunkInfo.chunkLength - readLength));
                        out.write(tmp, 0, n);
//                        fileOutputStream.write(tmp, 0, n);
                    }
                }else{//最后一块
                    randomAccessFile.seek(chunk*ChunkInfo.chunkLength);
                    int n = 0;
                    while ((n = randomAccessFile.read(tmp, 0, 1024)) != -1) {
                        out.write(tmp, 0, n);
//                        fileOutputStream.write(tmp, 0, n);
                    }
                }
                out.flush();
//                fileOutputStream.flush();
            } finally {
                randomAccessFile.close();
            }
        }
    }

    public File getFile() {
        return this.file;
    }
}
