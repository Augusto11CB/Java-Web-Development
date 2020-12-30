package aug.bueno.c1.l3.chat.app.service;

import aug.bueno.c1.l3.chat.app.model.ChatForm;
import aug.bueno.c1.l3.chat.app.model.ChatMessage;
import aug.bueno.c1.l3.chat.app.repository.mapper.ChatMessageMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MessageService {

    private ChatMessageMapper chatMessageMapper;

    public MessageService(ChatMessageMapper chatMessageMapper) {
        this.chatMessageMapper = chatMessageMapper;
    }

    public void addMessage(final ChatForm chatForm) {

        final ChatMessage newMessage = new ChatMessage();
        newMessage.setUsername(chatForm.getUserName());
        newMessage.setMessagetype(chatForm.getMessageType());

        switch (chatForm.getMessageType()) {
            case "Say":
                newMessage.setMessagetext(chatForm.getMessage());
                break;
            case "Shout":
                newMessage.setMessagetext(chatForm.getMessage().toUpperCase());
                break;
            case "Whisper":
                newMessage.setMessagetext(chatForm.getMessage().toLowerCase());
                break;
        }

        if (!filterForbiddenWorld(newMessage.getMessagetext(), this.forbiddenWords())) {
            this.chatMessageMapper.insert(newMessage);
        }
    }

    public List<ChatMessage> getAllMessages() {
        return this.chatMessageMapper.getAllChatMessages();
    }

    private boolean filterForbiddenWorld(String inputStr, List<String> items) {
        return items.stream().anyMatch(inputStr::contains);
    }

    public List<String> forbiddenWords() {
        // TODO Use a service or repo that retrieves these forbidden values
        return Arrays.asList("Hi", "Hello", "Hey", "Thank");
    }

//    @PostConstruct
//    private void initiateMessageServiceAttributes() {
//        messages = new ArrayList<>();
//    }

}
