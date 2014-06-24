/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import py.org.icarusdb.example.server.data.ContinentRepository;
import py.org.icarusdb.example.server.model.Continent;
import py.org.icarusdb.example.server.service.ContinentRegistration;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the
 * continents table.
 */
@Path("/continents")
@RequestScoped
public class ContinentResourceRESTService extends ResourceRESTService
{
    @Inject
    private ContinentRepository repository;

    @Inject
    ContinentRegistration registration;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Continent> listAllContinents()
    {
        return repository.findAllOrderedByName();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Continent lookupById(@PathParam("id") long id)
    {
        Continent continent = repository.findById(id);
        if (continent == null)
        {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return continent;
    }

    @POST
    @Path("/name")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Continent> lookupByName(String name)
    {
        if(name == null || name.isEmpty()) { 
            return null; // TODO return error code
        }

        return repository.findByName(name);
    }
    
    /**
     * Creates a new continent from the values provided. Performs validation,
     * and will return a JAX-RS response with either 200 ok, or with a map of
     * fields, and related errors.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createContinent(Continent continent)
    {

        Response.ResponseBuilder builder = null;

        try
        {
            // Validates continent using bean validation
            validateContinent(continent);

            registration.register(continent);

            // Create an "ok" response
            builder = Response.ok();
        }
        catch (ConstraintViolationException ce)
        {
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
        }
        catch (ValidationException e)
        {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("name", "name already exists");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        }
        catch (Exception e)
        {
            // Handle generic exceptions
            builder = super.getBadRequestResponse(e);
        }

        return builder.build();
    }

    /**
     * <p>
     * Validates the given Continent variable and throws validation exceptions
     * based on the type of error. If the error is standard bean validation
     * errors then it will throw a ConstraintValidationException with the set of
     * the constraints violated.
     * </p>
     * <p>
     * If the error is caused because an existing continent with the same name
     * is registered it throws a regular validation exception so that it can be
     * interpreted separately.
     * </p>
     * 
     * @param continent
     *            Continent to be validated
     * @throws ConstraintViolationException
     *             If Bean Validation errors exist
     * @throws ValidationException
     *             If continent with the same name already exists
     */
    private void validateContinent(Continent continent) throws ConstraintViolationException, ValidationException
    {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Continent>> violations = validator.validate(continent);

        if (!violations.isEmpty())
        {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the name address
        if (nameAlreadyExists(continent.getName()))
        {
            throw new ValidationException("Unique Name Violation");
        }
    }

    /**
     * Checks if a continent with the same name address is already registered.
     * This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "name")" constraint from the Continent
     * class.
     * 
     * @param name
     *            The name to check
     * @return True if the name already exists, and false otherwise
     */
    public boolean nameAlreadyExists(String name)
    {
        return !repository.findByName(name.trim()).isEmpty();
    }
}
