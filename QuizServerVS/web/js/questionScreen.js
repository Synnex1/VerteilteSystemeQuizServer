$(document).ready(function(){
  startQuizHttpRequest();
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
    myStopFunction();
    stopQuestion();
  }
}

function myStopFunction() {
    clearInterval(myVar);
}

function setAnswer (btn) {
  setTimer(sec);
  answer = btn;
  for (var i = 0; i < 4; i++) {
    document.getElementById("myForm").elements[i].disabled = "disabled";
  }
  console.log("setAnswer("+btn+")");
  $("[name=selected"+btn+"]").css("visibility","visible");      
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
  for (var i = 1; i <= 4; i++) {
    if (i != getAnswer() ) {
      $("form [name=btn"+i+"]").fadeTo(1000, 0.5);
    } else {
      $("[name=selected"+getAnswer()+"]").css("visibility","hidden");
      $("[name=correct"+getAnswer()+"]").css("visibility","visible");
    }
  } 


    // Antwort überprüfen
    if ( document.getElementById('h'+getAnswer()+'').value == 'true' ) {
      // Punkte updaten
      console.log('Die Antwort war korrekt!');
      updatePoints();
    } else {
      // do nothing ?
      console.log('Die Antwort war falsch!');
    }  
}

function startQuizHttpRequest() {
  var xmlhttp = null;
  var url = '../../ClientServlet3';
  var quiz_id = getURLParameter('quiz_id');
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
  xmlhttp.open("GET", url+'?code=startQuiz&js='+getURLParameter('pin')+'', true);    
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

