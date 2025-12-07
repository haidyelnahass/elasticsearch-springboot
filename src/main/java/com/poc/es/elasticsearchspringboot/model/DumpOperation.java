package com.poc.es.elasticsearchspringboot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.poc.es.elasticsearchspringboot.enums.OperationsEnum;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DumpOperation {
    private OperationsEnum operation;
    private Dump queryData;
    private Dump dumpData;
    public DumpOperation(OperationsEnum operation, Dump queryData, Dump dumpData) {
        this.operation = operation;
        this.queryData = queryData;
        this.dumpData = dumpData;
    }
    public OperationsEnum getOperation() {
        return operation;
    }
    public void setOperation(OperationsEnum operation) {
        this.operation = operation;
    }
    public Dump getQueryData() {
        return queryData;
    }
    public void setQueryData(Dump queryData) {
        this.queryData = queryData;
    }
    public Dump getDumpData() {
        return dumpData;
    }
    public void setDumpData(Dump dumpData) {
        this.dumpData = dumpData;
    }
}
