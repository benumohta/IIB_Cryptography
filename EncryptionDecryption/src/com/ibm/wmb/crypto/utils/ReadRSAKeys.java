package com.ibm.wmb.crypto.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Iterator;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;

public class ReadRSAKeys implements ReadPGPRSAKeysIntf {

	
	//public key variables.
	private PGPPublicKeyRingCollection pgpPublicKeyRingCollection;
	private PGPPublicKeyRing  pgpPublicKeyRing;
	private PGPPublicKey pgpRSAPublicKey;
	
	//private key variables
	private PGPSecretKeyRingCollection pgpSecretKeyRingCollection;
	private PGPSecretKeyRing pgpSecretKeyRing;
	private PGPSecretKey pgpSecretKey;
	private PGPPrivateKey pgpRSAPrivateKey;
	
	public static void main(String[] args)
	{
		ReadRSAKeys rd = new ReadRSAKeys();
		try {
			System.out.println(rd.readPublicKey(ConfigProperties.RSA_PUBLIC_KEY_FILEPATH,ConfigProperties.RSA_PASSWORD).getKeyID());
			System.out.println(rd.readPrivateKey(ConfigProperties.RSA_PRIVATE_KEY_FILEPATH, ConfigProperties.RSA_PASSWORD).getKeyID());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PGPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PGPPublicKey readPublicKey(String rsaPublicKeyPath,
			String rsaKeyStorePass) throws IOException, PGPException
			
	{
		InputStream keyIn = new BufferedInputStream(new FileInputStream(rsaPublicKeyPath));		 
		pgpPublicKeyRingCollection =  new PGPPublicKeyRingCollection(PGPUtil.getDecoderStream(keyIn));
		 Iterator<PGPPublicKeyRing> keyRingIter = pgpPublicKeyRingCollection.getKeyRings();
	        while (keyRingIter.hasNext())
	        {
	        	pgpPublicKeyRing = keyRingIter.next();

	            Iterator<PGPPublicKey> keyIter = pgpPublicKeyRing.getPublicKeys();
	            while (keyIter.hasNext())
	            {
	            	pgpRSAPublicKey = keyIter.next();

	                if (pgpRSAPublicKey.isEncryptionKey())
	                {
	                    return pgpRSAPublicKey;
	                }
	            }
	        }
	        throw new IllegalArgumentException("Can't find encryption key in key ring.");		
	}

	@SuppressWarnings({"unchecked"})
	@Override
	public PGPPrivateKey readPrivateKey(String rsaSecretKeyPath,
			String rsaKeyStorePass) throws IOException, PGPException, NoSuchProviderException 
	{
		InputStream keyIn 				  = new BufferedInputStream(new FileInputStream(rsaSecretKeyPath));		
		pgpSecretKeyRingCollection  	  = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(keyIn));

		Iterator<PGPSecretKeyRing> keyRingIter = pgpSecretKeyRingCollection.getKeyRings();
		
		boolean loop = true;

		while (keyRingIter.hasNext()&&loop)
        {
            pgpSecretKeyRing = (PGPSecretKeyRing)keyRingIter.next();
            Iterator<PGPSecretKey> keyIter = pgpSecretKeyRing.getSecretKeys();
            while (keyIter.hasNext())
            {
            	pgpSecretKey = (PGPSecretKey)keyIter.next();
            	
            	if (pgpSecretKey.isSigningKey())
            	{
            		loop = false;
            		break;
            	}            		
               
            }
            
        }
		if (pgpSecretKey.equals(null))
		{
			throw new IllegalArgumentException("Can't find signing key in key ring.");
		}
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		PBESecretKeyDecryptor decryptor = new BcPBESecretKeyDecryptorBuilder(new BcPGPDigestCalculatorProvider()).build(rsaKeyStorePass.toCharArray());
		pgpRSAPrivateKey = pgpSecretKey.extractPrivateKey(decryptor);
		
		        
		
		return pgpRSAPrivateKey;
	}
	
	 public static PGPPrivateKey findSecretKey(PGPSecretKeyRingCollection pgpSec, long keyID, char[] pass)
     throws PGPException, NoSuchProviderException
	 {
 		 PBESecretKeyDecryptor decryptor = new BcPBESecretKeyDecryptorBuilder(new BcPGPDigestCalculatorProvider()).build(pass);

	     PGPSecretKey pgpSecKey = pgpSec.getSecretKey(keyID);
	
	     if (pgpSecKey == null)
	     {
	         return null;
	     }
	
	     return pgpSecKey.extractPrivateKey(decryptor);
	 }
}
