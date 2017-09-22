package org.nuxeo.workshopguide.endpoints;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.workshopguide.NuxeoWorkshopGuideService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@WebObject(type = "NWGProduct")
@Path("/product")
public class ProductEndPoint extends ModuleRoot {

    @GET
    @Path("{productID}")
    public void computePrice(@PathParam("productID") String productID) {
        NuxeoWorkshopGuideService nuxeoWorkshopGuideService = Framework.getService(NuxeoWorkshopGuideService.class);

        CoreSession coreSession = ctx.getCoreSession();
        DocumentModel product = coreSession.getDocument(new IdRef(productID));

        if("NWGProduct".equals(product.getType())) {
            double newPrice = nuxeoWorkshopGuideService.computePrice(product, coreSession);
            coreSession.saveDocument(product);
        }
        else {
            // TODO Do we have a way to throw a custom HTTP code as an answer? Have a look at Response object:
            // https://stackoverflow.com/questions/4687271/jax-rs-how-to-return-json-and-http-status-code-together
        }
    }
}
