function $(id) {
    return document.getElementById(id);
}

var question_counter = 1;

function getURLParameter(name) {
  return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [null, ''])[1].replace(/\+/g, '%20')) || null;
}

function buildHtmlPage (jsonString) {

  var jsObject = JSON.parse( jsonString );            
  var htmlResponse = '';
  var x; 
  var i = 0;

  for (x in jsObject.questions) {

      htmlResponse +=  
                  " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\""+jsObject.questions[i].question+"\" name=\"question_1\" id=\"q\" /> <br>" +
                  " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\""+jsObject.questions[i].answers[0].answer+"\" name=\"answer_"+i+"_1\"/> " +
                  " <input type=\"checkbox\" "+checkIfCheckedOrNot(jsObject.questions[i].answers[0].correct)+" name=\"chk"+i+"_1\" /> <br> " +                          
                  " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\""+jsObject.questions[i].answers[1].answer+"\" name=\"answer_"+i+"_2\"/> " +
                  " <input type=\"checkbox\" "+checkIfCheckedOrNot(jsObject.questions[i].answers[1].correct)+" name=\"chk"+i+"_2\" /> <br> " +                           
                  " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\""+jsObject.questions[i].answers[2].answer+"\" name=\"answer_"+i+"_3\"/> " +
                  " <input type=\"checkbox\" "+checkIfCheckedOrNot(jsObject.questions[i].answers[2].correct)+" name=\"chk"+i+"_3\" /> <br> " +
                  " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\""+jsObject.questions[i].answers[3].answer+"\" name=\"answer_"+i+"_4\"/> " +
                  " <input type=\"checkbox\" "+checkIfCheckedOrNot(jsObject.questions[i].answers[3].correct)+" name=\"chk"+i+"_4\" /> <br>" +                
                  " <button type=\"button\" onclick=\"saveQuestion()\" class=\"btn btn-danger pull-right RbtnMargin\" >Frage speichern</button> " +
                  " <p></p> ";
      i++;  
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

//  var html =  " <input type=\"text\" class=\"form-control\" size=\"50\" placeholder=\"ok\" name=\"question_"+question_counter+"\" id=\"q\" /> <br>";

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
                " <button type=\"button\" onclick=\"saveQuestion()\" class=\"btn btn-danger pull-right RbtnMargin\" >Frage speichern</button> " +
                " <p></p> ";
    return html;
}

function saveQuestion() {
  alert("speichern!");
}

function nextQuestin() {

  question_counter++;

  var d1 = document.getElementById('q1');
  d1.insertAdjacentHTML('beforeend', newQuestionHtml(question_counter));

  var divs = document.getElementsByClassName('jumbotron');
  for(var i=0; i < divs.length; i++) { 
    divs[i].style.height = "100%";
  }
}

function createJSON(question_counter) {

  var quizname = document.getElementById("myForm2").elements[0].value;

    var json = '{ "quiz_name":"'+quizname+'", "questions": [ ';

    for (var i = 0;i < question_counter;i++) {   

      json +=      '{  "question":"'+document.getElementById("myForm").elements[0+(10*i)].value+'", ' +
                     ' "answers": [ {' +
                     ' "answer":"'+document.getElementById("myForm").elements[1+(10*i)].value+'" , ' +
                     ' "correct":'+document.getElementById("myForm").elements[2+(10*i)].checked+' }, ' +
                     ' {"answer":"'+document.getElementById("myForm").elements[3+(10*i)].value+'" , ' +
                     ' "correct":'+document.getElementById("myForm").elements[4+(10*i)].checked+' }, ' +
                     ' {"answer":"'+document.getElementById("myForm").elements[5+(10*i)].value+'" , ' +
                     ' "correct":'+document.getElementById("myForm").elements[6+(10*i)].checked+' }, ' +
                     ' {"answer":"'+document.getElementById("myForm").elements[7+(10*i)].value+'" , ';
                     if (i+1 != question_counter) {
                      json += ' "correct":'+document.getElementById("myForm").elements[8+(10*i)].checked+' } ] }, ';
                     } else {
                      json += ' "correct":'+document.getElementById("myForm").elements[8+(10*i)].checked+' } ] } ] }';
                     }                       
    }
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
    }
  };
  xmlhttp.open("GET", url, true); 
  xmlhttp.send(null);
}