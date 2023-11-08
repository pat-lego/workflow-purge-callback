package com.adobe.aem.support.workflow.packages.purge;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.core.purge.WorkflowPurgeCallback;
import com.adobe.granite.workflow.exec.Workflow;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;

@Component(immediate = true, service = WorkflowPurgeCallback.class)
public class WorkflowPackagePurgeCallback implements WorkflowPurgeCallback {

    private final Logger log = LoggerFactory.getLogger(WorkflowPackagePurgeCallback.class);

    @Override
    public void onDelete(WorkflowSession session, String instanceId, String payload, String modelId, Workflow.State state)
            throws Exception {
        log.info("Workflow Package Purge Callback execution started, modelId : {}", modelId);
        log.info("Payload : {}, instance Id : {}", payload, instanceId);
        if (payload == null || (! (payload.startsWith("/var/workflow/packages")
                || payload.startsWith("/etc/workflow/packages")))) {
            log.info("Workflow Package purge execution terminated, payload is either null or is not a package");
            return;
        }
        Session s = session.adaptTo(Session.class);
        boolean rmPayload = false;
        if (s.nodeExists(payload)) {
            s.checkPermission(payload, Session.ACTION_REMOVE);
            rmPayload = true;
        }
        if (rmPayload) {
            s.removeItem(payload);
        }
        log.info("Workflow Package execution completed, modelId : {}, Payload: {}", modelId, payload);
    }

}
