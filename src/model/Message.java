package model;

import starter.ServerMain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Message {
    private final String message;
    private final String user;
    private final String time;

    public Message(String message, String user, LocalDateTime time) {
        this.message = filter(message);
        this.user = user;
        this.time = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss").format(time);
    }

    private String filter(String message){
        List<String> words = Arrays.asList(message.split(" "));
        return words.stream().map(word-> {
            if(!ServerMain.forbiddenWords.contains(word.toLowerCase()))
                return word;
            int wordLength = word.length();
            char first = word.charAt(0);
            char last = word.charAt(wordLength-1);
            return first + String.join("", Collections.nCopies(wordLength-2,"*")) + last;
        }).collect(Collectors.joining(" "));
    }

    @Override
    public String toString() {
        return time + " " + user + ": " + message;
    }
}
