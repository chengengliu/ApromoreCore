/*-
 * #%L
 * This file is part of "Apromore Core".
 * %%
 * Copyright (C) 2018 - 2020 Apromore Pty Ltd.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

package org.apromore.plugin.portal.processdiscoverer.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;

import org.apromore.plugin.portal.processdiscoverer.PDController;
import org.apromore.plugin.portal.processdiscoverer.data.ContextData;
import org.apromore.plugin.portal.processdiscoverer.data.LogData;
import org.apromore.plugin.portal.processdiscoverer.data.UserOptionsData;
import org.apromore.plugin.portal.processdiscoverer.utils.InputDialog;
import org.deckfour.xes.model.XLog;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;

public class LogExportController extends AbstractController {
    private ContextData contextData;
    private UserOptionsData userOptions;
    private LogData logData;
    
    public LogExportController(PDController controller) {
        super(controller);
        contextData = parent.getContextData();
        userOptions = parent.getUserOptions();
        logData = parent.getLogData();
    }
    
    @Override
    public void onEvent(Event event) throws Exception {
        if (!parent.prepareCriticalServices()) {
            return;
        }
        
        InputDialog.showInputDialog(
            Labels.getLabel("e.pd.saveLogWin.text"), // "Save filtered log",
            "Enter a log name (no more than 60 characters)", 
            contextData.getLogName() + "_filtered", 
            "^[a-zA-Z0-9_\\(\\)\\-\\s\\.]{1,60}$",
            "a-z, A-Z, 0-9, (), hyphen, underscore, space and dot. No more than 60 chars.",
            new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if (event.getName().equals("onOK")) {
                        String logName = (String)event.getData();
//                        userOptions.setActivityFilterValue(activities.getCurpos());
//                        userOptions.setArcFilterValue(arcs.getCurpos());
                        saveLog(logData.getLog().getActualXLog(), logName);
                    }
                }
            });
    }
    
    private void saveLog(XLog filtered_log, String logName) {
        try {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            parent.getEvenLogService().exportToStream(outputStream, filtered_log);

            parent.getEvenLogService().importLog(contextData.getPortalContext().getCurrentUser().getUsername(), contextData.getFolderId(),
                    logName, new ByteArrayInputStream(outputStream.toByteArray()), "xes.gz",
                    contextData.getDomain(), DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()).toString(),
                    false);
            
            Messagebox.show("A new log named '" + logName + "' has been saved in the '" + contextData.getFolderName() + "' folder.", Labels.getLabel("brand.name"), Messagebox.OK, Messagebox.NONE);

            contextData.getPortalContext().refreshContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
