package com.near.ProjetoNear.controller;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.near.ProjetoNear.exceptions.ServiceExc;
import com.near.ProjetoNear.models.Loja;
import com.near.ProjetoNear.repository.LojaRepositorio;
import com.near.ProjetoNear.service.ServiceLoja;
import com.near.ProjetoNear.util.Util;

@Controller
public class LojaController {
	
	@Autowired
	private LojaRepositorio lojaRepositorio;
	
	@Autowired
	private ServiceLoja serviceLoja;
	
	
	@RequestMapping("/")
	public ModelAndView home () {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("busca/home");
		return mv;
	}
	
	
	//Redirecionamento para a pagina de cadastro
	@GetMapping ("/cadastro")
	public ModelAndView cadastroLoja (Loja cadastro) {
		ModelAndView mv = new ModelAndView("cadastro/cadastro");
		mv.addObject("cadastro", cadastro);
		return mv;
	}
	
	//Envio do dados digitados para o banco de dados
	@PostMapping("/cadastro/confirmar")
	
	public ModelAndView salvar (Loja cadastro, BindingResult result) throws Exception {
		if (result.hasErrors()) {
			return cadastroLoja(cadastro);
		}
		ModelAndView mv = new ModelAndView();
		serviceLoja.salvarLoja(cadastro);
		mv.setViewName("redirect:/login");
		return mv;

	}
	
	@GetMapping ("/login")
	public ModelAndView login (Loja login) {
		ModelAndView mv = new ModelAndView("cadastro/login");
		mv.addObject("login", login);
		return mv;
	}
	
	@PostMapping ("/login/confirmar")
	public ModelAndView confirmarLogin (@Valid Loja login, BindingResult br, HttpSession session) throws NoSuchAlgorithmException, ServiceExc {
		 
		ModelAndView mv = new ModelAndView();
		mv.addObject("login" , new Loja());
		if(br.hasErrors()) {
			mv.setViewName("/login");
		}
		
		Loja loginLoja = serviceLoja.loginLoja(login.getEmail(), Util.md5(login.getSenha()));
		if(loginLoja == null) {
			mv.addObject("msg", "Email ou Senha não encontrados. Tente Novamente");
		} else {
			session.setAttribute("lojaLogada", loginLoja);
			return home();
		}
		
		return mv;
		
	}
}
