/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.ej.instagrim.stores;

import com.datastax.driver.core.utils.Bytes;
import java.nio.ByteBuffer;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class Comment {

    private String user;
    private String content;
    private java.util.UUID UUID=null;
    private java.util.UUID picUUID=null;
    private Date dateAdded = null;
    
    public void Comment() {

    }
    public void setUUID(java.util.UUID UUID){
        this.UUID =UUID;
    }
    
    public void setPicUUID(java.util.UUID picUUID){
        this.picUUID = picUUID;
    }
    
    public void setDate(Date date)
    {
        this.dateAdded = date;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    
    public String getSUUID(){
        return UUID.toString();
    }
    
    public String getPicUUID(){
        return picUUID.toString();
    }
    
    public int getDate()
    {
        return dateAdded.getDate();
    }
    
    public int[] getTime()
    {
        int[] time = new int[3];
        
        time[0] = dateAdded.getHours();
        time[1] = dateAdded.getMinutes();
        time[2] = dateAdded.getSeconds();
        return time;
    }
    
    public String getContent(){
        return content;
    }
    
    public String getUser() {
        return user;
    }


}
