package com.kauangamestore.service;

import jakarta.enterprise.context.ApplicationScoped;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@ApplicationScoped
public class HashServiceImpl implements HashService {

    private String salt = "#$127732&Games";
    private Integer iterationCount = 403;
    private Integer keyLength = 512;

    @Override
    public String getHashSenha(String senha) {
        try {
            byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
                    .generateSecret(new PBEKeySpec(senha.toCharArray(),
                            salt.getBytes(),
                            iterationCount,
                            keyLength))
                    .getEncoded();
            return Base64.getEncoder().encodeToString(result);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Erro ao gerar o hash da senha", e);
        }
    }
}