package org.nuxeo.workshopguide.endpoints;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class NuxeoWorkshopGuideApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        HashSet<Class<?>> result = new HashSet<Class<?>>();
        result.add(ProductEndPoint.class);
        return result;
    }
}
