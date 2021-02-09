package com.ilyabuglakov.raise.model.service.validator;

import com.ilyabuglakov.raise.domain.Answer;
import com.ilyabuglakov.raise.domain.Question;
import com.ilyabuglakov.raise.domain.type.Characteristic;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Set;

public class TestValidatorTest {

    private static Set<Answer> defaultAnswerSet;
    private static Set<Question> defaultQuestionSet;
    private static Answer correctAnswer;
    private static Answer incorrectAnswer;
    private static Answer wrongAnswer;
    private static Question question;
    private static Question wrongQuestion;

    @BeforeClass
    public void initTest() {
        correctAnswer = Answer.builder()
                .content("Sample content")
                .correct(true)
                .build();
        incorrectAnswer = Answer.builder()
                .content("Sample content")
                .correct(false)
                .build();
        wrongAnswer = Answer.builder()
                .content("")
                .correct(false)
                .build();
        question = Question.builder()
                .content("Sample")
                .answers(Set.of(correctAnswer, incorrectAnswer))
                .build();
        wrongQuestion = Question.builder()
                .answers(Set.of(correctAnswer))
                .content("")
                .build();
    }

//    @DataProvider(name = "test")
//    public Object[][] getData() {
//        return new Object[][]{
//                {
//                        com.ilyabuglakov.raise.domain.Test.builder()
//                                .characteristics(Arrays.asList(Characteristic.values()))
//                                .testName("Test 1`")
//                                .difficulty(1)
//                                .questions()
//                }
//        }
//    }

    @Test
    public void testIsValid() {
    }

    @Test
    public void testIsValidTestName() {
    }

    @Test
    public void testIsValidCharacteristics() {
    }

    @Test
    public void testIsValidQuestion() {
    }

    @Test
    public void testIsValidAnswer() {
    }
}