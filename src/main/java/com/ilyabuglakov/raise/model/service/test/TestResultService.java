package com.ilyabuglakov.raise.model.service.test;

import com.ilyabuglakov.raise.domain.Answer;
import com.ilyabuglakov.raise.domain.Question;
import com.ilyabuglakov.raise.domain.Test;
import com.ilyabuglakov.raise.model.dto.QuestionDto;
import com.ilyabuglakov.raise.model.dto.TestDto;
import com.ilyabuglakov.raise.model.dto.TestResultDto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * The type Test result service.
 */
public class TestResultService {

    private static class InstanceHolder {
        /**
         * The constant INSTANCE.
         */
        public static TestResultService INSTANCE = new TestResultService();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static TestResultService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Create test result dto.
     *
     * @param userAnswer the user answer
     * @param test       the test
     * @return the test result dto
     */
    public TestResultDto createResult(TestDto userAnswer, Test test) {
        TestResultDto resultDto = new TestResultDto();
        Map<Question, QuestionDto> questionMap = getQuestionMap(userAnswer.getQuestions(), test.getQuestions());
        Set<Question> failedQuestions = new HashSet<>(test.getQuestions());
        failedQuestions.removeAll(questionMap.keySet());

        int score = 0;
        for (Map.Entry<Question, QuestionDto> entry : questionMap.entrySet()) {
            long correctAnswersAmount = entry.getKey().getAnswers().stream()
                    .filter(Answer::isCorrect)
                    .count();
            int userCorrectAmount = getUserCorrectAmount(entry.getValue(), entry.getKey());
            int questionScore = processQuestionScore(correctAnswersAmount, userCorrectAmount, entry.getValue().getAnswers().size());
            if (questionScore < 1)
                failedQuestions.add(entry.getKey());
            score += questionScore;
        }

        resultDto.setTestId(test.getId());
        resultDto.setIncorrectQuestions(failedQuestions);
        resultDto.setResult(processTestResult(test.getQuestions().size(), score));

        return resultDto;
    }

    private Map<Question, QuestionDto> getQuestionMap(Set<QuestionDto> questionDtos, Set<Question> questions) {
        Map<Question, QuestionDto> questionMap = new HashMap<>();
        for (Question question : questions) {
            Optional<QuestionDto> questionDtoMapping = questionDtos.stream()
                    .filter(questionDto -> questionDto.getId().equals(question.getId()))
                    .findFirst();
            questionDtoMapping.ifPresent(questionDto -> questionMap.put(question, questionDto));
        }
        return questionMap;
    }

    private int getUserCorrectAmount(QuestionDto questionDto, Question question) {
        return (int) questionDto.getAnswers().stream()
                .filter(quesDtoId -> question.getAnswers().stream()
                        .filter(Answer::isCorrect)
                        .anyMatch(ques -> ques.getId().equals(quesDtoId)))
                .count();
    }

    private int processQuestionScore(long correctAnswersAmount, int userCorrectAmount, int userAnswersAmount) {
        return (int) (userCorrectAmount / (correctAnswersAmount + userAnswersAmount - userCorrectAmount));
    }

    private int processTestResult(int questionsAmount, int userScore) {
        return 100 * userScore / questionsAmount;
    }
}
