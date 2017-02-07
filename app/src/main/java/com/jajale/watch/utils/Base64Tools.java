package com.jajale.watch.utils;

import java.io.IOException;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

/**
 * Created by athena on 2015/12/6.
 * Email: lizhiqiang@bjjajale.com
 */



public class Base64Tools {

    /**
     * base 64 encode
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes){
        return new BASE64Encoder().encode(bytes);
    }


    /**
     * base 64 decode
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code){
        try {
            return CMethod.isEmpty(base64Code) ? null : new BASE64Decoder().decodeBuffer(base64Code);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
