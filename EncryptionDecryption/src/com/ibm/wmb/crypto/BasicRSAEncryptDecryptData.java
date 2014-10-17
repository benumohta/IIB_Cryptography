package com.ibm.wmb.crypto;


import java.io.IOException;
import java.security.Security;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;

import com.ibm.wmb.crypto.exceptions.CipherException;
import com.ibm.wmb.crypto.exceptions.KeyNotFoundException;
import com.ibm.wmb.crypto.utils.ConfigProperties;
import com.ibm.wmb.crypto.utils.KeysUtilities;



public class BasicRSAEncryptDecryptData implements BasicRSAEncryptionDecryptionIntf {

	/**
	 * @param args
	 */	
	
	
	private AsymmetricKeyParameter rsaPublicKey;
	private AsymmetricBlockCipher cipher;
	private String encryptedText;
	private KeysUtilities keyutils;
	private AsymmetricKeyParameter rsaPrivateKey;
	private String decyptedText;
	
	
	public BasicRSAEncryptDecryptData()
	{
		super();
		keyutils = new KeysUtilities();
	}
	
	
	@Override
	public byte[] decryptMessage(String encryptedText ,String privateKeyFileWithPath) throws KeyNotFoundException, IOException, CipherException, Throwable 
	{
		decyptedText ="";
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		String key = keyutils.getKey(privateKeyFileWithPath);
		if (key.equals("")|| key.equals(null))
		{
			throw new KeyNotFoundException(ConfigProperties.ERR_NO_KEY_READ);
		}				
		
		rsaPrivateKey = (AsymmetricKeyParameter) PrivateKeyFactory.createKey(keyutils.decode(key));
		cipher = new RSAEngine();
		cipher = new org.bouncycastle.crypto.encodings.PKCS1Encoding(cipher);
		cipher.init(false, rsaPrivateKey);
		
		byte[] messageBytes = keyutils.decode(encryptedText);
		
		
		int i = 0;
		int len = cipher.getInputBlockSize();
		try
		{
			while (i < messageBytes.length)
			{
				if (i + len > messageBytes.length)
					len = messageBytes.length - i;

				byte[] hexEncodedCipher = cipher.processBlock(messageBytes, i, len);
				decyptedText = decyptedText + new String(hexEncodedCipher);
				i += cipher.getInputBlockSize();
			}
		}
		catch (InvalidCipherTextException e) {
			new CipherException(e.getMessage());
		}
		return decyptedText.getBytes();
	}
	
	@Override
	public String encryptMessage(byte[] dataToEncrypt,String publicKeyFileWithPath) throws KeyNotFoundException, IOException, CipherException, Exception
	{
		
		 try
         {
			
			encryptedText = "";	
			
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            String key = keyutils.getKey(publicKeyFileWithPath);
            
            if (key.equals("") || key.equals(null))
            {
            	throw new KeyNotFoundException(ConfigProperties.ERR_NO_KEY_READ);
            }
            
            
            rsaPublicKey = (AsymmetricKeyParameter) PublicKeyFactory.createKey(keyutils.decode(key));
            cipher = new RSAEngine();
            cipher = new org.bouncycastle.crypto.encodings.PKCS1Encoding(cipher);
            cipher.init(true, rsaPublicKey);
 
            
            int i = 0;
            int len = cipher.getInputBlockSize();
           
	            while (i < dataToEncrypt.length)
	            {
	                if (i + len > dataToEncrypt.length)
	                    len = dataToEncrypt.length - i;
	 
	                byte[] hexEncodedCipher = cipher.processBlock(dataToEncrypt, i, len);
	                encryptedText = encryptedText + keyutils.encode(hexEncodedCipher);
	                i += cipher.getInputBlockSize();
	            }
            }
	        catch(InvalidCipherTextException e)
	        {
	        	throw new CipherException(e.getMessage());
	        }
	        catch (Throwable t) {
				  throw new Exception(t.getMessage());
			}
		return encryptedText;
	}

}
