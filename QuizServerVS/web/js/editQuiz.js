function $(id) {
    return document.getElementById(id);
}

var question_counter = 0;

function getURLParameter(name) {
  return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [null, ''])[1].replace(/\+/g, '%20')) || null;
}

function buildHtmlPage (jsonString) {

  var jsObject = JSON.parse( jsonString );            
  var htmlResponse = '';
  question_counter = 0;
  var i = 0;  
    
  for (var x in jsObject.questions) {    
      htmlResponse +=  
                  " <input type=\"text\" class=\"form-control question\" size=\"50\" placeholder=\""+jsObject.questions[i].question+"\" name=\""+jsObject.questions[i].question+"\" id=\""+jsObject.questions[i].question_id+"\" /> <br>" +
                  " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\""+jsObject.questions[i].answers[0].answer+"\" name=\"answer_"+i+"_1\" id=\""+jsObject.questions[i].answers[0].answer_id+"\" /> " +
                  " <input type=\"checkbox\" "+checkIfCheckedOrNot(jsObject.questions[i].answers[0].correct)+" name=\"chk"+i+"_1\" /> <br> " +                          
                  " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\""+jsObject.questions[i].answers[1].answer+"\" name=\"answer_"+i+"_2\" id=\""+jsObject.questions[i].answers[1].answer_id+"\" /> " +
                  " <input type=\"checkbox\" "+checkIfCheckedOrNot(jsObject.questions[i].answers[1].correct)+" name=\"chk"+i+"_2\" /> <br> " +                           
                  " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\""+jsObject.questions[i].answers[2].answer+"\" name=\"answer_"+i+"_3\" id=\""+jsObject.questions[i].answers[2].answer_id+"\" /> " +
                  " <input type=\"checkbox\" "+checkIfCheckedOrNot(jsObject.questions[i].answers[2].correct)+" name=\"chk"+i+"_3\" /> <br> " +
                  " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\""+jsObject.questions[i].answers[3].answer+"\" name=\"answer_"+i+"_4\" id=\""+jsObject.questions[i].answers[3].answer_id+"\" /> " +
                  " <input type=\"checkbox\" "+checkIfCheckedOrNot(jsObject.questions[i].answers[3].correct)+" name=\"chk"+i+"_4\" /> <br>" +                
                  " <button type=\"button\" onclick=\"saveQuestion(this.id)\" class=\"btn btn-danger RbtnMargin\" id=\""+i+"\">Frage speichern</button> " +
                  " <p></p> ";
      question_counter++;  
      i++;
  }
  return htmlResponse;                
}

function buildHtmlQuizName (jsonString) {
var jsObject = JSON.parse( jsonString );            
  var htmlResponse = '';
    
  for (var x in jsObject) {  
      htmlResponse +=
                  " <input type=\"text\" class=\"form-control quizName\" size=\"50\" placeholder=\""+jsObject.quiz_name+"\" name=\""+jsObject.quiz_name+"\" id=\""+jsObject.quiz_id+"\" /> <br><br> " +
                  " <button type=\"button\" onclick=\"saveQuizName(this.id)\" class=\"btn btn-danger RbtnMargin\" id=\""+question_counter+"\">Namen ändern</button> ";
                  break;
  }
  return htmlResponse;
}

function checkIfCheckedOrNot (value) {
  if (value == true) {
    return "checked=\"true\"";
  } else {
    return "";
  }
}  

// Hier den Question-Counter im Button beachten
function newQuestionHtml(question_counter) {
    var html =  " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\"Frage\" name=\"question_"+question_counter+"\" id=\"q\" /> <br>" +
                " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\"Antwort 1 hier eintragen\" name=\"answer_"+question_counter+"_1\"/> " +
                " <input type=\"checkbox\" name=\"chk"+question_counter+"_1\" /> <br> " +                          
                " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\"Antwort 2 hier eintragen\" name=\"answer_"+question_counter+"_2\"/> " +
                " <input type=\"checkbox\" name=\"chk"+question_counter+"_2\" /> <br> " +                           
                " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\"Antwort 3 hier eintragen\" name=\"answer_"+question_counter+"_3\"/> " +
                " <input type=\"checkbox\" name=\"chk"+question_counter+"_3\" /> <br> " +
                " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\"Antwort 4 hier eintragen\" name=\"answer_"+question_counter+"_4\"/> " +
                " <input type=\"checkbox\" name=\"chk"+question_counter+"_4\" /> <br>" +                
                " <button type=\"button\" onclick=\"addQuestion(this.id)\" class=\"btn btn-danger pull-right RbtnMargin\" id=\""+question_counter+"\">Frage hinzufügen</button> " +
                " <p></p> ";
    question_counter++;                
    return html;
}

function saveQuestion(thisId) {
  var question_id = document.getElementById("myForm").elements[0+(10*thisId)].id;
  console.log("saveQuestion() question_id: " + question_id);
  postHttpRequest(thisId, question_id, "sQ");
  console.log("qs-counter: " + question_counter);
  console.log("thisId: " + thisId);
}

function addQuestion(thisId) {
  postHttpRequest(thisId, 0, "aQ");
}

function saveQuizName() {  
  postHttpRequest(0,0,"sQ_Name");
}

function nextQuestion() {

  var d1 = document.getElementById('q1');
  d1.insertAdjacentHTML('beforeend', newQuestionHtml(question_counter));

  var divs = document.getElementsByClassName('jumbotron');
  for(var i=0; i < divs.length; i++) { 
    divs[i].style.height = "100%";
  }
}
// Erstellen eines Frage-Json String um die Frage upzudaten
function createQuestionJson(button_id, question_id) {
  // Für den QuizNamen
  // var quizname = document.getElementById("myForm2").elements[0].value;

  var j = 10; // elemente in myForm

    if (question_id != false) {
      var json = '{ "question_id":'+question_id+',';  
    } else {
      var json = '{ "quiz_id":'+ document.getElementById("myForm2").elements[0].id +', "questions": [ {';
    }

    json +=      '  "question":"'+document.getElementById("myForm").elements[0+(j*button_id)].value+'", '  +
                   ' "answers": [' +
                   ' {"answer":"'+document.getElementById("myForm").elements[1+(j*button_id)].value+'" ,  ';
    if (question_id != false) {json+= ' "answer_id":'+document.getElementById("myForm").elements[1+(j*button_id)].id+' ,    ';}
    json +=        ' "correct":'+document.getElementById("myForm").elements[2+(j*button_id)].checked+' }, ' +
                   ' {"answer":"'+document.getElementById("myForm").elements[3+(j*button_id)].value+'" ,  ';
    if (question_id != false) {json+= ' "answer_id":'+document.getElementById("myForm").elements[3+(j*button_id)].id+' ,    ';}
    json +=        ' "correct":'+document.getElementById("myForm").elements[4+(j*button_id)].checked+' }, ' +
                   ' {"answer":"'+document.getElementById("myForm").elements[5+(j*button_id)].value+'" ,  ';
    if (question_id != false) {json+= ' "answer_id":'+document.getElementById("myForm").elements[5+(j*button_id)].id+' ,    ';}
    json +=        ' "correct":'+document.getElementById("myForm").elements[6+(j*button_id)].checked+' }, ' +
                   ' {"answer":"'+document.getElementById("myForm").elements[7+(j*button_id)].value+'" ,  ';
    if (question_id != false) {json+= ' "answer_id":'+document.getElementById("myForm").elements[7+(j*button_id)].id+' ,    ';}
    json +=        ' "correct":'+document.getElementById("myForm").elements[8+(j*button_id)].checked+' } ] } ] }';                                              
    return json;
}

function getJsonString() {

  var quiz_id = getURLParameter('quiz_id');
  console.log("getParameterByName: " + quiz_id);
  var xmlhttp = null;
  url = "../ClientServlet?code=edit&quiz_id="+quiz_id+"";
                
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
        var html = buildHtmlPage(xmlhttp.responseText);
        $('q1').innerHTML = html;
        var html2 = buildHtmlQuizName(xmlhttp.responseText);
        $('q0').innerHTML = html2;
    }
  };
  xmlhttp.open("GET", url, true); 
  xmlhttp.send(null);
}

function postHttpRequest(button_id, question_id, code) {
    var param, updateString;
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
            console.log("Update! Json an Servlet übertragen");
        }
    };

    xmlhttp.open("POST", url, true); 
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

    if (code == "sQ") {
      console.log("sQ");
      updateString = createQuestionJson(button_id, question_id);
      param = "js="+updateString+"&code=update";
    }
    if (code == "aQ") {
      console.log("aQ");
      updateString = createQuestionJson(button_id,false);
      param = "js="+updateString+"&code=addQuiz";
    }
    if (code == "sQ_Name") {
      console.log("sQ_Name");
      var quiz_id = document.getElementById("myForm2").elements[0].id;
      var quiz_name = document.getElementById("myForm2").elements[0].value;
      param = "js="+quiz_name+"&quiz_id="+quiz_id+"&code=updateName";
      console.log("param: " + param);
    }

    xmlhttp.send(param);
}