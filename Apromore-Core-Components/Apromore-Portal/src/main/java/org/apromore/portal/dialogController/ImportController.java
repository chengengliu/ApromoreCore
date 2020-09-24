/*-
 * #%L
 * This file is part of "Apromore Core".
 * 
 * Copyright (C) 2011 - 2017 Queensland University of Technology.
 * Copyright (C) 2017 Adriano Augusto.
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

package org.apromore.portal.dialogController;

import org.apache.commons.io.FileUtils;
import org.apromore.plugin.portal.FileImporterPlugin;
import org.apromore.portal.ConfigBean;
import org.apromore.portal.common.UserSessionManager;
import org.apromore.portal.exception.*;
import org.apromore.portal.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.*;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ImportController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportController.class);

    private MainController mainC;
    private Window importWindow;

    private String nativeType;
    private String ignoredFiles;

    private Media media = null;
    private Label fileNameLabel;
    private Checkbox isPublicCheckbox;

    private Button okButton;

    private Textbox fileUrl;
    private Label fileNameLabelURL;
    private Button okButton_URL;


    private List<ImportOneProcessController> toImportList = new ArrayList<>();
    private List<ImportOneProcessController> importedList = new ArrayList<>();
    private List<FileImporterPlugin> fileImporterPlugins;

    @FunctionalInterface
    public interface NotificationHandler {
        public void show(String string);
    }

    private NotificationHandler note;

    /** Unit testing constructor. */
    public ImportController(MainController mainC, ConfigBean configBean, List<FileImporterPlugin> fileImporterPlugins, NotificationHandler note) throws DialogException {
        super(configBean);
        this.fileImporterPlugins = fileImporterPlugins;
        this.note = note;
    }

    public ImportController(MainController mainC) throws DialogException {
        this.ignoredFiles = "";
        this.mainC = mainC;
        this.fileImporterPlugins = (List<FileImporterPlugin>) SpringUtil.getBean("fileImporterPlugins");
        //this.note = (message) -> { Messagebox.show(message); };
        this.note = new NotificationHandler() { public void show(String message) { Messagebox.show(message); } };

        try {
            final Window win = (Window) Executions.createComponents("macros/import.zul", null, null);
            this.importWindow = (Window) win.getFellow("importWindow");
            Button uploadButton = (Button) this.importWindow.getFellow("uploadButton");
            this.fileUrl = (Textbox) this.importWindow.getFellow("fileUrl");
            Button uploadURLButton = (Button) this.importWindow.getFellow("uploadURLButton");
            Button cancelButton = (Button) this.importWindow.getFellow("cancelButtonImport");
            Button cancelButtonURL = (Button) this.importWindow.getFellow("cancelButtonImportURL");
            okButton = (Button) this.importWindow.getFellow("okButtonImport");
            okButton_URL = (Button) this.importWindow.getFellow("okButtonImportURL");
            this.fileNameLabel = (Label) this.importWindow.getFellow("fileNameLabel");
            this.fileNameLabelURL = (Label) this.importWindow.getFellow("fileNameLabelURL");
            Label supportedExtL = (Label) this.importWindow.getFellow("supportedExt");
            Label supportedExtURL = (Label) this.importWindow.getFellow("supportedExtURL");
            isPublicCheckbox = ((Checkbox) this.importWindow.getFellow("public"));

            // build the list of supported extensions to display
            SortedSet<String> supportedExt = new TreeSet<>();
            Collections.addAll(supportedExt, "xes", "xes.gz", "mxml", "mxml.gz", "zip");
            supportedExt.addAll(this.mainC.getNativeTypes().keySet());
            List<FileImporterPlugin> fileImporterPlugins = (List<FileImporterPlugin>) SpringUtil.getBean("fileImporterPlugins");
            for (FileImporterPlugin fileImporterPlugin: fileImporterPlugins) {
                supportedExt.addAll(fileImporterPlugin.getFileExtensions());
            }

            String supportedExtS = null;
            for (String aSupportedExt : supportedExt) {
                if (supportedExtS == null) {
                    supportedExtS = aSupportedExt;
                } else {
                    supportedExtS += ", " + aSupportedExt;
                }
            }
            supportedExtL.setValue(supportedExtS);
            supportedExtURL.setValue(supportedExtS);

            uploadButton.addEventListener("onUpload", new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    uploadFile((UploadEvent) event);
                }
            });
            uploadURLButton.addEventListener("onClick", new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    uploadFileFromURL(fileUrl.getValue());
                }
            });
            okButton.addEventListener("onClick", new EventListener<MouseEvent>() {
                public void onEvent(MouseEvent event) throws Exception {
                    importWindow.detach();
                    Sessions.getCurrent().setAttribute("fileimportertarget", ((event.getKeys() & MouseEvent.META_KEY) != 0) ? "page" : "modal");
                    importFile(ImportController.this.media);
                }
            });
            okButton_URL.addEventListener("onClick", new EventListener<MouseEvent>() {
                public void onEvent(MouseEvent event) throws Exception {
                    importWindow.detach();
                    Sessions.getCurrent().setAttribute("fileimportertarget", ((event.getKeys() & MouseEvent.META_KEY) != 0) ? "page" : "modal");
//                    uploadFileFromURL(fileUrl.getValue());
                    importFile(ImportController.this.media);
                }
            });
            cancelButton.addEventListener("onClick", new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    importWindow.detach();
                }
            });
            cancelButtonURL.addEventListener("onClick", new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    importWindow.detach();
                }
            });

            win.doModal();
        } catch (Exception e) {
            LOGGER.error("Failed to construct ImportController", e);
            throw new DialogException("Error in importProcesses controller: " + e.getMessage());
        }
    }

    private void uploadFile(UploadEvent event) throws ExceptionFormats, ExceptionImport {
        media = event.getMedia();
        fileNameLabel.setStyle("color: blue");
        fileNameLabel.setValue(media.getName());
        String extension = findExtension(media.getName());

        List<FileImporterPlugin> fileImporterPlugins = (List<FileImporterPlugin>) SpringUtil.getBean("fileImporterPlugins");
        for (FileImporterPlugin fileImporterPlugin: fileImporterPlugins) {
            if (fileImporterPlugin.getFileExtensions().contains(extension)) {
                okButton.setDisabled(false);
                return;
            }
        }

        if(!extension.equalsIgnoreCase("zip") && !extension.equalsIgnoreCase("gz") && !extension.equalsIgnoreCase("xes") && !extension.equalsIgnoreCase("mxml")) {
            String fileType = this.mainC.getNativeTypes().get(extension);
            if (fileType == null) {
                throw new ExceptionImport("Unsupported extension.");
            }
            nativeType = fileType;
        }
        okButton.setDisabled(false);
    }

    /**
     * Controller for uploading file from URL
     * @param fileUrl - URL string user inputted
     * @throws ExceptionImport if IOException, URISyntaxException, MalformedURLException happens
     * @throws ExceptionFormats
     */
    private void uploadFileFromURL(String fileUrl) throws ExceptionImport, ExceptionFormats {

        URL url;
        String filename;
        int CONNECT_TIMEOUT = 10000;
        int READ_TIMEOUT = 10000;

        String patternDropBox = "^https:\\/\\/www\\.dropbox\\.com.*dl=0$";
        String patternGoogleDrive = "^https:\\/\\/drive\\.google\\.com\\/file\\/d\\/.*sharing$";
        String patternOneDrive = "^https:\\/\\/onedrive\\.live\\.com\\/embed\\?cid.*resid.*authkey.*";

        // Parse sharable link of DropBox, GoogleDrive, OneDrive to direct file download URL
        if (Pattern.matches(patternDropBox, fileUrl)) {
            fileUrl = fileUrl.substring(0, fileUrl.length()-1) + 1;
        }
        if (Pattern.matches(patternGoogleDrive, fileUrl)) {
            String fileID;
            int fileIDStart = fileUrl.indexOf("/d/");
            int fileIDEnd = fileUrl.indexOf('/', fileIDStart + 3);
            if (fileIDStart == -1) {
                return;
            }
            if (fileIDEnd == -1){
                fileID = fileUrl.substring(fileIDStart+3);
            } else {
                fileID = fileUrl.substring(fileIDStart+3, fileIDEnd);
            }
            fileUrl = "https://drive.google.com/uc?export=download&id=" + fileID;
        }
        if (Pattern.matches(patternOneDrive, fileUrl)) {
            fileUrl = fileUrl.replace("embed?", "download?");
        }

        try {
            url = new URL(fileUrl.trim());

            // open the connection
            URLConnection con = url.openConnection();
            // get and verify the header field
            String fieldValue = con.getHeaderField("Content-Disposition");

            filename = getFileName(fileUrl, fieldValue);

            if (filename == null) {
                note.show("Couldn't find supported file. ");
                throw new ExceptionImport("Unsupported file.");
            }

            fileNameLabelURL.setStyle("color: blue");
            fileNameLabelURL.setFocus(false);
            fileNameLabelURL.setValue(filename);

            File testData = new File(filename);
            FileUtils.copyURLToFile(
                    url,
                    testData,
                    CONNECT_TIMEOUT,
                    READ_TIMEOUT);
            InputStream targetStream = new FileInputStream(testData);

            media = new MediaImpl(testData.getName(), targetStream, Charset.forName("UTF-8"));
            this.fileUrl.setValue(fileUrl);

            String extension = findExtension(media.getName());

            List<FileImporterPlugin> fileImporterPlugins = (List<FileImporterPlugin>) SpringUtil.getBean(
                    "fileImporterPlugins");
            for (FileImporterPlugin fileImporterPlugin : fileImporterPlugins) {
                if (fileImporterPlugin.getFileExtensions().contains(extension)) {
                    okButton_URL.setDisabled(false);
                    return;
                }
            }

            assert extension != null;
            if (!extension.equalsIgnoreCase("zip") && !extension.equalsIgnoreCase("gz") && !extension.equalsIgnoreCase(
                    "xes") && !extension.equalsIgnoreCase("mxml")) {
                String fileType = this.mainC.getNativeTypes().get(extension);
                if (fileType == null) {
                    throw new ExceptionImport("Unsupported extension.");
                }
                nativeType = fileType;
            }
            okButton_URL.setDisabled(false);

        } catch (MalformedURLException | URISyntaxException e) {
            okButton_URL.setDisabled(true);
            throw new ExceptionImport("URL link is not correct.");
        } catch (IOException | NullPointerException e) {
            okButton_URL.setDisabled(true);
            throw new ExceptionImport("Couldn't find supported file. Please check the URL and try again. ");
        }
    }

    private String getFileName(String url, String contentDisposition) throws URISyntaxException {
        String fileName = "";
        if (!StringUtil.isEmpty(contentDisposition)) {
            fileName = StringUtil.contentDispositionFileName(contentDisposition);
        }
        if (StringUtil.isEmpty(fileName) && !StringUtil.isEmpty(contentDisposition)) {
            fileName = contentDisposition.substring(contentDisposition.indexOf("filename=") + 9);
        }
        if (StringUtil.isEmpty(fileName)) {
            fileName = url.substring(url.lastIndexOf('/') + 1);
        }
        if (fileName.startsWith("\"")) {
            fileName = fileName.substring(1);
        }
        if (fileName.endsWith("\"")) {
            fileName = fileName.substring(0, fileName.length() - 1);
        }
        if (StringUtil.isEmpty(fileName) || !StringUtil.isValidFileName(fileName)) {
            fileName = String.valueOf(System.currentTimeMillis());
        }
        return fileName;
    }

    private final Pattern FILE_EXTENSION_PATTERN = Pattern.compile("(?<basename>.*)\\.(?<extension>[^/\\.]*)");

    private String findBasename(String name) {
        Matcher matcher = FILE_EXTENSION_PATTERN.matcher(name);
        return matcher.matches() ? matcher.group("basename") : null;
    }

    private String findExtension(String name) {
        Matcher matcher = FILE_EXTENSION_PATTERN.matcher(name);
        return matcher.matches() ? matcher.group("extension") : null;
    }

    void importFile(Media importedMedia) throws InterruptedException, IOException, ExceptionDomains, ExceptionAllUsers, JAXBException {
        String name = importedMedia.getName();
        String extension = findExtension(name);

        // Check whether any of the pluggable file importers can handle this file
        for (FileImporterPlugin fileImporterPlugin: fileImporterPlugins) {
            if (fileImporterPlugin.getFileExtensions().contains(extension)) {
                //TODO isMatchedMapping NOT HERE, separate concern
                fileImporterPlugin.importFile(importedMedia, isPublicCheckbox.isChecked());
                return;
            }
        }

        // Check the hardcoded file importer methods
        if (extension == null) {
            //ignoredFiles += (ignoredFiles.isEmpty() ? "" : " ,") + name;
            note.show("Ignoring file with no extension: " + name);
        } else if (extension.toLowerCase().equals("zip")) {
            importZip(importedMedia);
        } else if (name.toLowerCase().endsWith("xes") || name.toLowerCase().endsWith("xes.gz") || name.toLowerCase().endsWith("mxml") || name.toLowerCase().endsWith("mxml.gz")) {
            importLog(importedMedia);
        } else if (extension.toLowerCase().equals("gz")) {
            importGzip(importedMedia);
        } else if (extension.toLowerCase().equals("bpmn")) {
            importProcess(this.mainC, this, importedMedia.getStreamData(), name.split("\\.")[0], this.nativeType, name);
        } else {
            //ignoredFiles += (ignoredFiles.isEmpty() ? "" : " ,") + name;
            note.show("Ignoring file with unknown extension: " + name);
        }
    }

    private void importLog(Media logMedia) {
        try {
            Integer folderId = 0;
            if (this.mainC.getPortalSession().getCurrentFolder() != null) {
                folderId = this.mainC.getPortalSession().getCurrentFolder().getId();
            }

            String fileName = logMedia.getName();
            String extension = discoverExtension(fileName);
            System.out.println(fileName);
            System.out.println(extension);
            String logFileName = fileName.substring(0, fileName.indexOf(extension) - 1);

            getService().importLog(UserSessionManager.getCurrentUser().getUsername(), folderId, logFileName, logMedia.getStreamData(),
                    extension, "", DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()).toString(), isPublicCheckbox.isChecked());

            mainC.refresh();
        } catch (Exception e) {
            LOGGER.warn("Import failed for " + logMedia.getName(), e);
            Messagebox.show("Import failed (" + e.getMessage() + ")", "Attention", Messagebox.OK, Messagebox.ERROR);
        } /* finally {
            closePopup();
        } */
    }

    /*
    private void closePopup() {
        if (importWindow != null) {
            importWindow.detach();
        }
    }
    */

    private String discoverExtension(String logFileName) {
        if(logFileName.endsWith("mxml")) {
            return "mxml";
        }else if(logFileName.endsWith("mxml.gz")) {
            return "mxml.gz";
        }else if(logFileName.endsWith("xes")) {
            return "xes";
        }else if(logFileName.endsWith("xes.gz")) {
            return "xes.gz";
        }
        return null;
    }

    /**
     * Read uploaded file: zip archive or file which contains native description
     * in one of the supported native format.
     * zip or tar: extract files and import each if possible
     * file: import
     * @throws InterruptedException
     */
    private void importZip(Media zippedMedia) throws InterruptedException {
        try {
            ZipInputStream in = new ZipInputStream(zippedMedia.getStreamData());
            ZipEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                try { 
                    importFile(new MediaImpl(entry.getName(), in, Charset.forName("UTF-8")));
                    break;  // TODO: remove this line, making multi-file uploads possible

                } catch (ExceptionAllUsers | ExceptionDomains e) {
                    note.show("Zip component couldn't be loaded: " + e);
                }
            }
        } catch (IOException | JAXBException e) {
            LOGGER.warn("Import failed for " + zippedMedia.getName(), e);
            Messagebox.show("Import failed (" + e.getMessage() + ")", "Attention", Messagebox.OK, Messagebox.ERROR);
        }
    }

    private void importGzip(Media gzippedMedia) throws ExceptionAllUsers, ExceptionDomains, IOException, InterruptedException, JAXBException {
        importFile(new MediaImpl(findBasename(gzippedMedia.getName()), new GZIPInputStream(gzippedMedia.getStreamData()), Charset.forName("UTF-8")));
    }

    private void importProcess(MainController mainC, ImportController importC, InputStream xml_is, String processName, String nativeType, String filename) throws SuspendNotAllowedException, InterruptedException, JAXBException, IOException, ExceptionDomains, ExceptionAllUsers {
        ImportOneProcessController oneImport = new ImportOneProcessController(mainC, importC, xml_is, processName, nativeType, filename, isPublicCheckbox.isChecked());
        this.toImportList.add(oneImport);
    }

    /*
      * cancel all remaining imports
      */
    public void cancelAll() throws InterruptedException, IOException {
        for (ImportOneProcessController aToImportList : this.toImportList) {
            if (aToImportList.getImportOneProcessWindow() != null) {
                this.ignoredFiles += ", " + aToImportList.getFileName();
                aToImportList.getImportOneProcessWindow().detach();
            }
        }
        this.toImportList.clear();
        reportImport();
        importWindow.detach();
    }

    public List<ImportOneProcessController> getImportedList() {
        if (importedList == null) {
            importedList = new ArrayList<>();
        }
        return this.importedList;
    }

    public List<ImportOneProcessController> getToImportList() {
        if (toImportList == null) {
            toImportList = new ArrayList<>();
        }
        return this.toImportList;
    }

    // remove from the list of processes to be imported
    // if the list exhausted, display a message and terminate import
    public void deleteFromToBeImported(ImportOneProcessController importOneProcess) throws IOException, InterruptedException {
        this.toImportList.remove(importOneProcess);

        if (this.toImportList.size() == 0) {
            reportImport();
            importWindow.detach();
        }
    }

    public void reportImport() throws InterruptedException {
        String report = "Import of " + this.importedList.size();
        if (this.importedList.size() == 0) {
            report += " process.";
        }
        if (this.importedList.size() == 1) {
            report += " process completed.";
        } else if (this.importedList.size() > 1) {
            report += " processes completed.";
        }
        if (this.ignoredFiles.compareTo("") != 0) {
            report += "\n (" + this.ignoredFiles + " ignored).";
        }
        this.mainC.displayMessage(report);
    }

    /* Import all remaining files. Called from ImportOneProcessController after user clicked "OK all"
      * Apply default values to all file still to be imported:
      * - version name
      * - domain
      */
    public void importAllProcess(String domain) throws InterruptedException, IOException {
        List<ImportOneProcessController> importAll = new ArrayList<>();
        importAll.addAll(this.toImportList);
        for (ImportOneProcessController importOneProcess : importAll) {
            try {
                importOneProcess.importProcess(domain, UserSessionManager.getCurrentUser().getUsername());
            } catch (IOException e) {
                LOGGER.warn("Import failed in domain " + domain, e);
                Messagebox.show("Import failed (" + e.getMessage() + ")", "Attention", Messagebox.OK, Messagebox.ERROR);
            }
        }
        this.cancelAll();
    }
}
