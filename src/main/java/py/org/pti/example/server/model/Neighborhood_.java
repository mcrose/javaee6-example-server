package py.org.pti.example.server.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-06-18T13:03:47.412-0400")
@StaticMetamodel(Neighborhood.class)
public class Neighborhood_ {
	public static volatile SingularAttribute<Neighborhood, Long> id;
	public static volatile SingularAttribute<Neighborhood, City> city;
	public static volatile SingularAttribute<Neighborhood, String> name;
	public static volatile SingularAttribute<Neighborhood, Boolean> active;
}
