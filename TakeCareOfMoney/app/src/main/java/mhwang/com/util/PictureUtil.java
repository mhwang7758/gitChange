package mhwang.com.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

/**
 * 项目名称：
 * 类描述：照片工具类
 * 作者：王明海
 * 创建时间：2016/5/6
 */
public class PictureUtil {
    private static void showLog(String msg){
        Log.d("--PictureUtil--->",msg);
    }

    public static Bitmap resizeBitmap(Bitmap bitmap,int width,int height){
        // 获得相片的原始大小
        int oriWidth = bitmap.getWidth();
        int oriHeight = bitmap.getHeight();

        // 获得相片的缩放比例
        float scaleWidth = (float)width/oriWidth;
        float scaleHeight = (float)height/oriHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        Bitmap mBitmap = Bitmap.createBitmap(bitmap,0,0,
                width,height,matrix,true);
        // 记得将原来的bitmap对象回收
        bitmap.recycle();
        return mBitmap;
    }

//    public static Bitmap getBitmap(String imgPath) {
//        // Get bitmap through image path
//        BitmapFactory.Options newOpts = new BitmapFactory.Options();
//        newOpts.inJustDecodeBounds = false;
//        newOpts.inPurgeable = true;
//        newOpts.inInputShareable = true;
//        // Do not compress
//        newOpts.inSampleSize = 1;
//        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
//        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
//        if (bitmap == null){
//            showLog("line 45 bitmap is null");
//        }
//        return bitmap;
//    }

    /** 根据路径获取图片
     * @param path
     * @return
     */
    public static Bitmap getBitmap(String path){
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (bitmap == null){
            showLog("line 45 bitmap is null");
        }
        return bitmap;
    }
}
