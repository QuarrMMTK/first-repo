package com.java.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.java.bean.User;
import com.java.bean.Webtoon;
import com.java.dao.UserDao;

@Controller
public class UserController {
	@Autowired
	UserDao dao;
	 
	@RequestMapping(value="/searchUser", method = RequestMethod.POST) 
	public String searchUser(@ModelAttribute("search") User search, Model m, RedirectAttributes reAtt) {	 String UserName=search.getUsername(); 
		 List<User> searchResults = dao.getUserByName(UserName);
		 if (!searchResults.isEmpty()) {
		        m.addAttribute("list", searchResults);
		        m.addAttribute("search", new User()); // Add this line to ensure "search" is in the model
		        return "viewuser";
		    } else {
		        reAtt.addFlashAttribute("resultFlag", 0);
		        reAtt.addFlashAttribute("message", "No Result Found!");
		        return "redirect:/viewuser";
		    }
	 }
	@ModelAttribute(value = "search")
	public User getSearch()
	{
		return new User();
	}
	@RequestMapping("/logout")
	public String logout(Model model, HttpSession session) {
	    if (session != null) {
	        // Invalidate the session
	        session.invalidate();
	    }
	    // Assuming "login" is the name of your login page
	    return "redirect:/login"; // Redirect to the login page after logout
	}
	
	
	@RequestMapping("/register")
	public String showform(Model m, HttpSession session){
		if(session != null && session.getAttribute("userId") != null) {
			int userId=(int)session.getAttribute("userId");
			int role_id = dao.getUserRoleById(userId);
			m.addAttribute("role_id", role_id);
			m.addAttribute("command", new User());
			return "register";
		}
		return "login";
	}


	@RequestMapping(value="/save",method = RequestMethod.POST)
	public String save(@ModelAttribute("user") User user){
	dao.save(user);
	return "redirect:/viewuser";
	}
	
	

	@RequestMapping("/viewuser")
	public String viewemp(Model m, HttpSession session){
		if(session != null && session.getAttribute("userId") != null) {
			int userId=(int)session.getAttribute("userId");
			int role_id = dao.getUserRoleById(userId);
			m.addAttribute("role_id", role_id);
//		int SessionId = session.getAttribute(session);
//		session.setAttribute("userId", userId);
		List<User> list=dao.getUsers();
		m.addAttribute("list",list);
		return "viewuser";
	}
	return "login";
	}
	@RequestMapping(value="/deleteuser/{id}",method = RequestMethod.GET)
	public String delete(@PathVariable int id){
	dao.delete(id);
	return "redirect:/viewuser";
	}
	@RequestMapping(value="/viewuser/{pageid}")
	public String edit1(@PathVariable int pageid,Model m){
	int total=5;
	if(pageid==1){}
	else{
	pageid=(pageid-1)*total+1;
	}
	System.out.println(pageid);
	List<User> list=dao.getUsersByPages(pageid,total);
	m.addAttribute("list", list);
	return "viewuser";
	}	
	
	
	@RequestMapping(value="signin" , method= RequestMethod.POST)
	public String loginUser(@Valid @ModelAttribute("inputUser") User user, Model m, HttpSession session) {
	    String uname = user.getUsername();
	    String upass = user.getPassword();
	    
	    
//	    Checking UserName And Password That Match With database If The Data Are Match Return True
	    if(dao.getUserData(user)==1) {
	    	int userId = dao.getUserIdByUsername(uname,upass); // Get User ID from username and Password Matching
//	    	String loginUser=dao.getUserById(userId);
	    	int role_id = dao.getUserRoleById(userId);
//	    	If The User Role is Author or User
	    	if(role_id == 2 || role_id == 1) {
	    		session.setAttribute("userId", userId);
		    	m.addAttribute("role_id", role_id);
		    	m.addAttribute("search" ,new Webtoon());
		    	m.addAttribute("webtoon", new Webtoon());
		    	return "redirect:/homepage";
	    	}
	    }
	    return "redirect:/login";
	}
}