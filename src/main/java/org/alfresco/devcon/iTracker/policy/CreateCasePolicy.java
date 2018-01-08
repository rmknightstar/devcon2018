package org.alfresco.devcon.iTracker.policy;

import org.alfresco.devcon.iTracker.IssueTrackerConstants;
import org.alfresco.devcon.iTracker.impl.IssueTrackerComponent;
import org.alfresco.repo.node.NodeServicePolicies.OnCreateNodePolicy;
import org.alfresco.repo.policy.Behaviour.NotificationFrequency;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CreateCasePolicy implements OnCreateNodePolicy {

	private PolicyComponent policyComponent;
	IssueTrackerComponent issueTrackerComponent;
	private static final Log logger = LogFactory.getLog(CreateCasePolicy.class);

	public void setPolicyComponent(PolicyComponent policyComponent) {
		this.policyComponent = policyComponent;
	}



	public void initialise() {

		this.policyComponent.bindClassBehaviour(
				OnCreateNodePolicy.QNAME,
				IssueTrackerConstants.TYPE_CASE,
				new JavaBehaviour(this, OnCreateNodePolicy.QNAME.getLocalName(), NotificationFrequency.TRANSACTION_COMMIT));
		logger.debug("INITIALISED");
	}	


	@Override
	public void onCreateNode(ChildAssociationRef childAssocRef) {
		issueTrackerComponent.registerCaseId(childAssocRef.getChildRef());
	}

}