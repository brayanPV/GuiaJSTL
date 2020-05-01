/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.IllegalOrphanException;
import DAO.exceptions.NonexistentEntityException;
import DAO.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DTO.Mensaje;
import DTO.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author stive
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getMensajeList() == null) {
            usuario.setMensajeList(new ArrayList<Mensaje>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Mensaje> attachedMensajeList = new ArrayList<Mensaje>();
            for (Mensaje mensajeListMensajeToAttach : usuario.getMensajeList()) {
                mensajeListMensajeToAttach = em.getReference(mensajeListMensajeToAttach.getClass(), mensajeListMensajeToAttach.getId());
                attachedMensajeList.add(mensajeListMensajeToAttach);
            }
            usuario.setMensajeList(attachedMensajeList);
            em.persist(usuario);
            for (Mensaje mensajeListMensaje : usuario.getMensajeList()) {
                Usuario oldUsuarioOfMensajeListMensaje = mensajeListMensaje.getUsuario();
                mensajeListMensaje.setUsuario(usuario);
                mensajeListMensaje = em.merge(mensajeListMensaje);
                if (oldUsuarioOfMensajeListMensaje != null) {
                    oldUsuarioOfMensajeListMensaje.getMensajeList().remove(mensajeListMensaje);
                    oldUsuarioOfMensajeListMensaje = em.merge(oldUsuarioOfMensajeListMensaje);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getUsuario()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getUsuario());
            List<Mensaje> mensajeListOld = persistentUsuario.getMensajeList();
            List<Mensaje> mensajeListNew = usuario.getMensajeList();
            List<String> illegalOrphanMessages = null;
            for (Mensaje mensajeListOldMensaje : mensajeListOld) {
                if (!mensajeListNew.contains(mensajeListOldMensaje)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Mensaje " + mensajeListOldMensaje + " since its usuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Mensaje> attachedMensajeListNew = new ArrayList<Mensaje>();
            for (Mensaje mensajeListNewMensajeToAttach : mensajeListNew) {
                mensajeListNewMensajeToAttach = em.getReference(mensajeListNewMensajeToAttach.getClass(), mensajeListNewMensajeToAttach.getId());
                attachedMensajeListNew.add(mensajeListNewMensajeToAttach);
            }
            mensajeListNew = attachedMensajeListNew;
            usuario.setMensajeList(mensajeListNew);
            usuario = em.merge(usuario);
            for (Mensaje mensajeListNewMensaje : mensajeListNew) {
                if (!mensajeListOld.contains(mensajeListNewMensaje)) {
                    Usuario oldUsuarioOfMensajeListNewMensaje = mensajeListNewMensaje.getUsuario();
                    mensajeListNewMensaje.setUsuario(usuario);
                    mensajeListNewMensaje = em.merge(mensajeListNewMensaje);
                    if (oldUsuarioOfMensajeListNewMensaje != null && !oldUsuarioOfMensajeListNewMensaje.equals(usuario)) {
                        oldUsuarioOfMensajeListNewMensaje.getMensajeList().remove(mensajeListNewMensaje);
                        oldUsuarioOfMensajeListNewMensaje = em.merge(oldUsuarioOfMensajeListNewMensaje);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usuario.getUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Mensaje> mensajeListOrphanCheck = usuario.getMensajeList();
            for (Mensaje mensajeListOrphanCheckMensaje : mensajeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Mensaje " + mensajeListOrphanCheckMensaje + " in its mensajeList field has a non-nullable usuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
