package com.ibm.wmb.crypto.utils;

import java.io.IOException;
import java.security.NoSuchProviderException;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;




public interface ReadPGPRSAKeysIntf {
	
	public PGPPublicKey readPublicKey(String rsaPublicKeyPath,String rsaKeyStorePass) throws IOException, PGPException;
	public PGPPrivateKey readPrivateKey(String readSecretKeyPath,String rsaKeyStorePass) throws IOException, PGPException,NoSuchProviderException;

}
