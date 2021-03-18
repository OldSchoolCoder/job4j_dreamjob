package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Model;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;
import ru.job4j.dream.store.Store;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PsqlStoreStub implements Store {
    private final static AtomicInteger POST_ID = new AtomicInteger();
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    @Override
    public Collection<Post> findAllPosts() throws SQLException {
        return new ArrayList<Post>(posts.values());
    }

    @Override
    public Collection<Candidate> findAllCandidates() throws SQLException {
        return null;
    }

    @Override
    public User findByEmail(String email) throws SQLException {
        return null;
    }

    @Override
    public void save(Model post) throws SQLException {
        if (post.getId() == 0) {
            post.setId(POST_ID.incrementAndGet());
        }
        posts.put(post.getId(), (Post) post);
    }

    @Override
    public void save(User user) throws SQLException {

    }

    @Override
    public Post findById(int id) throws SQLException {
        return posts.get(id);
    }

    @Override
    public Candidate findCandidateById(int id) throws SQLException {
        return null;
    }

    @Override
    public void delete(Model model) throws SQLException {

    }

    @Override
    public void delete(User user) throws SQLException {

    }
}
