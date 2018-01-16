package gestioneutente;

/**
 * Oggetto astratto Utente che contiene le informazioni comuni a tutti gli utenti.
 * 
 * @author  Caggiano Gianluca
 * 
 * @version 1.0
 */
public abstract class Utente {

	private String user;
	private String password;
	private String nome;
	private String cognome;
	
	/**
	 * Costruttore vuoto
	 * 
	 * @author Caggiano Gianluca
	 */
	public Utente(){
		super();
	}
	
	/**
	 * Costruttore con parametri:
	 *  
	 * @param user Stringa che rappresenta la user di accesso al sistema
	 * @param password Stringa che rappresenta la password di accesso al sistema
	 * @param nome Stringa che rappresenta il nome dell'utente
	 * @param cognome Stringa che rappresenta il cognome dell'utente
	 * 
	 * @author Caggiano Gianluca
	 */
	public Utente(String user, String password, String nome, String cognome) {
		super();
		this.user = user;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
	}
	
	/**
	 * Restituisce la user utilizzata dall'utente per accedere al sistema
	 * 
	 * @return La user di accesso dell'utente
	 * 
	 * @author Caggiano Gianluca
	 */
	public String getUser() {
		return user;
	}
	/**
	 * Setta l'user dell'utente.
	 * 
	 * @param user	Stringa che rappresenta la user di accesso dell'utente
	 * 
	 * @pre user != null
	 * @pre user.length() &#62;= 2 and nome.length() &#60;= 64
	 * 
	 * @author Caggiano Gianluca
	 */
	public void setUser(String user) {
		this.user = user;
	}
	
	/**
	 * Restituisce la password utilizzata dall'utente per accedere al sistema
	 * 
	 * @return La password dell'utente
	 * 
	 * @author Caggiano Gianluca
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * Setta la password dell'utente.
	 * 
	 * @param password Stringa che rappresenta la password dell'utente
	 * 
	 * @pre password != null
	 * @pre password.length() &#62;= 2 and password.length() &#60;= 32
	 * 
	 * @author Caggiano Gianluca
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Restituisce il nome dell'utente.
	 * 
	 * @return Il nome dell'utente
	 * 
	 * @author Caggiano Gianluca
	 */
	public String getNome() {
		return nome;
	}
	/**
	 * Setta il nome dell'utente
	 * 
	 * @param nome Stringa che rappresenta il nome dell'utente
	 * 
	 * @pre nome != null
	 * @pre nome.length() &#62;= 2 and nome.length() &#60;= 30
	 * 
	 * @author Caggiano Gianluca
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	/**
	 * Restituisce il cognome dell'utente
	 * 
	 * @return Il cognome dell'utente
	 * 
	 * @author Caggiano Gianluca
	 */
	public String getCognome() {
		return cognome;
	}
	/**
	 * Setta il cognome dell'utente
	 * 
	 * @param cognome Stringa che rappresenta il cognome dell'utente
	 * 
	 * @pre cognome != null
	 * @pre cognome.length() &#62;= 2 and nome.length() &#60;= 30
	 * 
	 * @author Caggiano Gianluca
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	
	/**
	* Permette di definire una stringa che pu� essere considerata come la 
	* "rappresentazione testuale" dell'oggetto Utente.
	* 
	* @return Stringa che rappresenta una descrizione pi� accurata e consona dell'oggetto
	*/
	@Override
	public String toString() {
		return getClass().getName()+" [user= " + user + ", password= " + password + ", nome=" + nome + ", cognome=" + cognome + "]";
	}

	/**
	* Determina se due oggetti rappresentano lo stesso utente confrontando gli user dei suddetti.
	* 
	* @return true se gli user dei due oggetti sono gli stessi, false altrimenti
	*/
	@Override
	public boolean equals(Object object) {
		if (object == null) {
		      return false;
		}
		    
		if (object.getClass() != getClass()) {
		      return false;
		}
		    
		Utente utenteRegistrato = (Utente) object;
		    
		return user.equals(utenteRegistrato.getUser());
	}

}
