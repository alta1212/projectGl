package com.hcl.admin.admin_management.service;

import com.hcl.admin.admin_management.model.bill;
import com.hcl.admin.admin_management.rep.billRep;
import com.mashape.unirest.http.*;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.*;

@Service
public class systemService {

  @Autowired
  billRep brep;

  private Path rootLocation;

  @Value("${mailgun.api-key}")
  private String apikey;
  @Value("${mailgun.domain}")
  private String domain;

  systemService() {

    this.rootLocation = Paths.get("src\\main\\resources\\upload");

  }

  public JsonNode sendmail(String mail, String subject, String containt) throws UnirestException {
    HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + domain + "/messages")
        .basicAuth("api", apikey).field("from", "alta@" + domain)
        .field("to", mail)
        .field("subject", subject)
        .field("text", containt)
        .asJson();
    return request.getBody();
  }

  public String uploadFile(MultipartFile file, String filename) {
    try {
      if (file.isEmpty()) {
        System.out.println("File is nulll");
      }

      try (InputStream inputStream = file.getInputStream()) {

        Files.copy(file.getInputStream(), this.rootLocation.resolve(filename),
            StandardCopyOption.REPLACE_EXISTING);
        System.out.println("ok");
        return "ok";
      }
    } catch (Exception e) {
      System.out.println(e);
      System.out.println("error");
    }
    return null;
  }

  public MultipartFile createReport(String date) {
    return null;
  }

  public ByteArrayInputStream tutorialsToCSV(List<bill> bills) {

    final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
      List<String> HEADERs = Arrays.asList(

          "Pro_name", "Left_Quantity", "Price", "SupplierID");
      csvPrinter.printRecord(HEADERs);

      for (bill bill : bills) {
        List<String> data = Arrays.asList(
          //TODO : get data to csv
            Integer.toString(bill.getBill_ID()));

        csvPrinter.printRecord(data);
      }

      csvPrinter.flush();
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
    }
  }

  public List<bill> getBillBydate(String date) {
    return brep.findBydateIgnoreCaseContaining(date);
  }

}