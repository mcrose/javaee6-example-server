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
package py.org.icarusdb.example.server.service;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import py.org.icarusdb.example.server.model.Country;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class CountryManager
{

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    public Serializable persist(Country entity) throws Exception
    {
        entity.setName(entity.getName().trim());
        
        log.info("Persisting " + entity.getName());
        em.persist(entity);
        
        return entity.getId();
    }
    
    public Country update(Country entity) throws Exception
    {
        entity.setName(entity.getName().trim());
        
        log.info("updating " + entity.getName());
        
        // TODO store flush mode in a [upper?] class
        em.merge(entity);
        //em.flush();
        
        return entity;
    }

    public Country save(Country entity) throws Exception
    {
        if(entity.getId() == null) {
            Serializable id = persist(entity);
            return em.find(Country.class, id);
        } else {
            return update(entity);
        }
        
    }

    public String remove(Country entity) throws Exception
    {
        em.remove(em.find(Country.class, entity.getId()));
        
        return "removed";
    }
    
}
