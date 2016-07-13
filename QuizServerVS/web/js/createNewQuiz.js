function $(id) {
    return document.getElementById(id);
}

var question_counter = 1;

function questionHtml(question_counter) {
    var html =  " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\"Frage\" name=\"question_"+question_counter+"\" id=\"q\" /> <br>" +
                " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\"Antwort 1 hier eintragen\" name=\"answer_"+question_counter+"_1\"/> " +
                " <input type=\"checkbox\" name=\"chk"+question_counter+"_1\" /> <br> " +                          
                " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\"Antwort 2 hier eintragen\" name=\"answer_"+question_counter+"_2\"/> " +
                " <input type=\"checkbox\" name=\"chk"+question_counter+"_2\" /> <br> " +                           
                " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\"Antwort 3 hier eintragen\" name=\"answer_"+question_counter+"_3\"/> " +
                " <input type=\"checkbox\" name=\"chk"+question_counter+"_3\" /> <br> " +
                " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\"Antwort 4 hier eintragen\" name=\"answer_"+question_counter+"_4\"/> " +
                " <input type=\"checkbox\" name=\"chk"+question_counter+"_4\" /> <br>" +
                " <p></p> ";

    return html;
}

function nextQuestin() {

    question_counter++;

    var d1 = document.getElementById('q1');
    d1.insertAdjacentHTML('beforeend', questionHtml(question_counter));

    var divs = document.getElementsByClassName('jumbotron');
    for(var i=0; i < divs.length; i++) { 
      divs[i].style.height = "100%";
    }
}

function createJSON(question_counter) {

  var quizname = document.getElementById("myForm2").elements[0].value;

    var json = "{ \"quiz_name\": \""+quizname+"\", \"questions\": [ ";

    for (var i = 0;i < question_counter;i++) {   

        json +=      '{  "question":"'+document.getElementById("myForm").elements[0+(10*i)].value+'", ' +
                       ' "answers": [ {' +
                       ' "answer":"'+document.getElementById("myForm").elements[1+(10*i)].value+'" , ' +
                       ' "correct":"'+document.getElementById("myForm").elements[2+(10*i)].checked+'" }, ' +
                       ' {"answer":"'+document.getElementById("myForm").elements[3+(10*i)].value+'" , ' +
                       ' "correct":"'+document.getElementById("myForm").elements[4+(10*i)].checked+'" }, ' +
                       ' {"answer":"'+document.getElementById("myForm").elements[5+(10*i)].value+'" , ' +
                       ' "correct":"'+document.getElementById("myForm").elements[6+(10*i)].checked+'" }, ' +
                       ' {"answer":"'+document.getElementById("myForm").elements[7+(10*i)].value+'" , ';
                       if (i+1 != question_counter) {
                        json += ' "correct":"'+document.getElementById("myForm").elements[8+(10*i)].checked+'" } ] }, ';
                       } else {
                        json += ' "correct":"'+document.getElementById("myForm").elements[8+(10*i)].checked+'" } ] } ] }';
                       }                       
    }
    return json;
}

function postHttpRequest(url) {
    var xmlhttp = null;
    url = "../ClientServlet2";
                  
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
            // Do something
        }
        if(xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            console.log("Json an Servlet übertragen");
        }
    };

    xmlhttp.open("POST", url, true); 
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

    var quiz = createJSON(question_counter);

    var param = "js="+quiz+"";
    xmlhttp.send(param);
}