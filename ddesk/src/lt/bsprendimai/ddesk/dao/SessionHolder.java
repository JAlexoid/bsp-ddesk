/*
 * SessionHolder.java
 *
 
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package lt.bsprendimai.ddesk.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * 
 * @author JAlexoid
 */
public class SessionHolder {
    public static final SessionFactory sessionFactory;

    static {
	try {
	    // Create the SessionFactory from hibernate.cfg.xml
	    sessionFactory = new Configuration().configure()
		    .buildSessionFactory();
	} catch (Exception ex) {
	    // Make sure you log the exception, as it might be swallowed
	    System.err.println("Initial SessionFactory creation failed." + ex);
	    throw new ExceptionInInitializerError(ex);
	}
    }

    public static void clearCache() {
	try {
	    sessionFactory.evict(Ticket.class);
	    sessionFactory.evict(TicketInfo.class);
	    sessionFactory.evict(TicketHistory.class);
	    sessionFactory.evict(Company.class);
	    sessionFactory.evict(CompanyContract.class);
	    sessionFactory.evict(Person.class);
	} catch (Exception ex) {
	}
    }

    public static void close() {
	try {
	    sessionFactory.close();
	} catch (Exception ex) {
	}
    }

    public static final ThreadLocal<TxSessionHolder> session = new ThreadLocal<TxSessionHolder>();

    public static TxSessionHolder currentSession() throws HibernateException {
	TxSessionHolder s = session.get();

	// Open a new Session, if this thread has none yet
	if (s == null) {
	    s = new TxSessionHolder(sessionFactory.openSession());
	    s.setTx(s.getSess().beginTransaction());
	    // Store it in the ThreadLocal variable
	    session.set(s);
	}
	return s;
    }

    public static void endSession() {
	TxSessionHolder s = (TxSessionHolder) session.get();
	if (s != null) {
	    try {
		s.getSess().clear();
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	    try {
		// System.out.println("Rollback transaction");
		s.getTx().rollback();
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	    try {
		s.getSess().close();
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	    session.set(null);
	}

    }

    public static void closeSession() {
	TxSessionHolder s = (TxSessionHolder) session.get();
	if (s != null) {
	    try {
		s.getSess().flush();
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	    try {
		// System.out.println("Commit transaction");
		s.getTx().commit();
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	    try {
		s.getSess().close();
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }

	}
	session.set(null);
    }

    public static void restartTransaction() throws HibernateException {
	TxSessionHolder s = session.get();
	if (s != null) {
	    try {
		s.getSess().flush();
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	    try {
		// System.out.println("Commit transaction");
		s.getTx().commit();
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	    s.setTx(s.getSess().beginTransaction());

	}
    }

    public static class TxSessionHolder {
	private Session sess;
	private Transaction tx;

	public TxSessionHolder(Session sess) {
	    this.sess = sess;
	}

	public Session getSess() {
	    return sess;
	}

	public void setSess(Session sess) {
	    this.sess = sess;
	}

	public Transaction getTx() {
	    return tx;
	}

	public void setTx(Transaction tx) {
	    this.tx = tx;
	}

    }

}
