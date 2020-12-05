package aug.bueno.c1.l3.chat.app.controller;

import aug.bueno.c1.l3.chat.app.model.ChatForm;
import aug.bueno.c1.l3.chat.app.model.ChatMessage;
import aug.bueno.c1.l3.chat.app.service.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ChatController {

    final private MessageService messageService;

    public ChatController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/chat")
    public String getChatPage(
            @ModelAttribute("chatMessage") ChatMessage chatMessage,
            @ModelAttribute("chatForm") ChatForm chatForm,
            Model model

    ) {
        /*
         * 1 - Update selection element template TODO[]
         * <select th:field="*{percentage}">
         *   <option th:each="i : ${#numbers.sequence(0, 100)}" th:value="${i}" th:text="${i}">
         *   </option>
         * </select>
         *
         * */

        // 2 - Insert fo
        // model.addAttribute("listOfForbiddenWords", this.messageService.getForbiddenMessages());

        return "chat";
    }

    @PostMapping("/chat")
    public String sendMessageInChat(
            @ModelAttribute("chatForm") ChatForm chatForm,
            Model model

    ) {
        messageService.addMessage(chatForm);

        chatForm.setMessage("");

        model.addAttribute("chatMessages", this.messageService.getAllMessages());

        return "chat";
    }

    @ModelAttribute("allMessageTypes")
    public String[] allMessageTypes() {
        return new String[]{"Say", "Shout", "Whisper"};
    }

}

/*
*
<td  th:if="${chatMessage.mode} == 'Say'" th:text="${'Author:' + msg.userName + ' Message:' + msg.message }">This is the value if the name is null</td>
th:if="${chatMessage.mode} == 'Shout'" th:text="${'Author:' + msg.userName + ' Message:' + msg.message }"
th:if="${chatMessage.mode} == 'Whisper'" th:text="${'Author:' + msg.userName + ' Message:' + msg.message }"
th:unless="${#lists.contains(listOfForbiddenWords, msg.message)}}"
*
* <span th:with="messageWords=${#strings.listSplit(chatMessage.message,' ')}" th:text="${messageWords}"></span>
* */