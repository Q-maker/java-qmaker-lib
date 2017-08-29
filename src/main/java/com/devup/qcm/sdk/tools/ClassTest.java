package com.devup.qcm.sdk.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import istat.android.base.tools.ToolKits;

import com.devup.qcm.sdk.Exercise;
import com.devup.qcm.sdk.Qcm;
import com.devup.qcm.sdk.Questionnaire;
import com.devup.qcm.sdk.User;
import com.devup.qcm.sdk.Qcm.Proposition;
import com.devup.qcm.sdk.Qcm.Question;
import com.devup.qcm.sdk.QuestionnaireSummary.QuestionnaireAuthor;
import com.devup.qcm.sdk.QuestionnaireSummary.QuestionnaireConfig;
import com.devup.qcm.sdk.QuestionnaireSummary.QuestionnaireInfo;
import com.devup.qcm.sdk.QuestionnaireSummary.QuestionnaireURLSet;

public final class ClassTest {
	static int MAX_QCM_ANSWER = 10, MAX_QCM_QUETION = 50;

	public static User UserInstance() {
		String random = ToolKits.Word.ShortWord("" + Math.random(), 10);
		User user = new User(random, "email::" + random, "password::" + random);
		user.metaData = "meta::" + random;
		return user;
	}

	public static Proposition propositionInstance() {
		Random randomise = new Random();
		String random = ToolKits.Word.ShortWord("" + Math.random(), 10);
		Proposition answer1 = new Proposition();
		answer1.setAnimationURL("anim" + random);
		answer1.setTruth(randomise.nextBoolean());
		answer1.setFontURL("font" + random);
		answer1.setImageURL("image" + random);
		answer1.setLabel("label::" + random);
		answer1.setSoundURL("sound" + random);
		answer1.setVideoURL("video" + random);
		return answer1;
	}

	public static Question QuestionInstance() {
		String random = ToolKits.Word.ShortWord("" + Math.random(), 10);
		Question question = new Question();
		question.setAnimationURL("anim" + random);
		question.setComment("comment" + random);
		question.setFontURL("font" + random);
		question.setImageURL("image" + random);
		question.setLabel("Qlabel::" + random);
		question.setSoundURL("sound" + random);
		question.setVideoURL("video" + random);
		return question;
	}

	public static Qcm QcmInstance(String TAG) {
		Random randomise = new Random();
		Question question = new Question();
		question.setAnimationURL("anim" + TAG);
		question.setComment("comment" + TAG);
		question.setFontURL("font" + TAG);
		question.setImageURL("image" + TAG);
		question.setLabel("Qlabel::" + TAG);
		question.setSoundURL("sound" + TAG);
		question.setVideoURL("video" + TAG);
		int answerCount = randomise.nextInt(MAX_QCM_ANSWER);
		if (answerCount == 0)
			answerCount++;

		Qcm qcm = new Qcm(question);
		for (int i = 0; i < answerCount; i++)
			qcm.addProposition(propositionInstance());
		return qcm;
	}

	public static Qcm QcmInstance() {
		Random randomise = new Random();
		String random = ToolKits.Word.ShortWord("" + Math.random(), 10);
		Question question = new Question();

		question.setAnimationURL("anim" + random);
		question.setComment("comment" + random);
		question.setFontURL("font" + random);
		question.setImageURL("image" + random);
		question.setLabel("Qlabel::" + random);
		question.setSoundURL("sound" + random);
		question.setVideoURL("video" + random);

		int answerCount = randomise.nextInt(MAX_QCM_ANSWER);
		if (answerCount == 0)
			answerCount++;
		Qcm qcm = new Qcm(question);
		for (int i = 0; i < answerCount; i++)
			qcm.addProposition(propositionInstance());
		return qcm;
	}

	public static Questionnaire QuestionnaireInstance() {
		List<String> keyWord = new ArrayList<String>();
		QuestionnaireAuthor author = new QuestionnaireAuthor("1", "ISTAT");
		QuestionnaireInfo info = new QuestionnaireInfo();
		info.setDescription("desciption").setLanguage("FR")
				.setQuestionnaireTime(60000).setTotalQuestionCount(8);
		QuestionnaireConfig config = new QuestionnaireConfig(false, false)
				.setMarksPolicy(Exercise.MARKS_POLICY_ANNUL_ON_ECHEC);
		//QuestionnaireURLSet URLSet=new QuestionnaireURLSet("/sdcard/log_email.txt","/sdcard/kizeo.xlsx", "/sdcard/justice.txt");
		QuestionnaireURLSet URLSet=new QuestionnaireURLSet("qcmIconURL","qcmSoundURL", "qcmFileURL");
		int keywordCount = new Random().nextInt(MAX_QCM_QUETION);
		if (keywordCount < 4)
			keywordCount = 4;
		for (int i = 0; i < keywordCount; i++)
			keyWord.add("KeyWord::"+Math.random());
		Questionnaire questionnaire = new Questionnaire("1","QTitle","2014/03/12 21:07:09","2014/12/06 06:17:09");
		questionnaire.setKeyWord(keyWord);
		questionnaire.setAuthor(author);
		questionnaire.setInfo(info);
		questionnaire.setConfig(config);
		questionnaire.setURLSet(URLSet);
		
		
		int answerCount = new Random().nextInt(MAX_QCM_QUETION);
		if (answerCount < 4)
			answerCount = 4;
		for (int i = 0; i < answerCount; i++)
			questionnaire.addQcm(QcmInstance());
		
		return questionnaire;
	}
}
