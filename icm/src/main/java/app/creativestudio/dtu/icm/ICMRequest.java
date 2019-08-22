package app.creativestudio.dtu.icm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ICMRequest {
    private static Bitmap bitmap;
    static String ICMServer = "https://tranquil-citadel-60464.herokuapp.com";
    static ICMAttr icmAttr;
    ICMRequest(ICMAttr icmAttr){
        this.icmAttr = icmAttr;
    }
    public static ICMRequest init(){
        return new ICMRequest(new ICMAttr());
    }
    public static ICMRequest uploadImg(Uri uri, Context context, final CallBack callBack){
        JSONObject jsonObject = new JSONObject();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            decodeUri(context,uri,150).compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] Data= baos.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(Data, 0, Data.length);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String encodedImage =Base64.encodeToString(byteArray, Base64.NO_WRAP);
            try {
                jsonObject.put("byte64",encodedImage);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String analysnudefrombytearr = "/analysnudefrombytearr/";
        GetJsonAPI.setUrl("https://tranquil-citadel-60464.herokuapp.com"+analysnudefrombytearr).post(new ApiCall.AsyncApiCall() {
            @Override
            public void onSuccess(long resTime ,JsonSnapshot resultJson) {
                callBack.onSuccess(resTime,bitmap,resultJson);
            }

            @Override
            public void onFail(int responeCode, String mess) {
                callBack.onFail(responeCode,bitmap,mess);
            }
        },new JsonSnapshot(jsonObject));
        return new ICMRequest(icmAttr);
    }
    public static ICMRequest sendURL(String url, Context context, final CallBack callBack){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("url",url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String analysnudefrombytearr = "/analysnudefromurl/";
        GetJsonAPI.setUrl("https://tranquil-citadel-60464.herokuapp.com"+analysnudefrombytearr).post(new ApiCall.AsyncApiCall() {
            @Override
            public void onSuccess(long resTime ,JsonSnapshot resultJson) {
                callBack.onSuccess(resTime,bitmap,resultJson);
            }

            @Override
            public void onFail(int responeCode, String mess) {
                callBack.onFail(responeCode,bitmap,mess);
            }
        },new JsonSnapshot(jsonObject));
        return new ICMRequest(icmAttr);
    }
    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth
                , height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }
    public interface CallBack{
        void onSuccess(long resTime, Bitmap bitmap, JsonSnapshot resultJson);
        void onFail(int responeCode, Bitmap bimap, String mess);
    }
}
