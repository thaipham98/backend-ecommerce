package com.example.finalproject.Controller;

import com.example.finalproject.Manager.PaxosManager;
import com.example.finalproject.Model.Promise;
import com.example.finalproject.Response.ResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PaxosController {

    private PaxosManager paxosManager;

    @PostMapping("/prepare")
    public ResponseEntity<Object> prepare(@RequestParam Long proposalId) {
        Object result = paxosManager.prepare(proposalId);
        return ResponseHandler.generateResponse("Success preparing!", HttpStatus.OK, result);
    }

    @PostMapping("/accept")
    public ResponseEntity<Object> accept(@RequestParam Long currentProposal, HttpServletRequest request) {
        Object result = paxosManager.accept(currentProposal, request);
        return ResponseHandler.generateResponse("Success accepting!", HttpStatus.OK, result);
    }

    @PostMapping("/decide")
    public ResponseEntity<Object> decide() {
        Object result = paxosManager.decide();
        return ResponseHandler.generateResponse("Success deciding!", HttpStatus.OK, result);
    }

}
