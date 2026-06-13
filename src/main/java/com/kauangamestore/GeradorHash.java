package com.kauangamestore;

import com.kauangamestore.service.HashService;
import com.kauangamestore.service.HashServiceImpl;

public class GeradorHash {
    public static void main(String[] args) {
        HashService service = new HashServiceImpl();

        String hash = service.getHashSenha("123456");
        System.out.println("COPIE ESTE HASH PARA O SEU IMPORT.SQL:");
        System.out.println(hash);
    }
}