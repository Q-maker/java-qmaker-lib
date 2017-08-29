package com.devup.qcm.sdk.managers;

import istat.android.base.tools.ToolKits.FileKits;

import java.io.File;
import java.util.List;

import com.devup.qcm.sdk.QcmsFile;
import com.devup.qcm.sdk.Questionnaire;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public final class QcmsFileManager {

    Context mContext;

    public QcmsFileManager(Context context) {
        mContext = context;
    }

    public boolean execute(QcmsFile qcmsFile) {
        if (qcmsFile == null || !qcmsFile.getFile().exists())
            return false;
        if (qcmsFile.getType() == QcmsFile.FILE_EXTANSION_BINARY) {
            qcmsFile = findBinaryAssociateWithSummary(qcmsFile);
        }
        if (qcmsFile == null || !qcmsFile.getFile().exists())
            return false;
        String path = qcmsFile.getFile().getAbsolutePath();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.setDataAndType(Uri.parse(path), "application/" + qcmsFile.getType());
        try {
            mContext.startActivity(intent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static List<File> find(Context context, String extend) {
        return new QcmsFileManager(context).find(extend);
    }

    public List<File> find(String extend) {
        return FileKits.searchOnProviderFileWithExtention(mContext, extend);

    }

    public List<File> findBinary() {
        return find(QcmsFile.FILE_EXTANSION_BINARY);
    }

    public List<File> findArchive() {
        return find(QcmsFile.FILE_EXTANSION_ARCHIVE);
    }

    public List<File> findSummary() {
        return find(QcmsFile.FILE_EXTANSION_SUMMARY);
    }

    public List<File> findSourceCode() {
        return find(QcmsFile.FILE_EXTANSION_SOURCE_CODE);
    }

    public QcmsFile findBinaryAssociateWithSummary(QcmsFile sum) {
        Questionnaire questionnaire = sum.getContentAsQuestionnaire();
        String title = questionnaire.getTitle();
        File binaryFile = new File(sum.getFile().getParentFile(), title + "."
                + QcmsFile.FILE_EXTANSION_BINARY);
        QcmsFile binary = QcmsFile.newInstance(binaryFile);
        return binary;
    }

    public QcmsFile findSummaryAssociateWithBinaryFile(File bin) {
        String title = bin.getName();
        File binaryFile = new File(bin.getParentFile(),
                title.replace(QcmsFile.FILE_EXTANSION_BINARY,
                        QcmsFile.FILE_EXTANSION_SUMMARY));
        QcmsFile binary = QcmsFile.newInstance(binaryFile);
        return binary;
    }
}
