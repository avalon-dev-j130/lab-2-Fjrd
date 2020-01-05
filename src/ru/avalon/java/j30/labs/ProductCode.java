package ru.avalon.java.j30.labs;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Класс описывает представление о коде товара и отражает соответствующую 
 * таблицу базы данных Sample (таблица PRODUCT_CODE).
 * 
 * @author Daniel Alpatov <danial.alpatov@gmail.com>
 */
public class ProductCode {

    /**
     * Код товара
     */
    private String code;

    /**
     * Кода скидки
     */
    private char discountCode;

    /**
     * Описание
     */
    private String description;

    /**
     * Основной конструктор типа {@link ProductCode}
     * 
     * @param code код товара
     * @param discountCode код скидки
     * @param description описание 
     */
    public ProductCode(String code, char discountCode, String description) {
        this.code = code;
        this.discountCode = discountCode;
        this.description = description;
    }

    /**
     * Инициализирует объект значениями из переданного {@link ResultSet}
     * 
     * @param set {@link ResultSet}, полученный в результате запроса, 
     * содержащего все поля таблицы PRODUCT_CODE базы данных Sample.
     */
    private ProductCode(ResultSet set) throws SQLException {
        this.code = set.getString("PROD_CODE");
        this.discountCode = set.getString("DISCOUNT_CODE").charAt(0);
        this.description = set.getString("DESCRIPTION");
    }

    /**
     * Возвращает код товара
     * 
     * @return Объект типа {@link String}
     */
    public String getCode() {
        return code;
    }

    /**
     * Устанавливает код товара
     * 
     * @param code код товара
     */
    public void setCode(String code) {
        this.code = code;
    }
    
    /**
     * Возвращает код скидки
     * 
     * @return Объект типа {@link String}
     */
    public char getDiscountCode() {
        return discountCode;
    }

    /**
     * Устанавливает код скидки
     * 
     * @param discountCode код скидки
     */
    public void setDiscountCode(char discountCode) {
        this.discountCode = discountCode;
    }

    /**
     * Возвращает описание
     * 
     * @return Объект типа {@link String}
     */
    public String getDescription() {
        return description;
    }

    /**
     * Устанавливает описание
     * 
     * @param description описание
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Сравнивает некоторый произвольный объект с текущим объектом типа
     * {@link ProductCode}
     *
     * @param o Объект, скоторым сравнивается текущий объект.
     * @return true, если объект obj тождественен текущему объекту. В обратном
     * случае - false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCode that = (ProductCode) o;
        return discountCode == that.discountCode &&
                Objects.equals(code, that.code) &&
                Objects.equals(description, that.description);
    }

    /**
     * Хеш-функция типа {@link ProductCode}.
     *
     * @return Значение хеш-кода объекта типа {@link ProductCode}
     */
    @Override
    public int hashCode() {
        return Objects.hash(code, discountCode, description);
    }

/**
     * Возвращает строковое представление кода товара.
     * 
     * @return Объект типа {@link String}
     */
    @Override
    public String toString() {
        return "ProductCode{" +
                "code='" + code + '\'' +
                ", discountCode=" + discountCode +
                ", description='" + description + '\'' +
                '}';
    }

    /**
     * Возвращает запрос на выбор всех записей из таблицы PRODUCT_CODE 
     * базы данных Sample
     * 
     * @param connection действительное соединение с базой данных
     * @return Запрос в виде объекта класса {@link PreparedStatement}
     */
    public static PreparedStatement getSelectQuery(Connection connection) throws SQLException {
        String query = "SELECT * from PRODUCT_CODE";
        return connection.prepareStatement(query);
    }

    /**
     * Возвращает запрос на добавление записи в таблицу PRODUCT_CODE 
     * базы данных Sample
     * 
     * @param connection действительное соединение с базой данных
     * @return Запрос в виде объекта класса {@link PreparedStatement}
     */
    public static PreparedStatement getInsertQuery(Connection connection) throws SQLException {
        String query = "Insert Into PRODUCT_CODE (PROD_CODE, DISCOUNT_CODE, DESCRIPTION) values (?,?,?)";
        return connection.prepareStatement(query);
    }

    /**
     * Возвращает запрос на обновление значений записи в таблице PRODUCT_CODE 
     * базы данных Sample
     * 
     * @param connection действительное соединение с базой данных
     * @return Запрос в виде объекта класса {@link PreparedStatement}
     */
    public static PreparedStatement getUpdateQuery(Connection connection) throws SQLException {
        String query = "Update PRODUCT_CODE set PROD_CODE = ?, DISCOUNT_CODE = ?, DESCRIPTION = ? where PROD_CODE = ?";
        //String query = "Update PRODUCT_CODE set DISCOUNT_CODE = ?, DESCRIPTION = ? where PROD_CODE=?";
        return connection.prepareStatement(query);
    }

    /**
     * Преобразует {@link ResultSet} в коллекцию объектов типа {@link ProductCode}
     * 
     * @param set {@link ResultSet}, полученный в результате запроса, содержащего 
     * все поля таблицы PRODUCT_CODE базы данных Sample
     * @return Коллекция объектов типа {@link ProductCode}
     * @throws SQLException 
     */
    public static Collection<ProductCode> convert(ResultSet set) throws SQLException {
        Collection<ProductCode> collection = new LinkedList<>();
        while (set.next()) {
            String code = set.getString("PROD_CODE");
            String discountCode = set.getString("DISCOUNT_CODE");
            String description = set.getString("DESCRIPTION");
            collection.add(new ProductCode(code, discountCode.charAt(0), description));
        }
        return new ArrayList<>(collection);
    }

    /**
     * Сохраняет текущий объект в базе данных. 
     * <p>
     * Если запись ещё не существует, то выполняется запрос типа INSERT.
     * <p>
     * Если запись уже существует в базе данных, то выполняется запрос типа UPDATE.
     * 
     * @param connection действительное соединение с базой данных
     */
    public void save(Connection connection) throws SQLException {
        String selectQuery = "SELECT * FROM PRODUCT_CODE WHERE PROD_CODE = '" + code + "' OR DESCRIPTION = '" + description + "' ";
        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(selectQuery)) {
                if (!resultSet.next()) {
                    insertIntoDataBase(connection);
                } else {
                    updateDataBase(connection);
                }
            }
        }
    }

    private void insertIntoDataBase(Connection connection) throws SQLException {
        System.out.println("insert");
        PreparedStatement ps = getInsertQuery(connection);
        ps.setString(1, code);
        ps.setString(2, String.valueOf(discountCode));
        ps.setString(3, description);
        ps.execute();
    }

    private void updateDataBase(Connection connection) throws SQLException {
        System.out.println("update");
        PreparedStatement ps = getUpdateQuery(connection);
        ps.setString(1, String.valueOf(code));
        ps.setString(2, String.valueOf(discountCode));
        ps.setString(3, description);
        ps.setString(4, code);
        ps.executeUpdate();
    }

    /**
     * Возвращает все записи таблицы PRODUCT_CODE в виде коллекции объектов
     * типа {@link ProductCode}
     * 
     * @param connection действительное соединение с базой данных
     * @return коллекция объектов типа {@link ProductCode}
     * @throws SQLException 
     */
    public static Collection<ProductCode> all(Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = getSelectQuery(connection)) {
            try (ResultSet result = preparedStatement.executeQuery()) {
                return convert(result);
            }
        }
    }
}
