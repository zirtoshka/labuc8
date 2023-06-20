package common.crypto;

import common.exceptions.EncryptionException;

import java.math.BigInteger;
import java.security.MessageDigest;

public class SHA1Generator {
    public static String hash(String str) throws EncryptionException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(str.getBytes("utf8"));
            return String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (Exception e){
            throw new EncryptionException();
        }
    }
}
