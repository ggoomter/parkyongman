package com.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.board.util.UiUtils;

@Controller
public class MainController extends UiUtils{
	
	@GetMapping(value = "/")
	public String openBoardWrite( Model model) {
		System.out.println("메인컨트롤러 진입");
		return "main/index";
	}
	

}
