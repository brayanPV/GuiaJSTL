/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.NonexistentEntityException;
import DTO.Mensaje;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DTO.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author stive
 */
public class MensajeJpaController implements Serializable {

    public MensajeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Mensaje mensaje) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario = mensaje.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getUsuario());
                mensaje.setUsuario(usuario);
            }
            em.persist(mensaje);
            if (usuario != null) {
                usuario.getMensajeList().add(mensaje);
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Mensaje mensaje) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Mensaje persistentMensaje = em.find(Mensaje.class, mensaje.getId());
            Usuario usuarioOld = persistentMensaje.getUsuario();
            Usuario usuarioNew = mensaje.getUsuario();
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getUsuario());
                mensaje.setUsuario(usuarioNew);
            }
            mensaje = em.merge(mensaje);
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getMensajeList().remove(mensaje);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getMensajeList().add(mensaje);
                usuarioNew = em.merge(usuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = mensaje.getId();
                if (findMensaje(id) == null) {
                    throw new NonexistentEntityException("The mensaje with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Mensaje mensaje;
            try {
                mensaje = em.getReference(Mensaje.class, id);
                mensaje.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mensaje with id " + id + " no longer exists.", enfe);
            }
            Usuario usuario = mensaje.getUsuario();
            if (usuario != null) {
                usuario.getMensajeList().remove(mensaje);
                usuario = em.merge(usuario);
            }
            em.remove(mensaje);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Mensaje> findMensajeEntities() {
        return findMensajeEntities(true, -1, -1);
    }

    public List<Mensaje> findMensajeEntities(int maxResults, int firstResult) {
        return findMensajeEntities(false, maxResults, firstResult);
    }

    private List<Mensaje> findMensajeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Mensaje.class));
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

    public Mensaje findMensaje(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Mensaje.class, id);
        } finally {
            em.close();
        }
    }

    public int getMensajeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Mensaje> rt = cq.from(Mensaje.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
