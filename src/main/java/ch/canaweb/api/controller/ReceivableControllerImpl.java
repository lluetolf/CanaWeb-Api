package ch.canaweb.api.controller;

import ch.canaweb.api.core.Payable.Payable;
import ch.canaweb.api.core.Receivable.Receivable;
import ch.canaweb.api.core.Receivable.ReceivableService;
import ch.canaweb.api.error.BaseHttpException;
import ch.canaweb.api.persistence.ReceivableRepository;
import com.google.cloud.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ReceivableControllerImpl implements ReceivableService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ReceivableRepository repository;

    @Autowired
    public void setRepository(ReceivableRepository repository){
        this.repository=repository;
    }

    @Override
    public Flux<Receivable> getAllReceivables() {
        this.logger.info("getAllReceivables()");
        return this.repository.findAll();
    }

    @Override
    public Mono<Receivable> updateDeductible(String receivableId, String phase, double amount) {
        this.logger.info(String.format("%1$s(%2$s, %2$s, %2$d)", "updateDeductible", receivableId, phase, amount));

        return this.repository.findById(receivableId)
                .flatMap(x -> {
                    if("preliquidation".equals(phase)) {
                        x.getPreliquidation().setDeductible(amount);
                    } else if ("liquidation".equals(phase)) {
                        x.getLiquidation().setDeductible(amount);
                    } else if ("ajuste".equals(phase)) {
                        x.getAjuste().setDeductible(amount);
                    } else {
                        this.logger.info("Failed to update Receivable, phase does not exist", phase);
                        throw new BaseHttpException("Failed to update Receivable, phase does not exist", phase);
                    }
                    return this.repository.save(x);
                })
                .onErrorMap( e -> {
                    this.logger.info("Failed to update Receivable", e.getMessage());
                    return new BaseHttpException("Failed to update Receivable: " + receivableId, e.getMessage());
                });
    }

}
