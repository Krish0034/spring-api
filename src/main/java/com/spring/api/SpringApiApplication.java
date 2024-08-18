package com.spring.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.ssl.SslAutoConfiguration;
import com.spring.api.util.EncryptionUtil;
@SpringBootApplication(exclude = {SslAutoConfiguration.class})
public class SpringApiApplication {
	


	public static void main(String[] args) {
		SpringApplication.run(SpringApiApplication.class, args);
		try {
            String originalText = "Hello World!";
            
            // Encrypt the text
            String encryptedText = EncryptionUtil.encryptText(originalText);
            System.out.println("Encrypted: " + encryptedText);

            // Decrypt the text
            String decryptedText = EncryptionUtil.decryptText("7bBr5GJoZYwvJgVovNKhFw==");
            System.out.println("Decrypted: " + decryptedText);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Application start....");

	}

}
