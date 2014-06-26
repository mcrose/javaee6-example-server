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

import py.org.icarusdb.example.server.converter.ConverterHelper;
import py.org.icarusdb.example.server.data.ContinentRepository;
import py.org.icarusdb.example.server.dto.ContinentDTO;
import py.org.icarusdb.example.server.model.Continent;
import py.org.icarusdb.example.server.service.ContinentManager;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the
 * continentDTOs table.
 */
@Path("/continents")
@RequestScoped
public class ContinentResourceRESTService extends ResourceRESTService
{
    @Inject
    private ContinentRepository repository;

    @Inject
    ContinentManager registration;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ContinentDTO> listAllContinentDTOs()
    {
        return ConverterHelper.convertContinentsToDTO(repository.findAllOrderedByName());
    }

    @GET
    @Path("/actives")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ContinentDTO> listAllActiveContinentDTOs()
    {
        return ConverterHelper.convertContinentsToDTO(repository.findActives());
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public ContinentDTO lookupById(@PathParam("id") long id)
    {
        Continent continent = repository.findById(id);
        if (continent == null)
        {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return new ContinentDTO(continent);
    }

    @POST
    @Path("/name")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<ContinentDTO> lookupByName(String name)
    {
        if(name == null || name.isEmpty()) { 
            return null; // TODO return error code
        }

        return ConverterHelper.convertContinentsToDTO(repository.findByName(name));
    }
    
    /**
     * Creates a new continentDTO from the values provided. Performs validation,
     * and will return a JAX-RS response with either 200 OK, or with a map of
     * fields, and related errors.
     * 
     * TODO merge create and update methods
     * 
     */
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createContinentDTO(ContinentDTO continentDTO)
    {

        Response.ResponseBuilder builder = null;

        try
        {
            // Validates continentDTO using bean validation
            validateContinentDTO(continentDTO);

            registration.register(continentDTO.toEntity());

            // Create an "ok" response
            builder = Response.ok();
        }
        catch (ConstraintViolationException ce)
        {
            // Handle bean validation issues
            builder = constraintViolationResponse(ce.getConstraintViolations());
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
     * Validates the given ContinentDTO variable and throws validation exceptions
     * based on the type of error. If the error is standard bean validation
     * errors then it will throw a ConstraintValidationException with the set of
     * the constraints violated.
     * </p>
     * <p>
     * If the error is caused because an existing continentDTO with the same name
     * is registered it throws a regular validation exception so that it can be
     * interpreted separately.
     * </p>
     * 
     * @param continentDTO
     *            ContinentDTO to be validated
     * @throws ConstraintViolationException
     *             If Bean Validation errors exist
     * @throws ValidationException
     *             If continentDTO with the same name already exists
     */
    private void validateContinentDTO(ContinentDTO continentDTO) throws ConstraintViolationException, ValidationException
    {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<ContinentDTO>> violations = validator.validate(continentDTO);

        if (!violations.isEmpty())
        {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the name address
        if (nameAlreadyExists(continentDTO.getName(), continentDTO.getId()))
        {
            throw new ValidationException("Unique Name Violation");
        }
    }

    /**
     * Checks if a continentDTO with the same name address is already registered.
     * This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "name")" constraint from the ContinentDTO
     * class.
     * 
     * @param name
     *            The name to check
     * @return True if the name already exists, and false otherwise
     */
    public boolean nameAlreadyExists(String name, Long id)
    {
        List<Continent> continents = repository.findByName(name.trim());
        
        if (continents.isEmpty()) return false;
        
        for(int index = 0; index < continents.size(); index++)
        {
            if(name.trim().equalsIgnoreCase(continents.get(index).getName()))
            {
                if(id != null && continents.get(index).getId().longValue() == id.longValue())
                {
                    return false;
                }
                return true;
            }
        }
        
        return false;
    }
    
    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(ContinentDTO dto)
    {
        if(dto.getId() == null)
        {
            return createContinentDTO(dto);
        }
        

        Response.ResponseBuilder builder = null;

        try
        {
            // Validates continentDTO using bean validation
            validateContinentDTO(dto);
            
            Continent continent = repository.findById(dto.getId());
            continent.setName(dto.getName());
            continent.setActive(dto.getActive());

            registration.update(continent);

            // Create an "ok" response
            builder = Response.ok();
        }
        catch (ConstraintViolationException ce)
        {
            // Handle bean validation issues
            builder = constraintViolationResponse(ce.getConstraintViolations());
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
    
}
