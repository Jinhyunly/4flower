package com.example.demo.controller;

import java.io.File;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.domain.user.UserPrincipal;
import com.example.demo.entity.file.Files;
import com.example.demo.service.file.FilesService;


@Controller
public class GalleryController {

	@Autowired
  private FilesService fileService;

	@RequestMapping(value = "/gallery")
	public ModelAndView gallery() {

		ModelAndView modelAndView = new ModelAndView();

		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
			System.out.println(userPrincipal.toString());

			//modelAndView.addObject("userName", "Welcome " + userPrincipal.getName() + " (" + userPrincipal.getId() + ")");
			modelAndView.addObject("userName", userPrincipal.getName());

			String tes2 = userPrincipal.getId();
			//관리자 id를 일단 momo라고 했다
			if("momo".equals(userPrincipal.getId())){
				modelAndView.addObject("userId", userPrincipal.getId());
			}

			modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");



		}catch(Exception e) {

		}

		List<Files> fileList =  fileService.selectAll();

		modelAndView.addObject("fileList", fileList);
    modelAndView.setViewName("gallery");

		System.out.println("gallery controller start");
		return modelAndView;
	}

	@GetMapping("/gallery/{id}")
	public ModelAndView galleryInsert(@PathVariable("id") Long id, HttpServletRequest request,Principal principal) throws Exception{
		Files file = fileService.getFileById(id);
		ModelAndView modelAndView = new ModelAndView();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try{
			UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
			System.out.println(userPrincipal.toString());

			if("momo".equals(userPrincipal.getId())){
				modelAndView.addObject("userId", userPrincipal.getId());
			}

		}catch(Exception e){

		}

		modelAndView.addObject("file", file);
		modelAndView.setViewName("galleryInfo");

		return modelAndView;

	}
	//https://stackoverflow.com/questions/24256051/delete-or-put-methods-in-thymeleaf
	//@DeleteMapping("/gallery/{id}")
//	@RequestMapping(value="/gallery/{id}", method=RequestMethod.DELETE)
//	public String galleryDelete(HttpServletRequest request) throws Exception{
//
//		String abt = "";
//
//		return abt;
//	}
//
//	@PutMapping("/gallery/{id}")
//	public String galleryPut(HttpServletRequest request) throws Exception{
//
//		String abt = "";
//
//		return abt;
//
//	}

	@PostMapping("/gallery/{id}")
	public ModelAndView galleryPost(@PathVariable("id") Long id, HttpServletRequest request,Principal principal) throws Exception{
		String method = request.getParameter("method");

		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if("delete".equals(method)) {

			Files fileObj = new Files();

			Files file = fileService.getFileById(id);

			String sourceFileName = file.getGallery_fileName();
//			String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName).toLowerCase();

			String path = new ClassPathResource("/static/gallery").getFile().getAbsolutePath();
			String fileUrl = path +"\\";

			File destinationFile = new File(fileUrl + sourceFileName);

			try {

				destinationFile.delete();

				UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
				System.out.println(userPrincipal.toString());

				if("momo".equals(userPrincipal.getId())){
					modelAndView.addObject("userId", userPrincipal.getId());
				}

			}catch(Exception ex) {

			}


			fileService.deleteFileById(id);
		}

		List<Files> fileList =  fileService.selectAll();


		modelAndView.addObject("fileList", fileList);
    modelAndView.setViewName("gallery");

		return modelAndView;

	}


	@ResponseBody
	@RequestMapping(value="/gallery/fileinsert")
	public ModelAndView galleryFileinsert(HttpServletRequest request, @RequestParam(name = "file") MultipartFile file) throws Exception {

		String galleryTitle = request.getParameter("galleryTitle");

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
		System.out.println(userPrincipal.toString());



		Files fileObj = new Files();

		String sourceFileName = file.getOriginalFilename();
		String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName).toLowerCase();
		File destinationFile;
		String destinationFileName;

		//C:\dev\eclipse\git\4flower_git\4flower_git\4flowerSite\bin\main\static\gallery
		String path = new ClassPathResource("/static/gallery").getFile().getAbsolutePath();
		String fileUrl = path +"\\";

		do {
    			destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + sourceFileNameExtension;
    			destinationFile = new File(fileUrl + destinationFileName);
		} while (destinationFile.exists());

		destinationFile.getParentFile().mkdirs();
		file.transferTo(destinationFile);

		fileObj.setGallery_fileName(destinationFileName);
		fileObj.setGallery_fileOriName(sourceFileName);
		//fileObj.setGallery_url(fileUrl);
		fileObj.setGallery_url("/static/gallery");
		fileObj.setGallery_title(galleryTitle);
		fileObj.setEnt_kbn("1");

		fileService.saveFiles(fileObj);

		List<Files> fileList =  fileService.selectAll();

		ModelAndView modelAndView = new ModelAndView();

		if("momo".equals(userPrincipal.getId())){
			modelAndView.addObject("userId", userPrincipal.getId());
		}

		modelAndView.addObject("fileList", fileList);
    modelAndView.setViewName("gallery");

		return modelAndView;
//		String abt = "";
//
//		return abt;

	}

}
