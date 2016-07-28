$(document).ready(function(){

  var questionNo = localStorage.getItem("questionNo"); 
  document.getElementById('currentQuestion').innerHTML = questionNo;
  localStorage.setItem("questionNo", ++questionNo);
  
  startQuizHttpRequest();
  setTimeout(function(){ checkEndQuizFlag(); }, 3000);    
});

var sec = 26;
var myVar = setInterval(function(){ myTimer() }, 1000);
var answer = 0;
var time = 0;  
var getIt ;

function getURLParameter(name) {
  return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [null, ''])[1].replace(/\+/g, '%20')) || null;
}

function myTimer() {
  sec--;
  document.getElementById("timer").innerHTML = sec ;
  if(sec == 00) {
    clearInterval(myVar);
    stopQuestion();
  }
}

function stopQuestion() {

  var themeSong = document.getElementById("themeSong");
  themeSong.pause();

  var countdown = document.getElementById("countdown");
  countdown.play();

  for (var i = 1; i <= 4; i++) {
    if ( document.getElementById('h'+i+'').value != 'true' ) {
      $("form [name=btn"+i+"]").fadeTo(1000, 0.5);
    } else {
      $("[name=selected"+i+"]").css("visibility","hidden");
      $("[name=correct"+i+"]").css("visibility","visible");
    }
  } 
  // Die Statistik anzeigen
  document.getElementById("answers").innerHTML = "Ergebnisse werden geladen!";
  getStats();
}

function startQuizHttpRequest() {
  var xmlhttp = null;
  var url = '../../ClientServlet3';
  var quiz_id = getURLParameter('quiz_id');
  var sessionHandle = '';
  if ( getURLParameter('param') == 'startSession' ) {
    sessionHandle = '&param=startSession';
  }   
  if (window.XMLHttpRequest) {
      xmlhttp = new XMLHttpRequest(); // Mozilla
  }    
  else if (window.ActiveXObject) {
      xmlhttp = new ActiveXObject("Microsoft.XMLHTTP"); // IE
  }
 
  xmlhttp.onreadystatechange = function() {
      if(xmlhttp.readyState === 4 && xmlhttp.status === 200) {
        
        var jsonString = xmlhttp.responseText;          
        var jsObject = JSON.parse( jsonString );
        console.log(jsonString);
        document.getElementById('question').innerHTML = jsObject.question;

        for (var i = 1; i <= 4; i++) {
          document.getElementById('a'+i+'').innerHTML =jsObject.answers[i-1].answer;
          document.getElementById('h'+i+'').value =jsObject.answers[i-1].correct;            
        }       
      }
  };
  xmlhttp.open("GET", url+'?code=startQuiz'+sessionHandle+'&js='+getURLParameter('pin')+'', true);    
  xmlhttp.send(); 
}

function getStats() {
  var xmlhttp = null;
  var url = '../../ClientServlet3'; 
  var param = '';
  if (window.XMLHttpRequest) {
      xmlhttp = new XMLHttpRequest(); // Mozilla
  }    
  else if (window.ActiveXObject) {
      xmlhttp = new ActiveXObject("Microsoft.XMLHTTP"); // IE
  }
 
  xmlhttp.onreadystatechange = function() {
      if(xmlhttp.readyState === 4 && xmlhttp.status === 200) {
      var jsonString = xmlhttp.responseText;
      console.log(jsonString);  
      var jsObject = JSON.parse( jsonString );
      var players = 0;

      html = '<ol class="list-group">';

      for (var x in jsObject) {
        html += '<li class="list-group-item list-group-item-success"><h3>'+jsObject[players].name+'</h3><h3>'+jsObject[players].highscore+'</h3></li>';     
        players++;
      }
      
      console.log("getIt: " + getIt);
      if ( getIt == true) {
        console.log("checkEndQuizFlag: true");
        html += '<br><li> <button type="button" class="btn-lg btn-danger" id="endQuiz" onclick="endQuiz()">Quiz beenden</button> </li></ol>';
      } 
      if (getIt == false) {
        console.log("checkEndQuizFlag: false");
        html += '<br><li> <button type="button" class="btn-lg btn-success" id="nextQuestion" onclick="nextQuestion()">Nächste Frage</button> </li></ol>';
      }
      document.getElementById('container2').innerHTML = html;   
      }       
  }; 
  xmlhttp.open("GET", url+'?code=getPoints&js='+getURLParameter('pin')+'', true);    
  xmlhttp.send();   
}

function nextQuestion() {
  window.location.replace('questionScreenMaster.html?pin='+ getURLParameter('pin') +'');
}

function checkEndQuizFlag() {
  var xmlhttp = null;
  var url = '../../ClientServlet3'; 
  if (window.XMLHttpRequest) {
      xmlhttp = new XMLHttpRequest(); // Mozilla
  }    
  else if (window.ActiveXObject) {
      xmlhttp = new ActiveXObject("Microsoft.XMLHTTP"); // IE
  }
 
  xmlhttp.onreadystatechange = function() {
      if(xmlhttp.readyState === 4 && xmlhttp.status === 200) {
        var jsonString = xmlhttp.responseText;
        console.log(jsonString);  
          
        if (jsonString == "endQuizFlag") {
          console.log("Im if");
          getIt = true;    
        } else {
          console.log("Im else");
          getIt = false;              
        }         
      }       
  };
  
  xmlhttp.open("GET", url+'?code=checkEndQuizFlag&js='+getURLParameter('pin')+'', true);    
  xmlhttp.send(); 

}




