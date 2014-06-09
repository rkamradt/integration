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
import net.kamradtfamily.integration.supplier.data.Inventory;
import net.kamradtfamily.integration.supplier.data.PartPackage;

/**
 *
 * @author randalkamradt
 */
public class CreateTestInventoryTask extends JPATasks {

    @Override
    public void run() {
        Random r = new Random();
        TypedQuery<PartPackage> q = getEm().createQuery("select pp from PartPackage pp", PartPackage.class);
        List<PartPackage> pps = q.getResultList();
        for(PartPackage pp : pps) {
            TypedQuery<Inventory> iq = getEm().createQuery("select i from Inventory i where i.partPackage = :pp", Inventory.class);
            iq.setParameter("pp", pp);
            Inventory i;
            try {
                i = iq.getSingleResult();
            } catch(Exception ex) {
                // no inventory, create some
                i = new Inventory();
                i.setPartPackage(pp);
                i.setQuantity(1000);
                getEm().persist(i);
            }
            if(i.getQuantity() < 1000) {
                i.setQuantity(i.getQuantity() + r.nextInt(1000));
            }
        }
    }
}
