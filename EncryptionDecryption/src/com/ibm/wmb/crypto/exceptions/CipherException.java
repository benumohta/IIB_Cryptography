package com.ibm.wmb.crypto.exceptions;
import org.bouncycastle.crypto.InvalidCipherTextException;

public class CipherException extends InvalidCipherTextException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7537828939992957299L;
	
	public CipherException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
