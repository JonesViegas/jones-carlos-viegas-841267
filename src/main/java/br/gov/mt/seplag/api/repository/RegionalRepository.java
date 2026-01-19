package br.gov.mt.seplag.api.repository;

import br.gov.mt.seplag.api.model.Regional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RegionalRepository extends JpaRepository<Regional, Integer> {
    
    // Este método é importante: ele busca no banco apenas as regionais que estão marcadas como 'ativo = true'
    List<Regional> findAllByAtivoTrue();
}