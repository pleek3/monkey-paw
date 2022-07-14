package com.minequest.minecraft;

import com.minequest.minecraft.parser.FileParser;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YannicK S. on 14.07.2022
 */
public class Bootstrap {


    /**
     * It creates a table called custom_heads if it doesn't already exist
     */
    private static void createTableIfNotExists() {
        try (Connection connection = dataSource().getConnection(); PreparedStatement statement = connection.prepareStatement(
                "create table if not exists custom_heads(id BIGINT primary key, category text null, texture_url text null, name text null);")) {
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * It takes a list of custom heads, and for each custom head, it inserts it into the database
     *
     * @param heads The list of CustomHeads you want to insert into the database.
     */
    private static void insertCustomHeadsToDatabase(List<CustomHead> heads) {
        heads.forEach(customHead -> {
            try (Connection connection = dataSource().getConnection(); PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO custom_heads(id,category,texture_url, name ) VALUES (?,?,?,?);")) {

                statement.setLong(1, customHead.id);
                statement.setString(2, customHead.category);
                statement.setString(3, customHead.skin);
                statement.setString(4, customHead.name);
                statement.execute();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        });
    }

    /**
     * We create a list of CustomHead objects, parse the file, and then insert the CustomHeads into the database
     */
    public static void main(String[] args) {
        List<CustomHead> heads = new ArrayList<>();
        FileParser parser = new FileParser();
        parser.parseFile().forEach(record -> heads.add(new CustomHead(record.get("category"),
                record.get("skin"),
                Long.parseLong(record.get("id")),
                record.get("name"))));

        createTableIfNotExists();
        insertCustomHeadsToDatabase(heads);
    }

    /**
     * It creates a connection to the database.
     *
     * @return A DataSource object.
     */
    private static DataSource dataSource() {
        MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setServerName("127.0.0.1");
        dataSource.setPortNumber(3300);
        dataSource.setDatabaseName("pvp");
        dataSource.setUser("root");
        dataSource.setPassword("root");
        return dataSource;
    }

    /**
     * It's a class that holds the information of a custom head
     */
    private static class CustomHead {
        private final String category, skin, name;
        private final long id;

        public CustomHead(String category, String skin, long id, String name) {
            this.category = category;
            this.skin = skin;
            this.id = id;
            this.name = name;
        }
    }
}
