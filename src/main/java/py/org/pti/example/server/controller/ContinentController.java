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
package py.org.pti.example.server.controller;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import py.org.pti.example.server.model.Continent;
import py.org.pti.example.server.service.ContinentRegistration;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean 
// that has an EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
@Model
public class ContinentController extends BaseController
{
    @Inject
    private ContinentRegistration continentRegistration;

    @Produces
    @Named
    private Continent newContinent;

    @PostConstruct
    public void initNewContinent()
    {
        newContinent = new Continent();
    }

    public void register() throws Exception
    {
        try
        {
            continentRegistration.register(newContinent);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Persisted!", "Successfully saved");
            facesContext.addMessage(null, m);
            initNewContinent();
        }
        catch (Exception e)
        {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Successfully saved");
            facesContext.addMessage(null, m);
        }
    }

}
