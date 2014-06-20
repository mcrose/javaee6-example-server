package py.org.icarusdb.example.server.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-06-20T10:21:47.187-0400")
@StaticMetamodel(City.class)
public class City_ {
	public static volatile SingularAttribute<City, Long> id;
	public static volatile SingularAttribute<City, State> state;
	public static volatile SingularAttribute<City, String> name;
	public static volatile SingularAttribute<City, Boolean> active;
	public static volatile SetAttribute<City, Neighborhood> neighborhoods;
}
