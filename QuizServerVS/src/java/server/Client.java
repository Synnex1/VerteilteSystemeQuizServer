/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author Mike
 */
class Client {
    String name;
    int highScore = 0;
    
    public Client(String name) {
        this.name = name;
    }
    
    public void increaseHighscore (int time){
        this.highScore += time;
    }
    
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder obj = Json.createObjectBuilder()
                .add("name", name)
                .add("highscore", highScore);
        
        return obj;
    }
}
