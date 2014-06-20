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
package py.org.pti.example.server.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import py.org.pti.example.server.model.Country;
import py.org.pti.example.server.model.Country_;

@ApplicationScoped
public class CountryRepository
{

    @Inject
    private EntityManager em;

    public Country findById(Long id)
    {
        return em.find(Country.class, id);
    }

    public Country findByName(String name)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Country> criteria = cb.createQuery(Country.class);
        Root<Country> country = criteria.from(Country.class);
        criteria.select(country).where(cb.equal(country.get(Country_.name), name));
        
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Country> findAllOrderedByName()
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Country> criteria = cb.createQuery(Country.class);
        Root<Country> country = criteria.from(Country.class);
        criteria.select(country).orderBy(cb.asc(country.get(Country_.name)));
        
        return em.createQuery(criteria).getResultList();
    }
}
