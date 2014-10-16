/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zz.Server;

import org.vertx.java.core.json.JsonObject;

/**
 *
 * @author zulk
 */
public interface ServerStarter {

    void send(String messageType, JsonObject message);

    void start();
    void stop();
    
    boolean isStarted();
}
