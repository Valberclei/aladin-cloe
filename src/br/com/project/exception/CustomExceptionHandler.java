package br.com.project.exception;

import java.util.Iterator;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.hibernate.SessionFactory;
import org.primefaces.context.RequestContext;

import br.com.framework.hibernate.session.HibernateUtil;

public class CustomExceptionHandler extends ExceptionHandlerWrapper {
	
	private ExceptionHandler wrapperd;
	
	final FacesContext facesContext = FacesContext.getCurrentInstance();
	
	final Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
	
	final NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();
	
	public CustomExceptionHandler(ExceptionHandler exceptionHandler) {
		
		this.wrapperd = exceptionHandler;
		
	}

	@Override
	public ExceptionHandler getWrapped() {
		
		return wrapperd;
	}
	
	@Override
	public void handle() throws FacesException {
		
		final Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();
		
		while(iterator.hasNext()) {
			
			ExceptionQueuedEvent event = iterator.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
			
			Throwable exeption = context.getException(); 
			
			try {
				
				requestMap.put("ExceptionMessage", exeption.getMessage());
				
				if(exeption != null && exeption.getMessage() != null && exeption.getMessage().indexOf("ConstraintViolationException") != -1); {
				
					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_WARN, "Registro "
							+ "não pode estar associado.", ""));
					
				} 
				
				if(exeption != null && exeption.getMessage() != null && exeption.getMessage().indexOf("org.hibernate.StaleObjectStateException") != -1) {
									
				FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registro foi atualizado ou excluido por outro usuário." + " Consulte novamente.", ""));	 
					 
				} else {
					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL, "O sistema se recuperou de um erro inesperado.", ""));
				    
					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Você pode continuar usando o sistema normalmente.", ""));
					
					FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL, "O erro foi causado por:\n" + exeption.getMessage(), ""));
					
					//Esse alert apenas é exibido se a pagina não redirecionar
					RequestContext.getCurrentInstance().execute("alert('O sistema se recuperou de um erro inesperado.')");
					
					RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "O sistema se recuperou de um erro inesperado."));
					
					navigationHandler.handleNavigation(facesContext, null, "/error/error.jsf?faces-redirect=true&expired=true");
				}
				
				facesContext.renderResponse();
				
			}finally {
				//aula 30
				SessionFactory sf = HibernateUtil.getSessionFactory();
				if(sf.getCurrentSession().getTransaction().isActive()) {
					sf.getCurrentSession().getTransaction().rollback();
					exeption.printStackTrace();
					
					iterator.remove();
				}
			}
			
			getWrapped().handle();
			
		}
		
	}

}
