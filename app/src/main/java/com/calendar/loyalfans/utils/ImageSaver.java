package com.calendar.loyalfans.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.vincent.videocompressor.VideoCompress;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

import static android.content.Context.MODE_PRIVATE;

public class ImageSaver {

    private String directoryName = "KidsXapKiosk";
    private String fileName = "kidsXap.png";
    private static final String TAG = "SaveFileInPrivateStorage";
    private Context context;
    private boolean external;
    public static final int IMAGE_MAX_EXPECTED_SIZE = 1024;
    public static final int STYLED_LAYOUT_ID = 1220;
    public static final int VIDEO_MAX_SIZE_ALLOWED = 10;//MB
    public static final int THUMB_SIZE = 320;

    public ImageSaver(Context context) {
        this.context = context;
    }


    public ImageSaver setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public ImageSaver setExternal(boolean external) {
        this.external = external;
        return this;
    }

    public ImageSaver setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
        return this;
    }

    public void save(Bitmap bitmapImage) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(createFile());
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    private File createFile() {
        File directory;
        if (external) {
            directory = getAlbumStorageDir(directoryName);
        } else {
            directory = context.getDir(directoryName, MODE_PRIVATE);
        }
        if (!directory.exists() && !directory.mkdirs()) {
            Log.e("ImageSaver", "Error creating directory " + directory);
        }

        return new File(directory, fileName);
    }

    public File getAlbumStorageDir(String albumName) {
        return new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static Bitmap ResizeImageAspectRatio(Context context, String filePath) throws
            FileNotFoundException {
        Uri uri = Uri.fromFile(new File(filePath));
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        Bitmap originalImage = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, o2);
        originalImage = Common.Companion.rotateBitmap(filePath, originalImage);
        return ResizeImageAspectRatioFromBitmap(originalImage, IMAGE_MAX_EXPECTED_SIZE);
    }

    public static Bitmap ResizeImageAspectRatioFromBitmap(Bitmap originalImage, int expectedSize) {
        float originalWidth = originalImage.getWidth();
        float originalHeight = originalImage.getHeight();
        int width = expectedSize, height = expectedSize;

        if (originalWidth > expectedSize || originalHeight > expectedSize) {
            if (originalWidth > originalHeight) {
                height = (int) ((originalHeight / originalWidth) * width);
            } else if (originalWidth < originalHeight) {
                width = (int) ((originalWidth / originalHeight) * height);
            }
        } else {
            return originalImage;
        }
        Bitmap background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(background);
        float scale = width / originalWidth;
        float xTranslation = 0.0f;
        float yTranslation = (height - originalHeight * scale) / 2.0f;
        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(originalImage, transformation, paint);
        return background;
    }

    public String saveFileIntoPrivateStorage(String filePath, VideoCompressListener videoCompressListener) throws IOException {

        File fileFrom = new File(filePath);
        String destFileName = getFileNameUsingFilePath(filePath);
        String destFileThumbName = getThumbFileForFilename(destFileName);
        Bitmap newBitMap = null;

        String destFilePath = new File(context.getFilesDir(), destFileName).getAbsolutePath();

        if (!Common.Companion.isVideo(filePath)) {
            newBitMap = ResizeImageAspectRatio(context, filePath);
            Bitmap newThumbBitMap = ResizeImageAspectRatioFromBitmap(newBitMap, THUMB_SIZE);
            CopyFile(context, fileFrom, destFileName, newBitMap);
            CopyFile(context, fileFrom, destFileThumbName, newThumbBitMap);
        } else {
            VideoCompress.compressVideoLow(fileFrom.getAbsolutePath(), destFilePath, new VideoCompress.CompressListener() {
                private long startTime;

                @SuppressLint("SetTextI18n")
                @Override
                public void onStart() {
                    Common.Companion.displayProgress(context);
                    startTime = System.currentTimeMillis();
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onSuccess() {
                    Common.Companion.dismissProgress();
                    Log.d("compressVideo", "onSuccess: " + "Total: " + ((startTime - System.currentTimeMillis()) / 1000) + "s" + "\n");
                    try {
                        CopyFile(context, fileFrom, destFileThumbName, ResizeImageAspectRatioFromBitmap(retrieveFirstFrameFromVideoPath(destFilePath), THUMB_SIZE));
                        videoCompressListener.onVideoCompress(destFilePath, null);
                    } catch (Exception e) {
                        videoCompressListener.onVideoCompress(null, "Fail : " + e.getMessage());
                    }
                }

                @Override
                public void onFail() {
                    Common.Companion.dismissProgress();
                    if (IsSizeLessThenOfFile(fileName, VIDEO_MAX_SIZE_ALLOWED)) {
                        try {
                            CopyFile(context, fileFrom, destFileName, null);
                            CopyFile(context, fileFrom, destFileThumbName, ResizeImageAspectRatioFromBitmap(retrieveFirstFrameFromVideoPath(destFilePath), THUMB_SIZE));
                            videoCompressListener.onVideoCompress(destFilePath, null);
                        } catch (Exception e) {
                            videoCompressListener.onVideoCompress(null, "Fail : " + e.getMessage());
                        }
                    } else {
                        videoCompressListener.onVideoCompress(null, "Fail : compress failed and file larger.");
                    }
                    Log.d("compressVideo", "onFail: " + "Compress Fail with less file size! " + ((startTime - System.currentTimeMillis()) / 1000) + "s" + "\n");
                }

                @Override
                public void onProgress(float percent) {
                    Log.d("VideoImage", percent + "%");
                }
            });
        }
        return destFilePath;
    }

    private Bitmap retrieveFirstFrameFromVideoPath(String videoPath) throws Exception {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            throw e;
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public static void CopyFile(Context context, File fileFrom, String destFileName, Bitmap newBitMap) throws IOException {
        InputStream in = new FileInputStream(fileFrom);
        try {
            FileOutputStream fOut = context.openFileOutput(destFileName, MODE_PRIVATE);
            try {

                if (newBitMap != null) {
                    newBitMap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                } else {
                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    while (in.read(buf) > 0) {
                        fOut.write(buf);
                    }
                    fOut.flush();
                }
            } finally {
                fOut.close();
            }
        } finally {
            in.close();
        }
    }

    public static void CopyToExternalFile(Context context, File fileFrom, String destFileName, Bitmap newBitMap) throws IOException {
        InputStream in = new FileInputStream(fileFrom);
        try {
            FileOutputStream fOut = new FileOutputStream(destFileName);
            try {

                if (newBitMap != null) {
                    newBitMap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                } else {
                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    while (in.read(buf) > 0) {
                        fOut.write(buf);
                    }
                    fOut.flush();

                }
            } finally {
                fOut.close();
            }
        } finally {
            in.close();
        }
    }

    public static String copyFileFunction(Context context, String srcPath) throws IOException {

        File src = new File(srcPath);
        File dst = new File(context.getExternalCacheDir(), getFilenameFromPath(srcPath));

        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            outChannel.close();
        }
        return dst.getAbsolutePath();
    }

    @NotNull
    public static String getFilenameFromPath(String path) {
        if (path.contains("/"))
            return path.substring(path.lastIndexOf("/")).replace("/", "");
        return path;
    }

    private String getFileNameUsingFilePath(String filePath) {
        return System.currentTimeMillis() + "_" + filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    private static String getThumbFileForFilename(String fileName) {
        int lastIndexOf = fileName.lastIndexOf('.');
        if (lastIndexOf > 0) {
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        }
        return fileName + "_Thumb.jpeg";
    }

    public static String GetThumbNameFromFileName(String fileName) {
        String thumbfileName = getThumbFileForFilename(fileName);
        File file = new File(thumbfileName);
        if (file.exists())
            return thumbfileName;
        return "";
    }

    public interface VideoCompressListener {
        void onVideoCompress(String destinationPath, String error);
    }


    public static boolean IsSizeLessThenOfFile(String fileName, int mbSize) {
        long fileSizeInMB = 0;
        File file = new File(fileName);
        long fileSizeInBytes = file.length();
        long fileSizeInKB = fileSizeInBytes / 1024;
        fileSizeInMB = fileSizeInMB + (fileSizeInKB / 1024);
        return fileSizeInMB <= mbSize;
    }
}