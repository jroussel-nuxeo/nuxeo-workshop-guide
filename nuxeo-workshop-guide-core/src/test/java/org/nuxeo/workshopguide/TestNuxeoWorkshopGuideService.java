package org.nuxeo.workshopguide;

import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.*;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.runtime.RuntimeService;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features({ CoreFeature.class })
//@Features({ PlatformFeature.class })
@Deploy("org.nuxeo.workshopguide.nuxeo-workshop-guide-core")
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
        DocumentModel docModel = coreSession.createDocumentModel("/", "myFile", "File");
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
        String queryForGettingDocument = "SELECT * FROM File";
        DocumentModelList queryResults = coreSession.query(queryForGettingDocument);
        Assert.assertEquals(1, queryResults.size());
    }
}
