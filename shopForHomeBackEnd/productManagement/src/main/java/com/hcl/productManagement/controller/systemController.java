package com.hcl.productManagement.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hcl.productManagement.config.JwtTokenUtil;
import com.hcl.productManagement.model.product;
import com.hcl.productManagement.model.wishlist;
import com.hcl.productManagement.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/system/")
public class systemController {
    @Autowired
    systemService sys;
    @Autowired
	private JwtTokenUtil jwtTokenUtil;
    @GetMapping("search")
    public List<product> productDetail(@RequestParam("key")  String key) {
        return sys.SearchProduct(key);
    }

    @PostMapping("auth/wishList")
    public void addWishList(HttpServletRequest request,@RequestParam("productId") int id) {
        String token = request.getHeader("authorization");
        sys.addWishList(jwtTokenUtil.getIdFromToken(token.substring(7)),id);
    }
}
