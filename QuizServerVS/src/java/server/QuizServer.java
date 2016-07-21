package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Mike
 */
public interface QuizServer extends Remote{
    /**
     * Checks if a user already exists in the database otherwise creates one.
     * 
     * @param id    user id as String
     * @param name  username as String
     * @return      QuizServerProxy object to remote call methods.
     * @throws RemoteException
     */
    public QuizServerProxy checkUser(String id, String name) throws RemoteException;    
}
