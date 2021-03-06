package com.hcl.productManagement.service;

import com.hcl.productManagement.rep.*;
import com.hcl.productManagement.model.*;

import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class productService {
    @Autowired
    productRep rep;

    @Autowired
    priceRep priceRep;
    @Autowired
    imageRep imageRep;
    public List<product> getAllproduct()
    {
        List<product> products =new ArrayList<product>();
        //TO-get product detail
       for (String[] a : rep.getAllProduct()) {
           product p=new product();
           p.setID_pro(Integer.parseInt(a[0]));
           p.setPro_name(a[1]);
           p.setPrice(Integer.parseInt(a[2]));
           p.setImage(a[3]);
           p.setLeft_Quantity(Integer.parseInt(a[4]));

           products.add(p);
       } 
        return products;
    }

    public product getProductDetail(int id) {
        String [] a=rep.getAllProduct().get(0);
        product p=new product();
            p.setID_pro(Integer.parseInt(a[0]));
            p.setPro_name(a[1]);
            p.setPrice(Integer.parseInt(a[2]));
            p.setImage(a[3]);
            p.setLeft_Quantity(Integer.parseInt(a[4]));
        return p;

    }

    public List<product> getProductByCategpry(Integer id) {
        //TODO-get product by category

        return null;
    }
    public void create(product p)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
        LocalDateTime now = LocalDateTime.now();  
        System.out.println(dtf.format(now));  
        p=rep.save(p);
        System.out.println(p.getImage());
        price pr=new price();
        pr.setProductID_pro(p.getID_pro());
        pr.setEnd_Date("8888-03-16");
        pr.setPrice_bf_dis(p.getPrice());
        pr.setStart_date(dtf.format(now));
        priceRep.save(pr);
        String [] images=p.getImage().split(",");
        for(int i=0;i<images.length;i++)
        {
            image ima=new image();
            ima.setProductID_pro(p.getID_pro());
            ima.setPath(images[i]);
            imageRep.save(ima);
        }
    }
    
}
