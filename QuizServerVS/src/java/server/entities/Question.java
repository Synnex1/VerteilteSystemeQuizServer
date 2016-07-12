package server.entities;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Question extends UnicastRemoteObject{
    int question_Id, quiz_Id_f;
    String question;
    Boolean trueOrFalse;

    public Question(int question_Id, int quiz_Id_f, String question, Boolean trueOrFalse) throws RemoteException {
        this.question_Id = question_Id;
        this.quiz_Id_f = quiz_Id_f;
        this.question = question;
        this.trueOrFalse = trueOrFalse;
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

    public Boolean getTrueOrFalse() {
        return trueOrFalse;
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

    public void setTrueOrFalse(Boolean trueOrFalse) {
        this.trueOrFalse = trueOrFalse;
    }
    
}
