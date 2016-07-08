function $(id) {
    return document.getElementById(id);
}

function getHttpRequest(url) {
    var xmlhttp = null;
    // Mozilla
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    }
    // IE
    else if (window.ActiveXObject) {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
   
    xmlhttp.onreadystatechange = function() {
        if(xmlhttp.readyState != 4) {
            $('quiz').innerHTML = 'Laden deiner Quiz...';
        }
        if(xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            $('quiz').innerHTML = xmlhttp.responseText;
        }
    }
    xmlhttp.open("GET", url, true);    
    xmlhttp.send();
}

function postHttpRequest(url) {
    // TO BE IMPLEMENTED!!!
}

function putHttpRequest(url, id) {
    // TO BE IMPLEMENTED!!!
}

function deleteHttpRequest(url, id) {
    // TO BE IMPLEMENTED!!!
}