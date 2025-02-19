package com.cpt.payments.service.Impl;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import com.cpt.payments.service.Interfaces.HmacSha256Service;

@Service														       //service called using interface
public class HmacSha256ServiceImpl implements HmacSha256Service {  

	private static final String HMAC_SHA256 = "HmacSHA256";

	@Override
	public String calculateHmac(String jsonInput) // json input given here
	
	{
		
		String secretKey="this is my key"; //secretkey + hmac
		
		
		try {
            // Define the HMAC SHA-256 algorithm
            Mac sha256Hmac = Mac.getInstance(HMAC_SHA256);

            // Initialize the HMAC with the secret key
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            sha256Hmac.init(keySpec);

            // Compute the HMAC on the input message
            byte[] hmacData = sha256Hmac.doFinal(jsonInput.getBytes(StandardCharsets.UTF_8));

            // Convert the result to a Base64-encoded string
            String signature=Base64.getEncoder().encodeToString(hmacData);
            		
            		
            return signature;								// signature using key & json input

        } catch (NoSuchAlgorithmException|InvalidKeyException e) {
            e.printStackTrace();
        }
		System.out.println("HmacSha256Service failed to generate signature null");
		return null;
	}

	@Override
	public boolean verifyHmac(String data, String recievedHmac) {

		String genratedSignature=calculateHmac(data);     // generate signature using data
		System.out.println("recievedHmac :"+recievedHmac);
		System.out.println("genratedSignature : "+genratedSignature);
		
		if(genratedSignature !=null && genratedSignature.equals(recievedHmac))   //compare generated & received signature
		{
			System.out.println("Hmacsha signature is valid");
			
			return true;
		}
		System.out.println("Hmacsha signature is invalid");
		
		return false;
	}

}
