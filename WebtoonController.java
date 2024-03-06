package com.java.controller;

import java.io.*;
import java.io.File;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.java.bean.Episode;
import com.java.bean.User;
import com.java.bean.Webtoon;
import com.java.dao.EpisodeDao;
import com.java.dao.UserDao;
import com.java.dao.WebtoonDao;

@Controller
public class WebtoonController {
	@Autowired
	WebtoonDao dao;
	@Autowired
	UserDao udao;
	@Autowired
	EpisodeDao edao;

	@RequestMapping(value="/homepage/{name}", method = RequestMethod.GET)
    public String handleCategoryClick(@PathVariable("name") String name, Model m,HttpSession session) {
		if(session != null && session.getAttribute("userId") != null) {
			List<Webtoon> webtoons = dao.getGenres(name);
			int userId=(int)session.getAttribute("userId");
			int role_id = udao.getUserRoleById(userId);
			m.addAttribute("role_id", role_id);
			m.addAttribute("search" ,new Webtoon());
			m.addAttribute("webtoon" ,webtoons);
			return "homepage";
		}
		return "login";
	}
	@RequestMapping("/homepage")
	public String goHome(Model m, HttpSession session) {
		if(session != null && session.getAttribute("userId") != null) {
			List<Webtoon> webtoons = dao.getWebtoons();
			int userId=(int)session.getAttribute("userId");
			int role_id = udao.getUserRoleById(userId);
			m.addAttribute("role_id", role_id);
			m.addAttribute("search" ,new Webtoon());
			m.addAttribute("webtoon" ,webtoons);
			return "homepage";
		}
		return "login";
	}
	
	
	@RequestMapping("/Author")
	public String helloAuthor(Model m, HttpSession session) {
		if(session != null && session.getAttribute("userId") != null) {
			int userId=(int)session.getAttribute("userId");
			int role_id = udao.getUserRoleById(userId);
			m.addAttribute("role_id", role_id);
			m.addAttribute("webtoon", new Webtoon());
			return "Author";
		}
		return "login";
	}
	@RequestMapping("/package")
	public String goPackage() {
		return "package";
	}
	@RequestMapping("/webtoonList")
	public String goWebtoonList(Model m, HttpSession session) {
		if(session != null && session.getAttribute("userId") != null) {
			int userId=(int)session.getAttribute("userId");
			int role_id = udao.getUserRoleById(userId);
			m.addAttribute("role_id", role_id);
			List<Webtoon> webtoons = dao.getWebtoons();
			m.addAttribute("search" ,new Webtoon());
			m.addAttribute("webtoons" ,webtoons);
			return "webtoonList";
		}
		return "login";
	}
	@RequestMapping("/")
	public String goLogin(Model m) {
		return "login";
	}
	@RequestMapping("/login")
	public String returnLogin(Model m) {
		return "login";
	}
	@RequestMapping("/addepisode")
	public String goAddEpisode(Model m, HttpSession session) {
		if(session != null && session.getAttribute("userId") != null) {
			int userId=(int)session.getAttribute("userId");
			int role_id = udao.getUserRoleById(userId);
			m.addAttribute("role_id", role_id);
			List<Webtoon> webtoonList = dao.getWebtoons();
			m.addAttribute("webtoonList",webtoonList);
			
			m.addAttribute("Episode",new Episode());
			return "addepisode";
		}
		return "login";
	}
	@RequestMapping("/add-webtoon")
	public String goAddWebtoon(Model m, HttpSession session) {
		if(session != null && session.getAttribute("userId") != null) {
			int userId=(int)session.getAttribute("userId");
			int role_id = udao.getUserRoleById(userId);
			m.addAttribute("role_id", role_id);
			m.addAttribute("webtoon", new Webtoon());
			return "Author";
		}
		return "Login";
	}
	
	@RequestMapping(value="Upload", method = RequestMethod.POST)
	public String uploadWebtoon(@ModelAttribute("webtoon") Webtoon webtoon,@RequestParam("input-image") MultipartFile input) {
		webtoon.setInputImage(input);
		dao.save(webtoon);
//		FolderCreate();
		String webtoonName = webtoon.getWebtoonTitle();
		String path = "D:/Java/J2EE/WebtoonProject/src/main/webapp/Webtoons/";
		String fullPath = path + webtoonName;
		File file = new File(fullPath);
		file.mkdirs();
		return "redirect:/homepage";
	}
	@RequestMapping(value="/readWebtoon/{id}")
	public String read(@PathVariable("id") int id, Model m,HttpSession session){
		if(session != null && session.getAttribute("userId") != null) {
			int userId=(int)session.getAttribute("userId");
			int role_id = udao.getUserRoleById(userId);
			m.addAttribute("role_id", role_id);
			List<Webtoon> webtoon = dao.getWebtoonById(id);
			List<Episode> episodes = edao.getAllEpisodeById(id);
			m.addAttribute("webtoonid",id);
			m.addAttribute("episodes",episodes);
			m.addAttribute("webtoon", webtoon);
			return "readingpage";
		}
		return "Login";
	}
	@RequestMapping(value="/searchList",method = RequestMethod.POST) 
	 public String searchWebtoonList(Webtoon search, Model m,RedirectAttributes reAtt) {
		 String webtoonTitle=search.getWebtoonTitle(); 
		 List<Webtoon> searchResults = dao.getWebtoonByName(webtoonTitle);
		 if(!searchResults.isEmpty()) {
			 m.addAttribute("webtoons", searchResults);
			 m.addAttribute("search" ,new Webtoon());
			 return "webtoonList";
		 }else {
//			 m.addAttribute("error", "No result found");
//			 showErrorDialog("An error occurred!", "Error");
			 reAtt.addFlashAttribute("resultFlag",0);
			 reAtt.addFlashAttribute("message","No Result Found!");
			 return "redirect:/webtoonList";
		 }
	 }
	@RequestMapping(value="/search",method = RequestMethod.POST) 
	 public String searchWebtoon(Webtoon search, Model m,RedirectAttributes reAtt) {
		 String webtoonTitle=search.getWebtoonTitle(); 
		 List<Webtoon> searchResults = dao.getWebtoonByName(webtoonTitle);
		 if(!searchResults.isEmpty()) {
			 m.addAttribute("webtoon", searchResults);
			 m.addAttribute("search" ,new Webtoon());
			 return "homepage";
		 }else {
//			 m.addAttribute("error", "No result found");
//			 showErrorDialog("An error occurred!", "Error");
			 reAtt.addFlashAttribute("resultFlag",0);
			 reAtt.addFlashAttribute("message","No Result Found!");
			 return "redirect:/homepage";
		 }
	 }
	@RequestMapping(value="/viewDetail/{id}")
	public String viewDetail(@PathVariable("id") int id,Model m, HttpSession session) {
		if(session != null && session.getAttribute("userId") != null) {
			int userId=(int)session.getAttribute("userId");
			int role_id = udao.getUserRoleById(userId);
			m.addAttribute("role_id", role_id);
			List<Webtoon> webtoon = dao.getWebtoonById(id);
			List<Episode> episodes = edao.getAllEpisodeById(id);
			m.addAttribute("webtoonid",id);
			m.addAttribute("episodes",episodes);
			m.addAttribute("webtoon", webtoon);
			return "readingpage";
		}
		return "login";
	}
	@RequestMapping(value="/deleteEpisode/{webtoonid}/{episodeNumber}",method = RequestMethod.POST)
	public String delete(@PathVariable("webtoonid") int webtoonid,@PathVariable("episodeNumber") int episodeNumber,Model m){
	edao.delete(webtoonid,episodeNumber);
	return "redirect:/readingpage";
	}
	@RequestMapping(value="/deleteWebtoon/{id}",method = RequestMethod.POST)
	public String delete(@PathVariable int id){
		List<Webtoon> webtoons = dao.getWebtoonById(id);
		if (webtoons != null) {
			edao.deleteByWebtoonId(id);
			dao.delete(id);
			return "redirect:/webtoonList";
			}
		return null;
		}
	 
}