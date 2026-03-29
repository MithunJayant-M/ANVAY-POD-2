package com.cts.mfrp.anvay.service;

import java.util.List;

import com.cts.mfrp.anvay.entity.Payment;

public interface PaymentService {
    Payment createPayment(Payment payment);
    Payment getPaymentById(Long paymentId);
    List<Payment> getPaymentsByUserId(Long userId);
    Payment updatePayment(Long paymentId, Payment payment);
    void deletePayment(Long paymentId);
}
