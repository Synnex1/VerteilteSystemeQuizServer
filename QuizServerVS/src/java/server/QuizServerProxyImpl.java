/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

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
    public ArrayList getAllQuizFromUser(String user_Id) throws RemoteException {
        return qsdb.getAllQuizFromUser(user_Id);
    }
    
}
