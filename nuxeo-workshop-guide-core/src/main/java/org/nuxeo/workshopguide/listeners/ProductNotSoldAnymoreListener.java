package org.nuxeo.workshopguide.listeners;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.workshopguide.NuxeoWorkshopGuideService;

public class ProductNotSoldAnymoreListener implements EventListener {

    private static final Log log = LogFactory.getLog(ProductNotSoldAnymoreListener.class);

    @Override
    public void handleEvent(Event event) {
        DocumentEventContext docCtx;
        if (event.getContext() instanceof DocumentEventContext) {
            docCtx = (DocumentEventContext) event.getContext();
        } else {
            return;
        }

        DocumentModel modifiedDocument = docCtx.getSourceDocument();
        if ("NWGProduct".equals(modifiedDocument.getType())) {
            log.debug("ProductNotSoldAnymoreListener has been called on a NWGProduct document.");

            boolean isAvailable = (boolean) modifiedDocument.getPropertyValue("nwgproduct:available");
            if (!isAvailable) {
                NuxeoWorkshopGuideService nwgService = Framework.getService(NuxeoWorkshopGuideService.class);
                nwgService.moveLinkedVisualsToHiddenFolder(modifiedDocument, docCtx.getCoreSession());
            }


        }
    }
}
