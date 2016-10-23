/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.ej.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.IOException;
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
import uk.ac.dundee.computing.ej.instagrim.models.User;
import uk.ac.dundee.computing.ej.instagrim.stores.LoggedIn;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "Register", urlPatterns = {"/Register"})
public class Register extends HttpServlet {
    Cluster cluster = null;
    private String warning = "";
    
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("register.jsp");
        request.setAttribute("warning", warning);
	rd.forward(request,response);
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
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String first=request.getParameter("first");
        String last=request.getParameter("last");
        String email=request.getParameter("email");

        
        if(username.length() <= 0)
        {
            warning += "Username is invalid!";
            response.sendRedirect("/Instagrim/Register");
            return;
        }
        
        if(password.length() < 6)
        {
            warning += "Password is invalid!  6 Characters Minimum!";
            response.sendRedirect("/Instagrim/Register");
            return;
        }
        
       if(first.length() <= 0)
       {
           warning += "First name is empty!";
           response.sendRedirect("/Instagrim/Register");
           return;
       }
       
       if(last.length() <= 0)
       {
           warning += "Last name is empty!";
           response.sendRedirect("/Instagrim/Register");
           return;
       }
       
       if(email.length() <= 0)
       {
           warning += "E-mail is empty!";
           response.sendRedirect("/Instagrim/Register");
           return;
       }
        
        User us=new User();
        us.setCluster(cluster);
        us.RegisterUser(username, password, first, last, email);
        
        HttpSession session=request.getSession();
        System.out.println("Session in servlet "+session);
        
        LoggedIn lg= new LoggedIn();
        lg.setLoggedIn();
        lg.setUsername(username);
        
        session.setAttribute("LoggedIn", lg);
        System.out.println("Session in servlet "+session);
        
	response.sendRedirect("/Instagrim");
        
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
