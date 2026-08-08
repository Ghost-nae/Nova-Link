package com.nova.Nova_Link.Entities;


import com.nova.Nova_Link.ENUMS.TransactionStatus;
import com.nova.Nova_Link.ENUMS.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name= "transactions")
@Getter
@Setter
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_account_id", nullable = false)
    private Account senderAccount;

    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="recipient_account_id", nullable = false)
    private Account recipientAccount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    private String description;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Transaction() {}

    @PrePersist
    public void prePersist() {
        this.transactionId = UUID.randomUUID();
        this.timestamp = LocalDateTime.now();
    }

}
