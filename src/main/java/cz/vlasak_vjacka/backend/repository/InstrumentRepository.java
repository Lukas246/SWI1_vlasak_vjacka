package cz.vlasak_vjacka.backend.repository;

import cz.vlasak_vjacka.backend.model.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, String> {

}