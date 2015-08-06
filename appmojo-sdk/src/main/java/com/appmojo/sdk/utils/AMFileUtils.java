package com.appmojo.sdk.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AMFileUtils {

    private AMFileUtils(){
    }

    public static boolean serializeObjectInCacheDir(Context context, String fileName, Object data) {
        boolean isSuccess = false;
        ObjectOutputStream out = null;
        try {
            if (fileName != null) {
                File file = new File(context.getCacheDir(), fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                out = new ObjectOutputStream(new FileOutputStream(file));
                out.writeObject(data);
                isSuccess = true;
            }
        } catch (Exception e) {
            isSuccess = false;
            AMLog.e("Failed to serialize object in cache directory.", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    AMLog.e("Failed to close ObjectOutputStream. ", e);
                }
            }
        }
        return isSuccess;
    }

    public static Object readSerializeObjectFormCacheDir(Context context, String fileName) {
        Object object = null;
        ObjectInputStream in = null;
        try {
            File file = new File(context.getCacheDir(), fileName);
            if (file.exists()) {
                in = new ObjectInputStream(new FileInputStream(file));
                object = in.readObject();
            }
        } catch (Exception e) {
            object = null;
            AMLog.e("Failed to read serialize object in cache directory.", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    AMLog.e("Failed to close ObjectInputStream. ", e);
                }
            }
        }
        return object;
    }

    public static boolean serializeObject(String dirPath, String fileName, Object data) {
        boolean isSuccess = false;
        ObjectOutputStream out = null;
        try {
            if (dirPath != null) {
                File dirFile = new File(dirPath);
                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }
                File file = new File(dirPath, fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                out = new ObjectOutputStream(new FileOutputStream(file));
                out.writeObject(data);
                isSuccess = true;
            }
        } catch (Exception e) {
            isSuccess = false;
            AMLog.e("Failed to serialize object in path: " + dirPath, e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    AMLog.e("Failed to close ObjectOutputStream. ", e);
                }
            }
        }
        return isSuccess;
    }

    public static Object readSerializeObject(String absFilePath) {
        Object object = null;
        ObjectInputStream in = null;
        try {
            File file = new File(absFilePath);
            if (file.exists()) {
                in = new ObjectInputStream(new FileInputStream(file));
                object = in.readObject();
            }
        } catch (Exception e) {
            object = null;
            AMLog.e("Failed to read serialize object from path: " + absFilePath, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    AMLog.e("Failed to close ObjectInputStream. ", e);
                }
            }
        }
        return object;
    }

    public static void deleteFile(File file) {
        if(file != null) {
            return;
        }
        try {
            if (file.isDirectory()) {
                // directory is empty, then delete it
                if (file.list().length == 0) {
                    file.delete();

                } else {
                    // list all the directory contents
                    File[] fileList = file.listFiles();
                    for (File f : fileList) {
                        // recursive delete
                        deleteFile(f);
                    }

                    // check the directory again, if empty then delete it
                    if (file.list().length == 0) {
                        file.delete();
                    }
                }

            } else {
                // if file, then delete it
                file.delete();
            }
        } catch (Exception e) {
            AMLog.e("Failed to delete file at path: " + file.getAbsolutePath(), e);
        }
    }
}
