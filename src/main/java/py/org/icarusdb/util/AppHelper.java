/**
 * Copyright 2014 Roberto Gamarra [icarus] ** ( Betto McRose  )
 *                mcrose@icarusdb.com.py | mcrose.dev@gmail.com
 *                
 * as you wish... at your service ;-P
 * 
 * IcarusDB.com.py
 *            
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package py.org.icarusdb.util;

import java.util.Locale;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.PersistenceException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.RollbackException;

import py.org.icarusdb.commons.util.AppBaseHelper;
import py.org.icarusdb.commons.util.BundleHelper;
import py.org.icarusdb.example.server.util.SessionParameters;

/**
 * @author Betto McRose [icarus]
 *         mcrose@icarusdb.com.py
 *         mcrose.dev@gmail.com
 *
 */
public class AppHelper extends AppBaseHelper
{
    
    public static String getDomainUrl()
    {
    	String host = getExternalContext().getRequestHeaderMap().get("host");
    	String appName = getExternalContext().getRequestContextPath();

    	return "http://" + host + appName;
    }

    public static ExternalContext getExternalContext()
    {
        return FacesContext.getCurrentInstance().getExternalContext();
    }
    
    public static ServletContext getServletContext()
    {
        return (ServletContext) getExternalContext().getContext();
    }
    
    public static HttpServletRequest getServletRequest()
    {
        return (HttpServletRequest) getExternalContext().getRequest();
    }
    
    public static HttpServletResponse getServletResponse()
    {
        return (HttpServletResponse) getExternalContext().getResponse();
    }
    
    public static Map<String, Object> getRequestContext()
    {
        return getExternalContext().getRequestMap();
    }
    
    public static Locale getLocale()
    {
        return FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }

    public static String getBundleMessage(String key)
    {
        return BundleHelper.getBundleMessage(SessionParameters.BUNDLE_URL, key);
    }
    
    /**
     * FIXME add jar to facilitate exceptions' debug and return detailed errors
     * 
     * @param e
     */
    public static void printException(Exception e)
    {
        String message = e.getMessage();
        
        if(e instanceof RollbackException)
        {
            RollbackException rbe = (RollbackException) e;
            Throwable cause = rbe.getCause();

            if(cause instanceof PersistenceException)
            {
                PersistenceException pe = (PersistenceException) cause;
                if(pe.getCause().toString().contains("ConstraintViolationException"))
                {
//                    ConstraintViolationException cve = (ConstraintViolationException) pe.getCause();
                    message = pe.getCause().getLocalizedMessage();
//                    for(ConstraintViolation<?> ex : cve.getConstraintViolations())
//                    {
//                        ex.getConstraintDescriptor();
//                        ex.getInvalidValue();
//                        ex.getRootBean();
//                    }
//                    message = cve.getMessage();
                }
            }
            
            
//            for (InvalidValue invalidValue : ise.getInvalidValues()) 
//            {
//                String msg = invalidValue.getBeanClass().getSimpleName() +
//                             " has an invalid property: " + invalidValue.getPropertyName() +
//                             " with message: " + invalidValue.getMessage() + ".";
//                System.err.println(msg);
//            }
            
        }
        else
        {
            e.printStackTrace();
        }
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "system.errors"));
    }

    public static String getClientIpAddr()
    {
        return getServletRequest().getRemoteAddr();
    }

}
