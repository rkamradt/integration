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

import java.util.List;
import java.util.Random;
import javax.persistence.TypedQuery;
import net.kamradtfamily.integration.supplier.data.Invoice;
import net.kamradtfamily.integration.supplier.data.PartPackage;
import net.kamradtfamily.integration.supplier.data.PartPackageSet;
import net.kamradtfamily.integration.supplier.data.PurchaseOrder;
import net.kamradtfamily.integration.supplier.data.PurchaseOrderFulfillment;
import net.kamradtfamily.integration.supplier.data.Shipment;

/**
 *
 * @author randalkamradt
 */
public class CreateTestShipmentTask extends JPATasks {

    @Override
    public void run() {
        Random r = new Random();
        TypedQuery<PurchaseOrder> q = getEm().createQuery("select po from PurchaseOrder po", PurchaseOrder.class);
        q.setMaxResults(1);
        List<PurchaseOrder> pos = q.getResultList();
        if(pos.size() > 0) {
            PurchaseOrder po = pos.get(0);
            if(po.getFulfillment() == null) {
                PurchaseOrderFulfillment fulfillment = new PurchaseOrderFulfillment();
                fulfillment.setPurchaseOrder(po);
                getEm().persist(fulfillment);
            }
            if(po.getItems().isEmpty()) {
                po.getFulfillment().setComplete(Boolean.TRUE);
                return;
            }
            for(PartPackageSet pps : po.getItems()) {
                PartPackage pp = pps.getPartPackage();
                int needs = po.getFulfillment().stillNeeds(pp);
                if(needs != 0 && r.nextBoolean()) {
                    Shipment s = new Shipment();
                    s.setPurchaseOrder(po);
                    PartPackageSet npps = new PartPackageSet();
                    pps.setPartPackage(pp);
                    pps.setQuantity(r.nextInt(needs));
                    getEm().persist(pps);
                    s.getItems().add(pps);
                    getEm().persist(s);
                }
            }
            boolean complete = true;
            for(PartPackageSet pps : po.getItems()) {
                PartPackage pp = pps.getPartPackage();
                int needs = po.getFulfillment().stillNeeds(pp);
                if(needs > 0) {
                    complete = false;
                }
            }
            if(complete) {
                po.getFulfillment().setComplete(complete);
                Invoice i = new Invoice();
                i.setPurchaseOrder(po);
                getEm().persist(i);
            }
        }
    }
}
