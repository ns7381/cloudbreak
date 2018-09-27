package com.sequenceiq.cloudbreak.structuredevent.rest.urlparsers;

import java.util.List;
import java.util.regex.Matcher;

import org.springframework.stereotype.Component;

@Component
public class V1V2ExistingResourceByIdEventRestUrlParser extends RestUrlParser {

    public static final int RESOURCE_TYPE_GROUP_NUMBER = 1;

    public static final int RESOURCE_ID_GROUP_NUMBER = 4;

    public static final int RESOURCE_EVENT_GROUP_NUMBER = 5;

    @Override
    protected List<String> parsedMethods() {
        return List.of("POST", "PUT", "GET");
    }

    @Override
    public String getPattern() {
        return "v[12]\\/([a-z|-]+)(\\/(user|account))?\\/(\\d+)\\/(.+)";
    }

    @Override
    protected String getWorkspaceId(Matcher matcher) {
        return null;
    }

    @Override
    protected String getResourceName(Matcher matcher) {
        return null;
    }

    @Override
    protected String getResourceId(Matcher matcher) {
        return matcher.group(RESOURCE_ID_GROUP_NUMBER);
    }

    @Override
    protected String getResourceType(Matcher matcher) {
        return matcher.group(RESOURCE_TYPE_GROUP_NUMBER);
    }

    @Override
    protected String getResourceEvent(Matcher matcher) {
        return matcher.group(RESOURCE_EVENT_GROUP_NUMBER);
    }
}
