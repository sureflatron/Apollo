
package myapps.servicio_basico.error;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;

public class CustomExceptionHandler extends ExceptionHandlerWrapper {

	private ExceptionHandler exceptionHandler;

	public CustomExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return this.exceptionHandler;
	}

	@Override
	public void handle() throws FacesException {
		for (Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator(); i.hasNext();) {
			ExceptionQueuedEvent exceptionQueuedEvent = i.next();

			ExceptionQueuedEventContext exceptionQueuedEventContext = (ExceptionQueuedEventContext) exceptionQueuedEvent.getSource();

			Throwable throwable = exceptionQueuedEventContext.getException();

			if (throwable != null) {

				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				throwable.printStackTrace(pw);

				FacesContext facesContext = FacesContext.getCurrentInstance();

				Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
				NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();

				try {
					requestMap.put("currentView", throwable.getMessage());
					requestMap.put("exceptionDetail", sw);
					// facesContext.getExternalContext().getFlash().put("exceptionCause", throwable.getCause());
					navigationHandler.handleNavigation(facesContext, null, "/error.xhtml?faces-redirect=true");

					facesContext.renderResponse();
				} finally {
					i.remove();

				}

			}

		}
		getWrapped().handle();
	}

}