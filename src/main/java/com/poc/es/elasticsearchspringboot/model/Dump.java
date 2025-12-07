package com.poc.es.elasticsearchspringboot.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dump {

    
    private String _id;
    private String md_msg_id;
    private String md_flow_id;
    private String md_msg_tp;
    private String md_creation_tmstmp;
    private String md_chnl_id;
    private String md_rltn_type;
    private String md_rltn_val;
    private String md_fun_id;
    private String md_msg_data;
    private String md_brkr_name;
    private String md_bank_id;
    private String md_branch_id;
    private String md_terminal_id;
    private String md_card_num;
    private String md_terminal_desc;
    private String md_request_id;
    private String md_service_id;
    private String md_app_name;
    private String md_srvr_name;
    private String md_qmgr_name;
    private String md_eg_name;
    private String md_process_id;
    private String md_session_id;
    private String md_party_id;
    private String md_msg_data1;
    private String md_msg_data2;
    private String md_msg_data3;
    private String md_msg_data4;
    private Integer size;
    public Dump(String md_msg_id, String md_flow_id, String md_msg_tp, String md_creation_tmstmp, String md_chnl_id,
            String md_rltn_type, String md_rltn_val, String md_fun_id, String md_msg_data, String md_brkr_name,
            String md_bank_id, String md_branch_id, String md_terminal_id, String md_card_num, String md_terminal_desc,
            String md_request_id, String md_service_id, String md_app_name, String md_srvr_name, String md_qmgr_name,
            String md_eg_name, String md_process_id, String md_session_id, String md_party_id, String md_msg_data1,
            String md_msg_data2, String md_msg_data3, String md_msg_data4) {
        this.md_msg_id = md_msg_id;
        this.md_flow_id = md_flow_id;
        this.md_msg_tp = md_msg_tp;
        this.md_creation_tmstmp = md_creation_tmstmp;
        this.md_chnl_id = md_chnl_id;
        this.md_rltn_type = md_rltn_type;
        this.md_rltn_val = md_rltn_val;
        this.md_fun_id = md_fun_id;
        this.md_msg_data = md_msg_data;
        this.md_brkr_name = md_brkr_name;
        this.md_bank_id = md_bank_id;
        this.md_branch_id = md_branch_id;
        this.md_terminal_id = md_terminal_id;
        this.md_card_num = md_card_num;
        this.md_terminal_desc = md_terminal_desc;
        this.md_request_id = md_request_id;
        this.md_service_id = md_service_id;
        this.md_app_name = md_app_name;
        this.md_srvr_name = md_srvr_name;
        this.md_qmgr_name = md_qmgr_name;
        this.md_eg_name = md_eg_name;
        this.md_process_id = md_process_id;
        this.md_session_id = md_session_id;
        this.md_party_id = md_party_id;
        this.md_msg_data1 = md_msg_data1;
        this.md_msg_data2 = md_msg_data2;
        this.md_msg_data3 = md_msg_data3;
        this.md_msg_data4 = md_msg_data4;
    }

    public Dump(String _id, Dump hit) {
		this._id = _id;
        this.md_msg_id = hit.md_msg_id;
        this.md_flow_id = hit.md_flow_id;
        this.md_msg_tp = hit.md_msg_tp;
        this.md_creation_tmstmp = hit.md_creation_tmstmp;
        this.md_chnl_id = hit.md_chnl_id;
        this.md_rltn_type = hit.md_rltn_type;
        this.md_rltn_val = hit.md_rltn_val;
        this.md_fun_id = hit.md_fun_id;
        this.md_msg_data = hit.md_msg_data;
        this.md_brkr_name = hit.md_brkr_name;
        this.md_bank_id = hit.md_bank_id;
        this.md_branch_id = hit.md_branch_id;
        this.md_terminal_id = hit.md_terminal_id;
        this.md_card_num = hit.md_card_num;
        this.md_terminal_desc = hit.md_terminal_desc;
        this.md_request_id = hit.md_request_id;
        this.md_service_id = hit.md_service_id;
        this.md_app_name = hit.md_app_name;
        this.md_srvr_name = hit.md_srvr_name;
        this.md_qmgr_name = hit.md_qmgr_name;
        this.md_eg_name = hit.md_eg_name;
        this.md_process_id = hit.md_process_id;
        this.md_session_id = hit.md_session_id;
        this.md_party_id = hit.md_party_id;
        this.md_msg_data1 = hit.md_msg_data1;
        this.md_msg_data2 = hit.md_msg_data2;
        this.md_msg_data3 = hit.md_msg_data3;
        this.md_msg_data4 = hit.md_msg_data4;
	}
    
}
