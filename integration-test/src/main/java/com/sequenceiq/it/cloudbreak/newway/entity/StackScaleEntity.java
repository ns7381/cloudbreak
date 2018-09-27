package com.sequenceiq.it.cloudbreak.newway.entity;

import javax.ws.rs.core.Response;

import com.sequenceiq.cloudbreak.api.model.stack.StackScaleRequestV2;
import com.sequenceiq.it.cloudbreak.newway.AbstractCloudbreakEntity;
import com.sequenceiq.it.cloudbreak.newway.Prototype;
import com.sequenceiq.it.cloudbreak.newway.context.TestContext;

@Prototype
public class StackScaleEntity extends AbstractCloudbreakEntity<StackScaleRequestV2, Response, StackScaleEntity> {

    public StackScaleEntity(StackScaleRequestV2 request, TestContext testContex) {
        super(request, testContex);
    }

    public StackScaleEntity(TestContext testContex) {
        super(new StackScaleRequestV2(), testContex);
    }

    public StackScaleEntity valid() {
        return  withGroup("master")
                .withDesiredCount(100);
    }

    public StackScaleEntity withGroup(String group) {
        getRequest().setGroup(group);
        return this;
    }

    public StackScaleEntity withDesiredCount(Integer count) {
        getRequest().setDesiredCount(count);
        return this;
    }
}
