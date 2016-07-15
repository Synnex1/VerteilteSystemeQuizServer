/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import server.QuizServerProxy;

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
               
        HttpSession session = request.getSession();
        QuizServerProxy qsp = (QuizServerProxy) session.getAttribute("qsp");
        
        if ( qsp != null ) {
            System.out.println("qsp != null");
        }  else {
            qsp = null;
            System.out.println("qsp == null");
        }     
        String code = request.getParameter("code");
        String id = (String)session.getAttribute("id");                               
        String param = request.getParameter("js");
        
        if (param == null) {
            System.out.println("param == null");
        } else if ("".equals(param)) {
            // The request parameter 'param' was present in the query string but has no value
            // e.g. http://hostname.com?param=&a=b
            System.out.println("else if (\"\".equals(param)");
        } else if (param.equals( request.getParameter("js") )) {   
             
            System.out.println("code: " + code);
            System.out.println("param: " + param);
            if (code.equals("update")) {
                qsp.updateQuestion(param);
            }
            if (code.equals("updateName")) {
                System.out.println("updateName()");
                int quiz_id = Integer.parseInt( request.getParameter("quiz_id") );
                qsp.updateQuiz(quiz_id, param);
            }
            if (code.equals("create")){
                qsp.createQuiz(param, id); 
            } 
            if (code.equals("addQuiz")) {
                qsp.createQuestions(param, id);
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
