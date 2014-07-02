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
package py.org.icarusdb.example.server.dto;

import py.org.icarusdb.example.server.model.City;

/**
 * @author Betto McRose [icarus]
 *         mcrose@icarusdb.com.py
 *         mcrose.dev@gmail.com
 *
 */
public class CityDTO
{
    private Long id;
    private StateDTO stateDTO;
    private String name;
    private Boolean active;
//    private Set<Neighborhood> neighborhoods = new HashSet<Neighborhood>(0);

    public CityDTO(){}

    public CityDTO(StateDTO state)
    {
        this.stateDTO = state;
    }
    
    public CityDTO(City entity)
    {
        this.id = entity.getId();
        this.name = entity.getName();
        this.active = entity.getActive();
        /* State is a NOT NULL so we don't check for null vales */
        this.stateDTO = new StateDTO(entity.getState());
    }

    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public StateDTO getStateDTO()
    {
        return this.stateDTO;
    }

    public void setStateDTO(StateDTO stateDTO)
    {
        this.stateDTO = stateDTO;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Boolean getActive()
    {
        return this.active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }

    public City toEntity()
    {
        City entity = new City();
        entity.setId(this.getId());
        entity.setName(this.getName());
        entity.setActive(this.getActive());
        
        entity.setState(this.getStateDTO().toEntity());
        
        return entity;
    }

    //    public Set<Neighborhood> getNeighborhoods()
//    {
//        return this.neighborhoods;
//    }
//
//    public void setNeighborhoods(Set<Neighborhood> neighborhoodses)
//    {
//        this.neighborhoods = neighborhoodses;
//    }

}
