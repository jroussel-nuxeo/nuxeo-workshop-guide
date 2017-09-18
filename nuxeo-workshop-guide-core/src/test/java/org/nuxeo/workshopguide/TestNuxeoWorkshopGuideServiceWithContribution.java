package org.nuxeo.workshopguide;

import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.LocalDeploy;

import static org.junit.Assert.assertNotNull;

@RunWith(FeaturesRunner.class)
@Features({ AutomationFeature.class })
@Deploy({"org.nuxeo.workshopguide.nuxeo-workshop-guide-core", "org.nuxeo.ecm.platform.filemanager.core", "org.nuxeo.ecm.webapp.core", "studio.extensions.jroussel-SANDBOX"})
@LocalDeploy({"org.nuxeo.ecm.automation.core:OSGI-INF/nuxeoworkshopguideservice-contrib.xml"})
public class TestNuxeoWorkshopGuideServiceWithContribution {

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
    public void testComputePrice() {
        // TODO contribution is not properly loaded, fix it. Have a look at LocalDeploy annotation
        /*
        final double expectedPrice = 10;

        DocumentModel docModel = coreSession.createDocumentModel("/", "myProduct", "NWGProduct");
        docModel.setPropertyValue("nwgproduct:price", 0);
        docModel = coreSession.createDocument(docModel);

        coreSession.save();

        Assert.assertEquals(expectedPrice, nuxeoworkshopguideservice.computePrice(docModel), 0.001);
        */
    }
}
