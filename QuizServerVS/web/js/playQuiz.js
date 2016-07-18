var myVar = setInterval(function(){ myTimer() }, 1000);
var sec = 26;
var answer = 0;

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

  function stopQuestion() {
    for (var i = 1; i <= 4; i++) {
      if (i != getAnswer() ) {
        $("form [name=btn"+i+"]").fadeTo(1000, 0.5);
      } else {
        $("[name=selected"+getAnswer()+"]").css("visibility","hidden");
        $("[name=correct"+getAnswer()+"]").css("visibility","visible");
      }
    }   
  }