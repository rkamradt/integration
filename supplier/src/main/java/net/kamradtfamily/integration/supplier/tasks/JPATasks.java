/*
 * Copyright 2014 Frogandladybug.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.kamradtfamily.integration.supplier.tasks;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 *
 * @author randalkamradt
 */
public abstract class JPATasks implements Runnable {
    private EntityManager em = null;
    private EntityTransaction trans = null;
    public void start(EntityManagerFactory emf) {
        em = emf.createEntityManager();
        trans = em.getTransaction();
        trans.begin();
    }
    public void end() {
        if(trans != null && trans.isActive()) {
            if(trans.getRollbackOnly()) {
                trans.rollback();
            } else {
                trans.commit();
            }
        }
        if(em != null && em.isOpen()) {
            em.close();
        }
    }

    /**
     * @return the em
     */
    public EntityManager getEm() {
        return em;
    }

    /**
     * @return the trans
     */
    public EntityTransaction getTrans() {
        return trans;
    }
}
