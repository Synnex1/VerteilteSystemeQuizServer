/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Mike
 */
public class QuizServerProxyImpl extends UnicastRemoteObject implements QuizServerProxy{
    QuizServerDB qsdb;
    
    public QuizServerProxyImpl() throws RemoteException {
        this.qsdb = new QuizServerDB();
    }

    @Override
    public String getAllQuizFromUser(String user_Id) throws RemoteException {
        return qsdb.getAllQuizFromUser(user_Id);
    }

    @Override
    public void createQuiz(String jsonString, String userId) throws RemoteException {
        qsdb.createQuiz(jsonString, userId);
    }

    @Override
    public String getQuizInfo(int quizId) throws RemoteException {
        return qsdb.getQuizInfo(quizId);
    }

    @Override
    public void updateQuiz(int quizId, String quizName) throws RemoteException {
        qsdb.updateQuiz(quizId, quizName);
    }

    @Override
    public void updateQuestion(String jsonString) throws RemoteException {
        qsdb.updateQuestion(jsonString);
    }

    @Override
    public void createQuestions(String jsonString, String userId) throws RemoteException {
        qsdb.createQuestions(jsonString, userId);
    }
    
}
