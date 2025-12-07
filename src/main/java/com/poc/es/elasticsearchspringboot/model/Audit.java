package com.poc.es.elasticsearchspringboot.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;


@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Audit {
	public Audit(String id, String tenantID, String apiName, String operationID, String categoryID, String processID,
			String processName, String processNameAR) {
		this.id = id;
		this.tenantID = tenantID;
		this.apiName = apiName;
		this.operationID = operationID;
		this.categoryID = categoryID;
		this.processID = processID;
		this.processName = processName;
		this.processNameAR = processNameAR;
	}
	public Audit(String _id, Audit hit) {
		this.id = _id;
		this.tenantID = hit.tenantID;
		this.apiName = hit.apiName;
		this.operationID = hit.operationID;
		this.categoryID = hit.categoryID;
		this.processID = hit.processID;
		this.processName = hit.processName;
		this.processNameAR = hit.processNameAR;
	}

	private String _id;

	private String id;


	private String tenantID;

	
	private String apiName;

	
	private String operationID;

	
	private String categoryID;

	
	private String processID;

	
	private String processName;

	private String processNameAR;

	// private String activityID;

	// private String activityName;

	// private String activityNameAR;

	// private Timestamp timestamp;

	// private String requestID;


	// private String userID;

	
	// private String username;

	// private String organizationID;

	// private String status;

	// private String statusDescription;

	// private String description;

    private Integer size;

}
