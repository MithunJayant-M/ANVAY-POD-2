package com.cts.mfrp.anvay.service.impl;

import com.cts.mfrp.anvay.entity.Payment;
import com.cts.mfrp.anvay.repository.PaymentRepository;
import com.cts.mfrp.anvay.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public Payment createPayment(Payment payment) {
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public Payment getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    @Override
    public Payment updatePayment(Long paymentId, Payment payment) {
        Payment existing = getPaymentById(paymentId);
        if (payment.getAmount() != null) existing.setAmount(payment.getAmount());
        if (payment.getStatus() != null) existing.setStatus(payment.getStatus());
        if (payment.getDescription() != null) existing.setDescription(payment.getDescription());
        existing.setUpdatedAt(LocalDateTime.now());
        return paymentRepository.save(existing);
    }

    @Override
    public void deletePayment(Long paymentId) {
        paymentRepository.deleteById(paymentId);
    }
}
