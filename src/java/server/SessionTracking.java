/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Christodoulos
 */
public class SessionTracking {
    
    String name = "";
    HttpSession session = null;
    
    public SessionTracking(String name){
        this.name = name;
    }
    
    public void createSession(){
//        session = request.
    }
    
    public String getStringName(){
        return this.name;
    }
    
    public static boolean checkTimeValid(long start,long current,long inactiveLimit){
        long timeIn = 0;
        timeIn = TimeUnit.MILLISECONDS.toSeconds(current) - TimeUnit.MILLISECONDS.toSeconds(start) ;
        System.out.println(timeIn+" "+inactiveLimit);
        if(timeIn >= inactiveLimit){
            return false;
        }
        return true;
    }
    
    public void setHttpSession(HttpSession session){
        this.session = session;
    }
    
}
