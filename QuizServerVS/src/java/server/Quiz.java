/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import client.ClientProxy;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 *
 * @author Mike
 */
public class Quiz {
    String quizName;
    JsonArray questions;
    int questionIndex = -1;
    Boolean joinFlag = true;
    HashMap<String, Client> cMap = new HashMap();
    
    
    public Quiz(String quizName, JsonArray questions) {
        this.quizName = quizName;
        this.questions = questions;
    }
    
    public String addClient(String clientId, String clientName, ClientProxy clp) {
        if (!this.joinFlag) {
            return null;
        }
        
        Client cl = new Client(clientName, clp);
        cMap.put(clientId, cl);
        return this.quizName;
    }
    
    public String getHighscore() {
        JsonArrayBuilder jArrB = Json.createArrayBuilder();
        
        for(Map.Entry<String, Client> entry : cMap.entrySet()) {
            jArrB.add(entry.getValue().toJson());
        }
        return jArrB.build().toString();
    }
    
    public Boolean increaseHighscore(String userId, int time) {
        Client c = cMap.get(userId);
        if ( c == null ) {
            System.err.println("UserId nicht gefunden!");
            return false;
        } else {
            c.increaseHighscore(time);
            return true;
        }
    }
    
    public String nextQuestion() {
        JsonObject nextQuestion;
        ClientProxy cp;
        this.questionIndex++;
        
        for(Map.Entry<String, Client> entry : cMap.entrySet()) {
            try {
                cp = entry.getValue().clp;
                cp.setFlag();
                if( questionIndex == questions.size()-1 ) {
                    cp.setEndFlag();
                    System.err.println("setEndFlag gesetzt");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
                System.err.println(e.getMessage());                
            }
        }
        if ( questionIndex < questions.size() ) {
            nextQuestion = questions.getJsonObject(questionIndex);
            return nextQuestion.toString();
        } else {
            return null;
        }                
    }
    
    public String getNextQuestion() {
        JsonObject nextQuestion;
        nextQuestion = questions.getJsonObject(questionIndex);
        return nextQuestion.toString();
    }
    
    public int questionCount() {
        return questions.size();
    }

    public void setJoinFlag(Boolean joinFlag) {
        System.out.println(this.joinFlag);
        this.joinFlag = joinFlag;
        System.out.println(this.joinFlag);
    }

    public Boolean getJoinFlag() {
        System.out.println(this.joinFlag);
        return joinFlag;
    }        
}
