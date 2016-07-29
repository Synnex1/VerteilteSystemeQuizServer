$(document).ready(function(){
  startQuizHttpRequest();

  var questionNo = localStorage.getItem("questionNo"); 
  document.getElementById('currentQuestion').innerHTML = questionNo;
  localStorage.setItem("questionNo", ++questionNo);

});

var sec = 26;
var myVar = setInterval(function(){ myTimer() }, 1000);
var answer = 0;
var time = 0;

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

function setAnswer (btn) {
  setTimer(sec);
  answer = btn;
  for (var i = 1; i <= 4; i++) {
    document.getElementById(''+i+'').disabled = "disabled";
    $('#'+i+'').off('click');
  }
  $("[name=selected"+btn+"]").css("visibility","visible");  

  // Antwort überprüfen
  if ( document.getElementById('h'+getAnswer()) ) {

    if ( document.getElementById('h'+getAnswer()+'').value == 'true' ) {
      // Punkte updaten
      updatePoints();
    }  
  }     
}

function getAnswer () {
  return answer;
}

function setTimer(sec) {
  time = sec;
}

function getTimer() {
  return time;
}

function stopQuestion() {

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
  waitForNextQuestion(); 
}

function startQuizHttpRequest() {
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
          var jsObject = JSON.parse( jsonString );
          document.getElementById('question').innerHTML = jsObject.question;          

          for (var i = 1; i <= 4; i++) {
            document.getElementById('a'+i+'').innerHTML =jsObject.answers[i-1].answer;
            document.getElementById('h'+i+'').value =jsObject.answers[i-1].correct;            
          }       
      }
  };
  xmlhttp.open("GET", url+'?code=getNextQuestionClient&js='+getURLParameter('pin')+'', true);    
  xmlhttp.send(); 
}

function updatePoints() {
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
      }
  };
  xmlhttp.open("GET", url+'?code=updatePoints&js='+getURLParameter('pin')+'&param='+getTimer()+'', true);    
  xmlhttp.send();   
}

function waitForNextQuestion() {
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
          var answer = xmlhttp.responseText;              
          console.log("waitForStart() response: " + answer);
          if ( answer == "endQuiz" ) {
            var endQuizHtml = 'Das Quiz ist vorbei!<br><button type="button" class="btn-lg btn-danger" id="toDashboard" onclick="toDashboard()">Zum Dashboard</button>';
            document.getElementById('answers').innerHTML = endQuizHtml;
          } else {
            window.location.replace('questionScreen.html?pin='+ getURLParameter('pin') +'');
          }                  
      }
  };
  xmlhttp.open("GET", url+"?code=waitForStart&js="+pin+"", true);    
  xmlhttp.send();  
}

function toDashboard() {
  window.location.replace('../dashboard.html');
}


