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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.xml.bind.JAXBException;

import py.org.icarusdb.example.server.model.Continent;
import py.org.icarusdb.example.server.model.Country;
import py.org.icarusdb.example.server.model.Country_;

@RequestScoped
public class CountryRepository
{
    private CriteriaBuilder cb = null;
    private CriteriaQuery<Country> criteria = null;
    private Root<Country> country = null;
    

    @Inject
    private EntityManager em;

    @PostConstruct
    public void init()
    {
        cb = em.getCriteriaBuilder();
        criteria = cb.createQuery(Country.class);
        country = criteria.from(Country.class);
    }
    
    
    public Country findById(Long id)
    {
        return em.find(Country.class, id);
    }
    
    public List<Country> findByName(String name)
    {
        criteria
            .select(country)
            .where(
                    cb.equal(country.get(Country_.name), name)
            )
            .orderBy(
                    cb.asc(country.get(Country_.name))
            );
        
        return em.createQuery(criteria).getResultList();
    }

    public List<Country> findAllOrderedByName()
    {
        criteria.select(country).orderBy(cb.asc(country.get(Country_.name)));
        
        return em.createQuery(criteria).getResultList();
    }

    public List<Country> find(Properties parameters) throws JAXBException
    {
        List<Predicate> predicates = new ArrayList<Predicate>();
        
        if(parameters.containsKey(Country_.id.getName()))
        {
            predicates.add(cb.equal(country.get(Country_.id), parameters.get(Country_.id.getName())));
        }
        
        if(parameters.containsKey(Country_.name.getName()))
        {
            predicates.add(cb.equal(country.get(Country_.name), parameters.get(Country_.name.getName())));
        }
        
        if(parameters.containsKey(Country_.active.getName()))
        {
            predicates.add(cb.equal(country.get(Country_.active), parameters.get(Country_.active.getName())));
        }
        
        if(parameters.containsKey(Country_.continent.getName()))
        {
            Long id = null;
            
            Object continent = parameters.get(Country_.continent.getName());
            if(continent instanceof Number) {
                id = new Long(continent.toString().trim());
            } else {
                // FIXME use codehous deserializer
//                Continent con = (Continent) JAXBContextHelper.getEntity(Continent.class, continent.toString());
//                ObjectMapper mapper = ResteasyJacksonProvider.locateMapper(Object.class, MediaType.APPLICATION_JSON_TYPE);
//                System.out.println(con.getName());
                
                id = ((Continent)continent).getId();
            }
            
            continent = em.find(Continent.class, id);
            
            predicates.add(cb.equal(country.get(Country_.continent), continent));
        }
        
//
//        Iterator<Object> iterator = parameters.keySet().iterator();
//
//        while (iterator.hasNext())
//        {
//
//            final Object key = iterator.next();
//            final Object value = parameters.get(key);
//
//            if ((key != null) && (value != null))
//            {
//
//                predicates.add(cb.equal(country.get(key.toString()), value));
//            }
//        }
//
        criteria
            .select(country)
            .where(
                    cb.and(predicates.toArray(new Predicate[predicates.size()]))
            )
            .orderBy(
                    cb.asc(country.get(Country_.name))
            );
        
        return em.createQuery(criteria).getResultList();
    }
}
