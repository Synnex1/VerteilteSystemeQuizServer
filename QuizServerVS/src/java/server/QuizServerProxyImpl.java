/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import client.ClientProxy;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

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
        JsonObject obj = qsdb.getQuestions(quizId);
        String quizName = obj.getString("quiz_name");
        JsonArray questions = obj.getJsonArray("questions");
        String code = qs.readyQuiz(quizName, questions);
        
        JsonObject returnObj = Json.createObjectBuilder()
                .add("code", code)
                .add("quizName", quizName)
                .build();
        
        
        return returnObj.toString();
    }
    
    @Override
    public void startQuiz(String code) throws RemoteException {
        qs.startQuiz(code);
    }

    @Override
    public String joinQuiz(String code, String userId, ClientProxy clp) throws RemoteException {
        String name = qsdb.getUserName(userId);
        if ( name == null ) {
            System.err.println("Datenbank gab keinen Usernamen zur UserId aus!");
            return null;
        } else {
            return qs.joinQuiz(code, userId, name, clp);
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

    @Override
    public Boolean endQuiz(String code) throws RemoteException {
        return qs.endQuiz(code);
    }

    @Override
    public String nextQuestion(String code) throws RemoteException {
        return qs.nextQuestion(code);
    }

    @Override
    public String getNextQuestion(String code) throws RemoteException {
        return qs.getNextQuestion(code);
    }
    
    
}
