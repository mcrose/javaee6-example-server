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
package py.org.icarusdb.example.server.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 * @author Betto McRose [icarus]
 *         mcrose@icarusdb.com.py
 *         mcrose.dev@gmail.com
 *
 */
public abstract class ResourceRESTService
{

    @Inject
    protected Logger log;

    @Inject
    protected Validator validator;


    /**
     * Creates a JAX-RS "Bad Request" response including a map of all violation
     * fields, and their message. This can then be used by clients to show
     * violations.
     * 
     * @param violations
     *            A set of violations that needs to be reported
     * @return JAX-RS response containing all violations
     */
    protected Response.ResponseBuilder constraintViolationResponse(Set<ConstraintViolation<?>> violations)
    {
        log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<String, String>();

        for (ConstraintViolation<?> violation : violations)
        {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }


    /**
     * Handle generic exceptions
     * @param e
     * 
     * @return
     */
    protected ResponseBuilder getBadRequestResponse(Exception e)
    {
        Map<String, String> responseObj = new HashMap<String, String>();
        responseObj.put("error", e.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }


    public ResponseBuilder uniqueValidationViolationResponse(String key, String value)
    {
        Map<String, String> responseObj = new HashMap<String, String>();
        responseObj.put(key, value);
        return Response.status(Response.Status.CONFLICT).entity(responseObj);
    }


}
