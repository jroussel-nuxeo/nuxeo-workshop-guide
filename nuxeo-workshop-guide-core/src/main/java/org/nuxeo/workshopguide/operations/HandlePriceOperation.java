package org.nuxeo.workshopguide.operations;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.workshopguide.NuxeoWorkshopGuideService;

/**
 *
 */
@Operation(id=HandlePriceOperation.ID, category=Constants.CAT_DOCUMENT, label="Document.HandlePriceOperation", description="Describe here what your operation does.")
public class HandlePriceOperation {

    private static final Log log = LogFactory.getLog(HandlePriceOperation.class);

    public static final String ID = "Document.HandlePriceOperation";

    @Context
    protected CoreSession session;

    @OperationMethod
    public void run(DocumentModel documentModel) {
        log.debug("HandlePriceOperation called for one DocumentModel.");

        if ("NWGProduct".equals(documentModel.getType())) {
            NuxeoWorkshopGuideService nwgService = Framework.getService(NuxeoWorkshopGuideService.class);

            nwgService.computePrice(documentModel);
            session.saveDocument(documentModel);
        }
    }

    @OperationMethod
    public void run(DocumentModelList documentModels) {
        log.debug("HandlePriceOperation called for several DocumentModel.");

        NuxeoWorkshopGuideService nwgService = Framework.getService(NuxeoWorkshopGuideService.class);

        for (DocumentModel documentModel : documentModels) {
            if ("NWGProduct".equals(documentModel.getType())) {
                nwgService.computePrice(documentModel);
                session.saveDocument(documentModel);
            }
        }
    }
}
