const MIN_ANSWERS = 2;
const MAX_ANSWERS = 6;
const MIN_QUESTIONS = 1;
const MAX_QUESTIONS = 3;
const ANSWER_ROWS = 4;
const QUESTION_ROWS = 4;
const QUESTION_NAME_ROWS = 1;

const TEST_NAME_MIN = 2;
const MIN_FIELD_LENGTH = 2;

const ALERT_STYLE = "background-color: #f44336; color: white;";

let locales = {};
let formParameters = {};

function showSubCategories(select){
    console.log(select.value);
    deleteDefaultOption(select);
    let subSelects = document.querySelectorAll(".subCategorySelect");
    for(let subSelect of subSelects){
        subSelect.setAttribute("hidden", "hidden");
    }
    let subSelectHidden = document.getElementById("subByParent"+select.value);
    // let options = subSelectHidden.querySelectorAll(".childCharacteristic");
    subSelectHidden.removeAttribute("hidden");
    console.log(subSelectHidden);
}

function deleteDefaultOption(select){
    if(document.getElementById("defaultSelectOption")!=null){
        select.removeChild(document.getElementById("defaultSelectOption"));
    }
}

function initScript(testNameLength, questionTitleLength, questionContentLength, answerContentLength, minFieldLength) {
    formParameters["testNameLength"] = testNameLength;
    formParameters["questionTitleLength"] = questionTitleLength;
    formParameters["questionContentLength"] = questionContentLength;
    formParameters["answerContentLength"] = answerContentLength;
    formParameters["minFieldLength"] = minFieldLength;
}

async function sendResult() {
    let testObj = createTestObj();
    if (validate()) {
        let modal = document.getElementById("testPostModal");
        modal.style.display = "grid";
        document.getElementById("testJson").value = JSON.stringify(testObj);
        window.onclick = (event) => {
            if (event.target === modal) {
                modal.style.display = "none";
            }
        }
    } else {
        document.getElementById("testGuide").removeAttribute("hidden");
    }
}

function validate() {
    let validTestNode = validateTestNode();
    let validCategory = validateCategory();
    let validQuestionsNode = validateQuestionsNode();

    return validTestNode && validCategory && validQuestionsNode;
}

function validateCategory(){
    // let subSelect = document.getElementById("childCategories");
    let subSelects = document.querySelectorAll(".subCategorySelect");
    for(let subSelect of subSelects){
        if(!subSelect.hasAttribute("hidden"))
            if(subSelect.value){
                subSelects[0].parentNode.parentNode.removeAttribute("style");
                console.log(subSelect);
                return true;
            }

    }
    subSelects[0].parentNode.parentNode.setAttribute("style", ALERT_STYLE);
    return false;
    // if(!subSelect.value){
    //     subSelect.parentNode.setAttribute("style", ALERT_STYLE);
    //     return false;
    // } else
    //     subSelect.parentNode.removeAttribute("style");
    // return true;
}

function validateTestNode() {
    let testName = document.getElementsByName("testName")[0];
    let valid = validateTestName(testName);
    let validCharacteristics = validateCharacteristics();
    if (!valid) {
        testName.parentNode.setAttribute("style", ALERT_STYLE);
        return false;
    } else {
        testName.parentNode.removeAttribute("style")
    }
    return validCharacteristics;
}

function validateTestName(testNameInput) {
    let testName = testNameInput.value;
    let regex = /^[^\d',.-][^\n_!¡?÷¿\/\\+=@#$%ˆ&*(){}|~<>;:\[\]]{2,}$/;
    return testName.match(regex);
}

function validateQuestionsNode() {
    let questions = document.getElementById("questions").querySelectorAll(".question");
    let valid = true;
    let notEmpty = questions.length>0;
    for (let questionNode of questions) {
        let anyChecked = false;
        for(let correct of questionNode.querySelectorAll("input[type=checkbox]"))
            if(correct.checked)
                anyChecked = true;
        if (!anyChecked) {
            questionNode.querySelector(".answers").querySelectorAll(".answer").forEach(
                answer => answer.setAttribute("style", ALERT_STYLE)
            );
        } else {
            questionNode.querySelector(".answers").querySelectorAll(".answer").forEach(
                answer => answer.removeAttribute("style")
            );
        }
        let emptyAnswers = false;
        for(let answer of questionNode.querySelectorAll(".answer")){
            emptyAnswers = answer.querySelector(".answer-content").value === "";
        }
        let validQuestion = anyChecked
            && questionNode.querySelector(".question-content").value !== ""
            && questionNode.querySelector(".question-name").value !== "";
        if(!validQuestion || emptyAnswers)
            valid = false;
    }
    return valid && notEmpty;
}

function validateCharacteristics(){
    let characteristics = document.getElementById("characteristics");
    let anyChecked = false;
    for(let correct of characteristics.querySelectorAll("input[type=checkbox]"))
        if(correct.checked)
            anyChecked = true;
    if(!anyChecked){
        characteristics.setAttribute("style", ALERT_STYLE);
        return false;
    } else {
        characteristics.removeAttribute("style");
    }
    return true;
}

function validateTest(test) {
    for (let question of test["questions"]) {
        if (!validateQuestion(question)) {
            return false;
        }
    }
    return test["questions"].length > 0 && test["testName"] !== '' && test["characteristics"].length !== 0
}

function validateQuestion(question) {
    if (question["name"] === null || question["name"] === '')
        return false;
    let hasCorrect = false;
    for (let answer of question["answers"]) {
        if (answer["content"] === '')
            return false;
        if (answer["correct"] === true)
            hasCorrect = true;
    }
    return hasCorrect;
}

function createTestObj() {
    let testName = document.getElementsByName("testName")[0].value;
    // let categoryId = document.getElementById("childCategories").value;
    let characteristics = document.getElementsByName("characteristic");
    let checkedCharacteristics = [];
    for (let characteristic of characteristics) {
        if (characteristic.checked) {
            checkedCharacteristics.push(characteristic.value);
        }
    }
    let questions = document.getElementById("questions").querySelectorAll(".question");
    let questionJsons = [];
    for (let questionNode of questions)
        questionJsons.push(createQuestionObj(questionNode));

    let obj = {};
    obj["testName"] = testName;
    obj["characteristics"] = checkedCharacteristics;
    obj["questions"] = questionJsons;
    let subSelects = document.querySelectorAll(".subCategorySelect");
    for(let subSelect of subSelects){
        if(!subSelect.hasAttribute("hidden")){
            obj["category"] = {"id":subSelect.value};
            break;
        }
    }

    return obj;
}

function createQuestionObj(questionNode) {
    let answers = questionNode.querySelector(".answers").querySelectorAll(".answer");
    let answerJsons = [];
    for (let answer of answers)
        answerJsons.push(createAnswerObj(answer));
    let questionContent = questionNode.querySelector(".question-content").value;
    let questionName = questionNode.querySelector(".question-name").value;

    let obj = {};
    obj["name"] = questionName;
    obj["content"] = questionContent;
    obj["answers"] = answerJsons;
    return obj;
}

function createAnswerObj(answerNode) {
    let obj = {};
    obj["correct"] = answerNode.querySelector("input").checked;
    obj["content"] = answerNode.querySelector("textarea").value;
    return obj;
}

function disableElement(element) {
    element.setAttribute("disabled", "disabled");
}

function enableElement(element) {
    element.removeAttribute("disabled");
}

function deleteAnswer(button) {
    let div = button.parentNode;
    let answersNode = div.parentNode;
    if (div.children.length > MIN_ANSWERS)
        answersNode.removeChild(div);
    switchAnswerDeleteButtons(answersNode);
    switchAddAnswerButtons(answersNode.parentNode);
}

function deleteQuestion(button) {
    let question = button.parentNode.parentNode;
    let questions = document.getElementById("questions");
    if (questions.querySelectorAll(".question").length > MIN_QUESTIONS) {
        questions.removeChild(question.nextSibling);
        questions.removeChild(question);
    }
    switchQuestionDeleteButtons(locales["questionTitle"]);
    switchAddQuestionButton();
}

function switchAnswerDeleteButtons(answersNode) {
    let deleteButtons = answersNode.querySelectorAll(".dlt-answer-button");
    let switchAction;
    if (deleteButtons.length <= MIN_ANSWERS) {
        switchAction = disableElement;
    } else {
        switchAction = enableElement;
    }
    for (let deleteButton of deleteButtons) {
        switchAction(deleteButton);
    }
    regenerateAnswersNumeration(answersNode);
}

function switchAddAnswerButtons(question) {
    let answers = question.querySelectorAll(".answer");
    if (answers.length >= MAX_ANSWERS) {
        question.querySelector(".add-answer-button").setAttribute("disabled", "disabled");
    } else {
        question.querySelector(".add-answer-button").removeAttribute("disabled");
    }
}

function switchQuestionDeleteButtons(questionTitleText) {
    let questionsNode = document.getElementById("questions");
    let buttons = questionsNode.querySelectorAll(".delete-question-btn");
    let switchAction;
    if (buttons.length <= MIN_QUESTIONS) {
        switchAction = disableElement;
    } else {
        switchAction = enableElement;
    }
    for (let button of buttons) {
        switchAction(button);
    }
    regenerateQuestionNumeration(questionTitleText);
}

function switchAddQuestionButton() {
    let addQuestionButton = document.getElementById("addQuestionButton");
    let questions = document.getElementById("questions").querySelectorAll(".question");
    let switchAction;
    if (questions.length >= MAX_QUESTIONS) {
        switchAction = disableElement;
    } else {
        switchAction = enableElement;
    }
    switchAction(addQuestionButton);
}

function regenerateAnswersNumeration(answersNode) {
    let numbers = answersNode.querySelectorAll(".answerOrder");
    let i = 1;
    for (let number of numbers) {
        number.innerHTML = i++ + ". ";
    }
}

function regenerateQuestionNumeration(titleText) {
    let questionTitles = document.getElementById("questions").querySelectorAll(".questionTitle");
    let i = 1;
    for (let questionTitle of questionTitles) {
        questionTitle.innerText = titleText + " " + i++ + ":";
    }
}

function addQuestion(button, questionTitleText, addAnswerButtonName, correctCaptionText, deleteQuestionText, questionNamePlaceholder) {
    locales["questionTitle"] = questionTitleText;
    locales["correct"] = correctCaptionText;
    if (document.getElementById("questions").querySelectorAll('.question').length >= MAX_QUESTIONS)
        return;
    let question = document.createElement("div");
    question.setAttribute("class", "question items-gap-vertical margin-l-2rem");

    let questionContentDiv = document.createElement("div");
    questionContentDiv.setAttribute("class", "flex");
    let questionContent = document.createElement("textarea")
    questionContent.setAttribute("class", "question-content unresize flex-auto form-input");
    questionContent.setAttribute("required", "required");
    questionContent.setAttribute("name", "question-content");
    questionContent.setAttribute("minLength", formParameters.minFieldLength);
    questionContent.setAttribute("maxLength", formParameters.questionContentLength);
    questionContent.setAttribute("rows", QUESTION_ROWS.toString());
    questionContentDiv.appendChild(questionContent);

    let questionHeader = createQuestionHeader(deleteQuestionText);

    let questionName = document.createElement("textarea");
    questionName.setAttribute("class", "question-name unresize flex-auto form-input");
    questionName.setAttribute("required", "required");
    questionName.setAttribute("name", "question-name");
    questionName.setAttribute("minLength", formParameters.minFieldLength);
    questionName.setAttribute("maxLength", formParameters.questionTitleLength);
    questionName.setAttribute("placeholder", questionNamePlaceholder)
    questionName.setAttribute("rows", QUESTION_NAME_ROWS.toString());

    let answers = document.createElement("div");
    answers.setAttribute("class", "answers items-gap-vertical-sm");

    let addAnswerButton = createAddAnswerButton(addAnswerButtonName);

    question.appendChild(questionHeader);
    question.appendChild(questionName);
    question.appendChild(questionContentDiv);
    question.appendChild(answers);
    question.appendChild(addAnswerButton);

    let breakLine = document.createElement("div");
    breakLine.setAttribute("class", "breakline");

    document.getElementById("questions").appendChild(question);
    document.getElementById("questions").appendChild(breakLine);

    for (let i = 0; i < MIN_ANSWERS; ++i)
        addAnswer(addAnswerButton, correctCaptionText);

    switchAddQuestionButton();
    switchQuestionDeleteButtons(questionTitleText);
}

function addAnswer(button) {
    let answer = document.createElement("div");
    answer.setAttribute("class", "answer dec-pancake items-gap-sm");
    answer.appendChild(createAnswerOrder());
    answer.appendChild(createAnswerTextArea());
    answer.appendChild(createAnswerCorrect(locales["correct"]));
    answer.appendChild(createDeleteAnswerButton());
    let answers = button.parentNode.querySelector(".answers");
    answers.appendChild(answer);
    regenerateAnswersNumeration(answers);
    switchAddAnswerButtons(button.parentNode);
    switchAnswerDeleteButtons(button.parentNode.querySelector(".answers"));
}

function createQuestionHeader(deleteQuestionText) {
    let questionTitle = document.createElement("h3");
    questionTitle.setAttribute("class", "questionTitle");
    let deleteQuestionButton = document.createElement("button");
    deleteQuestionButton.setAttribute("class", "delete-question-btn btn btn-red margin-left-auto");
    deleteQuestionButton.setAttribute("type", "button");
    deleteQuestionButton.setAttribute("onclick", "deleteQuestion(this, 'Question')");
    deleteQuestionButton.innerHTML = deleteQuestionText;
    let div = document.createElement("div");
    div.setAttribute("class", "question-header flex align-items-center");
    div.appendChild(questionTitle);
    div.appendChild(deleteQuestionButton);
    return div;
}

function createAddAnswerButton(addAnswerButtonName) {
    let addAnswerButton = document.createElement("button");
    addAnswerButton.setAttribute("class", "btn btn-black margin-auto add-answer-button");
    addAnswerButton.setAttribute("type", "button");
    addAnswerButton.setAttribute("onclick", "addAnswer(this, 'correct')")
    addAnswerButton.innerHTML = addAnswerButtonName;
    return addAnswerButton;
}

function createAnswerOrder() {
    let answerOrder = document.createElement("span")
    answerOrder.setAttribute("class", "answerOrder");
    return answerOrder;
}

function createAnswerTextArea() {
    let answerTextArea = document.createElement("textarea")
    answerTextArea.setAttribute("class", "answer-content unresize flex-auto form-input w-auto");
    answerTextArea.setAttribute("required", "required");
    answerTextArea.setAttribute("name", "answer-content");
    answerTextArea.setAttribute("minLength", formParameters.minFieldLength);
    answerTextArea.setAttribute("maxLength", formParameters.answerContentLength);
    answerTextArea.setAttribute("rows", ANSWER_ROWS.toString());
    return answerTextArea;
}

function createAnswerCorrect(captionText) {
    let caption = document.createElement("span");
    caption.setAttribute("class", "margin-r-2rem");
    caption.setAttribute("onchange", "validateQuestionsNode()");
    caption.innerHTML = "<input type=\"checkbox\" name=\"answer-correct\"/>" + captionText;
    return caption;
}

function createDeleteAnswerButton() {
    let button = document.createElement("button");
    button.setAttribute("type", "button");
    button.setAttribute("class", "dlt-answer-button btn-sm btn-red rounded-10 margin-left-auto");
    button.setAttribute("onclick", "deleteAnswer(this)");
    button.innerHTML = "&times";
    return button;
}