package com.ilyabuglakov.raise.model.service.validator;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The type Result set validator.
 */
public class ResultSetValidator {

    /**
     * Has all values boolean.
     *
     * @param resultSet the result set
     * @param values    the values
     * @return the boolean
     * @throws SQLException the sql exception
     */
    public boolean hasAllValues(ResultSet resultSet, String... values) throws SQLException {
        for (String value : values) {
            resultSet.getObject(value);
            if (resultSet.wasNull())
                return false;
        }
        return true;
    }

}
