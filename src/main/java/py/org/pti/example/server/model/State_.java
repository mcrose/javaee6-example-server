package py.org.pti.example.server.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-06-18T08:30:14.654-0400")
@StaticMetamodel(State.class)
public class State_ {
	public static volatile SingularAttribute<State, Long> id;
	public static volatile SingularAttribute<State, Country> country;
	public static volatile SingularAttribute<State, String> name;
	public static volatile SingularAttribute<State, Boolean> active;
	public static volatile SetAttribute<State, City> cities;
}
