package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.WrongTypeException;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Model;
import ru.job4j.dream.model.Post;

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

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
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
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        try (Connection cn = pool.getConnection()) {
            var ps = cn.prepareStatement("INSERT INTO post(name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS);
            try {
                if (model instanceof Candidate) {
                    ps = cn.prepareStatement("INSERT INTO candidate(name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS);
                } else if (!(model instanceof Post)) {
                    throw new WrongTypeException("Error! Wrong instance type!");
                }
                ps.setString(1, model.getName());
                ps.execute();
                try (ResultSet id = ps.getGeneratedKeys()) {
                    if (id.next()) {
                        model.setId(id.getInt(1));
                    }
                }
            } finally {
                ps.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    private void update(Model model) {
        try (Connection cn = pool.getConnection()) {
            var ps = cn.prepareStatement("UPDATE post SET name=? WHERE id=?");
            try {
                if (model instanceof Candidate) {
                    ps = cn.prepareStatement("UPDATE candidate SET name=? WHERE id=?");
                } else if (!(model instanceof Post)) {
                    throw new WrongTypeException("Error! Wrong instance type!");
                }
                ps.setString(1, model.getName());
                ps.setInt(2, model.getId());
                ps.execute();
            } finally {
                ps.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                while (rs.next()) {
                    resultPost.setId(rs.getInt("id"));
                    resultPost.setName(rs.getString("name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultPost;
    }
}
