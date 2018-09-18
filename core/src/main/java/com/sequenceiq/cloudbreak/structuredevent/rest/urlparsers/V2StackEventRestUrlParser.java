package com.sequenceiq.cloudbreak.structuredevent.rest.urlparsers;

import java.util.regex.Matcher;

import org.springframework.stereotype.Component;

@Component
public class V2StackEventRestUrlParser extends RestUrlParser {

    public static final int RESOURCE_NAME_GROUP_NUMBER = 1;

    public static final int RESOURCE_EVENT_GROUP_NUMBER = 2;

    @Override
    protected String getAntiPattern() {
        return ".*(scaling|start|stop|sync|reinstall).*";
    }

    @Override
    public String getPattern() {
        return "v2\\/stacks\\/(?!account|user)([^\\/\\d]+)\\/([a-z|-]+)";
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
        return "stacks";
    }

    @Override
    protected String getResourceEvent(Matcher matcher) {
        return matcher.group(RESOURCE_EVENT_GROUP_NUMBER);
    }
}
