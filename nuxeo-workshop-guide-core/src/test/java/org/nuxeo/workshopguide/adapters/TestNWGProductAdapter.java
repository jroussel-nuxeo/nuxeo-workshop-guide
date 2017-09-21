package org.nuxeo.workshopguide.adapters;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.workshopguide.features.NuxeoWorkshopGuideDefaultFeature;

import javax.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features({NuxeoWorkshopGuideDefaultFeature.class})
public class TestNWGProductAdapter {
    @Inject
    CoreSession session;

    @Test
    public void shouldCallTheAdapter() {
        String doctype = "NWGProduct";
        String testTitle = "My Adapter Title";
        boolean isAvailable = true;
        double price = 42;
        long size = 42;

        DocumentModel doc = session.createDocumentModel("/", "test-adapter", doctype);
        doc.setPropertyValue("nwgproduct:available", isAvailable);
        doc.setPropertyValue("nwgproduct:price", price);
        doc.setPropertyValue("nwgproduct:size", size);

        NWGProductAdapter adapter = doc.getAdapter(NWGProductAdapter.class);
        adapter.setTitle(testTitle);
        adapter.create();
        session.save();

        Assert.assertNotNull("The adapter can't be used on the " + doctype + " document type", adapter);
        Assert.assertEquals("Document title does not match when using the adapter", testTitle, adapter.getTitle());
        Assert.assertEquals(isAvailable, adapter.getAvailable());
        Assert.assertEquals(price, adapter.getPrice(), 0.001);
        Assert.assertEquals(size, adapter.getSize());

        // TODO pousser le test en utilisant et testant les setters de l'adapter
    }
}
