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

package org.apromore.portal.dialogController.workspaceOptions;

// import org.apromore.exception.NotAuthorizedException;
import org.apromore.portal.common.UserSessionManager;
import org.apromore.portal.dialogController.BaseController;
import org.apromore.portal.dialogController.BaseListboxController;
import org.apromore.portal.dialogController.ProcessListboxController;
import org.apromore.portal.dialogController.MainController;
import org.apromore.model.ExportFormatResultType;
import org.apromore.model.ImportProcessResultType;
import org.apromore.model.FolderType;
import org.apromore.model.ProcessSummaryType;
import org.apromore.model.LogSummaryType;
import org.apromore.model.SummariesType;
import org.apromore.model.SummaryType;
import org.apromore.model.UserType;
import org.apromore.model.VersionSummaryType;
import org.apromore.dao.model.User;


import org.zkoss.zul.Messagebox;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.io.IOException;
import javax.xml.datatype.DatatypeFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CopyAndPasteController extends BaseController {

    private final int MAX_RECURSIVE = 4;
    private final String DOMAIN = "";
    private final String NATIVE_TYPE = "BPMN 2.0";
    private final boolean IS_PUBLIC = false;
    private final int PAGE_INDEX = 0;
    private final int PAGE_SIZE = 100;

    private MainController mainController;
    private UserType user;
    private String userName;
    private String userId;
    private Integer selectedTargetFolderId = null;
    private ArrayList<Object> selectedItems = new ArrayList<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(CopyAndPasteController.class);

    public CopyAndPasteController(MainController mainController, UserType user) {
        this.mainController = mainController;
        this.user = user;
        this.userName = user.getUsername();
        this.userId = user.getId();
    }

    public String genCreatedDate() throws Exception {
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()).toString();
    }

    public List<FolderType> getSubFolders(Integer folderId) {
        return getService().getSubFolders(userId, folderId == null ? 0 : folderId);
    }

    public Set<Integer> getSubFolderIds(Integer folderId) {
        List<FolderType> subFolders = getSubFolders(folderId);
        Set<Integer> idSet = new HashSet<Integer>();
        for(FolderType subFolder: subFolders) {
            idSet.add(subFolder.getId());
        }
        return idSet;
    }

    public Integer createFolder(Integer targetFolderId, String folderName) throws Exception {
        Set<Integer> targetSubFolderIds = getSubFolderIds(targetFolderId);
        getService().createFolder(userId, folderName, targetFolderId);
        Set<Integer> newTargetSubFolderIds = getSubFolderIds(targetFolderId);
        newTargetSubFolderIds.removeAll(targetSubFolderIds);
        Integer newFolderId;
        try {
            newFolderId = (Integer)newTargetSubFolderIds.iterator().next();
        } catch (Exception e) {
            newFolderId = -1;
        }
        return newFolderId;
    }

    private void cloneFolder(FolderType folder, Integer targetFolderId, int level) throws Exception {
        Integer folderId = folder.getId();
        String folderName = folder.getFolderName();
        List<FolderType> sourceSubFolders = folder.getFolders();
        Integer newTargetFolderId = createFolder(targetFolderId, folderName);
        if (newTargetFolderId < 0) {
            LOGGER.error("Fail to clone folder");
            return;
        }

        SummariesType logSummaries = getService().getLogSummaries(userId, folderId, PAGE_INDEX, PAGE_SIZE);
        for(SummaryType summaryType : logSummaries.getSummary()) {
            cloneLog((LogSummaryType) summaryType, newTargetFolderId);
        }
        SummariesType processSummaries = getService().getProcessSummaries(userId, folderId, PAGE_INDEX, PAGE_SIZE);
        for(SummaryType summaryType : processSummaries.getSummary()) {
            cloneModel((ProcessSummaryType) summaryType, newTargetFolderId);
        }
        if (level < MAX_RECURSIVE) {
            for(FolderType subFolder: sourceSubFolders) {
                cloneFolder(subFolder, newTargetFolderId, level + 1);
            }
        }
    }

    private void cloneLog(LogSummaryType log, Integer targetFolderId) throws Exception {
        if (log == null || targetFolderId == null) {
            LOGGER.error("No log or target folder is defined");
            return;
        }
        String created = genCreatedDate();
        String logName = log.getName();
        Integer sourceLogId = log.getId();
        this.getEventLogService().cloneLog(userName, targetFolderId, logName, sourceLogId, DOMAIN, created, false);
    }

    private void cloneModel(ProcessSummaryType model, Integer targetFolderId) throws Exception {
        if (model == null || targetFolderId == null) {
            LOGGER.error("No process or target folder is defined");
            return;
        }
        String created = genCreatedDate();
        String processName = model.getName();
        Integer sourceProcessId = model.getId();
        VersionSummaryType version = model.getVersionSummaries().get(0);

        ExportFormatResultType exportResult = this.getService().exportFormat(
                sourceProcessId, processName, version.getName(),
                version.getVersionNumber(), model.getOriginalNativeType(), userName
        );
        InputStream nativeStream = exportResult.getNative().getInputStream();
        ImportProcessResultType importResult = mainController
                .getService()
                .importProcess(
                        userName, targetFolderId, NATIVE_TYPE, processName,
                        version.getVersionNumber(), nativeStream, DOMAIN, "",
                        created, created, IS_PUBLIC
                );
    }

    public void clearSelectedItems() {
        selectedItems.clear();
    }

    public ArrayList<Object> getSelectedItems() {
        return selectedItems;
    }

    public int getSelectedItemsSize() {
        return selectedItems.size();
    }

    private void updateSelectedItems(Set<Object> selections) {
        this.clearSelectedItems();
        for (Object obj : selections) {
            if (obj instanceof FolderType) {
                selectedItems.add((FolderType) obj);
            } else if (obj instanceof LogSummaryType) {
                selectedItems.add((LogSummaryType) obj);
            } else if (obj instanceof ProcessSummaryType) {
                selectedItems.add((ProcessSummaryType) obj);
            }
        }
    }

    public void cloneSelectedItems() throws Exception {
        for (Object obj : selectedItems) {
            if (obj instanceof FolderType) {
                cloneFolder((FolderType) obj, selectedTargetFolderId, 0);
            } else if (obj instanceof LogSummaryType) {
                cloneLog((LogSummaryType) obj, selectedTargetFolderId);
            } else if (obj instanceof ProcessSummaryType) {
                cloneModel((ProcessSummaryType) obj, selectedTargetFolderId);
            }
        }
    }

    public void copy(Set<Object> selections) {
        updateSelectedItems(selections);
        if (selectedItems.isEmpty()) {
            Messagebox.show("Please select at least one item.", "Copy", Messagebox.OK, Messagebox.ERROR);
        } else {
            Messagebox.show(getSelectedItemsSize() +
                    " item(s) has been selected for copying.", "Copy", Messagebox.OK, Messagebox.INFORMATION);
        }
    }

    public void paste(Integer targetFolderId) throws Exception {
        selectedTargetFolderId = targetFolderId;
        if (selectedItems.isEmpty()) {
            Messagebox.show("No item has been copied", "Copy", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        cloneSelectedItems();
    }
}
