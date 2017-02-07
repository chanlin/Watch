package com.jajale.watch.utils;


import org.apache.http.protocol.HTTP;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/4/12.
 */
public class ByteUtil {

    public static String getUrlText(String fileName,String s) {
        String text = "";
        String sCurrentLine = "";
        
        try {
            URL url = new URL(s);
            java.net.HttpURLConnection l_connection = (java.net.HttpURLConnection) url
                    .openConnection();
            l_connection.connect();
            InputStream l_urlStream = l_connection.getInputStream();
            java.io.InputStreamReader read = new InputStreamReader(l_urlStream,
                    HTTP.UTF_8);
            java.io.BufferedReader l_reader = new java.io.BufferedReader(read);

            while ((sCurrentLine = l_reader.readLine()) != null) {
                text += sCurrentLine;
            }// viewSource(url);
        } catch (MalformedURLException ex) {
            System.out.println("网络连接错误:" + s);
            ex.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("文件不存在:" + s);
            e.printStackTrace();

        } catch (IOException e) {
            System.out.println("其他IO错误:" + s);
            e.printStackTrace();
        }
        return text;
    }


    //根据byte数组，生成文件
    public static void getFile(String play_content, String filePath) {

        byte[] bfile=hexStringToBytes(play_content);
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


    public static byte[] getBytes(String filePath){
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static String getByteArrayFromFileName(String fileName) {
        byte[] bytes=getBytes(fileName);

        return bytesToHexString(bytes);
    }


    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src==null||src.length==0) {
            return null;
        }
        for (byte aSrc : src) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (CMethod.isEmpty(hexString)) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static byte[] FZY(byte[] Rec) {
        int len = Rec.length;
        for (int i = 0; i < Rec.length; i++) {
            if (Rec[i] == 0x7D && Rec[i + 1] == 0x01) {
                len--;
                i++;
            } else if (Rec[i] == 0x7D && Rec[i + 1] == 0x02) {
                len--;
                i++;
            } else if (Rec[i] == 0x7D && Rec[i + 1] == 0x03) {
                len--;
                i++;
            } else if (Rec[i] == 0x7D && Rec[i + 1] == 0x04) {
                len--;
                i++;
            } else if (Rec[i] == 0x7D && Rec[i + 1] == 0x05) {
                len--;
                i++;
            }
        }
        byte[] Return = new byte[len];
        len = 0;
        for (int i = 0; i < Rec.length; i++) {
            if (Rec[i] == 0x7D && Rec[i + 1] == 0x01) {
                Return[i - len] = 0x7D;
                len++;
                i++;
            } else if (Rec[i] == 0x7D && Rec[i + 1] == 0x02) {
                Return[i - len] = 0x5B;
                len++;
                i++;
            } else if (Rec[i] == 0x7D && Rec[i + 1] == 0x03) {
                Return[i - len] = 0x5D;
                len++;
                i++;
            } else if (Rec[i] == 0x7D && Rec[i + 1] == 0x04) {
                Return[i - len] = 0x2C;
                len++;
                i++;
            } else if (Rec[i] == 0x7D && Rec[i + 1] == 0x05) {
                Return[i - len] = 0x2A;
                len++;
                i++;
            } else {
                Return[i - len] = Rec[i];
            }
        }
        return Return;
    }

    public static byte[] ZY(byte[] Rec) {
        int len = Rec.length;
        for (byte aRec : Rec) {
            if (aRec == 0x7D) {
                len++;
            } else if (aRec == 0x5B) {
                len++;
            } else if (aRec == 0x5D) {
                len++;
            } else if (aRec == 0x2C) {
                len++;
            } else if (aRec == 0x2A) {
                len++;
            }
        }
        byte[] Return = new byte[len];
        len = 0;
        for (int i = 0; i < Rec.length; i++) {
            if (Rec[i] == 0x7D) {
                Return[i + len] = 0x7D;
                Return[i + len + 1] = 0x01;
                len++;
            } else if (Rec[i] == 0x5B) {
                Return[i + len] = 0x7D;
                Return[i + len + 1] = 0x02;
                len++;
            } else if (Rec[i] == 0x5D) {
                Return[i + len] = 0x7D;
                Return[i + len + 1] = 0x03;
                len++;
            } else if (Rec[i] == 0x2C) {
                Return[i + len] = 0x7D;
                Return[i + len + 1] = 0x04;
                len++;
            } else if (Rec[i] == 0x2A) {
                Return[i + len] = 0x7D;
                Return[i + len + 1] = 0x05;
                len++;
            } else {
                Return[i + len] = Rec[i];
            }
        }
        return Return;
    }

}
