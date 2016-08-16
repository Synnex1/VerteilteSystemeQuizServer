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
 * Interface class for the client to remote call methods.
 *
 * @author Mike
 */
public interface QuizServerProxy extends Remote{

    /**
     * Returns all quiz entries in the database created by a specified user.
     * 
     * @param user_Id   id of the specified user.
     * @return  Returns a json formatted String of all found quiz entries.
     * @throws RemoteException
     */
    public String getAllQuizFromUser(String user_Id) throws RemoteException;

    /**
     * Creates a new entry of a quiz in the database.
     * 
     * @param jsonString a string in JSON format including quizname and all created questions with answers.
     * @param userId specified user-id of the user that created the quiz.
     * @throws RemoteException
     */
    public void createQuiz(String jsonString, String userId) throws RemoteException;

    /**
     * Gets all information about a quiz from the database to the given quizId.
     * 
     * @param quizId the ID of the specified quiz to get information to.
     * @return Returns a json formatted string of the quiz and all of its questions with answers.
     * @throws RemoteException
     */
    public String getQuizInfo(int quizId) throws RemoteException;

    /**
     * Updates the name of a quiz to the given quiz-id.
     * 
     * @param quizId Quiz-id to the quiz that shall be updated
     * @param quizName The stringvalue the name of the quiz will change to.
     * @throws RemoteException
     */
    public void updateQuiz(int quizId, String quizName) throws RemoteException;

    /**
     * Updates a question with all its answers in the database to the given parameters given in the JSON string.
     * 
     * @param jsonString JSON string, that includes all information to the question with all its answers.
     * @throws RemoteException
     */
    public void updateQuestion(String jsonString) throws RemoteException;

    /**
     * Creates a entry in the database of the given questions and answers.
     * 
     * @param jsonString JSON string, that includes an array of questions and their answers.
     * @param userId User-id of the user, who created the questions.
     * @throws RemoteException
     */
    public void createQuestions(String jsonString, String userId) throws RemoteException;

    /**
     * Deletes the quiz and all of its questions with answers given by the quiz-id.
     * 
     * @param quizId Quiz-id of the quiz, that will be deleted.
     * @throws RemoteException
     */
    public void deleteQuiz(int quizId) throws RemoteException;

    /**
     * Deletes the entry of a question in a database, found by the given question-id.
     *
     * @param questionId Id of the question that will be deleted.
     * @throws RemoteException
     */
    public void deleteQuestion(int questionId) throws RemoteException;

    /**
     * Readies the quiz found by the given quiz-id. It also creates a random alphanumeric code to join the quiz.
     *
     * @param quizId Id of the quiz that will be readied up.
     * @return Returns a JSON string that contains the name of the quiz and a code that gives access to join the quiz.
     * @throws RemoteException
     */
    public String readyQuiz(int quizId) throws RemoteException;

    /**
     * Starts the quiz and sets a Flag, that signals every user that wants to join that it is already started.
     *
     * @param code A random generated alphanumeric code, that specifies a quiz.
     * @throws RemoteException
     */
    public void startQuiz(String code) throws RemoteException;

    /**
     * Joins a quiz thats specified by the given code.
     *
     * @param code A random generated alphanumeric code, that specifies a quiz.
     * @param userId User-id of the user, who wants to join the quiz.
     * @param clp A remotobject to synchronize the quizevent.
     * @return Returns the name of the quiz.
     * @throws RemoteException
     */
    public String joinQuiz(String code, String userId, ClientProxy clp) throws RemoteException;

    /**
     * Increases the Highscore of an user in the specified quiz given by the code.
     *
     * @param code A random generated alphanumeric code, that specifies a quiz.
     * @param userId User-if of the user, which highscore will be increased.
     * @param time The remaining time the user had after he clicked the correct answer.
     * @return Returns true if the highscore got increased.
     * @throws RemoteException
     */
    public Boolean increaseHighscore(String code, String userId, int time) throws RemoteException;

    /**
     * Returns a list of all players and their highscore in the specified quiz.
     *
     * @param code A random generated alphanumeric code, that specifies a quiz.
     * @return Returns a list of all players and their highscore.
     * @throws RemoteException
     */
    public String getHighscore(String code) throws RemoteException;
    
    /**
     * Increases the question-index of the quiz by 1 and returns the question pointed to by the question-index in the quiz object. This method is called only by the quizmaster.
     *
     * @param code A random generated alphanumeric code, that specifies a quiz.
     * @return Returns a JSON string, including the question and all of its answers.
     * @throws RemoteException
     */
    public String nextQuestion(String code) throws RemoteException;
    
    /**
     * Returns the next question ponted to by the question-index in the quiz object. This method is called only by the players.
     *
     * @param code A random generated alphanumeric code, that specifies a quiz.
     * @return Returns a JSON string, including the question and all of its answers.
     * @throws RemoteException
     */
    public String getNextQuestion(String code) throws RemoteException;

    /**
     * Ends a quiz after the last question. This method is called only by the quizmaster.
     *
     * @param code A random generated alphanumeric code, that specifies a quiz.
     * @return Returns true if the quiz ended properly.
     * @throws RemoteException
     */
    public Boolean endQuiz(String code) throws RemoteException;
    
    /**
     * Returns the number of questions in the quiz specifed by the given code.
     *
     * @param code A random generated alphanumeric code, that specifies a quiz.
     * @return Returns the number of questions in a quiz.
     * @throws RemoteException
     */
    public int questionCount(String code) throws RemoteException;
}
