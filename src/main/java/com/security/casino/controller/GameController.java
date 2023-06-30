package com.security.casino.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@RestController
public class GameController {

    private int Na;
    private String ra;
    private String rb;
    private String h_commit;

    @GetMapping("/game/roll")
    public RollResponse roll(@RequestParam String rb) {
        // Generate a random number Na between 1-6
        Na = (int) (Math.random() * 6) + 1;

        this.rb=rb;
        // Generate a random string ra
        ra = generateRandomString();

        // Calculate h_commit = SHA2_256("Na" || ra || rb)
        String concatenatedString = Na + ra + rb;
        h_commit = calculateSHA256(concatenatedString);

        return new RollResponse(h_commit, Na, ra);
    }

    @GetMapping("/game/result")
    public ResultResponse result(@RequestParam int Nb) {
        // Calculate h2 = SHA2_256("Na" || ra || rb)
        String concatenatedString = Na + ra + rb;

        // Check if Nb > Na
        if (Nb > Na) {
            return new ResultResponse(true, concatenatedString);
        }
        // Check if Nb < Na
        else if (Nb < Na) {
            return new ResultResponse(false, concatenatedString);
        }
        // No one won
        else {
            return new ResultResponse(null, concatenatedString);
        }
    }

    private String generateRandomString() {
        byte[] array = new byte[16]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        return generatedString;
    }

    private static String calculateSHA256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle exception appropriately
            return null;
        }
    }

    static class RollResponse {
        private String h_commit;
        private int Na;

        private String ra;

        public RollResponse(String h_commit, int Na, String ra) {
            this.h_commit = h_commit;
            this.Na = Na;
            this.ra = ra;
        }

        public String getH_commit() {
            return h_commit;
        }

        public String getRa() {
            return ra;
        }

        public void setRa(String ra) {
            this.ra = ra;
        }

        public int getNa() {
            return Na;
        }
    }

    static class ResultResponse {
        private Boolean win;
        private String concatenatedString;

        public ResultResponse(Boolean win, String concatenatedString) {
            this.win = win;
            this.concatenatedString = concatenatedString;
        }

        public Boolean getWin() {
            return win;
        }

        public String getConcatenatedString() {
            return concatenatedString;
        }
    }
}
