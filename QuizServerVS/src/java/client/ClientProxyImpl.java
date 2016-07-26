/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Mike
 */
public class ClientProxyImpl extends UnicastRemoteObject implements ClientProxy, Serializable{
    HttpSession session;
    
    public ClientProxyImpl(HttpSession session) throws RemoteException {
        this.session = session;
    }
    
    @Override
    public void setFlag() throws RemoteException {
        session.setAttribute("nextQuestionFlag", true);
    }

    @Override
    public void setEndFlag() throws RemoteException {
        session.setAttribute("endQuizFlag", true);
    }
    
}
