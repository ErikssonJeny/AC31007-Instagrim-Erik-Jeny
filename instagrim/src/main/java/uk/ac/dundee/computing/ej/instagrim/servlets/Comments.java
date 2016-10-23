/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.ej.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uk.ac.dundee.computing.ej.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.ej.instagrim.lib.Convertors;
import uk.ac.dundee.computing.ej.instagrim.models.CommentModel;
import uk.ac.dundee.computing.ej.instagrim.models.PicModel;
import uk.ac.dundee.computing.ej.instagrim.servlets.Image;
import uk.ac.dundee.computing.ej.instagrim.stores.Comment;
import uk.ac.dundee.computing.ej.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.ej.instagrim.stores.Pic;

/**
 *
 * @author Development
 */
@WebServlet(name = "Comments", urlPatterns = { "Comment/*", "Comment/*/Delete" })
public class Comments extends HttpServlet {
    
    Cluster cluster=null;
    
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String args[] = Convertors.SplitRequestPath(request);
        String username = "majed";
        Image image = new Image(cluster);

        HttpSession session=request.getSession();
        LoggedIn lg = (LoggedIn)session.getAttribute("LoggedIn");
        
        try
        {
            //Gets username otherwise boots out
            if (lg.getLoggedIn()){
                username=lg.getUsername();
            }
            else
            {
               response.sendRedirect("/Instagrim");
            }
        }
        catch(Exception ex)
        {
            response.sendRedirect("/Instagrim");
        }
        
        if(args.length == 4){
            if(args[1].equals("Image"))
            {
                request.setAttribute("UUID", args[2]);
                System.out.println("Attribute UUID: " + args[2]);
                image.DisplayImage(Convertors.DISPLAY_PROCESSED, args[2], response);
                displayCommentList(args[1], request, response);
            }
            else
            {
            }
        }
    }
    
    private void displayCommentList(String UUID, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CommentModel cm = new CommentModel();
        cm.setCluster(cluster);
        java.util.LinkedList<Comment> lsComments = cm.getCommentsForImage(UUID);
        request.setAttribute("Comments", lsComments);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
