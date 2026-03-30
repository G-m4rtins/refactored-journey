package com.example.ejb;

import com.example.ejb.exception.BeneficioBusinessException;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Stateless
@Service
public class BeneficioEjbService {

    @PersistenceContext
    private EntityManager em;

    @Transactional(rollbackFor = Exception.class)
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        validateTransferRequest(fromId, toId, amount);

        Long firstLockId = Math.min(fromId, toId);
        Long secondLockId = Math.max(fromId, toId);

        Beneficio firstLocked = em.find(Beneficio.class, firstLockId, LockModeType.PESSIMISTIC_WRITE);
        Beneficio secondLocked = em.find(Beneficio.class, secondLockId, LockModeType.PESSIMISTIC_WRITE);

        if (firstLocked == null || secondLocked == null) {
            throw new BeneficioBusinessException("Beneficio de origem ou destino nao encontrado.");
        }

        Beneficio from = fromId.equals(firstLockId) ? firstLocked : secondLocked;
        Beneficio to = toId.equals(secondLockId) ? secondLocked : firstLocked;

        validateBeneficioState(from, to, amount);

        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));
        em.flush();
    }

    private void validateTransferRequest(Long fromId, Long toId, BigDecimal amount) {
        if (fromId == null || toId == null) {
            throw new BeneficioBusinessException("Origem e destino sao obrigatorios.");
        }
        if (fromId.equals(toId)) {
            throw new BeneficioBusinessException("Origem e destino devem ser diferentes.");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new BeneficioBusinessException("Valor da transferencia deve ser maior que zero.");
        }
    }

    private void validateBeneficioState(Beneficio from, Beneficio to, BigDecimal amount) {
        if (!Boolean.TRUE.equals(from.getAtivo()) || !Boolean.TRUE.equals(to.getAtivo())) {
            throw new BeneficioBusinessException("Beneficios inativos nao podem participar de transferencia.");
        }
        if (from.getValor().compareTo(amount) < 0) {
            throw new BeneficioBusinessException("Saldo insuficiente para transferencia.");
        }
    }
}
