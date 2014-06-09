package net.kamradtfamily.integration.supplier;

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

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import net.kamradtfamily.integration.supplier.tasks.CreateTestInventoryTask;
import net.kamradtfamily.integration.supplier.tasks.CreateTestPartPackageTask;
import net.kamradtfamily.integration.supplier.tasks.CreateTestPartsTask;
import net.kamradtfamily.integration.supplier.tasks.CreateTestShipmentTask;
import net.kamradtfamily.integration.supplier.tasks.JPATasks;

/**
 * Web application lifecycle listener.
 *
 * @author randalkamradt
 */
@WebListener()
public class TestTaskListener implements ServletContextListener {
    private static final Logger log = Logger.getLogger(TestTaskListener.class.getName());
    private EntityManagerFactory emf;
    private int threadCount = 1;
    private final ThreadFactory tf = new ThreadFactory() {

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "TaskThread-" + threadCount);
        }
    };
    private final ScheduledThreadPoolExecutor tpe = new ScheduledThreadPoolExecutor(10, tf) {
        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            if(r instanceof JPATasks) {
                JPATasks jt = (JPATasks) r;
                log.log(Level.INFO, "starting task type {0}", r.getClass().getSimpleName());
                jt.start(emf);
            }
        }            
        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            if(r instanceof JPATasks) {
                JPATasks jt = (JPATasks) r;
                jt.end();
                log.log(Level.INFO, "ended task type {0}", r.getClass().getSimpleName());
            }
        }            
    };
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.log(Level.ALL, "***************************************************");
        log.log(Level.ALL, "initializing testing context for supply webservices");
        emf = Persistence.createEntityManagerFactory("net.kamradtfamily.integration_supplier_war_1.0-SNAPSHOTPU");
        log.log(Level.ALL, "adding some test parts for supply webservices");
        tpe.execute(new CreateTestPartsTask());
        log.log(Level.ALL, "adding some test part packaging for supply webservices");
        tpe.execute(new CreateTestPartPackageTask());
        log.log(Level.ALL, "creating some test inventory for supply webservices");
        tpe.execute(new CreateTestInventoryTask());
//        tpe.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
//        tpe.scheduleWithFixedDelay(new CreateTestInventoryTask(), 10, 5, TimeUnit.SECONDS);
//        tpe.scheduleWithFixedDelay(new CreateTestShipmentTask(), 10, 5, TimeUnit.SECONDS);
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.log(Level.ALL, "destroying testing context for supply webservices");
        tpe.shutdown();
        log.log(Level.ALL, "send shutdown signal for task processor for supply webservices");
        try {
            tpe.awaitTermination(100, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            log.log(Level.SEVERE, "timout while waiting to terminate tasks", ex);
        }
        log.log(Level.ALL, "shutdown task processor for supply webservices");
        if(emf != null && emf.isOpen()) {
            emf.close();
        }
        log.log(Level.ALL, "emf closed for supply webservices, context destroyed");
        log.log(Level.ALL, "****************************************************");
    }
}
