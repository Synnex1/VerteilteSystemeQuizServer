package server.entities;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Users extends UnicastRemoteObject{
    int user_Id;
    String vorname, nachname;

    public Users(int user_id, String Vorname, String Nachname) throws RemoteException {
        this.user_Id = user_id;
        this.vorname = Vorname;
        this.nachname = Nachname;
    }    
    
    public int getUser_id() {
        return user_Id;
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setUser_id(int user_id) {
        this.user_Id = user_id;
    }

    public void setVorname(String Vorname) {
        this.vorname = Vorname;
    }

    public void setNachname(String Nachname) {
        this.nachname = Nachname;
    }

}