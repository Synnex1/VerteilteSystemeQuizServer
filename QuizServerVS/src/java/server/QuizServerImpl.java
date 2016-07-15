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
    QuizServerDB qsdb;
    
    public QuizServerImpl() throws RemoteException {
        this.qsdb = new QuizServerDB();
    }
    
    public static void main(String[] args) {
        //CreateTables ct = new CreateTables();
        //ct.createTables();
        
        try {
            QuizServerImpl obj = new QuizServerImpl();
            QuizServer stub = (QuizServer) UnicastRemoteObject.exportObject(obj, 0);
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT).rebind("QuizServer", stub);
        } catch (Exception e) {}     
    }
    
    @Override
    public QuizServerProxy checkUser(String id, String name) throws RemoteException {
        System.out.println("HIER BIN ICH!");
        if (qsdb.checkUser(id, name)) {
            System.out.println("HIER BIN ICH RICHTIG!");
            QuizServerProxy qsp;
            qsp = new QuizServerProxyImpl();
            qsdb.closeConn();
            return qsp;
        } else {
            qsdb.closeConn();
            return null;
        }
        
    }
}
