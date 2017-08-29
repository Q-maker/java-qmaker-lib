package com.devup.qcm.sdk;


import java.io.File;
import java.io.FileNotFoundException;

import org.json.JSONException;

import com.devup.qcm.sdk.managers.ArchiveManager;
import com.devup.qcm.sdk.utils.QuestionnaireUtils;

public class QcmArchive extends QcmsFile {
	public QcmArchive(File file) throws JSONException, FileNotFoundException{
		super(file);
	}
	public QcmArchive(File file, Questionnaire qcm) {
		this.file = file;
		this.questionnaire = qcm;
	}

	public QuestionnaireSummary getQuestionnaireSummary(File tmpDir)
			throws JSONException {
		if (file == null || !file.exists()) {
			if (questionnaire == null) {
				return null;
			} else {
				return QuestionnaireUtils
						.optQuestionnaireSummary(questionnaire);
			}
		}
		return ArchiveManager.readArchiveSummary(file, tmpDir);
	}

}
