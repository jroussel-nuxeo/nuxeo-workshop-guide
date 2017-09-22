package org.nuxeo.workshopguide.adapters;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;

/**
 *
 */
public class NWGProductAdapter {
    protected final DocumentModel doc;

    protected String titleXpath = "dc:title";
    protected String descriptionXpath = "dc:description";

    protected String availableXpath = "nwgproduct:available";
    protected String priceXpath = "nwgproduct:price";
    protected String sizeXpath = "nwgproduct:size";


    public NWGProductAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    // Basic methods
    //
    // Note that we voluntarily expose only a subset of the DocumentModel API in this adapter.
    // You may wish to complete it without exposing everything!
    // For instance to avoid letting people change the document state using your adapter,
    // because this would be handled through workflows / buttons / events in your application.
    //
    public void create() {
        CoreSession session = doc.getCoreSession();
        session.createDocument(doc);
    }

    public void save() {
        CoreSession session = doc.getCoreSession();
        session.saveDocument(doc);
    }

    public DocumentRef getParentRef() {
        return doc.getParentRef();
    }

    // Technical properties retrieval
    public String getId() {
        return doc.getId();
    }

    public String getName() {
        return doc.getName();
    }

    public String getPath() {
        return doc.getPathAsString();
    }

    public String getState() {
        return doc.getCurrentLifeCycleState();
    }

    // Metadata get / set
    public String getTitle() {
        return doc.getTitle();
    }

    public void setTitle(String value) {
        doc.setPropertyValue(titleXpath, value);
    }

    public String getDescription() {
        return (String) doc.getPropertyValue(descriptionXpath);
    }

    public void setDescription(String value) {
        doc.setPropertyValue(descriptionXpath, value);
    }

    public boolean getAvailable() {
        return (boolean) doc.getPropertyValue(availableXpath);
    }

    public void setAvailable(boolean availability) {
        doc.setPropertyValue(availableXpath, availability);
    }

    public double getPrice() {
        return (double) doc.getPropertyValue(priceXpath);
    }

    public void setPrice(double price) {
        doc.setPropertyValue(priceXpath, price);
    }

    public long getSize() {
        return (long) doc.getPropertyValue(sizeXpath);
    }

    public void setSize(long size) {
        doc.setPropertyValue(sizeXpath, size);
    }
}
