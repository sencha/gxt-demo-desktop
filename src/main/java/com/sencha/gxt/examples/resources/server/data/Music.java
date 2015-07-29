package com.sencha.gxt.examples.resources.server.data;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import com.sencha.gxt.examples.resources.server.EntityManagerUtil;

@Entity
public class Music {

  public static final EntityManager entityManager() {
    return EntityManagerUtil.getEntityManager();
  }

  public static Music findMusic(Integer id) {
    if (id == null) {
      return null;
    }
    EntityManager em = entityManager();
    return em.find(Music.class, id);
  }

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Integer id;

  @Version
  private Integer version;

  @Size(min = 1, max = 100)
  private String name;

  @Size(min = 1,max = 100)
  private String author;

  private String genre;


  public String getAuthor() {
    return author;
  }

  public String getGenre() {
    return genre;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Integer getVersion() {
    return version;
  }

  public Music persist() {
    EntityManager em = entityManager();

    EntityTransaction tx = em.getTransaction();
    if (tx.isActive()) {
      return em.merge(this);
    } else {
      tx.begin();
      Music saved = em.merge(this);
      tx.commit();
      return saved;
    }
  }

  public void remove() {
    EntityManager em = entityManager();
    em.remove(this);

  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

}
