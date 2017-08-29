package com.devup.qcm.sdk.managers;

import istat.android.base.tools.Zips;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.json.JSONException;

import com.devup.qcm.sdk.Qcm;
import com.devup.qcm.sdk.QcmArchive;
import com.devup.qcm.sdk.QcmsFile;
import com.devup.qcm.sdk.Questionnaire;
import com.devup.qcm.sdk.QuestionnaireSummary;
import com.devup.qcm.sdk.utils.FileUtils;
import com.devup.qcm.sdk.utils.QuestionnaireUtils;
import com.devup.qcm.sdk.utils.ToolKits;

public class ArchiveManager {
	File tmpDirectory;
	File appDirectory;

	public ArchiveManager(File appDir) {
		appDirectory = appDir;
	}

	public ArchiveManager(String appDir) {
		appDirectory = new File(appDir);
	}

	public void setTmpDirectory(File tmpDirectory) {
		this.tmpDirectory = tmpDirectory;
		if (!tmpDirectory.exists())
			tmpDirectory.mkdirs();
	}

	public QcmArchive createArchive(QcmArchive archive) throws ZipException,
			IOException {
		prepareTemporyDir(archive);
		createArchive(archive.getFile(), tmpDirectory,
				archive.getContentAsQuestionnaire());
		FileUtils.clearDirectory(tmpDirectory);
		return archive;
	}

	public QcmArchive createArchive(File file, Questionnaire qcms)
			throws ZipException, IOException {
		QcmArchive archive = new QcmArchive(file, qcms);
		return createArchive(archive);
	}

	public QcmArchive createArchive(Questionnaire qcms) throws ZipException,
			IOException {
		return createArchive(
				new File(appDirectory, FileUtils.CREATION_INSTALLABLE_DIR
						+ qcms.getTitle() + ".qcm"), qcms);
	}

	public static QcmArchive readArchive(File file) throws ZipException,
			IOException, JSONException {
		QcmArchive archive = new QcmArchive(file);
		return archive;
	}

	public static QuestionnaireSummary readArchiveSummary(File inputFile)
			throws JSONException {
		File tmp = new File(inputFile.getParentFile(), FileUtils.TMP_DIR);
		if (!tmp.exists()) {
			tmp.mkdirs();
		}
		try {
			QuestionnaireSummary summary = readArchiveSummary(inputFile, tmp);
			return summary;
		} finally {
			FileUtils.deleteDirectory(tmp);
		}
	}

	public static QuestionnaireSummary readArchiveSummary(String inputFile)
			throws JSONException {
		return readArchiveSummary(new File(inputFile));
	}

	public QcmArchive installArchive(QcmArchive archive) throws IOException,
			JSONException {
		return installArchive(archive.getFile());
	}

	public QcmArchive installArchive(File archiveFile) throws IOException,
			JSONException {

		File tmpDir = new File(archiveFile.getParentFile(), FileUtils.TMP_DIR);
		if (!tmpDir.exists()) {
			tmpDir.mkdirs();
		}
		try {
			Questionnaire qcm = (Questionnaire) readArchiveSummary(archiveFile,
					tmpDir);
			File installDir = new File(getInstallDir(), qcm.getTitle() + "/");
			installArchive(archiveFile, installDir);

			return new QcmArchive(archiveFile, qcm);
		} finally {
			FileUtils.deleteDirectory(tmpDir);
		}
	}

	public File getTempDir() {
		return tmpDirectory != null ? tmpDirectory : (tmpDirectory = new File(
				appDirectory, FileUtils.TMP_DIR));
	}

	public File getDefaultTempDir() {
		File tmpDir = new File(appDirectory, FileUtils.TMP_DIR);
		if (!tmpDir.exists())
			tmpDir.mkdirs();
		return tmpDir;
	}

	public File getInstallDir() {
		File tmpDir = new File(appDirectory, FileUtils.INSTALL_DIR);
		if (!tmpDir.exists())
			tmpDir.mkdirs();
		return tmpDir;
	}

	public File getInstallableDir() {
		File tmpDir = new File(appDirectory, FileUtils.CREATION_EDITABLE_DIR);
		if (!tmpDir.exists())
			tmpDir.mkdirs();
		return tmpDir;
	}

	public File getEditableDir() {
		File tmpDir = new File(appDirectory, FileUtils.CREATION_EDITABLE_DIR);
		if (!tmpDir.exists())
			tmpDir.mkdirs();
		return tmpDir;
	}

	public File getCreationDir() {
		File tmpDir = new File(appDirectory, FileUtils.CREATION_DIR);
		if (!tmpDir.exists())
			tmpDir.mkdirs();
		return tmpDir;
	}

	public static QuestionnaireSummary readArchiveSummary(File inputFile,
														  File tempDir) throws JSONException {
		try {
			ZipFile zipFile = new ZipFile(inputFile);
			Enumeration<?> enu = zipFile.entries();
			while (enu.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) enu.nextElement();
				String name = zipEntry.getName();
				if (!name.matches(".*\\." + QcmsFile.FILE_EXTANSION_SUMMARY))
					continue;
				File file = new File(tempDir.getAbsoluteFile() + "/" + name);
				InputStream is = zipFile.getInputStream(zipEntry);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] bytes = new byte[1024];
				int length;
				while ((length = is.read(bytes)) >= 0) {
					fos.write(bytes, 0, length);
				}
				is.close();
				fos.close();
				return QuestionnaireUtils.readSummaryFile(file);
			}
			zipFile.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	private void installArchive(File archiveFile, File extDirectory)
			throws IOException, JSONException {
		// try {
		ZipFile zipFile = new ZipFile(archiveFile);
		Enumeration<?> enu = zipFile.entries();
		while (enu.hasMoreElements()) {
			ZipEntry zipEntry = (ZipEntry) enu.nextElement();

			String name = zipEntry.getName();
			/*
			 * if ( name.matches(".*\\.sum")) continue;
			 */

			File file = new File((extDirectory.getAbsoluteFile()) + "/" + name);
			if (name.endsWith("/")) {
				file.mkdirs();
				continue;
			}

			File parent = file.getParentFile();
			if (parent != null) {
				parent.mkdirs();
			}

			InputStream is = zipFile.getInputStream(zipEntry);
			FileOutputStream fos = new FileOutputStream(file);
			byte[] bytes = new byte[1024];
			int length;
			while ((length = is.read(bytes)) >= 0) {
				fos.write(bytes, 0, length);
			}
			is.close();
			fos.close();
			// if ( name.matches(".*\\.sum")){
			// qcm=QuestionnaireManager.readBinaryFile(file);
			// }

		}
		zipFile.close();
		// return new QcmArchive(archiveFile, qcm);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// return null;
	}

	private void prepareTemporyDir(QcmArchive archive) {
		if (tmpDirectory == null) {
			setTmpDirectory(new File(appDirectory, FileUtils.TMP_DIR));
		}
	}

	private static void copyQuestionsRessources(Questionnaire qcms, File root)
			throws FileNotFoundException {
		for (Qcm qcm : qcms.getQcms()) {
			for (String path : qcm.getfontURLList())
				ToolKits.copyFile(path, root.getAbsolutePath() + DIR_RES_FONTS);
			for (String path : qcm.getImageURLList())
				ToolKits.copyFile(path, root.getAbsolutePath() + DIR_RES_IMAGES);
			for (String path : qcm.getSoundURLList())
				ToolKits.copyFile(path, root.getAbsolutePath() + DIR_RES_SOUNDS);
			for (String path : qcm.getVideoURLList())
				ToolKits.copyFile(path, root.getAbsolutePath() + DIR_RES_VIDEOS);
			for (String path : qcm.getAnimationURLList())
				ToolKits.copyFile(path, root.getAbsolutePath()
						+ DIR_RES_ANIMATIONS);

		}
	}

	public final static String DIR_RES_SOUNDS = "/sounds/";
	public final static String DIR_RES_IMAGES = "/images/";
	public final static String DIR_RES_ANIMATIONS = "/animations/";
	public final static String DIR_RES_VIDEOS = "/videos/";
	public final static String DIR_RES_FONTS = "/fonts/";

	private static void createArchive(File file, File tmpDir, Questionnaire qcm)
			throws ZipException, IOException {
		FileUtils.clearDirectory(tmpDir);
		copyQcmsRessources(qcm, tmpDir);
		QuestionnaireUtils.createQcmBinaryFile(tmpDir, qcm);
		QuestionnaireUtils.createQcmSummaryFile(tmpDir, qcm);
		file.getParentFile().mkdirs();
		System.out.println("from::" + tmpDir.getAbsolutePath() + "----to::"
				+ file.getAbsolutePath());
		Zips.Ziper.zip(tmpDir, file);
	}

	private static void copyQcmsRessources(Questionnaire qcm, File tmpDir)
			throws FileNotFoundException {
		ToolKits.copyFile(qcm.getURLSet().iconURL, tmpDir.getAbsolutePath()
				+ DIR_RES_IMAGES);
		ToolKits.copyFile(qcm.getURLSet().soundURL, tmpDir.getAbsolutePath()
				+ DIR_RES_SOUNDS);
		copyQuestionsRessources(qcm, tmpDir);
	}
}