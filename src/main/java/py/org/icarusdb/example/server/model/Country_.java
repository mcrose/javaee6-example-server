package py.org.icarusdb.example.server.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-06-20T10:21:47.489-0400")
@StaticMetamodel(Country.class)
public class Country_ {
	public static volatile SingularAttribute<Country, Long> id;
	public static volatile SingularAttribute<Country, Continent> continent;
	public static volatile SingularAttribute<Country, String> name;
	public static volatile SingularAttribute<Country, Boolean> active;
	public static volatile SetAttribute<Country, State> states;
}
