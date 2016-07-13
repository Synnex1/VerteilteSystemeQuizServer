/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Mike
 */
public interface QuizServerProxy extends Remote{
    public String getAllQuizFromUser(String user_Id) throws RemoteException;
    public void createQuiz(String jsonString, String userId) throws RemoteException;
}
