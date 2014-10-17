package com.ibm.wmb;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;

import org.bouncycastle.openpgp.PGPException;

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.*;
import com.ibm.wmb.crypto.utils.GenerateEncrptionKeys;;

public class GeneratePGPRSAKeys_GenratePGPRSAKeys extends MbJavaComputeNode {
	
	private static String RSA_PUBLIC_KEY_FILEPATH; 	
	private static String RSA_PRIVATE_KEY_FILEPATH;	
	private static String RSA_PUBLIC_KEY_FILENAME;
	private static String RSA_PRIVATE_KEY_FILENAME;	
	private static Integer KEY_SIZE;
	private static String RSA_KEY_INDENTTIY;
	private static String RSA_KEY_PASSPHRASE;
	private static Boolean IS_BASE64ENCODED;
	
	public void onInitialize() throws MbException
	{
		RSA_PUBLIC_KEY_FILEPATH = (String)getUserDefinedAttribute("RSA_PUBLIC_KEY_FILEPATH");
		RSA_PRIVATE_KEY_FILEPATH = (String)getUserDefinedAttribute("RSA_PRIVATE_KEY_FILEPATH");
		RSA_PUBLIC_KEY_FILENAME = (String)getUserDefinedAttribute("RSA_PUBLIC_KEY_FILENAME");
		RSA_PRIVATE_KEY_FILENAME = (String)getUserDefinedAttribute("RSA_PRIVATE_KEY_FILENAME");
		KEY_SIZE = (Integer)getUserDefinedAttribute("KEY_SIZE");
		RSA_KEY_INDENTTIY = (String)getUserDefinedAttribute("RSA_KEY_INDENTTIY");
		RSA_KEY_PASSPHRASE = (String)getUserDefinedAttribute("RSA_KEY_PASSPHRASE");
		IS_BASE64ENCODED = (Boolean)getUserDefinedAttribute("IS_BASE64ENCODED");
	}
	
	@SuppressWarnings("unused")
	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		MbOutputTerminal out = getOutputTerminal("out");
		MbOutputTerminal alt = getOutputTerminal("alternate");

		
		MbMessage inMessage = inAssembly.getMessage();

		// create new message
		MbMessage outMessage = new MbMessage();
		MbMessageAssembly outAssembly = new MbMessageAssembly(inAssembly,
				outMessage);
		
		String publicKeyFileNameWithPath = RSA_PUBLIC_KEY_FILEPATH+java.io.File.separator+RSA_PUBLIC_KEY_FILENAME;
		String secretKeyFileNameWithPath = RSA_PRIVATE_KEY_FILEPATH+java.io.File.separator+RSA_PRIVATE_KEY_FILENAME;
		try {
			GenerateEncrptionKeys genKey = new GenerateEncrptionKeys();
			try {
				genKey.generatePGPRSAKeys("RSA",publicKeyFileNameWithPath, secretKeyFileNameWithPath, RSA_KEY_INDENTTIY, RSA_KEY_PASSPHRASE, IS_BASE64ENCODED, KEY_SIZE);
			} catch (InvalidKeyException e) {
				throw new MbUserException(this.getClass().getName(),
						"evaluate",
						inMessage.getRootElement().toString(),
						"message key","Could Not generate keys "+ e.getMessage() + " CRITICAL",
						null );
			} catch (NoSuchAlgorithmException e) {
				throw new MbUserException(this.getClass().getName(),
						"evaluate",
						inMessage.getRootElement().toString(),
						"message key","Could Not generate keys "+ e.getMessage() + " CRITICAL",
						null );
			} catch (NoSuchProviderException e) {
				throw new MbUserException(this.getClass().getName(),
						"evaluate",
						inMessage.getRootElement().toString(),
						"message key","Could Not generate keys "+ e.getMessage() + " CRITICAL",
						null );
			} catch (SignatureException e) {
				throw new MbUserException(this.getClass().getName(),
						"evaluate",
						inMessage.getRootElement().toString(),
						"message key","Could Not generate keys "+ e.getMessage() + " CRITICAL",
						null );
			} catch (IOException e) {
				throw new MbUserException(this.getClass().getName(),
						"evaluate",
						inMessage.getRootElement().toString(),
						"message key","Could Not generate keys "+ e.getMessage() + " CRITICAL",
						null );
			} catch (PGPException e) {
				throw new MbUserException(this.getClass().getName(),
						"evaluate",
						inMessage.getRootElement().toString(),
						"message key","Could Not generate keys "+ e.getMessage() + " CRITICAL",
						null );
			}
			catch (Exception e) {
				String exceptionText = e.getMessage();						
				throw new MbUserException(this.getClass().getName(),
						"evaluate",
						inMessage.getRootElement().toString(),
						"message key","Could Not generate keys "+ e.getMessage() + " CRITICAL",
						null );
			}

		} finally {

			// clear the outMessage even if there's an exception
			outMessage.clearMessage();
		}
	}

	public void copyMessageHeaders(MbMessage inMessage, MbMessage outMessage)
			throws MbException {
		MbElement outRoot = outMessage.getRootElement();

		// iterate though the headers starting with the first child of the root
		// element
		MbElement header = inMessage.getRootElement().getFirstChild();
		while (header != null && header.getNextSibling() != null) // stop before
		// the last
		// child
		// (body)
		{
			// copy the header and add it to the out message
			outRoot.addAsLastChild(header.copy());
			// move along to next header
			header = header.getNextSibling();
		}
	}

}
