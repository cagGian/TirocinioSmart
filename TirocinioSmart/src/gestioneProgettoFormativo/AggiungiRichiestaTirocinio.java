package gestioneProgettoFormativo;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gestioneUtente.Studente;
import gestioneUtente.Utente;
import storageLayer.DatabaseGu;
import storageLayer.DatabasePf;

/**
 * Servlet implementation class AggiungiRichiestaTirocinio.
 * Gestisce l'aggiunta di una RichiestaTirocinio
 * 
 * @author Iannuzzi Nicola'
 * 
 * @version 1.0
 */
@WebServlet("/AggiungiRichiestaTirocinio")
public class AggiungiRichiestaTirocinio extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AggiungiRichiestaTirocinio() 
    {
        super();
    }

    /**
	 * Registra una Richiesta Tirocinio nel database
	 * 
	 * @param request, response
	 * @throws ServletException, IOException
	 * 
	 * @author Iannuzzi Nicola'
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String azienda = request.getParameter("azienda");
		String professore = request.getParameter("professore");
		Studente studente = (Studente) request.getSession().getAttribute("studente");
		RichiestaTirocinio richiesta = new RichiestaTirocinio();
		try 
		{
			richiesta.setAzienda(DatabaseGu.getAziendaByEmail(azienda));
			richiesta.setProfessore(DatabaseGu.getProfessoreByEmail(professore));
			DatabasePf.AddRichiesta(richiesta, studente);
			request.getSession().setAttribute("studente", DatabaseGu.getStudenteByEmail(studente.getUser()));
			RequestDispatcher dispatcher = request.getRequestDispatcher("/areaPersonale.jsp");
			dispatcher.forward(request, response);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
	}

	 /**
		 * Registra una Richiesta Tirocinio nel database, chiamando il metodo doGet();
		 * 
		 * @param request, response
		 * @throws ServletException, IOException
		 * 
		 * @author Iannuzzi Nicola'
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

}
