/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.controllers;

import com.mycompany.controllers.exceptions.IllegalOrphanException;
import com.mycompany.controllers.exceptions.NonexistentEntityException;
import com.mycompany.controllers.exceptions.PreexistingEntityException;
import com.mycompany.models.StarActor;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.models.Videos;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author michellanet
 */
public class StarActorJpaController implements Serializable {

    public StarActorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(StarActor starActor) throws PreexistingEntityException, Exception {
        if (starActor.getVideosCollection() == null) {
            starActor.setVideosCollection(new ArrayList<Videos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Videos> attachedVideosCollection = new ArrayList<Videos>();
            for (Videos videosCollectionVideosToAttach : starActor.getVideosCollection()) {
                videosCollectionVideosToAttach = em.getReference(videosCollectionVideosToAttach.getClass(), videosCollectionVideosToAttach.getId());
                attachedVideosCollection.add(videosCollectionVideosToAttach);
            }
            starActor.setVideosCollection(attachedVideosCollection);
            em.persist(starActor);
            for (Videos videosCollectionVideos : starActor.getVideosCollection()) {
                StarActor oldStarActorFkOfVideosCollectionVideos = videosCollectionVideos.getStarActorFk();
                videosCollectionVideos.setStarActorFk(starActor);
                videosCollectionVideos = em.merge(videosCollectionVideos);
                if (oldStarActorFkOfVideosCollectionVideos != null) {
                    oldStarActorFkOfVideosCollectionVideos.getVideosCollection().remove(videosCollectionVideos);
                    oldStarActorFkOfVideosCollectionVideos = em.merge(oldStarActorFkOfVideosCollectionVideos);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findStarActor(starActor.getId()) != null) {
                throw new PreexistingEntityException("StarActor " + starActor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(StarActor starActor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            StarActor persistentStarActor = em.find(StarActor.class, starActor.getId());
            Collection<Videos> videosCollectionOld = persistentStarActor.getVideosCollection();
            Collection<Videos> videosCollectionNew = starActor.getVideosCollection();
            List<String> illegalOrphanMessages = null;
            for (Videos videosCollectionOldVideos : videosCollectionOld) {
                if (!videosCollectionNew.contains(videosCollectionOldVideos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Videos " + videosCollectionOldVideos + " since its starActorFk field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Videos> attachedVideosCollectionNew = new ArrayList<Videos>();
            for (Videos videosCollectionNewVideosToAttach : videosCollectionNew) {
                videosCollectionNewVideosToAttach = em.getReference(videosCollectionNewVideosToAttach.getClass(), videosCollectionNewVideosToAttach.getId());
                attachedVideosCollectionNew.add(videosCollectionNewVideosToAttach);
            }
            videosCollectionNew = attachedVideosCollectionNew;
            starActor.setVideosCollection(videosCollectionNew);
            starActor = em.merge(starActor);
            for (Videos videosCollectionNewVideos : videosCollectionNew) {
                if (!videosCollectionOld.contains(videosCollectionNewVideos)) {
                    StarActor oldStarActorFkOfVideosCollectionNewVideos = videosCollectionNewVideos.getStarActorFk();
                    videosCollectionNewVideos.setStarActorFk(starActor);
                    videosCollectionNewVideos = em.merge(videosCollectionNewVideos);
                    if (oldStarActorFkOfVideosCollectionNewVideos != null && !oldStarActorFkOfVideosCollectionNewVideos.equals(starActor)) {
                        oldStarActorFkOfVideosCollectionNewVideos.getVideosCollection().remove(videosCollectionNewVideos);
                        oldStarActorFkOfVideosCollectionNewVideos = em.merge(oldStarActorFkOfVideosCollectionNewVideos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = starActor.getId();
                if (findStarActor(id) == null) {
                    throw new NonexistentEntityException("The starActor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            StarActor starActor;
            try {
                starActor = em.getReference(StarActor.class, id);
                starActor.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The starActor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Videos> videosCollectionOrphanCheck = starActor.getVideosCollection();
            for (Videos videosCollectionOrphanCheckVideos : videosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This StarActor (" + starActor + ") cannot be destroyed since the Videos " + videosCollectionOrphanCheckVideos + " in its videosCollection field has a non-nullable starActorFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(starActor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<StarActor> findStarActorEntities() {
        return findStarActorEntities(true, -1, -1);
    }

    public List<StarActor> findStarActorEntities(int maxResults, int firstResult) {
        return findStarActorEntities(false, maxResults, firstResult);
    }

    private List<StarActor> findStarActorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(StarActor.class));
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

    public StarActor findStarActor(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(StarActor.class, id);
        } finally {
            em.close();
        }
    }

    public int getStarActorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<StarActor> rt = cq.from(StarActor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
