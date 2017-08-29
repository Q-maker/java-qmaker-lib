package com.devup.qcm.sdk.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.devup.qcm.sdk.managers.ArchiveManager;

import android.text.TextUtils;

import istat.android.base.tools.ToolKits;
import istat.android.base.utils.Matrice;


public final class ToolKits {
    private ToolKits() {

    }

    public static OutputStream copyFile(String fileFrom, String directoryTo)
            throws FileNotFoundException {
        // Log.e("HUUUM",fileFrom+"...."+directoryTo+new
        // File(fileFrom).getName());
        File tmp = new File(fileFrom);
        if (!tmp.exists())
            return null;
        if (tmp.isDirectory())
            return null;
        // if(tmp.isFile()) return null;
        // Log.e("PAS NULL",fileFrom+"...."+directoryTo+new
        // File(fileFrom).getName());
        File tmpfile = new File(directoryTo);
        if (!tmpfile.exists())
            tmpfile.mkdirs();
        // Log.e("CREATION",fileFrom+"...."+directoryTo+new
        // File(fileFrom).getName());

        return istat.android.base.tools.ToolKits.Stream
                .copyStream(fileFrom, directoryTo + tmp.getName());
    }

    public static String toSentence(String question, String endingPontuation) {
        question = beginByUpperCase(question);
        int index = question.indexOf(endingPontuation);
        // if(index!=question.length()-1)return question+endingPontuation;
        if (index <= 0)
            return question + endingPontuation;

        return question;
    }

    public static String beginByUpperCase(String word) {
        if (word.length() > 1) {
            String begin = word.substring(0, 1).toUpperCase(
                    Locale.getDefault());
            word = begin + word.substring(1);
        }
        return word;
    }

    public static String timeToString(long millisec) {
        String tmp = "";
        if (millisec >= 1000 * 60 * 60 * 24)
            tmp += "dd:";
        if (millisec >= 1000 * 60 * 60)
            tmp += "HH:";
        // if (millisec >= 1000 * 60)
        // tmp += "mm:";
        // if (millisec >= 1000)
        tmp += "mm:ss";
        return new SimpleDateFormat(tmp, Locale.getDefault()).format(new Date(
                millisec));
    }

    public static long getTimeSecFromMatrix(Matrice m) {
        int out = 0;
        try {
            out = m.getmijint(1, 1) * 24 * 60 * 60 + m.getmijint(2, 1) * 60
                    * 60 + m.getmijint(3, 1) * 60 + m.getmijint(3, 1);
        } catch (Exception e) {
        }

        return out;
    }

    public static Matrice getMatrixFromTime(int millisec) {
        Matrice out = new Matrice(4, 1);
        int secs = millisec / 1000;
        if (secs == 0) {
            return new Matrice("[0;0;0;0]");
        }
        int minutes = (int) (secs / 60);
        int hours = (int) (minutes / 60);
        int days = (int) (hours / 24);
        if (days >= 1) {
            out.setmij(1, 1, "" + days);

            hours = hours - days * 24;

        } else {

            out.setmij(1, 1, "0");
        }
        // -----------------------------------------------------------
        if (hours >= 1) {

            out.setmij(2, 1, "" + hours);

        } else {
            out.setmij(2, 1, "0");
        }

        minutes = minutes - hours * 60 - days * 24 * 60;
        // -----------------------------------------------------------
        if (minutes >= 1) {

            out.setmij(3, 1, "" + minutes);

        } else {
            out.setmij(3, 1, "0");
        }
        secs = secs - minutes * 60 - hours * 60 * 60 - days * 24 * 60 * 60;
        // ----------------------------------------------------------
        if (secs >= 1) {

            out.setmij(4, 1, "" + secs);

        } else {
            out.setmij(4, 1, "0");
        }

        return out;
    }

    public static String optInternalQcmArchiveSoundsRes(String path) {
        if (TextUtils.isEmpty(path))
            return "";
        String[] path_section = path.split("/");
        if (path_section.length >= 1)
            return path;
        String name = path_section[path_section.length - 1];
        return ArchiveManager.DIR_RES_SOUNDS + name;
    }

    public static String optAbsoluteQuestionnaireResURL(String basepath, String path) {
        if (TextUtils.isEmpty(path))
            return "";
        if (path.startsWith("http://") || path.startsWith("https://")) return path;
        return "file://" + basepath + path;
    }

    public static String optInternalQcmArchiveImagesRes(String path) {
        if (TextUtils.isEmpty(path))
            return "";
        if (path.startsWith("http://") || path.startsWith("https://")) return path;
        String[] path_section = path.split("/");
        if (path_section.length <= 1)
            return path;
        String name = path_section[path_section.length - 1];
        return ArchiveManager.DIR_RES_IMAGES + name;
    }

    public static String optInternalQcmArchiveAnnimationsRes(String path) {
        if (TextUtils.isEmpty(path))
            return "";
        if (path.startsWith("http://")) return path;
        String[] path_section = path.split("/");
        if (path_section.length <= 1)
            return path;
        String name = path_section[path_section.length - 1];
        return ArchiveManager.DIR_RES_ANIMATIONS + name;
    }

    public static String optInternalQcmArchiveVideosRes(String path) {
        if (TextUtils.isEmpty(path))
            return "";
        if (path.startsWith("http://")) return path;
        String[] path_section = path.split("/");
        if (path_section.length <= 1)
            return path;
        String name = path_section[path_section.length - 1];
        return ArchiveManager.DIR_RES_VIDEOS + name;
    }

}
