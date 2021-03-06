package storagelayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import gestioneprogettoformativo.ProgettoFormativo;
import gestioneprogettoformativo.RichiestaTirocinio;
import gestioneutente.Azienda;
import gestioneutente.Professore;
import gestioneutente.Segreteria;
import gestioneutente.Studente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Classe per i test di unita' di DatabasePf.
 * 
 * @author Caggiano Gianluca
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDatabasePf {

  private static Studente s;
  private static Azienda a;
  private static Professore p;
  private static Segreteria segreteria;
  private static RichiestaTirocinio rt;
  private static ProgettoFormativo pf;
  // Id della richiesta che andremo a creare per il test e che verr� poi eliminata
  private static int idRichiesta;
  private static int idProgetto;

  /**
   * Inizializzazione degli oggetti utili nel test.
   */
  @BeforeClass
  public static void init() {
    try {
      s = DatabaseGu.getStudenteByEmail("ciccio@studenti.unisa.it");
      a = DatabaseGu.getAziendaByEmail("convenzione2@gmail.com");
      p = DatabaseGu.getProfessoreByEmail("massimo@unisa.it");
      segreteria = DatabaseGu.getSegreteriaByUser("segreteriaUnisa");
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // Nuovi dati da inserire per il test
    rt = new RichiestaTirocinio();
    pf = new ProgettoFormativo();
  }

  /**
   * Test method for
   * {@link storagelayer.DatabasePf#addRichiesta 
   * (gestioneprogettoformativo.RichiestaTirocinio, gestioneutente.Studente)}.
   */
  @Test
  public void test01_AddRichiesta() {
    rt.setAzienda(a);
    rt.setProfessore(p);
    try {
      idRichiesta = DatabasePf.addRichiesta(rt, s);
      assertNotEquals(-1, idRichiesta);
    } catch (SQLException e) {
      fail("Non doveva capitare");
    }

    // Testa il metodo equals di RichiestaTirocinio
    assertTrue(rt.equals(rt));
    assertFalse(rt.equals(null));
    assertFalse(rt.equals(pf));
    // Caso in cui non � uguale convalida azienda
    RichiestaTirocinio richiesta = new RichiestaTirocinio(1, a, p, true, true);
    assertFalse(rt.equals(richiesta));
    // Caso in cui non � uguale convalida prof
    richiesta = new RichiestaTirocinio(1, a, p, false, true);
    assertFalse(rt.equals(richiesta));
    // Caso in cui non � uguale l'azienda
    Azienda az = new Azienda("ciao", "ciao", "ciao", "mare", "ciao", "ciao", "ciao", "ciao",
        "mareee", "eeee", false);
    richiesta = new RichiestaTirocinio(1, az, p, false, false);
    assertFalse(rt.equals(richiesta));
    // Caso in cui non � uguale il professore
    Professore prof = new Professore("ciao", "ciao", "ciao", "ciao", false, "Bah");
    richiesta = new RichiestaTirocinio(1, a, prof, false, false);
    assertFalse(rt.equals(richiesta));
  }

  /**
   * Test method for
   * {@link storagelayer.DatabasePf#addProgettoFormativo 
   * (gestioneprogettoformativo.ProgettoFormativo)}.
   * 
   * @throws SQLException
   *           Eccezzione lanciata nel caso in cui il collegamento al database o la query non va a
   *           buon fine
   */
  @Test
  public void test02_AddProgettoFormativo() throws SQLException {
    pf.setAzienda(a);
    pf.setProfessore(p);
    pf.setSegreteria(segreteria);
    pf.setStudente(s);
    pf.setObiettivi("Test Progetto Formativo");
    pf.setDataInizio("2018-01-15");
    pf.setDataFine("2018-02-20");
    try {
      Boolean done = DatabasePf.addProgettoFormativo(pf);
      assertTrue(done);
    } catch (SQLException e) {
      fail("Non doveva capitare");
    }

    // Recupera l'id del progetto formativo appena inserito nel database
    Connection connection = null;

    try {
      connection = Database.getConnection();
      Statement statement = connection.createStatement();

      ResultSet rs = statement.executeQuery("SELECT MAX(ID) FROM progettoformativo");

      if (rs.next()) {
        idProgetto = rs.getInt(1);
        pf.setId(idProgetto);
      }
      rs.close();
      statement.close();
    } finally {

      Database.releaseConnection(connection);

    }

    // Testa il metodo equals di ProgettoFormativo
    assertTrue(pf.equals(pf));
    assertFalse(pf.equals(null));
    assertFalse(pf.equals(rt));
    // Caso in cui il progetto formativo � diverso
    ProgettoFormativo progetto = new ProgettoFormativo();
    assertFalse(pf.equals(progetto));
  }

  /**
   * Testa il caso in cui l'id della richiesta di tirocinio non esiste. Test method for
   * {@link storagelayer.DatabasePf#getRichiestaById(int)}.
   */
  @Test
  public void test03_GetRichiestaById() {
    try {
      RichiestaTirocinio idErrato = DatabasePf.getRichiestaById(0);
      assertNull(idErrato);
    } catch (SQLException e) {
      fail("Non doveva capitare");
    }
  }

  /**
   * Test method for {@link storagelayer.DatabasePf#doRetrieveRichiesteAziende(java.lang.String)}.
   */
  @Test
  public void test04_DoRetrieveRichiesteAziende() {
    try {
      ArrayList<RichiestaTirocinio> arrayAtteso = new ArrayList<RichiestaTirocinio>();
      rt.setId(idRichiesta);
      arrayAtteso.add(rt);
      String atteso = arrayAtteso.toString();

      ArrayList<RichiestaTirocinio> arrayTest = DatabasePf.doRetrieveRichiesteAziende(a.getUser());
      String test = arrayTest.toString();

      assertEquals(atteso, test);
    } catch (SQLException e) {
      fail("Non doveva capitare");
    }
  }

  /**
   * Test method for
   * {@link storagelayer.DatabasePf#doRetrieveRichiesteProfessore(java.lang.String)}.
   */
  @Test
  public void test05_DoRetrieveRichiesteProfessore() {
    try {
      ArrayList<RichiestaTirocinio> arrayAtteso = new ArrayList<RichiestaTirocinio>();
      rt.setId(idRichiesta);
      arrayAtteso.add(rt);
      String atteso = arrayAtteso.toString();

      ArrayList<RichiestaTirocinio> arrayTest = DatabasePf
          .doRetrieveRichiesteProfessore(p.getUser());
      String test = arrayTest.toString();

      assertEquals(atteso, test);
    } catch (SQLException e) {
      fail("Non doveva capitare");
    }
  }

  /**
   * Test method for {@link storagelayer.DatabasePf#setConvalidaAzienda(int)}.
   */
  @Test
  public void test06_SetConvalidaAzienda() {
    try {
      Boolean done = DatabasePf.setConvalidaAzienda(idRichiesta);
      assertTrue(done);
    } catch (SQLException e) {
      fail("Non doveva capitare");
    }
  }

  /**
   * Test method for {@link storagelayer.DatabasePf#setConvalidaProf(int)}.
   */
  @Test
  public void test07_SetConvalidaProf() {
    try {
      Boolean done = DatabasePf.setConvalidaProf(idRichiesta);
      assertTrue(done);
    } catch (SQLException e) {
      fail("Non doveva capitare");
    }
  }

  /**
   * Test method for
   * {@link storagelayer.DatabasePf#doRetrieveRichiesteConvalidate(java.lang.String)}.
   */
  @Test
  public void test08_DoRetrieveRichiesteConvalidate() {
    try {
      rt.setId(idRichiesta);
      rt.setConvalidaAzienda(true);
      rt.setConvalidaProf(true);
      ArrayList<RichiestaTirocinio> arrayAtteso = new ArrayList<RichiestaTirocinio>();
      arrayAtteso.add(rt);
      String atteso = arrayAtteso.toString();

      ArrayList<RichiestaTirocinio> arrayTest = DatabasePf
          .doRetrieveRichiesteConvalidate(a.getUser());
      String test = arrayTest.toString();

      assertEquals(atteso, test);
    } catch (SQLException e) {
      fail("Non doveva capitare");
    }
  }

  /**
   * Test method for {@link storagelayer.DatabasePf#getStudenteByIdRichiesta(int)}.
   */
  @Test
  public void test09_GetStudenteByIdRichiesta() {
    try {
      // Caso in cui lo studente con tale richiesta c'�
      Studente ciccio = DatabasePf.getStudenteByIdRichiesta(idRichiesta);
      assertEquals(s.toString(), ciccio.toString());
      // Caso in cui lo studente con una richiesta non c'�
      ciccio = DatabasePf.getStudenteByIdRichiesta(0);
      assertNull(ciccio);
    } catch (SQLException e) {
      fail("Non doveva capitare");
    }
  }

  /**
   * Test method for
   * {@link storagelayer.DatabasePf#doRetrievProgettoFormativoStudente(java.lang.String)}.
   */
  @Test
  public void test10_DoRetrievProgettoFormativoStudente() {
    try {
      ProgettoFormativo progettoTest = DatabasePf
          .doRetrievProgettoFormativoStudente(s.getMatricola());

      assertNotNull(progettoTest);
    } catch (SQLException e) {
      fail("Non doveva capitare");
    }
  }

  /**
   * Test method for
   * {@link storagelayer.DatabasePf#doRetrievProgettoFormativoProfessore(java.lang.String)}.
   */
  @Test
  public void test11_DoRetrievProgettoFormativoProfessore() {
    try {
      ArrayList<ProgettoFormativo> nonAtteso = new ArrayList<ProgettoFormativo>();
      ArrayList<ProgettoFormativo> progettoTest = DatabasePf
          .doRetrievProgettoFormativoProfessore(p.getUser());

      assertNotEquals(nonAtteso.toString(), progettoTest.toString());
    } catch (SQLException e) {
      fail("Non doveva capitare");
    }
  }

  /**
   * Test method for {@link storagelayer.DatabasePf#setConvalidaProfProgetto(int)}.
   */
  @Test
  public void test12_SetConvalidaProfProgetto() {
    try {
      Boolean done = DatabasePf.setConvalidaProfProgetto(idProgetto);
      assertTrue(done);
    } catch (SQLException e) {
      fail("Non doveva capitare");
    }
  }

  /**
   * Test method for {@link storagelayer.DatabasePf#setSottoscrizioneStuProgetto(int)}.
   */
  @Test
  public void test13_SetSottoscrizioneStuProgetto() {
    try {
      Boolean done = DatabasePf.setSottoscrizioneStuProgetto(idProgetto);
      assertTrue(done);
    } catch (SQLException e) {
      fail("Non doveva capitare");
    }
  }

  /**
   * Test method for
   * {@link storagelayer.DatabasePf#doRetrievProgettoFormativoSegreteria(java.lang.String)}.
   */
  @Test
  public void test14_DoRetrievProgettoFormativoSegreteria() {
    try {
      ArrayList<ProgettoFormativo> nonAtteso = new ArrayList<ProgettoFormativo>();
      ArrayList<ProgettoFormativo> progettoTest = DatabasePf
          .doRetrievProgettoFormativoSegreteria(segreteria.getUser());

      assertNotEquals(nonAtteso.toString(), progettoTest.toString());
    } catch (SQLException e) {
      fail("Non doveva capitare");
    }
  }

  /**
   * Test method for {@link storagelayer.DatabasePf#setConvalidaSegrProgetto(int)}.
   */
  @Test
  public void test15_SetConvalidaSegrProgetto() {
    try {
      Boolean done = DatabasePf.setConvalidaSegrProgetto(idProgetto);
      assertTrue(done);
    } catch (SQLException e) {
      fail("Non doveva capitare");
    }
  }

  /**
   * Elimina gli oggetti creati nel database durante il test.
   * 
   * @throws SQLException
   *           Eccezioni lanciate durante l'interazione con il database.
   */
  @AfterClass
  public static void elimina() throws SQLException {

    eliminaRichiestaStudente();
    eliminaRichiesta();
    eliminaProgettoFormativo();
  }

  private static void eliminaRichiestaStudente() throws SQLException {
    Connection con = null;

    String deleteRichiestaStudente = "UPDATE studente SET RichiestaTirocinioID= null "
        + "WHERE Matricola=?";

    // Elimina la chiave esterna della richiesta in studente
    try {
      con = Database.getConnection();

      PreparedStatement ps = con.prepareStatement(deleteRichiestaStudente);
      ps.setString(1, s.getMatricola());

      ps.executeUpdate();
      ps.close();
      con.commit();

    } finally {

      Database.releaseConnection(con);

    }

  }

  private static void eliminaRichiesta() throws SQLException {
    Connection con = null;

    String rimuoviRichiesta = "DELETE FROM richiestatirocinio WHERE ID=?;";

    // Elimina la richiesta dalla tabella nel database
    try {
      con = Database.getConnection();

      PreparedStatement ps = con.prepareStatement(rimuoviRichiesta);
      ps.setInt(1, idRichiesta);

      ps.executeUpdate();
      ps.close();
      con.commit();

    } finally {

      Database.releaseConnection(con);

    }
  }

  private static void eliminaProgettoFormativo() throws SQLException {
    Connection con = null;

    String rimuoviRichiesta = "DELETE FROM progettoformativo ";

    // Elimina la richiesta dalla tabella nel database
    try {
      con = Database.getConnection();

      PreparedStatement ps = con.prepareStatement(rimuoviRichiesta);

      ps.executeUpdate();
      ps.close();
      con.commit();

    } finally {

      Database.releaseConnection(con);

    }
  }
}
