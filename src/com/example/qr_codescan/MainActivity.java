package com.example.qr_codescan;


import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private TextView mTextView ;

	private ImageView mImageView;
	private final int QR_WIDTH=120;
	private final int QR_HEIGHT=120;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTextView = (TextView) findViewById(R.id.result); 
		mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);
		
		//�����ť��ת����ά��ɨ����棬�����õ���startActivityForResult��ת
		//ɨ������֮������ý���
		Button mButton = (Button) findViewById(R.id.button1);
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				try{
//					 mImageView.setImageBitmap(Create2DCode("www.baidu.com"));
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//				createQRImage("www.baidu.com");
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, MipcaActivityCapture.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			}
		});
	}
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if(resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				String url=bundle.getString("result");
				//��ʾɨ�赽������
				mTextView.setText(url);
				if(!TextUtils.isEmpty(url)){
					if(url.trim().startsWith("http://")) {
						onNetwork(url);
					}
				}
				//��ʾ
				mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
				
			}
			break;
		}
    }	
	public void onNetwork(String url){
		//��Ĭ���������ɨ��õ��ĵ�ַ
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(url);
		intent.setData(content_url);
		startActivity(intent);
	}
	 public void createQRImage(String url)
	    {
	        try
	        {
	            //�ж�URL�Ϸ���
	            if (url == null || "".equals(url) || url.length() < 1)
	            {
	                return;
	            }
	            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
	            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
	            //ͼ������ת����ʹ���˾���ת��
	            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
	            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
	            //�������ﰴ�ն�ά����㷨��������ɶ�ά���ͼƬ��
	            //����forѭ����ͼƬ����ɨ��Ľ��
	            for (int y = 0; y < QR_HEIGHT; y++)
	            {
	                for (int x = 0; x < QR_WIDTH; x++)
	                {
	                	//���ɶ�άͼ��
	                    if (bitMatrix.get(x, y))
	                    {
	                        pixels[y * QR_WIDTH + x] = 0xff000000;
	                    }
	                    //���ɿհ״�
	                    else
	                    {
	                        pixels[y * QR_WIDTH + x] = 0xffffffff;
	                    }
	                }
	            }
	            //���ɶ�ά��ͼƬ�ĸ�ʽ��ʹ��ARGB_8888
	            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
	            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
	            //��ʾ��һ��ImageView����
	            mImageView.setImageBitmap(bitmap);
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	    }
	 public Bitmap Create2DCode(String str) throws Exception {  
	        //���ɶ�ά����,����ʱָ����С,��Ҫ������ͼƬ�Ժ��ٽ�������,������ģ������ʶ��ʧ��  
	        BitMatrix matrix = new MultiFormatWriter().encode(str,BarcodeFormat.QR_CODE, 300, 300);  
	        int width = matrix.getWidth();  
	        int height = matrix.getHeight();  
	        //��ά����תΪһά��������,Ҳ����һֱ��������  
	        int[] pixels = new int[width * height];  
	        for (int y = 0; y < height; y++) {  
	            for (int x = 0; x < width; x++) {  
	                if(matrix.get(x, y)){  
	                    pixels[y * width + x] = 0xff000000;  
	                }  
	            }  
	        }  
	          
	        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);  
	        //ͨ��������������bitmap,����ο�api  
	        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);  
	        return bitmap;  
	    }  
}
