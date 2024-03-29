package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Model;
import ru.job4j.dream.model.Post;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class PsqlMain {
    public static void main(String[] args) throws SQLException {
        Store store = PsqlStore.instOf();
        store.save(new Post(0, "Java Job"));
        System.out.println("Test function findAllPosts:");
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }
        System.out.println();
        System.out.println("Test function findById:");
        System.out.println("Id = " + store.findById(1).getId());
        System.out.println("Name = " + store.findById(1).getName());
        System.out.println();
        System.out.println("Test function update for Post:");
        store.save(new Post(1, "Scala Job"));
        System.out.println("Id = " + store.findById(1).getId());
        System.out.println("Name = " + store.findById(1).getName());
        System.out.println();
        System.out.println("Test function findAllCandidates");
        store.save(new Candidate(0, "Mike", "TestPhoto","TestCity"));
        for (Candidate candidate : store.findAllCandidates()) {
            System.out.println(candidate.getId() + " " + candidate.getName());
        }
        System.out.println();
        System.out.println("Test function update for Candidates:");
        store.save(new Candidate(1, "Donald", "TestPhoto","TestCity"));
        System.out.println("Id = " + store.findCandidateById(1).getId());
        System.out.println("Name = " + store.findCandidateById(1).getName());
    }
}