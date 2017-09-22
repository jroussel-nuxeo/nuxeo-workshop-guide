package org.nuxeo.workshopguide.adapters;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.workshopguide.features.NuxeoWorkshopGuideDefaultFeature;

import javax.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features({NuxeoWorkshopGuideDefaultFeature.class})
public class TestNWGProductAdapter {
    @Inject
    CoreSession coreSession;

    @Test
    public void shouldCallTheAdapter() {
        String doctype = "NWGProduct";
        String testTitle = "My Adapter Title";
        boolean isAvailable = true;
        double price = 42;
        long size = 42;

        DocumentModel doc = coreSession.createDocumentModel("/", "test-adapter", doctype);
        doc.setPropertyValue("nwgproduct:available", isAvailable);
        doc.setPropertyValue("nwgproduct:price", price);
        doc.setPropertyValue("nwgproduct:size", size);
        doc = coreSession.createDocument(doc);

        NWGProductAdapter adapter = doc.getAdapter(NWGProductAdapter.class);
        adapter.setTitle(testTitle);
        adapter.create();
        adapter.save();

        Assert.assertNotNull("The adapter can't be used on the " + doctype + " document type", adapter);
        Assert.assertEquals("Document title does not match when using the adapter", testTitle, adapter.getTitle());
        Assert.assertEquals(isAvailable, adapter.getAvailable());
        Assert.assertEquals(price, adapter.getPrice(), 0.001);
        Assert.assertEquals(size, adapter.getSize());

        adapter.setAvailable(!isAvailable);
        adapter.setPrice(0);
        adapter.setSize(0);
        adapter.save();

        DocumentModel updatedDoc = coreSession.getDocument(new IdRef(doc.getId()));
        Assert.assertEquals(!isAvailable, (boolean) updatedDoc.getPropertyValue("nwgproduct:available"));
        Assert.assertEquals(0, (double) updatedDoc.getPropertyValue("nwgproduct:price"), 0.001);
        Assert.assertEquals(0, (long) updatedDoc.getPropertyValue("nwgproduct:size"));
    }
}
