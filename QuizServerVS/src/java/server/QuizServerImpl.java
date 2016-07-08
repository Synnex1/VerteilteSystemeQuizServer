/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Mike
 */
public class QuizServerImpl implements QuizServer{
    QuizServerDB qsDB = new QuizServerDB();
    
    public QuizServerImpl() throws RemoteException {
    }
    
    public static void main(String[] args) {
        try {
            QuizServerImpl obj = new QuizServerImpl();
            QuizServer stub = (QuizServer) UnicastRemoteObject.exportObject(obj, 0);
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT).rebind("QuizServer", stub);
        } catch (Exception e) {}     
    }
    
    @Override
    public QuizServerProxy checkUser(String id, String name) {
        QuizServerProxy qsp;
        qsp = new QuizServerProxyImpl();
        return qsp;
    }
}
