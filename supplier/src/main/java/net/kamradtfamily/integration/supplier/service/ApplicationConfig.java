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

package net.kamradtfamily.integration.supplier.service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author randalkamradt
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(net.kamradtfamily.integration.supplier.service.CustomerFacadeREST.class);
        resources.add(net.kamradtfamily.integration.supplier.service.InventoryFacadeREST.class);
        resources.add(net.kamradtfamily.integration.supplier.service.InvoiceFacadeREST.class);
        resources.add(net.kamradtfamily.integration.supplier.service.PartFacadeREST.class);
        resources.add(net.kamradtfamily.integration.supplier.service.PartPackageFacadeREST.class);
        resources.add(net.kamradtfamily.integration.supplier.service.PartPackageSetFacadeREST.class);
        resources.add(net.kamradtfamily.integration.supplier.service.PaymentFacadeREST.class);
        resources.add(net.kamradtfamily.integration.supplier.service.PurchaseOrderFacadeREST.class);
        resources.add(net.kamradtfamily.integration.supplier.service.PurchaseOrderFulfillmentFacadeREST.class);
        resources.add(net.kamradtfamily.integration.supplier.service.ShipmentFacadeREST.class);
    }
    
}
