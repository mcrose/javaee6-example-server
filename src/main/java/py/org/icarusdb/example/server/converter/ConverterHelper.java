/**
 * Copyright 2014 Roberto Gamarra [icarus] ** ( Betto McRose  )
 *                mcrose@icarusdb.com.py | mcrose.dev@gmail.com
 *                
 * as you wish... at your service ;-P
 * 
 * IcarusDB.com.py
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
package py.org.icarusdb.example.server.converter;

import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import py.org.icarusdb.example.server.dto.CityDTO;
import py.org.icarusdb.example.server.dto.ContinentDTO;
import py.org.icarusdb.example.server.dto.CountryDTO;
import py.org.icarusdb.example.server.dto.StateDTO;
import py.org.icarusdb.example.server.model.City;
import py.org.icarusdb.example.server.model.Continent;
import py.org.icarusdb.example.server.model.Country;
import py.org.icarusdb.example.server.model.State;

/**
 * @author Betto McRose [icarus]
 *         mcrose@icarusdb.com.py
 *         mcrose.dev@gmail.com
 *
 */
@Stateless
public class ConverterHelper
{
    @Inject
    private EntityManager em;


    public List<ContinentDTO> convertContinentsToDTO(List<Continent> entities)
    {
        List<ContinentDTO> dtos = new LinkedList<ContinentDTO>();
        
        for(int index=0; index < entities.size(); index++)
        {
            dtos.add(new ContinentDTO(entities.get(index)));
        }
        return dtos;
    }

    public List<CountryDTO> convertCountriesToDTO(List<Country> entities)
    {
        List<CountryDTO> dtos = new LinkedList<CountryDTO>();
        
        for(int index=0; index < entities.size(); index++)
        {
            dtos.add(new CountryDTO(em.merge(entities.get(index))));
        }
        return dtos;
    }

    public List<StateDTO> convertStatesToDTO(List<State> entities)
    {
        List<StateDTO> dtos = new LinkedList<StateDTO>();
        
        for(int index=0; index < entities.size(); index++)
        {
            dtos.add(new StateDTO(em.merge(entities.get(index))));
        }
        return dtos;
    }

    public List<CityDTO> convertCitiesToDTO(List<City> entities)
    {
        List<CityDTO> dtos = new LinkedList<CityDTO>();
        
        for(int index=0; index < entities.size(); index++)
        {
            dtos.add(new CityDTO(em.merge(entities.get(index))));
        }
        return dtos;
    }

}
