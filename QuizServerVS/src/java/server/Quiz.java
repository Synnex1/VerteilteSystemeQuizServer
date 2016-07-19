/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArrayBuilder;

/**
 *
 * @author Mike
 */
public class Quiz {
    int quizId;
    String quizName;
    String code;
    Boolean joinFlag = true;
    HashMap<String, Client> cMap = new HashMap();
    
    
    public Quiz(int quizId, String code) {
        this.quizId = quizId;
        this.code = code;
    }
    
    public Boolean addClient(String clientId, String clientName) {
        if (!this.joinFlag) {
            return false;
        }
        
        Client cl = new Client(clientName);
        cMap.put(clientId, cl);
        return true;
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
}
