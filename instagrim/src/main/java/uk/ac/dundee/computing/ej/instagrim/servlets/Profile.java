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
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import uk.ac.dundee.computing.ej.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.ej.instagrim.lib.Convertors;
import uk.ac.dundee.computing.ej.instagrim.models.ProfilePicModel;
import uk.ac.dundee.computing.ej.instagrim.models.User;
import uk.ac.dundee.computing.ej.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.ej.instagrim.stores.Pic;

/**
 *
 * @author Development
 */
@WebServlet(name = "Profile", urlPatterns = {"/Profile", "/Profile/*", "/Profile/*/picture"})

@MultipartConfig

public class Profile extends HttpServlet {
    
    Cluster cluster=null;
    
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }
    
     protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String args[] = Convertors.SplitRequestPath(request);
        String username = "majed";

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
        
        if( args.length == 4 )
            DisplayImage(args[2], request, response);
        else
        {
            if(checkForImage(username))
                request.setAttribute("haspic", true);
            else
                request.setAttribute("haspic", false);
        }
            
                
        User us = new User();
        us.setCluster(cluster);
        String[] data = us.DisplayDetails(username);

        if (data != null){
            request.setAttribute("first", data[0]);
            request.setAttribute("last",  data[1]);
            request.setAttribute("email", data[2]);
        }
        else{
            response.sendRedirect("/Instagrim");
        }   
        
        RequestDispatcher rd=request.getRequestDispatcher("profile.jsp");
	rd.forward(request,response);
    }
     
     private void DisplayImage(String username, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProfilePicModel tm = new ProfilePicModel();
        tm.setCluster(cluster);

        Pic p = tm.getPic(username);
        
        if(p == null)
        {
            System.out.println("No picture found");
            return;
        }
        
        OutputStream out = response.getOutputStream();

        response.setContentType(p.getType());
        response.setContentLength(p.getLength());
        //out.write(Image);
        InputStream is = new ByteArrayInputStream(p.getBytes());
        BufferedInputStream input = new BufferedInputStream(is);
        byte[] buffer = new byte[8192];
        for (int length = 0; (length = input.read(buffer)) > 0;) {
            out.write(buffer, 0, length);
        }
        out.close();
        
        return;
    }
     
     private boolean checkForImage(String username) throws ServletException, IOException {
        ProfilePicModel tm = new ProfilePicModel();
        tm.setCluster(cluster);

        Pic p = tm.getPic(username);
        
        if(p == null)
        {
            System.out.println("No picture found");
            return false;
        }

        return true;
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
        for (Part part : request.getParts()) {
            System.out.println("Part Name " + part.getName());

            String type = part.getContentType();
            String filename = part.getSubmittedFileName();
            
            
            InputStream is = request.getPart(part.getName()).getInputStream();
            int i = is.available();
            HttpSession session=request.getSession();
            LoggedIn lg = (LoggedIn)session.getAttribute("LoggedIn");
            String username="majed";
            if (lg.getLoggedIn()){
                username=lg.getUsername();
            }
            if (i > 0) {
                byte[] b = new byte[i + 1];
                is.read(b);
                System.out.println("Length : " + b.length);
                ProfilePicModel tm = new ProfilePicModel();
                tm.setCluster(cluster);
                tm.insertPic(b, type, filename, username);

                is.close();
            }
            
            response.sendRedirect("/Instagrim/Profile");
        }
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
