/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import client.ClientProxy;
import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 * Representing a player that is part of an active quiz.
 *
 * @author Mike
 */
class Client {
    String name;
    ClientProxy clp;
    int highScore = 0;
    
    public Client(String name, ClientProxy clp) {
        this.name = name;
        this.clp = clp;
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
