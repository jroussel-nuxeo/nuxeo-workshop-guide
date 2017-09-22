package org.nuxeo.workshopguide;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.collections.core.adapter.Collection;
import org.nuxeo.ecm.core.api.*;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

import java.util.ArrayList;
import java.util.List;

public class NuxeoWorkshopGuideServiceImpl extends DefaultComponent implements NuxeoWorkshopGuideService {

    private static final Log log = LogFactory.getLog(NuxeoWorkshopGuideServiceImpl.class);

    final private static double PRICE_TO_ADD = 42;

    final private static String DEFAULT_FOLDER_NAME_WHERE_TO_MOVE_VISUALS = "HiddenFolderForVisuals";

    private double overriddenPriceToAdd = 0;

    public double getOverriddenPriceToAdd() {
        return overriddenPriceToAdd;
    }

    public void setOverriddenPriceToAdd(double overriddenPriceToAdd) {
        this.overriddenPriceToAdd = overriddenPriceToAdd;
    }

    /**
     * Component activated notification.
     * Called when the component is activated. All component dependencies are resolved at that moment.
     * Use this method to initialize the component.
     *
     * @param context the component context.
     */
    @Override
    public void activate(ComponentContext context) {
        super.activate(context);
    }

    /**
     * Component deactivated notification.
     * Called before a component is unregistered.
     * Use this method to do cleanup if any and free any resources held by the component.
     *
     * @param context the component context.
     */
    @Override
    public void deactivate(ComponentContext context) {
        super.deactivate(context);
    }

    /**
     * Application started notification.
     * Called after the application started.
     * You can do here any initialization that requires a working application
     * (all resolved bundles and components are active at that moment)
     *
     * @param context the component context. Use it to get the current bundle context
     * @throws Exception
     */
    @Override
    public void applicationStarted(ComponentContext context) {
        // do nothing by default. You can remove this method if not used.
    }

    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
        if ("amount".equals(extensionPoint)) {
            NuxeoWorkShopGuideServiceDescriptor descr = (NuxeoWorkShopGuideServiceDescriptor) contribution;

            double amount = descr.getAmount();

            this.setOverriddenPriceToAdd(amount);
        }
    }

    @Override
    public void unregisterContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
        this.setOverriddenPriceToAdd(0);
    }

    @Override
    public double computePrice(DocumentModel documentModel, CoreSession coreSession) {
        log.debug("computePrice(..) method called");

        if (!"NWGProduct".equals(documentModel.getType())) {
            return 0d;
        }

        double currentPrice;
        try {
            currentPrice = (double) documentModel.getPropertyValue("nwgproduct:price");
        }
        catch (NullPointerException ex) {
            if (log.isDebugEnabled()) {
                log.debug("computePrice(..) has been called on a product which has no price currently set. Product: " + documentModel.getPath());
            }
            currentPrice = 0d;
        }

        double newPrice = currentPrice + this.getPriceToAdd();

        if (log.isDebugEnabled()) {
            log.debug("Old price: " + currentPrice + " | New price: " + newPrice);
        }

        // TODO potentiellement déporter le set dans l'operation, qui fait également le save. Le computePrice ne ferait alors que le calcul du prix
        // Ou alors passer le coreSession en input de cette méthode, et s'assurer de faire le coreSession.save() après le set
        documentModel.setPropertyValue("nwgproduct:price", newPrice);
        coreSession.save();

        return newPrice;
    }

    @Override
    public boolean moveLinkedVisualsToHiddenFolder(DocumentModel documentModel, CoreSession coreSession) {
        // If we need to get/create a CoreSession on the fly:
        // Framework.getService(CoreSessionService.class).createCoreSession(String repository, NuxeoPrincipal nuxeoPrincipal)

        // If we need to execute something using a CoreSession similar to admin, a possibilité is to use:
        // UnrestrictedSessionRunner: by implementing a class you get a job without restrictions, which has a run() method
        // If needed see with Florent

        if (log.isDebugEnabled()) {
            log.debug("moveLinkedVisualsToHiddenFolder(..) method called on document: " + documentModel.getPathAsString());
        }

        Collection docCollection = documentModel.getAdapter(Collection.class);

        List<String> collectedDocumentIds = docCollection.getCollectedDocumentIds();
        if (collectedDocumentIds.size() == 0) {
            log.debug("No NWGVisuals documents attached to this document");

            return false;
        }

        List<DocumentRef> listIdRef = new ArrayList<>();
        for (String collectedDocumentId : collectedDocumentIds) {
            IdRef idRef = new IdRef(collectedDocumentId);
            DocumentModel visual = coreSession.getDocument(idRef);
            listIdRef.add(visual.getRef());
        }

        // If we also want to remove visual from the collection, the following lines should do the trick
        // It would require to also create a DocumentModelList to populate in the above loop
        /*
        CollectionManager collectionManager = Framework.getService(CollectionManager.class);
        collectionManager.removeAllFromCollection(documentModel, visualList, coreSession);
        */


        // We then try to retrieve the default folder where to move visuals
        // Retrieve a document using a query
        String queryForGettingDefaultFolder = "SELECT * FROM Folder WHERE ecm:mixinType != 'HiddenInNavigation'" +
                " AND ecm:isProxy = 0 AND ecm:isCheckedInVersion = 0 AND ecm:currentLifeCycleState != 'deleted'" +
                " AND dc:title='" + DEFAULT_FOLDER_NAME_WHERE_TO_MOVE_VISUALS + "'";
        DocumentModelList queryResults = coreSession.query(queryForGettingDefaultFolder);

        if (queryResults.size() == 0) {
            if (log.isDebugEnabled()) {
                log.debug("Was unable to move visuals as no Folder were found with the name '" + DEFAULT_FOLDER_NAME_WHERE_TO_MOVE_VISUALS + "'.");
            }
            return false;
        }
        DocumentModel folder = queryResults.get(0);

        coreSession.move(listIdRef, folder.getRef());

        return true;
    }

    private double getPriceToAdd() {
        if (this.getOverriddenPriceToAdd() != 0) {
            if (log.isDebugEnabled()) {
                log.debug("Contribution found with the following value: " + this.getOverriddenPriceToAdd());
            }
            return this.getOverriddenPriceToAdd();
        } else {
            return PRICE_TO_ADD;
        }
    }
}
