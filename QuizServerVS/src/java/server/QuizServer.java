package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Mike
 */
public interface QuizServer extends Remote{
    public QuizServerProxy checkUser(String id, String name);
}
