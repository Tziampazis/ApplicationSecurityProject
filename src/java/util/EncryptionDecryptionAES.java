/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;


import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionDecryptionAES {
static Cipher cipher;


static String secretKey = "theencryptionkey";
static String encodedString = Base64.getEncoder().encodeToString(secretKey.getBytes());
static byte[] decodedKey = Base64.getDecoder().decode(encodedString);
// rebuild key using SecretKeySpec
static SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); 

public static String encrypt(String plainText)
throws Exception {
 cipher = Cipher.getInstance("AES");
 byte[] plainTextByte = plainText.getBytes();
 cipher.init(Cipher.ENCRYPT_MODE, originalKey);
 byte[] encryptedByte = cipher.doFinal(plainTextByte);
 Base64.Encoder encoder = Base64.getEncoder();
 String encryptedText = encoder.encodeToString(encryptedByte);
 return encryptedText;
}

public static String decrypt(String encryptedText)
throws Exception {
  cipher = Cipher.getInstance("AES");   
 Base64.Decoder decoder = Base64.getDecoder();
 byte[] encryptedTextByte = decoder.decode(encryptedText);
 cipher.init(Cipher.DECRYPT_MODE, originalKey);
 byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
 String decryptedText = new String(decryptedByte);
 return decryptedText;
}


//public static void main(String[] args) throws Exception {
//// KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//// keyGenerator.init(128);
//// SecretKey secretKey = keyGenerator.generateKey();
// cipher = Cipher.getInstance("AES"); 
//
//    System.out.println("key " + originalKey );
// String plainText =  new String("AES Symmetric Encryption Decryption");
// System.out.println("Plain Text Before Encryption: " + plainText);
//
// String encryptedText = encrypt(plainText);
// System.out.println("Encrypted Text After Encryption: " + encryptedText);
//
// String decryptedText = decrypt(encryptedText);
// System.out.println("Decrypted Text After Decryption: " + decryptedText);
//}

}
