package com.ibm.wmb.crypto;

import java.io.IOException;
import java.security.NoSuchProviderException;

import org.bouncycastle.openpgp.PGPException;

import com.ibm.wmb.crypto.exceptions.CipherException;
import com.ibm.wmb.crypto.exceptions.KeyNotFoundException;

public interface PGPRSAEncrptionDecryptionIntf 
{

	
	public String encryptMessage(byte[] dataToEncrypt,String pgpPublicKey) throws KeyNotFoundException, IOException,CipherException, Exception;
	
	public byte[] decryptMessage(String encryptedText,String secretKeyFilePath,String secretKeyPassphrase, boolean isPassphraseEncoded)throws KeyNotFoundException, IOException, CipherException, PGPException, NoSuchProviderException; 

}
