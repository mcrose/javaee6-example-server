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

import java.util.HashSet;
import java.util.List;
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

import py.org.icarusdb.commons.util.IDBProperties;
import py.org.icarusdb.example.server.converter.ConverterHelper;
import py.org.icarusdb.example.server.data.StateRepository;
import py.org.icarusdb.example.server.dto.StateDTO;
import py.org.icarusdb.example.server.model.State;
import py.org.icarusdb.example.server.service.StateManager;
import py.org.icarusdb.util.AppHelper;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the
 * states table.
 */
@Path("/states")
@RequestScoped
public class StateResourceRESTService extends ResourceRESTService
{
    @Inject
    private StateRepository repository;

    @Inject
    StateManager manager;

    @Inject
    ConverterHelper dtoConverter;

    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<StateDTO> listAllCountries()
    {
        return dtoConverter.convertStatesToDTO(repository.findAllOrderedByName());
    }

    @GET
    @Path("/actives")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StateDTO> listAllActiveContinentDTOs()
    {
        return dtoConverter.convertStatesToDTO(repository.findActives());
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public State lookupStateById(@PathParam("id") long id)
    {
        State state = repository.findById(id);
        if (state == null)
        {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return state;
    }

    @POST
    @Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<StateDTO> search(IDBProperties parameters)
    {
        if(parameters == null || parameters.isEmpty())
        {
            // FIXME throw the correct exception
            // TODO create exception
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        
        try
        {
            return dtoConverter.convertStatesToDTO(repository.find(parameters));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            // FIXME throw the correct exception
            // TODO create exception
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        
    }

    /**
     * Creates a new State from the values provided. Performs validation,
     * and will return a JAX-RS response with either 200 ok, or with a map of
     * fields, and related errors.
     */
    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(StateDTO dto)
    {

        Response.ResponseBuilder builder = null;

        try
        {
            // Validates State using bean validation
            validateStateDTO(dto);

            /*State entity = */manager.save(dto.toEntity());

            // Create an "ok" response
            builder = Response.ok();//.entity(new StateDTO(entity));
        }
        catch (ConstraintViolationException ce)
        {
            // Handle bean validation issues
            builder = constraintViolationResponse(ce.getConstraintViolations());
        }
        catch (ValidationException e)
        {
            // Handle the unique constrain violation
            String key = AppHelper.getBundleMessage("label.country.name");
            String value =  AppHelper.getBundleMessage("error.already.exists");
            builder = super.uniqueValidationViolationResponse(key, key+" "+value);
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
     * Validates the given State variable and throws validation exceptions
     * based on the type of error. If the error is standard bean validation
     * errors then it will throw a ConstraintValidationException with the set of
     * the constraints violated.
     * </p>
     * <p>
     * If the error is caused because an existing State with the same name
     * is registered it throws a regular validation exception so that it can be
     * interpreted separately.
     * </p>
     * 
     * @param dto
     *            State to be validated
     * @throws ConstraintViolationException
     *             If Bean Validation errors exist
     * @throws ValidationException
     *             If State with the same name already exists
     */
    private void validateStateDTO(StateDTO dto) throws ConstraintViolationException, ValidationException
    {
        // FIXME is this really working ???
        // Create a bean validator and check for issues.
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<StateDTO>> violations = validator.validate(dto);

        if (!violations.isEmpty())
        {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the name address
        if (nameAlreadyExists(dto.getName(), dto.getId()))
        {
            throw new ValidationException("Unique Name Violation");
        }
    }

    /**
     * Checks if a State with the same name address is already registered.
     * This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "name")" constraint from the State
     * class.
     * 
     * @param name
     *            The name to check
     * @return True if the name already exists, and false otherwise
     */
    public boolean nameAlreadyExists(String name, Long id)
    {
        List<State> entities = repository.findByName(name.trim());
        
        if (entities.isEmpty()) return false;
        
        for(int index = 0; index < entities.size(); index++)
        {
            if(name.trim().equalsIgnoreCase(entities.get(index).getName()))
            {
                if(id != null && entities.get(index).getId().longValue() == id.longValue())
                {
                    return false;
                }
                return true;
            }
        }
        
        return false;
    }
}
