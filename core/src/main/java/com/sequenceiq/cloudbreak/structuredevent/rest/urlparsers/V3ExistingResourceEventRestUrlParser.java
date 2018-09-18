package com.sequenceiq.cloudbreak.structuredevent.rest.urlparsers;

import java.util.regex.Matcher;

import org.springframework.stereotype.Component;

@Component
public class V3ExistingResourceEventRestUrlParser extends RestUrlParser {

    public static final int WORKSPACE_ID_GROUP_NUMBER = 1;

    public static final int RESOURCE_NAME_GROUP_NUMBER = 3;

    public static final int RESOURCE_TYPE_GROUP_NUMBER = 2;

    public static final int RESOURCE_EVENT_GROUP_NUMBER = 4;

    @Override
    protected String getAntiPattern() {
        return ".*(flexsubscriptions|imagecatalogs|ambari_password).*";
    }

    @Override
    public String getPattern() {
        return "v3\\/(\\d+)\\/([a-z|-]*)\\/([^\\/]+)\\/([a-z|A-Z|-]+)";
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
        return matcher.group(RESOURCE_TYPE_GROUP_NUMBER);
    }

    @Override
    protected String getResourceEvent(Matcher matcher) {
        return matcher.group(RESOURCE_EVENT_GROUP_NUMBER);
    }
}
