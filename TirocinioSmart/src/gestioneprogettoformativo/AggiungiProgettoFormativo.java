package gestioneprogettoformativo;

import gestioneutente.Azienda;
import gestioneutente.Professore;
import gestioneutente.Segreteria;
import gestioneutente.Studente;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import storagelayer.DatabaseGu;
import storagelayer.DatabasePf;

/**
 * Servlet implementation class AggiungiProgettoFormativo. 
 * Gestisce l'aggiunta di un Progetto Formativo.
 * 
 * @author Iannuzzi Nicola'
 * 
 * @version 1.0
 */
@WebServlet("/AggiungiProgettoFormativo")
public class AggiungiProgettoFormativo extends HttpServlet {

  private static final long serialVersionUID = 1L;
  private String errore;

  /**
   * Costruttore della classe.
   * 
   * @see HttpServlet#HttpServlet()
   */
  public AggiungiProgettoFormativo() {
    super();
  }

  /**
   * Registra un Progetto Formativo nel database, chiamando il metodo doPost().
   * 
   * @param request,
   *          response
   * @throws ServletException,
   *           IOException
   * 
   * @author Iannuzzi Nicola'
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doPost(request, response);
  }

  /**
   * Registra un Progetto Formativo nel database.
   * 
   * @param request,
   *          response
   * @throws ServletException,
   *           IOException
   * 
   * @author Iannuzzi Nicola'
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    errore = "";
    if (controllo(request, response)) {
      String studente = request.getParameter("matricolaStudente");
      Studente s = new Studente();
      s.setMatricola(studente);
      String professore = request.getParameter("professoreEmail");
      Professore p = new Professore();
      p.setUser(professore);
      Azienda azienda = (Azienda) request.getSession().getAttribute("azienda");
      String obiettivi = request.getParameter("obiettivi");
      String dataInizio = request.getParameter("dataInizio");
      String dataFine = request.getParameter("dataFine");
      ProgettoFormativo progettoFormativo = new ProgettoFormativo();
      try {
        progettoFormativo.setAzienda(azienda);
        progettoFormativo.setDataInizio(dataInizio);
        progettoFormativo.setDataFine(dataFine);
        progettoFormativo.setObiettivi(obiettivi);
        progettoFormativo.setStudente(s);
        progettoFormativo.setSegreteria(DatabaseGu.getSegreteriaByUser(Segreteria.USERNAME));
        progettoFormativo.setProfessore(p);

        DatabasePf.addProgettoFormativo(progettoFormativo);

        RequestDispatcher dispatcher = request
            .getRequestDispatcher("/compilaProgettoFormativoAziendaLista.jsp");
        dispatcher.forward(request, response);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      RequestDispatcher dispatcher = request
          .getRequestDispatcher("/compilaProgettoFormativoAzienda.jsp?errore=" + errore);
      dispatcher.forward(request, response);
    }
  }

  /**
   * Controllo del formato dei parametri lato Server.
   * 
   * @param request,
   *          response
   * 
   * @author Caggiano Gianluca
   */
  private boolean controllo(HttpServletRequest request, HttpServletResponse response) {
    String obiettivi = request.getParameter("obiettivi");
    String dataInizio = request.getParameter("dataInizio");
    String dataFine = request.getParameter("dataFine");
    obiettivi = obiettivi.trim();
    dataInizio = dataInizio.trim();
    dataFine = dataFine.trim();
    
    if (obiettivi.length() < ProgettoFormativo.MIN_LUNGHEZZA_OBIETTIVO
        || obiettivi.length() > ProgettoFormativo.MAX_LUNGHEZZA_OBIETTIVO) {
      errore = "Campo obiettivi troppo corto o troppo lungo (max 255 caratteri)";
    }

    DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
    try {
      java.util.Date dataI = df.parse(dataInizio);
      java.util.Date dataF = df.parse(dataFine);
      Calendar dtI = new GregorianCalendar();
      dtI.setTime(dataI);
      Calendar dtF = new GregorianCalendar();
      dtF.setTime(dataF);

      if (dtI.after(dtF)) {
        errore = "Data inizio successiva alla data di fine";
      }

    } catch (ParseException e) {
      errore = "Data non valida";
    }

    if (errore.length() != 0) {
      return false;
    } else {
      return true;
    }
  }

}
