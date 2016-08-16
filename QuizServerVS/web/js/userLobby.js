$(document).ready(function(){
    document.getElementById('pin').innerHTML = getURLParameter('pin');

    var questionNo = '1';
    localStorage.setItem("questionNo", questionNo);
    getQuestionAmount();    

    window.setInterval(function() {
        getPlayers();
    }, 7000); 

    waitForStart();
});

function getURLParameter(name) {
  return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [null, ''])[1].replace(/\+/g, '%20')) || null;
}

function getPlayers() {
    var xmlhttp = null;
    var url = '../../ClientServlet3';
    var pin = getURLParameter('pin');
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
    		  html += '<li class="list-group-item list-group-item-info">'+jsObject[players].name+'</li>';
    		  players++;
    		}

    		html += '</ul>';	            
           	document.getElementById('playerList').innerHTML = html;
        	document.getElementById('players').innerHTML = players;          
        }
    };
    xmlhttp.open("GET", url+"?code=getPlayers&js="+ pin +"", true);    
    xmlhttp.send();
}

function waitForStart() {
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
            window.location.replace('questionScreen.html?pin='+ document.getElementById('pin').innerHTML +'');        
        }
    };
    xmlhttp.open("GET", url+"?code=waitForStart&js="+pin+"", true);    
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
            console.log("getQuestionAmount(): "+xmlhttp.responseText);
            localStorage.setItem("questionAmount", xmlhttp.responseText );          
        }
    };
    xmlhttp.open("GET", url+'?code=getQuestionAmount&js='+pin+'', true);    
    xmlhttp.send();    
}

