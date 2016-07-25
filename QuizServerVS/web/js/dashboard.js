function $(id) {
    return document.getElementById(id);
}

var activeQuizId = null;

function setActiveQuizId(thisId) {
    activeQuizId = thisId;
    console.log("setActiveQuizId(): " + thisId);
}

function getActiveQuizId() {
    console.log("getActiveQuizId(): " + activeQuizId);
    return activeQuizId;
}

function checkRadioPressedOrNot() {
    var quizName = document.forms[0];
    for (var i = 0; i < quizName.length; i++) {
        if (quizName[i].checked) {
            return true;
        }
    }
    return false;    
}

function getHttpRequest(url) {
    var xmlhttp = null;
    // Mozilla
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    }
    // IE
    else if (window.ActiveXObject) {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
   
    xmlhttp.onreadystatechange = function() {
        if(xmlhttp.readyState !== 4) {
            $('quiz').innerHTML = 'Laden deiner Quiz...';
        }
        if(xmlhttp.readyState === 4 && xmlhttp.status === 200) {

            var Response = xmlhttp.responseText;

            if (Response != "Noch kein Quiz erstellt!" && Response != "[]") {

                console.log("Response: " + Response);
            
                var jsonString = xmlhttp.responseText; 
                var jsObject = JSON.parse( jsonString );            
                var htmlResponse = ''; 
                           
                for (index = 0; index < jsObject.length; ++index) {
                    if (jsObject[index] !== null) {
                    htmlResponse +="<input readonly type=\"text\" class=\"form-control\" size=\"50\" placeholder=\""+jsObject[index].name+"\">" +
                                   "<input          type=\"radio\"class=\"form-control radio\" name=\"quizName\" onclick=\"setActiveQuizId(this.id)\" value=\""+jsObject[index].quiz_id+"\" id=\""+jsObject[index].quiz_id+"\"></input> <br><br>"; 
                     }
                }
                $('quiz').innerHTML = htmlResponse;
            } else {
                $('quiz').innerHTML = "Noch kein Quiz erstellt!";
            }
        }
    };
    xmlhttp.open("GET", url+"?code=getQuiz", true);    
    xmlhttp.send();
}

function editQuiz(quiz_id) {
    if ( checkRadioPressedOrNot() === true) {
        window.location.replace("editQuiz.html?quiz_id="+getActiveQuizId()+" ");
    } else {
        alert("Sie müssen ein Quiz auswählen, indem Sie einen Radio-Button aktivieren!");
    }
}

function startQuiz() {
    if ( checkRadioPressedOrNot() === true) {    
        window.location.replace('quizGame/startScreen.html?quiz_id='+getActiveQuizId()+'');
    } else {
        alert("Sie müssen ein Quiz auswählen, indem Sie einen Radio-Button aktivieren!");
    }    
}

function joinQuiz() {
    var pin = document.getElementById('pin').value;
    var xmlhttp = null;
    var url = '../ClientServlet3';

    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest(); // Mozilla
    }    
    else if (window.ActiveXObject) {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP"); // IE
    }
   
    xmlhttp.onreadystatechange = function() {
        if(xmlhttp.readyState === 4 && xmlhttp.status === 200) {

            var Response = xmlhttp.responseText;
            console.log("joinQuiz(): " + Response);

            window.location.replace('quizGame/userLobby.html?pin='+ pin +'&role=player');
        }
    };
    xmlhttp.open("GET", url+'?js='+pin+'&code=joinQuiz', true);    
    xmlhttp.send();    

}
