package com.contactsImprove.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCode {
	
	static final int BLACK = 0xFF000000;
	static final int WHITE = 0xFFFFFFFF;
	static String format="png";
	static String charset="UTF-8";
    static int width = 200; // 图像宽度
    static int height = 200; // 图像高度
    public static BufferedImage logo=null;


   public static void init(File f) {
    	try {
			logo=ImageIO.read(f);
			f=null;
		} catch (IOException e) {			
			LoggerUtil.error(e.getMessage(), e);
		}     	
    }
    final static Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>(){ 
		private static final long serialVersionUID = 1L;
		{ 
    		//编码 
    		put(EncodeHintType.CHARACTER_SET, charset);
    		//容错级别
    		put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
    		//边距 
    		put(EncodeHintType.MARGIN, 1); 
    	}
    };
 	
	public static String getEncode(String content) throws Exception {
        String mkdir = "D://qRCode/";
        File f = new File(mkdir);
        if (!f.exists()) f.mkdirs();
        String string = "111";
        String path = mkdir + string + ".png";
        createQRCode(content,new FileOutputStream(new File(path)));                
        return path;
    }
	private static BitMatrix deleteWhite(BitMatrix matrix) {
		int[] rec = matrix.getEnclosingRectangle(); 
		int resWidth = rec[2] + 1; 
		int resHeight = rec[3] + 1; 
		BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
		resMatrix.clear(); 
		for (int i = 0; i < resWidth; i++) {
			for (int j = 0; j < resHeight; j++) { 
				if (matrix.get(i + rec[0], j + rec[1])) 
					resMatrix.set(i, j); 
				} 
			}
		return resMatrix; 
	}

	public static void createQRCode(String content,int width,int height,OutputStream out) {
		  try {
				BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
				        BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵	
				bitMatrix=deleteWhite(bitMatrix);
				width=bitMatrix.getWidth();
				height=bitMatrix.getHeight();
	            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
	            for (int x = 0; x < width; x++) {
	                for (int y = 0; y < height; y++) {
	                    bi.setRGB(x, y, bitMatrix.get(x, y) ? BLACK : WHITE);
	                }
	            }
				Graphics2D g=bi.createGraphics();
				g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.getDeviceConfiguration().createCompatibleImage(width,height,Transparency.TRANSLUCENT);
				int widthLogo=logo.getWidth();
				int heightLogo=logo.getHeight();
				int x=(bi.getWidth()-widthLogo)>>1;
				int y=(bi.getHeight()-heightLogo)>>1;
				g.drawImage(logo, x, y, widthLogo, heightLogo, null); 
				g.setStroke(new BasicStroke(2)); // 画笔粗细
				g.setColor(Color.WHITE); // 边框颜色
				g.drawRect(x, y, widthLogo, heightLogo); // 矩形边框
				logo.flush();
				g.dispose();			
				ImageIO.write(bi, "png", out);			
			} catch (Exception e) {
				LoggerUtil.error(e.toString(), e);
			}						
	}
	public static void createQRCode(String content,OutputStream out) {		
		createQRCode(content, width, height, out);
	}
		
    public static void main(String[] args) throws Exception {
    	for(int i=0;i<1;i++) {
	        String path = getEncode("wxp://f2f19ueaymKVF0KIEejA8fO1X6VVEmjBj2bi");
	        System.out.println(path);
    	}    	    	
    }

}
