package org.nuxeo.workshopguide.operations;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.workshopguide.features.NuxeoWorkshopGuideDefaultFeature;

import javax.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features({NuxeoWorkshopGuideDefaultFeature.class})
public class TestHandlePriceOperation {

    @Inject
    protected CoreSession coreSession;

    @Inject
    protected AutomationService automationService;

    @Test
    public void testHandlingPriceOnOneProduct() throws OperationException {
        double expectedPrice = 42;

        DocumentModel product = coreSession.createDocumentModel("/", "myProduct", "NWGProduct");
        product.setPropertyValue("nwgproduct:price", 0);
        product = coreSession.createDocument(product);

        coreSession.save();

        OperationContext ctx = new OperationContext(coreSession);
        ctx.setInput(product);

        automationService.run(ctx, HandlePriceOperation.ID);

        product = coreSession.getDocument(new IdRef(product.getId()));

        Assert.assertEquals(expectedPrice, (Double) product.getPropertyValue("nwgproduct:price"), 0.001);
    }

    @Test
    public void testHandlingPriceOnMultipleProducts() throws OperationException {
        DocumentModel product1 = coreSession.createDocumentModel("/", "myProduct1", "NWGProduct");
        product1.setPropertyValue("nwgproduct:price", 0);
        product1 = coreSession.createDocument(product1);

        DocumentModel product2 = coreSession.createDocumentModel("/", "myProduct1", "NWGProduct");
        product2.setPropertyValue("nwgproduct:price", 10);
        product2 = coreSession.createDocument(product2);

        coreSession.save();

        DocumentModelList documentModels = new DocumentModelListImpl();
        documentModels.add(product1);
        documentModels.add(product2);

        OperationContext ctx = new OperationContext(coreSession);
        ctx.setInput(documentModels);

        automationService.run(ctx, HandlePriceOperation.ID);

        product1 = coreSession.getDocument(new IdRef(product1.getId()));
        product2 = coreSession.getDocument(new IdRef(product2.getId()));

        Assert.assertEquals(42, (Double) product1.getPropertyValue("nwgproduct:price"), 0.001);
        Assert.assertEquals(52, (Double) product2.getPropertyValue("nwgproduct:price"), 0.001);
    }
}
