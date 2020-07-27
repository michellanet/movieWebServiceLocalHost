/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.controllers;

import com.mycompany.controllers.exceptions.NonexistentEntityException;
import com.mycompany.controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.models.StarActor;
import com.mycompany.models.Videos;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author michellanet
 */
public class VideosJpaController implements Serializable {

    public VideosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Videos videos) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            StarActor starActorFk = videos.getStarActorFk();
            if (starActorFk != null) {
                starActorFk = em.getReference(starActorFk.getClass(), starActorFk.getId());
                videos.setStarActorFk(starActorFk);
            }
            em.persist(videos);
            if (starActorFk != null) {
                starActorFk.getVideosCollection().add(videos);
                starActorFk = em.merge(starActorFk);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findVideos(videos.getId()) != null) {
                throw new PreexistingEntityException("Videos " + videos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Videos videos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Videos persistentVideos = em.find(Videos.class, videos.getId());
            StarActor starActorFkOld = persistentVideos.getStarActorFk();
            StarActor starActorFkNew = videos.getStarActorFk();
            if (starActorFkNew != null) {
                starActorFkNew = em.getReference(starActorFkNew.getClass(), starActorFkNew.getId());
                videos.setStarActorFk(starActorFkNew);
            }
            videos = em.merge(videos);
            if (starActorFkOld != null && !starActorFkOld.equals(starActorFkNew)) {
                starActorFkOld.getVideosCollection().remove(videos);
                starActorFkOld = em.merge(starActorFkOld);
            }
            if (starActorFkNew != null && !starActorFkNew.equals(starActorFkOld)) {
                starActorFkNew.getVideosCollection().add(videos);
                starActorFkNew = em.merge(starActorFkNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = videos.getId();
                if (findVideos(id) == null) {
                    throw new NonexistentEntityException("The videos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Videos videos;
            try {
                videos = em.getReference(Videos.class, id);
                videos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The videos with id " + id + " no longer exists.", enfe);
            }
            StarActor starActorFk = videos.getStarActorFk();
            if (starActorFk != null) {
                starActorFk.getVideosCollection().remove(videos);
                starActorFk = em.merge(starActorFk);
            }
            em.remove(videos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Videos> findVideosEntities() {
        return findVideosEntities(true, -1, -1);
    }

    public List<Videos> findVideosEntities(int maxResults, int firstResult) {
        return findVideosEntities(false, maxResults, firstResult);
    }

    private List<Videos> findVideosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Videos.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Videos findVideos(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Videos.class, id);
        } finally {
            em.close();
        }
    }

    public int getVideosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Videos> rt = cq.from(Videos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
