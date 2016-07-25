/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@WebServlet(name = "ClientServlet3", urlPatterns = {"/ClientServlet3"})
public class ClientServlet3 extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
               
        HttpSession session = request.getSession();
        QuizServerProxy qsp = (QuizServerProxy) session.getAttribute("qsp");
        ClientProxy clp = (ClientProxy) session.getAttribute("clp");
        
        if ( clp != null ) {
            System.out.println("ClientServlet3: clp != null");
        }  else {
            session.setAttribute("clp", clp);
            System.out.println("ClientServlet3: clp erstellt");
        }        
                        
        if ( qsp != null ) {
            System.out.println("ClientServlet3: qsp != null");
        }  else {
            qsp = null;
            System.out.println("ClientServlet3: qsp == null");
        }     
        String code = request.getParameter("code");
        String user_Id = (String)session.getAttribute("id");                               
        String param = request.getParameter("js");
        String answer = "";
        
        if (param == null) {
            System.out.println("ClientServlet3 param == null");
        } else if ("".equals(param)) {
            // The request parameter 'param' was present in the query string but has no value
            // e.g. http://hostname.com?param=&a=b
            System.out.println("else if (\"\".equals(param)");
        } else if (param.equals( request.getParameter("js") )) {   
             
            System.out.println("ClientServlet3 code: " + code);
            System.out.println("ClientServlet3 param: " + param);
            
            if (code.equals("beginQuiz")) {
                System.out.println("+++ beginQuiz +++");
                System.out.println("quiz_id: " + request.getParameter("quiz_id"));                
                int quiz_id = Integer.parseInt( request.getParameter("quiz_id") );
                answer = qsp.readyQuiz(quiz_id);
            }
            else if (code.equals("joinQuiz")) {
                System.out.println("joinQuiz()");
                String pin = request.getParameter("js");
                System.out.println("pin: " + pin);
                answer = qsp.joinQuiz(pin, user_Id, clp);                
            }
            else if (code.equals("getPlayers")) {
                System.out.println("getPlayers()");
                String pin = request.getParameter("js");
                answer = qsp.getHighscore(pin);
            }
            else if (code.equals("startQuiz")) {
                System.out.println("startQuiz()");
                String pin = request.getParameter("js");
                qsp.startQuiz(pin);                
                System.out.println("Pin: " + pin);
                answer = qsp.nextQuestion(pin);                    
            }
            else if (code.equals("updatePoints")) {
                System.out.println("updatePoints()");
                String pin = request.getParameter("js");
                int time = Integer.parseInt( request.getParameter("param") );                
                Boolean increase = qsp.increaseHighscore(pin, user_Id, time);                
                answer = qsp.getHighscore(pin);                                
            }  
            else if (code.equals("waitForStart")) {
                System.out.println("waitForStart()");
                Boolean ready = false;
                while (ready==false) {
                    session = request.getSession();
                    if ( (Boolean) session.getAttribute("nextQuestionFlag") != null ) {
                        ready = (Boolean) session.getAttribute("nextQuestionFlag");
                        System.err.println((Boolean) session.getAttribute("nextQuestionFlag"));
                        System.err.println("if");
                    } else {
                        System.err.println("else Brah");
                    }
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ClientServlet3.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                    answer = ("nextQuestionFlag: " + session.getAttribute("nextQuestionFlag"));
            }
            else if (code.equals("test")) {
                System.out.println("test()");
                session.setAttribute("nextQuestionFlag", (Boolean)true);
            }  
            try {
                PrintWriter out = response.getWriter();
                out.print( answer );             
            } catch (IOException ex) {
                Logger.getLogger(ClientServlet.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println(ex.getMessage());
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