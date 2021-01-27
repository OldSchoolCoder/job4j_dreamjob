package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Model;
import ru.job4j.dream.model.Post;

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
             //PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate")
             //PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate INNER JOIN photo ON id=id")
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate INNER JOIN photo ON candidate.id=photo.id")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    //candidates.add(new Candidate(it.getInt("id"), it.getString("name")));zebra.jpeg
                    //candidates.add(new Candidate(it.getInt("id"), it.getString("name"),"zebra.jpeg"));
                    //candidates.add(new Candidate(it.getInt("candidate.id"), it.getString("candidate.name"),"zebra.jpeg"));
                    //candidates.add(new Candidate(it.getInt(1), it.getString(2),"zebra.jpeg"));
                    candidates.add(new Candidate(it.getInt(1), it.getString(2),it.getString(4)));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error! SQLException!", e);
        }
        return candidates;
    }

    public void save(Model model) {
        if (model.getId() == 0) {
            create(model);
        } else {
            update(model);
        }
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
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate WHERE id=?")
        ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result.setId(rs.getInt("id"));
                    result.setName(rs.getString("name"));
                } else {
                    LOGGER.warning("Error! Can't find data in storage");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error! SQLException!", e);
        }
        return result;
    }
}
