package client;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONObject;

import server.QuizServer;
import server.QuizServerProxy;
import server.entities.Quiz;

/**
 *
 * @author Mike
 */
@WebServlet(urlPatterns = {"/ClientServlet"})
public class ClientServlet extends HttpServlet {
    QuizServer qs = null;
    QuizServerProxy qsp = null;
    Quiz q = null;
    
    
    
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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ClientCredentialsServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ClientCredentialsServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
<<<<<<< HEAD
            throws ServletException, IOException { 

        int i = 0;     
        ArrayList<Quiz> al = new ArrayList<>();
        
        Quiz q1 = new Quiz(99, 3, "QuizNummer99");
        al.add(q1);  
        Quiz q2 = new Quiz(98, 77, "QuizNoTwo");
        al.add(q2);
        Quiz q3 = new Quiz(97, 50, "QuizJep");
        al.add(q3);        
        
        Iterator itr = al.iterator();
        JSONObject[] JsonArray = new JSONObject[ al.size() ];        
        
        while( itr.hasNext() ){              
            try {                
                Quiz element = (Quiz) itr.next();                
                JSONObject obj = new JSONObject();            
                obj.put("quiz_Id", element.getQuiz_Id() );                
                obj.put("quiz_Id_f", element.getUsers_Id_f() );                
                obj.put("name", element.getName() );               
                JsonArray[i++] = obj;                
            } catch (JSONException ex) {
                Logger.getLogger(ClientServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } // foreach

        PrintWriter out = response.getWriter();            
        out.println( Arrays.toString(JsonArray));   
=======
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("id");
        System.out.println(id);
     
>>>>>>> origin/master
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
        String id;
        String name;
        HttpSession session;
        // Erzeuge Session aus Request
        session = request.getSession();
        
        // Ziehe Informationen aus dem Request
        id = request.getParameter("id");
        name = request.getParameter("name");
        System.out.println("info is: " +id);
        System.out.println("name is: " +name);
        
        session.setAttribute("id", id);
        session.setAttribute("name", name);
        
        try {
                Registry registry = LocateRegistry.getRegistry();
                qs = (QuizServer) registry.lookup("QuizServer");
                //chatsrv = (ChatServer)Naming.lookup("rmi://131.173.110.7/ChatServer");
                
                // checkUser im QuizServer implementieren!
                qsp = qs.checkUser(id, name);
                if(qsp==null) {
                    System.out.println("YEAH ES HAT GEKLAPPT");
                }
        } catch (NotBoundException | RemoteException e) { 
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
