package com.jz.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		doPost(request,response);
	}

	@SuppressWarnings("deprecation")
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");  //Set Encoding method
        //Get disk file item factory 
        DiskFileItemFactory factory = new DiskFileItemFactory();  
        //Get destination path  
        String path = request.getRealPath("/upload");  
        File file=new File(path);
        if(!file.exists()){
        	file.mkdirs();
        }
        factory.setRepository(new File(path));  
        //Set buf size, if file is to large, use tmp space
        factory.setSizeThreshold(1024*1024) ;  
        //Upload API
        ServletFileUpload upload = new ServletFileUpload(factory);  
        try {  
            //Multiple file uploading supported  
            List<FileItem> list = (List<FileItem>)upload.parseRequest(request);  
            for(FileItem item : list){  
                //get field name
                String name = item.getFieldName();  
                if(item.isFormField()){                     
                    //get string
                    String value = item.getString() ;  
                    request.setAttribute(name, value);  
                }else{  
                    //get path name
                    String value = item.getName() ;  
                    //index to the end
                    int start = value.lastIndexOf("\\");  
  
                    String filename = value.substring(start+1);  
                    request.setAttribute(name, filename);  
                    //write file
                    item.write( new File(path,filename) );
                    System.out.println("Upload Succeed£º"+filename);
                    response.getWriter().print(filename);// return path to the client
                }  
            }  
              
        } catch (Exception e) {  
        	System.out.println("Upload Failed");
        	response.getWriter().print("Upload Failed£º"+e.getMessage());
        }  
		
	}


}
