function $(id) {
    return document.getElementById(id);
}

var question_counter = 1; // Zählt die Fragen
var j = 10; // Zählt die Elemente pro Frage

function createQuestionHtml(question_counter) {
    var html =  " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\"Frage\" name=\"question_"+question_counter+"\" id=\"q\" />" +
                " <input type=\"checkbox\" name=\"chk"+question_counter+"_1\" style=\"visibility:hidden\" /> <br> " +
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
// Überprüft die Eingabedaten der Nutzer
function checkUserInputValues () {
  var i = 0; // Läuft alle Elemente durch
  var k = 1; // Gibt die Frage an die bearbeitet wird
  var p = 0; // Für die richtige Angabe der Frage oder Antwort in der DialogBox
  var chkBox = false; // Gibt an ob mindestens eine CheckBox einer Frage aktiviert wurde  

  for (; k <= question_counter; k++) {
    if ( document.getElementById("myForm2").elements[0].value == null || document.getElementById("myForm2").elements[0].value == "" ) {
      alert("Sie müssen einen Quiznamen eingeben bevor Sie das Quiz speichern können!");
      return false;
    }
    for (; i < (j*k); i+=2) {
      if ( document.getElementById("myForm").elements[i].value == null || document.getElementById("myForm").elements[i].value == "" ) {
        if (i == j*(k-1) || i == 0) {
          alert("Sie müssen Frage"+k+" eingeben!");
          return false;
        } else {              
          alert("Sie müssen Antwort"+p+" von Frage"+k+" eingeben!");
          return false;      
        }
      }
      p++;
    }
    for (i = 3+((k-1)*j); i < (j*k); i+=2) {
      if ( document.getElementById("myForm").elements[i].checked == true || document.getElementById("myForm").elements[i].checked == "true" ) {
        chkBox = true;
      }
    }
    if (chkBox != true) {
      alert("Sie müssen mindestens eine Checkbox bei Frage"+k+" aktivieren");
      return false;
    }
    console.log("k: " + k);
    chkBox = false;
    i-=1;
    p=0;
  }
    console.log("checkUserInputValues() complete!");
    return true; // Falls alle Angaben vorhanden  
}

function nextQuestion() {

    question_counter++;

    var d1 = document.getElementById('q1');
    d1.insertAdjacentHTML('beforeend', createQuestionHtml(question_counter));

    var divs = document.getElementsByClassName('jumbotron');
    for(var i=0; i < divs.length; i++) { 
      divs[i].style.height = "100%";
    }
}

function createJSON(question_counter) {

  var quizname = document.getElementById("myForm2").elements[0].value;

    var json = '{ "quiz_name":"'+quizname+'", "questions": [ ';

    for (var i = 0;i < question_counter;i++) {   

        json +=      '{  "question":"'+document.getElementById("myForm").elements[0+(j*i)].value+'", ' +
                       ' "answers": [' +
                       ' {"answer":"'+document.getElementById("myForm").elements[2+(j*i)].value+'" , ' +
                       ' "correct":'+document.getElementById("myForm").elements[3+(j*i)].checked+' }, ' +
                       ' {"answer":"'+document.getElementById("myForm").elements[4+(j*i)].value+'" , ' +
                       ' "correct":'+document.getElementById("myForm").elements[5+(j*i)].checked+' }, ' +
                       ' {"answer":"'+document.getElementById("myForm").elements[6+(j*i)].value+'" , ' +
                       ' "correct":'+document.getElementById("myForm").elements[7+(j*i)].checked+' }, ' +
                       ' {"answer":"'+document.getElementById("myForm").elements[8+(j*i)].value+'" , ';
                       if (i+1 != question_counter) {
                        json += ' "correct":'+document.getElementById("myForm").elements[9+(j*i)].checked+' } ] }, ';
                       } else {
                        json += ' "correct":'+document.getElementById("myForm").elements[9+(j*i)].checked+' } ] } ] }';
                       }                       
    }
    return json;
}

function createQuiz() {
  if ( checkUserInputValues() == true ) {
    postHttpRequest();
  } else {
    console.log("createQuiz() error!");
  }
}

function postHttpRequest() {
    var xmlhttp = null;
    url = "../ClientServlet2";
                  
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest(); // Mozilla
    }    
    else if (window.ActiveXObject) {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP"); // IE
    }

    xmlhttp.onreadystatechange = function() {
        if(xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            console.log("Json an Servlet übertragen");            
            window.setTimeout('window.location = "dashboard.html"',3000);
            alert("Das Quiz wurde erfolgreich erstellt. Sie werden in 3 Sekunden\n\
                  zum Dashboard weitergeleitet");
        }
    };

    xmlhttp.open("POST", url, true); 
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

    var quiz = createJSON(question_counter);

    var param = "js="+quiz+"&code=create";
    xmlhttp.send(param);
}