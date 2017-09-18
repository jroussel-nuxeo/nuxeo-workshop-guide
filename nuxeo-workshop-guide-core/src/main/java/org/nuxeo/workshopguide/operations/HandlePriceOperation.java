package org.nuxeo.workshopguide.operations;

import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.workshopguide.NuxeoWorkshopGuideService;

/**
 *
 */
@Operation(id=HandlePriceOperation.ID, category=Constants.CAT_DOCUMENT, label="Document.HandlePriceOperation", description="Describe here what your operation does.")
public class HandlePriceOperation {

    public static final String ID = "Document.HandlePriceOperation";

    @Context
    protected CoreSession session;

    @OperationMethod
    public void run(DocumentModel documentModel) {
        if ("NWGProduct".equals(documentModel.getType())) {
            NuxeoWorkshopGuideService nwgService = Framework.getService(NuxeoWorkshopGuideService.class);

            nwgService.computePrice(documentModel);
            session.saveDocument(documentModel);
        }
    }

    @OperationMethod
    public void run(DocumentModelList documentModels) {
        NuxeoWorkshopGuideService nwgService = Framework.getService(NuxeoWorkshopGuideService.class);

        for (DocumentModel documentModel : documentModels) {
            if ("NWGProduct".equals(documentModel.getType())) {
                nwgService.computePrice(documentModel);
                session.saveDocument(documentModel);
            }
        }
    }
}
