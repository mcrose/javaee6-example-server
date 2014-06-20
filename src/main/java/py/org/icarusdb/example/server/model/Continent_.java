package py.org.icarusdb.example.server.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-06-20T10:21:47.431-0400")
@StaticMetamodel(Continent.class)
public class Continent_ {
	public static volatile SingularAttribute<Continent, Long> id;
	public static volatile SingularAttribute<Continent, String> name;
	public static volatile SingularAttribute<Continent, Boolean> active;
	public static volatile SetAttribute<Continent, Country> countries;
}
