package com.happyman.Ruby.customerSupport.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.happyman.Ruby.common.BaseController;
import com.happyman.Ruby.masterService.dao.Complaint;
import com.happyman.Ruby.masterService.dao.Feedback;

@Controller
@RequestMapping("/customerSupport")
public class CustomerSupportController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(CustomerSupportController.class);

	@GetMapping("/feedback")
	public String authenticate() {
		return "customerSupportSystem/feedback";
	}

	@GetMapping("/feedbackPage")
	public String authenticate1(Model model) {
		model.addAttribute("acceptedFeedbacks",
			masterService.getAllFeedbacks().stream().filter(Feedback::isActive).toList());
		return "customerSupportSystem/feedbackPage";
	}

	@PostMapping("/addFeedback")
	public String addFeedback(@ModelAttribute Feedback feedback) {
		masterService.addFeedback(feedback);
		return "redirect:/success";
	}

	@PostMapping("/deleteFeedback")
	public String deleteFeedback(Integer feedbackId, Model model) {
		masterService.deleteFeedbackById(feedbackId);
		model.addAttribute("feedbacks", masterService.getAllFeedbacks());
		return "common/admin_sidebar";
	}

	@PostMapping("/acceptFeedback")
	public String acceptFeedback(Integer feedbackId, Model model) {
		Feedback feedback = masterService.getFeedbackById(feedbackId);
		feedback.setActive(true);
		masterService.addFeedback(feedback);
		model.addAttribute("feedbacks", masterService.getAllFeedbacks());
		return "common/admin_sidebar";
	}

	@GetMapping("/giveComplaintForm")
	public String giveComplaint(String bookingId, Model model) {
		model.addAttribute("complaint", masterService.findComplaintByBookingId(bookingId));
		return "customerSupportSystem/complain";
	}

	@PostMapping("/saveComplaint")
	public String saveComplaint(@ModelAttribute Complaint complaint, String bookingId, Model model) {
		Complaint cmp = complaint.getComplaintId() == null ? complaint : masterService.findComplaintById(complaint.getComplaintId());
		cmp.setCustomerName(complaint.getCustomerName());
		cmp.setEmail(complaint.getEmail());
		cmp.setReservation(masterService.findReservationById(bookingId));
		cmp.setComplaintDesc(complaint.getComplaintDesc());

		masterService.saveComplaint(cmp);
		return "redirect:/success";
	}
}