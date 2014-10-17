/**
 * 
 */
package com.ibm.broker.crytpo.runtime;



import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import org.bouncycastle.openpgp.PGPException;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbInputTerminal;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbNode;
import com.ibm.broker.plugin.MbNodeInterface;
import com.ibm.broker.plugin.MbUserException;
import com.ibm.wmb.crypto.utils.GenerateEncrptionKeys;

/**
 * @author Benu
 *
 */

public class GenerateEncryptionKeysImpl extends MbNode implements
		MbNodeInterface {

	/**
	 * 
	 */
	
	
	String publicKeyPath;
	String privateKeyPath;	
	String keyIdentity;
	String keyPassphrase;
	boolean isPassprahseEncoded = false;
	int keySize;
	String algorithm = "RSA";
		
	

	public String getAlgorithm() {
		return this.algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getPublicKeyPath() {
		return publicKeyPath;
	}

	public void setPublicKeyPath(String publicKeyPath) {
		this.publicKeyPath = publicKeyPath;
	}

	public String getPrivateKeyPath() {
		return privateKeyPath;
	}

	public void setPrivateKeyPath(String privateKeyPath) {
		this.privateKeyPath = privateKeyPath;
	}

	public String getKeyIdentity() {
		return keyIdentity;
	}

	public void setKeyIdentity(String keyIdentity) {
		this.keyIdentity = keyIdentity;
	}

	public String getKeyPassphrase() {
		return keyPassphrase;
	}

	public void setKeyPassphrase(String keyPassphrase) {
		this.keyPassphrase = keyPassphrase;
	}

	public String  getIsPassprahseEncoded() {
		return Boolean.toString(isPassprahseEncoded);
	}

	public void setIsPassprahseEncoded(String isPassprahseEncoded) {
		this.isPassprahseEncoded = Boolean.parseBoolean(isPassprahseEncoded);
	}

	public String getKeySize() {
		return Integer.toString(keySize);
	}

	public void setKeySize(String keySize) {
		this.keySize = Integer.parseInt(keySize);
	}

	public GenerateEncryptionKeysImpl() throws MbException{
		// TODO Auto-generated constructor stub
		createInputTerminal ("in");

	}

	/* (non-Javadoc)
	 * @see com.ibm.broker.plugin.MbNodeInterface#evaluate(com.ibm.broker.plugin.MbMessageAssembly, com.ibm.broker.plugin.MbInputTerminal)
	 */
	@SuppressWarnings("unused")
	@Override
	public void evaluate(MbMessageAssembly inAssembly, MbInputTerminal arg1)
			throws MbException {
			MbMessage inMessage = inAssembly.getMessage();
			if (!this.algorithm.equalsIgnoreCase("RSA"))
			{
				throw new MbUserException(this.getClass().getName(),
						"evaluate",
						inMessage.getRootElement().toString(),
						"Only RSA Alogrithm is support as of now",
						"CRITICAL", null );
				
			}
				
			try {							
				
				GenerateEncrptionKeys genKey = new GenerateEncrptionKeys();
				genKey.generatePGPRSAKeys(getAlgorithm(),getPublicKeyPath(), getPrivateKeyPath(), getKeyIdentity(), getKeyPassphrase(), 
											Boolean.parseBoolean(getIsPassprahseEncoded()), Integer.parseInt(getKeySize()));
			} 
			catch (InvalidKeyException e) {
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
			
			finally {

				
			}


	}
	
	public static String getNodeName()
	{
	   return "GenerateKeysNode";
	}

}
