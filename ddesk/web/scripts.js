
function checkboxOnView(checkbox,elementToShow){
    if(checkbox.checked){
        document.getElementById(elementToShow).style.display = "";
    } else {
        document.getElementById(elementToShow).style.display = "none";
    }
}

function clickView(elementToShow){
    var element = document.getElementById(elementToShow);
    if(element.style.display == "none"){
        element.style.display = "";
    } else {
        element.style.display = "none";
    }
}

function oneOfTwoView(elementToShow, elemntToHide){
    var element = document.getElementById(elementToShow);
    if(element.style.display == "none"){
        element.style.display = "";
    } else {
        element.style.display = "none";
    }
    element = document.getElementById(elemntToHide);
    element.style.display = "none";
}