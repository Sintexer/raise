package com.ilyabuglakov.raise.model.service.validator;

import com.ilyabuglakov.raise.domain.Answer;
import com.ilyabuglakov.raise.domain.Question;
import com.ilyabuglakov.raise.domain.type.Characteristic;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * The type Test validator.
 */
public class TestValidator {

    /**
     * The constant testNamePattern.
     */
    public static String testNamePattern = "^[^\\d',.-][^\\n_!¡?÷?¿\\/\\\\+=@#$%ˆ&*(){}|~<>;:\\[\\]]{2,}$";

    /**
     * Is valid test name boolean.
     *
     * @param testName the test name
     * @return the boolean
     */
    public boolean isValidTestName(String testName) {
        if (testName == null)
            return false;
        Pattern pattern = Pattern.compile(testNamePattern);
        return pattern.matcher(testName).matches();
    }

    /**
     * Is valid characteristics boolean.
     *
     * @param characteristics the characteristics
     * @return the boolean
     */
    public boolean isValidCharacteristics(Set<Characteristic> characteristics) {
        return characteristics != null
                && !characteristics.isEmpty()
                && ObjectUtils.allNotNull(characteristics);
    }

    /**
     * Is valid question boolean.
     *
     * @param question the question
     * @return the boolean
     */
    public boolean isValidQuestion(Question question) {
        if (question == null || question.getContent() == null || question.getContent().isEmpty())
            return false;
        boolean validAnswers = question.getAnswers().stream()
                .allMatch(this::isValidAnswer);
        if (!validAnswers)
            return false;
        return question.getAnswers().size() > 1
                && question.getAnswers().stream().anyMatch(Answer::isCorrect);
    }

    /**
     * Is valid answer boolean.
     *
     * @param answer the answer
     * @return the boolean
     */
    public boolean isValidAnswer(Answer answer) {
        return answer != null && !answer.getContent().isEmpty();
    }

}
