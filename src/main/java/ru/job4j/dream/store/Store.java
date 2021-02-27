package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Model;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.sql.SQLException;
import java.util.Collection;

public interface Store {
    Collection<Post> findAllPosts() throws SQLException;

    Collection<Candidate> findAllCandidates();

    User findByEmail(String email);

    void save(Model model);

    void save(User user);

    Post findById(int id);

    Candidate findCandidateById(int id);

    void delete(Model model);

    void delete(User user);
}
