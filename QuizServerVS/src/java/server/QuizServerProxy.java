/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import client.ClientProxy;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Mike
 */
public interface QuizServerProxy extends Remote{

    /**
     * Returns all quiz entries in the database created by a specified user.
     * 
     * @param user_Id   id of the specified user
     * @return  returns a json formatted String of all found quiz entries.
     * @throws RemoteException
     */
    public String getAllQuizFromUser(String user_Id) throws RemoteException;

    /**
     * 
     * 
     * @param jsonString
     * @param userId
     * @throws RemoteException
     */
    public void createQuiz(String jsonString, String userId) throws RemoteException;

    /**
     *
     * @param quizId
     * @return
     * @throws RemoteException
     */
    public String getQuizInfo(int quizId) throws RemoteException;

    /**
     *
     * @param quizId
     * @param quizName
     * @throws RemoteException
     */
    public void updateQuiz(int quizId, String quizName) throws RemoteException;

    /**
     *
     * @param jsonString
     * @throws RemoteException
     */
    public void updateQuestion(String jsonString) throws RemoteException;

    /**
     *
     * @param jsonString
     * @param userId
     * @throws RemoteException
     */
    public void createQuestions(String jsonString, String userId) throws RemoteException;

    /**
     *
     * @param quizId
     * @throws RemoteException
     */
    public void deleteQuiz(int quizId) throws RemoteException;

    /**
     *
     * @param questionId
     * @throws RemoteException
     */
    public void deleteQuestion(int questionId) throws RemoteException;

    /**
     *
     * @param quizId
     * @return
     * @throws RemoteException
     */
    public String readyQuiz(int quizId) throws RemoteException;

    /**
     *
     * @param code
     * @throws RemoteException
     */
    public void startQuiz(String code) throws RemoteException;

    /**
     *
     * @param code
     * @param userId
     * @param clp
     * @return
     * @throws RemoteException
     */
    public String joinQuiz(String code, String userId, ClientProxy clp) throws RemoteException;

    /**
     *
     * @param code
     * @param userId
     * @param time
     * @return
     * @throws RemoteException
     */
    public Boolean increaseHighscore(String code, String userId, int time) throws RemoteException;

    /**
     *
     * @param code
     * @return
     * @throws RemoteException
     */
    public String getHighscore(String code) throws RemoteException;
    
    public String nextQuestion(String code) throws RemoteException;
    
    public String getNextQuestion(String code) throws RemoteException;

    /**
     *
     * @param code
     * @return
     * @throws RemoteException
     */
    public Boolean endQuiz(String code) throws RemoteException;
}
