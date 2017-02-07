package com.jajale.watch.image;

import android.os.Environment;

import com.jajale.watch.utils.L;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/10/22.
 */
public class GetImage {

    private final static String ALBUM_PATH = "/storage/sdcard0/Android/data/com.jajale.watch/cache/";

    public static byte[] download(String url, String fileName) {
//        String url = "http://www.baidu.com/img/baidu_sylogo1.gif";
        byte[] btImg = getImageFromNetByUrl(url);
        if(null != btImg && btImg.length > 0){
            L.e("读取到：" + btImg.length + " 字节");
            writeImageToDisk(btImg, fileName);
        }else{
            L.e("没有从该连接获得内容");
        }
        return btImg;
    }
    /**
     * 将图片写入到磁盘
     * @param img 图片数据流
     * @param fileName 文件保存时的名称
     */
    public static void writeImageToDisk(byte[] img, String fileName){
        try {
            File file = new File(ALBUM_PATH + fileName);
            FileOutputStream fops = new FileOutputStream(file);
            fops.write(img);
            fops.flush();
            fops.close();
            L.e("图片已经写入到C盘");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 根据地址获得数据的字节流
     * @param strUrl 网络连接地址
     * @return
     */
    public static byte[] getImageFromNetByUrl(String strUrl){
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 从输入流中获取数据
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
}
