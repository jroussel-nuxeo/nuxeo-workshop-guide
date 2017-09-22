package org.nuxeo.workshopguide.endpoints;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.workshopguide.NuxeoWorkshopGuideService;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@WebObject(type = "NWGProduct")
@Path("/product")
public class ProductEndPoint extends ModuleRoot {

    @POST
    @Path("/computePrice/{productID}")
    public Response computePrice(@PathParam("productID") String productID) {
        NuxeoWorkshopGuideService nuxeoWorkshopGuideService = Framework.getService(NuxeoWorkshopGuideService.class);

        CoreSession coreSession = ctx.getCoreSession();
        DocumentModel product = coreSession.getDocument(new IdRef(productID));

        if("NWGProduct".equals(product.getType())) {
            double newPrice = nuxeoWorkshopGuideService.computePrice(product, coreSession);
            coreSession.saveDocument(product);

            return Response.ok().build();
        }
        else {
            return Response.status(404).build();
        }
    }
}
