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

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import py.org.icarusdb.commons.util.IDBProperties;
import py.org.icarusdb.example.server.model.City;
import py.org.icarusdb.example.server.model.City_;
import py.org.icarusdb.example.server.model.Continent;
import py.org.icarusdb.example.server.model.Country;
import py.org.icarusdb.example.server.model.Country_;
import py.org.icarusdb.example.server.model.State;
import py.org.icarusdb.example.server.model.State_;

@RequestScoped
public class CityRepository
{

    @Inject
    private EntityManager em;

    public City findById(Long id)
    {
        return em.find(City.class, id);
    }

    public List<City> findByName(String name)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<City> criteria = cb.createQuery(City.class);
        Root<City> city = criteria.from(City.class);
        criteria.select(city).where(cb.equal(city.get(City_.name), name));
        
        return em.createQuery(criteria).getResultList();
    }

    public List<City> findAllOrderedByName()
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<City> criteria = cb.createQuery(City.class);
        Root<City> city = criteria.from(City.class);
        criteria.select(city).orderBy(cb.asc(city.get(City_.name)));
        
        return em.createQuery(criteria).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<City> find(IDBProperties parameters)
    {
        String ejbql = "select o from City o ";
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
        
        if(parameters.containsKey(City_.state.getName()))
        {
            Long id = null;
            
            Object state = parameters.get(City_.state.getName());
            if(state instanceof Number) {
                id = new Long(state.toString().trim());
            } else {
                // FIXME use codehous deserializer
                id = ((State)state).getId();
            }
            
            if (addAnd) restrictions += " and ";
            restrictions += " o.state.id = " + id;
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
            restrictions += " o.state.country.id = " + id;
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
            restrictions += " o.state.country.continent.id = " + id;
            addAnd = true;
        }
        
        if(restrictions.length() == 5) restrictions = "";
        
        return em.createQuery(ejbql + restrictions).getResultList();
    }
}
