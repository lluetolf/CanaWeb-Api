package ch.canaweb.api.controller;

import ch.canaweb.api.core.Receivable.Receivable;
import ch.canaweb.api.core.Receivable.ReceivableService;
import ch.canaweb.api.persistence.ReceivableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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
}
