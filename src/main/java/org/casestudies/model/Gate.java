package org.casestudies.model;

import org.casestudies.enums.GateStatus;

public abstract class Gate extends BaseModel {
    private int number;
    private Operator operator;
    private GateStatus gateStatus;

    protected Gate(int number) {
        this.number = number;
        this.gateStatus = GateStatus.CLOSED;
    }

    // Getter for gate number
    public int getNumber() {
        return number;
    }

    // Getter Setter for operator
    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    // Getter Setter for gate status
    public GateStatus getGateStatus() {
        return gateStatus;
    }

    public void setGateStatus(GateStatus gateStatus) {
        this.gateStatus = gateStatus;
    }
}
