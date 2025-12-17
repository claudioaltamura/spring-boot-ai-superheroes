package de.claudioaltamura.springboot.ai.superheroes.controller;

import de.claudioaltamura.springboot.ai.superheroes.model.Answer;
import de.claudioaltamura.springboot.ai.superheroes.model.GetSideKick;
import de.claudioaltamura.springboot.ai.superheroes.model.Question;
import de.claudioaltamura.springboot.ai.superheroes.service.OpenAIService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionController {
    private final OpenAIService openAIService;

    public QuestionController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/sidekick")
    public Answer getSidekick(@RequestBody GetSideKick getSideKickRequest) {
        return this.openAIService.getSideKick(getSideKickRequest);
    }

    @PostMapping("/ask")
    public Answer askQuestion(@RequestBody Question question) {
        return openAIService.getAnswer(question);
    }
}