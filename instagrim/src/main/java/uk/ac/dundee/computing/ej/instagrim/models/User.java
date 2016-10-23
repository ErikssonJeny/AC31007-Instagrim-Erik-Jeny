/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.ej.instagrim.models;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import uk.ac.dundee.computing.ej.instagrim.lib.AeSimpleSHA1;
import uk.ac.dundee.computing.ej.instagrim.stores.Pic;

/**
 *
 * @author Administrator
 */
public class User {
    
    Cluster cluster;
    
    public User(){
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
    
    public boolean RegisterUser(String username, String password){
        AeSimpleSHA1 sha1handler =  new AeSimpleSHA1();
        String EncodedPassword=null;
        try {
            EncodedPassword= sha1handler.SHA1(password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("insert into userprofiles (login,password) Values(?,?)");
       
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username,EncodedPassword));
        session.close();
        return true;
    }
    
    public boolean RegisterUser(String username, String password, String first, String last, String email){
        AeSimpleSHA1 sha1handler =  new AeSimpleSHA1();
        String EncodedPassword=null;
        try {
            EncodedPassword= sha1handler.SHA1(password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("insert into userprofiles (login,password,first_name,last_name,email) Values(?,?,?,?,?) if not exists");
       
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username,EncodedPassword,first,last,email));
        session.close();
        return true;
    }
    
    public boolean IsValidUser(String username, String password){
        AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
        try {
            EncodedPassword= sha1handler.SHA1(password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select password from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return false;
        } else {
            for (Row row : rs) {
               
                String StoredPass = row.getString("password");
                if (StoredPass.compareTo(EncodedPassword) == 0)
                    return true;
            }
        }
        session.close();
        return false;  
    }
    
    public String[] DisplayDetails(String username){
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select first_name, last_name, email from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        if (rs.isExhausted()) {
            System.out.println("No user with that name found.");
            return null;
        }
        else {
            for (Row row : rs) {
                String[] StoredData = new String[3];
                StoredData[0] = row.getString("first_name");
                StoredData[1] = row.getString("last_name");
                StoredData[2] = row.getString("email");
                System.err.println(StoredData[0]);
                System.err.println(StoredData[1]);
                System.err.println(StoredData[2]);
                return StoredData;
            }
        }
        return null;  
    }
    
    public boolean deleteUser(String username){
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("delete from userprofiles where login =?");
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute(boundStatement.bind(username));
        session.close();
        return true;
    }


    
}
