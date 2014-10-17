package com.ibm.wmb;

import java.io.IOException;

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.*;
import com.ibm.wmb.crypto.BasicRSAEncryptDecryptData;
import com.ibm.wmb.crypto.exceptions.CipherException;
import com.ibm.wmb.crypto.exceptions.KeyNotFoundException;

public class BasicRSADecryption_Decrypt extends MbJavaComputeNode {

	private static String RSA_PRIVATE_KEY_FILE;
	
	private String encryptedText;
	private byte[] decryptedData;

	public void onInitialize() throws MbException
	{
		RSA_PRIVATE_KEY_FILE = (String)getUserDefinedAttribute("RSA_PRIVATE_KEY_FILE");		
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
						
			BasicRSAEncryptDecryptData encyrptData = new BasicRSAEncryptDecryptData();
			decryptedData = encyrptData.decryptMessage(encryptedText,RSA_PRIVATE_KEY_FILE);
			
			MbElement outRootParser = outMessage.getRootElement().createElementAsLastChild(MbBLOB.PARSER_NAME);
			
			MbElement outBody 		= outRootParser.createElementAsLastChild(MbElement.TYPE_NAME_VALUE,"BLOB",decryptedData);
			// End of user code
			// ----------------------------------------------------------

			// The following should only be changed
			// if not propagating message to the 'out' terminal
			out.propagate(outAssembly);

		} catch (CipherException e) {
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
		} catch (Throwable e) {
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
