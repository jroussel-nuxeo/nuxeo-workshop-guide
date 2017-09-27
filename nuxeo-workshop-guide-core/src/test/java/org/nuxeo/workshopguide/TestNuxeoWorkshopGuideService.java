package org.nuxeo.workshopguide;

import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.collections.api.CollectionManager;
import org.nuxeo.ecm.core.api.*;
import org.nuxeo.runtime.RuntimeService;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.LocalDeploy;
import org.nuxeo.runtime.transaction.TransactionHelper;
import org.nuxeo.workshopguide.features.NuxeoWorkshopGuideDefaultFeature;

import java.util.HashMap;

import static org.junit.Assert.assertNotNull;

@RunWith(FeaturesRunner.class)
@Features({NuxeoWorkshopGuideDefaultFeature.class})
public class TestNuxeoWorkshopGuideService {

    @Inject
    protected NuxeoWorkshopGuideService nuxeoworkshopguideservice;

    @Inject
    protected CoreSession coreSession;

    @Test
    public void testService() {
        assertNotNull(nuxeoworkshopguideservice);
    }

    @Test
    public void isNuxeoStarted() {
        Assert.assertNotNull("Runtime is not available", Framework.getRuntime());
    }

    @Test
    public void isComponentLoaded() {
        RuntimeService runtime = Framework.getRuntime();
        Assert.assertNotNull(runtime.getComponent("org.nuxeo.workshopguide.NuxeoWorkshopGuideService"));
    }

    @Test
    public void testDocumentCreation() {
        DocumentModel docModel = coreSession.createDocumentModel("/", "myProduct", "NWGProduct");
        docModel = coreSession.createDocument(docModel);

        coreSession.save();

        // Retrieve a document by ID
        IdRef docIdRef = new IdRef(docModel.getId());
        DocumentModel docByID = coreSession.getDocument(docIdRef);
        Assert.assertNotNull(docByID);

        // Retrieve a document by path
        PathRef docPathRef = new PathRef(docModel.getPathAsString());
        DocumentModel docByPath = coreSession.getDocument(docPathRef);
        Assert.assertNotNull(docByPath);

        // Retrieve a document using a query
        String queryForGettingDocument = "SELECT * FROM NWGProduct";
        DocumentModelList queryResults = coreSession.query(queryForGettingDocument);
        Assert.assertEquals(1, queryResults.size());


        // Test adding a distributor complex metadata

        HashMap<String, Object> distributor = new HashMap<>();
        distributor.put("name", "Douglas Adams");
        distributor.put("sellLocation", "42 rue de la fin du monde");
        docModel.setPropertyValue("nwgproduct:distributor", distributor);
        coreSession.save();

        String retrievedDistributorName = (String) ((HashMap<String, Object>)docModel.getPropertyValue("nwgproduct:distributor")).get("name");
        String retrievedDistributorSellLocation = (String) ((HashMap<String, Object>)docModel.getPropertyValue("nwgproduct:distributor")).get("sellLocation");

        Assert.assertEquals("Douglas Adams", retrievedDistributorName);
        Assert.assertEquals("42 rue de la fin du monde", retrievedDistributorSellLocation);
    }

    @Test
    public void testComputePrice() {
        final double expectedPrice = 50;

        DocumentModel docModel = coreSession.createDocumentModel("/", "myProduct", "NWGProduct");
        docModel.setPropertyValue("nwgproduct:price", 8);
        docModel = coreSession.createDocument(docModel);

        coreSession.save();

        double newPrice = nuxeoworkshopguideservice.computePrice(docModel, coreSession);
        coreSession.saveDocument(docModel);

        DocumentModel updatedDoc = coreSession.getDocument(new IdRef(docModel.getId()));

        Assert.assertEquals(expectedPrice, newPrice, 0.001);
        Assert.assertEquals(expectedPrice, (double) updatedDoc.getPropertyValue("nwgproduct:price"), 0.001);
    }

    @Test
    public void testComputePriceWhenNoPriceHasBeenSet() {
        final double expectedPrice = 42;

        DocumentModel docModel = coreSession.createDocumentModel("/", "myProduct", "NWGProduct");
        docModel = coreSession.createDocument(docModel);

        coreSession.save();

        Assert.assertEquals(expectedPrice, nuxeoworkshopguideservice.computePrice(docModel, coreSession), 0.001);
    }

    @Test
    @LocalDeploy({"org.nuxeo.workshopguide.nuxeo-workshop-guide-core:nuxeoworkshopguideservice-contrib.xml"})
    public void testComputePriceWithContribution() {
        final double expectedPrice = 10;

        DocumentModel docModel = coreSession.createDocumentModel("/", "myProduct", "NWGProduct");
        docModel.setPropertyValue("nwgproduct:price", 0);
        docModel = coreSession.createDocument(docModel);

        coreSession.save();

        Assert.assertEquals(expectedPrice, nuxeoworkshopguideservice.computePrice(docModel, coreSession), 0.001);
    }

    @Test
    public void testMoveVisualsMethod() {
        DocumentModel defaultFolder = coreSession.createDocumentModel("/", "HiddenFolderForVisuals", "Folder");
        defaultFolder.setPropertyValue("dc:title", "HiddenFolderForVisuals");
        defaultFolder = coreSession.createDocument(defaultFolder);
        coreSession.save();

        DocumentModel visual1 = coreSession.createDocumentModel("/", "myVisual1", "NWGVisual");
        visual1 = coreSession.createDocument(visual1);
        coreSession.save();
        Assert.assertEquals("/myVisual1", visual1.getPathAsString());

        DocumentModel visual2 = coreSession.createDocumentModel("/", "myVisual2", "NWGVisual");
        visual2 = coreSession.createDocument(visual2);
        coreSession.save();
        Assert.assertEquals("/myVisual2", visual2.getPathAsString());

        DocumentModel product = coreSession.createDocumentModel("/", "myProduct", "NWGProduct");
        product.setPropertyValue("nwgproduct:available", true);
        product = coreSession.createDocument(product);
        coreSession.save();

        CollectionManager collectionManager = Framework.getService(CollectionManager.class);
        collectionManager.addToCollection(product, visual1, coreSession);
        collectionManager.addToCollection(product, visual2, coreSession);

        coreSession.save();


        nuxeoworkshopguideservice.moveLinkedVisualsToHiddenFolder(product, coreSession);
        visual1 = coreSession.getDocument(new IdRef(visual1.getId()));
        visual2 = coreSession.getDocument(new IdRef(visual2.getId()));

        //coreSession.save();
        Assert.assertEquals("/HiddenFolderForVisuals/myVisual1", visual1.getPathAsString());
        Assert.assertEquals("/HiddenFolderForVisuals/myVisual2", visual2.getPathAsString());
    }
}
