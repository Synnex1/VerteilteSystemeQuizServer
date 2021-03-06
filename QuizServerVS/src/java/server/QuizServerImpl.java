/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import client.ClientProxy;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Random;
import javax.json.JsonArray;

/**
 *
 * @author Mike
 */
public class QuizServerImpl implements QuizServer{
    HashMap<String, Quiz> qMap = new HashMap();
    QuizServerDB qsdb;
    
    public QuizServerImpl() throws RemoteException {
        this.qsdb = new QuizServerDB();
    }
    
    public static void main(String[] args) {
        try {
            QuizServerImpl obj = new QuizServerImpl();
            QuizServer stub = (QuizServer) UnicastRemoteObject.exportObject(obj, 0);
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT).rebind("QuizServer", stub);
        } catch (Exception e) {
            System.err.println("Ein Fehler ist bei der Registrierung des Servers aufgetreten!");
            System.err.println(e.getMessage());
        }     
    }
    
    @Override
    public QuizServerProxy checkUser(String id, String name) throws RemoteException {
        if (qsdb.checkUser(id, name)) {
            QuizServerProxy qsp;
            qsp = new QuizServerProxyImpl(this);
            return qsp;
        } else {
            return null;
        }
        
    }
    
    /**
     *
     * @param quizName
     * @param questions
     * @return
     */
    public String readyQuiz (String quizName, JsonArray questions) {
        String code = getSaltString();
        Boolean contains = true;
        Quiz q = new Quiz(quizName, questions);
        
        do {
            if(qMap.containsKey(code)) {
                code = getSaltString();
            }else {
                qMap.put(code, q);
                contains = false;
            }
        } while (contains);
        return code;               
    }
    
    /**
     *
     * @param code
     */
    public void startQuiz (String code ) {
        Quiz q = qMap.get(code);
        if (q == null) {
            System.err.println("Code nicht vorhanden!");
        } else {
            Boolean jFlag = false;
            q.setJoinFlag(jFlag);
        }
        System.err.println(q.getJoinFlag());
    }
    
    /**
     *
     * @return
     */
    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 5) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
    
    /**
     *
     * @param code
     * @param userId
     * @param name
     * @param clp
     * @return
     */
    public String joinQuiz(String code, String userId, String name, ClientProxy clp) {
        Quiz q = qMap.get(code);                
        
        if( q == null ) {
            System.err.println("Code falsch!");
            return "Falscher Code";
        } else {
            if (q.getJoinFlag()) {
                return q.addClient(userId, name, clp) + q.getJoinFlag();
            } else {
                System.err.println("Quiz ist bereits gestartet!");
                return "Quiz ist bereits gestartet!";
            }
            
        }
    }
    
    /**
     *
     * @param code
     * @param userId
     * @param time
     * @return
     */
    public Boolean increaseHighscore(String code, String userId, int time) {
        Quiz q = qMap.get(code);
        if (q == null) {
            System.err.println("Code nicht gefunden!");
            return false;
        } else {
            if (q.increaseHighscore(userId, time)) {
                return true;
            } else {
                return false;
            }
        }
    }
    
    /**
     *
     * @param code
     * @return
     */
    public String getHighscore(String code) {
        Quiz q = qMap.get(code);
        if ( q == null ) {
            System.err.println("Code nicht gefunden!");
            return null;
        } else {
            return q.getHighscore();
        }
    }
    
    /**
     *
     * @param code
     * @return
     */
    public Boolean endQuiz(String code) {
        if(qMap.containsKey(code)) {
            qMap.remove(code);
            return true;
        } else {
            System.err.println("Code wurde nicht gefunden!");
            return false;
        }
    }
    
    public String nextQuestion(String code) {
        Quiz q = qMap.get(code);
        if(q == null){
            System.err.println("Code nicht gefunden!");
            return null;
        } else {
            return q.nextQuestion();
        }
    }
    
    public String getNextQuestion(String code) {
        Quiz q = qMap.get(code);
        if(q == null) {
            System.err.println("Code nicht gefunden!");
            return null;
        } else {
            return q.getNextQuestion();
        }
    }
    
    public int questionCount(String code) {
        Quiz q = qMap.get(code);
        if(q == null) {
            System.err.println("Code nicht gefunden!");
            return 0;
        } else {
            return q.questionCount();
        }
    }
}
