package com.accuvally.hdtui.utils.chunk;

import java.io.Serializable;

/**
 * Created by Andy Liu on 2017/7/10.
 */
public class ChunkInfo  extends FileInfo implements Serializable {

    public static final int chunkLength=1024*1024;//1024*1024



    /**
     * 文件的当前分片值
     */
    private int chunk=1;
    /**
     * 文件总分片值
     */
    private int chunks=1;


    public String getChunkName() {
        return chunkName;
    }

    public void setChunkName(String chunkName) {
        this.chunkName = chunkName;
    }

    private String chunkName;

    public int getChunks() {
        return chunks;
    }

    public void setChunks(int chunks) {
        this.chunks = chunks;
    }

    public int getChunk() {
        return chunk;
    }

    public void setChunk(int chunk) {
        this.chunk = chunk;
    }

    @Override
    public String toString() {
        return "ChunkInfo{" +
                "chunk=" + chunk +
                ", chunks=" + chunks +
                ", chunkName='" + chunkName + '\'' +
                '}';
    }
}