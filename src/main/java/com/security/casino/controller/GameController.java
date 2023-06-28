package com.security.casino.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Controller
public class GameController {

    /* Receives a random string from client and send the output of sha256 algorithm (digest). sha256 takes as parameter
    the concatenated string of a random number, which ranges from 1 to 6, random string of client and a random string of server.
     */
    @GetMapping("/digest")
    @ResponseBody
    public byte[] sendDigest (@RequestParam(name = "clientRandomString") String clientRandomString) {
        Random randomNumberGenerator = new Random (System.currentTimeMillis());
        //nextInt is going to produce a number which ranges from 1 to 6
        int randomNumber = randomNumberGenerator.nextInt(1, 7);
        //Generates a random string with 7 characters
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String serverRandomString = new String(array, Charset.forName("UTF-8"));
        //Concatenates random number, random string of server and random string of client.
        String concatenatedString = randomNumber + serverRandomString + clientRandomString;
        byte [] response = new byte [15];
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            response = digest.digest(concatenatedString.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {

        }
        return response;
    }

}
