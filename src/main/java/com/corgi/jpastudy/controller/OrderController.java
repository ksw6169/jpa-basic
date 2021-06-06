package com.corgi.jpastudy.controller;

import com.corgi.jpastudy.domain.Item;
import com.corgi.jpastudy.domain.Member;
import com.corgi.jpastudy.service.ItemService;
import com.corgi.jpastudy.service.MemberService;
import com.corgi.jpastudy.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final MemberService memberService;
    private final ItemService itemService;
    private final OrderService orderService;

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public ModelAndView createForm() {

        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        ModelAndView mav = new ModelAndView();
        mav.addObject("members", members);
        mav.addObject("items", items);
        mav.setViewName("/order/orderForm");

        return mav;
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public ResponseEntity order(@RequestParam(value = "memberId") Long memberId,
                                @RequestParam(value = "itemId") Long itemId,
                                @RequestParam(value = "count") int count) {

        orderService.order(memberId, itemId, count);
        return new ResponseEntity(HttpStatus.OK);
    }
}
