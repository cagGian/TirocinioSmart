package gestioneutente;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import storagelayer.DatabaseGu;

/**
 * Servlet implementation class Login. Gestisce il login della Segreteria.
 * 
 * @author Iannuzzi Nicola'
 * 
 * @version 1.0
 */
@WebServlet("/LoginSegreteria")
public class LoginSegreteria extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * Costruttore della classe.
   * 
   * @see HttpServlet#HttpServlet()
   */
  public LoginSegreteria() {
    super();
  }

  /**
   * Login della segretria tramite metodo doGet().
   * 
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doPost(request, response);
  }

  /**
   * Login della segreteria tramite metodo doPost().
   * 
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String errore = "Email o password errati";

    String username = request.getParameter("username");
    String password = request.getParameter("password");

    try {
      Segreteria segr = DatabaseGu.getSegreteriaByUser(username);

      if (segr != null) {
        if (segr.getPassword().equals(password)) {
          segr.setAutenticato(true);
          HttpSession session = request.getSession();
          session.setAttribute("segreteria", segr);
          request.getRequestDispatcher("areaPersonaleSegreteria.jsp").forward(request, response);
        } else {
          request.getRequestDispatcher("loginSegreteria.jsp?errore=" + errore).forward(request,
              response);
        }
      } else {
        request.getRequestDispatcher("loginSegreteria.jsp?errore=" + errore).forward(request,
            response);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}