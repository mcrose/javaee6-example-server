/**
 * 
 */
package py.org.pti.example.server.rest;

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
 * @author rgamarra
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
    protected Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations)
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


}
