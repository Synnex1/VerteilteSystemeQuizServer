$(document).ready(function(){

    var questionNo = '1';
    localStorage.setItem("questionNo", questionNo);

    beginQuizHttpRequest();
});

window.setInterval(function() {
  getPlayers();
}, 5000); 

function getURLParameter(name) {
  return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [null, ''])[1].replace(/\+/g, '%20')) || null;
}

function beginQuizHttpRequest() {
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

            document.getElementById('pin').innerHTML = jsObject.code;
            // document.getElementById('players').innerHTML =jsObject.quizName;          
        }
    };
    xmlhttp.open("GET", url+"?code=beginQuiz&js=beginQuiz&quiz_id="+quiz_id+"", true);    
    xmlhttp.send();
}

function getPlayers() {
    var xmlhttp = null;
    var url = '../../ClientServlet3';
    var quiz_id = getURLParameter('quiz_id');
    var players = 0;
    var html = '';

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

            html = '<ul class="list-group">';

    		for (var x in jsObject) {
    		  html += '<li class="list-group-item list-group-item-info"><h3>'+jsObject[players].name+'</h3></li>';
    		  players++;
    		}

    		html += '</ul>';	            
           	document.getElementById('playerList').innerHTML = html;
        	document.getElementById('players').innerHTML = players;          
        }
    };
    xmlhttp.open("GET", url+"?code=getPlayers&js="+ document.getElementById('pin').innerHTML +"", true);    
    xmlhttp.send();
}	

function startQuiz() {
	window.location.replace('questionScreenMaster.html?param=startSession&pin='+ document.getElementById('pin').innerHTML +'');
}