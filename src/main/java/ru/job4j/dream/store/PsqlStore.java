package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Model;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.io.IOException;
import java.sql.*;
import java.util.logging.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {

    private final BasicDataSource pool = new BasicDataSource();
    private static final Logger LOGGER = Logger.getLogger(PsqlStore.class.getName());

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error! IOException!", e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error! ClassNotFoundException!", e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<String> getCityList() throws SQLException {
        List<String> cityList = new ArrayList<>();
        Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement("SELECT * FROM city_id");
        ResultSet it = ps.executeQuery();
        while (it.next()) {
            cityList.add(it.getString("city"));
        }
        cn.close();
        ps.close();
        it.close();
        return cityList;
    }

    @Override
    public Collection<Post> findAllPosts() throws SQLException {
        List<Post> posts = new ArrayList<>();
        Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement("SELECT * FROM post");
        ResultSet it = ps.executeQuery();
        while (it.next()) {
            posts.add(new Post(it.getInt("id"), it.getString("name")));
        }
        cn.close();
        ps.close();
        it.close();
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() throws SQLException {
        List<Candidate> candidates = new ArrayList<>();
        Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate LEFT JOIN photo ON candidate.id=photo.id");
        ResultSet it = ps.executeQuery();
        while (it.next()) {
            candidates.add(new Candidate(it.getInt(1), it.getString(2), it.getString(5), it.getString(3)));
        }
        cn.close();
        ps.close();
        it.close();
        return candidates;
    }

    @Override
    public User findByEmail(String email) throws SQLException {
        User resultUser = new User();
        Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement("SELECT * FROM job_user WHERE email=?");
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            resultUser.setId(rs.getInt("id"));
            resultUser.setName(rs.getString("name"));
            resultUser.setEmail(rs.getString("email"));
            resultUser.setPassword(rs.getString("password"));
        } else {
            throw new SQLException("Error! Can't find data in storage");
        }
        cn.close();
        ps.close();
        rs.close();
        return resultUser;
    }

    public void save(Model model) throws SQLException {
        if (model.getId() == 0) {
            create(model);
        } else {
            update(model);
        }
    }

    @Override
    public void save(User user) throws SQLException {
        if (user.getId() == 0) {
            create(user);
        } else {
            update(user);
        }
    }

    private void create(User user) throws SQLException {
        Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement("INSERT INTO job_user(name, email, password) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, user.getName());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getPassword());
        ResultSet rs = ps.getGeneratedKeys();
        ps.executeUpdate();
        if (rs.next()) {
            user.setId(rs.getInt(1));
        }
        cn.close();
        ps.close();
        rs.close();
    }

    private void update(User user) throws SQLException {
        Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement("UPDATE job_user SET name=?, email=?, password=? WHERE id=?");
        ps.setString(1, user.getName());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getPassword());
        ps.setInt(4, user.getId());
        ps.execute();
        cn.close();
        ps.close();
    }

    private Model create(Model model) throws SQLException {
        final String tableName = model.getClass().getSimpleName().toLowerCase();
        Connection cn = pool.getConnection();
        PreparedStatement ps = null;
        if (model.getCity() == null) {
            ps = cn.prepareStatement("INSERT INTO " + tableName + "(name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, model.getName());
        } else {
            ps = cn.prepareStatement("INSERT INTO " + tableName + "(name, cityid) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, model.getName());
            ps.setString(2, model.getCity());
        }
        ResultSet rs = ps.getGeneratedKeys();
        ps.executeUpdate();
        if (rs.next()) {
            model.setId(rs.getInt(1));
        }
        cn.close();
        ps.close();
        rs.close();
        return model;
    }

    private void update(Model model) throws SQLException {
        final String tableName = model.getClass().getSimpleName().toLowerCase();
        Connection cn = pool.getConnection();
        PreparedStatement ps = null;
        if (model.getCity() == null) {
            ps = cn.prepareStatement("UPDATE " + tableName + " SET name=? WHERE id=?");
            ps.setString(1, model.getName());
            ps.setInt(2, model.getId());
        } else {
            ps = cn.prepareStatement("UPDATE " + tableName + " SET name=?, cityid=? WHERE id=?");
            ps.setString(1, model.getName());
            ps.setString(2, model.getCity());
            ps.setInt(3, model.getId());
        }
        ps.execute();
        cn.close();
        ps.close();
    }

    @Override
    public Post findById(int id) throws SQLException {
        Post resultPost = new Post(0, "");
        Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement("SELECT * FROM post WHERE id=?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            resultPost.setId(rs.getInt("id"));
            resultPost.setName(rs.getString("name"));
        } else {
            throw new SQLException("Error! Can't find data in storage");
        }
        cn.close();
        ps.close();
        rs.close();
        return resultPost;
    }

    @Override
    public Candidate findCandidateById(int id) throws SQLException {
        Candidate result = new Candidate(0, "", "TestPhoto", "");
        Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate LEFT JOIN photo ON candidate.id=photo.id where candidate.id=?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            result.setId(rs.getInt("id"));
            result.setName(rs.getString("name"));
            result.setPhoto(rs.getString(4));
        } else {
            throw new SQLException("Error! Can't find data in storage");
        }
        cn.close();
        ps.close();
        rs.close();
        return result;
    }

    @Override
    public void delete(Model model) throws SQLException {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM candidate where id=?; DELETE FROM photo where id=?")
        ) {
            ps.setInt(1, model.getId());
            ps.setInt(2, model.getId());
            ps.execute();
        }
    }

    @Override
    public void delete(User user) throws SQLException {
        Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement("DELETE FROM job_user where id=?");
        ps.setInt(1, user.getId());
        ps.execute();
        cn.close();
        ps.close();
    }

}
