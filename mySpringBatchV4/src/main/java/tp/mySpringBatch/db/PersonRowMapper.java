package tp.mySpringBatch.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import tp.mySpringBatch.model.Person;

public class PersonRowMapper implements RowMapper<Person> {

	public static final String ID_COLUMN = "id";
    public static final String FIRSTNAME_COLUMN = "first_name";
    public static final String LASTNAME_COLUMN = "last_name";
    public static final String AGE_COLUMN = "age";
    public static final String ACTIVE_COLUMN = "is_active";

    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        Person person = new Person();
        person.setId(rs.getLong(ID_COLUMN));
        person.setFirstName(rs.getString(FIRSTNAME_COLUMN));
        person.setLastName(rs.getString(LASTNAME_COLUMN));
        person.setAge(rs.getInt(AGE_COLUMN));
        person.setActive(rs.getBoolean(ACTIVE_COLUMN));

        return person;
    }
}
