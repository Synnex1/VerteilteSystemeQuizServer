$(document).ready(function(){

  var questionNo = localStorage.getItem("questionNo"); 
  document.getElementById('currentQuestion').innerHTML = questionNo;
  localStorage.setItem("questionNo", ++questionNo);
  getQuestionAmount();
  
  startQuizHttpRequest();
  // setTimeout(function(){ checkEndQuizFlag(); }, 7000);    
});

var sec = 26;
var myVar = setInterval(function(){ myTimer() }, 1000);
var answer = 0;
var time = 0;  
// var getIt;
var questionAmount;

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
        // Ausgabe des Quiz-JSON
        // console.log(jsonString);
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
      // Ausgabe der Punkte mit Namen
      // console.log(jsonString);  
      var jsObject = JSON.parse( jsonString );
      var players = 0;

      html = '<ol class="list-group">';

      for (var x in jsObject) {
        html += '<li class="list-group-item list-group-item-success"><h3>'+jsObject[players].name+' -> '+jsObject[players].highscore+' XP</h3></li>';     
        players++;
      }
      if ( localStorage.getItem("questionNo")-1 == questionAmount ) {
        html += '<br><li> <button type="button" class="btn-lg btn-danger" id="endQuiz" onclick="endQuiz()">Quiz beenden</button> </li></ol>';
      } 
      else {
        html += '<br><li> <button type="button" class="btn-lg btn-success" id="nextQuestion" onclick="nextQuestion()">Nächste Frage</button> </li></ol>';
      }
      /*
      document.getElementById('timer_wrapper').innerHTML = '';
      document.getElementById('question').innerHTML = '<div id="answers">Scoreboard</div>';
      */
      document.getElementById('answers').innerHTML = html;   
      }       
  }; 
  xmlhttp.open("GET", url+'?code=getPoints&js='+getURLParameter('pin')+'', true);    
  xmlhttp.send();   
}

function nextQuestion() {
  window.location.replace('questionScreenMaster.html?pin='+ getURLParameter('pin') +'');
}
/*
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
        console.log("response: " + xmlhttp.responseText);
          
        if (jsonString == "true") {
          getIt = true;    
        } else {
          getIt = false;              
        }
        console.log("getIt->" + getIt);         
      }       
  };
  
  xmlhttp.open("GET", url+'?code=checkEndQuizFlag&js='+getURLParameter('pin')+'', true);    
  xmlhttp.send(); 
} */

function endQuiz() {
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
          
        setTimeout(function(){ window.location.replace('../dashboard.html'); }, 3000);

      }       
  };
  
  xmlhttp.open("GET", url+'?code=endQuiz&js='+getURLParameter('pin')+'', true);    
  xmlhttp.send();   
}

function getQuestionAmount() {
    var xmlhttp = null;
    var url = '../../ClientServlet3';
    var pin = getURLParameter('pin');    
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest(); // Mozilla
    }    
    else if (window.ActiveXObject) {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP"); // IE
    }
   
    xmlhttp.onreadystatechange = function() {
        if(xmlhttp.readyState === 4 && xmlhttp.status === 200) {           
            document.getElementById('questionAmount').innerHTML = xmlhttp.responseText;
            questionAmount = xmlhttp.responseText;
        }
    };
    xmlhttp.open("GET", url+'?code=getQuestionAmount&js='+pin+'', true);    
    xmlhttp.send();    
}
