package com.linukey.todo.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by linukey on 17-3-11.
 */

public class Document implements Serializable {
    private String fileName;

    //InputStream 不能被序列化
    private transient InputStream inputStream;

    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public Document(String fileName, InputStream inputStream) {
        if(fileName == null || fileName.trim().length() == 0 || inputStream == null) {
            throw new IllegalArgumentException("fileName:" + fileName + " | inputStream:" + inputStream);
        }
        this.fileName = fileName;
        this.inputStream = inputStream;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        System.out.println("调用了ReadObject");
        ois.defaultReadObject();//读取可反序列化的内容

        //读取流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int ret = -1;
        while((ret = ois.read(buffer, 0, 1024)) != -1) {
            baos.write(buffer, 0, ret);
        }
        byte[] data = baos.toByteArray();

        inputStream = new ByteArrayInputStream(data);

    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        System.out.println("调用了writeObject");
        oos.defaultWriteObject();//将可以序列化的内容写出到流

        //接下来将inputStream也写出
        byte[] buffer = new byte[1024];
        int ret = -1;
        while((ret = inputStream.read(buffer, 0, 1024)) != -1) {
            oos.write(buffer, 0, ret);
        }
        oos.flush();
    }

    public void saveFile() throws IOException, FileNotFoundException {
    	FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);

            byte[] buffer = new byte[1024];
            int ret = -1;
            while((ret = inputStream.read(buffer, 0, 1024)) != -1) {
                fos.write(buffer, 0, ret);
            }
        } finally {
            if(fos != null) {
                fos.close();
            }
        }
    }
}