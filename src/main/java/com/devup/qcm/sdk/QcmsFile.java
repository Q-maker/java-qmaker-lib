package com.devup.qcm.sdk;

import istat.android.base.tools.ToolKits.FileKits;

import java.io.File;
import java.io.FileNotFoundException;
import org.json.JSONException;
import android.os.Parcel;
import android.os.Parcelable;

import com.devup.qcm.sdk.managers.ArchiveManager;
import com.devup.qcm.sdk.utils.QuestionnaireUtils;

public class QcmsFile implements Parcelable {
	public final static String EXTRA_TAG = "qcmsfile";
	public final static String EXTRA_FILE_BINARY = "qcms_binary_file";
	public final static String FILE_EXTANSION_UNKNOW = "unknow";
	public final static String FILE_EXTANSION_ARCHIVE = "qcm";
	public final static String FILE_EXTANSION_SUMMARY = "qcmex";
	public final static String FILE_EXTANSION_BINARY = "qcmbin";
	public final static String FILE_EXTANSION_SOURCE_CODE = "qcmsrc";
	Questionnaire questionnaire;
	File file;

	public static QcmsFile newInstance(File file) {
		try {
			return new QcmsFile(file);
		} catch (Exception e) {
			return new QcmsFile();
		}
	}

	public static QcmsFile newInstance(String file) {

		return newInstance(new File(file));

	}

	private QcmsFile(Parcel parcel) {
		file = new File(parcel.readString());
		questionnaire = (Questionnaire) parcel
				.readParcelable(Questionnaire.class.getClassLoader());
	}

	public QcmsFile(File file) throws FileNotFoundException, JSONException {
		this.file = file;
		build();
	}

	public QcmsFile build() throws JSONException, FileNotFoundException {
		String type = getType();
		if (type.equals(FILE_EXTANSION_ARCHIVE)) {
			questionnaire = (Questionnaire) ArchiveManager
					.readArchiveSummary(file);
		} else if (type.equals(FILE_EXTANSION_SOURCE_CODE)) {
			questionnaire = QuestionnaireUtils.readSourceBinaryFile(file);
		} else if (type.equals(FILE_EXTANSION_SUMMARY)) {
			questionnaire = QuestionnaireUtils.readSummaryFile(file);
		} else if (type.equals(FILE_EXTANSION_BINARY)) {
			questionnaire = QuestionnaireUtils.readBinaryFile(file, "decode");
		}
		return this;
	}

	QcmsFile() {
	}

	public File getFile() {
		return file;
	}

	public Questionnaire getContentAsQuestionnaire() {
		return questionnaire;
	}

	public QuestionnaireSummary getContentAsSummary() {
		if (questionnaire != null)
			return questionnaire.getSummary();
		else
			return null;
	}

	public String getType() {
		return FileKits.fileExtention(file);

	}

	public boolean exist() {
		if (file == null)
			return false;
		return file.exists()
				&& (questionnaire != null || getType().equals(
						FILE_EXTANSION_ARCHIVE));
	}

	@Override
	public int describeContents() {
		
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int arg1) {
		
		parcel.writeString(file.getAbsolutePath());
		parcel.writeParcelable(questionnaire, arg1);
	}

	public static final Parcelable.Creator<QcmsFile> CREATOR = new Parcelable.Creator<QcmsFile>() {
		@Override
		public QcmsFile createFromParcel(Parcel source) {
			return new QcmsFile(source);
		}

		@Override
		public QcmsFile[] newArray(int size) {
			return new QcmsFile[size];
		}
	};

}
