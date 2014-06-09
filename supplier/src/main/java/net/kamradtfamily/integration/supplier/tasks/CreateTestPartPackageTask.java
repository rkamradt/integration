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

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import javax.persistence.TypedQuery;
import net.kamradtfamily.integration.supplier.data.Part;
import net.kamradtfamily.integration.supplier.data.PartPackage;

/**
 *
 * @author randalkamradt
 */
public class CreateTestPartPackageTask extends JPATasks {

    @Override
    public void run() {
        Random r = new Random();
        TypedQuery<Part> q = getEm().createQuery("select p from Part p", Part.class);
        List<Part> ps = q.getResultList();
        for(Part p : ps) {
            int packageQty = r.nextInt(10);
            for(int i = 0; i < packageQty; i++) {
                PartPackage pp = new PartPackage();
                pp.setPart(p);
                if(p.getName().equals("grease") || p.getName().equals("linen")) {
                    pp.setAmount(BigDecimal.valueOf(r.nextDouble() * r.nextInt(10)));
                } else {
                    pp.setQty(r.nextInt(10));
                }
                pp.setCost(BigDecimal.valueOf(r.nextDouble() * r.nextInt(10)));
                pp.setPrice(pp.getCost().multiply(BigDecimal.valueOf(1.5)));
                getEm().persist(pp);
            }
        }
    }
}
