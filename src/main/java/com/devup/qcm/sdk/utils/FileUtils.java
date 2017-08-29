package com.devup.qcm.sdk.utils;

import istat.android.base.tools.ToolKits.FileKits;

import java.io.File;

public class FileUtils {
    private FileUtils() {

    }

    public static String APP_FILE_NAME = "QcmMaker", APP_CACHE = "cache",
            TMP_DIR = "tmp/", CREATION_DIR = "creations/",
            CREATION_INSTALLABLE_DIR = CREATION_DIR + "installables/",
            CREATION_EDITABLE_DIR = CREATION_DIR + "editables/",
            INSTALL_DIR = "installations/";
    public static String FILE_CACHE_APP_FILE_NAME = "QcmMaker/",
            FILE_CACHE_APP_CACHE = FILE_CACHE_APP_FILE_NAME + "cache/",
            FILE_CACHE_TMP_DIR = FILE_CACHE_APP_FILE_NAME + "tmp/",
            FILE_CACHE_CREATION_DIR = FILE_CACHE_APP_FILE_NAME + "creations/",
            FILE_CACHE_CREATION_INSTALLABLE_DIR = FILE_CACHE_APP_FILE_NAME
                    + FILE_CACHE_CREATION_DIR + "installables/",
            FILE_CACHE_CREATION_EDITABLE_DIR = FILE_CACHE_APP_FILE_NAME
                    + FILE_CACHE_CREATION_DIR + "editables/",
            FILE_CACHE_INSTALL_DIR = FILE_CACHE_APP_FILE_NAME
                    + "installations/";

    public static void deleteDirectory(File dir) {
        FileKits.deleteDirectory(dir);
    }

    public static void clearDirectory(File dir) {
        FileKits.clearDirectory(dir);
    }
}
