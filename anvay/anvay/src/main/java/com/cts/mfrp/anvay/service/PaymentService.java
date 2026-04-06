package com.cts.mfrp.anvay.service;

import java.util.List;

import com.cts.mfrp.anvay.entity.Payment;

public interface PaymentService {
    Payment createPayment(Payment payment);
    Payment getPaymentById(Integer paymentId);
    List<Payment> getPaymentsByUserId(Integer userId);
    Payment updatePayment(Integer paymentId, Payment payment);
    void deletePayment(Integer paymentId);
}
