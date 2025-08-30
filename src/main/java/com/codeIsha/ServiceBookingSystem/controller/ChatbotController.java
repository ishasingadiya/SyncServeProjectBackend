package com.codeIsha.ServiceBookingSystem.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatbotController {

    private String api_key = "AIzaSyBe1cZ-bh0zqs6Y_ey3auRjogFliJFIIj8";

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> request) {
        String userMessage = request.get("prompt");
        String botResponse;
        try {
            botResponse = processUserMessage(userMessage);
        } catch (IOException e) {
            e.printStackTrace();
            botResponse = "Error processing your request.";
        }
        Map<String, String> response = new HashMap<>();
        response.put("response", botResponse);
        return ResponseEntity.ok(response);
    }

    public String processUserMessage(String input) throws IOException {
        URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + api_key);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        connection.setDoOutput(true);

        String systemInstruction = "You are syncbot a help desk at SyncServe,\n" +
                "\n" +
                "SyncServe Overview\n" +
                "SyncServe is a service management platform that simplifies lives by automating inquiries, enabling real-time service tracking, and enhancing customer satisfaction through seamless operations and intelligent automation.\n" +
                "\n" +
                "Pricing\n" +
                "Services start at ₹499, offering affordability without compromising on quality.\n" +
                "\n" +
                "Services Offered\n" +
                "- Plumbing\n" +
                "- Home cleaning\n" +
                "- AC repair & servicing\n" +
                "- Painting & waterproofing\n" +
                "- Electronics troubleshooting\n" +
                "- TV repair & installation\n" +
                "- etc..\n" +
                "\n" +
                "When user says thankyou replay it to a welcome how can i assist"+
                "if user says ok or related to this tell them how can i assist for further questions"+
                "The user will not be directly book an service they first register or if they registered then have to login and then book a service"+
                "Core Values\n" +
                "- Efficiency, professionalism, and customer satisfaction.\n" +
                "You are not allowed to answer any other question which is not related to SyncServe and its services.";

        String requestBody = "{" +
                "\"contents\": [" +
                "{" +
                "\"parts\": [" +
                "{" +
                "\"text\": \"" + systemInstruction + "\\n" + input + "\"" +
                "}" +
                "]" +
                "}" +
                "]" +
                "}";

        try (OutputStream os = connection.getOutputStream();
             OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(osw)) {
            writer.write(requestBody);
            writer.flush();
        }

        StringBuilder response = new StringBuilder();
        try (InputStream is = connection.getInputStream();
             InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        connection.disconnect();

        // Parse the JSON response and extract the text
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.toString());
        JsonNode candidates = root.path("candidates");

        if (candidates.isArray() && candidates.size() > 0) {
            JsonNode parts = candidates.get(0).path("content").path("parts");
            if (parts.isArray() && parts.size() > 0) {
                return parts.get(0).path("text").asText();
            }
        }

        return "Unable to extract response from Gemini API.";
    }

}