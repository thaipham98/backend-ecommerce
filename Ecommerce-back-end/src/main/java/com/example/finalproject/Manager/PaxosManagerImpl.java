package com.example.finalproject.Manager;

import com.example.finalproject.Model.Promise;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class PaxosManagerImpl implements PaxosManager{

    private HttpServletRequest acceptedValue = null;
    private Long acceptedProposal = -1L;
    private boolean alreadyAccepted = false;

    @Override
    public Promise prepare(Long currentProposal) {
        if (currentProposal < this.acceptedProposal) {
            return null;
        }

        this.acceptedProposal = currentProposal;

        if (this.alreadyAccepted) {
            return new Promise(this.acceptedProposal, this.acceptedValue).promise().accept();
        }

        return new Promise(currentProposal, null).promise();
    }

    @Override
    public Promise accept(Long currentProposal, HttpServletRequest request) {
        if (currentProposal == this.acceptedProposal) {
            this.alreadyAccepted = true;
            this.acceptedProposal = currentProposal;
            this.acceptedValue = request;
            return new Promise(this.acceptedProposal, this.acceptedValue).accept();
        }
        return null;
    }

    // TODO: rewrite the decide method to pass request to Service
    @Override
    public Boolean decide() {
        reset();
        return true;
    }

    private void reset() {
        this.acceptedValue = null;
        this.acceptedProposal = null;
        this.alreadyAccepted = false;
    }
}
