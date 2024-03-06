package com.java.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.java.bean.Episode;
import com.java.bean.User;
import com.java.bean.Webtoon;
import com.java.dao.EpisodeDao;
import com.java.dao.UserDao;
import com.java.dao.WebtoonDao;
@Controller
public class EpisodeController {
	@Autowired
	EpisodeDao episodeDao;
	@Autowired
	WebtoonDao webtoonDao;
	@Autowired
	UserDao udao;
	
//	@RequestMapping(value = "/Upload-episode", method = RequestMethod.POST)
//	public String uploadEpisode(@ModelAttribute("Episode") Episode episode) {
//
//	    // FolderCreate();  // (Commented out) A method to create folders; it's currently commented out.
//
//	    // Extracting information from the Episode object
//	    String episodeName = episode.getEpisodeTitle();
//	    int webtoonId = episode.getWebtoon_id();
//	    
//	    // Retrieving the webtoon title based on the webtoonId using webtoonDao
//	    String webtoonName = webtoonDao.getWebtoonTitle(webtoonId);
//
//	    // Constructing the path for the episode's folder
//	    String path = "D:/Java/J2EE/WebtoonProject/src/main/webapp/Webtoons/";
//	    String fullPath = path + webtoonName + "/" + episodeName;
//
//	    // Creating the directory for the episode using File
//	    File file = new File(fullPath);
//	    file.mkdirs();
//
//	    // Saving the episode using episodeDao
//	    episodeDao.save(episode);
//
//	    // Redirecting to the "addepisode" endpoint
//	    return "redirect:/addepisode";
//	}


	
	@RequestMapping(value = "/Upload-episode", method = RequestMethod.POST)
	public String uploadEpisode(@ModelAttribute("Episode") Episode episode, @RequestParam("files") MultipartFile[] files) {

	    if (files.length == 0) {
	        return "redirect:/addepisode";
	    }

	    try {
	        String episodeName = episode.getEpisodeTitle();
	        int webtoonId = episode.getWebtoon_id();
	        String webtoonName = webtoonDao.getWebtoonTitle(webtoonId);

	        // Construct the full path
	        String fullPath = "D:/Java/J2EE/WebtoonProject/src/main/webapp/Webtoons/" + webtoonName + "/" + episodeName;

	        // Create the folder if it doesn't exist
	        File file = new File(fullPath);
	        file.mkdirs();
	        int totalImages = 0; // Initialize the total images count
	        // Specify the directory where you want to save the uploaded files
	        for (MultipartFile image : files) {
	            if (image.isEmpty()) {
	                continue; // Skip empty files
	            }

	            // Construct the file path for each image
	            String imagePath = fullPath + "/" + image.getOriginalFilename();
	            image.transferTo(new File(imagePath));
	            totalImages++; // Increment the total images count
	        }

	        // Set the file path in the Episode object
	        episode.setFile(fullPath);
	        episode.setTotalImages(totalImages);
	        // Save the Episode object to the database
	        episodeDao.save(episode);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return "redirect:/addepisode"; // Ensure that "addepisode" is a valid endpoint
	}


	@RequestMapping(value="/EpisodePage/{webtoonid}/{episode}")
	public String goEpisodePage(@PathVariable("webtoonid") int id,@PathVariable("episode") int epi,Model m, HttpSession session) {
		if(session != null && session.getAttribute("userId") != null) {
			int userId=(int)session.getAttribute("userId");
			int role_id = udao.getUserRoleById(userId);
			m.addAttribute("role_id", role_id);
			
			
			int episodeId=episodeDao.getEpisodeId(id,epi);
			String webtoonTitle = webtoonDao.getWebtoonTitleFromEpisodeId(episodeId);
			String episodeTitle = episodeDao.getEpisodeNameById(episodeId);
			List<Episode> episodes = episodeDao.getAllEpisodeById(id);
			int totalImg=episodeDao.getImgCount(episodeId);
			
			int epiCount=episodeDao.getCount(id);
			m.addAttribute("epiCount",epiCount);
			
			m.addAttribute("episodes",episodes);
			m.addAttribute("count",totalImg);
			m.addAttribute("webtoonTitle",webtoonTitle);
			m.addAttribute("episodeTitle",episodeTitle);
			
			m.addAttribute("webtoonid",id);
			m.addAttribute("currentEpisode",epi);
//			String path = episodes.getFile();
			return "EpisodePage";
		}
		return "login";

	}
}
