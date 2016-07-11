/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.logging.Level;
import static java.util.logging.Level.parse;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import server.entities.Quiz;

/**
 *
 * @author Alex
 */
@WebServlet(name = "ClientServlet2", urlPatterns = {"/ClientServlet2"})
public class ClientServlet2 extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String param = request.getParameter("js"); 
        String qname = request.getParameter("quizname"); 
        
        if (param == null) {
            System.out.println("param == null");
        } else if ("".equals(param)) {
            // The request parameter 'param' was present in the query string but has no value
            // e.g. http://hostname.com?param=&a=b
            System.out.println("else if (\"\".equals(param)");
        } else if (param.equals( request.getParameter("js") )) {   
            
            System.out.println("param: " + param);
            System.out.println("qname: " + qname);
            
            try {
                JSONArray json = new JSONArray(param);
                ArrayList<String> array = new ArrayList<String>();

                for(int index = 0; index < json.length(); index++) {

                    JSONObject jsonObject = json.getJSONObject(index);

                    String str= jsonObject.getString("question");

                    array.add(str);
                    
                    System.out.println("str " + index + ": " + str);
                    
            }} catch (JSONException ex) {
                Logger.getLogger(ClientServlet2.class.getName()).log(Level.SEVERE, null, ex);
            }                    
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
        processRequest(request, response);
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
