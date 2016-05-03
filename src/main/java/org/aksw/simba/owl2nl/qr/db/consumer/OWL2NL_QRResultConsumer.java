package org.aksw.simba.owl2nl.qr.db.consumer;

import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRExperimentResult;
import org.aksw.simba.owl2nl.qr.data.rowMapper.OWL2NL_QRObjectRowMapper;
import org.aksw.simba.qr.db.ExperimentResultConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OWL2NL_QRResultConsumer implements ExperimentResultConsumer<OWL2NL_QRExperimentResult> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(OWL2NL_QRResultConsumer.class);

    protected static final OWL2NL_QRObjectRowMapper OBJECT_ROW_MAPPER = new OWL2NL_QRObjectRowMapper();

}
