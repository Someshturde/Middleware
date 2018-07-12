package com.niit.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.niit.dao.PostDao;
import com.niit.dao.UserDao;
import com.niit.model.ErrorClazz;
import com.niit.model.Post;
import com.niit.model.User;

@Controller
public class PostController {
	
	@Autowired
	private PostDao postDao;
	
	@Autowired
	private UserDao userDao;
	
	@RequestMapping(value="/addpost",method=RequestMethod.POST)
	public ResponseEntity<?> addpost(@RequestBody Post post,HttpSession session)
	{
		String email=(String) session.getAttribute("loginId");
		if(email==null)
		{
			ErrorClazz error=new ErrorClazz(4, "Unauthirized access");
			return new ResponseEntity<ErrorClazz>(error,HttpStatus.UNAUTHORIZED);
		}
		post.setPostedOn(new Date());
		User postedBy=userDao.getUser(email);
		post.setPostedBy(postedBy);
		try
		{
			postDao.addPost(post);
			return new ResponseEntity<Post>(post,HttpStatus.OK);
		}
		catch (Exception e) {
			ErrorClazz error=new ErrorClazz(2, "Unable to post some required fields are empty !!!");
			System.out.println(e.getMessage());
			return new ResponseEntity<ErrorClazz>(error,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/allpost", method=RequestMethod.GET)
	public ResponseEntity<?> getAllPost(HttpSession session)
	{
		String email=(String)session.getAttribute("loginId");
		if(email==null)
		{
			ErrorClazz error = new ErrorClazz(4, "Unauthorird access");
			return new ResponseEntity<ErrorClazz>(error,HttpStatus.UNAUTHORIZED);
		}
		List<Post>posts=postDao.getAllPost();
		return new ResponseEntity<List<Post>>(posts,HttpStatus.OK);
	}

}
