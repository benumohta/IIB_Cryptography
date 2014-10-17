/**
 * 
 */
package com.ibm.wmb.crypto;
import java.io.IOException;

import com.ibm.wmb.crypto.exceptions.CipherException;
import com.ibm.wmb.crypto.exceptions.KeyNotFoundException;

/**
 * @author bg86690
 *
 */
public interface BasicRSAEncryptionDecryptionIntf {
	

	
	public String encryptMessage(byte[] dataToEncrypt,String publicKeyFileWithPath) throws KeyNotFoundException, IOException,CipherException, Exception;
	
	public byte[] decryptMessage(String encryptedText, String privateKeyFileWithPath)throws KeyNotFoundException,IOException,CipherException, Throwable;
	

}
