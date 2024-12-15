package edu.alexu.cse.dripmeup.service;

import java.util.HashMap;
import java.util.Map;

public class ResponseBodyMessage {
    public static Map<String, String>  message(String message){
        Map<String, String> messageResponse = new HashMap<>();
        messageResponse.put("message", message);
        return messageResponse;
    }
    public static Map<String, String>  error(String error){
        Map<String, String> messageResponse = new HashMap<>();
        messageResponse.put("error", error);
        return messageResponse;
    }
}
