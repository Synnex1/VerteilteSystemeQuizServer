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
    QuizServerImpl qs;
    
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

    @Override
    public void deleteQuiz(int quizId) throws RemoteException {
        qsdb.deleteQuiz(quizId);
    }

    @Override
    public void deleteQuestion(int questionId) throws RemoteException {
        qsdb.deleteQuestion(questionId);
    }

    @Override
    public String readyQuiz(int quizId) throws RemoteException {
        String code = qs.readyQuiz(quizId);
        return qsdb.getQuizInfo(quizId, code);
    }
    
    @Override
    public void startQuiz(String code) throws RemoteException {
        qs.startQuiz(code);
    }

    @Override
    public Boolean joinQuiz(String code, String userId) throws RemoteException {
        String name = qsdb.getUserName(userId);
        if ( name == null ) {
            System.err.println("Datenbank gab keinen Usernamen zur UserId aus!");
            return false;
        } else {
            if( qs.joinQuiz(code, userId, name) ) {
                return true;
            } else {
                System.err.println("Konnte Client dem Quiz net zuweisen!");
                return false;
            }
        }
    }

    @Override
    public Boolean increaseHighscore(String code, String userId, int time) throws RemoteException {
        if (qs.increaseHighscore(code, userId, time)) {
            return true;
        } else {
            return false;
        }      
    }

    @Override
    public String getHighscore(String code) throws RemoteException {
        return qs.getHighscore(code);
    }
    
    
}
