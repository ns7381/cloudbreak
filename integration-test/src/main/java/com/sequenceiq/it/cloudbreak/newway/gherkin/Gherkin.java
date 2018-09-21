package com.sequenceiq.it.cloudbreak.newway.gherkin;

import com.sequenceiq.cloudbreak.api.model.JsonEntity;
import com.sequenceiq.it.cloudbreak.newway.ApplicationContextProvider;
import com.sequenceiq.it.cloudbreak.newway.entity.CloudbreakEntity;
import com.sequenceiq.it.cloudbreak.newway.log.Log;

public class Gherkin {

    public static <T extends CloudbreakEntity<T>> T given(Class<T> clss) {
        Log.log("Given " + clss.getSimpleName());
        CloudbreakEntity<T> bean = ApplicationContextProvider.getBean(clss);
        return bean.valid();
    }

    public static <T extends CloudbreakEntity<T>> T given(JsonEntity entity, Class<T> clss) {
        return given(entity, clss, String.format("a %s", entity.getClass().getSimpleName()));
    }

    public static <T extends CloudbreakEntity<T>> T given(JsonEntity entity, Class<T> clss, String message) {
        if (entity == null) {
            throw new IllegalStateException("Entity cannot be null: '" + message + "'");
        }
        Log.log("Given " + message);
        CloudbreakEntity<T> bean = ApplicationContextProvider.getBean(clss, entity);
        return bean.valid();
    }

}
