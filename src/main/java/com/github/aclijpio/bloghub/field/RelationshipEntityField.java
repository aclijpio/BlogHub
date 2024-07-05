package com.github.aclijpio.bloghub.field;

import com.github.aclijpio.bloghub.utils.ConnectionPool;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RelationshipEntityField implements EntityField {
    private final String columnName;
    private final RelationshipType type;

    public RelationshipEntityField(Relationship relationship) {
        this.columnName = relationship.mapperBy();
        this.type = relationship.value();
    }


    @Override
    public String createQuery() {
        String sqlQuery = "SELECT * FROM User WHERE id = ?";

        try (PreparedStatement statement = ConnectionPool.UTIL.getConnection().prepareStatement(sqlQuery)) {
            statement.setInt(1, 1);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                System.out.println("Имя пользователя: " + name);
                // Дополнительная обработка результата запроса
            }
        } catch (SQLException e) {
            System.err.println("Ошибка выполнения запроса.");
            e.printStackTrace();
        }
        return "";
    }
}
