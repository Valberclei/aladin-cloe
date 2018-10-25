package br.com.framework.hibernate.session;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.SessionFactoryImplementor;

import br.com.framework.implementacao.crud.VariavelConexaoUtil;

/**
 * responsavel por estabelecer a conexão com o hibernate
 * @author Rockstead
 *
 */
public class HibernateUtil implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public static String JAVA_COMP_ENV_JDBC_DATA_SOURCE = "java:/comp/env/jdbc/datasource";
	
	private static SessionFactory sessionFactory = buildSessionFactory();
	
	@SuppressWarnings("deprecation")
	private static SessionFactory buildSessionFactory() {
		
		try {
			
			if(sessionFactory == null) {
				sessionFactory = new Configuration().configure().buildSessionFactory();
			}
			return sessionFactory;
			
		}catch(Exception e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError("Erro criando conexão sessionFactory");
		}
		
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	/**
	 * Retorna a sessão do sessionFactory
	 * @return
	 */
	public static Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}
	
	/**
	 * Abre uma nova sessão no sessionFactory
	 * return session
	 */
	public static Session openSession() {
		if(sessionFactory == null) {
			buildSessionFactory();
		}
		return sessionFactory.openSession();
	}
	
	/**
	 * Obtém a connection do provedor de conexões configurado
	 * @return ConnectionSQL
	 * @throws SQLException
	 */
	public static Connection getConnectionProvider() throws SQLException {
		return ((SessionFactoryImplementor) sessionFactory).getConnectionProvider().getConnection();
	}
	
	public static Connection getConnection()throws Exception{
		InitialContext context = new InitialContext();
		DataSource ds = (DataSource)context.lookupLink(JAVA_COMP_ENV_JDBC_DATA_SOURCE);
		
		return ds.getConnection();
	}
	
	public DataSource getDataSourceJndi()throws NamingException{
		
		InitialContext context = new InitialContext();
		return(DataSource) context.lookup(VariavelConexaoUtil.JAVA_COMP_ENV_JDBC_DATA_SOURCE);
		
	}

}
