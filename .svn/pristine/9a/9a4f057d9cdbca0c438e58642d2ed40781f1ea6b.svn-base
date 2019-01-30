package com.contactsImprove.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import com.contactsImprove.entity.api.PaymentMode;


/**
 * 文件上传工具类
 * 
 */
public class UploadUtil {

	static String rootPayment="/upload/payment/";
	
	
	public static String uploadPaymentFile(HttpSession session,PaymentMode pm,String fileName){
		MultipartFile file=pm.getPaymentQR();
		String path = session.getServletContext().getRealPath("/");
		File targetFile = new File(path,fileName);
	    //保存    
        try {    
	        if(!targetFile.exists()){
	        	if(!targetFile.getParentFile().exists()) {
	        		targetFile.getParentFile().mkdirs();
	        	}
				targetFile.getParentFile().createNewFile();
	        }    
        	file.transferTo(targetFile);    
        } catch (Exception e) {    
            LoggerUtil.error("修改支付图片出错", e); 
        }   
        
        return fileName;
     
     }
	
	public static String uploadPaymentFile(HttpSession session,PaymentMode pm){
		MultipartFile file=pm.getPaymentQR();
		String resultPath =rootPayment+pm.getUserId()+"/"+pm.getPaymentType()+"/";
		//获得项目路径
		String path = session.getServletContext().getRealPath(resultPath);
		//把路径中的包括\\全部换成/
		path=path.replace("\\","/");
		String endWith=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS");
		String fileName=sdf.format(new Date())+endWith;
		resultPath=resultPath+fileName;

		File targetFile = new File(path,fileName);          

        if(!targetFile.getParentFile().exists()){    
            targetFile.getParentFile().mkdirs();    
        }
  
        //保存    
        try {    
        	file.transferTo(targetFile);    
        } catch (Exception e) {    
            e.printStackTrace();    
        }   
        
        return resultPath;
     
     }
	
	/**
	 * 单文件上传  ，  保存文件后返回文件路径。 
	 * 
	 * @param file
	 * @param session
	 * @param folderName
	 * */
	public static String uploadFile(MultipartFile file,HttpSession session,String folderName){
		//获得项目路径
		String path = session.getServletContext().getRealPath("/upload/"+folderName);
		//把路径中的包括\\全部换成/
		path = path.replace("\\","/");
		//System.out.println("文件地址"+path);
		//获取文件名
        String fileName = file.getOriginalFilename();
        String fName = "";  
        String suffix = "";  
        if(fileName.indexOf(".")>=0){  
            int  indexdot =  fileName.indexOf(".");  
            suffix =  fileName.substring(indexdot);  
            fName = fileName.substring(0,fileName.lastIndexOf("."));  
            //使用UUID为文件重新命名，保证文件名唯一性
            fName = "file" + "_"  +UUID.randomUUID().toString().replace("-", "");  
            fName =  fName  + suffix;  
        }  
        File targetFile = new File(path, fName);    
        String resultPath = "/"+folderName+"/"+fName;
        if(!targetFile.exists()){    
            targetFile.mkdirs();    
        }    
        //保存    
        try {    
        	file.transferTo(targetFile);    
        } catch (Exception e) {    
            e.printStackTrace();    
        }   
        
        return resultPath;
     
     }
	
	/**
	 * 多文件上传  ，  保存文件后返回文件路径。  以逗号分隔
	 * 
	 * @param files
	 * @param session
	 * @param folderName
	 * */
	public static String uploadFiles(List<MultipartFile> files,HttpSession session,String folderName,HttpServletRequest request){
		//获得项目路径
		String path = session.getServletContext().getRealPath("/images/upload/"+folderName);
		//String path = session.getServletContext().getContextPath()+"/images/upload/"+folderName;
		//把路径中的包括\\全部换成/
		path = path.replace("\\","/");
		System.out.println("文件地址"+path);
        List<String> mylist = new ArrayList<String>();
        //遍历
        for(MultipartFile item:files){
	        String fileName = item.getOriginalFilename();
	        String fName = "";  
	        String suffix = "";  
	        if(fileName.indexOf(".")>=0){  
	            int  indexdot =  fileName.indexOf(".");  
	            suffix =  fileName.substring(indexdot);  
	            fName = fileName.substring(0,fileName.lastIndexOf("."));  
	            fName = "file" + "_"  +UUID.randomUUID().toString().replace("-", "");  	//为文件重新命名
	            fName =  fName  + suffix;  
	        }  
	        String localhost = request.getContextPath();
	        File targetFile = new File(path, fName);
	        mylist.add("http://45.119.98.31:8080"+localhost+"/images/upload/"+folderName+"/"+fName);
	        if(!targetFile.exists()){    
	            targetFile.mkdirs();    
	        }    
	        //保存    
	        try {    
	        	item.transferTo(targetFile);    
	        } catch (Exception e) {    
	            e.printStackTrace();    
	        }   
        }
     if(mylist.size()==0) {
    	 return null;
     }else {
	     StringBuffer result = new StringBuffer();
	     boolean first = true;
	      for(String str:mylist) {
	    	  //第一个不拼接","
	    	  if(first) {
	    		  first = false;
	    	  }else {
	    		  result.append(",");
	    	  }
	    	  result.append(str);
	      }
			return result.toString();
	     }
     }
}
