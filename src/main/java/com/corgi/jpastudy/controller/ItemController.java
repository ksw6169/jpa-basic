package com.corgi.jpastudy.controller;

import com.corgi.jpastudy.domain.Book;
import com.corgi.jpastudy.domain.Item;
import com.corgi.jpastudy.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @RequestMapping(value = "/items/new", method = RequestMethod.GET)
    public String createForm() {
        return "/items/createItemForm";
    }

    @RequestMapping(value = "/items/new", method = RequestMethod.POST)
    public String create(Book item) {
        itemService.saveItem(item);
        return "redirect:/items";
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public ModelAndView list() {

        List<Item> items = itemService.findItems();

        ModelAndView mav = new ModelAndView();
        mav.addObject("items", items);
        mav.setViewName("/items/itemList");

        return mav;
    }

    @RequestMapping(value = "/items/{itemId}/edit", method = RequestMethod.GET)
    public ModelAndView updateItemForm(@PathVariable(value = "itemId") Long itemId) {

        Item item = itemService.findOne(itemId);

        ModelAndView mav = new ModelAndView();
        mav.addObject("item", item);
        mav.setViewName("/items/updateItemForm");

        return mav;
    }

    @RequestMapping(value = "/items/{itemId}/edit", method = RequestMethod.POST)
    public String update(@PathVariable(value = "itemId") String itemId,
                         Book item) {

        itemService.saveItem(item);
        return "redirect:/items";
    }
}
