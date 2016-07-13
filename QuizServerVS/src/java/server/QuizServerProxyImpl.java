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
    
}
