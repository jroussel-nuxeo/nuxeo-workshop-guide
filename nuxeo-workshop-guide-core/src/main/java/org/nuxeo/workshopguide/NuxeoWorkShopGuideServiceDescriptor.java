package org.nuxeo.workshopguide;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

@XObject("handlePriceOperation")
public class NuxeoWorkShopGuideServiceDescriptor {

    @XNode("@amountToAdd")
    private double amount;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
