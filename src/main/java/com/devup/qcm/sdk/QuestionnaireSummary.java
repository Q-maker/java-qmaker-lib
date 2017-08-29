package com.devup.qcm.sdk;

import istat.android.base.tools.ToolKits;

import java.util.ArrayList;
import java.util.List;

import com.devup.qcm.sdk.utils.SDK;

import android.text.TextUtils;

public class QuestionnaireSummary {
	public final static String EXTRA_TAG = "questionnairesummary";
	public static String NONE = "NONE";
	protected String id = NONE;
	protected String title = NONE;
	protected String created_at = ToolKits.Dates.simpleDateTime();
	protected String updated_at = created_at;
	protected List<String> keyWord = new ArrayList<String>();
	protected QuestionnaireAuthor author = new QuestionnaireAuthor(NONE, NONE);
	protected QuestionnaireConfig config = new QuestionnaireConfig(SDK.VERSION,
			false, false);
	protected QuestionnaireInfo info = new QuestionnaireInfo();
	protected QuestionnaireURLSet URLSet = new QuestionnaireURLSet("/", "/",
			"/");
	protected List<StudyLevel> studyLevelCompatibility = new ArrayList<StudyLevel>();
	protected Subject subject = new Subject();

	QuestionnaireSummary() {
	}

	QuestionnaireSummary(String id, String title, String create_at,
                         String update_at) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.title = title;
		this.created_at = create_at;
		this.updated_at = update_at;
	}

	public QuestionnaireAuthor getAuthor() {
		return author;
	}

	public boolean isRandomEnable() {
		return info.maxRandom > 2;
	}

	public QuestionnaireConfig getConfig() {
		return config;
	}

	public String getCreated_at() {
		return created_at;
	}

	public QuestionnaireInfo getInfo() {
		return info;
	}

	public String getId() {
		return id;
	}

	public List<String> getKeyWords() {
		return keyWord;
	}

	public List<StudyLevel> getStudyLevelCompatibility() {
		return studyLevelCompatibility;
	}

	public Subject getSubject() {
		return subject;
	}

	public String getTitle() {
		return title;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public QuestionnaireURLSet getURLSet() {
		return URLSet;
	}

	public void setConfig(QuestionnaireConfig config) {
		this.config = config;
	}

	public void setAuthor(QuestionnaireAuthor author) {
		this.author = author;
	}

	public void setInfo(QuestionnaireInfo info) {
		this.info = info;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public void setKeyWord(List<String> keyWord) {
		this.keyWord = keyWord;
	}

	public void setId(String questionnaireId) {
		this.id = questionnaireId;
	}

	public void setStudyLevels(List<StudyLevel> studyLevelCompatibility) {
		this.studyLevelCompatibility = studyLevelCompatibility;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTime(int time) {
		info.setQuestionnaireTime(time);
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public void setURLSet(QuestionnaireURLSet uRLSet) {
		URLSet = uRLSet;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public void addKeyword(String keyword) {
		
		keyWord.add(keyword);
	}

	public void addStudyLevel(StudyLevel level) {
		
		studyLevelCompatibility.add(level);
	}

	public boolean shouldBeSendToRemoteURL() {
		if (!TextUtils.isEmpty(URLSet.qcmPostAnswersURL)) {
			return URLSet.qcmPostAnswersURL
					.matches("(https?://.*)|(www\\..*\\..*)");
		} else {
			return false;
		}
	}

	public static class QuestionnaireConfig {
		private boolean protectionEnable = false, smartChoiceEnable = true;
		private int marksPolicy = Exercise.MARKS_POLICY_ANNUL_ON_ECHEC;
		private int sdk_version = SDK.VERSION;

		public QuestionnaireConfig(int sdkVersion, boolean protection,
								   boolean smartChoice) {
			protectionEnable = protection;
			smartChoiceEnable = smartChoice;
			if (sdkVersion > 0) {
				sdk_version = sdkVersion;
			}
		}

		public QuestionnaireConfig(boolean protection, boolean smartChoice) {
			protectionEnable = protection;
			smartChoiceEnable = smartChoice;
		}

		public boolean isProtected() {
			return protectionEnable;
		}

		public boolean isSmartChoiceEnable() {
			return smartChoiceEnable;
		}

		public int getMarksPolicy() {
			return marksPolicy;
		}

		public QuestionnaireConfig setMarksPolicy(int marksPolicy) {
			this.marksPolicy = marksPolicy;
			return this;
		}

		public QuestionnaireConfig setProtectionEnable(boolean protectionEnable) {
			this.protectionEnable = protectionEnable;
			return this;
		}

		public QuestionnaireConfig setSmartChoiceEnable(
				boolean smartChoiceEnable) {
			this.smartChoiceEnable = smartChoiceEnable;
			return this;
		}

		public int getSdkVersion() {
			return sdk_version;
		}

	}

	public static class QuestionnaireAuthor {
		public String id = "0";
		public String name = "";

		public QuestionnaireAuthor(String id, String name) {
			this.id = id;
			this.name = name;
		}
	}

	public static class QuestionnaireInfo {
		// public String storeNote = "0.0";
		public int questionnaireTime = 0;
		public String description = NONE;
		public String language = NONE;
		public float price = 0;
		int maxRandom = -1;
		int questionCount = 0;
		private int totalQuestionCount = 0;
		private boolean autoCorrectionEnable = true;

		public QuestionnaireInfo setDescription(String description) {
			this.description = description;
			return this;
		}

		public QuestionnaireInfo setMaxRandom(int maxRandum) {
			this.maxRandom = maxRandum;
			return this;
		}

		public int getMaxRandom() {
			return maxRandom;
			// return maxRandom >= questionCount ? -1 : maxRandom;
		}

		public QuestionnaireInfo setPrice(float price) {
			this.price = price;
			return this;
		}

		public QuestionnaireInfo setQuestionnaireTime(int questionnaireTime) {
			this.questionnaireTime = questionnaireTime;
			return this;
		}

		public QuestionnaireInfo setTotalQuestionCount(int questionNumber) {
			this.totalQuestionCount = questionNumber;
			return this;
		}

		// public QuestionnaireInfo setStoreNote(String storeNote) {
		// this.storeNote = storeNote;
		// return this;
		// }

		public QuestionnaireInfo setLanguage(String language) {
			this.language = language;
			return this;
		}

		public int getTotalQuestionCount() {
			return totalQuestionCount > 0 ? totalQuestionCount : questionCount;
		}

		public int getQuestionCount() {
			return questionCount;
		}

		public boolean isAutoCorrectionEnable() {
			return autoCorrectionEnable;
		}

		public QuestionnaireInfo setAutoCorrectionEnable(
				boolean autoCorrectionEnable) {
			this.autoCorrectionEnable = autoCorrectionEnable;
			return this;
		}
	}

	public static class QuestionnaireURLSet {
		public String iconURL;
		public String soundURL;
		public String qcmFileURL;
		public String qcmPostAnswersURL;

		public QuestionnaireURLSet(String icon, String sound, String file) {
			iconURL = icon;
			soundURL = sound;
			qcmFileURL = file;
		}

		public QuestionnaireURLSet setIconURL(String iconURL) {
			this.iconURL = iconURL;
			return this;
		}

		public QuestionnaireURLSet setQcmFileURL(String qcmFileURL) {
			this.qcmFileURL = qcmFileURL;
			return this;
		}

		public QuestionnaireURLSet setSoundURL(String soundURL) {
			this.soundURL = soundURL;
			return this;
		}

		public QuestionnaireURLSet setQcmPostAnswersURL(String qcmPostAnswersURL) {
			this.qcmPostAnswersURL = qcmPostAnswersURL;
			return this;
		}

		public String getIconURL() {
			return iconURL;
		}

		public String getQcmFileURL() {
			return qcmFileURL;
		}

		public String getSoundURL() {
			return soundURL;
		}
	}

}
