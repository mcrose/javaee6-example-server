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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import py.org.icarusdb.example.server.model.Continent;
import py.org.icarusdb.example.server.model.Continent_;

@ApplicationScoped
public class ContinentRepository
{

    @Inject
    private EntityManager em;

    public Continent findById(Long id)
    {
        return em.find(Continent.class, id);
    }

    public Continent findByName(String name)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Continent> criteria = cb.createQuery(Continent.class);
        Root<Continent> continent = criteria.from(Continent.class);
        criteria.select(continent).where(cb.equal(continent.get(Continent_.name), name));
        
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Continent> findAllOrderedByName()
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Continent> criteria = cb.createQuery(Continent.class);
        Root<Continent> continent = criteria.from(Continent.class);
        criteria.select(continent).orderBy(cb.asc(continent.get(Continent_.name)));
        
        return em.createQuery(criteria).getResultList();
    }
}
