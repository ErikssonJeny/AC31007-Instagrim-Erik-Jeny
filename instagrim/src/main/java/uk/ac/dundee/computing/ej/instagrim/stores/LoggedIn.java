/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.ej.instagrim.stores;

/**
 *
 * @author Administrator
 */
public class LoggedIn {
    private boolean loggedin=false;
    private String Username=null;
    public void LoggedIn(){
        
    }
    
    public void setUsername(String name){
        this.Username=name;
    }
    public String getUsername(){
        return Username;
    }
    public void setLoggedIn(){
        loggedin=true;
    }
    public void setLoggedOut(){
        loggedin=false;
    }
    
    public void setLoginState(boolean loggedin){
        this.loggedin=loggedin;
    }
    public boolean getLoggedIn(){
        return loggedin;
    }
}
