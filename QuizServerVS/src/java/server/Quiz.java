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
 * Class that represents an active quiz.
 *
 * @author Mike
 */
public class Quiz {
    String quizName;
    JsonArray questions;
    int questionIndex = -1;
    Boolean joinFlag;
    HashMap<String, Client> cMap = new HashMap();
    
    /**
     * Constructor
     * @param quizName The name of the quiz
     * @param questions A JSON array containing the questions of the quiz
     */
    public Quiz(String quizName, JsonArray questions) {
        this.quizName = quizName;
        this.questions = questions;
        this.joinFlag = true;
    }
    
    /**
     * Creates a client object and adds it with the given client-id as key into the map.
     *
     * @param clientId Represents the user-id.
     * @param clientName Full name of the user.
     * @param clp ClientProxy object to remote call methods.
     * @return Returns the name of the quiz.
     */
    public String addClient(String clientId, String clientName, ClientProxy clp) {
        if (!this.joinFlag) {
            return null;
        }
        
        Client cl = new Client(clientName, clp);
        cMap.put(clientId, cl);
        return this.quizName;
    }
    
    /**
     * Builds a JSONarray of all players and their highscore. 
     *
     * @return Returns a JSONarray string of all players and their highscore.
     */
    public String getHighscore() {
        JsonArrayBuilder jArrB = Json.createArrayBuilder();
        
        for(Map.Entry<String, Client> entry : cMap.entrySet()) {
            jArrB.add(entry.getValue().toJson());
        }
        return jArrB.build().toString();
    }
    
    /**
     * Adds the given parameter time to the highscore of a player specified by the user-id in the map.
     *
     * @param userId User-id
     * @param time Remaining time after clicked the correct answer as Integer.
     * @return Returns true if the score is increased. Otherwise returns false.
     */
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
    
    /**
     * Increases the question-index parameter of the quiz by one and building a JSON string of the next question and its answers.
     * This method is called only by the quizmaster.
     *
     * @return Returns JSON string of the question and its answeres pointed to by the question-index.
     */
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
    
    /**
     * Builds a JSON string of the next question and its answers.
     *
     * @return Returns a JSON string of the question and its answeres pointed to by the question-index parameter.
     */
    public String getNextQuestion() {
        JsonObject nextQuestion;
        nextQuestion = questions.getJsonObject(questionIndex);
        return nextQuestion.toString();
    }
    
    /**
     * Counts the question in the question array. 
     *
     * @return Returns the count of the questions in a quiz.
     */
    public int questionCount() {
        return questions.size();
    }

    /**
     * Setter of the parameter joinFlag
     *
     * @param joinFlag The value joinFlag will change to.
     */
    public void setJoinFlag(Boolean joinFlag) {
        this.joinFlag = joinFlag;
    }

    /**
     * Getter of the parameter joinFlag
     *
     * @return Returns the value of joinFlag
     */
    public Boolean getJoinFlag() {
        return this.joinFlag;
    }        
}
