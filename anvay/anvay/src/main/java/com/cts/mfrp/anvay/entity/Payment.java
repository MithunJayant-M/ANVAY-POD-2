package com.cts.mfrp.anvay.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "event_id")
    private Integer eventId;

    @Column(name = "institution_id")
    private Integer institutionId;

    @Column(name = "payment_type", length = 100)
    private String paymentType; // registration, membership

    @Column(name = "amount")
    private Double amount;

    @Column(name = "payment_method", length = 100)
    private String paymentMethod; // online, cash, card

    @Column(name = "transaction_id", length = 255)
    private String transactionId;

    @Column(name = "status", length = 50)
    private String status; // pending, completed, failed, refunded

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @PrePersist
    protected void onCreate() {
        if (paidAt == null) paidAt = LocalDateTime.now();
        if (status == null) status = "pending";
    }
}
