package py.org.pti.example.server.model;

// Generated 18/06/2014 08:18:20 AM by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Cities generated by hbm2java
 */
@Entity
@Table(name = "cities", schema = "public")
public class City implements java.io.Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -2325091694716940868L;
    
    private Long id;
    private State state;
    private String name;
    private Boolean active;
    private Set<Neighborhood> neighborhoods = new HashSet<Neighborhood>(0);

    public City()
    {
    }

    public City(State state)
    {
        this.state = state;
    }

    private final static String SEQUENCE_GENERATOR = "CITIES_SEQUENCE_GENERATOR";

    @Id
    @SequenceGenerator(name = SEQUENCE_GENERATOR, sequenceName = "cities_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_GENERATOR)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id", nullable = false)
    public State getState()
    {
        return this.state;
    }

    public void setState(State state)
    {
        this.state = state;
    }

    @Column(name = "name", nullable = false, length = 150)
    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Column(name = "active")
    public Boolean getActive()
    {
        return this.active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "city")
    public Set<Neighborhood> getNeighborhoods()
    {
        return this.neighborhoods;
    }

    public void setNeighborhoods(Set<Neighborhood> neighborhoodses)
    {
        this.neighborhoods = neighborhoodses;
    }

}
