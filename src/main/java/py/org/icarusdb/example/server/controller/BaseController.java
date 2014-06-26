/**
 * Copyright 2014 Roberto Gamarra (Betto McRose [icarus]
           mcrose@icarusdb.com.py@icarusdb.com.py)
 * Icarus
 * Betto McRose
 * Roberto Gamarra
 * 
 * as you wish... at your service
 * 
 * IcarusDB
 * 
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
package py.org.icarusdb.example.server.controller;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * @author Betto McRose [icarus]
 *         mcrose@icarusdb.com.py
 *         mcrose.dev@gmail.com
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
