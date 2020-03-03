package org.corgi.jpastudy.example.inheritance;

import org.corgi.jpastudy.example.inheritance.joined.domain.Movie;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class InheritanceMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Movie movie = new Movie();
            movie.setDirector("directorA");
            movie.setActor("actorA");
            movie.setName("movieA");
            movie.setPrice(10000);

            em.persist(movie);

            em.flush();
            em.clear();

            Movie findMovie = em.find(Movie.class, movie.getId());
            System.out.println("findMove = " + findMovie);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
