function validateUsername() {
    const name = document.getElementById("username");
    if (name.value.length < 1) {
        alert("Username cannot be empty!");
        return false;
    } else {
        return true;
    }
}

function validateSignUp() {
    document.cookie = "posttype=signup"; //used to differentiate how a post req on / was generated
    const name = document.getElementById("name");
    if (name.value.length < 1) {
        alert("Username cannot be empty!");
        return false;
    }
    const email = document.getElementById("email");
    if (email.value.length < 1) {
        alert("Email cannot be empty!");
        return false;
    } else {
        return true;
    }
}

function validateNewClass() {
    document.cookie = "posttype=joinclass"; //used to differentiate how a post req on / was generated
    const name = document.getElementById("classnum");
    if (name.value.length < 1) {
        alert("Please enter a valid number.");
        return false;
    }
}

function validateQuestionNum() {
    const num = document.getElementById("num");
    if (num.value.length < 1) {
        alert("Number of questions cannot be empty!");
        return false;
    }
    return true;
}