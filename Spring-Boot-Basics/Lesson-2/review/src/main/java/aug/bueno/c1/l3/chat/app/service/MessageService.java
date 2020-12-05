package aug.bueno.c1.l3.chat.app.service;

import aug.bueno.c1.l3.chat.app.model.ChatForm;
import aug.bueno.c1.l3.chat.app.model.ChatMessage;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MessageService {

    private List<ChatMessage> messages;

    public void addMessage(final ChatForm chatForm) {

        ChatMessage newMessage = new ChatMessage();
        newMessage.setUserName(chatForm.getUserName());
        newMessage.setMessageType(chatForm.getMessageType());

        switch (chatForm.getMessageType()) {
            case "Say":
                newMessage.setMessage(chatForm.getMessage());
                break;
            case "Shout":
                newMessage.setMessage(chatForm.getMessage().toUpperCase());
                break;
            case "Whisper":
                newMessage.setMessage(chatForm.getMessage().toLowerCase());
                break;
        }

        if (!filterForbiddenWorld(newMessage.getMessage(), this.forbiddenWords())) {
            this.messages.add(newMessage);
        }
    }


    public List<ChatMessage> getAllMessages() {
        return messages;
    }

    private boolean filterForbiddenWorld(String inputStr, List<String> items) {
        return items.stream().anyMatch(inputStr::contains);
    }

    public List<String> forbiddenWords() {
        // TODO Use a service or repo that retrieves these forbidden values
        return Arrays.asList("Hi", "Hello", "Hey", "Thank");
    }

    @PostConstruct
    private void initiateMessageServiceAttributes() {
        messages = new ArrayList<>();
    }

}
