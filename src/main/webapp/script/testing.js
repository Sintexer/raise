function postResult(){
    let modalMessage = document.getElementById("modalMessage");
    if(checkEmptyQuestions()){
        modalMessage.setAttribute("hidden", "hidden");
    } else {
        modalMessage.removeAttribute("hidden");
    }
    let responseObj = createResponseObject();
    console.log(JSON.stringify(responseObj));
    document.getElementById("testJson").value = JSON.stringify(responseObj);

    let modal = document.getElementById("testingPostModal");
    modal.style.display = "grid";
    window.onclick = (event) => {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    }
}

function createResponseObject(){
    let obj = {};
    obj["id"] = document.getElementById("testId").value;
    obj["questions"] = createQuestionsList();

    return obj;
}

function createQuestionsList() {
    let questionsNode = document.getElementById("questions");
    let questions = questionsNode.querySelectorAll(".question");
    let questionObjects = [];

    for(let question of questions){
        questionObjects.push(createQuestionObject(question));
    }
    return questionObjects;
}

function createQuestionObject(question) {
    let obj = {};
    obj["id"] = question.querySelector(".questionId").value;
    obj["answers"] = [];
    for(let answer of question.querySelectorAll(".answer")){
        if(answer.querySelector(".answerOption").checked)
            obj["answers"].push(answer.querySelector(".answerId").value);
    }
    return obj;
}

function checkEmptyQuestions(){
    let questions = document.getElementById("questions").querySelectorAll(".question");
    for(let question of questions){
        let anyChecked = false;
        for(let checkbox of question.querySelectorAll(".answerOption")){
            if(checkbox.checked)
                anyChecked = true;
        }
        if(!anyChecked)
            return false;
    }
    return true;
}