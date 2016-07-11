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
        if(xmlhttp.readyState !== 4) {
            $('quiz').innerHTML = 'Laden deiner Quiz...';
        }
        if(xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            
            var jsonString = xmlhttp.responseText; 
            var jsObject = JSON.parse( jsonString );            
            var htmlResponse = ''; 
                       
            for (index = 0; index < jsObject.length; ++index) {
                if (jsObject[index] !== null) {
                htmlResponse +="<input readonly type=\"text\" class=\"form-control\" size=\"50\" placeholder=\""+jsObject[index].name+"\">" + ""
                             + "<button type=\"button\" class=\"btn btn-danger pull-right RbtnMargin\" id=\""+jsObject[index].quiz_Id+"\" >Starten</button> " + ""
                             + "<button type=\"button\" class=\"btn btn-danger pull-right\" id=\""+jsObject[index].quiz_Id+"\">Bearbeiten</button><br><br>"; 
                 }
            }
            $('quiz').innerHTML = htmlResponse;
        }
    };
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