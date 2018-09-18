package com.sequenceiq.cloudbreak.structuredevent.rest.urlparsers;

import java.util.regex.Matcher;

import org.springframework.stereotype.Component;

@Component
public class V3FlexEventRestUrlParser extends RestUrlParser {

    public static final int WORKSPACE_ID_GROUP_NUMBER = 1;

    public static final int RESOURCE_NAME_GROUP_NUMBER = 3;

    public static final int RESOURCE_EVENT_GROUP_NUMBER = 2;

    @Override
    public String getPattern() {
        return "v3\\/(\\d+)\\/flexsubscriptions\\/([a-z|A-Z|-]+)\\/([^\\/]+)";
    }

    @Override
    protected String getWorkspaceId(Matcher matcher) {
        return matcher.group(WORKSPACE_ID_GROUP_NUMBER);
    }

    @Override
    protected String getResourceName(Matcher matcher) {
        return matcher.group(RESOURCE_NAME_GROUP_NUMBER);
    }

    @Override
    protected String getResourceId(Matcher matcher) {
        return null;
    }

    @Override
    protected String getResourceType(Matcher matcher) {
        return "flexsubscriptions";
    }

    @Override
    protected String getResourceEvent(Matcher matcher) {
        return matcher.group(RESOURCE_EVENT_GROUP_NUMBER);
    }
}
