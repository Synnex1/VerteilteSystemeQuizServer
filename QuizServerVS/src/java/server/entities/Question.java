package server.entities;

public class Question {
    int question_Id, quiz_Id_f;
    String question;

    public Question(int question_Id, int quiz_Id_f, String question) {
        this.question_Id = question_Id;
        this.quiz_Id_f = quiz_Id_f;
        this.question = question;
    }

    public int getQuestion_Id() {
        return question_Id;
    }

    public int getQuiz_Id_f() {
        return quiz_Id_f;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion_Id(int question_Id) {
        this.question_Id = question_Id;
    }

    public void setQuiz_Id_f(int quiz_Id_f) {
        this.quiz_Id_f = quiz_Id_f;
    }

    public void setQuestion(String question) {
        this.question = question;
    }        
}
