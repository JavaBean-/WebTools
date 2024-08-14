package com.boylegu.springboot_vue.controller;

import com.boylegu.springboot_vue.controller.pagination.PaginationFormatting;
import com.boylegu.springboot_vue.controller.pagination.PaginationMultiTypeValuesHelper;
import com.boylegu.springboot_vue.dao.PersonsRepository;
import com.boylegu.springboot_vue.entities.Persons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/persons")
public class MainController {

    @Autowired
    private PersonsRepository personsRepository;

    @Value(("${com.boylegu.paginatio.max-per-page}"))
    Integer maxPerPage;

    @RequestMapping(value = "/sex", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSexAll() {

        ArrayList<Map<String, String>> results = new ArrayList<>();

        for (Object value : personsRepository.findSex()) {

            Map<String, String> sex = new HashMap<>();

            sex.put("label", value.toString());

            sex.put("value", value.toString());

            results.add(sex);
        }

        ResponseEntity<ArrayList<Map<String, String>>> responseEntity = new ResponseEntity<>(results,
                HttpStatus.OK);

        return responseEntity;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, PaginationMultiTypeValuesHelper> getPersonsAll(
            @RequestParam(value = "page", required = false) Integer pages, @RequestParam("sex") String sex, @RequestParam("email") String email) {

        if (pages == null) {

            pages = 1;

        }

        Sort sort = new Sort(Direction.ASC, "id");

        Pageable pageable = new PageRequest(pages - 1, maxPerPage, sort);

        PaginationFormatting paginInstance = new PaginationFormatting();

        return paginInstance.filterQuery(sex, email, pageable);
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Persons> getUserDetail(@PathVariable Long id) {

        Persons user = personsRepository.findById(id).get();

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Persons updateUser(@PathVariable Long id, @RequestBody Persons data) {

        Persons user = personsRepository.findById(id).get();
        user.setPhone(data.getPhone());
        user.setZone(data.getZone());

        return personsRepository.save(user);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable Long id) {
        Persons user = personsRepository.findById(id).get();
        personsRepository.delete(user);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Persons saveUser(@RequestBody Persons data) {

        Persons user = personsRepository.findById(data.getId()).orElse(data);

        return personsRepository.save(user);
    }


}