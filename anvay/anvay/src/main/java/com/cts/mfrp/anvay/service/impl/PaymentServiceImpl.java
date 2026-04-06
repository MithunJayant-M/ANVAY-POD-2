package com.cts.mfrp.anvay.service.impl;

import com.cts.mfrp.anvay.entity.Payment;
import com.cts.mfrp.anvay.repository.PaymentRepository;
import com.cts.mfrp.anvay.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public Payment getPaymentById(Integer paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByUserId(Integer userId) {
        return paymentRepository.findByUserId(userId);
    }

    @Override
    public Payment updatePayment(Integer paymentId, Payment payment) {
        Payment existing = getPaymentById(paymentId);
        if (payment.getAmount() != null) existing.setAmount(payment.getAmount());
        if (payment.getStatus() != null) existing.setStatus(payment.getStatus());
        if (payment.getPaymentMethod() != null) existing.setPaymentMethod(payment.getPaymentMethod());
        if (payment.getTransactionId() != null) existing.setTransactionId(payment.getTransactionId());
        return paymentRepository.save(existing);
    }

    @Override
    public void deletePayment(Integer paymentId) {
        paymentRepository.deleteById(paymentId);
    }
}
