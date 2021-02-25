package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Model;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.util.logging.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error! IllegalStateException!", e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error! IllegalStateException!", e);
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
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error! SQLException!", e);
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate LEFT JOIN photo ON candidate.id=photo.id")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(it.getInt(1), it.getString(2), it.getString(4)));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error! SQLException!", e);
        }
        return candidates;
    }

    @Override
    public User findByEmail(String email) {
        User resultUser = new User();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM job_user WHERE email=?")
        ) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    resultUser.setId(rs.getInt("id"));
                    resultUser.setName(rs.getString("name"));
                    resultUser.setEmail(rs.getString("email"));
                    resultUser.setPassword(rs.getString("password"));
                } else {
                    LOGGER.warning("Error! Can't find data in storage");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error! SQLException!", e);
        }
        return resultUser;
    }

    public void save(Model model) {
        if (model.getId() == 0) {
            create(model);
        } else {
            update(model);
        }
    }

    @Override
    public void save(User user) {
        if (user.getId() == 0) {
            create(user);
        } else {
            update(user);
        }
    }

    private void create(User user) {
        try (Connection cn = pool.getConnection();
             //PreparedStatement ps = cn.prepareStatement("INSERT INTO user(name, email, password) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)
             //PreparedStatement ps = cn.prepareStatement("INSERT INTO user VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)
             PreparedStatement ps = cn.prepareStatement("INSERT INTO user(name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getName());
            //ps.setString(2, user.getEmail());
            //ps.setString(3, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error! SQLException!", e);
        }
        LOGGER.warning("Hello from create User! Log");
        System.out.println("Hello from create User!");
    }

    private Model create(Model model) {
        final String tableName = model.getClass().getSimpleName().toLowerCase();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO " + tableName + "(name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, model.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    model.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error! SQLException!", e);
        }
        return model;
    }

    private void update(Model model) {
        final String tableName = model.getClass().getSimpleName().toLowerCase();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE " + tableName + " SET name=? WHERE id=?")
        ) {
            ps.setString(1, model.getName());
            ps.setInt(2, model.getId());
            ps.execute();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error! SQLException!", e);
        }
    }

    @Override
    public Post findById(int id) {
        Post resultPost = new Post(0, "");
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post WHERE id=?")
        ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    resultPost.setId(rs.getInt("id"));
                    resultPost.setName(rs.getString("name"));
                } else {
                    LOGGER.warning("Error! Can't find data in storage");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error! SQLException!", e);
        }
        return resultPost;
    }

    @Override
    public Candidate findCandidateById(int id) {
        Candidate result = new Candidate(0, "", "TestPhoto");
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate LEFT JOIN photo ON candidate.id=photo.id where candidate.id=?")
        ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result.setId(rs.getInt("id"));
                    result.setName(rs.getString("name"));
                    result.setPhoto(rs.getString(4));
                } else {
                    LOGGER.warning("Error! Can't find data in storage");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error! SQLException!", e);
        }
        return result;
    }

    @Override
    public void delete(Model model) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM candidate where id=?; DELETE FROM photo where id=?")
        ) {
            ps.setInt(1, model.getId());
            ps.setInt(2, model.getId());
            ps.execute();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error! SQLException!", e);
        }
    }

    @Override
    public void delete(User user) {

    }
}
