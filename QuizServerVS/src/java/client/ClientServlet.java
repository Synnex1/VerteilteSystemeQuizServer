package client;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import server.QuizServer;
import server.QuizServerProxy;

/**
 *
 * @author Mike
 */
@WebServlet(urlPatterns = {"/ClientServlet"})
public class ClientServlet extends HttpServlet {
    QuizServer qs = null;
    QuizServerProxy qsp = null;
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
            throws ServletException, IOException {
        System.out.println("+++doGet() vom ClientServlet+++");
        
        String code = request.getParameter("code");
        System.out.println("code: " + code);
        
        HttpSession session = request.getSession();
        if ( (QuizServerProxy)session.getAttribute("qsp") != null ) {
            qsp = (QuizServerProxy)session.getAttribute("qsp");
            System.out.println("qsp != null");
        }  else {
            qsp = null;
            System.out.println("qsp == null");
        }         
        
        // Dieser if-Block wird ausgeführt wenn der User ein Quiz bearbeiten möchte
        if (code.equals("edit") ) { 
            if ( request.getParameter("quiz_id") != "null" ) {                
            
                int quiz_id = Integer.parseInt( request.getParameter("quiz_id") );
                System.out.println("quiz_id: " + quiz_id);

                String jsonString = qsp.getQuizInfo( quiz_id );
                System.out.println("doGet() ServerResponse: " + jsonString);

                try {
                    PrintWriter out = response.getWriter();
                    out.print( jsonString );             
                } catch (IOException ex) {
                    Logger.getLogger(ClientServlet.class.getName()).log(Level.SEVERE, null, ex);
                    System.err.println(ex.getMessage());
                }             
            }
        }
        
        // Dieser if-Block wird ausgeführt wenn das Dashboard aufgebaut wird und die 
        // Quiz-Namen gebraucht werden
        if (code.equals("getQuiz") ) {
            System.out.println("code.equals(\"getQuiz\")");
                    
            int i = 0;                    
            String id = (String)session.getAttribute("id");        

            if ( qsp != null ) { 

                String al = qsp.getAllQuizFromUser(id);             
                System.out.println("return dashboardJsonString: " + al);        

                try {
                    PrintWriter out = response.getWriter();
                    out.print( al );             
                } catch (IOException ex) {
                    Logger.getLogger(ClientServlet.class.getName()).log(Level.SEVERE, null, ex);
                    System.err.println(ex.getMessage());
                }  
            } else {
                try {
                    PrintWriter out = response.getWriter();
                    response.setContentType("text/html");
                    out.print("Noch kein Quiz erstellt!");             
                } catch (IOException ex) {
                    Logger.getLogger(ClientServlet.class.getName()).log(Level.SEVERE, null, ex);
                    System.err.println(ex.getMessage());
                } 
            }
        }
} // doGet

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
        System.out.println("+++doPost() vom ClientServlet+++");
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
                System.out.println("qs: " + qs);
                //chatsrv = (ChatServer)Naming.lookup("rmi://131.173.110.7/ChatServer");               
                
                // checkUser im QuizServer implementieren!
                qsp = qs.checkUser(id, name);
                System.out.println("checkUser()");
                if(qsp!=null) {
                    System.out.println("YEAH ES HAT GEKLAPPT");
                    session.setAttribute("qsp", qsp);
                } else {
                    System.out.println("Hast du überhört!");
                }
        } catch (NotBoundException | RemoteException e) { 
            System.out.println("Ich bin im catch");
            e.getMessage();
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
