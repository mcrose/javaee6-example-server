package py.org.icarusdb.example.server.model;

// Generated 18/06/2014 08:18:20 AM by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Neighborhoods generated by hbm2java
 */
@Entity
@Table(name = "neighborhoods", schema = "public")
public class Neighborhood implements java.io.Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -1902290693710575402L;
    
    private Long id;
    private City city;
    private String name;
    private Boolean active;

    public Neighborhood()
    {
    }

    public Neighborhood(City city)
    {
        this.city = city;
    }

    private final static String SEQUENCE_GENERATOR = "NEIGHBORHOODS_SEQUENCE_GENERATOR";

    @Id
    @SequenceGenerator(name = SEQUENCE_GENERATOR, sequenceName = "neighborhoods_id_seq", allocationSize = 1)
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
    @JoinColumn(name = "city_id", nullable = false)
    public City getCity()
    {
        return this.city;
    }

    public void setCity(City cities)
    {
        this.city = cities;
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

}
