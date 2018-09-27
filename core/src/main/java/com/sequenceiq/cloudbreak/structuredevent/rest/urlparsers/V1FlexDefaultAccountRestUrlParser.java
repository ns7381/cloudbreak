package com.sequenceiq.cloudbreak.structuredevent.rest.urlparsers;

import java.util.regex.Matcher;

import org.springframework.stereotype.Component;

@Component
public class V1FlexDefaultAccountRestUrlParser extends RestUrlParser {

    public static final int RESOURCE_NAME_GROUP_NUMBER = 2;

    @Override
    public String getPattern() {
        return "v1\\/flexsubscriptions\\/(account|user)\\/setdefault\\/([^\\d][^\\/]+)";
    }

    @Override
    protected String getWorkspaceId(Matcher matcher) {
        return null;
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
        return "setdefault";
    }
}
