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
import com.jhlabs.image.OilFilter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import static org.imgscalr.Scalr.OP_ANTIALIAS;
import static org.imgscalr.Scalr.OP_GRAYSCALE;
import static org.imgscalr.Scalr.pad;
import static org.imgscalr.Scalr.resize;
import uk.ac.dundee.computing.ej.instagrim.lib.Convertors;
import uk.ac.dundee.computing.ej.instagrim.stores.Comment;
import uk.ac.dundee.computing.ej.instagrim.stores.Pic;

/**
 *
 * @author Development
 */
public class CommentModel {
    
    Cluster cluster;

    public void CommentModel() {

    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public void insertComment(String content, String user, String UUID){
        try {
            Convertors convertor = new Convertors();

            java.util.UUID commentid = convertor.getTimeUUID();
            java.util.UUID picid = java.util.UUID.fromString(UUID);
            
            Session session = cluster.connect("instagrim");

            PreparedStatement psInsertComment = session.prepare("insert into comments ( picid, cmntid, cmnt_added, user, comment) values(?,?,?,?,?)");
            BoundStatement bsInsertComment = new BoundStatement(psInsertComment);

            Date DateAdded = new Date();
            session.execute(bsInsertComment.bind(picid, commentid, DateAdded, user, content));
            session.close();

        } 
        catch (Exception ex) {
            System.out.println("Error --> " + ex);
        }
    }
    
    public void deleteComment(String UUID){
        try {
            Session session = cluster.connect("instagrim");

            PreparedStatement psDeleteComment = session.prepare("delete from comments where cmntid =?");
            BoundStatement bsDeleteComment = new BoundStatement(psDeleteComment);

            Date DateAdded = new Date();
            session.execute(bsDeleteComment.bind(java.util.UUID.fromString(UUID)));
            session.close();

        } 
        catch (Exception ex) {
            System.out.println("Error --> " + ex);
        }
    }
   
    public java.util.LinkedList<Comment> getCommentsForImage(String picid) {
        java.util.LinkedList<Comment> comments = new java.util.LinkedList<>();
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select (cmntid, cmnt_added, user, comment) from comments where picid =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        java.util.UUID.fromString(picid)));
        if (rs.isExhausted()) {
            System.out.println("No Comments returned");
            return null;
        } else {
            for (Row row : rs) {
                Comment comment = new Comment();
                
                comment.setUUID(row.getUUID("cmntid"));
                comment.setPicUUID(row.getUUID("picid"));
                comment.setDate(row.getTimestamp("cmnt_added"));
                comment.setUser(row.getString("user"));
                comment.setContent(row.getString("comment"));
                
                comments.add(comment);

            }
        }
        return comments;
    }

}
