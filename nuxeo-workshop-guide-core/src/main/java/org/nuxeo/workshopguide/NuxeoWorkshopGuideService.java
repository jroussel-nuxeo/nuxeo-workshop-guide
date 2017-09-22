package org.nuxeo.workshopguide;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface NuxeoWorkshopGuideService {

    /**
     * Compute and set a new price for a NWGProduct document
     * @param documentModel a NWGProduct document
     * @return new price of the NWGProduct
     */
    double computePrice(DocumentModel documentModel, CoreSession coreSession);

    boolean moveLinkedVisualsToHiddenFolder(DocumentModel documentModel, CoreSession coreSession);
}
