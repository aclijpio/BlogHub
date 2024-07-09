package com.github.aclijpio.bloghub.database.field.entity;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EntityPool {

    private final Set<Line> lines = new HashSet<>();


    public void addLine(Line line) {
        lines.add(line);
    }
    public Set<Line> getLine(){
        return this.lines;
    }


    public static EntityPool create(EntityInfo entityInfo, Object object) {
        EntityPool entityPool = new EntityPool();
        Map<String, EntityField> columns = entityInfo.getColumnNames();

        Field [] fields = object.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                String fieldName = field.getName();
                if (columns.containsKey(fieldName )) {
                    field.setAccessible(true);

                    String columnName = columns.get(fieldName).getColumnName();
                    Object value = field.get(object);

                    Class<?> fieldType = field.getType();
                    field.setAccessible(false);

                    Line line = new Line(
                            columnName,
                            value,
                            fieldType
                    );
                    entityPool.addLine(line);
                }

            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    return entityPool;
    }

    public record Line(String columnName, Object value, Class<?> type) {


        @Override
        public String toString() {
            return "Line{" +
                    "columnName='" + columnName + '\'' +
                    ", value=" + value +
                    ", type=" + type +
                    '}';
        }
    }


}
