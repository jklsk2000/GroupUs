function addTestSurvey() {
    const q1 = document.getElementById("q1").value;
    const q2 = document.getElementById("q2").value;
    const q3 = document.getElementById("q3").value;
    const q1weight = document.getElementById("q1weight").selectedIndex;
    const q2weight = document.getElementById("q2weight").selectedIndex;
    const q3weight = document.getElementById("q3weight").selectedIndex;

    fetch('/instructor?question1=' + q1 + "&question2=" + q2 + "&question3=" + q3 +
        "&q1weight=" + q1weight + "&q2weight=" + q2weight + "&q3weight=" + q3weight, {
            method: "Post",
        }
    );
}

function ask() {
    let response = prompt("How many questions do you want to add?")

    let n = Number(response)

    let feedback = n > 0 ?
        `Nice! We'll prepare a survey for you` :
        `You should enter more than 1` ;
    alert(feedback);
}