package com.mycompany.template.utils;

import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: azee
 * Date: 10/24/13
 * Time: 6:01 PM
 */
@Service
public class StringUtils {

    /**
     * Encode a striong into MD5
     * @param input
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String getMd5String(String input) throws NoSuchAlgorithmException {
        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(input.getBytes(Charset.forName("UTF8")));
        final byte[] resultByte = messageDigest.digest();
        final String result = Hex.encodeHexString(resultByte);
        return result;
    }

}
