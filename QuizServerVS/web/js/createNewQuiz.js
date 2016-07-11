function $(id) {
    return document.getElementById(id);
}
var question_counter = 1;

function questionHtml(question_counter) {
    var html =  " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\"Frage\" name=\"question_"+question_counter+"\" id=\"q\" /> " +
               " <input type=\"checkbox\" class=\"form-control\" name=\"chk0\" id=\"x\" /> <br> " +
               " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\"Antwort1 hier eintragen\" name=\"answer_"+question_counter+"_1\"/> " +
               " <input type=\"checkbox\" name=\"chk"+question_counter+"_1\" /> <br> " +                          
               " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\"Antwort2 hier eintragen\" name=\"answer_"+question_counter+"_2\"/> " +
               " <input type=\"checkbox\" name=\"chk"+question_counter+"_2\" /> <br> " +                           
               " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\"Antwort3 hier eintragen\" name=\"answer_"+question_counter+"_3\"/> " +
               " <input type=\"checkbox\" name=\"chk"+question_counter+"_3\" /> <br> " +
               " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\"Antwort4 hier eintragen\" name=\"answer_"+question_counter+"_4\"/> " +
               " <input type=\"checkbox\" name=\"chk"+question_counter+"_4\" /> <p></p> ";

    return html;
}

function nextQuestin() {

    question_counter++;

    var d1 = document.getElementById('q1');
    d1.insertAdjacentHTML('beforeend', questionHtml(question_counter));

    var divs = document.getElementsByClassName('jumbotron');
    for(var i=0; i < divs.length; i++) { 
      divs[i].style.height = '100%';
    }    

}

function createJSON(question_counter) {

    var json = '[';
    var i = 0;

    for (;i < question_counter;i++) {   

        json +=      '{  "question":"'+document.getElementById("myForm").elements[0+(10*i)].value+'", ' +
                       ' "answer1":"'+document.getElementById("myForm").elements[2+(10*i)].value+'" , ' +
                       ' "answer1_correct":"'+document.getElementById("myForm").elements[3+(10*i)].checked+'" , ' +
                       ' "answer2":"'+document.getElementById("myForm").elements[4+(10*i)].value+'" , ' +
                       ' "answer2_correct":"'+document.getElementById("myForm").elements[5+(10*i)].checked+'" , ' +
                       ' "answer3":"'+document.getElementById("myForm").elements[6+(10*i)].value+'" , ' +
                       ' "answer3_correct":"'+document.getElementById("myForm").elements[7+(10*i)].checked+'" , ' +
                       ' "answer4":"'+document.getElementById("myForm").elements[8+(10*i)].value+'" , ';
                       if (i+1 != question_counter) {
                        json += ' "answer4_correct":"'+document.getElementById("myForm").elements[9+(10*i)].checked+'" }, ';
                       } else {
                        json += ' "answer4_correct":"'+document.getElementById("myForm").elements[9+(10*i)].checked+'" } ';
                       }
                       
    }
    json += " ]}' ";
    return json;
}

function postHttpRequest(url) {
    var xmlhttp = null;
    url = "../ClientServlet2";
    var quizname = document.getElementById("myForm2").elements[0].value;
                  
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
            console.log("Übertragung komplett");
        }
    };

  
    xmlhttp.open("POST", url, true); 
    //Send the proper header information along with the request
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

    var quiz = createJSON(question_counter);

    var param = "js="+quiz+"&quizname="+quizname+"";
    xmlhttp.send(param);
}