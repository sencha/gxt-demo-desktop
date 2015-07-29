package com.sencha.gxt.examples.resources.server.data;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sencha.gxt.examples.resources.server.EntityManagerUtil;


public class MusicTest {
  private static EntityManager em;
  private EntityTransaction tx;

  @BeforeClass
  public static void setupEm() {
    em = EntityManagerUtil.getEntityManager();
  }

  @Before
  public void setupTx() {
    tx = em.getTransaction();
    tx.begin();
  }

  @After
  public void teardownTx() {
    tx.rollback();
  }

  @Test
  public void testCreatePersist() {
    Music m1 = new Music();
    m1.setAuthor("asdf");
    m1.setGenre("fdsa");
    Music m2 = new Music();
    m2.setAuthor("1234");
    m2.setGenre("4321");

    m1 = m1.persist();
    m2 = m2.persist();

    Assert.assertTrue(m1.getId() > 0);
    Assert.assertNotSame(m1.getId(), m2.getId());
  }

  @Test
  public void testModifyPersist() {
    Music m = new Music();
    m.setAuthor("asdf");
    m.setGenre("asdffdsa");
    m.setName("gfweg");

    m = m.persist();
    em.flush();

    Integer id = m.getId();
    Music m2 = em.find(Music.class, id);
    m2.setAuthor("1234");

    Music mSaved = m2.persist();
    Assert.assertEquals("1234", mSaved.getAuthor());
    Integer idSaved = mSaved.getId();
    Assert.assertEquals(id, idSaved);
  }
}
