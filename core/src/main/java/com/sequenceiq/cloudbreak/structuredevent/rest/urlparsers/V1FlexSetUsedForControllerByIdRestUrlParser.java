package com.sequenceiq.cloudbreak.structuredevent.rest.urlparsers;

import java.util.regex.Matcher;

import org.springframework.stereotype.Component;

@Component
public class V1FlexSetUsedForControllerByIdRestUrlParser extends RestUrlParser {

    public static final int RESOURCE_ID_GROUP_NUMBER = 3;

    @Override
    public String getPattern() {
        return "v1\\/flexsubscriptions(\\/(user|account))?\\/setusedforcontroller\\/(\\d+)";
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
        return "flexsubscriptions";
    }

    @Override
    protected String getResourceEvent(Matcher matcher) {
        return "setusedforcontroller";
    }
}
