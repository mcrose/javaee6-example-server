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
package py.org.icarusdb.example.server.data;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import py.org.icarusdb.commons.util.IDBProperties;
import py.org.icarusdb.example.server.model.Continent;
import py.org.icarusdb.example.server.model.Country;
import py.org.icarusdb.example.server.model.Country_;
import py.org.icarusdb.example.server.model.State;
import py.org.icarusdb.example.server.model.State_;

@RequestScoped
public class StateRepository
{
    private CriteriaBuilder cb = null;
    private CriteriaQuery<State> criteria = null;
    private Root<State> state = null;

    @Inject
    private EntityManager em;

    @PostConstruct
    public void init()
    {
        cb = em.getCriteriaBuilder();
        criteria = cb.createQuery(State.class);
        state = criteria.from(State.class);
    }
    
    public State findById(Long id)
    {
        return em.find(State.class, id);
    }

    public List<State> findByName(String name)
    {
        criteria.select(state).where(cb.equal(state.get(State_.name), name));
        
        return em.createQuery(criteria).getResultList();
    }

    public List<State> findAllOrderedByName()
    {
        criteria.select(state).orderBy(cb.asc(state.get(State_.name)));
        
        return em.createQuery(criteria).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<State> find(IDBProperties parameters)
    {
        String ejbql = "select o from State o ";
        String restrictions = "where";
        boolean addAnd = false;
        
        String name = (String) parameters.get(State_.name.getName());
        if(name != null && !name.trim().isEmpty())
        {
            restrictions += " lower(o.name) like '%" + name.toLowerCase() + "%'";
            addAnd = true;
        }
        
        if(parameters.containsKey(State_.active.getName()))
        {
            if (addAnd) restrictions += " and ";
            restrictions += " o.active = " + parameters.get(State_.active.getName());
            addAnd = true;
        }
        
        if(parameters.containsKey(State_.country.getName()))
        {
            Long id = null;
            
            Object country = parameters.get(State_.country.getName());
            if(country instanceof Number) {
                id = new Long(country.toString().trim());
            } else {
                // FIXME use codehous deserializer
                id = ((Country)country).getId();
            }
            
            if (addAnd) restrictions += " and ";
            restrictions += " o.country.id = " + id;
            addAnd = true;
        }
        
        if(parameters.containsKey(Country_.continent.getName()))
        {
            Long id = null;
            
            Object continent = parameters.get(Country_.continent.getName());
            if(continent instanceof Number) {
                id = new Long(continent.toString().trim());
            } else {
                // FIXME use codehous deserializer
                id = ((Continent)continent).getId();
            }
            
            if (addAnd) restrictions += " and ";
            restrictions += " o.country.continent.id = " + id;
            addAnd = true;
        }
        
        if(restrictions.length() == 5) restrictions = "";
        
        return em.createQuery(ejbql + restrictions).getResultList();
    }

    public List<State> findActives()
    {
        cb = em.getCriteriaBuilder();
        criteria = cb.createQuery(State.class);
        state = criteria.from(State.class);
        
        criteria
            .select(state)
            .where(
                    cb.equal(state.get(State_.active), Boolean.TRUE)
            );
        
        return em.createQuery(criteria).getResultList();
    }
}
