package com.devup.qcm.sdk.utils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.devup.qcm.sdk.Qcm;
import com.devup.qcm.sdk.Qcm.Proposition;
import com.devup.qcm.sdk.Qcm.Question;

import istat.android.base.tools.JSON;

public class QcmUtils {
	private QcmUtils() {
	}

	public static void shuffleQcmAnswer(Qcm qcm) {
		Collections.shuffle(qcm.getPropositions());
	}

	public static Qcm customiseQcm(Qcm qcm, boolean randomise,
			int max_answer, int max_truth) {
		List<Proposition> trueAnswers = getPropositionsWithTruth(qcm, true);
		List<Proposition> falseAnswers = getPropositionsWithTruth(qcm, false);
		List<Proposition> answers = new ArrayList<Proposition>();
		int index = 0;
		for (Proposition trueA : trueAnswers) {
			answers.add(trueA);
			index++;
			if (index >= max_truth) {
				break;
			}
		}
		for (Proposition answer : falseAnswers) {
			answers.add(answer);
			index++;
			if (index >= max_answer) {
				//index = 0;
				break;
			}
		}
		qcm.setPropositions(answers);
		if (randomise) {
			shuffleQcmAnswer(qcm);
		}
		return qcm;
	}

	public static String qcmToString(Qcm qcm) {
		// TODO Auto-generated method stub
		try {
			return JSONWriter.qcmToJSONObject(qcm).toString();
		} catch (Exception e) {
			return "" + e;
		}
	}

	public static String questionToString(Question question) {
		// TODO Auto-generated method stub
		try {
			return JSONWriter.questionToJSONObject(question).toString();
		} catch (Exception e) {
			return "" + e;
		}
	}

	public static String answerToString(Proposition answer) {
		// TODO Auto-generated method stub
		try {
			return JSONWriter.answerToJSONObject(answer).toString();
		} catch (Exception e) {
			return "" + e;
		}
	}

	public static Qcm applyFilter(Qcm qcm) {
		final List<Proposition> propositions = new ArrayList<Proposition>();
		for (Proposition proposition : qcm.getPropositions()) {
			if (!TextUtils.isEmpty(proposition.getLabel())) {
				propositions.add(proposition);
			}
		}
		qcm.setPropositions(propositions);
		return qcm;
	}

	public static boolean isValidQcm(Qcm qcm) {
		if (qcm.getQuestion() == null)
			return false;
		if (qcm.getQuestion().getLabel() == null)
			return false;
		if (TextUtils.isEmpty(qcm.getQuestion().getLabel()))
			return false;
		if (qcm.getPropositions() == null)
			return false;
		if (qcm.getPropositions().size() == 0)
			return false;
		List<Proposition> answers = qcm.getPropositions();
		boolean validAnswer = false;
		for (Proposition answer : answers) {

			if (!TextUtils.isEmpty(answer.getLabel())) {
				validAnswer = true;
				break;
			}

		}
		if (!validAnswer)
			return false;

		return true;
	}

	public static List<Proposition> getPropositionsWithTruth(Qcm qcm, boolean truth) {
		List<Proposition> answers = qcm.getPropositions();
		return getPropositionsWithTruth(answers, truth);
	}

	public static List<Proposition> getPropositionsWithTruth(List<Proposition> answers,
															 boolean truth) {
		final List<Proposition> resultAnswers = new ArrayList<Proposition>();
		for (Proposition answer : answers) {
			if (answer.getTruth() == truth) {
				resultAnswers.add(answer);
			}
		}
		return resultAnswers;
	}

	@SuppressLint("DefaultLocale")
	public static boolean isEqual(Proposition answer1, Proposition answer2) {
		return answer1.toString().toLowerCase()
				.equals(answer2.toString().toLowerCase());

	}

	// public static List<Answer> getPropositionsWithTruth(List<Answer> answers,
	// boolean truth) {
	// for (Answer answer : answers) {
	// if (answer.getTruth() != truth) {
	// answers.remove(answer);
	// }
	// }
	// return answers;
	// }
	public static class JSONWriter {
		public static JSONObject qcmToJSONObject(Qcm qcm) throws JSONException {
			JSONObject json = new JSONObject();
			json.accumulate("question", questionToJSONObject(qcm.getQuestion()));
			accumulateAnswers(qcm.getPropositions(), json);
			return json;
		}

		private static JSONObject answerToJSONObject(Proposition answer)
				throws JSONException {
			JSONObject json = new JSONObject();
			json.accumulate("label",
					ToolKits.beginByUpperCase(answer.getLabel()));// Toolkits.toSentence(answer.getLabel(),"."));
			json.putOpt("font_url", answer.getFontURL());
			json.putOpt("image_url", answer.getImageURL());
			json.putOpt("sound_url", answer.getSoundURL());
			json.putOpt("video_url", answer.getVideoURL());
			json.putOpt("animation_url", answer.getAnimationURL());
			json.accumulate("truth", answer.getTruth());
			return json;
		}

		private static JSONObject accumulateAnswers(List<Proposition> answers,
				JSONObject json) {
			for (Proposition answer : answers)
				try {
					json.accumulate("answers", answerToJSONObject(answer));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			return json;
		}

		public static JSONObject answersToJSONObject(List<Proposition> answers) {
			JSONObject json = new JSONObject();
			accumulateAnswers(answers, json);
			return json;
		}

		private static JSONObject questionToJSONObject(Question question)
				throws JSONException {
			JSONObject json = new JSONObject();
			json.accumulate("label",
					ToolKits.toSentence(question.getLabel(), "?"));
			json.putOpt("comment", question.getComment());
			json.putOpt("font_url", question.getFontURL());
			json.putOpt("image_url", question.getImageURL());
			json.putOpt("sound_url", question.getSoundURL());
			json.putOpt("video_url", question.getVideoURL());
			json.putOpt("animation_url", question.getAnimationURL());
			json.accumulate("type", question.getType());
			return json;
		}
	}

	public static class JSONReader {
		public static Qcm qcmFromJSONObject(String json) {
			try {
				return qcmFromJSONObject(new JSONObject(json));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		public static Qcm qcmFromJSONObject(JSONObject json)
				throws JSONException {
			Qcm qcm = new Qcm();
			qcm.setQuestion(questionFromJSONObject(json
					.getJSONObject("question")));
			qcm.setPropositions(extractAnswers(json));
			return qcm;
		}

		private static Proposition answerFromJSONObject(JSONObject json)
				throws JSONException {
			Proposition answer = new Proposition(json.getString("label"),
					json.getBoolean("truth"));
			answer.setAnimationURL(json.optString("animation_url"));
			answer.setFontURL(json.optString("font_url"));
			answer.setImageURL(json.optString("image_url"));
			answer.setSoundURL(json.optString("sound_url"));
			answer.setVideoURL(json.optString("video_url"));
			return answer;
		}

		public static List<Proposition> extractAnswers(String json) {
			try {
				return extractAnswers(new JSONObject(json));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ArrayList<Proposition>();
		}

		public static List<Proposition> extractAnswers(JSONObject json)
				throws JSONException {
			List<Proposition> answers = new ArrayList<Proposition>();
			JSONArray array = json.optJSONArray("answers");
			// .getJSONArray("answers")
			if (array != null) {
				for (JSONObject answerJson : JSON.JSONArrayToJsonList(array))
					answers.add(answerFromJSONObject(answerJson));
			} else {
				answers.add(answerFromJSONObject(json.getJSONObject("answers")));
			}

			return answers;
		}

		private static Question questionFromJSONObject(JSONObject json)
				throws JSONException {
			Question question = new Question();
			question.setAnimationURL(json.optString("animation_url"));
			question.setFontURL(json.optString("font_url"));
			question.setImageURL(json.optString("image_url"));
			question.setSoundURL(json.optString("sound_url"));
			question.setVideoURL(json.optString("video_url"));
			question.setComment(json.optString("comment"));
			question.setLabel(json.getString("label"));
			question.setType(json.optInt("type"));
			return question;
		}
	}

}
