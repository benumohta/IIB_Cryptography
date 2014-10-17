package com.ibm.wmb;

import java.io.IOException;
import java.security.NoSuchProviderException;

import org.bouncycastle.openpgp.PGPException;

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.*;
import com.ibm.wmb.crypto.BasicRSAEncryptDecryptData;
import com.ibm.wmb.crypto.PGPRSAEncryptionDecryptionData;
import com.ibm.wmb.crypto.exceptions.CipherException;
import com.ibm.wmb.crypto.exceptions.KeyNotFoundException;

public class PGPDecryption_PGPDecrypt extends MbJavaComputeNode {

	private String encryptedText;
	private byte[] decryptedData;
	
	private static String SECRET_KEY_FILE;
	private static String KEY_PASSPRHASE;
	private static Boolean IS_PASSPHRASE_ENCODED;
	
	
	public void onInitialize() throws MbException
	{
		SECRET_KEY_FILE = (String)getUserDefinedAttribute("SECRET_KEY_FILE");
		KEY_PASSPRHASE = (String)getUserDefinedAttribute("KEY_PASSPRHASE");
		IS_PASSPHRASE_ENCODED = (Boolean)getUserDefinedAttribute("IS_PASSPHRASE_ENCODED");
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

		try {
			// optionally copy message headers
			copyMessageHeaders(inMessage, outMessage);

			// ----------------------------------------------------------
			// Add user code below
			MbElement inputRoot = inMessage.getRootElement();			
			encryptedText = new String((byte[])(inputRoot.getFirstElementByPath("/BLOB/BLOB").getValue()));			
						
			PGPRSAEncryptionDecryptionData decryptData = new PGPRSAEncryptionDecryptionData();
			decryptedData = decryptData.decryptMessage(encryptedText,SECRET_KEY_FILE,KEY_PASSPRHASE,IS_PASSPHRASE_ENCODED.booleanValue());
			
			MbElement outRootParser = outMessage.getRootElement().createElementAsLastChild(MbBLOB.PARSER_NAME);			
			MbElement outBody 		= outRootParser.createElementAsLastChild(MbElement.TYPE_NAME_VALUE,"BLOB",decryptedData);
			out.propagate(outAssembly);

		} catch (CipherException e) {
			throw new MbUserException(this.getClass().getName(),
					"evaluate",
					inMessage.getRootElement().toString(),
					"message key","Error in Decrypting message : "+ e.getMessage() + " CRITICAL",
					null );
		} catch (NoSuchProviderException e) {
			throw new MbUserException(this.getClass().getName(),
					"evaluate",
					inMessage.getRootElement().toString(),
					"message key","Error in Decrypting message : "+ e.getMessage() + " CRITICAL",
					null );
		} catch (KeyNotFoundException e) {
			throw new MbUserException(this.getClass().getName(),
					"evaluate",
					inMessage.getRootElement().toString(),
					"message key","Error in Decrypting message : "+ e.getMessage() + " CRITICAL",
					null );
		} catch (IOException e) {
			throw new MbUserException(this.getClass().getName(),
					"evaluate",
					inMessage.getRootElement().toString(),
					"message key","Error in Decrypting message : "+ e.getMessage() + " CRITICAL",
					null );
		} catch (PGPException e) {
			throw new MbUserException(this.getClass().getName(),
					"evaluate",
					inMessage.getRootElement().toString(),
					"message key","Error in Decrypting message : "+ e.getMessage() + " CRITICAL",
					null );
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
