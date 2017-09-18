package org.nuxeo.workshopguide;

import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.*;
import org.nuxeo.runtime.RuntimeService;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(FeaturesRunner.class)
@Features({ AutomationFeature.class })
@Deploy({"org.nuxeo.workshopguide.nuxeo-workshop-guide-core", "org.nuxeo.ecm.platform.filemanager.core", "org.nuxeo.ecm.webapp.core", "studio.extensions.jroussel-SANDBOX"})
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
        String queryForGettingDocument = "SELECT * FROM NWGVisual";
        DocumentModelList queryResults = coreSession.query(queryForGettingDocument);
        Assert.assertEquals(1, queryResults.size());
    }

    @Test
    public void testComputePrice() {
        final double expectedPrice = 42;

        DocumentModel docModel = coreSession.createDocumentModel("/", "myProduct", "NWGProduct");
        docModel.setPropertyValue("nwgproduct:price", 0);
        docModel = coreSession.createDocument(docModel);

        coreSession.save();

        Assert.assertEquals(expectedPrice, nuxeoworkshopguideservice.computePrice(docModel), 0.001);
    }
}
