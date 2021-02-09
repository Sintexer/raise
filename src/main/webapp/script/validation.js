

function validatePassword(msg) {
    let pass1 = document.getElementById("password");
    let pass2 = document.getElementById("passwordR");
    if(pass1.value!==pass2.value)
        pass2.setCustomValidity(msg);
    else
        pass2.setCustomValidity('');
}

function blockScreen(){
    let form = document.getElementById("reg-form");
    let inputs = form.querySelectorAll("input");
    console.log(inputs)
    for(let input of inputs){
        console.log(input)
        if(!input.checkValidity())
            return;
    }

    document.getElementById('loading-modal').setAttribute('style', 'display: grid;')
}