$(document).ready(function(){
    document.getElementById('pin').innerHTML = getURLParameter('pin');

    window.setInterval(function() {
        getPlayers();
    }, 5000); 

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
            var i = 0;

            html = '<ul class="list-group">';

		for (var x in jsObject) {
		if (i = 0) {
			html += '<li class="list-group-item list-group-item-success"><h3>'+jsObject[players].name+'</h3></li>';			
		}
		else if (i = 1) {
			html += '<li class="list-group-item list-group-item-info"><h3>'+jsObject[players].name+'</h3></li>';
		}
		else if (i = 2) {
			html += '<li class="list-group-item list-group-item-warning"><h3>'+jsObject[players].name+'</h3></li>';
			var i = 0;
		} 		    
		i++;
		players++;
		}

		html += '</ul>';	            
        console.log(jsonString);
        console.log(players);
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
            console.log("antwort: " + answer);        
        }
    };
    xmlhttp.open("GET", url+"?code=waitForStart&js="+pin+"", true);    
    xmlhttp.send();    
}

function test() {
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
            console.log("test_"+xmlhttp.responseText);          
        }
    };
    xmlhttp.open("GET", url+"?code=test&js=test", true);    
    xmlhttp.send();
}