package server.entities;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Answer extends UnicastRemoteObject {
    int answer_Id, question_Id_f;
    String answer;

    public Answer(int answer_Id, int question_Id_f, String answer) throws RemoteException{
        this.answer_Id = answer_Id;
        this.question_Id_f = question_Id_f;
        this.answer = answer;
    }

    public int getAnswer_Id() {
        return answer_Id;
    }

    public int getQuestion_Id_f() {
        return question_Id_f;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer_Id(int answer_Id) {
        this.answer_Id = answer_Id;
    }

    public void setQuestion_Id_f(int question_Id_f) {
        this.question_Id_f = question_Id_f;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
