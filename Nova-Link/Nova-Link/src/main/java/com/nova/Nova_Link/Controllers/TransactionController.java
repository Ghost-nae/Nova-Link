package com.nova.Nova_Link.Controllers;

import com.nova.Nova_Link.Service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<String> transferFunds(
            @RequestParam String senderAccountNumber,
            @RequestParam String recipientAccountNumber,
            @RequestParam BigDecimal amount
    ) {

        String response = transactionService.transferFunds(
                senderAccountNumber,
                recipientAccountNumber,
                amount
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reversal")
    public ResponseEntity<String> reversalOfFunds(
            @RequestParam UUID originalTransactionId
    ) {

        String response = transactionService.reversalOfFunds(originalTransactionId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Transaction>> getMyTransactions(
        Authentication authentication) {

        return ResponseEntity.ok(
            transactionService.getMyTransactions(
                    authentication.getName()
                )
        );
    }
}
