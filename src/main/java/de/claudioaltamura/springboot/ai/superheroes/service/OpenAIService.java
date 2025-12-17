package de.claudioaltamura.springboot.ai.superheroes.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.claudioaltamura.springboot.ai.superheroes.model.Answer;
import de.claudioaltamura.springboot.ai.superheroes.model.GetSideKick;
import de.claudioaltamura.springboot.ai.superheroes.model.Question;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OpenAIService {

    private final ChatModel chatModel;
    private final ObjectMapper objectMapper;

    public OpenAIService(ChatModel chatModel, ObjectMapper objectMapper) {
        this.chatModel = chatModel;
        this.objectMapper = objectMapper;
    }

    @Value("classpath:templates/get-sidekick-prompt.st")
    private Resource getSidekickPrompt;

    public Answer getSideKick(GetSideKick getSideKickRequest) {
        PromptTemplate promptTemplate = new PromptTemplate(getSidekickPrompt);
        Prompt prompt = promptTemplate.create(Map.of("sidekickName", getSideKickRequest.superhero()));
        ChatResponse response = chatModel.call(prompt);

        String responseString;

        try {
            JsonNode jsonNode = objectMapper.readTree(response.getResult().getOutput().getText());
            responseString = jsonNode.get("answer").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return new Answer(responseString);
    }

    public Answer getAnswer(Question question) {
        PromptTemplate promptTemplate = new PromptTemplate(question.question());
        Prompt prompt = promptTemplate.create();
        ChatResponse response = chatModel.call(prompt);

        return new Answer(response.getResult().getOutput().getText());
    }

}
