package com.jforce.springboot_voting_App.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.jforce.springboot_voting_App.dto.Admin;
import com.jforce.springboot_voting_App.dto.User;
import com.jforce.springboot_voting_App.dto.Vote;
import com.jforce.springboot_voting_App.dto.VoteCount;
import com.jforce.springboot_voting_App.repository.VoteRepository;
import com.jforce.springboot_voting_App.repository.Vote_Repository;
import com.jforce.springboot_voting_App.repository.Voting_Repository;
import com.jforce.springboot_voting_App.service.VotingImplementation;

import jakarta.servlet.http.HttpServletRequest;

@Controller

public class Voting_Controller {
	@Autowired
	private VotingImplementation service;
	@Autowired
	private Voting_Repository repository;
	private Vote_Repository repository1;

	@GetMapping("/signup")
	public String Form(Model model) {
		model.addAttribute("voting", new User());
		return "signup";
	}

	@PostMapping("/signup")
	public String votingSubmit(@ModelAttribute("voting") User user, Model model) {
		System.out.println("Data Inserted" + user);
		repository.save(user);
		System.out.println("Data Entered");
		model.addAttribute("voting", user);
		return "index";
	}

	@GetMapping("/login")
	public String showVotinglogin(Model model) {
		User user = new User();
		model.addAttribute("voting", user);
		return "index";
	}

	@PostMapping("/login")
	public ModelAndView validate(@ModelAttribute("voting") User user, Model model, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("index");
		System.out.println(user.getUsrername() + "\n" + user.getPassword());
				
		User userTwo = new User();
		userTwo = repository.findByUsrername(user.getUsrername());
		System.out.println(user.getUsrername());
		try {
		if (user.getUsrername().equals(userTwo.getUsrername()) && user.getPassword().equals(userTwo.getPassword())) {
//				model.addAttribute("userId", userTwo.getId());
			modelAndView.setViewName("voting");
//			modelAndView.addObject("userId", userTwo.getId());
			request.getSession().setAttribute("user", userTwo);
			return modelAndView;
		}
		} catch (Exception e) {
			// TODO: handle exception
			modelAndView.setViewName("exception");

			return modelAndView;
		}
	
		return modelAndView;

	}
	
	@GetMapping("/adminlogin")
	public String adminlogin1(Model model) {
		model.addAttribute("voteCounts", new Admin());
		return "admin";
	}
	
	@Autowired
    private VoteRepository voteRepository;
    
    @GetMapping("/voteCount")
    public String voteCounts(Model model) {
    	VoteCount votecounts = new VoteCount();
        model.addAttribute("voteCounts",votecounts );
        return "votecount";
    }
	@PostMapping("/adminlogin")
	public ModelAndView adminlogin(Model model)
	{
		ModelAndView modelAndView = new ModelAndView("admin");
		Admin admin = new Admin();
    	List<VoteCount> votecounts = voteRepository.getVoteCounts();

		if (admin.getUsername().equals("Admin") && admin.getPassword().equals("Admin")) {
			model.addAttribute("voteCounts",votecounts);
			model.addAllAttributes(votecounts);
			modelAndView.setViewName("votecount");
			return modelAndView;
		}
		return modelAndView;


	}

	@Autowired
//		private  Vote_Repository repository2;

	public Voting_Controller(Voting_Repository repository, Vote_Repository repository1) {
		super();
		this.repository = repository;
		this.repository1 = repository1;
	}

	@GetMapping("/letsvote")
	public String handleVote(Model model) {
		Vote vote = new Vote();
		model.addAttribute("vote", vote);
		return "voting";
	}


	@PostMapping("/letsvote")
	public String handleVote(@ModelAttribute("voting") Vote vote, Model model, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		System.out.println(user);
		vote.setUserId(user.getId());
		Optional<Vote> voter = repository1.findById(vote.getUserId());
		if (voter.isEmpty()) {
			System.out.println("Data Inserted" + vote);
			repository1.save(vote);
			System.out.println("Data Entered");
			model.addAttribute("voting", vote);
			return "votedone";
		}
		model.addAttribute("voting", user);
		return "alreadyvoted";

	}

	public Voting_Controller() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VotingImplementation getService() {
		return service;
	}

	public void setService(VotingImplementation service) {
		this.service = service;
	}

	public Voting_Repository getRepository() {
		return repository;
	}

	public void setRepository(Voting_Repository repository) {
		this.repository = repository;
	}

	public Vote_Repository getRepository1() {
		return repository1;
	}

	public void setRepository1(Vote_Repository repository1) {
		this.repository1 = repository1;
	}
	
	
	
	

}
