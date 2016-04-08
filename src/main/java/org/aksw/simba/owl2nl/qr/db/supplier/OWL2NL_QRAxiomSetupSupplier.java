package org.aksw.simba.owl2nl.qr.db.supplier;

import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRExperimentSetup;
import org.springframework.jdbc.core.RowMapper;

public abstract class OWL2NL_QRAxiomSetupSupplier extends OWL2NL_QRSetupSupplier {
    protected static final String AXIOM_QUERY = "SELECT A.axiom, A.verbalization FROM Axioms AS A WHERE A.id=?;";

    public OWL2NL_QRAxiomSetupSupplier(RowMapper<OWL2NL_QRExperimentSetup> rowMapper) {
        super(rowMapper);
    }
}
