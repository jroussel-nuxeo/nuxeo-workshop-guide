package org.nuxeo.workshopguide.features;

import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.SimpleFeature;

@Features({AutomationFeature.class})
@Deploy({"org.nuxeo.workshopguide.nuxeo-workshop-guide-core", "org.nuxeo.ecm.platform.filemanager.core", "org.nuxeo.ecm.webapp.core", "org.nuxeo.ecm.platform.collections.core", "studio.extensions.jroussel-SANDBOX"})
public class NuxeoWorkshopGuideDefaultFeature extends SimpleFeature {

}
