/**
 * 
 */
package py.org.pti.example.server.controller;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * @author Betto McRose [icarus]
           mcrose@icarusdb.com.py
 *
 */
public abstract class BaseController
{
    @Inject
    protected FacesContext facesContext;


    protected String getRootErrorMessage(Exception e)
    {
        // Default to general error message that registration failed.
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null)
        {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null)
        {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }

    
}
