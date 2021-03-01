package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Model;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.sql.SQLException;
import java.util.Collection;

public interface Store {
    Collection<Post> findAllPosts() throws SQLException;

    Collection<Candidate> findAllCandidates() throws SQLException;

    User findByEmail(String email) throws SQLException;

    void save(Model model) throws SQLException;

    void save(User user) throws SQLException;

    Post findById(int id) throws SQLException;

    Candidate findCandidateById(int id) throws SQLException;

    void delete(Model model) throws SQLException;

    void delete(User user) throws SQLException;
}
