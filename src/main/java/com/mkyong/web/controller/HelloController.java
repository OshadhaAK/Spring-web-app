package com.mkyong.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.sql.DataSource;
import java.util.ArrayList;


@Controller
public class HelloController {


	ArrayList<Pet> result = new ArrayList();
	RestTemplate restTemplate = new RestTemplate();

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {

		model.addAttribute("message", new Pet());
		return "hello";

	}

	@RequestMapping(value = "/hello/{name:.+}", method = RequestMethod.GET)
	public ModelAndView hello(@PathVariable("name") String name) {

		ModelAndView model = new ModelAndView();
		model.setViewName("hello");
		model.addObject("msg", name);

		return model;

	}

	@RequestMapping(value = "/result", method = RequestMethod.POST)
	public String formSubmit(@ModelAttribute("message") Pet pet, Model model) {

		final String uri = "http://localhost:8081/pets/";

		Pet obj = new Pet();
		obj.setName(pet.getName());
		obj.setSpecies(pet.getSpecies());
		obj.setBreed(pet.getBreed());

		Object addedpet = restTemplate.postForObject( uri, obj, Object.class);

		Object pet1 = restTemplate.getForObject("http://localhost:8081/pets/", Object.class);


		model.addAttribute("message", pet1);
		return "result";
	}

	@RequestMapping(value = "/result", method = RequestMethod.GET)
	public String viewResultsPage(Model model) {
		Object pets = restTemplate.getForObject("http://localhost:8081/pets/", Object.class);
		model.addAttribute("message", pets);
		return "result";
	}

	@RequestMapping(value = "/delete-pet", method = RequestMethod.GET)
	public String deletePet(Model model, @RequestParam ("id") String id) {

		final String uri = "http://localhost:8081/pets/" + id;
		restTemplate.delete(uri);

		Object pets = restTemplate.getForObject("http://localhost:8081/pets/", Object.class);
		model.addAttribute("message", pets);
		return "result";
	}

}