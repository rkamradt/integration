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

package net.kamradtfamily.integration.supplier.data;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author randalkamradt
 */
@Entity
@XmlRootElement
public class PurchaseOrderFulfillment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @OneToOne
    private PurchaseOrder purchaseOrder;
    @OneToMany
    private Set<Shipment> shipments = new TreeSet<Shipment>();
    private Boolean complete;
    
    public int stillNeeds(PartPackage pp) {
        int needs = 0;
        int sent = 0;
        for (PartPackageSet pps : purchaseOrder.getItems()) {
            if(pp == pps.getPartPackage()) {
                needs += pps.getQuantity();
            }
        }
        for (Shipment s : shipments) {
            for(PartPackageSet pps : s.getItems()) {
                if(pp == pps.getPartPackage()) {
                    sent += pps.getQuantity();
                }
            }
        }
        return needs - sent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PurchaseOrderFulfillment)) {
            return false;
        }
        PurchaseOrderFulfillment other = (PurchaseOrderFulfillment) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "net.kamradtfamily.integration.supplier.data.PurchaseOrderFulfillment[ id=" + id + " ]";
    }

    /**
     * @return the purchaseOrder
     */
    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    /**
     * @param purchaseOrder the purchaseOrder to set
     */
    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    /**
     * @return the shipments
     */
    @XmlTransient
    public Set<Shipment> getShipments() {
        return shipments;
    }

    /**
     * @param shipments the shipments to set
     */
    public void setShipments(Set<Shipment> shipments) {
        this.shipments = shipments;
    }

    /**
     * @return the complete
     */
    public Boolean getComplete() {
        return complete;
    }

    /**
     * @param complete the complete to set
     */
    public void setComplete(Boolean complete) {
        this.complete = complete;
    }
    
}
