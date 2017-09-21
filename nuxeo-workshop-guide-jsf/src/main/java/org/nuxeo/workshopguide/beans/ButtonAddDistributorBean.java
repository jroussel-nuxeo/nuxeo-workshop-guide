package org.nuxeo.workshopguide.beans;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;

import java.io.Serializable;
import java.util.HashMap;

@Name("buttonAddDistributorBean")
@Scope(ScopeType.EVENT)
public class ButtonAddDistributorBean implements Serializable {
    private static final Log log = LogFactory.getLog(ButtonAddDistributorBean.class);

    @In(create = true, required = false)
    protected transient CoreSession documentManager;

    @In(create = true)
    protected NavigationContext navigationContext;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    private String name;

    private String sellLocation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSellLocation() {
        return sellLocation;
    }

    public void setSellLocation(String sellLocation) {
        this.sellLocation = sellLocation;
    }

    public void submit() {
        DocumentModel document = navigationContext.getCurrentDocument();

        if (log.isDebugEnabled()) {
            log.debug("Submit method of ButtonAddDistributorBean has been called on document: " + document.getPathAsString());
            log.debug("'name' value: " + this.getName());
            log.debug("'sellLocation' value: " + this.getSellLocation());
        }

        HashMap<String, Object> distributor = new HashMap<>();
        distributor.put("name", this.getName());
        distributor.put("sellLocation", this.getSellLocation());
        document.setPropertyValue("nwgproduct:distributor", distributor);
        documentManager.saveDocument(document);

        facesMessages.add(StatusMessage.Severity.INFO, "Distributor added.");
    }
}
