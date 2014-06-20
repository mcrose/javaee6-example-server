package py.org.icarusdb.example.server.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-06-20T10:21:47.520-0400")
@StaticMetamodel(Neighborhood.class)
public class Neighborhood_ {
	public static volatile SingularAttribute<Neighborhood, Long> id;
	public static volatile SingularAttribute<Neighborhood, City> city;
	public static volatile SingularAttribute<Neighborhood, String> name;
	public static volatile SingularAttribute<Neighborhood, Boolean> active;
}
