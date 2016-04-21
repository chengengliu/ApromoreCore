/*
 * Copyright © 2009-2016 The Apromore Initiative.
 *
 * This file is part of "Apromore".
 *
 * "Apromore" is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * "Apromore" is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.
 * If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package org.apromore.service.metrics.impl;

import au.edu.qut.metrics.ComplexityCalculator;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.apromore.service.metrics.MetricsService;

import java.util.Map;

/**
 * Created by Adriano Augusto on 18/04/2016.
 */
@Service
public class MetricsServiceImpl implements MetricsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsServiceImpl.class);

    @Override
    public Map<String, String> computeMetrics(BPMNDiagram model, boolean size, boolean cfc, boolean acd,
                                              boolean mcd, boolean cnc, boolean density,
                                              boolean structuredness, boolean separability,
                                              boolean duplicates)
    {
        ComplexityCalculator cc = new ComplexityCalculator();
        return cc.computeComplexity( model, size, cfc, acd, mcd, cnc, density,
                                     structuredness, separability, duplicates);
    }
}
